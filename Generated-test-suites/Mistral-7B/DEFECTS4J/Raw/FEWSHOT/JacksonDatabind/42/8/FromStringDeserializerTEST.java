package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertEquals(new File("test.txt"), deserializer.deserialize(mapper.getFactory(), mapper.getDeserializationContext(), "test.txt"));
    }

    @Test
    public void testURLDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        assertEquals(new URL("http://example.com"), deserializer.deserialize(mapper.getFactory(), mapper.getDeserializationContext(), "http://example.com"));
    }

    @Test
    public void testURIDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        assertEquals(new URI("http://example.com"), deserializer.deserialize(mapper.getFactory(), mapper.getDeserializationContext(), "http://example.com"));
    }

    @Test
    public void testClassDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertEquals("String", deserializer.deserialize(mapper.getFactory(), mapper.getDeserializationContext(), "String.class"));
    }

    @Test
    public void testJavaTypeDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        assertEquals(javaType, deserializer.deserialize(mapper.getFactory(), mapper.getDeserializationContext(), "com.fasterxml.jackson.core.type.TypeReference<java.lang.String>"));
    }

    @Test
    public void testCurrencyDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        assertEquals(Currency.USD, deserializer.deserialize(mapper.getFactory(), mapper.getDeserializationContext(), "USD"));
    }

    @Test
    public void testPatternDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        assertEquals(Pattern.compile("(.*?)"), deserializer.deserialize(mapper.getFactory(), mapper.getDeserializationContext(), "(.*?)"));
    }

    @Test
    public void testLocaleDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertEquals(Locale.US, deserializer.deserialize(mapper.getFactory(), mapper.getDeserializationContext(), "US"));
        assertEquals(Locale.CHINA, deserializer.deserialize(mapper.getFactory(), mapper.getDeserialationContext(), "zh_CN"));
        assertEquals(Locale.ITALY, deserializer.deserialize(mapper.getFactory(), mapper.getDeserialationContext(), "it_IT"));
    }

    @Test
    public void testCharsetDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        assertEquals(Charset.forName("UTF-8"), deserializer.deserialize(mapper.getFactory(), mapper.getDeserialationContext(), "UTF-8"));
    }

    @Test
    public void testTimeZoneDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        assertEquals(TimeZone.getTimeZone("UTC"), deserializer.deserialize(mapper.getFactory(), mapper.getDeserialationContext(), "UTC"));
        assertEquals(TimeZone.getTimeZone("America/New_York"), deserializer.deserialize(mapper.getFactory(), mapper.getDeserialationContext(), "America/New_York"));
    }

    @Test
    public void testInetAddressDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        assertEquals(InetAddress.getByName("192.168.1.1"), deserializer.deserialize(mapper.getFactory(), mapper.getDeserialationContext(), "192.168.1.1"));
    }

    @Test
    public void testInetSocketAddressDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        assertEquals(new InetSocketAddress("localhost", 80), deserializer.deserialize(mapper.getFactory(), mapper.getDeserialationContext(), "localhost:80"));
        assertEquals(new InetSocketAddress("255.255.255.255", 65535), deserializer.deserialize(mapper.getFactory(), mapper.getDeserialationContext(), "[255.255.255.255]:65535"));
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        deserializer.deserialize(mapper.getFactory(), mapper.getDeserialationContext(), "123");
    }
}