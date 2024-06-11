package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JsonParserSequence;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        String json = "{\"type\":\"MyType\", \"data\":123}";
        JavaType javaType = mapper.getTypeFactory().constructType(MyType.class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "type", false, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
        assertEquals(MyType.class, result.getClass());
        assertEquals(123, result.toString().toInt());
    }

    @Test
    public void testDeserializeTypedFromObjectWithDefaultImpl() throws Exception {
        String json = "{\"data\":123}";
        JavaType javaType = mapper.getTypeFactory().constructType(MyTypeWithDefaultImpl.class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "type", false, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
        assertEquals(MyTypeWithDefaultImpl.class, result.getClass());
        assertEquals(123, result.toString().toInt());
    }

    @Test
    public void testDeserializeTypedFromObjectWithNativeTypeId() throws Exception {
        String json = "{\"@type\":\"MyType\", \"data\":123}";
        JavaType javaType = mapper.getTypeFactory().constructType(MyType.class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), null, false, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
        assertEquals(MyType.class, result.getClass());
        assertEquals(123, result.toString().toInt());
    }

    @Test
    public void testDeserializeTypedFromObjectMissingTypeProperty() throws Exception {
        String json = "{\"data\":123}";
        JavaType javaType = mapper.getTypeFactory().constructType(MyType.class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "type", false, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser(json);
        try {
            deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            assertEquals("missing property 'type' that is to contain type id  (for class MyType)", e.getMessage());
        }
    }

    @Test
    public void testDeserializeTypedFromObjectNestedJson() throws Exception {
        String json = "{\"data\":{\"type\":\"MyType\", \"data\":123}}";
        JavaType javaType = mapper.getTypeFactory().constructType(MyType.class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "data.type", false, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
        assertEquals(MyType.class, result.getClass());
        assertEquals(123, result.toString().toInt());
    }

    @Test
    public void testDeserializeTypedFromObjectArray() throws Exception {
        String json = "[{\"type\":\"MyType1\", \"data\":1}, {\"type\":\"MyType2\", \"data\":2}]";
        TypeReference<List<Object>> javaType = new TypeReference<List<Object>>() {}.getType();
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "type", false, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
        assertEquals(List.class, result.getClass());
        List<?> list = (List<?>) result;
        assertEquals(2, list.size());
        assertEquals(MyType1.class, list.get(0).getClass());
        assertEquals(MyType2.class, list.get(1).getClass());
    }

    @Test
    public void testDeserializeTypedFromObjectWithTokenBuffer() throws Exception {
        String json = "{\"type\":\"MyType\", \"data\":123}";
        JavaType javaType = mapper.getTypeFactory().constructType(MyType.class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "type", false, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser(json);
        TokenBuffer tokenBuffer = new TokenBuffer(parser, mapper.getDeserializationContext());
        deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
        ObjectNode node = mapper.readValue(tokenBuffer.asParser(parser), ObjectNode.class);
        assertEquals("MyType", node.get("type").asText());
    }

    @Test
    public void testDeserializeTypedFromObjectWithJsonParserSequence() throws Exception {
        String json = "{\"type\":\"MyType\", \"data\":123}";
        JavaType javaType = mapper.getTypeFactory().constructType(MyType.class);
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "type", false, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonParserSequence parserSequence = new JsonParserSequence(parser, new TokenBuffer(parser, mapper.getDeserializationContext()));
        Object result = deserializer.deserializeTypedFromObject(parserSequence, mapper.getDeserializationContext());
        assertEquals(MyType.class, result.getClass());
        assertEquals(123, result.toString().toInt());
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        String json = "[{\"type\":\"MyType1\", \"data\":1}, {\"type\":\"MyType2\", \"data\":2}]";
        TypeReference<List<Object>> javaType = new TypeReference<List<Object>>() {}.getType();
        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), null, false, null, As.PROPERTY);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromArray(parser, mapper.getDeserializationContext());
        assertEquals(List.class, result.getClass());
        List<?> list = (List<?>) result;
        assertEquals(2, list.size());
        assertEquals(MyType1.class, list.get(0).getClass());
        assertEquals(MyType2.class, list.get(1).getClass());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String json = "{\"type\":\"MyType\", \"data\":123}";
        JavaType javaType = mapper.getTypeFactory().constructType(MyType.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonDeserializer<MyType> myTypeDeserializer = objectMapper.deserializerForClass(MyType.class);

        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "type", false, null, As.PROPERTY);
        JsonDeserializer<Object> foundDeserializer = deserializer.findDeserializer(mapper.getDeserializationContext(), "MyType");
        assertEquals(myTypeDeserializer, foundDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        String json = "{\"data\":123}";
        JavaType javaType = mapper.getTypeFactory().constructType(MyTypeWithDefaultImpl.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonDeserializer<MyTypeWithDefaultImpl> myTypeWithDefaultImplDeserializer = objectMapper.deserializerForClass(MyTypeWithDefaultImpl.class);

        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "type", false, null, As.PROPERTY);
        JsonDeserializer<Object> foundDeserializer = deserializer.findDefaultImplDeserializer(mapper.getDeserializationContext());
        assertEquals(myTypeWithDefaultImplDeserializer, foundDeserializer);
    }

    @Test
    public void testDeserializeIfNatural() throws Exception {
        String jsonNumber = "123";
        String jsonBooleanTrue = "true";
        String jsonBooleanFalse = "false";
        String jsonString = "Hello World!";

        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(MyType.class);

        TypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "type", false, null, As.PROPERTY);

        JsonParser parserNumber = mapper.getFactory().createParser(jsonNumber);
        Object resultNumber = deserializer.deserializeIfNatural(parserNumber, mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(int.class));
        assertEquals(123, resultNumber);

        JsonParser parserBooleanTrue = mapper.getFactory().createParser(jsonBooleanTrue);
        Object resultBooleanTrue = deserializer.deserializeIfNatural(parserBooleanTrue, mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(boolean.class));
        assertTrue((boolean) resultBooleanTrue);

        JsonParser parserBooleanFalse = mapper.getFactory().createParser(jsonBooleanFalse);
        Object resultBooleanFalse = deserializer.deserializeIfNatural(parserBooleanFalse, mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(boolean.class));
        assertFalse((boolean) resultBooleanFalse);

        JsonParser parserString = mapper.getFactory().createParser(jsonString);
        Object resultString = deserializer.deserializeIfNatural(parserString, mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(String.class));
        assertEquals(jsonString, resultString.toString());
    }
}

class MyType {
    private int data;

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}

class MyTypeWithDefaultImpl {
    private int data;

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public static MyTypeWithDefaultImpl defaultImpl() {
        return new MyTypeWithDefaultImpl();
    }
}