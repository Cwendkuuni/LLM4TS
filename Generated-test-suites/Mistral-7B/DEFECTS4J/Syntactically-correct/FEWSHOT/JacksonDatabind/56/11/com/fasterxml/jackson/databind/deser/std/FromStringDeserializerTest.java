package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FromStringDeserializerTest {

    @Test
    public void testStdDeserialize() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        // Test deserializing various types from strings
        String jsonString = "\"foo.txt\"";
        File file = mapper.readerForUpdating(File.class).readValue(jsonString);
        assertEquals("foo.txt", file.getName());

        jsonString = "\"http://example.com/api\"";
        URL url = mapper.readerForUpdating(URL.class).readValue(jsonString);
        assertEquals("http://example.com/api", url.toString());

        jsonString = "\"http://example.com/api?param=value\"";
        URI uri = mapper.readerForUpdating(URI.class).readValue(jsonString);
        assertEquals("http://example.com/api?param=value", uri.toString());

        jsonString = "java.lang.String";
        Class<?> clazz = mapper.readerForUpdating(Class.class).readValue(jsonString);
        assertEquals(String.class, clazz);

        jsonString = "com.example.MyClass";
        JavaType javaType = mapper.readerForUpdating(JavaType.class).readValue(jsonString);
        assertEquals(mapper.getTypeFactory().constructType(MyClass.class), javaType);

        jsonString = "USD";
        Currency currency = mapper.readerForUpdating(Currency.class).readValue(jsonString);
        assertEquals(Currency.getInstance("USD"), currency);

        jsonString = "/pattern/[a-z]+/g";
        Pattern pattern = mapper.readerForUpdating(Pattern.class).readValue(jsonString);
        assertEquals("/pattern/[a-z]+/g", pattern.toString());

        jsonString = "en_US";
        Locale locale = mapper.readerForUpdating(Locale.class).readValue(jsonString);
        assertEquals(Locale.US, locale);

        jsonString = "UTF-8";
        Charset charset = mapper.readerForUpdating(Charset.class).readValue(jsonString);
        assertEquals(Charset.forName("UTF-8"), charset);

        jsonString = "Europe/London";
        TimeZone timeZone = mapper.readerForUpdating(TimeZone.class).readValue(jsonString);
        assertEquals(TimeZone.getTimeZone("Europe/London"), timeZone);

        jsonString = "192.168.1.1";
        InetAddress inetAddress = mapper.readerForUpdating(InetAddress.class).readValue(jsonString);
        assertNotNull(inetAddress);

        jsonString = "[::1]:8080";
        InetSocketAddress inetSocketAddress = mapper.readerForUpdating(InetSocketAddress.class).readValue(jsonString);
        assertNotNull(inetSocketAddress);
        assertEquals("[::1]", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());

        jsonString = "\"\"";
        File emptyFile = mapper.readerForUpdating(File.class).readValue(jsonString);
        assertNotNull(emptyFile);
        assertEquals("", emptyFile.getName());

        jsonString = "null";
        Object nullValue = mapper.readerForUpdating(Object.class).readValue(jsonString);
        assertNull(nullValue);

        jsonString = "[]";
        Object emptyArray = mapper.readerForUpdating(Object.class).readValue(jsonString);
        assertNotNull(emptyArray);
        assertEquals(JsonToken.START_ARRAY, mapper.readTree(new ByteArrayInputStream(jsonString.getBytes())).getCurrentToken());

        // Test deserializing null values
        jsonString = "null";
        Object nullDeserialized = mapper.readerForUpdating(Object.class).readValue(jsonString);
        assertNull(nullDeserialized);

        jsonString = "null";
        File nullFile = mapper.readerForUpdating(File.class).readValue(jsonString);
        assertNull(nullFile);

        jsonString = "null";
        URL nullUrl = mapper.readerForUpdating(URL.class).readValue(jsonString);
        assertNull(nullUrl);

        jsonString = "null";
        URI nullUri = mapper.readerForUpdating(URI.class).readValue(jsonString);
        assertNull(nullUri);

        jsonString = "null";
        Class<?> nullClazz = mapper.readerForUpdating(Class.class).readValue(jsonString);
        assertNull(nullClazz);

        jsonString = "null";
        JavaType nullJavaType = mapper.readerForUpdating(JavaType.class).readValue(jsonString);
        assertNull(nullJavaType);

        jsonString = "null";
        Currency nullCurrency = mapper.readerForUpdating(Currency.class).readValue(jsonString);
        assertNull(nullCurrency);

        jsonString = "null";
        Pattern nullPattern = mapper.readerForUpdating(Pattern.class).readValue(jsonString);
        assertNull(nullPattern);

        jsonString = "null";
        Locale nullLocale = mapper.readerForUpdating(Locale.class).readValue(jsonString);
        assertNull(nullLocale);

        jsonString = "null";
        Charset nullCharset = mapper.readerForUpdating(Charset.class).readValue(jsonString);
        assertNull(nullCharset);

        jsonString = "null";
        TimeZone nullTimeZone = mapper.readerForUpdating(TimeZone.class).readValue(jsonString);
        assertNull(nullTimeZone);

        jsonString = "null";
        InetAddress nullInetAddress = mapper.readerForUpdating(InetAddress.class).readValue(jsonString);
        assertNull(nullInetAddress);

        jsonString = "null";
        InetSocketAddress nullInetSocketAddress = mapper.readerForUpdating(InetSocketAddress.class).readValue(jsonString);
        assertNull(nullInetSocketAddress);
    }

    @Test
    public void testStdDeserializeWithEmptyString() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        // Test deserializing various types from empty strings
        String jsonString = "\"\"";
        File emptyFile = mapper.readerForUpdating(File.class).readValue(jsonString);
        assertEquals("", emptyFile.getName());

        jsonString = "\"\"";
        URL emptyUrl = mapper.readerForUpdating(URL.class).readValue(jsonString);
        assertNull(emptyUrl);

        jsonString = "\"\"";
        URI emptyUri = mapper.readerForUpdating(URI.class).readValue(jsonString);
        assertNull(emptyUri);

        jsonString = "\"\"";
        Class<?> emptyClazz = mapper.readerForUpdating(Class.class).readValue(jsonString);
        assertNull(emptyClazz);

        jsonString = "\"\"";
        JavaType emptyJavaType = mapper.readerForUpdating(JavaType.class).readValue(jsonString);
        assertNull(emptyJavaType);

        jsonString = "\"\"";
        Currency emptyCurrency = mapper.readerForUpdating(Currency.class).readValue(jsonString);
        assertNull(emptyCurrency);

        jsonString = "\"\"";
        Pattern emptyPattern = mapper.readerForUpdating(Pattern.class).readValue(jsonString);
        assertNull(emptyPattern);

        jsonString = "\"\"";
        Locale emptyLocale = mapper.readerForUpdating(Locale.class).readValue(jsonString);
        assertEquals(Locale.ROOT, emptyLocale);

        jsonString = "\"\"";
        Charset emptyCharset = mapper.readerForUpdating(Charset.class).readValue(jsonString);
        assertNull(emptyCharset);

        jsonString = "\"\"";
        TimeZone emptyTimeZone = mapper.readerForUpdating(TimeZone.class).readValue(jsonString);
        assertEquals(TimeZone.getDefault(), emptyTimeZone);

        jsonString = "\"\"";
        InetAddress emptyInetAddress = mapper.readerForUpdating(InetAddress.class).readValue(jsonString);
        assertNull(emptyInetAddress);

        jsonString = "\"\"";
        InetSocketAddress emptyInetSocketAddress = mapper.readerForUpdating(InetSocketAddress.class).readValue(jsonString);
        assertNull(emptyInetSocketAddress);

        jsonString = "null";
        File nullFile = mapper.readerForUpdating(File.class).readValue(jsonString);
        assertNull(nullFile);

        jsonString = "null";
        URL nullUrl = mapper.readerForUpdating(URL.class).readValue(jsonString);
        assertNull(nullUrl);

        jsonString = "null";
        URI nullUri = mapper.readerForUpdating(URI.class).readValue(jsonString);
        assertNull(nullUri);

        jsonString = "null";
        Class<?> nullClazz = mapper.readerForUpdating(Class.class).readValue(jsonString);
        assertNull(nullClazz);

        jsonString = "null";
        JavaType nullJavaType = mapper.readerForUpdating(JavaType.class).readValue(jsonString);
        assertNull(nullJavaType);

        jsonString = "null";
        Currency nullCurrency = mapper.readerForUpdating(Currency.class).readValue(jsonString);
        assertNull(nullCurrency);

        jsonString = "null";
        Pattern nullPattern = mapper.readerForUpdating(Pattern.class).readValue(jsonString);
        assertNull(nullPattern);

        jsonString = "null";
        Locale nullLocale = mapper.readerForUpdating(Locale.class).readValue(jsonString);
        assertNull(nullLocale);

        jsonString = "null";
        Charset nullCharset = mapper.readerForUpdating(Charset.class).readValue(jsonString);
        assertNull(nullCharset);

        jsonString = "null";
        TimeZone nullTimeZone = mapper.readerForUpdating(TimeZone.class).readValue(jsonString);
        assertNull(nullTimeZone);

        jsonString = "null";
        InetAddress nullInetAddress = mapper.readerForUpdating(InetAddress.class).readValue(jsonString);
        assertNull(nullInetAddress);

        jsonString = "null";
        InetSocketAddress nullInetSocketAddress = mapper.readerForUpdating(InetSocketAddress.class).readValue(jsonString);
        assertNull(nullInetSocketAddress);
    }
}