package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws Exception {
        FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer.Std(File.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"/path/to/file.txt\"";
        File result = mapper.readValue(jsonString, File.class, new DeserializationContext(null));
        assertEquals(new File("/path/to/file.txt"), result);
    }

    @Test
    public void testURLDeserialization() throws Exception {
        FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer.Std(URL.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"https://www.example.com\"";
        URL result = mapper.readValue(jsonString, URL.class, new DeserializationContext(null));
        assertEquals(new URL("https://www.example.com"), result);
    }

    @Test
    public void testURIDeserialization() throws Exception {
        FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer.Std(URI.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"https://www.example.com/path/to/file.txt\"";
        URI result = mapper.readValue(jsonString, URI.class, new DeserializationContext(null));
        assertEquals(new URI("https://www.example.com/path/to/file.txt"), result);
    }

    @Test
    public void testClassDeserialization() throws Exception {
        FromStringDeserializer<Class<?>> classDeserializer = new FromStringDeserializer.Std(Class.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"java.lang.String\"";
        Class<?> result = mapper.readValue(jsonString, Class.class, new DeserializationContext(null));
        assertEquals(String.class, result);
    }

    @Test
    public void testJavaTypeDeserialization() throws Exception {
        FromStringDeserializer<JavaType> javaTypeDeserializer = new FromStringDeserializer.Std(JavaType.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"com.example:java.util.List<java.lang.Integer>\"]\"";
        JavaType result = mapper.readValue(jsonString, JavaType.class, new DeserializationContext(null));
        assertEquals(JavaType.construct(ClassUtil.loadClass(mapper, "com.example.List"), Integer.class), result);
    }

    @Test
    public void testCurrencyDeserialization() throws Exception {
        FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer.Std(Currency.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "$USD";
        Currency result = mapper.readValue(jsonString, Currency.class, new DeserializationContext(null));
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testPatternDeserialization() throws Exception {
        FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer.Std(Pattern.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"\\w{3,5}\\.\\w{2,3}\\b\"";
        Pattern result = mapper.readValue(jsonString, Pattern.class, new DeserializationContext(null));
        assertNotNull(result);
        assertEquals(Pattern.compile("\\w{3,5}\\.\\w{2,3}\\b"), result);
    }

    @Test
    public void testLocaleDeserialization() throws Exception {
        FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer.Std(Locale.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"en_US\"";
        Locale result = mapper.readValue(jsonString, Locale.class, new DeserializationContext(null));
        assertEquals(Locale.US, result);

        jsonString = "\"en_GB_x\""; // invalid locale
        try {
            mapper.readValue(jsonString, Locale.class, new DeserializationContext(null));
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        jsonString = "\"en_GB_x-latn\""; // valid locale with script
        Locale result2 = mapper.readValue(jsonString, Locale.class, new DeserializationContext(null));
        assertEquals(Locale.getLocale("en", "GB", "x", "latn"), result2);
    }

    @Test
    public void testCharsetDeserialization() throws Exception {
        FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer.Std(Charset.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"UTF-8\"";
        Charset result = mapper.readValue(jsonString, Charset.class, new DeserializationContext(null));
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testTimeZoneDeserialization() throws Exception {
        FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"America/New_York\"";
        TimeZone result = mapper.readValue(jsonString, TimeZone.class, new DeserializationContext(null));
        assertEquals(TimeZone.getTimeZone("America/New_York"), result);
    }

    @Test
    public void testInetAddressDeserialization() throws Exception {
        FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"192.168.1.1\"";
        InetAddress result = mapper.readValue(jsonString, InetAddress.class, new DeserializationContext(null));
        assertEquals(InetAddress.getByName("192.168.1.1"), result);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws Exception {
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "[\"192.168.1.1\", 80]";
        InetSocketAddress result = mapper.readValue(jsonString, InetSocketAddress.class, new DeserializationContext(null));
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 80), result);

        jsonString = "[\"::1\", 80]"; // IPv6 with port
        InetSocketAddress result2 = mapper.readValue(jsonString, InetSocketAddress.class, new DeserializationContext(null));
        assertEquals(new InetSocketAddress(InetAddress.getByAddress(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }), 80), result2);

        jsonString = "\"192.168.1.1:80\""; // IPv4 with port
        InetSocketAddress result3 = mapper.readValue(jsonString, InetSocketAddress.class, new DeserializationContext(null));
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 80), result3);
    }
}