package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.time.ZoneId;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(File.class, FromStringDeserializer.findDeserializer(File.class));
        module.addDeserializer(URL.class, FromStringDeserializer.findDeserializer(URL.class));
        module.addDeserializer(URI.class, FromStringDeserializer.findDeserializer(URI.class));
        module.addDeserializer(Class.class, FromStringDeserializer.findDeserializer(Class.class));
        module.addDeserializer(JavaType.class, FromStringDeserializer.findDeserializer(JavaType.class));
        module.addDeserializer(Currency.class, FromStringDeserializer.findDeserializer(Currency.class));
        module.addDeserializer(Pattern.class, FromStringDeserializer.findDeserializer(Pattern.class));
        module.addDeserializer(Locale.class, FromStringDeserializer.findDeserializer(Locale.class));
        module.addDeserializer(Charset.class, FromStringDeserializer.findDeserializer(Charset.class));
        module.addDeserializer(TimeZone.class, FromStringDeserializer.findDeserializer(TimeZone.class));
        module.addDeserializer(InetAddress.class, FromStringDeserializer.findDeserializer(InetAddress.class));
        module.addDeserializer(InetSocketAddress.class, FromStringDeserializer.findDeserializer(InetSocketAddress.class));
        module.addDeserializer(StringBuilder.class, FromStringDeserializer.findDeserializer(StringBuilder.class));
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testFileDeserialization() throws IOException {
        String filePath = "path/to/file.txt";
        File file = objectMapper.readValue("{\"className\":\"java.io.File\",\"value\":\"" + filePath + "\"}", File.class);
        assertTrue(file.exists());
    }

    @Test
    public void testURLDeserialization() throws IOException {
        String urlString = "http://example.com";
        URL url = objectMapper.readValue("{\"className\":\"java.net.URL\",\"value\":\"" + urlString + "\"}", URL.class);
        assertEquals(urlString, url.toString());
    }

    @Test
    public void testURIDeserialization() throws IOException {
        String uriString = "http://example.com/path/to/resource#fragment";
        URI uri = objectMapper.readValue("{\"className\":\"java.net.URI\",\"value\":\"" + uriString + "\"}", URI.class);
        assertEquals(uriString, uri.toString());
    }

    @Test
    public void testClassDeserialization() throws IOException, ClassNotFoundException {
        String className = "java.lang.StringBuilder";
        Class<?> clazz = objectMapper.readValue("{\"className\":\"" + className + "\",\"value\":\"new \"" + className + "()\"}", Class.class);
        assertEquals( classicalClass(StringBuilder.class), clazz);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        String typeName = "java.util.List<java.lang.String>";
        JavaType javaType = objectMapper.readValue("{\"className\":\"com.fasterxml.jackson.databind.JavaType\",\"value\":{\"type\":\"" + typeName + "\"}}", JavaType.class);
        assertEquals( classicalType(List.class, String.class), javaType);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        String currencyString = "USD";
        Currency currency = objectMapper.readValue("{\"className\":\"java.util.Currency\",\"value\":\"" + currencyString + "\"}", Currency.class);
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        String patternString = "[a-z]+";
        Pattern pattern = objectMapper.readValue("{\"className\":\"java.util.regex.Pattern\",\"value\":\"" + patternString + "\"}", Pattern.class);
        assertNotNull(pattern);
        assertTrue(pattern.matcher("apple").matches());
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        String localeString = "en_US";
        Locale locale = objectMapper.readValue("{\"className\":\"java.util.Locale\",\"value\":\"" + localeString + "\"}", Locale.class);
        assertEquals(Locale.US, locale);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        String charsetString = "UTF-8";
        Charset charset = objectMapper.readValue("{\"className\":\"java.nio.charset.Charset\",\"value\":\"" + charsetString + "\"}", Charset.class);
        assertEquals(Charset.forName(charsetString), charset);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        String timeZoneString = "America/Los_Angeles";
        TimeZone timeZone = objectMapper.readValue("{\"className\":\"java.util.TimeZone\",\"value\":\"" + timeZoneString + "\"}", TimeZone.class);
        assertEquals(TimeZone.getTimeZone(timeZoneString), timeZone);
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        String ipAddressString = "192.168.1.1";
        InetAddress ipAddress = objectMapper.readValue("{\"className\":\"java.net.InetAddress\",\"value\":\"" + ipAddressString + "\"}", InetAddress.class);
        assertEquals(InetAddress.getByName(ipAddressString), ipAddress);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        String ipAddressString = "192.168.1.1";
        String portString = "8080";
        InetSocketAddress inetSocketAddress = objectMapper.readValue("{\"className\":\"java.net.InetSocketAddress\",\"value\":{\"address\":\"" + ipAddressString + "\",\"port\":\"" + portString + "\"}}", InetSocketAddress.class);
        assertEquals(new InetSocketAddress(InetAddress.getByName(ipAddressString), Integer.parseInt(portString)), inetSocketAddress);
    }

    @Test
    public void testStringBuilderDeserialization() throws IOException {
        String initialValue = "initial value";
        StringBuilder stringBuilder = objectMapper.readValue("{\"className\":\"java.lang.StringBuilder\",\"value\":\"" + initialValue + "\"}", StringBuilder.class);
        assertEquals(initialValue, stringBuilder.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserialization() throws IOException {
        String invalidValue = "invalid value";
        objectMapper.readValue("{\"className\":\"java.net.URL\",\"value\":\"" + invalidValue + "\"}", URL.class);
    }

    private Class<?> classicalClass(Class<?> clazz) {
        try {
            return Class.forName(clazz.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private JavaType classicalType(Class<?> componentType, Class<?> containerType) {
        try {
            return objectMapper.getTypeFactory().constructParametricType(containerType, new Class[]{componentType});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}