package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<File> fileDeserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        File result = fileDeserializer.deserialize(mapper.getFactory().createParser("path/to/file.txt"), null);
        assertEquals(new File("path/to/file.txt"), result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeUrl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> urlDeserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        URL result = urlDeserializer.deserialize(mapper.getFactory().createParser("http://example.com"), null);
        assertEquals(new URL("http://example.com"), result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeUri() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> uriDeserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        URI result = uriDeserializer.deserialize(mapper.getFactory().createParser("http://example.com/path/to/file"), null);
        assertEquals(new URI("http://example.com/path/to/file"), result);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<String> stringDeserializer = (FromStringDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
        String result = stringDeserializer.deserialize(mapper.getFactory().createParser("java.lang.String"), null);
        assertEquals("java.lang.String", result.getClass().getName());
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<JavaType> javaTypeDeserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType result = javaTypeDeserializer.deserialize(mapper.getFactory().createParser("com.example.MyClass"), null);
        assertEquals(JavaType.class, result.getClass());
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Currency> currencyDeserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = currencyDeserializer.deserialize(mapper.getFactory().createParser("USD"), null);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Pattern> patternDeserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = patternDeserializer.deserialize(mapper.getFactory().createParser("/foo/bar"), null);
        assertEquals(Pattern.compile("/foo/bar"), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> localeDeserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = localeDeserializer.deserialize(mapper.getFactory().createParser("en_US"), null);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Charset> charsetDeserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = charsetDeserializer.deserialize(mapper.getFactory().createParser("UTF-8"), null);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<TimeZone> timeZoneDeserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = timeZoneDeserializer.deserialize(mapper.getFactory().createParser("America/Los_Angeles"), null);
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetAddress> inetAddressDeserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = inetAddressDeserializer.deserialize(mapper.getFactory().createParser("192.168.1.1"), null);
        assertEquals(InetAddress.getByName("192.168.1.1"), result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80"), null);
        assertEquals(new InetSocketAddress(InetAddress.getByName("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]"), 80), result);
    }

    @Test
    public void testDeserializeUnknownType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Object> objectDeserializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Integer.class);
        try {
            Object result = objectDeserializer.deserialize(mapper.getFactory().createParser("123"), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Don't know how to convert embedded Object of type class [B to class java.lang.Integer", e.getMessage());
        }
    }

    @Test
    public void testDeserializeNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<String> stringDeserializer = (FromStringDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
        String result = stringDeserializer.deserialize(mapper.getFactory().createParser(null), null);
        assertNull(result);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<String> stringDeserializer = (FromStringDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
        String result = stringDeserializer.deserialize(mapper.getFactory().createParser(""), null);
        assertNull(result);
    }

    @Test
    public void testDeserializeEmptyStringUri() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> uriDeserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        URI result = uriDeserializer.deserialize(mapper.getFactory().createParser(""), null);
        assertEquals(URI.create(""), result);
    }

    @Test
    public void testDeserializeEmptyStringLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> localeDeserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = localeDeserializer.deserialize(mapper.getFactory().createParser(""), null);
        assertEquals(Locale.ROOT, result);
    }
}