package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

    private AsWrapperTypeDeserializer typeDeserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;

    @Before
    public void setUp() {
        JavaType baseType = new SimpleType(Object.class);
        TypeIdResolver idResolver = new TestTypeIdResolver();
        String typePropertyName = "foo";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        typeDeserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        jsonParser = new ObjectMapper().getFactory().createParser("{}");
        deserializationContext = new ObjectMapper().reader().createDeserializationContext(jsonParser, null);
    }

    @Test
    public void testConstructor() {
        assertEquals(new SimpleType(Object.class), typeDeserializer.baseType());
        assertEquals(As.WRAPPER_OBJECT, typeDeserializer.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        Object result = typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        Object result = typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext);

        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        Object result = typeDeserializer.deserializeTypedFromScalar(jsonParser, deserializationContext);

        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        Object result = typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);

        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void test_deserialize_with_native_type_id() throws IOException {
        jsonParser.setCurrentToken(JsonToken.VALUE_STRING);
        jsonParser.setTypeId("nativeTypeId");
        jsonParser.nextToken();
        jsonParser.nextToken();
        
        Object result = typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void test_deserialize_with_native_type_id_throws_EOFException() throws IOException {
        jsonParser.setCurrentToken(JsonToken.VALUE_STRING);
        jsonParser.setTypeId("nativeTypeId");
        
        assertThrows(JsonEOFException.class, () -> {
            typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        });
    }

    @Test
    public void test_deserialize_with_missing_type_id() throws IOException {
        jsonParser.setCurrentToken(JsonToken.FIELD_NAME);
        
        assertThrows(MismatchedInputException.class, () -> {
            typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        });
    }

    @Test
    public void test_deserialize_with_invalid_type_id() throws IOException {
        jsonParser.setCurrentToken(JsonToken.FIELD_NAME);
        jsonParser.setText("invalidTypeId");
        
        assertThrows(IllegalArgumentException.class, () -> {
            typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        });
    }

    @Test
    public void test_deserialize_with_type_id_visible() throws IOException {
        typeDeserializer = new AsWrapperTypeDeserializer(new SimpleType(Object.class), new TestTypeIdResolver(),
                "bar", true, Object.class);

        jsonParser = new ObjectMapper().getFactory().createParser("{\"bar\":\"typeId\",\"foo\":\"value\"}");
        deserializationContext = new ObjectMapper().reader().createDeserializationContext(jsonParser, null);

        Object result = typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void test_deserialize_with_type_id_visible_skips() throws IOException {
        typeDeserializer = new AsWrapperTypeDeserializer(new SimpleType(Object.class), new TestTypeIdResolver(),
                "bar", true, Object.class);

        jsonParser = new ObjectMapper().getFactory().createParser("{\"baz\":\"typeId\",\"foo\":\"value\"}");
        deserializationContext = new ObjectMapper().reader().createDeserializationContext(jsonParser, null);

        Object result = typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void test_deserialize_with_type_id_visible_throws() throws IOException {
        typeDeserializer = new AsWrapperTypeDeserializer(new SimpleType(Object.class), new TestTypeIdResolver(),
                "bar", true, Object.class);

        jsonParser = new ObjectMapper().getFactory().createParser("{\"name\":\"typeId\",\"foo\":\"value\"}");
        deserializationContext = new ObjectMapper().reader().createDeserializationContext(jsonParser, null);

        assertThrows(MismatchedInputException.class, () -> {
            typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        });
    }

    @Test
    public void test_deserialize_with_type_id_invisible() throws IOException {
        typeDeserializer = new AsWrapperTypeDeserializer(new SimpleType(Object.class), new TestTypeIdResolver(),
                "bar", false, Object.class);

        jsonParser = new ObjectMapper().getFactory().createParser("{\"name\":\"typeId\",\"foo\":\"value\"}");
        deserializationContext = new ObjectMapper().reader().createDeserializationContext(jsonParser, null);

        Object result = typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        assertEquals(Object.class, result.getClass());
    }

    private static class TestTypeIdResolver implements TypeIdResolver {
        @Override
        public void init(JavaType baseType) {
            // no-op
        }

        @Override
        public String idFromValue(Object value) {
            return value.getClass().getSimpleName();
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> suggestedType) {
            return suggestedType.getSimpleName();
        }

        @Override
        public String idFromBaseType() {
            return baseTypeName();
        }

        @Override
        public String getDescForKnownTypeIds() {
            return baseTypeName();
        }

        @Override
        public JavaType typeFromId(DatabindContext context, String id) {
            return null;
        }

        @Override
        public JavaType typeFromId(DatabindContext context, String id, JsonDeserializer<?> deserializer) {
            return null;
        }

        private String baseTypeName() {
            return "Object";
        }
    }
}