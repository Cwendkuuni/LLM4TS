package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        String json = "\"/path/to/file.txt\"";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        FromStringDeserializer<File> deserializer = new FromStringDeserializer.Std(File.class);
        File result = deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()));

        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        String json = "\"http://example.com\"";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        FromStringDeserializer<URL> deserializer = new FromStringDeserializer.Std(URL.class);
        URL result = deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()));

        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeUri() throws IOException {
        String json = "\"http://example.com/path?query#fragment\"";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        FromStringDeserializer<URI> deserializer = new FromStringDeserializer.Std(URI.class);
        URI result = deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()));

        assertEquals("http://example.com/path", result.toURI().toURL().toString());
        assertEquals("query", result.getQuery());
        assertEquals("fragment", result.getFragment());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        String json = "\"java.lang.String\"";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        FromStringDeserializer<Class<?>> deserializer = new FromStringDeserializer.Std(Class.class);
        Class<?> result = deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()));

        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        String json = "\"class com.example.MyClass\"";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        FromStringDeserializer<JavaType> deserializer = new FromStringDeserializer.Std(JavaType.class);
        JavaType result = deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()));

        assertEquals(JavaType.construct(String[].class), result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        String json = "\"USD\"";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer.Std(Currency.class);
        Currency result = deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()));

        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        String json = "\"/[a-zA-Z]+\\.txt\"";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer.Std(Pattern.class);
        Pattern result = deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()));

        assertEquals(Pattern.compile("/[a-zA-Z]+\\.txt"), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        String json = "\"en_US\"";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer.Std(Locale.class);
        Locale result = deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()));

        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        String json = "\"UTF-8\"";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer.Std(Charset.class);
        Charset result = deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()));

        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        String json = "\"America/Los_Angeles\"";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer.Std(TimeZone.class);
        TimeZone result = deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()));

        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        String json = "\"192.168.1.1\"";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer.Std(InetAddress.class);
        InetAddress result = deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()));

        assertEquals("192.168.1.1", result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        String json = "[\"192.168.1.1\", 80]";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer.Std(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()));

        assertEquals("192.168.1.1", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());

        json = "[\"192.168.1.1\", \"http\"]";
        parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        result = deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()));

        assertEquals("192.168.1.1", result.getAddress().getHostAddress());
        assertEquals("/http", result.getPort());
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        String json = "{\"@class\":\"com.example.MyClass\", \"value\":\"Hello, World!\"}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        FromStringDeserializer<MyClass> deserializer = new MyClassDeserializer();
        MyClass result = deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()));

        assertEquals("Hello, World!", result.getValue());
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        String json = "\"\"";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        FromStringDeserializer<String> deserializer = new FromStringDeserializer.Std(String.class);
        String result = deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()));

        assertNull(result);
    }

    @Test
    public void testDeserializeInvalidJson() throws IOException {
        String json = "\"invalidJSON\"";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));

        FromStringDeserializer<String> deserializer = new FromStringDeserializer.Std(String.class);

        try {
            deserializer.deserialize(parser, new DeserializationContext(mapper.getConfig()));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }
}

class MyClass {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}