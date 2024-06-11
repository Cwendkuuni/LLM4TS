package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeFactory(), "", false, mapper.getTypeFactory().constructType(String.class));

        TypeIdResolver idRes = new StubTypeIdResolver();
        assertNull(typeDeserializerBase._handleUnknownTypeId(new DeserializationContext(mapper, idRes), "unknownTypeId", idRes, typeDeserializerBase._baseType));
    }

    @Test
    public void testFindDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeFactory(), "", false, mapper.getTypeFactory().constructType(String.class));

        TypeIdResolver idRes = new StubTypeIdResolver();
        idRes.putIdMapping(mapper.getTypeFactory().constructType(Integer.class), "intTypeId");
        JsonDeserializer<Object> jsonDeserializer = new StubJsonDeserializer();
        idRes.putDeserializerMapping("intTypeId", jsonDeserializer);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(new DeserializationContext(mapper, idRes), "intTypeId");
        assertEquals(jsonDeserializer, deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeFactory(), "", false, mapper.getTypeFactory().constructType(String.class));

        DeserializationContext ctxt = new DeserializationContext(mapper, null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNull(deserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeFactory(), "", false, mapper.getTypeFactory().constructType(String.class));

        JsonParser parser = mapper.getFactory().createParser("{\"typeId\":\"StringTypeId\", \"value\":\"test\"}");
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(parser, new DeserializationContext(mapper, null), "StringTypeId");
        assertEquals("test", result);
    }

    static class BaseTypeDeserializer extends TypeDeserializerBase {
        public BaseTypeDeserializer(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        BaseTypeDeserializer(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new BaseTypeDeserializer(this, prop);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.EXTERNAL_PROPERTY;
        }
    }

    static class StubJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "StubJsonDeserializer";
        }
    }

    static class StubTypeIdResolver extends TypeIdResolverBase {
        @Override
        public String getDescForKnownTypeIds() {
            return "";
        }

        public void putIdMapping(JavaType type, String typeId) {
            // empty
        }

        public void putDeserializerMapping(String typeId, JsonDeserializer<?> deserializer) {
            _deserializers.put(typeId, deserializer);
        }
    }
}