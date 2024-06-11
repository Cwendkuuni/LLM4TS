package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);

        String json = "\"path/to/file.txt\"";
        File result = deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext());

        assertNotNull(result);
        assertNotNull(result.getAbsolutePath());
        assertNotNull(result.getAbsoluteFile());
    }

    @Test
    public void testDeserializeURL() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);

        String json = "\"http://example.com/path/to/resource\"";
        URL result = deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext());

        assertNotNull(result);
        assertNotNull(result.getProtocol());
        assertNotNull(result.getHost());
        assertNotNull(result.getPort());
        assertNotNull(result.getPath());
        assertNotNull(result.getFile());
    }

    @Test
    public void testDeserializeURI() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);

        String json = "\"http://example.com/path/to/resource\"";
        URI result = deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext());

        assertNotNull(result);
        assertNotNull(result.getScheme());
        assertNotNull(result.getHost());
        assertNotNull(result.getPort());
        assertNotNull(result.getPath());
        assertNotNull(result.getFragment());
        assertNotNull(result.getRawSchemeSpecificPart());
    }

    @Test
    public void testDeserializeClass() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);

        String json = "java.util.Locale";
        Class<?> result = deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext());

        assertNotNull(result);
        assertNotNull(Locale.class.isAssignableFrom(result));
    }

    @Test
    public void testDeserializeJavaType() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);

        String json = "com.example.MyClass";
        JavaType result = deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext());

        assertNotNull(result);
        assertNotNull(result.getRawClass());
        assertNotNull(result.getRawType());
    }

    @Test
    public void testDeserializeCurrency() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);

        String json = "USD";
        Currency result = deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext());

        assertNotNull(result);
        assertNotNull(result.getCurrencyCode());
        assertNotNull(result.getDisplayName());
        assertNotNull(result.toString());
    }

    @Test
    public void testDeserializePattern() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);

        String json = "/^[a-z]+$/";
        Pattern result = deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext());

        assertNotNull(result);
        assertNotNull(result.pattern());
    }

    @Test
    public void testDeserializeLocale() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);

        String json = "en_US";
        Locale result = deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext());

        assertNotNull(result);
        assertNotNull(result.getLanguage());
        assertNotNull(result.getCountry());
    }

    @Test
    public void testDeserializeCharset() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);

        String json = "UTF-8";
        Charset result = deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext());

        assertNotNull(result);
        assertNotNull(result.name());
        assertNotNull(result.displayName());
    }

    @Test
    public void testDeserializeTimeZone() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);

        String json = "America/Los_Angeles";
        TimeZone result = deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext());

        assertNotNull(result);
        assertNotNull(result.getID());
        assertNotNull(result.getDisplayName());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);

        String json = "192.168.0.1";
        InetAddress result = deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext());

        assertNotNull(result);
        assertNotNull(result.getHostAddress());
        assertNotNull(result.getHostName());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);

        String json = "[192.168.0.1]:8080";
        InetSocketAddress result = deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext());

        assertNotNull(result);
        assertNotNull(result.getAddress());
        assertNotNull(result.getPort());

        json = "{\"address\":\"192.168.0.1\",\"port\":8080}";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        result = deserializer.deserialize(mapper.getFactory().createParser(node), new DeserializationContext());

        assertNotNull(result);
        assertNotNull(result.getAddress());
        assertNotNull(result.getPort());
    }

    @Test
    public void testDeserializeStringBuilder() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);

        String json = "\"Hello, world!\"";
        StringBuilder result = deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext());

        assertNotNull(result);
        assertNotNull(result.length());
        assertNotNull(result.toString());
    }

    @Test
    public void testDeserializeArray() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<String[]> deserializer = FromStringDeserializer.findDeserializer(String[].class);

        String json = "[\"Hello\", \"world\"]";
        String[] result = deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext());

        assertNotNull(result);
        assertNotNull(result[0]);
        assertNotNull(result[1]);
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "{\"class\":\"java.lang.StringBuilder\",\"value\":\"Hello, world!\"}";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        Object result = FromStringDeserializer.Std.STD_STRING_BUILDER.deserialize(mapper.getFactory().createParser(node), new DeserializationContext());

        assertNotNull(result);
        assertNotNull(((StringBuilder) result).length());
        assertNotNull(((StringBuilder) result).toString());
    }

    @Test
    public void testDeserializeEmptyString() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);

        String json = "\"\"";
        Object result = deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext());

        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidString() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);

        String json = "invalid";
        deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext());
    }
}