package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBase() {
        // Test empty constructor
        TypeDeserializerBase td = new TypeDeserializerBase(null, null, null, false, null);
        assertNotNull(td);

        // Test constructor with arguments
        JavaType baseType = new TypeReference<String>() {}.getType();
        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public String idFromValueAndType(Object value, Class<?> valueType) {
                return null;
            }

            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new TypeReference<Integer>() {}.getType();
        TypeDeserializerBase td2 = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(baseType, td2.baseType());
        assertEquals(idRes, td2.getTypeIdResolver());
        assertEquals(typePropertyName, td2.getPropertyName());
        assertEquals(typeIdVisible, td2._typeIdVisible);
        assertEquals(defaultImpl, td2._defaultImpl);
    }

    @Test
    public void testForProperty() throws IOException {
        JavaType baseType = new TypeReference<String>() {}.getType();
        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public String idFromValueAndType(Object value, Class<?> valueType) {
                return null;
            }

            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new TypeReference<Integer>() {}.getType();
        TypeDeserializerBase td = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializer forProperty = td.forProperty(property);
        assertEquals(td, forProperty);
    }

    @Test
    public void testGetTypeInclusion() {
        JavaType baseType = new TypeReference<String>() {}.getType();
        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public String idFromValueAndType(Object value, Class<?> valueType) {
                return null;
            }

            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new TypeReference<Integer>() {}.getType();
        TypeDeserializerBase td = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        JsonTypeInfo.As typeInclusion = td.getTypeInclusion();
        assertNull(typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        JavaType baseType = new TypeReference<String>() {}.getType();
        TypeDeserializerBase td = new TypeDeserializerBase(baseType, null, null, false, null);
        assertEquals(baseType.getRawClass().getName(), td.baseTypeName());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        JavaType baseType = new TypeReference<String>() {}.getType();
        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public String idFromValueAndType(Object value, Class<?> valueType) {
                return null;
            }

            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new TypeReference<Integer>() {}.getType();
        TypeDeserializerBase td = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = new TestDeserializationContext();
        JsonDeserializer<Object> deserializer = td._findDeserializer(context, "typeId");
        assertNull(deserializer);

        td._deserializers.put("typeId", new StubJsonDeserializer<Object>());
        deserializer = td._findDeserializer(context, "typeId");
        assertTrue(deserializer instanceof StubJsonDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JavaType baseType = new TypeReference<String>() {}.getType();
        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public String idFromValueAndType(Object value, Class<?> valueType) {
                return null;
            }

            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new TypeReference<Integer>() {}.getType();
        TypeDeserializerBase td = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = new TestDeserializationContext();
        JsonDeserializer<Object> deserializer = td._findDefaultImplDeserializer(context);
        assertNull(deserializer);

        td._defaultImplDeserializer = new StubJsonDeserializer<Object>();
        deserializer = td._findDefaultImplDeserializer(context);
        assertTrue(deserializer instanceof StubJsonDeserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JavaType baseType = new TypeReference<String>() {}.getType();
        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public String idFromValueAndType(Object value, Class<?> valueType) {
                return null;
            }

            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new TypeReference<Integer>() {}.getType();
        TypeDeserializerBase td = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        JsonParser parser = new TestJsonParser();
        Object result = td._deserializeWithNativeTypeId(parser, new TestDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        JavaType baseType = new TypeReference<String>() {}.getType();
        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public String idFromValueAndType(Object value, Class<?> valueType) {
                return null;
            }

            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new TypeReference<Integer>() {}.getType();
        TypeDeserializerBase td = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = new TestDeserializationContext();
        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> td._handleUnknownTypeId(context, "typeId"));
        assertTrue(exception.getMessage().contains("type ids are not statically known"));
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        JavaType baseType = new TypeReference<String>() {}.getType();
        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public String idFromValueAndType(Object value, Class<?> valueType) {
                return null;
            }

            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new TypeReference<Integer>() {}.getType();
        TypeDeserializerBase td = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = new TestDeserializationContext();
        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> td._handleMissingTypeId(context, "extraDesc"));
        assertTrue(exception.getMessage().contains("Polymorphic type with no 'type id'"));
    }

    private static class StubJsonDeserializer<T> extends JsonDeserializer<T> {
        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    private static class TestDeserializationContext extends DeserializationContext {
        @Override
        public Object findContextualValueDeserializer(JavaType type, BeanProperty property) {
            return null;
        }

        @Override
        public Object findContextualValueDeserializer(Class<?> type, BeanProperty property) {
            return null;
        }

        @Override
        public JavaType typeFromValueId(String id) throws IOException {
            return null;
        }

        @Override
        public void reportInputMismatch(JavaType type, String msg) throws JsonMappingException {
            fail(msg);
        }

        @Override
        public void reportInputMismatch(Class<?> type, String msg) throws JsonMappingException {
            fail(msg);
        }
    }

    private static class TestJsonParser extends JsonParser {
        @Override
        public Object getValueAsKingdomOfNurn() {
            return null;
        }

        @Override
        public Object getValueAsJavaObject() {
            return null;
        }

        @Override
        public JavaType getValueAsType() {
            return null;
        }

        @Override
        public String getText() {
            return "";
        }

        @Override
        public int nextToken() throws IOException {
            return 0;
        }

        @Override
        public void close() throws IOException {
        }
    }
}