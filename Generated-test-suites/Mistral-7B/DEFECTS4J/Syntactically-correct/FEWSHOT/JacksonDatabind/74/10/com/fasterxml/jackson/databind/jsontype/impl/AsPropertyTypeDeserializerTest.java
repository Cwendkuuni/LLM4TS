package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AsPropertyTypeDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeTypedFromObject_JsonObject() throws IOException {
        String json = "{\"typePropertyName\": \"MyType\", \"value\": 123}";
        ObjectNode node = mapper.readTree(json);
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), mapper.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(mapper.getFactory().createParser(node.traversingParser()), mapper.getDeserializationContext());
        assertInstanceOf(MyType.class, result);
    }

    @Test
    public void testDeserializeTypedFromObject_JsonArray() throws IOException {
        String json = "[1, \"typePropertyName\": \"MyType\"]";
        ObjectNode node = mapper.readTree(json);
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), mapper.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(mapper.getFactory().createParser(node.traversingParser()), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_Null() throws IOException {
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), mapper.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(null, mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_Empty() throws IOException {
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), mapper.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(mapper.getFactory().createParser(""), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_WrongPropertyName() throws IOException {
        String json = "{\"wrongPropertyName\": \"MyType\", \"value\": 123}";
        ObjectNode node = mapper.readTree(json);
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), mapper.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(mapper.getFactory().createParser(node.traversingParser()), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_MissingTypeProperty() throws IOException {
        String json = "{\"value\": 123}";
        ObjectNode node = mapper.readTree(json);
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), mapper.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(mapper.getFactory().createParser(node.traversingParser()), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_MissingValue() throws IOException {
        String json = "{\"typePropertyName\": \"MyType\"}";
        ObjectNode node = mapper.readTree(json);
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), mapper.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(mapper.getFactory().createParser(node.traversingParser()), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_EmptyTypeId() throws IOException {
        String json = "{\"typePropertyName\": \"\", \"value\": 123}";
        ObjectNode node = mapper.readTree(json);
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), mapper.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(mapper.getFactory().createParser(node.traversingParser()), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_NullTypeId() throws IOException {
        String json = "{\"typePropertyName\": null, \"value\": 123}";
        ObjectNode node = mapper.readTree(json);
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), mapper.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(mapper.getFactory().createParser(node.traversingParser()), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_EmptyValue() throws IOException {
        String json = "{\"typePropertyName\": \"MyType\", \"value\": \"\"}";
        ObjectNode node = mapper.readTree(json);
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), mapper.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(mapper.getFactory().createParser(node.traversingParser()), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_InvalidTypeId() throws IOException {
        String json = "{\"typePropertyName\": \"MyType\", \"value\": \"invalidTypeId\"}";
        ObjectNode node = mapper.readTree(json);
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), mapper.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(mapper.getFactory().createParser(node.traversingParser()), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_NoTypeIdVisible() throws IOException {
        String json = "{\"typePropertyName\": \"MyType\", \"value\": 123}";
        ObjectNode node = mapper.readTree(json);
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), mapper.getTypeIdResolver(), "typePropertyName", false, null, As.PROPERTY).deserializeTypedFromObject(mapper.getFactory().createParser(node.traversingParser()), mapper.getDeserializationContext());
        assertEquals(MyType.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromObject_NoDefaultImpl() throws IOException {
        String json = "{\"typePropertyName\": \"MyType\", \"value\": 123}";
        ObjectNode node = mapper.readTree(json);
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), null, "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(mapper.getFactory().createParser(node.traversingParser()), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_DefaultImplNotFound() throws IOException {
        String json = "{\"typePropertyName\": \"MyType\", \"value\": 123}";
        ObjectNode node = mapper.readTree(json);
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), mapper.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY).deserializeTypedUsingDefaultImpl(mapper.getFactory().createParser(node.traversingParser()), mapper.getDeserializationContext(), null);
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_DefaultImplFound() throws IOException {
        String json = "[1, \"typePropertyName\": \"MyType\"]";
        ObjectNode node = mapper.readTree(json);
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), mapper.getTypeIdResolver(), "typePropertyName", true, mapper.getTypeFactory().constructType(DefaultImplType.class), As.PROPERTY).deserializeTypedUsingDefaultImpl(mapper.getFactory().createParser(node.traversingParser()), mapper.getDeserializationContext(), null);
        assertInstanceOf(DefaultImplType.class, result);
    }

    @Test
    public void testDeserializeTypedFromObject_NaturalType() throws IOException {
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), mapper.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(mapper.getFactory().createParser("123"), mapper.getDeserializationContext());
        assertEquals(123, result.hashCode());
    }

    @Test
    public void testDeserializeTypedFromObject_StartArray() throws IOException {
        // This should be handled as if "as-wrapper-array" was requested
        Object result = new AsPropertyTypeDeserializer(mapper.getTypeFactory().constructType(MyType.class), mapper.getTypeIdResolver(), "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromAny(mapper.getFactory().createParser("[1, \"typePropertyName\": \"MyType\"]"), mapper.getDeserializationContext());
        assertInstanceOf(MyType[].class, result.getClass());
    }

    private static class MyType {}
    private static class DefaultImplType {}
}