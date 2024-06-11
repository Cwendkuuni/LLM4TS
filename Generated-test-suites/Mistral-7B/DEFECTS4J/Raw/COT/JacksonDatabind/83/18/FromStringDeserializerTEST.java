package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Tests for {@link FromStringDeserializer} class.
 */
public class FromStringDeserializerTest {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @org.junit.Test
    public void testDeserialize() throws IOException {
        // Test deserialization for various supported types
        ObjectMapper objectMapper = new ObjectMapper();

        // File
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"path/to/file.txt\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        File file = (File) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertEquals("path/to/file.txt", file.getAbsolutePath());

        // URL
        jsonParser = objectMapper.getFactory().createParser("\"http://example.com/\"");
        deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL url = (URL) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertEquals("http", url.getProtocol());
        assertEquals("/", url.getPath());

        // URI
        jsonParser = objectMapper.getFactory().createParser("\"uri://example.com/path/to/resource\"");
        deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI uri = (URI) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertEquals("uri", uri.getScheme());
        assertEquals("/path/to/resource", uri.getPath());

        // Class
        jsonParser = objectMapper.getFactory().createParser("java.lang.String.class");
        deserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class clazz = (Class) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertEquals(String.class, clazz);

        // JavaType
        deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JsonFactory jsonFactory = new JsonFactory();
        JavaType javaType = deserializer.deserialize(jsonFactory.createParser("java.lang.String[]"), new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertEquals(String[].class, javaType.getRawClass());

        // Currency
        jsonParser = objectMapper.getFactory().createParser("\"USD\"");
        deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency currency = (Currency) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertEquals("USD", currency.getCurrencyCode());

        // Pattern
        jsonParser = objectMapper.getFactory().createParser("\"(.*)\\.txt\"");
        deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern pattern = (Pattern) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertEquals("[^.]+\\.txt", pattern.pattern().toString());

        // Locale
        jsonParser = objectMapper.getFactory().createParser("\"en-US\"");
        deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale locale = (Locale) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());

        // Charset
        jsonParser = objectMapper.getFactory().createParser("\"UTF-8\"");
        deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset charset = (Charset) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertEquals("UTF-8", charset.name());

        // TimeZone
        jsonParser = objectMapper.getFactory().createParser("\"America/New_York\"");
        deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone timeZone = (TimeZone) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertEquals("America/New_York", timeZone.getID());

        // InetAddress
        jsonParser = objectMapper.getFactory().createParser("\"127.0.0.1\"");
        deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress inetAddress = (InetAddress) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertEquals("127.0.0.1", inetAddress.getHostAddress());

        // InetSocketAddress
        jsonParser = objectMapper.getFactory().createParser("\"[127.0.0.1]:8080\"");
        deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertEquals("127.0.0.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());

        // StringBuilder
        jsonParser = objectMapper.getFactory().createParser("\"Hello World!\"");
        deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder stringBuilder = (StringBuilder) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertEquals("Hello World!", stringBuilder.toString());
    }

    @org.junit.Test
    public void testEmptyString() throws IOException {
        // Test deserialization for empty String
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder stringBuilder = (StringBuilder) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertNull(stringBuilder);

        deserializer = FromStringDeserializer.findDeserializer(File.class);
        File file = (File) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertNull(file);

        deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL url = (URL) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertNull(url);

        deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI uri = (URI) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertNull(uri);

        deserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class clazz = (Class) deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertNull(clazz);

        deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JsonFactory jsonFactory = new JsonFactory();
        JavaType javaType = (JavaType) deserializer.deserialize(jsonFactory.createParser("\"java.lang.String[]\""), new DeserializationContext(ObjectMapper.getDefaultInstance()));
        assertNull(javaType);

        deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));
    }

    @org.junit.Test
    public void testInvalidString() throws IOException {
        // Test deserialization for invalid strings
        ObjectMapper objectMapper = new ObjectMapper();

        // File
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"invalid_file_path\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        // URL
        jsonParser = objectMapper.getFactory().createParser("\"invalid_url_scheme:/path/to/file.txt\"");
        deserializer = FromStringDeserializer.findDeserializer(URL.class);
        assertThrows(MalformedURLException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        // URI
        jsonParser = objectMapper.getFactory().createParser("\"invalid_uri_scheme:/path/to/file.txt\"");
        deserializer = FromStringDeserializer.findDeserializer(URI.class);
        assertThrows(URISyntaxException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        // Class
        jsonParser = objectMapper.getFactory().createParser("\"java.lang.String.Class\"");
        deserializer = FromStringDeserializer.findDeserializer(Class.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        // JavaType
        JsonFactory jsonFactory = new JsonFactory();
        jsonParser = jsonFactory.createParser("\"java.lang.String[]\"");
        deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        // Currency
        jsonParser = objectMapper.getFactory().createParser("\"ABC\"");
        deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        // Pattern
        jsonParser = objectMapper.getFactory().createParser("\"(.*).txt\\\"");
        deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        // Locale
        jsonParser = objectMapper.getFactory().createParser("\"en_US$\"");
        deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        // Charset
        jsonParser = objectMapper.getFactory().createParser("\"UTF-8@invalid_charset\"");
        deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        // TimeZone
        jsonParser = objectMapper.getFactory().createParser("\"America/New_York:+5:00\"");
        deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        // InetAddress
        jsonParser = objectMapper.getFactory().createParser("\"invalid_ip_address:127.0.0.1\"");
        deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        assertThrows(InvalidFormatException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        // InetSocketAddress
        jsonParser = objectMapper.getFactory().createParser("\"[invalid_ip_address]:8080\"");
        deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        assertThrows(InvalidFormatException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));

        // StringBuilder
        jsonParser = objectMapper.getFactory().createParser("\"invalid_string_builder\"");
        deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, new DeserializationContext(ObjectMapper.getDefaultInstance())));
    }
}