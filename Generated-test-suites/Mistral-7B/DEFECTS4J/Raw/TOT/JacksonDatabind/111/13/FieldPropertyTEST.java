package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class FieldPropertyTest {

    static class TestBean {
        @JsonDeserialize(using = TestFieldDeserializer.class)
        private transient Field _privateField;
        private int publicField;

        public TestBean() {}

        public TestBean(int publicField, Field privateField) {
            this.publicField = publicField;
            this._privateField = privateField;
        }

        public int getPublicField() {
            return publicField;
        }

        private int getPrivateField() {
            return _privateField.getInt(this);
        }

        public static class TestFieldDeserializer extends StdSerializer<TestBean> {
            private static final long serialVersionUID = 1L;

            public TestFieldDeserializer() {
                this(null);
            }

            protected TestFieldDeserializer(Class<TestBean> t) {
                super(t);
            }

            @Override
            public TestBean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                TestBean obj = new TestBean();
                Field field = TestBean.class.getDeclaredField("_privateField");
                ClassUtil.setAccessible(field, true);
                if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
                    int value = p.getIntValue();
                    try {
                        field.setInt(obj, value);
                    } catch (IllegalAccessException e) {
                        throw new IOException("Failed to set private field: " + e.getMessage(), e);
                    }
                }
                return obj;
            }
        }

        static class TestTypeDeserializer extends StdSerializer<TestBean> implements TypeDeserializer {
            private static final long serialVersionUID = 1L;

            @Override
            public TestBean deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vtd) throws IOException {
                return deserialize(p, ctxt);
            }

            @Override
            public TestBean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                TestBean obj = new TestBean();
                Field field = TestBean.class.getDeclaredField("_privateField");
                ClassUtil.setAccessible(field, true);
                if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
                    int value = p.getIntValue();
                    try {
                        field.setInt(obj, value);
                    } catch (IllegalAccessException e) {
                        throw new IOException("Failed to set private field: " + e.getMessage(), e);
                    }
                }
                return obj;
            }
        }
    }

    @org.junit.Test
    public void testFieldPropertyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean(42, null);

        JsonParser p = mapper.getFactory().createParser("{\"publicField\": 42, \"_privateField\": 123}");
        Object readValue = mapper.readValue(p, TestBean.class);
        assertEquals(bean.getPublicField(), readValue.getPublicField());
        assertEquals(bean.getPrivateField(), readValue.getPrivateField());

        JsonParser nullValueParser = mapper.getFactory().createParser("{\"publicField\": null, \"_privateField\": 123}");
        Object readNullValue = mapper.readValue(nullValueParser, TestBean.class);
        assertNull(readNullValue.getPublicField());
        assertNull(readNullValue.getPrivateField());

        JsonParser missingValueParser = mapper.getFactory().createParser("{\"publicField\": 42}");
        assertThrows(com.fasterxml.jackson.databind.JsonMappingException.class, () -> mapper.readValue(missingValueParser, TestBean.class));

        JsonParser missingFieldParser = mapper.getFactory().createParser("{\"publicField\": 42, \"missingField\": 123}");
        assertThrows(com.fasterxml.jackson.databind.JsonMappingException.class, () -> mapper.readValue(missingFieldParser, TestBean.class));

        JsonParser invalidTypeParser = mapper.getFactory().createParser("{\"publicField\": \"notANumber\", \"_privateField\": 123}");
        assertThrows(com.fasterxml.jackson.core.JsonProcessingException.class, () -> mapper.readValue(invalidTypeParser, TestBean.class));
    }

    @org.junit.Test
    public void testFieldPropertySerialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean(42, null);

        JsonParser p = mapper.getFactory().createParser(mapper.writeValueAsString(bean));
        TestBean deserialized = mapper.readValue(p, TestBean.class);
        assertEquals(bean.getPublicField(), deserialized.getPublicField());
        assertEquals(bean.getPrivateField(), deserialized.getPrivateField());
    }

    @org.junit.Test
    public void testFieldPropertyNullValueProvider() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean(null, null);

        JsonParser p = mapper.getFactory().createParser("{\"publicField\": null, \"_privateField\": null}");
        Object readValue = mapper.readValue(p, TestBean.class);
        assertNull(readValue.getPublicField());
        assertNull(readValue.getPrivateField());
    }

    @org.junit.Test
    public void testFieldPropertyTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerTypeDeserializer(TestBean.class, new TestBean.TestTypeDeserializer());

        TestBean bean = new TestBean(42, null);

        JsonParser p = mapper.getFactory().createParser("{\"publicField\": 42, \"_privateField\": 123}");
        Object readValue = mapper.readValue(p, TestBean.class);
        assertEquals(bean.getPublicField(), readValue.getPublicField());
        assertEquals(bean.getPrivateField(), readValue.getPrivateField());
    }
}