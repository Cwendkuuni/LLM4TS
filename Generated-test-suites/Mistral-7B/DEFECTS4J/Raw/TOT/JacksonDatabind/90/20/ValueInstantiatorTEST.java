package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.fasterxml.jackson.core.util.TestUtils.convertValueToTree;
import static org.junit.Assert.assertEquals;

public class ValueInstantiatorTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDefaultInstantiator() throws IOException {
        ValueInstantiator instantiator = mapper.getValueInstantiatorInstance(String.class);
        assertEquals("default", instantiator.createUsingDefault(mapper.deserializationContext()));
    }

    @Test(expected = IOException.class)
    public void testMissingInstantiator() throws IOException {
        ValueInstantiator instantiator = mapper.getValueInstantiatorInstance(UnsupportedClass.class);
        instantiator.createUsingDefault(mapper.deserializationContext());
    }

    @Test
    public void testStringInstantiator() throws IOException {
        ValueInstantiator instantiator = mapper.getValueInstantiatorInstance(String.class);
        ObjectNode node = ObjectMapperTest.MAPPER.createObjectNode();
        node.put("value", "test");
        Object value = instantiator.createFromObjectWith(mapper.deserializationContext(), node);
        assertEquals("test", value);
    }

    @Test
    public void testCustomInstantiator() throws IOException {
        class MyCustomClass {
            private final int value;

            @JsonCreator
            public MyCustomClass(@JsonProperty("value") int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        ValueInstantiator instantiator = mapper.getValueInstantiatorInstance(MyCustomClass.class);
        ObjectNode node = ObjectMapperTest.MAPPER.createObjectNode();
        node.put("value", 123);
        MyCustomClass value = instantiator.createFromObjectWith(mapper.deserializationContext(), node);
        assertEquals(123, value.getValue());
    }

    @Test(expected = IOException.class)
    public void testMissingCreatorProperties() throws IOException {
        class MyMissingPropertyClass {
            private final int value;
            private final String missingProperty;

            @JsonCreator
            public MyMissingPropertyClass(@JsonProperty("value") int value) {
                this.value = value;
            }
        }

        ValueInstantiator instantiator = mapper.getValueInstantiatorInstance(MyMissingPropertyClass.class);
        ObjectNode node = ObjectMapperTest.MAPPER.createObjectNode();
        node.put("value", 123);
        instantiator.createFromObjectWith(mapper.deserializationContext(), node);
    }

    @Test
    public void testBooleanInstantiator() throws IOException {
        ValueInstantiator instantiator = mapper.getValueInstantiatorInstance(boolean.class);
        ObjectNode node = ObjectMapperTest.MAPPER.createObjectNode();
        node.putBoolean("value", true);
        boolean value = instantiator.createFromObjectWith(mapper.deserializationContext(), node);
        assertEquals(true, value);
    }

    @Test
    public void testNumberInstantiator() throws IOException {
        ValueInstantiator instantiator = mapper.getValueInstantiatorInstance(int.class);
        ObjectNode node = ObjectMapperTest.MAPPER.createObjectNode();
        node.put("value", 123);
        int value = instantiator.createFromObjectWith(mapper.deserializationContext(), node).asInt();
        assertEquals(123, value);
    }

    private static class UnsupportedClass {
    }

    static class StdValueInstantiatorTest extends BaseValueInstantiator {
        public StdValueInstantiatorTest() {
            super(String.class);
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            return StdValueInstantiator.createFromObjectWith(ctxt, getFromObjectArguments(ctxt), this);
        }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return StdValueInstantiator.createUsingDefault(ctxt, this);
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, SettableBeanProperty[] props, PropertyValueBuffer buffer) throws IOException {
            return StdValueInstantiator.createFromObjectWith(ctxt, props, buffer, this);
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object delegate) throws IOException {
            return StdValueInstantiator.createFromObjectWith(ctxt, delegate, this);
        }

        @Override
        public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
            return StdValueInstantiator.createFromStringFallbacks(ctxt, value, this);
        }

        @Override
        public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
            return StdValueInstantiator.createFromBoolean(ctxt, value, this);
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return StdValueInstantiator.createFromInt(ctxt, value, this);
        }

        @Override
        public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
            return StdValueInstantiator.createFromLong(ctxt, value, this);
        }

        @Override
        public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
            return StdValueInstantiator.createFromDouble(ctxt, value, this);
        }

        @Override
        public AnnotatedWithParams getDefaultCreator() {
            return null;
        }

        @Override
        public AnnotatedWithParams getDelegateCreator() {
            return null;
        }

        @Override
        public AnnotatedWithParams getArrayDelegateCreator() {
            return null;
        }

        @Override
        public AnnotatedWithParams getWithArgsCreator() {
            return null;
        }

        @Override
        public AnnotatedParameter getIncompleteParameter() {
            return null;
        }
    }

    static class BaseValueInstantiator extends ValueInstantiator {
        public BaseValueInstantiator(Class<?> valueClass) {
            super();
            this.valueClass = valueClass;
        }

        @Override
        public Class<?> getValueClass() {
            return valueClass;
        }
    }
}