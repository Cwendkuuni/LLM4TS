package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Test class for MethodProperty
 */
public class MethodPropertyTest {

    private static final Class<?> TEST_CLASS = MethodProperty.class;
    private static final String TEST_FIELD_NAME = "testField";
    private static final Class<?> TEST_FIELD_TYPE = String.class;
    private static final String TEST_METHOD_NAME = "setTestField";
    private static final Class<?> TEST_METHOD_RETURN_TYPE = Void.TYPE;

    private JsonParser mockParser;
    private DeserializationContext mockContext;
    private Object testInstance;
    private Method testMethod;
    private SettableBeanProperty testProperty;

    @Before
    public void setup() throws Exception {
        mockParser = mock(JsonParser.class);
        mockContext = mock(DeserializationContext.class);
        testInstance = new Object();
        testMethod = Mockito.spy(Mockito.doReturn(new Method() {}).when(TEST_CLASS)
                .getDeclaredMethod(TEST_METHOD_NAME, TEST_FIELD_TYPE));

        Object propertyDefinition = mock(BeanPropertyDefinition.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
        Mockito.when(annotatedMethod.getAnnotated()).thenReturn(testMethod);

        testProperty = new MethodProperty(new PropertyName(TEST_FIELD_NAME), propertyDefinition, javaType, typeDeserializer, contextAnnotations, annotatedMethod, testMethod);
    }

    @Test
    public void testConstructorWithAllArguments() {
        MethodProperty methodProperty = new MethodProperty(
                new PropertyName("testProperty"),
                new BeanPropertyDefinition() {},
                new JavaType() {},
                new TypeDeserializer() {},
                new Annotations() {},
                Mockito.mock(AnnotatedMethod.class),
                Mockito.mock(Method.class)
        );
        assertNotNull(methodProperty);
    }

    @Test
    public void testWithName() {
        SettableBeanProperty result = testProperty.withName(new PropertyName("newName"));
        assertEquals(new PropertyName("newName"), result.getName());
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        SettableBeanProperty result = testProperty.withValueDeserializer(deserializer);
        assertEquals(deserializer, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() throws Exception {
        NullValueProvider nva = mock(NullValueProvider.class);
        SettableBeanProperty result = testProperty.withNullProvider(nva);
        assertEquals(nva, result.getNullProvider());
    }

    @Test
    public void testFixAccess() throws Exception {
        testProperty.fixAccess(mockContext);
        verify(testMethod).fixAccess(mockContext.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test
    public void testGetAnnotation() throws Exception {
        Annotation annotation = mock(Annotation.class);
        when(testMethod.getAnnotations()).thenReturn(new Annotation[0]);
        when(testMethod.getAnnotation(Annotation.class)).thenReturn(annotation);

        Annotation result = testProperty.getAnnotation(annotation.getClass());
        assertEquals(annotation, result);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember result = testProperty.getMember();
        assertEquals(testMethod, result);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object testValue = new Object();

        when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "testValue";
            }
        });

        testProperty.deserializeAndSet(mockParser, mockContext, testInstance);

        verify(testMethod).invoke(testInstance, testValue);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValueAndSkipNulls() throws Exception {
        when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
        when(mockContext.getNullValueProvider()).thenReturn(null);

        testProperty.deserializeAndSet(mockParser, mockContext, testInstance);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValueAndNoSkipNulls() throws Exception {
        when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
        when(mockContext.getNullValueProvider()).thenReturn(mock(NullValueProvider.class));

        testProperty.deserializeAndSet(mockParser, mockContext, testInstance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object testValue = new Object();

        when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "testValue";
            }
        });

        Object result = testProperty.deserializeSetAndReturn(mockParser, mockContext, testInstance);

        assertEquals(testInstance, result);
        verify(testMethod).invoke(testInstance, testValue);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithNullValueAndSkipNulls() throws Exception {
        when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
        when(mockContext.getNullValueProvider()).thenReturn(null);

        testProperty.deserializeSetAndReturn(mockParser, mockContext, testInstance);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithNullValueAndNoSkipNulls() throws Exception {
        when(mockParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
        when(mockContext.getNullValueProvider()).thenReturn(mock(NullValueProvider.class));

        testProperty.deserializeSetAndReturn(mockParser, mockContext, testInstance);
    }

    @Test
    public void testSet() throws Exception {
        Object testValue = new Object();

        testProperty.set(testInstance, testValue);

        verify(testMethod).invoke(testInstance, testValue);
    }

    @Test
    public void testSetWithNullValueAndSkipNulls() throws Exception {
        testProperty.set(testInstance, null);

        verifyNoMoreInteractions(testMethod);
    }

    @Test(expected = IOException.class)
    public void testSetWithNullValueAndNoSkipNulls() throws Exception {
        testProperty.set(testInstance, null);

        fail();
    }

    private void _throwAsIOE(Object... args) throws IOException {
        throw new IOException("Test error", new Exception("Inner exception", args));
    }
}