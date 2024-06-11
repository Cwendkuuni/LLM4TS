package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize() throws IOException, JsonMappingException {
        testDeserialize(URL.class, "http://example.com");
        testDeserialize(URI.class, "http://example.com");
        testDeserialize(File.class, "path/to/file.txt");
        testDeserialize(Class.class, "java.lang.String.class");
        testDeserialize(JavaType.class, "com.example.MyClass");
        testDeserialize(Currency.class, "USD");
        testDeserialize(Pattern.class, "^[a-z]+");
        testDeserialize(Locale.class, "en_US");
        testDeserialize(Charset.class, "UTF-8");
        testDeserialize(TimeZone.class, "America/Los_Angeles");
        testDeserialize(InetAddress.class, "192.168.1.1");
        testDeserialize(InetSocketAddress.class, "192.168.1.1:8080");
        testDeserialize(InetSocketAddress.class, "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:8080");
        testDeserialize(StringBuilder.class, "");
        testDeserialize(StringBuilder.class, "abcdefg");

        testDeserialize(Locale.class, "en_US_US");
        testDeserialize(Locale.class, "en_US_CA");
        testDeserialize(Locale.class, "en_GB");
        testDeserialize(Locale.class, "fr_FR");
        testDeserialize(Locale.class, "es_ES");

        testDeserialize(TimeZone.class, "UTC");
        testDeserialize(TimeZone.class, "Pacific/Honolulu");
        testDeserialize(TimeZone.class, "Asia/Shanghai");

        testDeserialize(Currency.class, "EUR");
        testDeserialize(Currency.class, "GBP");
        testDeserialize(Currency.class, "JPY");

        testDeserialize(Pattern.class, "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,}$");
        testDeserialize(Pattern.class, ".*");

        testDeserialize(File.class, (String) null);
        testDeserialize(URL.class, (String) null);
        testDeserialize(URI.class, (String) null);
        testDeserialize(Class.class, (String) null);
        testDeserialize(JavaType.class, (String) null);
        testDeserialize(Currency.class, (String) null);
        testDeserialize(Pattern.class, (String) null);
        testDeserialize(Locale.class, (String) null);
        testDeserialize(Charset.class, (String) null);
        testDeserialize(TimeZone.class, (String) null);
        testDeserialize(InetAddress.class, (String) null);
        testDeserialize(InetSocketAddress.class, (String) null);
        testDeserialize(StringBuilder.class, (String) null);

        testDeserializeWithInvalidFormatException(URL.class, "invalid_url");
        testDeserializeWithInvalidFormatException(URI.class, "invalid_uri");
        testDeserializeWithInvalidFormatException(File.class, "invalid_file_path");
        testDeserializeWithInvalidFormatException(Class.class, "com.example.InvalidClass");
        testDeserializeWithInvalidFormatException(JavaType.class, "java.lang.String");
        testDeserializeWithInvalidFormatException(Currency.class, "invalid_currency_code");
        testDeserializeWithInvalidFormatException(Pattern.class, "[^a-z]+");
        testDeserializeWithInvalidFormatException(Locale.class, "en__US");
        testDeserializeWithInvalidFormatException(Charset.class, "invalid_charset_name");
        testDeserializeWithInvalidFormatException(TimeZone.class, "InvalidTimeZone");
        testDeserializeWithInvalidFormatException(InetAddress.class, "invalid_ip_address");
        testDeserializeWithInvalidFormatException(InetSocketAddress.class, "invalid_socket_address");
        testDeserializeWithInvalidFormatException(StringBuilder.class, null);

        testDeserializeEmbeddedObject(URL.class, "{\"url\": \"http://example.com\"}");
        testDeserializeEmbeddedObject(URI.class, "{\"uri\": \"http://example.com\"}");
        testDeserializeEmbeddedObject(File.class, "{\"file\": \"path/to/file.txt\"}");
        testDeserializeEmbeddedObject(Class.class, "{\"class\": \"java.lang.String.class\"}");
        testDeserializeEmbeddedObject(JavaType.class, "{\"javaType\": \"com.example.MyClass\"}");
        testDeserializeEmbeddedObject(Currency.class, "{\"currency\": \"USD\"}");
        testDeserializeEmbeddedObject(Pattern.class, "{\"pattern\": \"^[a-z]+\"}");
        testDeserializeEmbeddedObject(Locale.class, "{\"locale\": \"en_US\"}");
        testDeserializeEmbeddedObject(Charset.class, "{\"charset\": \"UTF-8\"}");
        testDeserializeEmbeddedObject(TimeZone.class, "{\"timeZone\": \"America/Los_Angeles\"}");
        testDeserializeEmbeddedObject(InetAddress.class, "{\"inetAddress\": \"192.168.1.1\"}");
        testDeserializeEmbeddedObject(InetSocketAddress.class, "{\"inetSocketAddress\": \"192.168.1.1:8080\"}");
        testDeserializeEmbeddedObject(StringBuilder.class, "{\"stringBuilder\": \"abcdefg\"}");

        testDeserializeArray(URL.class, "[\"http://example1.com\", \"http://example2.com\"]");
        testDeserializeArray(URI.class, "[\"http://example1.com\", \"http://example2.com\"]");
        testDeserializeArray(File.class, "[\"path/to/file1.txt\", \"path/to/file2.txt\"]");
        testDeserializeArray(Class.class, "[\"java.lang.String.class\", \"java.util.ArrayList.class\"]");
        testDeserializeArray(JavaType.class, "[\"com.example.MyClass\", \"com.example.AnotherClass\"]");
        testDeserializeArray(Currency.class, "[\"USD\", \"EUR\"]");
        testDeserializeArray(Pattern.class, "[\"^[a-z]+\\$, \"^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,}\"]");
        testDeserializeArray(Locale.class, "[\"en_US\", \"fr_FR\"]");
        testDeserializeArray(Charset.class, "[\"UTF-8\", \"ISO-8859-1\"]");
        testDeserializeArray(TimeZone.class, "[\"America/Los_Angeles\", \"Asia/Shanghai\"]");
        testDeserializeArray(InetAddress.class, "[\"192.168.1.1\", \"2001:0db8:85a3:0000:0000:8a2e:0370:7334\"]");
        testDeserializeArray(InetSocketAddress.class, "[\"192.168.1.1:8080\", \"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:8080\"]");
        testDeserializeArray(StringBuilder.class, "[\"\", \"abcdefg\"]");

        // Test with empty String array
        testDeserializeArrayWithEmptyArray(URL.class);
        testDeserializeArrayWithEmptyArray(URI.class);
        testDeserializeArrayWithEmptyArray(File.class);
        testDeserializeArrayWithEmptyArray(Class.class);
        testDeserializeArrayWithEmptyArray(JavaType.class);
        testDeserializeArrayWithEmptyArray(Currency.class);
        testDeserializeArrayWithEmptyArray(Pattern.class);
        testDeserializeArrayWithEmptyArray(Locale.class);
        testDeserializeArrayWithEmptyArray(Charset.class);
        testDeserializeArrayWithEmptyArray(TimeZone.class);
        testDeserializeArrayWithEmptyArray(InetAddress.class);
        testDeserializeArrayWithEmptyArray(InetSocketAddress.class);
        testDeserializeArrayWithEmptyArray(StringBuilder.class);
    }

    private void testDeserialize(Class<?> clazz, String jsonString) throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = (T) FromStringDeserializer.findDeserializer(clazz).deserialize(jsonParser, ctxt);
        assertEquals(clazz, result.getClass());

        if (clazz.isArray()) {
            JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
            assertEquals(javaType.getRawClass(), result.getClass().getComponentType());
        }
    }

    private void testDeserializeWithInvalidFormatException(Class<?> clazz, String jsonString) throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        assertThrows(InvalidFormatException.class, () -> FromStringDeserializer.findDeserializer(clazz).deserialize(jsonParser, ctxt));
    }

    private void testDeserializeEmbeddedObject(Class<?> clazz, String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Object object = ctxt.readValue(jsonParser, Object.class);
        T result = (T) FromStringDeserializer.findDeserializer(clazz).deserializeEmbedded(object, ctxt);
        assertEquals(clazz, result.getClass());
    }

    private void testDeserializeArray(Class<?> clazz, String jsonArrayString) throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonArrayString);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T[] results = (T[]) FromStringDeserializer.findDeserializer(clazz)._deserializeFromArray(jsonParser, ctxt);

        JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
        assertEquals(javaType.getComponentType(), results[0].getClass());
        assertEquals(javaType.getArraySize(), results.length);
    }

    private void testDeserializeArrayWithEmptyArray(Class<?> clazz) throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T[] results = (T[]) FromStringDeserializer.findDeserializer(clazz)._deserializeFromArray(jsonParser, ctxt);
        assertEquals(0, results.length);
    }
}