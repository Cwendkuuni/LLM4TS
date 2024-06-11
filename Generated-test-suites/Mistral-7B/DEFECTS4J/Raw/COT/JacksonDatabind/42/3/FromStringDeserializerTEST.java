package com.example; // replace with the actual package name

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFileDeserialization() throws JsonMappingException, IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertEquals(new File("test.txt"), deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, "test.txt"));
    }

    @Test
    public void testUrlDeserialization() throws JsonMappingException, IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        assertEquals(new URL("http://www.example.com"), deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, "http://www.example.com"));
    }

    @Test
    public void testUriDeserialization() throws JsonMappingException, IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        assertEquals(new URI("http://www.example.com"), deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, "http://www.example.com"));
    }

    @Test
    public void testClassDeserialization() throws JsonMappingException, IOException, ClassNotFoundException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertEquals("test", deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, "test"));

        Class<?> clazz = Class.forName("java.lang.String");
        assertEquals(clazz, deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, "java.lang.String"));
    }

    @Test
    public void testJavaTypeDeserialization() throws JsonMappingException, IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(int[].class);
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(javaType.getClass());
        assertEquals(javaType, deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, javaType.getClass().getName()));
    }

    @Test
    public void testCurrencyDeserialization() throws JsonMappingException, IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        assertEquals(Currency.getInstance("EUR"), deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, "EUR"));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidPatternDeserialization() throws JsonMappingException, IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, "invalid_pattern");
    }

    @Test
    public void testLocaleDeserialization() throws JsonMappingException, IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertEquals(Locale.US, deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, "US"));
        assertEquals(Locale.FRANCE, deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, "fr_FR"));
        assertEquals(Locale.ITALY, deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, "it_IT"));
    }

    @Test
    public void testCharsetDeserialization() throws JsonMappingException, IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        assertEquals(Charset.forName("UTF-8"), deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, "UTF-8"));
    }

    @Test
    public void testTimeZoneDeserialization() throws JsonMappingException, IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        assertEquals(TimeZone.getTimeZone("UTC"), deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, "UTC"));
    }

    @Test
    public void testInetAddressDeserialization() throws JsonMappingException, IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        assertEquals(InetAddress.getByName("127.0.0.1"), deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, "127.0.0.1"));
    }

    @Test
    public void testInetSocketAddressDeserialization() throws JsonMappingException, IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        assertEquals(new InetSocketAddress("localhost", 80), deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, "[::1]:80"));
        assertEquals(new InetSocketAddress("localhost", 80), deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, "localhost:80"));
    }

    @Test
    public void testEmptyStringDeserialization() throws JsonMappingException, IOException {
        ForStringDeserializer<Object> deserializer = new FromStringDeserializer(Object.class);
        assertNull(deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, ""));
    }

    @Test(expected = JsonMappingException.class)
    public void testNullDeserialization() throws JsonMappingException, IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Object.class);
        deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserialization() throws JsonMappingException, IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Object.class);
        deserializer.deserialize(objectMapper.getFactory(), objectMapper, null, "invalid");
    }
}