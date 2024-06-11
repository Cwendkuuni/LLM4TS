
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserializationWithDefaultImpl_NullInput_ReturnNull() throws Exception {
        TypeDeserializerBase deserializer = new MockDeserializer(null, null);
        Object result = deserializer._deserializeWithNativeTypeId(null, null, null);
        assertNull(result);
    }

    @Test(expected = Exception.class)
    public void testDeserializationWithDefaultImpl_NoTypeIdFound_ThrowException() throws Exception {
        TypeDeserializerBase deserializer = new MockDeserializer(null, null);
        deserializer._deserializeWithNativeTypeId(null, null, "unknownType");
    }

    @Test
    public void testDeserializationWithDefaultImpl_ValidTypeId_ReturnDeserializedObject() throws Exception {
        TypeDeserializerBase deserializer = new MockDeserializer(null, "{\"property\":\"value\"}");
        Object result = deserializer._deserializeWithNativeTypeId(null, null, "knownType");
        assertNotNull(result);
        assertEquals(MyObject.class, result.getClass());
        assertEquals("value", ((MyObject) result).getProperty());
    }

    private static class MockDeserializer extends TypeDeserializerBase {
        private final String json;

        protected MockDeserializer(JavaType baseType, String json) {
            super(baseType, null, null, false, null);
            this.json = json;
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
            if ("knownType".equals(typeId)) {
                return new StdDeserializer<MyObject>(MyObject.class) {
                    @Override
                    public MyObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return new MyObject(p.readValueAs(MyObject.class));
                    }
                };
            } else {
                throw new IOException("Unknown type id: " + typeId);
            }
        }

        private static class MyObject {
            private final String property;

            public MyObject(String property) {
                this.property = property;
            }

            public String getProperty() {
                return property;
            }
        }
    }
}

