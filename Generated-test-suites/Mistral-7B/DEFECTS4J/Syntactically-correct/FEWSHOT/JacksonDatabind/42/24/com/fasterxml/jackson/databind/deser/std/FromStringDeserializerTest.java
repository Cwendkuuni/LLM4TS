package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> JavaType getType(Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getTypeFactory().constructType(clazz);
    }

    @Test
    public void testDeserializeFile() throws IOException, JsonProcessingException {
        String json = "\"/test.txt\"";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Std.StdFile stdFile = FromStringDeserializer.findDeserializer(File.class);
        File result = stdFile.deserialize(new JsonParser(new StringReader(json)), ctxt);
        assertEquals(new File("/test.txt"), result);
    }

    @Test
    public void testDeserializeURL() throws IOException, JsonProcessingException {
        String json = "\"http://test.com\"";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Std.StdURL stdURL = FromStringDeserializer.findDeserializer(URL.class);
        URL result = stdURL.deserialize(new JsonParser(new StringReader(json)), ctxt);
        assertEquals(new URL("http://test.com"), result);
    }

    @Test
    public void testDeserializeURI() throws IOException, JsonProcessingException {
        String json = "\"http://test.com/test\"";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Std.StdURI stdURI = FromStringDeserializer.findDeserializer(URI.class);
        URI result = stdURI.deserialize(new JsonParser(new StringReader(json)), ctxt);
        assertEquals(new URI("http://test.com/test"), result);
    }

    @Test
    public void testDeserializeClass() throws IOException, JsonProcessingException {
        String json = "\"java.lang.String\"";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Std.StdClass stdClass = FromStringDeserializer.findDeserializer(String.class);
        String result = stdClass.deserialize(new JsonParser(new StringReader(json)), ctxt);
        assertEquals("java.lang.String", result.getClass().getName());
    }

    @Test
    public void testDeserializeJavaType() throws IOException, JsonProcessingException {
        String json = "\"com.fasterxml.jackson.databind.deser.std.FromStringDeserializer<java.lang.Integer>\"]\"";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Std.StdJavaType stdJavaType = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType<Integer> result = (JavaType<Integer>) stdJavaType.deserialize(new JsonParser(new StringReader(json)), ctxt);
        assertEquals(getType(Integer.class), result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException, JsonProcessingException {
        String json = "\"EUR\"";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Std.StdCurrency stdCurrency = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = stdCurrency.deserialize(new JsonParser(new StringReader(json)), ctxt);
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException, JsonProcessingException {
        String json = "\"/[a-zA-Z]+\\.txt$/\"";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Std.StdPattern stdPattern = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = stdPattern.deserialize(new JsonParser(new StringReader(json)), ctxt);
        assertEquals(Pattern.compile("/[a-zA-Z]+\\.txt$/"), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException, JsonProcessingException {
        String json = "\"en_US\"";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Std.StdLocale stdLocale = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = stdLocale.deserialize(new JsonParser(new StringReader(json)), ctxt);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException, JsonProcessingException {
        String json = "\"US-ASCII\"";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Std.StdCharset stdCharset = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = stdCharset.deserialize(new JsonParser(new StringReader(json)), ctxt);
        assertEquals(Charset.forName("US-ASCII"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException, JsonProcessingException {
        String json = "\"America/Los_Angeles\"";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Std.StdTimeZone stdTimeZone = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = stdTimeZone.deserialize(new JsonParser(new StringReader(json)), ctxt);
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException, JsonProcessingException {
        String json = "\"192.168.1.1\"";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Std.StdInetAddress stdInetAddress = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = stdInetAddress.deserialize(new JsonParser(new StringReader(json)), ctxt);
        assertEquals(InetAddress.getByName("192.168.1.1"), result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException, JsonProcessingException {
        String json = "[\"192.168.1.1\", 80]";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Std.StdInetSocketAddress stdInetSocketAddress = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = stdInetSocketAddress.deserialize(new JsonParser(new StringReader(json)), ctxt);
        assertEquals(new InetSocketAddress("192.168.1.1", 80), result);
    }

    @Test
    public void testDeserializeInvalidType() throws IOException, JsonProcessingException {
        String json = "\"java.util.List\"";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(ArrayList.class);
        try {
            deserializer.deserialize(new JsonParser(new StringReader(json)), ctxt);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Not a valid representation of java.util.List for value: [java.lang.String\"java.util.List\"]", e.getMessage());
        }
    }

    @Test
    public void testUnwrapSingleValueArray() throws IOException, JsonProcessingException {
        String json = "[\"java.lang.Integer\", 123]";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Std.StdInteger stdInteger = FromStringDeserializer.findDeserializer(Integer.class);
        Integer result = stdInteger.deserialize(new JsonParser(new StringReader(json)), ctxt);
        assertEquals(Integer.valueOf(123), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testUnwrapSingleValueArrayWithMultipleValues() throws IOException, JsonProcessingException {
        String json = "[\"java.lang.Integer\", 123, \"java.lang.String\", \"abc\"]";
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Std.StdInteger stdInteger = FromStringDeserializer.findDeserializer(Integer.class);
        stdInteger.deserialize(new JsonParser(new StringReader(json)), ctxt);
    }

    @Test
    public void testEmbeddedObjectNull() throws IOException, JsonProcessingException {
        String json = "null";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Std.StdObject stdObject = FromStringDeserializer.findDeserializer(Object.class);
        Object result = stdObject.deserializeEmbedded(null, ctxt);
        assertNull(result);
    }

    @Test
    public void testEmbeddedObjectInstanceOfType() throws IOException, JsonProcessingException {
        String json = "{\"@type\":\"com.fasterxml.jackson.databind.deser.std.StdObject\", \"value\":\"TestObject\"}";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Std.StdObject stdObject = FromStringDeserializer.findDeserializer(Object.class);
        Object result = stdObject.deserializeEmbedded(new Object(), ctxt);
        assertEquals(new Object(), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testEmbeddedObjectInvalidType() throws IOException, JsonProcessingException {
        String json = "{\"@type\":\"java.lang.String\", \"value\":\"TestObject\"}";
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Std.StdObject stdObject = FromStringDeserializer.findDeserializer(Object.class);
        stdObject.deserializeEmbedded(new Object(), ctxt);
    }
}

class StringReader extends InputStreamReader {
    StringReader(String input) {
        super(new ByteArrayInputStream(input.getBytes()));
    }
}