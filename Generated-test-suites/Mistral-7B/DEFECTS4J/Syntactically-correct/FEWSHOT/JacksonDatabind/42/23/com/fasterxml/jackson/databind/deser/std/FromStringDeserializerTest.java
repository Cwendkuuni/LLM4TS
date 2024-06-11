package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> TypeDeserializer<T> createDeserializer(Class<T> type) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper.getTypeFactory().constructTypeDeserializer(type);
    }

    @Test
    public void testFileDeserialization() throws IOException {
        JsonFactory factory = new JsonFactory();
        File file = new File("test.txt");
        String fileString = file.getAbsolutePath();
        DeserializationContext ctxt = new DeserializationContext(factory);
        FromStringDeserializer<File> deserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(factory.createParser(new StringReader(fileString)), ctxt);
        assertEquals(file, result);
    }

    @Test
    public void testUrlDeserialization() throws IOException {
        JsonFactory factory = new JsonFactory();
        String urlString = "http://www.example.com";
        DeserializationContext ctxt = new DeserializationContext(factory);
        FromStringDeserializer<URL> deserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(factory.createParser(new StringReader(urlString)), ctxt);
        assertEquals(new URL(urlString), result);
    }

    @Test
    public void testUriDeserialization() throws IOException {
        JsonFactory factory = new JsonFactory();
        String uriString = "http://www.example.com/path/to/file";
        DeserializationContext ctxt = new DeserializationContext(factory);
        FromStringDeserializer<URI> deserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(factory.createParser(new StringReader(uriString)), ctxt);
        assertEquals(new URI(uriString), result);
    }

    @Test
    public void testClassDeserialization() throws IOException {
        JsonFactory factory = new JsonFactory();
        String className = "java.lang.String";
        DeserializationContext ctxt = new DeserializationContext(factory);
        FromStringDeserializer<Class<?>> deserializer = (FromStringDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(Class.class);
        Class<?> result = deserializer.deserialize(factory.createParser(new StringReader(className)), ctxt);
        assertEquals(Class.forName(className), result);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        JsonFactory factory = new JsonFactory();
        String javaTypeString = "java.util.List<java.lang.String>";
        DeserializationContext ctxt = new DeserializationContext(factory);
        JavaType javaType = new ObjectMapper().getTypeFactory().constructType(javaTypeString);
        FromStringDeserializer<JavaType> deserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType result = deserializer.deserialize(factory.createParser(new StringReader(javaTypeString)), ctxt);
        assertEquals(javaType, result);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        JsonFactory factory = new JsonFactory();
        String currencyString = "USD";
        DeserializationContext ctxt = new DeserializationContext(factory);
        FromStringDeserializer<Currency> deserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(factory.createParser(new StringReader(currencyString)), ctxt);
        assertEquals(Currency.getInstance(currencyString), result);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        JsonFactory factory = new JsonFactory();
        String patternString = "\\w+@\\w+\\.\\w+";
        DeserializationContext ctxt = new DeserializationContext(factory);
        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(factory.createParser(new StringReader(patternString)), ctxt);
        assertEquals(Pattern.compile(patternString), result);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        JsonFactory factory = new JsonFactory();
        String localeString = "en_US";
        DeserializationContext ctxt = new DeserializationContext(factory);
        FromStringDeserializer<Locale> deserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(factory.createParser(new StringReader(localeString)), ctxt);
        assertEquals(Locale.forLanguageTag(localeString), result);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        JsonFactory factory = new JsonFactory();
        String charsetString = "UTF-8";
        DeserializationContext ctxt = new DeserializationContext(factory);
        FromStringDeserializer<Charset> deserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(factory.createParser(new StringReader(charsetString)), ctxt);
        assertEquals(Charset.forName(charsetString), result);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        JsonFactory factory = new JsonFactory();
        String timeZoneString = "America/New_York";
        DeserializationContext ctxt = new DeserializationContext(factory);
        FromStringDeserializer<TimeZone> deserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(factory.createParser(new StringReader(timeZoneString)), ctxt);
        assertEquals(TimeZone.getTimeZone(timeZoneString), result);
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        JsonFactory factory = new JsonFactory();
        String ipv4AddressString = "192.168.1.1";
        String ipv6AddressString = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]";
        String ipv6AddressWithPortString = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80";
        String invalidIpv6AddressWithPortString = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80:80";
        DeserializationContext ctxt = new DeserializationContext(factory);
        FromStringDeserializer<InetAddress> deserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress ipv4AddressResult = deserializer.deserialize(factory.createParser(new StringReader(ipv4AddressString)), ctxt);
        InetAddress ipv6AddressResult = deserializer.deserialize(factory.createParser(new StringReader(ipv6AddressString)), ctxt);
        InetAddress ipv6AddressWithPortResult = deserializer.deserialize(factory.createParser(new StringReader(ipv6AddressWithPortString)), ctxt);
        InetAddress ipv6AddressWithPortInvalidResult = deserializer.deserialize(factory.createParser(new StringReader(invalidIpv6AddressWithPortString)), ctxt);
        assertEquals(InetAddress.getByName(ipv4AddressString), ipv4AddressResult);
        assertEquals(InetAddress.getByName(ipv6AddressString.substring(1, ipv6AddressString.length()-1)), ipv6AddressResult);
        assertEquals(new InetSocketAddress(ipv6AddressString.substring(1, ipv6AddressString.length()-1), 80), ipv6AddressWithPortResult);
        assertNull(ipv6AddressWithPortInvalidResult);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        JsonFactory factory = new JsonFactory();
        String ipv4AddressString = "192.168.1.1:80";
        String ipv6AddressString = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80";
        String invalidIpv6AddressString = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80:80";
        DeserializationContext ctxt = new DeserializationContext(factory);
        FromStringDeserializer<InetSocketAddress> deserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress ipv4AddressResult = deserializer.deserialize(factory.createParser(new StringReader(ipv4AddressString)), ctxt);
        InetSocketAddress ipv6AddressResult = deserializer.deserialize(factory.createParser(new StringReader(ipv6AddressString)), ctxt);
        InetSocketAddress ipv6AddressInvalidResult = deserializer.deserialize(factory.createParser(new StringReader(invalidIpv6AddressString)), ctxt);
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 80), ipv4AddressResult);
        assertEquals(new InetSocketAddress(InetAddress.getByName(ipv6AddressString.substring(1, ipv6AddressString.length()-1)), 80), ipv6AddressResult);
        assertNull(ipv6AddressInvalidResult);
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        JsonFactory factory = new JsonFactory();
        DeserializationContext ctxt = new DeserializationContext(factory);
        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
        Object result = deserializer.deserialize(factory.createParser(new StringReader("")), ctxt);
        assertNull(result);
    }
}