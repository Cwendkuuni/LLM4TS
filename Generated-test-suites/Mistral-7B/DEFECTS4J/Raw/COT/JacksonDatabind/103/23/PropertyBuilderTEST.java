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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.TreeMap;

import static org.junit.Assert.*;

/**
 * Test cases for PropertyBuilder class.
 */
public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    private BeanDescription beanDescription;
    private SerializationConfig serializationConfig;
    private AnnotationIntrospector annotationIntrospector;

    @Before
    public void setup() throws Exception {
        serializationConfig = Mockito.mock(SerializationConfig.class);
        beanDescription = Mockito.mock(BeanDescription.class);
        annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);

        propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
    }

    @Test
    public void testConstructor() {
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        when(beanDescription.getClassAnnotations()).thenReturn(new Annotations());
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertEquals(annotations.getClass(), Annotations.class);
    }

    @Test
    public void testBuildWriter() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        Object defaultBean = new String("defaultValue");

        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JsonSerializer<?> jsonSerializer = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);

        when(beanDescription.getBeanClass()).thenReturn(String.class);
        when(beanDescription.getBeanClassInfo()).thenReturn(new ClassInfo(String.class));
        when(beanDescription.findPropertyDefinition("testProp")).thenReturn(propDef);
        when(beanDescription.findAnnotations("testProp")).thenReturn(new Annotations());
        when(beanDescription.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDescription.findDefaultViews()).thenReturn(new Class<?>[0]);
        when(annotationIntrospector.findNullSerializer(am)).thenReturn(jsonSerializer);
        when(annotationIntrospector.findUnwrappingNameTransformer(am)).thenReturn(null);

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, propDef, javaType, jsonSerializer, typeSerializer, null, am, true);
        assertNotNull(beanPropertyWriter);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterWithException() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        Object defaultBean = new String("defaultValue");

        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);

        when(beanDescription.getBeanClass()).thenReturn(String.class);
        when(beanDescription.getBeanClassInfo()).thenReturn(new ClassInfo(String.class));
        when(beanDescription.findPropertyDefinition("testProp")).thenReturn(propDef);
        when(beanDescription.findAnnotations("testProp")).thenReturn(new Annotations());
        when(beanDescription.getClassAnnotations()).thenThrow(new JsonMappingException("Test exception"));

        try {
            propertyBuilder.buildWriter(prov, propDef, javaType, null, null, null, null, true);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Test exception", e.getMessage());
        }
    }

    @Test
    public void testFindSerializationType() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        Annotated a = Mockito.mock(Annotated.class);

        when(annotationIntrospector.refineSerializationType(serializationConfig, a, javaType)).thenReturn(javaType);
        JavaType actualType = propertyBuilder.findSerializationType(a, false, javaType);
        assertEquals(javaType, actualType);

        when(annotationIntrospector.refineSerializationType(serializationConfig, a, javaType)).thenReturn(TypeFactory.defaultInstance().constructType(Integer.class));
        actualType = propertyBuilder.findSerializationType(a, false, javaType);
        assertNotEquals(javaType, actualType);
    }

    @Test
    public void testFindSerializationTypeWithException() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        Annotated a = Mockito.mock(Annotated.class);

        when(annotationIntrospector.refineSerializationType(serializationConfig, a, javaType)).thenThrow(new JsonMappingException("Test exception"));

        try {
            propertyBuilder.findSerializationType(a, false, javaType);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Test exception", e.getMessage());
        }
    }

    @Test
    public void testGetDefaultBean() throws Exception {
        when(beanDescription.instantiateBean(true)).thenReturn(new Object() {});

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);

        when(beanDescription.instantiateBean(true)).thenThrow(new InstantiationException(""));

        try {
            propertyBuilder.getDefaultBean();
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        String defaultBeanValue = "defaultBeanValue";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        when(member.getValue(defaultBean)).thenReturn(defaultBeanValue);

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("testProp", member, type);
        assertEquals(defaultBeanValue, propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        Serializable defaultValue = (Serializable) "defaultValue";
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);

        Object propertyDefaultValue = propertyBuilder.getDefaultValue(javaType);
        assertEquals(defaultValue, propertyDefaultValue);
    }

    @Test
    public void testThrowWrapped() throws Exception {
        Exception exception = new Exception("Test exception");

        String propName = "testProp";
        Object defaultBean = new Object() {};

        Throwable t = propertyBuilder._throwWrapped(exception, propName, defaultBean);
        assertEquals(exception.getCause(), t.getCause());
    }

    @Test
    public void testFindMethods() {
        Class<?> clazz = PropertyBuilder.class;
        Method[] methods = clazz.getMethods();

        TreeMap<String, Method> methodsMap = new TreeMap<>();
        for (Method method : methods) {
            methodsMap.put(method.getName(), method);
        }

        assertTrue(methodsMap.containsKey("buildWriter"));
        assertTrue(methodsMap.containsKey("findSerializationType"));
        assertTrue(methodsMap.containsKey("getDefaultBean"));
        assertTrue(methodsMap.containsKey("getPropertyDefaultValue"));
        assertTrue(methodsMap.containsKey("getDefaultValue"));
        assertTrue(methodsMap.containsKey("_throwWrapped"));
        assertTrue(methodsMap.containsKey("getClassAnnotations"));
    }
}