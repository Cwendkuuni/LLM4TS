package com.example.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(File.class);
        JsonParser parser = mapper.getFactory().createParser("\"/path/to/file.txt\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        File result = deserializer.deserialize(parser, ctxt);
        assertEquals(new File("/path/to/file.txt"), result);
    }

    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URL.class);
        JsonParser parser = mapper.getFactory().createParser("\"https://www.example.com\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        URL result = deserializer.deserialize(parser, ctxt);
        assertEquals("https://www.example.com", result.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URI.class);
        JsonParser parser = mapper.getFactory().createParser("\"https://www.example.com/path/to/file.txt\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        URI result = deserializer.deserialize(parser, ctxt);
        assertEquals("https://www.example.com/path/to/file.txt", result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class);
        JsonParser parser = mapper.getFactory().createParser("\"java.util.String\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializer typeDeserializer = ctxt.findValueDeserializer(String.class, null);
        String result = deserializer.deserialize(parser, ctxt);
        assertEquals("java.util.String", result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(JavaType.class);
        JsonParser parser = mapper.getFactory().createParser("\"com.example.MyClass\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType result = deserializer.deserialize(parser, ctxt);
        assertEquals(JavaType.class, result.getClass());
        assertEquals("com.example.MyClass", result.getTypeName());
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Currency.class);
        JsonParser parser = mapper.getFactory().createParser("\"EUR\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Currency result = deserializer.deserialize(parser, ctxt);
        assertEquals(Currency.EUR, result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Pattern.class);
        JsonParser parser = mapper.getFactory().createParser("\"\\w+@\\w+(\\.\\w+)*\\.\\w+\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Pattern result = deserializer.deserialize(parser, ctxt);
        assertEquals(Pattern.compile("\\w+@\\w+(\\.\\w+)*\\.\\w+"), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Locale.class);
        JsonParser parser = mapper.getFactory().createParser("\"en_US\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Locale result = deserializer.deserialize(parser, ctxt);
        assertEquals(Locale.US, result);

        parser = mapper.getFactory().createParser("\"en_US_POSIX\"");
        result = deserializer.deserialize(parser, ctxt);
        assertEquals(Locale.US, result);

        parser = mapper.getFactory().createParser("\"en_GB_POSIX\"");
        result = deserializer.deserialize(parser, ctxt);
        assertEquals(Locale.UK, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Charset.class);
        JsonParser parser = mapper.getFactory().createParser("\"UTF-8\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Charset result = deserializer.deserialize(parser, ctxt);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(TimeZone.class);
        JsonParser parser = mapper.getFactory().createParser("\"America/Los_Angeles\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TimeZone result = deserializer.deserialize(parser, ctxt);
        assertEquals("America/Los_Angeles", result.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(InetAddress.class);
        JsonParser parser = mapper.getFactory().createParser("\"192.168.1.1\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        InetAddress result = deserializer.deserialize(parser, ctxt);
        assertNotNull(result);
        assertEquals("192.168.1.1", result.getHostAddress());

        parser = mapper.getFactory().createParser("\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]\"");
        result = deserializer.deserialize(parser, ctxt);
        assertNotNull(result);
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334", result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(InetSocketAddress.class);
        JsonParser parser = mapper.getFactory().createParser("\"[192.168.1.1]:80\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        InetSocketAddress result = deserializer.deserialize(parser, ctxt);
        assertNotNull(result);
        assertEquals("192.168.1.1", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());

        parser = mapper.getFactory().createParser("\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:8080\"");
        result = deserializer.deserialize(parser, ctxt);
        assertNotNull(result);
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334", result.getAddress().getHostAddress());
        assertEquals(8080, result.getPort());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyStringFile() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(File.class);
        JsonParser parser = mapper.getFactory().createParser("\"\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        deserializer.deserialize(parser, ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyStringURI() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URI.class);
        JsonParser parser = mapper.getFactory().createParser("\"\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        deserializer.deserialize(parser, ctxt);
    }

    @Test
    public void testDeserializeNull() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class);
        JsonParser parser = mapper.getFactory().createParser("\"null\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String result = deserializer.deserialize(parser, ctxt);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownType() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Object.class);
        JsonParser parser = mapper.getFactory().createParser("\"12345\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        deserializer.deserialize(parser, ctxt);
    }
}