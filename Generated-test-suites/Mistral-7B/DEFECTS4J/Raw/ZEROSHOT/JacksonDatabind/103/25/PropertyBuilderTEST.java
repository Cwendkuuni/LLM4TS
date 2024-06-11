package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PropertyBuilderTest {

    private SerializationConfig _config;
    private BeanDescription _beanDesc;
    private AnnotationIntrospector _annotationIntrospector;
    private Object _defaultBean;

    @Before
    public void setUp() {
        _config = mock(SerializationConfig.class);
        _beanDesc = mock(BeanDescription.class);
        _annotationIntrospector = mock(AnnotationIntrospector.class);

        when(_config.getDefaultPropertyInclusion(Mockito.any(Class.class), Mockito.eq(JsonInclude.Value.empty()))).thenReturn(JsonInclude.Value.empty());
        when(_config.getDefaultPropertyInclusion()).thenReturn(JsonInclude.Value.empty());
        when(_config.getAnnotationIntrospector()).thenReturn(_annotationIntrospector);
    }

    @Test
    public void testPropertyBuilderConstruction() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        Assert.assertNotNull(propertyBuilder);
        Assert.assertEquals(_config, propertyBuilder._config);
        Assert.assertEquals(_beanDesc, propertyBuilder._beanDesc);
        Assert.assertEquals(_annotationIntrospector, propertyBuilder._annotationIntrospector);
    }

    @Test
    public void testGetClassAnnotations() throws Exception {
        when(_beanDesc.getClassAnnotations()).thenReturn(new Annotations() {});
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        Assert.assertEquals(new Annotations(), propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testBuildWriter_NullSerializer() throws Exception {
        Object ser = null;
        when(_beanDesc.findPropertyDefinition("testProperty")).thenReturn(null);
        when(_config.findRootValueSerializer(Mockito.any(Class.class), Mockito.eq(null))).thenReturn(null);

        try {
            PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
            propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), ser, Mockito.mock(TypeSerializer.class), Mockito.mock(TypeSerializer.class), Mockito.mock(AnnotatedMember.class), true);
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("Property 'testProperty' is not defined on class [class com.test.TestClass]", e.getMessage());
        }
    }

    @Test
    public void testBuildWriter_NoTypeSerializer() throws Exception {
        Object typeSer = null;
        Object contentTypeSer = null;
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        when(propDef.getName()).thenReturn("testProperty");
        when(_beanDesc.findPropertyDefinition("testProperty")).thenReturn(propDef);
        when(_config.findRootValueSerializer(Mockito.any(Class.class), Mockito.eq(null))).thenReturn(null);

        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, Mockito.mock(JavaType.class), null, typeSer, contentTypeSer, Mockito.mock(AnnotatedMember.class), true);

        Assert.assertNotNull(beanPropertyWriter);
        Assert.assertFalse(beanPropertyWriter.hasTypeSerializer());
    }

    @Test
    public void testBuildWriter_TypeSerializer() throws Exception {
        Object typeSer = mock(TypeSerializer.class);
        Object contentTypeSer = null;
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        when(propDef.getName()).thenReturn("testProperty");
        when(_beanDesc.findPropertyDefinition("testProperty")).thenReturn(propDef);
        when(_config.findRootValueSerializer(Mockito.any(Class.class), Mockito.eq(null))).thenReturn(null);

        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, Mockito.mock(JavaType.class), null, typeSer, contentTypeSer, Mockito.mock(AnnotatedMember.class), true);

        Assert.assertNotNull(beanPropertyWriter);
        Assert.assertTrue(beanPropertyWriter.hasTypeSerializer());
        Assert.assertEquals(typeSer, beanPropertyWriter.getTypeSerializer());
    }

    @Test
    public void testBuildWriter_ContentTypeSerializer() throws Exception {
        Object typeSer = null;
        Object contentTypeSer = mock(TypeSerializer.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        when(propDef.getName()).thenReturn("testProperty");
        when(_beanDesc.findPropertyDefinition("testProperty")).thenReturn(propDef);
        when(_config.findRootValueSerializer(Mockito.any(Class.class), Mockito.eq(null))).thenReturn(null);

        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, Mockito.mock(JavaType.class), null, typeSer, contentTypeSer, Mockito.mock(AnnotatedMember.class), true);

        Assert.assertNotNull(beanPropertyWriter);
        Assert.assertTrue(beanPropertyWriter.hasContentTypeSerializer());
        Assert.assertEquals(contentTypeSer, beanPropertyWriter.getContentTypeSerializer());
    }

    @Test
    public void testBuildWriter_SuppressionValues() throws Exception {
        Object valueToSuppress = mock(Object.class);
        boolean suppressNulls = true;
        boolean useDefaults = false;
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        when(propDef.getName()).thenReturn("testProperty");
        when(_beanDesc.findPropertyDefinition("testProperty")).thenReturn(propDef);
        when(_config.getDefaultPropertyInclusion(Mockito.any(Class.class), Mockito.eq(Mockito.mock(JavaType.class))))
                .thenReturn(JsonInclude.Value.builder().withValueInclusion(JsonInclude.Include.NON_NULL).build());
        when(_config.getDefaultPropertyInclusion())
                .thenReturn(JsonInclude.Value.builder().withValueInclusion(JsonInclude.Include.NON_NULL).build());

        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, Mockito.mock(JavaType.class), null, null, null, Mockito.mock(AnnotatedMember.class), true);

        Assert.assertFalse(beanPropertyWriter.suppressNulls());
        Assert.assertNull(beanPropertyWriter.getValueToSuppress());

        propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        beanPropertyWriter = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, Mockito.mock(JavaType.class), null, null, null, Mockito.mock(AnnotatedMember.class), false);

        Assert.assertTrue(beanPropertyWriter.suppressNulls());
        Assert.assertNull(beanPropertyWriter.getValueToSuppress());

        propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        beanPropertyWriter = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, Mockito.mock(JavaType.class), null, null, null, Mockito.mock(AnnotatedMember.class), true);
        beanPropertyWriter.setValueToSuppress(valueToSuppress);

        Assert.assertTrue(beanPropertyWriter.suppressNulls());
        Assert.assertEquals(valueToSuppress, beanPropertyWriter.getValueToSuppress());

        propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        beanPropertyWriter = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, Mockito.mock(JavaType.class), null, null, null, Mockito.mock(AnnotatedMember.class), true);
        beanPropertyWriter.setSuppressNulls(false);

        Assert.assertFalse(beanPropertyWriter.suppressNulls());
        Assert.assertNull(beanPropertyWriter.getValueToSuppress());

        propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        beanPropertyWriter = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class), propDef, Mockito.mock(JavaType.class), null, null, null, Mockito.mock(AnnotatedMember.class), true);
        beanPropertyWriter.setValueToSuppress(valueToSuppress);
        beanPropertyWriter.setSuppressNulls(false);

        Assert.assertFalse(beanPropertyWriter.suppressNulls());
        Assert.assertEquals(valueToSuppress, beanPropertyWriter.getValueToSuppress());
    }
}