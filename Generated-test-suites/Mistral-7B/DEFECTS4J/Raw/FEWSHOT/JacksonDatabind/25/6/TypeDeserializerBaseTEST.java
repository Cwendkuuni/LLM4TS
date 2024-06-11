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
    public void testFindDeserializerWithNullTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        DeserializationContext ctxt = mapper.getConfiguration().contextInstance();
        JsonDeserializer<Object> deser = deserializer._findDeserializer(ctxt, null);
        assertEquals(NullifyingDeserializer.instance, deser);
    }

    @Test
    public void testFindDeserializerWithEmptyTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        DeserializationContext ctxt = mapper.getConfiguration().contextInstance();
        JsonDeserializer<Object> deser = deserializer._findDeserializer(ctxt, "");
        assertNull(deser);
    }

    @Test
    public void testFindDeserializerWithTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        DeserializationContext ctxt = mapper.getConfiguration().contextInstance();
        String typeId = "com.example.MyType";
        TypeIdResolver idResolver = mapper.getTypeFactory();
        JsonDeserializer<Object> deser = deserializer._findDeserializer(ctxt, typeId);
        assertNotNull(deser);
        Class<?> deserClass = deser.getClass();
        assertTrue(deserClass.getPackage().getName().equals("com.example"));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        DeserializationContext ctxt = mapper.getConfiguration().contextInstance();
        JsonDeserializer<Object> deser = deserializer._findDefaultImplDeserializer(ctxt);
        assertNull(deser);
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImpl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeDeserializerBase subclass = mapper.getTypeFactory().constructType(SubclassTypeDeserializerBase.class);
        DeserializationContext ctxt = mapper.getConfiguration().contextInstance();
        JsonDeserializer<Object> deser = deserializer._findDefaultImplDeserializer(ctxt);
        assertNull(deser);
        deserializer = subclass;
        synchronized (deserializer) {
            deser = deserializer._defaultImplDeserializer;
            assertNotNull(deser);
        }
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        DeserializationContext ctxt = mapper.getConfiguration().contextInstance();
        String typeId = "unknown.type";
        deserializer._handleUnknownTypeId(ctxt, typeId, null, null);
        fail("Expected IOException");
    }
}

class SubclassTypeDeserializerBase extends TypeDeserializerBase {
    public SubclassTypeDeserializerBase() {
        super(null, null, null, false, MyDefaultImpl.class);
    }
    
    static class MyDefaultImpl {
    }
}