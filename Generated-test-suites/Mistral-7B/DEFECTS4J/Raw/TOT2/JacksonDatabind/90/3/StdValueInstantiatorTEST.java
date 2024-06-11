package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException.ReferenceType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.json.JsonDeserializer.None;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;

public class StdValueInstantiatorTest {

    private ObjectMapper objectMapper;
    private StdValueInstantiator stdValueInstantiator;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        stdValueInstantiator = new StdValueInstantiator(objectMapper.getConfig(), java.lang.Boolean.TYPE);
    }

    @Test
    public void testDefaultConstructor() throws IOException {
        assertTrue(stdValueInstantiator.canCreateUsingDefault());
        assertEquals(Boolean.TRUE, stdValueInstantiator.createUsingDefault(objectMapper.getDeserializationContext()));
        assertEquals(Boolean.FALSE, stdValueInstantiator.createUsingDefault(objectMapper.getDeserializationContext()));
    }

    @Test
    public void testConstructorWithArgs() throws IOException {
        AnnotatedWithParams constructorWithArgs = new AnnotatedWithParams(StdValueInstantiator.class.getDeclaredMethod("configureFromObjectSettings", AnnotatedWithParams.class, AnnotatedWithParams.class, JavaType.class, SettableBeanProperty[].class));
        stdValueInstantiator.configureFromObjectSettings(constructorWithArgs, null, null, null, null, null);
        assertTrue(stdValueInstantiator.canCreateFromObjectWith(objectMapper.getDeserializationContext(), new Object[] {}));
        assertEquals(Boolean.TRUE, stdValueInstantiator.createFromObjectWith(objectMapper.getDeserializationContext(), new Object[] {}));
    }

    @Test
    public void testDelegate() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(MyDelegate.class);
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(MyDelegate.class.getDeclaredConstructor());
        stdValueInstantiator.configureFromDelegateSettings(delegateCreator, javaType, null);
        assertTrue(stdValueInstantiator.canCreateUsingDelegate());
        MyDelegate delegate = (MyDelegate) stdValueInstantiator.createUsingDelegate(objectMapper.getDeserializationContext(), new Object());
        assertEquals(1, delegate.getValue());
    }

    @Test
    public void testArrayDelegate() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructArrayType(int[].class);
        AnnotatedWithParams arrayDelegateCreator = new AnnotatedWithParams(StdValueInstantiator.class.getDeclaredMethod("configureFromArraySettings", AnnotatedWithParams.class, JavaType.class, SettableBeanProperty[].class));
        stdValueInstantiator.configureFromArraySettings(arrayDelegateCreator, javaType, new SettableBeanProperty[] {});
        assertTrue(stdValueInstantiator.canCreateUsingArrayDelegate());
        int[] array = (int[]) stdValueInstantiator.createUsingArrayDelegate(objectMapper.getDeserializationContext(), new Object());
        assertArrayEquals(new int[] {1, 2, 3}, array);
    }

    @Test
    public void testFromString() throws IOException {
        AnnotatedWithParams fromStringCreator = new AnnotatedWithParams(StdValueInstantiator.class.getDeclaredMethod("configureFromStringCreator", AnnotatedWithParams.class));
        stdValueInstantiator.configureFromStringCreator(fromStringCreator);
        assertTrue(stdValueInstantiator.canCreateFromString());
        assertEquals(Boolean.TRUE, stdValueInstantiator.createFromString(objectMapper.getDeserializationContext(), "true"));
        assertEquals(Boolean.FALSE, stdValueInstantiator.createFromString(objectMapper.getDeserializationContext(), "false"));
    }

    @Test
    public void testFromInt() throws IOException {
        AnnotatedWithParams fromIntCreator = new AnnotatedWithParams(StdValueInstantiator.class.getDeclaredMethod("configureFromIntCreator", AnnotatedWithParams.class));
        stdValueInstantiator.configureFromIntCreator(fromIntCreator);
        assertTrue(stdValueInstantiator.canCreateFromInt());
        assertEquals(Boolean.TRUE, stdValueInstantiator.createFromInt(objectMapper.getDeserializationContext(), 1));
        assertEquals(Boolean.FALSE, stdValueInstantiator.createFromInt(objectMapper.getDeserializationContext(), 0));
    }

    @Test
    public void testFromLong() throws IOException {
        AnnotatedWithParams fromLongCreator = new AnnotatedWithParams(StdValueInstantiator.class.getDeclaredMethod("configureFromLongCreator", AnnotatedWithParams.class));
        stdValueInstantiator.configureFromLongCreator(fromLongCreator);
        assertTrue(stdValueInstantiator.canCreateFromLong());
        assertEquals(Boolean.TRUE, stdValueInstantiator.createFromLong(objectMapper.getDeserializationContext(), 1L));
        assertEquals(Boolean.FALSE, stdValueInstantiator.createFromLong(objectMapper.getDeserializationContext(), 0L));
    }

    @Test
    public void testFromDouble() throws IOException {
        AnnotatedWithParams fromDoubleCreator = new AnnotatedWithParams(StdValueInstantiator.class.getDeclaredMethod("configureFromDoubleCreator", AnnotatedWithParams.class));
        stdValueInstantiator.configureFromDoubleCreator(fromDoubleCreator);
        assertTrue(stdValueInstantiator.canCreateFromDouble());
        assertEquals(Boolean.TRUE, stdValueInstantiator.createFromDouble(objectMapper.getDeserializationContext(), 1.0D));
        assertEquals(Boolean.FALSE, stdValueInstantiator.createFromDouble(objectMapper.getDeserializationContext(), 0.0D));
    }

    @Test
    public void testFromBoolean() throws IOException {
        AnnotatedWithParams fromBooleanCreator = new AnnotatedWithParams(StdValueInstantiator.class.getDeclaredMethod("configureFromBooleanCreator", AnnotatedWithParams.class));
        stdValueInstantiator.configureFromBooleanCreator(fromBooleanCreator);
        assertTrue(stdValueInstantiator.canCreateFromBoolean());
        assertEquals(Boolean.TRUE, stdValueInstantiator.createFromBoolean(objectMapper.getDeserializationContext(), true));
        assertEquals(Boolean.FALSE, stdValueInstantiator.createFromBoolean(objectMapper.getDeserializationContext(), false));
    }

    @Test
    public void testIncompleteParameter() throws IOException {
        AnnotatedParameter incompleteParameter = new AnnotatedParameter(StdValueInstantiator.class.getDeclaredMethod("configureIncompleteParameter", AnnotatedParameter.class));
        stdValueInstantiator.configureIncompleteParameter(incompleteParameter);
        assertFalse(stdValueInstantiator.canCreateFromObjectWith(objectMapper.getDeserializationContext(), new Object[] {}));
        assertNull(stdValueInstantiator.createFromObjectWith(objectMapper.getDeserializationContext(), new Object[] {}));
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionFromString() throws IOException {
        AnnotatedWithParams fromStringCreator = new AnnotatedWithParams(StdValueInstantiator.class.getDeclaredMethod("configureFromStringCreator", AnnotatedWithParams.class));
        stdValueInstantiator.configureFromStringCreator(fromStringCreator);
        objectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, true);
        Object value = stdValueInstantiator.createFromString(objectMapper.getDeserializationContext(), "");
        fail("Expected JsonMappingException");
    }

    @Test
    public void testJsonMappingExceptionFromInt() throws IOException {
        AnnotatedWithParams fromIntCreator = new AnnotatedWithParams(StdValueInstantiator.class.getDeclaredMethod("configureFromIntCreator", AnnotatedWithParams.class));
        stdValueInstantiator.configureFromIntCreator(fromIntCreator);
        objectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, true);
        Object value = stdValueInstantiator.createFromInt(objectMapper.getDeserializationContext(), Integer.MIN_VALUE);
        assertNull(value);
    }

    @Test
    public void testJsonMappingExceptionFromLong() throws IOException {
        AnnotatedWithParams fromLongCreator = new AnnotatedWithParams(StdValueInstantiator.class.getDeclaredMethod("configureFromLongCreator", AnnotatedWithParams.class));
        stdValueInstantiator.configureFromLongCreator(fromLongCreator);
        objectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, true);
        Object value = stdValueInstantiator.createFromLong(objectMapper.getDeserializationContext(), Long.MIN_VALUE);
        assertNull(value);
    }

    @Test
    public void testJsonMappingExceptionFromDouble() throws IOException {
        AnnotatedWithParams fromDoubleCreator = new AnnotatedWithParams(StdValueInstantiator.class.getDeclaredMethod("configureFromDoubleCreator", AnnotatedWithParams.class));
        stdValueInstantiator.configureFromDoubleCreator(fromDoubleCreator);
        objectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, true);
        Object value = stdValueInstantiator.createFromDouble(objectMapper.getDeserializationContext(), Double.MIN_VALUE);
        assertNull(value);
    }

    @Test
    public void testJsonMappingExceptionFromBoolean() throws IOException {
        AnnotatedWithParams fromBooleanCreator = new AnnotatedWithParams(StdValueInstantiator.class.getDeclaredMethod("configureFromBooleanCreator", AnnotatedWithParams.class));
        stdValueInstantiator.configureFromBooleanCreator(fromBooleanCreator);
        objectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, true);
        Object value = stdValueInstantiator.createFromBoolean(objectMapper.getDeserializationContext(), false);
        assertNull(value);
    }

    @Test
    public void testJsonMappingExceptionFromObject() throws IOException {
        AnnotatedWithParams objectCreator = new AnnotatedWithParams(StdValueInstantiator.class.getDeclaredMethod("configureFromObjectSettings", AnnotatedWithParams.class, AnnotatedWithParams.class, JavaType.class, SettableBeanProperty[].class));
        stdValueInstantiator.configureFromObjectSettings(objectCreator, null, null, null, null, null);
        objectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, true);
        ObjectNode objectNode = objectMapper.createObjectNode();
        Object value = stdValueInstantiator.createFromObjectWith(objectMapper.getDeserializationContext(), new Object[] {objectNode});
        assertNull(value);
    }

    @Test
    public void testJsonMappingExceptionFromArray() throws IOException {
        JavaType arrayType = objectMapper.getTypeFactory().constructArrayType(int[].class);
        AnnotatedWithParams arrayCreator = new AnnotatedWithParams(StdValueInstantiator.class.getDeclaredMethod("configureFromArraySettings", AnnotatedWithParams.class, JavaType.class, SettableBeanProperty[].class));
        stdValueInstantiator.configureFromArraySettings(arrayCreator, arrayType, new SettableBeanProperty[] {});
        objectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, true);
        ObjectNode objectNode = objectMapper.createObjectNode();
        ObjectNode arrayNode = objectMapper.createObjectNode().putPOJO("value", new int[0]);
        Object value = stdValueInstantiator.createUsingArrayDelegate(objectMapper.getDeserializationContext(), new Object[] {objectNode, arrayNode});
        assertNull(value);
    }

    @Test
    public void testJsonMappingExceptionFromDelegate() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(MyDelegate.class);
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(MyDelegate.class.getDeclaredConstructor());
        stdValueInstantiator.configureFromDelegateSettings(delegateCreator, javaType, null);
        objectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, true);
        Object value = stdValueInstantiator.createUsingDelegate(objectMapper.getDeserializationContext(), new Object());
        assertNull(value);
    }

    private static void assertArrayEquals(int[] expected, int[] actual) {
        assertArrayEquals(expected, actual, "Array comparison failed");
    }

    static void assertArrayEquals(int[] expected, int[] actual, String message) {
        if (Arrays.equals(expected, actual)) {
            return;
        }
        fail(message + "\nExpected:\n" + Arrays.toString(expected) + "\nActual:\n" + Arrays.toString(actual));
    }

    static class MyDelegate {
        private int value;

        public MyDelegate() {
            value = 1;
        }

        public MyDelegate(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}