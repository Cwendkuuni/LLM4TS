package com.example.tests;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_String_ReturnsExpectedObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(FromStringDeserializer.class, String.class);
        FromStringDeserializer deserializer = mapper.findValueDeserializer(String.class);

        JsonParser parser = mapper.getFactory().createParser("\"expectedValue\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = deserializer.deserialize(parser, ctxt);

        assertEquals("expectedValue", result);
    }

    @Test
    public void testDeserialize_EmptyString_ReturnsExpectedObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(FromStringDeserializer.class, String.class);
        FromStringDeserializer deserializer = mapper.findValueDeserializer(String.class);

        JsonParser parser = mapper.getFactory().createParser("\"\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = deserializer.deserialize(parser, ctxt);

        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidString_ThrowsJsonMappingException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(FromStringDeserializer.class, String.class);
        FromStringDeserializer deserializer = mapper.findValueDeserializer(String.class);

        JsonParser parser = mapper.getFactory().createParser("\"invalidValue\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        deserializer.deserialize(parser, ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeArray_ThrowsJsonMappingException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(FromStringDeserializer.class, File.class);
        FromStringDeserializer deserializer = mapper.findValueDeserializer(File.class);

        JsonParser parser = mapper.getFactory().createParser("[\"" + File.class.getName() + "\",\"filePath\"]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        deserializer.deserialize(parser, ctxt);
    }

    @Test
    public void testDeserializeEmbeddedObject_ReturnsExpectedObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(FromStringDeserializer.class, String.class);
        FromStringDeserializer deserializer = mapper.findValueDeserializer(String.class);

        Object ob = new Object();
        String jsonString = "{\"className\":\"" + String.class.getName() + "\",\"value\":\"expectedValue\"}";
        JsonParser parser = mapper.getFactory().createParser(jsonString);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = deserializer._deserializeEmbedded(ob, ctxt);

        assertEquals("expectedValue", result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedObject_ThrowsJsonMappingException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(FromStringDeserializer.class, String.class);
        FromStringDeserializer deserializer = mapper.findValueDeserializer(String.class);

        Object ob = new Object();
        String jsonString = "{\"className\":\"InvalidClass\",\"value\":\"expectedValue\"}";
        JsonParser parser = mapper.getFactory().createParser(jsonString);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        deserializer._deserializeEmbedded(ob, ctxt);
    }

    @Test
    public void testTypes_ReturnsExpectedClasses() {
        Class<?>[] types = FromStringDeserializer.types();

        assertEquals(File.class, types[0]);
        assertEquals(URL.class, types[1]);
        assertEquals(URI.class, types[2]);
        assertEquals(Class.class, types[3]);
        assertEquals(JavaType.class, types[4]);
        assertEquals(Currency.class, types[5]);
        assertEquals(Pattern.class, types[6]);
        assertEquals(Locale.class, types[7]);
        assertEquals(Charset.class, types[8]);
        assertEquals(TimeZone.class, types[9]);
        assertEquals(InetAddress.class, types[10]);
        assertEquals(InetSocketAddress.class, types[11]);
    }
}