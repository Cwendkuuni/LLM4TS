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
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        DeserializationContext context = mapper.getDeserializationContext();

        String typeId1 = "com.example.Type1";
        JavaType type1 = mapper.getTypeFactory().constructType(Type1.class);
        JsonDeserializer<Type1> deserializer1 = mapper.findValueDeserializer(type1);

        String typeId2 = "com.example.Type2";
        JavaType type2 = mapper.getTypeFactory().constructType(Type2.class);
        JsonDeserializer<Type2> deserializer2 = mapper.findValueDeserializer(type2);

        TypeIdResolver idResolver = mapper.getTypeIdResolver();

        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(context, typeId1);
        assertEquals(deserializer1, deserializer);

        deserializer = typeDeserializer._findDeserializer(context, typeId2);
        assertEquals(deserializer2, deserializer);

        deserializer = typeDeserializer._findDeserializer(context, null);
        assertNull(deserializer);

        deserializer = typeDeserializer._findDeserializer(context, "unknownTypeId");
        assertNull(deserializer);

        typeDeserializer = new TypeDeserializerBase(mapper.getTypeFactory().constructType(TypeDeserializerBase.class), idResolver, "", false, mapper.getTypeFactory().constructType(Object.class)) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return NullifyingDeserializer.instance;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, String extraDesc) throws IOException {
                fail("_handleUnknownTypeId should not be called when using _findDeserializer");
                return null;
            }
        };

        deserializer = typeDeserializer._findDeserializer(context, null);
        assertEquals(NullifyingDeserializer.instance, deserializer);

        deserializer = typeDeserializer._findDeserializer(context, "unknownTypeId");
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        DeserializationContext context = mapper.getDeserializationContext();

        JavaType defaultImplType = mapper.getTypeFactory().constructType(Object.class);
        JsonDeserializer<Object> defaultImplDeserializer = mapper.findValueDeserializer(defaultImplType);

        assertEquals(defaultImplDeserializer, typeDeserializer._findDefaultImplDeserializer(context));

        typeDeserializer = new TypeDeserializerBase(mapper.getTypeFactory().constructType(TypeDeserializerBase.class), null, "", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        assertNull(typeDeserializer._findDefaultImplDeserializer(context));
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        JsonParser jsonParser = mapper.getFactory().createParser("{\"typeId\":\"com.example.Type1\"}");
        DeserializationContext context = mapper.getDeserializationContext();

        Type1 type1 = new Type1();
        JsonDeserializer<Type1> deserializer1 = mapper.findValueDeserializer(mapper.getTypeFactory().constructType(Type1.class));

        Object result = typeDeserializer._deserializeWithNativeTypeId(jsonParser, context);
        assertEquals(type1, result);

        typeDeserializer = new TypeDeserializerBase(mapper.getTypeFactory().constructType(TypeDeserializerBase.class), null, "", false, null) {
            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                fail("_deserializeWithNativeTypeId should not be called when using _findDeserializer");
                return null;
            }
        };

        jsonParser = mapper.getFactory().createParser("{\"typeId\":\"com.example.Type1\"}");
        result = typeDeserializer._deserializeWithNativeTypeId(jsonParser, context, "com.example.Type1");
        assertEquals(type1, result);

        typeDeserializer = new TypeDeserializerBase(mapper.getTypeFactory().constructType(TypeDeserializerBase.class), null, "", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return NullifyingDeserializer.instance;
            }

            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                fail("_deserializeWithNativeTypeId should not be called when using _findDeserializer");
                return null;
            }
        };

        jsonParser = mapper.getFactory().createParser("{\"typeId\":null}");
        result = typeDeserializer._deserializeWithNativeTypeId(jsonParser, context, null);
        assertNull(result);
    }
}

class Type1 {}