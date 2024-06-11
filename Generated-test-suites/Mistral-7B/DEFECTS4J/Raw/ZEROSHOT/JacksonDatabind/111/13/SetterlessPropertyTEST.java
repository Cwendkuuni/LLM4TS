package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

/**
 * Unit tests for {@link SetterlessProperty} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class SetterlessPropertyTest {

    private SetterlessProperty setterlessProperty;
    private Object testInstance;
    private JsonParser mockJsonParser;
    private DeserializationContext mockDeserializationContext;
    private TypeDeserializer mockTypeDeserializer;
    private NullValueProvider mockNullValueProvider;
    private Method getterMethod;

    @Before
    public void setUp() throws Exception {
        mockJsonParser = Mockito.mock(JsonParser.class);
        mockDeserializationContext = Mockito.mock(DeserializationContext.class);
        mockTypeDeserializer = Mockito.mock(TypeDeserializer.class);
        mockNullValueProvider = Mockito.mock(NullValueProvider.class);

        getterMethod = Mockito.spy(new MethodStub().getMethod());

        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
        BeanPropertyDefinition beanPropertyDefinition = Mockito.mock(BeanPropertyDefinition.class);

        setterlessProperty = new SetterlessProperty(beanPropertyDefinition, Mockito.mock(JavaType.class),
                mockTypeDeserializer, Annotations.empty(), annotatedMethod) {
            @Override
            protected Method getter() {
                return getterMethod;
            }
        };

        testInstance = new TestClass();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object valueToSet = new Object();

        Mockito.when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(mockJsonParser.getValueAsString()).thenReturn("testValue");

        setterlessProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, testInstance);

        assertEquals("testValue", getValueFromTestInstance(testInstance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object valueToSet = new Object();

        Mockito.when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(mockJsonParser.getValueAsString()).thenReturn("testValue");

        Object returnedObject = setterlessProperty.deserializeSetAndReturn(mockJsonParser,
                mockDeserializationContext, testInstance);

        assertEquals(testInstance, returnedObject);
        assertEquals("testValue", getValueFromTestInstance(testInstance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        setterlessProperty.set(testInstance, new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        setterlessProperty.setAndReturn(testInstance, new Object());
    }

    @Test
    public void testGetAnnotation() {
        JsonProperty jsonPropertyAnnotation = new JsonProperty("testProperty");

        Mockito.when(annotatedMethod.getAnnotation(JsonProperty.class)).thenReturn(jsonPropertyAnnotation);

        assertEquals(jsonPropertyAnnotation, setterlessProperty.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() {
        assertEquals(annotatedMethod, setterlessProperty.getMember());
    }

    private Object getValueFromTestInstance(Object instance) throws IOException, InvocationTargetException, IllegalAccessException {
        return getterMethod.invoke(instance);
    }

    private static class MethodStub {
        public Method getMethod() throws NoSuchMethodException, SecurityException {
            return Mockito.spy(TestClass.class.getMethod("getValue"));
        }
    }

    private static class TestClass {
        public Object getValue() {
            return new Object();
        }
    }
}