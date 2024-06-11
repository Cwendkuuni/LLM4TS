package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
        beanDesc = mapper.introspect(String[].class);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() throws Exception {
        AnnotatedMember am = null;
        Class<?> clazz = String[].class;
        JavaType declaredType = TypeFactory.defaultInstance().constructType(clazz);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNotNull(writer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriterWithIllegalArgument() throws Exception {
        AnnotatedMember am = null;
        Class<?> clazz = String[].class;
        JavaType declaredType = TypeFactory.defaultInstance().constructType(clazz);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, null, ser, typeSer, contentTypeSer, am, false);
    }

    @Test
    public void testFindSerializationType() throws Exception {
        Annotated a = mock(Annotated.class);
        BeanDescription beanDesc = mock(BeanDescription.class);
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.getType()).thenReturn(String[].class);
        when(beanDesc.findPropertyInclusion(any())).thenReturn(JsonInclude.Value.STANDARD_EXCLUDE);
        when(beanDesc.findPropertySerializationType(any())).thenReturn(String.class);
        when(beanDesc.findPropertySerializationType(any(), anyBoolean())).thenReturn(Integer.TYPE);

        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType expectedType = TypeFactory.defaultInstance().constructType(Integer.TYPE);

        JavaType resultType = propertyBuilder.findSerializationType(a, false, declaredType);
        assertEquals(expectedType, resultType);
    }

    @Test
    public void testFindSerializationTypeWithAnnotation() throws Exception {
        Annotated a = mock(Annotated.class);
        BeanDescription beanDesc = mock(BeanDescription.class);
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.getType()).thenReturn(String[].class);
        when(beanDesc.findPropertyInclusion(any())).thenReturn(JsonInclude.Value.STANDARD_INCLUDE);
        when(beanDesc.findPropertySerializationType(any())).thenReturn(String.class);
        when(beanDesc.findPropertySerializationType(any(), anyBoolean())).thenReturn(Integer.TYPE);
        when(a.getAnnotations()).thenReturn(new Annotation[] {new JsonSerialize() {}});

        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType expectedType = TypeFactory.defaultInstance().constructType(Integer.TYPE);

        JavaType resultType = propertyBuilder.findSerializationType(a, false, declaredType);
        assertEquals(expectedType, resultType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeWithIllegalAnnotation() throws Exception {
        Annotated a = mock(Annotated.class);
        BeanDescription beanDesc = mock(BeanDescription.class);
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.getType()).thenReturn(String[].class);
        when(beanDesc.findPropertyInclusion(any())).thenReturn(JsonInclude.Value.STANDARD_EXCLUDE);
        when(beanDesc.findPropertySerializationType(any())).thenReturn(String.class);
        when(beanDesc.findPropertySerializationType(any(), anyBoolean())).thenReturn(Integer.TYPE);
        when(a.getAnnotations()).thenReturn(new Annotation[] {new Object() {}});

        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType expectedType = TypeFactory.defaultInstance().constructType(Integer.TYPE);

        JavaType resultType = propertyBuilder.findSerializationType(a, false, declaredType);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        String[] array = new String[0];
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(member.getValue(anyObject())).thenReturn("default");
        when(member.getName()).thenReturn("name");

        Object defaultBean = propertyBuilder.getDefaultBean();
        Object result = propertyBuilder.getPropertyDefaultValue("name", member, TypeFactory.defaultInstance().constructType(String[].class));
        assertEquals("default", result);
    }

    @Test
    public void testGetDefaultBean() throws Exception {
        Class<?> clazz = String[].class;
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        Object defaultBean = propertyBuilder.getDefaultBean();
        Object result = constructor.newInstance();
        assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDefaultBeanWithNull() throws Exception {
        Class<?> clazz = String[].class;
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        propertyBuilder.getDefaultBean();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueWithNullPropertyName() throws Exception {
        String[] array = new String[0];
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(member.getName()).thenReturn(null);

        Object result = propertyBuilder.getPropertyDefaultValue(null, member, TypeFactory.defaultInstance().constructType(String[].class));
        fail();
    }

    @Test
    public void testGetPropertyDefaultValueWithEmptyStringPropertyName() throws Exception {
        String[] array = new String[0];
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(member.getName()).thenReturn("");

        Object result = propertyBuilder.getPropertyDefaultValue("", member, TypeFactory.defaultInstance().constructType(String[].class));
        assertEquals("", result);
    }

    @Test
    public void testGetPropertyDefaultValueWithBooleanPropertyName() throws Exception {
        String[] array = new String[0];
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(member.getName()).thenReturn("booleanProperty");

        Object result = propertyBuilder.getPropertyDefaultValue("booleanProperty", member, TypeFactory.defaultInstance().constructType(String[].class));
        assertEquals(false, result);
    }

    @Test
    public void testGetPropertyDefaultValueWithNumberPropertyName() throws Exception {
        String[] array = new String[0];
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(member.getName()).thenReturn("numberProperty");

        Object result = propertyBuilder.getPropertyDefaultValue("numberProperty", member, TypeFactory.defaultInstance().constructType(String[].class));
        assertEquals(0, result);
    }

    @Test
    public void testGetPropertyDefaultValueWithStringPropertyName() throws Exception {
        String[] array = new String[0];
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(member.getName()).thenReturn("stringProperty");

        Object result = propertyBuilder.getPropertyDefaultValue("stringProperty", member, TypeFactory.defaultInstance().constructType(String[].class));
        assertEquals("", result);
    }

    @Test
    public void testGetPropertyDefaultValueWithArrayListPropertyName() throws Exception {
        String[] array = new String[0];
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(member.getName()).thenReturn("listProperty");

        Object result = propertyBuilder.getPropertyDefaultValue("listProperty", member, TypeFactory.defaultInstance().constructType(List.class));
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testGetPropertyDefaultValueWithHashMapPropertyName() throws Exception {
        String[] array = new String[0];
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(member.getName()).thenReturn("mapProperty");

        Object result = propertyBuilder.getPropertyDefaultValue("mapProperty", member, TypeFactory.defaultInstance().constructType(Map.class));
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testGetPropertyDefaultValueWithCustomClassPropertyName() throws Exception {
        String[] array = new String[0];
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(member.getName()).thenReturn("customClassProperty");

        Object result = propertyBuilder.getPropertyDefaultValue("customClassProperty", member, new TypeReference<MyCustomClass>() {}.getType());
        fail();
    }

    @Test
    public void testGetDefaultValueWithArrayType() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        Object result = propertyBuilder.getDefaultValue(javaType);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testGetDefaultValueWithStringType() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);

        Object result = propertyBuilder.getDefaultValue(javaType);
        assertEquals("", result);
    }

    @Test
    public void testGetDefaultValueWithNumberType() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(int.class);

        Object result = propertyBuilder.getDefaultValue(javaType);
        assertEquals(0, result);
    }

    @Test
    public void testGetDefaultValueWithListType() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);

        Object result = propertyBuilder.getDefaultValue(javaType);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testGetDefaultValueWithMapType() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);

        Object result = propertyBuilder.getDefaultValue(javaType);
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testGetDefaultValueWithCustomClassType() throws Exception {
        JavaType javaType = new TypeReference<MyCustomClass>() {}.getType();

        Object result = propertyBuilder.getDefaultValue(javaType);
        fail();
    }

    @Test
    public void testThrowWrappedException() throws Exception {
        Exception e = new Exception("Test exception");
        String propName = "propName";
        Object defaultBean = new Object();

        Object result = propertyBuilder._throwWrapped(e, propName, defaultBean);
        fail();
    }

    private static class MyCustomClass {
    }
}