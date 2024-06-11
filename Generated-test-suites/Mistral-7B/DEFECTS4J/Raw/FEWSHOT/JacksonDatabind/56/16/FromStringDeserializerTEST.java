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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> FromStringDeserializer<T> getDeserializer(Class<T> valueType) {
        return (FromStringDeserializer<T>) FromStringDeserializer.findDeserializer(valueType);
    }

    @Test
    public void testFileDeserializer() throws IOException {
        FromStringDeserializer<File> deserializer = getDeserializer(File.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"/path/to/file.txt\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        File result = deserializer.deserialize(parser, ctxt);
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testURLDeserializer() throws IOException {
        FromStringDeserializer<URL> deserializer = getDeserializer(URL.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"http://example.com\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        URL result = deserializer.deserialize(parser, ctxt);
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testURIDeserializer() throws IOException {
        FromStringDeserializer<URI> deserializer = getDeserializer(URI.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"http://example.com/path\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        URI result = deserializer.deserialize(parser, ctxt);
        assertEquals("http://example.com/path", result.toString());
    }

    @Test
    public void testClassDeserializer() throws IOException {
        FromStringDeserializer<String> deserializer = getDeserializer(String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"java.lang.String\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String result = (String) deserializer.deserialize(parser, ctxt);
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testJavaTypeDeserializer() throws IOException {
        FromStringDeserializer<JavaType> deserializer = getDeserializer(JavaType.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"com.example.MyClass\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType result = deserializer.deserialize(parser, ctxt);
        assertEquals("com.example.MyClass", result.getRawClass().getName());
    }

    @Test
    public void testCurrencyDeserializer() throws IOException {
        FromStringDeserializer<Currency> deserializer = getDeserializer(Currency.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"EUR\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Currency result = deserializer.deserialize(parser, ctxt);
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testPatternDeserializer() throws IOException {
        FromStringDeserializer<Pattern> deserializer = getDeserializer(Pattern.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"/\\w+(\\.\\w+)*@\\w+(\\.\\w+)*\\.(\\w{2,3})\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Pattern result = deserializer.deserialize(parser, ctxt);
        String regex = "/\\w+(\\.\\w+)*@\\w+(\\.\\w+)*\\.(\\w{2,3})";
        assertEquals(Pattern.compile(regex), result);
    }

    @Test
    public void testLocaleDeserializer() throws IOException {
        FromStringDeserializer<Locale> deserializer = getDeserializer(Locale.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"en_US\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Locale result = deserializer.deserialize(parser, ctxt);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testCharsetDeserializer() throws IOException {
        FromStringDeserializer<Charset> deserializer = getDeserializer(Charset.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"UTF-8\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Charset result = deserializer.deserialize(parser, ctxt);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testTimeZoneDeserializer() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = getDeserializer(TimeZone.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"America/Los_Angeles\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TimeZone result = deserializer.deserialize(parser, ctxt);
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testInetAddressDeserializer() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = getDeserializer(InetAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"192.168.1.1\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        InetAddress result = deserializer.deserialize(parser, ctxt);
        assertInstanceOf(InetAddress.class, result);
        assertEquals("192.168.1.1", result.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserializer() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = getDeserializer(InetSocketAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"[255.255.255.255]:80\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        InetSocketAddress result = deserializer.deserialize(parser, ctxt);
        assertInstanceOf(InetSocketAddress.class, result);
        assertEquals("255.255.255.255", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());

        parser = mapper.getFactory().createParser("\"192.168.1.1:80\"");
        result = deserializer.deserialize(parser, ctxt);
        assertInstanceOf(InetSocketAddress.class, result);
        assertEquals("192.168.1.1", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        FromStringDeserializer<Object> deserializer = getDeserializer(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = deserializer.deserialize(parser, ctxt);
        assertNull(result);
    }
}