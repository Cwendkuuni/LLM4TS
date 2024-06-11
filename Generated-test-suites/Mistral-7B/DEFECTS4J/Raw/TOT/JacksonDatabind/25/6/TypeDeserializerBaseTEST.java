package com.example.jackson.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    private ObjectMapper mapper;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, null);
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = mapper.findProperty("_propertyName", String.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
        assertEquals(TypeDeserializerBase.class, typeDeserializer.getClass());
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithNullTypeId() throws Exception {
        String json = "{}";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        typeDeserializerBase._findDeserializer(ctxt, null);
    }

    @Test
    public void testFindDeserializerWithKnownTypeId() throws Exception {
        String json = "{\"typeId\":\"StringDeserializer\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, "StringDeserializer");
        assertNotNull(deserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithUnknownTypeId() throws Exception {
        String json = "{\"typeId\":\"UnknownDeserializer\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        typeDeserializerBase._findDeserializer(ctxt, "UnknownDeserializer");
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNull(deserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "{}";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, ctxt);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithUnknownTypeId() throws Exception {
        String json = "{\"typeId\":\"UnknownDeserializer\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, ctxt);
    }
}