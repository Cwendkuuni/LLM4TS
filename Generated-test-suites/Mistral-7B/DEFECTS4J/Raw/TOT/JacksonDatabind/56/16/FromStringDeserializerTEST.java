package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserializeFile() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("\"/path/to/file.txt\"");
        StdDeserializer deserializer = new Std.StdFile();
        assertEquals(new File("/path/to/file.txt"), deserializer.deserialize(parser, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeURL() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("\"http://example.com/path/to/file.txt\"");
        StdDeserializer deserializer = new Std.StdURL();
        assertEquals(new URL("http://example.com/path/to/file.txt"), deserializer.deserialize(parser, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeURI() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("\"uri://example.com/path/to/file.txt\"");
        StdDeserializer deserializer = new Std.StdURI();
        assertEquals(new URI("uri://example.com/path/to/file.txt"), deserializer.deserialize(parser, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeClass() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("\"java.util.ArrayList\"");
        StdDeserializer deserializer = new Std.StdClass();
        assertEquals(ArrayList.class, deserializer.deserialize(parser, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeJavaType() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("\"com.fasterxml.jackson.databind.JsonNode.class\"");
        StdDeserializer deserializer = new Std.StdJAVA_TYPE();
        JavaType javaType = deserializer.deserialize(parser, objectMapper.getDeserializationContext());
        assertEquals(JsonNode.class, javaType.getRawClass());
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("\"USD\"");
        StdDeserializer deserializer = new Std.StdCURRENCY();
        assertEquals(Currency.getInstance("USD"), deserializer.deserialize(parser, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializePattern() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("\"/^[a-z]+$/i\"");
        StdDeserializer deserializer = new Std.StdPATTERN();
        Pattern pattern = deserializer.deserialize(parser, objectMapper.getDeserializationContext());
        assertEquals(Pattern.compile("/^[a-z]+$/i"), pattern);
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("\"en_US\"");
        StdDeserializer deserializer = new Std.StdLOCALE();
        Locale locale = deserializer.deserialize(parser, objectMapper.getDeserializationContext());
        assertEquals(Locale.US, locale);

        parser = objectMapper.getFactory().createParser("\"fr_FR.UTF-8\"");
        locale = deserializer.deserialize(parser, objectMapper.getDeserializationContext());
        assertEquals(Locale.FRANCE, locale);

        parser = objectMapper.getFactory().createParser("\"fr_FR\"");
        locale = deserializer.deserialize(parser, objectMapper.getDeserializationContext());
        assertEquals(Locale.FRANCE, locale);
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("\"US-ASCII\"");
        StdDeserializer deserializer = new Std.StdCHARSET();
        assertEquals(Charset.forName("US-ASCII"), deserializer.deserialize(parser, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("\"UTC\"");
        StdDeserializer deserializer = new Std.StdTIME_ZONE();
        TimeZone timeZone = deserializer.deserialize(parser, objectMapper.getDeserializationContext());
        assertEquals(TimeZone.getTimeZone("UTC"), timeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("\"192.168.1.1\"");
        StdDeserializer deserializer = new Std.StdINET_ADDRESS();
        InetAddress inetAddress = deserializer.deserialize(parser, objectMapper.getDeserializationContext());
        assertEquals(InetAddress.getByName("192.168.1.1"), inetAddress);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("\"[255.255.255.255]:8080\"");
        StdDeserializer deserializer = new Std.StdINET_SOCKET_ADDRESS();
        InetSocketAddress inetSocketAddress = deserializer.deserialize(parser, objectMapper.getDeserializationContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 8080), inetSocketAddress);
    }

    @Test
    public void testDeserializeEmptyStringForClass() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("\"\"");
        StdDeserializer deserializer = new Std.StdCLASS();
        assertNull(deserializer.deserialize(parser, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeEmptyStringForURI() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("\"\"");
        StdDeserializer deserializer = new Std.StdURI();
        assertEquals(URI.create(""), deserializer.deserialize(parser, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeErrorForInvalidType() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("\"invalid_type\"");
        StdDeserializer deserializer = new Std.StdFILE();
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(parser, objectMapper.getDeserializationContext()));
    }
}