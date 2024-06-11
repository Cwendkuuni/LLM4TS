package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        config = mock(SerializationConfig.class);
        beanDesc = mock(BeanDescription.class);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = mock(Annotations.class);
        when(beanDesc.getClassAnnotations()).thenReturn(annotations);

        Annotations result = propertyBuilder.getClassAnnotations();

        assertNotNull(result);
        assertEquals(annotations, result);
    }

    @Test
    public void testBuildWriter() {
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter result = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertNotNull(result);
        assertEquals(propDef, result.getDefinition());
        assertEquals(am, result.getMember());
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Annotated a = mock(Annotated.class);
        boolean useStaticTyping = true;
        JavaType declaredType = mock(JavaType.class);

        JavaType result = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);

        assertNotNull(result);
        assertEquals(declaredType, result);
    }

    @Test
    public void testGetPropertyDefaultValueWithDefaultBean() throws Exception {
        String name = "name";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object defaultBean = new Object();

        when(beanDesc.instantiateBean(config.canOverrideAccessModifiers())).thenReturn(defaultBean);
        when(member.getValue(defaultBean)).thenReturn("defaultValue");

        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);

        assertNotNull(result);
        assertEquals("defaultValue", result);
    }

    @Test
    public void testGetPropertyDefaultValueWithoutDefaultBean() throws Exception {
        String name = "name";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object defaultBean = null;

        when(beanDesc.instantiateBean(config.canOverrideAccessModifiers())).thenReturn(defaultBean);
        when(member.getValue(defaultBean)).thenThrow(new Exception());

        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);

        assertNotNull(result);
        assertEquals(null, result);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = mock(JavaType.class);
        Class<?> cls = String.class;
        when(type.getRawClass()).thenReturn(cls);

        Object result = propertyBuilder.getDefaultValue(type);

        assertNotNull(result);
        assertEquals("", result);
    }
}