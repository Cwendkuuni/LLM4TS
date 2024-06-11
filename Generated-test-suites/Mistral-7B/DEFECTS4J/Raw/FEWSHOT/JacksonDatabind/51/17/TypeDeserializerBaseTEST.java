package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mapper.getTypeFactory();
        JavaType javaTypeInt = mapper.getTypeFactory().constructType(int.class);
        JavaType javaTypeString = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializer = new BaseTypeDeserializer(javaTypeInt, idResolver, null, false, javaTypeString);

        DeserializationContext ctxt = mapper.getConfig().buildContextualDeserializerProvider().findValueDeserializer(JsonToken.VALUE_NUMBER_INT, jsonParser -> jsonParser.getCurrentName(), null);
        JsonDeserializer<Object> deserializerInt = typeDeserializer._findDeserializer(ctxt, "INT");
        assertEquals(ctxt.findContextualValueDeserializer(javaTypeInt, null), deserializerInt);

        JsonDeserializer<Object> deserializerString = typeDeserializer._findDeserializer(ctxt, "STRING");
        assertEquals(ctxt.findContextualValueDeserializer(javaTypeString, null), deserializerString);

        JsonDeserializer<Object> deserializerNull = typeDeserializer._findDeserializer(ctxt, "NULL");
        assertEquals(NullifyingDeserializer.instance, deserializerNull);

        JsonDeserializer<Object> deserializerDefault = typeDeserializer._findDeserializer(ctxt, "DEFAULT");
        assertNull(deserializerDefault);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mapper.getTypeFactory();
        JavaType javaTypeString = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializer = new BaseTypeDeserializer(null, idResolver, null, false, javaTypeString);

        DeserializationContext ctxt = mapper.getConfig().buildContextualDeserializerProvider();
        JsonDeserializer<Object> deserializerString = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertEquals(ctxt.findContextualValueDeserializer(javaTypeString, null), deserializerString);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeIdResolver idResolver = mapper.getTypeFactory();
        JavaType javaTypeInt = mapper.getTypeFactory().constructType(int.class);
        TypeDeserializerBase typeDeserializer = new BaseTypeDeserializer(javaTypeInt, idResolver, null, false, null);

        DeserializationContext ctxt = mapper.getConfig().buildContextualDeserializerProvider();
        try {
            typeDeserializer._handleUnknownTypeId(ctxt, "UNKNOWN", idResolver, javaTypeInt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unknown type id: UNKNOWN for type: class java.lang.Integer", e.getMessage());
        }
    }

    static class BaseTypeDeserializer extends TypeDeserializerBase {
        BaseTypeDeserializer(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        BaseTypeDeserializer(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            throw new UnsupportedOperationException();
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            throw new UnsupportedOperationException();
        }
    }
}