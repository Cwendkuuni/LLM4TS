package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFileDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertEquals(new File("test.txt"), deserializer.deserialize(mapper.createParser("new File(\"test.txt\")"), mapper.getDeserializationContext()));
    }

    @Test
    public void testURLDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        assertEquals(new URL("http://localhost:8080"), deserializer.deserialize(mapper.createParser("new URL(\"http://localhost:8080\")"), mapper.getDeserializationContext()));
    }

    @Test
    public void testURIDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        assertEquals(new URI("http://localhost:8080/test"), deserializer.deserialize(mapper.createParser("new URI(\"http://localhost:8080/test\")"), mapper.getDeserializationContext()));
    }

    @Test
    public void testClassDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertEquals(String.class, deserializer.deserialize(mapper.createParser("java.lang.String"), mapper.getDeserializationContext()));
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType javaType = mapper.getTypeFactory().constructType(String.class.getName());
        assertEquals(javaType, deserializer.deserialize(mapper.createParser("com.fasterxml.jackson.core.type.TypeRef<java.lang.String>"), mapper.getDeserializationContext()));
    }

    @Test
    public void testCurrencyDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        assertEquals(Currency.getInstance("USD"), deserializer.deserialize(mapper.createParser("USD"), mapper.getDeserializationContext()));
    }

    @Test
    public void testPatternDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        assertEquals(Pattern.compile("^\\w+@\\w+\\.\\w+$"), deserializer.deserialize(mapper.createParser("\\A^\\w+@\\w+\\.\\w+$\\E"), mapper.getDeserializationContext()));
    }

    @Test
    public void testLocaleDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertEquals(Locale.US, deserializer.deserialize(mapper.createParser("en_US"), mapper.getDeserializationContext()));
        assertEquals(Locale.JAPAN, deserializer.deserialize(mapper.createParser("ja_JP"), mapper.getDeserializationContext()));
        assertEquals(Locale.CHINA, deserializer.deserialize(mapper.createParser("zh_CN"), mapper.getDeserializationContext()));
    }

    @Test
    public void testCharsetDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        assertEquals(Charset.forName("UTF-8"), deserializer.deserialize(mapper.createParser("UTF-8"), mapper.getDeserializationContext()));
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        assertEquals(TimeZone.getTimeZone("UTC"), deserializer.deserialize(mapper.createParser("UTC"), mapper.getDeserializationContext()));
        assertEquals(TimeZone.getTimeZone("America/New_York"), deserializer.deserialize(mapper.createParser("America/New_York"), mapper.getDeserializationContext()));
    }

    @Test
    public void testInetAddressDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
        assertEquals(inetAddress, deserializer.deserialize(mapper.createParser("\"127.0.0.1\""), mapper.getDeserializationContext()));
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
        assertEquals(inetSocketAddress, deserializer.deserialize(mapper.createParser("new InetSocketAddress(\"localhost\", 8080)"), mapper.getDeserializationContext()));
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        FromStringDeserializer<Class<?>> clazzDeserializer = FromStringDeserializer.findDeserializer(Class.class);
        FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);

        // File
        assertNull(fileDeserializer.deserialize(mapper.createParser(""), mapper.getDeserializationContext()));

        // URL
        assertNull(urlDeserializer.deserialize(mapper.createParser(""), mapper.getDeserializationContext()));

        // URI
        assertEquals(URI.create(""), urlDeserializer.deserialize(mapper.createParser("new URI(\"\")"), mapper.getDeserializationContext()));

        // Class
        assertNull(clazzDeserializer.deserialize(mapper.createParser(""), mapper.getDeserializationContext()));

        // JavaType
        assertNull(javaTypeDeserializer.deserialize(mapper.createParser(""), mapper.getDeserializationContext()));

        // Currency
        assertNull(currencyDeserializer.deserialize(mapper.createParser(""), mapper.getDeserializationContext()));

        // Pattern
        assertNull(patternDeserializer.deserialize(mapper.createParser(""), mapper.getDeserializationContext()));

        // Locale
        assertNull(localeDeserializer.deserialize(mapper.createParser(""), mapper.getDeserializationContext()));

        // Charset
        assertNull(charsetDeserializer.deserialize(mapper.createParser(""), mapper.getDeserializationContext()));

        // TimeZone
        assertNull(timeZoneDeserializer.deserialize(mapper.createParser(""), mapper.getDeserializationContext()));

        // InetAddress
        assertNull(inetAddressDeserializer.deserialize(mapper.createParser(""), mapper.getDeserializationContext()));

        // InetSocketAddress
        assertNull(inetSocketAddressDeserializer.deserialize(mapper.createParser(""), mapper.getDeserializationContext()));
    }
}