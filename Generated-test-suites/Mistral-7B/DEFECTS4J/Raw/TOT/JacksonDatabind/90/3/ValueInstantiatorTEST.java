package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValueInstantiatorTest {

    private static class MyValue {
        private int field1;
        private String field2;

        @JsonDeserialize(using = MyValueInstantiator.class)
        public MyValue(int field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public int getField1() {
            return field1;
        }

        public String getField2() {
            return field2;
        }
    }

    private static class MyValueInstantiator extends StdValueInstantiator {
        public MyValueInstantiator() {
            this(MyValue.class);
        }

        public MyValueInstantiator(Class<MyValue> c) {
            super(c);
        }

        @Override
        public boolean canCreateUsingObject() {
            return true;
        }

        @Override
        public Object createUsingObject(DeserializationContext ctxt, Object[] args) throws IOException {
            JavaType argType = ctxt.getTypeFactory().constructParamType(MyValue.class, Object.class);
            SettableBeanProperty[] props = new SettableBeanProperty[] {
                    PropertyHandler.construct(ctxt.getActiveContext(), "field1", Integer.class),
                    PropertyHandler.construct(ctxt.getActiveContext(), "field2", String.class)
            };
            Object[] argsArray = {new Long(1L), "test"};
            return createFromObjectWith(ctxt, props, new PropertyValueBuffer(props, argsArray));
        }
    }

    @Test
    public void testCreateValueInstanceFromJsonObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getConfig();

        MyValue value = mapper.readValue("{\"field1\":1,\"field2\":\"test\"}", MyValue.class);
        assertEquals(1, value.getField1());
        assertEquals("test", value.getField2());
    }

    @Test(expected = IOException.class)
    public void testMissingCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getConfig();

        JavaType type = config.getTypeFactory().constructType(MyValue.class);
        config.setFailOnEmptyBean(false);
        Object value = mapper.readValue("{}", type);
        assertNull(value);
    }

    @Test
    public void testStringConstructor() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getConfig();

        MyValue value = mapper.readValue("\"test\"", MyValue.class);
        assertNull(value.getField1());
        assertEquals("test", value.getField2());
    }

    @Test
    public void testIntConstructor() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getConfig();

        MyValue value = mapper.readValue("1", MyValue.class);
        assertEquals(1, value.getField1());
        assertNull(value.getField2());
    }

    @Test
    public void testLongConstructor() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getConfig();

        MyValue value = mapper.readValue("1L", MyValue.class);
        assertEquals(1, value.getField1());
        assertNull(value.getField2());
    }

    @Test
    public void testDoubleConstructor() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getConfig();

        MyValue value = mapper.readValue("1.0", MyValue.class);
        assertEquals(1, value.getField1());
        assertNull(value.getField2());
    }

    @Test
    public void testBooleanConstructor() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getConfig();

        MyValue value = mapper.readValue("true", MyValue.class);
        assertEquals(1, value.getField1());
        assertNull(value.getField2());
    }
}