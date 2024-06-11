package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.base.ParserUtils;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValueInstantiatorTest {

    private static class TestValueInstantiator extends ValueInstantiator {
        @Override
        public Class<?> getValueClass() {
            return TestValue.class;
        }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return new TestValue();
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            return new TestValue((Integer) args[0], (String) args[1]);
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return new TestValue(value);
        }
    }

    @Test
    public void testCreateUsingDefault() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{}");
        Object result = new TestValueInstantiator().createUsingDefault(parser.getContext());
        assertNotNull(result);
    }

    @Test
    public void testCreateFromInt() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("42");
        Object result = new TestValueInstantiator().createFromInt(parser.getContext(), parser.getValueAsInt());
        assertNotNull(result);
        assertEquals(42, ((TestValue) result).getIntValue());
    }

    @Test
    public void testCreateFromObjectWith() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"intValue\": 42, \"stringValue\": \"foo\"}");
        Object result = new TestValueInstantiator().createFromObjectWith(parser.getContext(), parser.readValueAs(Object[].class));
        assertNotNull(result);
        assertEquals(42, ((TestValue) result).getIntValue());
        assertEquals("foo", ((TestValue) result).getStringValue());
    }

    @Test
    public void testCanCreateFromString() {
        assertFalse(new TestValueInstantiator().canCreateFromString());
    }

    @Test
    public void testCanCreateFromLong() {
        assertFalse(new TestValueInstantiator().canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() {
        assertFalse(new TestValueInstantiator().canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() {
        assertFalse(new TestValueInstantiator().canCreateFromBoolean());
    }

    @Test
    public void testCanInstantiate() {
        assertTrue(new TestValueInstantiator().canInstantiate());
    }

    @Test
    public void testGetValueTypeDesc() {
        TestValueInstantiator instantiator = new TestValueInstantiator();
        assertEquals(TestValue.class.getName(), instantiator.getValueTypeDesc());
    }

    static class TestValue {
        private final int intValue;
        private final String stringValue;

        TestValue() {
            this(0, "");
        }

        TestValue(int intValue, String stringValue) {
            this.intValue = intValue;
            this.stringValue = stringValue;
        }

        int getIntValue() {
            return intValue;
        }

        String getStringValue() {
            return stringValue;
        }
    }
}