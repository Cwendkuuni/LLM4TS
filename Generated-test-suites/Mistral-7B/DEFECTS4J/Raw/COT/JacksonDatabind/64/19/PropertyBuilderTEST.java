package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;

    @Before
    public void setUp() {
        config = mock(SerializationConfig.class);
        beanDesc = mock(BeanDescription.class);
        annotationIntrospector = mock(AnnotationIntrospector.class);
    }

    @Test
    public void testPropertyBuilderConstructor() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertEquals(config, propertyBuilder._config);
        assertEquals(beanDesc, propertyBuilder._beanDesc);
        assertEquals(annotationIntrospector, propertyBuilder._annotationIntrospector);
    }

    @Test
    public void testGetClassAnnotations() {
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertEquals(new Annotations(), propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testBuildWriter_NullArguments() {
        try {
            new PropertyBuilder(null, beanDesc).buildWriter(null, null, null, null, null, null, null, false);
            assert false; // should not reach here
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testBuildWriter_NormalCase() throws JsonMappingException {
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        when(beanDesc.findPropertyInclusion(JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.builder().build());
        when(config.getDefaultPropertyInclusion(beanDesc.getBeanClass(), JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.builder().build());

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        // verify serializationType is null by default
        assertNull(bpw.getSerializationType());

        // verify contentTypeSer is not set by default
        assertNull(bpw.getContentTypeSerializer());

        // verify suppressNulls is false by default
        assertFalse(bpw.isSuppressNulls());

        // verify valueToSuppress is null by default
        assertNull(bpw.getValueToSuppress());
    }

    @Test
    public void testBuildWriter_WithContentTypeSer() throws JsonMappingException {
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.type(int[].class, String.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        when(beanDesc.findPropertyInclusion(JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.builder().build());
        when(config.getDefaultPropertyInclusion(beanDesc.getBeanClass(), JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.builder().build());

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType serializationType = TypeFactory.type(int[].class, String.class);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertEquals(serializationType, bpw.getSerializationType());
        assertEquals(contentTypeSer, bpw.getContentTypeSerializer());
        assertFalse(bpw.isSuppressNulls());
        assertNull(bpw.getValueToSuppress());
    }

    @Test
    public void testBuildWriter_WithNullSerializer() throws JsonMappingException {
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        when(beanDesc.findPropertyInclusion(JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.builder().build());
        when(config.getDefaultPropertyInclusion(beanDesc.getBeanClass(), JsonInclude.Value.empty())).thenReturn(JsonInclude.Value.builder().build());

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        try {
            propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
            assert false; // should not reach here
        } catch (JsonMappingException e) {
            assertEquals("Null serializer instance", e.getMessage());
        }
    }

    @Test
    public void testFindSerializationType_WithAnnotations() {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.type(int[].class, String.class);

        when(a.getAnnotations()).thenReturn(new Annotations());
        when(annotationIntrospector.refineSerializationType(config, a, declaredType)).thenReturn(TypeFactory.type(String.class));

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType result = propertyBuilder.findSerializationType(a, true, declaredType);
        assertEquals(TypeFactory.type(String.class), result);
    }

    @Test
    public void testFindSerializationType_WithoutAnnotations() {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.type(int[].class, String.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNull(propertyBuilder.findSerializationType(a, true, declaredType));
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String name = "name";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = TypeFactory.type(String.class);
        Object defaultBean = new Object();

        when(member.getValue(defaultBean)).thenReturn("defaultValue");

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String result = (String) propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals("defaultValue", result);
    }

    @Test
    public void testGetPropertyDefaultValue_NullDefaultBean() {
        String name = "name";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = TypeFactory.type(String.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNull(propertyBuilder.getPropertyDefaultValue(name, member, type));
    }

    @Test
    public void testGetPropertyDefaultValue_NonDefaultBeanWithEmptyString() {
        String name = "name";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = TypeFactory.type(String.class);
        Object defaultBean = new Object();

        when(member.getValue(defaultBean)).thenReturn("");

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String result = (String) propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertEquals(JsonInclude.Include.NON_EMPTY.toString(), result);
    }
}