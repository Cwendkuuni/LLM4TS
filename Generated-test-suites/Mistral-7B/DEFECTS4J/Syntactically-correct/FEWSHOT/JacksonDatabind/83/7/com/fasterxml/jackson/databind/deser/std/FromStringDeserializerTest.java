package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"/path/to/file.txt\"");
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"http://example.com\"");
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeURI() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"urn:example:com:id\"");
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        assertEquals("urn:example:com:id", result.toString());
    }

    @Test
    public void testDeserializeClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"java.lang.String\"");
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"com.example.MyType\"");
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        assertEquals(JavaType.constructType(MyType.class), result);
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"USD\"");
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"/[a-z]+/[0-9]+\"");
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        assertEquals("/[a-z]+/[0-9]+", result.pattern());
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"en_US\"");
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        assertEquals("en_US", result.toString());
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"UTF-8\"");
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        assertEquals("UTF-8", result.name());
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"America/Los_Angeles\"");
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        assertEquals("America/Los_Angeles", result.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"192.168.1.1\"");
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        assertEquals("192.168.1.1", result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80\"");
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());

        node = mapper.readTree("\"localhost:8080\"");
        result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        assertEquals("localhost", result.getAddress().getHostAddress());
        assertEquals(8080, result.getPort());
    }

    @Test
    public void testDeserializeStringBuilder() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"Hello, World!\"");
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        assertEquals("Hello, World!", result.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"invalid\"");
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        fail("Should have thrown JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNullString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.createNullNode();
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        fail("Should have thrown JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"java.util.ArrayList\"");
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext(mapper, null, null));
        fail("Should have thrown JsonMappingException for unknown type: java.util.ArrayList");
    }
}