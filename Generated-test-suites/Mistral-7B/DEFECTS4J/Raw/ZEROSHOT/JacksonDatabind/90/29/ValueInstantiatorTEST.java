package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;

import java.io.IOException;

/**
 * Test class for ValueInstantiator.
 */
public class ValueInstantiatorTest {

    private static class TestValueInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromInt() {
            return true;
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return new Integer(value);
        }
    }

    private static class TestValue {
        private int value;

        @JsonCreator
        public TestValue(@JsonProperty("value") int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @org.junit.Test
    public void testCanInstantiate() throws Exception {
        ValueInstantiator valueInstantiator = new TestValueInstantiator();
        assertTrue(valueInstantiator.canInstantiate());
    }

    @org.junit.Test
    public void testCreateFromInt() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        JsonParser parser = jsonFactory.createParser("{\"value\": 42}");
        ValueInstantiator valueInstantiator = new TestValueInstantiator();
        Object result = valueInstantiator.createFromInt(ctxt, 42);
        assertEquals(new Integer(42), result);
    }

    @org.junit.Test(expected = IOException.class)
    public void testCreateStringNotFound() throws Exception {
        ValueInstantiator valueInstantiator = new TestValueInstantiator();
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        JsonParser parser = jsonFactory.createParser("\"test\"");
        Object result = valueInstantiator.createFromString(ctxt, parser.getText());
        fail();
    }

    @org.junit.Test
    public void testCreateFromObjectWith() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        JsonParser parser = jsonFactory.createParser("{\"value\": 42}");
        Object result = new TestValueInstantiator().createFromObjectWith(ctxt, parser);
        TestValue testValue = (TestValue) result;
        assertEquals(42, testValue.getValue());
    }

    @org.junit.Test(expected = IOException.class)
    public void testCreateFromObjectWithMissingParameter() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        JsonParser parser = jsonFactory.createParser("{\"value\": 42, \"missing\": \"value\"}");
        SettableBeanProperty[] args = new TestValueInstantiator().getFromObjectArguments(ctxt);
        new TestValueInstantiator().createFromObjectWith(ctxt, args, null);
        fail();
    }

    @org.junit.Test(expected = IOException.class)
    public void testCreateFromObjectWithNotEnoughParameters() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        JsonParser parser = jsonFactory.createParser("{\"value\": 42, \"missing\": \"value1\", \"missing2\": \"value2\"}");
        SettableBeanProperty[] args = new TestValueInstantiator().getFromObjectArguments(ctxt);
        new TestValueInstantiator().createFromObjectWith(ctxt, args, null);
        fail();
    }

    @org.junit.Test(expected = IOException.class)
    public void testCreateUsingDefaultNotFound() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        JsonParser parser = jsonFactory.createParser("\"test\"");
        Object result = new TestValueInstantiator().createUsingDefault(ctxt);
        fail();
    }

    @org.junit.Test(expected = IOException.class)
    public void testCreateUsingDelegateNotFound() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        JsonParser parser = jsonFactory.createParser("{\"test\": 42}");
        Object result = new TestValueInstantiator().createUsingDelegate(ctxt, parser.nextValue());
        fail();
    }

    @org.junit.Test(expected = IOException.class)
    public void testCreateUsingArrayDelegateNotFound() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        JsonParser parser = jsonFactory.createParser("{\"arr\": [1, 2, 3]}");
        Object result = new TestValueInstantiator().createUsingArrayDelegate(ctxt, parser.nextValue());
        fail();
    }
}