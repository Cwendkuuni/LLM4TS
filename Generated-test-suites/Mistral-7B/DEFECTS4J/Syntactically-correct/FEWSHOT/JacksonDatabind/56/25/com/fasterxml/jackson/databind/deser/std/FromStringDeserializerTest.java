package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class FromStringDeserializerTest {

    private static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeFile() throws IOException {
        String fileStr = "path/to/file.txt";
        T deserialized = (T) FromStringDeserializer.findDeserializer(File.class).deserialize(mapper.getFactory().createParser(new File(fileStr)), null);
        assertEquals(new File(fileStr), deserialized);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeUrl() throws IOException {
        String urlStr = "http://example.com";
        T deserialized = (T) FromStringDeserializer.findDeserializer(URL.class).deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(urlStr.getBytes())), null);
        assertEquals(new URL(urlStr), deserialized);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeUri() throws IOException {
        String uriStr = "http://example.com/path/to/file.txt";
        T deserialized = (T) FromStringDeserializer.findDeserializer(URI.class).deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(uriStr.getBytes())), null);
        assertEquals(new URI(uriStr), deserialized);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeClass() throws IOException {
        String className = "java.lang.String";
        T deserialized = (T) FromStringDeserializer.findDeserializer(Class.class).deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(className.getBytes())), null);
        assertEquals(Class.forName(className), deserialized);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeJavaType() throws IOException {
        String typeName = "java.util.ArrayList<java.lang.Integer>";
        JavaType javaType = mapper.getTypeFactory().constructType(typeName);
        T deserialized = (T) FromStringDeserializer.findDeserializer(JavaType.class).deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(typeName.getBytes())), null);
        assertEquals(javaType, deserialized);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeCurrency() throws IOException {
        String currencyCode = "USD";
        T deserialized = (T) FromStringDeserializer.findDeserializer(Currency.class).deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(currencyCode.getBytes())), null);
        assertEquals(Currency.getInstance(currencyCode), deserialized);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializePattern() throws IOException {
        String patternStr = "(.)(.){2}";
        T deserialized = (T) FromStringDeserializer.findDeserializer(Pattern.class).deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(patternStr.getBytes())), null);
        assertEquals(Pattern.compile(patternStr), deserialized);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeLocale() throws IOException {
        String localeStr = "en_US";
        T deserialized = (T) FromStringDeserializer.findDeserializer(Locale.class).deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(localeStr.getBytes())), null);
        assertEquals(Locale.forLanguageTag(localeStr), deserialized);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeCharset() throws IOException {
        String charsetName = "UTF-8";
        T deserialized = (T) FromStringDeserializer.findDeserializer(Charset.class).deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(charsetName.getBytes())), null);
        assertEquals(Charset.forName(charsetName), deserialized);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeTimeZone() throws IOException {
        String timeZoneId = "UTC";
        T deserialized = (T) FromStringDeserializer.findDeserializer(TimeZone.class).deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(timeZoneId.getBytes())), null);
        assertEquals(TimeZone.getTimeZone(timeZoneId), deserialized);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeInetAddress() throws IOException {
        String ipv4Address = "192.168.1.1";
        T deserialized = (T) FromStringDeserializer.findDeserializer(InetAddress.class).deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(ipv4Address.getBytes())), null);
        assertEquals(InetAddress.getByName(ipv4Address), deserialized);

        String ipv6Address = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]";
        deserialized = (T) FromStringDeserializer.findDeserializer(InetSocketAddress.class).deserialize(mapper.getFactory().createParser(new ByteArrayInputStream(ipv6Address.getBytes())), null);
        InetSocketAddress expected = new InetSocketAddress(ipv6Address, 0);
        assertEquals(expected, deserialized);

        deserialized = (T) FromStringDeserializer.findDeserializer(InetSocketAddress.class).deserialize(mapper.getFactory().createParser(new ByteArrayInputStream((ipv6Address + ":80").getBytes())), null);
        expected = new InetSocketAddress(new InetSocketAddress(ipv6Address, 80));
        assertEquals(expected, deserialized);
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        for (Class<?> supportedType : FromStringDeserializer.types()) {
            StdDeserializer<?> deserializer = (StdDeserializer<?>) FromStringDeserializer.findDeserializer(supportedType);
            T deserialized = deserializer.deserializeFromEmptyString();
            if (supportedType == URI.class || supportedType == Locale.class) {
                assertNull(deserialized);
            } else {
                assertEquals(deserialized, null);
            }
        }
    }
}