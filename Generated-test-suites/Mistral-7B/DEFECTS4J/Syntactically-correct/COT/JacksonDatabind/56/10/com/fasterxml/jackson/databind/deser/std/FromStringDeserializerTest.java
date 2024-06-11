package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserialize_File() throws IOException {
        FromStringDeserializer<File> deserializer = new FromStringDeserializer<File>(File.class);

        String fileString = "path/to/file.txt";
        File expectedFile = new File(fileString);

        File resultFile = deserializer.deserialize(getJsonParser(mapper, "{\"className\":\"File\", \"value\":\"" + fileString + "\"}"), null);

        assertEquals(expectedFile, resultFile);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserialize_URL() throws IOException {
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class);

        String urlString = "http://example.com";
        URL expectedUrl = new URL(urlString);

        URL resultUrl = deserializer.deserialize(getJsonParser(mapper, "{\"className\":\"URL\", \"value\":\"" + urlString + "\"}"), null);

        assertEquals(expectedUrl, resultUrl);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserialize_URI() throws IOException {
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer<URI>(URI.class);

        String uriString = "http://example.com";
        URI expectedUri = new URI(uriString);

        URI resultUri = deserializer.deserialize(getJsonParser(mapper, "{\"className\":\"URI\", \"value\":\"" + uriString + "\"}"), null);

        assertEquals(expectedUri, resultUri);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserialize_Class() throws IOException {
        FromStringDeserializer<Class<?>> deserializer = new FromStringDeserializer<Class<?>>(Class.class);

        String className = "java.lang.String";
        Class<?> expectedClass = Class.forName(className);

        Class<?> resultClass = deserializer.deserialize(getJsonParser(mapper, "{\"className\":\"Class\", \"value\":\"" + className + "\"}"), null);

        assertEquals(expectedClass, resultClass);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserialize_JavaType() throws IOException {
        FromStringDeserializer<JavaType> deserializer = new FromStringDeserializer<JavaType>(JavaType.class);

        String jsonTypeString = "com.example.MyClass";
        JavaType expectedJavaType = mapper.getTypeFactory().constructType(Class.forName(jsonTypeString));

        JavaType resultJavaType = deserializer.deserialize(getJsonParser(mapper, "{\"className\":\"JavaType\", \"value\":\"" + jsonTypeString + "\"}"), null);

        assertEquals(expectedJavaType, resultJavaType);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserialize_Currency() throws IOException {
        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer<Currency>(Currency.class);

        String currencyString = "USD";
        Currency expectedCurrency = Currency.getInstance(currencyString);

        Currency resultCurrency = deserializer.deserialize(getJsonParser(mapper, "{\"className\":\"Currency\", \"value\":\"" + currencyString + "\"}"), null);

        assertEquals(expectedCurrency, resultCurrency);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserialize_Pattern() throws IOException {
        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer<Pattern>(Pattern.class);

        String patternString = "\\w+";
        Pattern expectedPattern = Pattern.compile(patternString);

        Pattern resultPattern = deserializer.deserialize(getJsonParser(mapper, "{\"className\":\"Pattern\", \"value\":\"" + patternString + "\"}"), null);

        assertEquals(expectedPattern, resultPattern);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserialize_Locale() throws IOException {
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<Locale>(Locale.class);

        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");

        Locale resultLocale = deserializer.deserialize(getJsonParser(mapper, "{\"className\":\"Locale\", \"value\":\"" + localeString + "\"}"), null);

        assertEquals(expectedLocale, resultLocale);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserialize_Charset() throws IOException {
        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer<Charset>(Charset.class);

        String charsetString = "UTF-8";
        Charset expectedCharset = Charset.forName(charsetString);

        Charset resultCharset = deserializer.deserialize(getJsonParser(mapper, "{\"className\":\"Charset\", \"value\":\"" + charsetString + "\"}"), null);

        assertEquals(expectedCharset, resultCharset);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserialize_TimeZone() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);

        String timeZoneString = "America/Los_Angeles";
        TimeZone expectedTimeZone = TimeZone.getTimeZone(timeZoneString);

        TimeZone resultTimeZone = deserializer.deserialize(getJsonParser(mapper, "{\"className\":\"TimeZone\", \"value\":\"" + timeZoneString + "\"}"), null);

        assertEquals(expectedTimeZone, resultTimeZone);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserialize_InetAddress() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);

        String ipv4AddressString = "192.168.1.1";
        InetAddress expectedIpv4Address = InetAddress.getByName(ipv4AddressString);

        InetAddress resultIpv4Address = deserializer.deserialize(getJsonParser(mapper, "{\"className\":\"InetAddress\", \"value\":\"" + ipv4AddressString + "\"}"), null);

        assertEquals(expectedIpv4Address, resultIpv4Address);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserialize_InetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);

        String ipv6AddressString = "[2001:db8:85a3:0:0:8a2e:370:7334]";
        String portString = "5000";
        InetSocketAddress expectedInetSocketAddress = new InetSocketAddress(ipv6AddressString, Integer.parseInt(portString));

        InetSocketAddress resultInetSocketAddress = deserializer.deserialize(getJsonParser(mapper, "{\"className\":\"InetSocketAddress\", \"value\":\"" + ipv6AddressString + ":" + portString + "\"}"), null);

        assertEquals(expectedInetSocketAddress, resultInetSocketAddress);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserialize_EmptyString() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);

        Object emptyResult = deserializer.deserialize(getJsonParser(mapper, "{\"className\":\"Object\", \"value\":\"\"}"), null);

        assertEquals(null, emptyResult);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidString() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);

        String invalidString = "InvalidValue";
        deserializer.deserialize(getJsonParser(mapper, "{\"className\":\"Object\", \"value\":\"" + invalidString + "\"}"), null);
    }

    private JsonParser getJsonParser(ObjectMapper mapper, String jsonString) throws IOException {
        ObjectNode node = mapper.readValue(jsonString, ObjectNode.class);
        return mapper.treeAsTokens(node);
    }

}