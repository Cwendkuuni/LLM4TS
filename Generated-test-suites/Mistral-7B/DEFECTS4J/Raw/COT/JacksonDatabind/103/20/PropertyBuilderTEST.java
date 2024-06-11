package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector.FindSerializationType;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ConverterUtils;
import com.fasterxml.jackson.databind.util.TypeFactory;

@RunWith(MockitoJUnitRunner.class)
public class PropertyBuilderTest {

    @Mock
    private SerializationConfig _config;

    @Mock
    private BeanDescription _beanDesc;

    @Mock
    private AnnotationIntrospector _annotationIntrospector;

    @InjectMocks
    private PropertyBuilder propertyBuilder;

    @Test
    public void testConstructor() {
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testBuildWriter_default() throws IOException {
        // Given
        AnnotatedElement element = Mockito.mock(AnnotatedElement.class);
        JavaType declaredType = TypeFactory.type(Integer[].class, Object.class);
        JavaType serializationType = TypeFactory.type(Integer[].class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        // When
        BeanPropertyWriter actual = propertyBuilder.buildWriter(_config, null, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        // Then
        assertNotNull(actual);
    }

    @Test
    public void testFindSerializationType_Annotated() throws JsonMappingException {
        // Given
        AnnotatedElement element = Mockito.mock(AnnotatedElement.class);
        JavaType declaredType = TypeFactory.type(Integer[].class, Object.class);
        JavaType secondary = TypeFactory.type(String[].class);

        // When
        JavaType actual = propertyBuilder.findSerializationType(element, true, declaredType);

        // Then
        assertNotNull(actual);
        assertNull(actual.getRawClass().isAssignableFrom(declaredType.getRawClass()));
        assertNull(actual.getRawClass().isAssignableFrom(secondary.getRawClass()));
        assertNull(actual.getRawClass().isAssignableFrom(Object.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationType_Annotated_IllegalType() throws JsonMappingException {
        // Given
        AnnotatedElement element = Mockito.mock(AnnotatedElement.class);
        JavaType declaredType = TypeFactory.type(Integer[].class, Object.class);
        JavaType secondary = TypeFactory.type(Object.class);

        // When
        propertyBuilder.findSerializationType(element, true, declaredType);
    }

    @Test
    public void testGetClassAnnotations() {
        // Given

        // When
        Annotations actual = propertyBuilder.getClassAnnotations();

        // Then
        assertNotNull(actual);
    }

    @Test
    public void testGetDefaultBean() {
        // Given

        // When
        Object actual = propertyBuilder.getDefaultBean();

        // Then
        assertNull(actual);
    }

    @Test
    public void testGetDefaultBean_Instance() {
        // Given

        // When
        Object actual = propertyBuilder.getDefaultBean();

        // Then
        assertNotNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_NullName() {
        // Given
        String name = null;
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.type(Integer.class);

        // When & Then
        propertyBuilder.getPropertyDefaultValue(name, member, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_NullMember() {
        // Given
        String name = "name";
        AnnotatedMember member = null;
        JavaType type = TypeFactory.type(Integer.class);

        // When & Then
        propertyBuilder.getPropertyDefaultValue(name, member, type);
    }

    @Test
    public void testGetPropertyDefaultValue_DefaultInstance() throws Exception {
        // Given
        String name = "name";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.type(Integer.class);
        Object defaultBean = new Object();

        // When
        Object actual = propertyBuilder.getPropertyDefaultValue(name, member, type);

        // Then
        assertNull(actual);
    }

    @Test
    public void testGetPropertyDefaultValue_Value() throws Exception {
        // Given
        String name = "name";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = TypeFactory.type(Integer.class);
        Object defaultBean = new Object() {
            int value = 123;
        };

        // When
        Object actual = propertyBuilder.getPropertyDefaultValue(name, member, type);

        // Then
        assertNotNull(actual);
        assertEquals(123, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_NullType() {
        // Given
        String name = "name";
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType type = null;

        // When & Then
        propertyBuilder.getPropertyDefaultValue(name, member, type);
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        // Given
        JavaType type = TypeFactory.type(Integer.class);

        // When
        Object actual = propertyBuilder.getDefaultValue(type);

        // Then
        assertNull(actual);
    }

    @Test
    public void testGetDefaultValue_Value() throws Exception {
        // Given
        JavaType type = TypeFactory.type(Integer.class);

        // When
        Object actual = propertyBuilder.getDefaultValue(type);

        // Then
        assertNotNull(actual);
        assertEquals(0, actual);
    }

    @Test
    public void test_throwWrapped() throws IOException {
        // Given
        Exception e = new RuntimeException("Test Exception");
        String propName = "propName";
        Object defaultBean = new Object();

        // When
        Throwable actual = propertyBuilder._throwWrapped(e, propName, defaultBean);

        // Then
        assertNotNull(actual);
        assertEquals("Failed to get property 'propName' of default "+defaultBean.getClass().getName()+" instance", actual.getMessage());
        assertEquals(RuntimeException.class, actual.getCause().getClass());
    }
}