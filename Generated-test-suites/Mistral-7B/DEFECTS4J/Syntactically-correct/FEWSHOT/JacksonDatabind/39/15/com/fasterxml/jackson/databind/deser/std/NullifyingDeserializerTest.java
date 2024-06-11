package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Assert;
import org.junit.Test;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserializeNullObject() throws IOException {
        JsonParser jsonParser = createParser();
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(jsonParser, new DeserializationContext(new ObjectMapper()));
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeNullObjectWithTypeDeserializer() throws IOException {
        JsonParser jsonParser = createParser();
        TypeDeserializer typeDeserializer = createTypeDeserializer();
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(new ObjectMapper()), typeDeserializer);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser jsonParser = createParser();
        jsonParser.nextToken(); // [
        JsonMappingException expectedException = assertThrows(JsonMappingException.class, () -> NullifyingDeserializer.instance.deserialize(jsonParser, new DeserializationContext(new ObjectMapper())));
        Assert.assertEquals("Cannot construct instance of null Java class", expectedException.getMessage());
        jsonParser.close();
    }

    @Test
    public void testDeserializeEmptyObject() throws IOException {
        JsonParser jsonParser = createParser();
        jsonParser.nextToken(); // {
        JsonMappingException expectedException = assertThrows(JsonMappingException.class, () -> NullifyingDeserializer.instance.deserialize(jsonParser, new DeserializationContext(new ObjectMapper())));
        Assert.assertEquals("Cannot construct instance of null Java class", expectedException.getMessage());
        jsonParser.close();
    }

    @Test
    public void testDeserializeNonArrayOrObject() throws IOException {
        JsonParser jsonParser = createParser();
        jsonParser.nextValue(); // Integer.TYPE
        JsonMappingException expectedException = assertThrows(JsonMappingException.class, () -> NullifyingDeserializer.instance.deserialize(jsonParser, new DeserializationContext(new ObjectMapper())));
        Assert.assertEquals("Cannot construct value of type [simple type, class java.lang.Object] from value of type [simple type, class java.lang.Integer]", expectedException.getMessage());
        jsonParser.close();
    }

    @Test
    public void testDeserializeStartArray() throws IOException {
        JsonParser jsonParser = createParser();
        jsonParser.nextToken(); // [
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(new ObjectMapper()), createTypeDeserializer());
        Assert.assertNull(result);
        jsonParser.close();
    }

    @Test
    public void testDeserializeStartObject() throws IOException {
        JsonParser jsonParser = createParser();
        jsonParser.nextToken(); // {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(new ObjectMapper()), createTypeDeserializer());
        Assert.assertNull(result);
        jsonParser.close();
    }

    @Test
    public void testDeserializeFieldName() throws IOException {
        JsonParser jsonParser = createParser();
        jsonParser.nextToken(); // "name"
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(new ObjectMapper()), createTypeDeserializer());
        Assert.assertNull(result);
        jsonParser.close();
    }

    @Test
    public void testDeserializeValue() throws IOException {
        JsonParser jsonParser = createParser();
        jsonParser.nextValue(); // "value"
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(new ObjectMapper()), createTypeDeserializer());
        Assert.assertNull(result);
        jsonParser.close();
    }

    private JsonParser createParser() {
        return new JsonFactory().createParser("[]");
    }

    private TypeDeserializer createTypeDeserializer() {
        return new SimpleTypeDeserializer();
    }
}