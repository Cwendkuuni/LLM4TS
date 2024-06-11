package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeFile() throws IOException {
        String json = "\"/path/to/file.txt\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null));
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        String json = "\"http://www.example.com\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null));
        assertEquals("http://www.example.com", result.toString());
    }

    @Test
    public void testDeserializeUri() throws IOException {
        String json = "\"http://www.example.com/path/to/resource\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null));
        assertEquals("http://www.example.com/path/to/resource", result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        String json = "\"java.lang.String\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null));
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        String json = "\"com.example.MyClass\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null));
        assertInstanceOf(JavaType.class, result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        String json = "\"USD\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null));
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        String json = "\"\\w+(\\.\\w+)*\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null));
        assertInstanceOf(Pattern.class, result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        String json = "\"en_US\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null));
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        String json = "\"UTF-8\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null));
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        String json = "\"America/Los_Angeles\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null));
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        String json = "\"192.168.0.1\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null));
        assertInstanceOf(InetAddress.class, result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        String json = "[\"192.168.0.1\", 80]";
        JsonParser parser = mapper.getFactory().createParser(json);
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null));
        assertEquals(new InetSocketAddress("192.168.0.1", 80), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidType() throws IOException {
        String json = "\"java.util.List\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null)); // should fail
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyString() throws IOException {
        String json = "\"\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null)); // should fail
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNull() throws IOException {
        String json = "null";
        JsonParser parser = mapper.getFactory().createParser(json);
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null)); // should fail
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        String json = "[]";
        JsonParser parser = mapper.getFactory().createParser(json);
        FromStringDeserializer<File[]> deserializer = FromStringDeserializer.findDeserializer(File[].class);
        File[] result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null));
        assertNull(result);
    }

}