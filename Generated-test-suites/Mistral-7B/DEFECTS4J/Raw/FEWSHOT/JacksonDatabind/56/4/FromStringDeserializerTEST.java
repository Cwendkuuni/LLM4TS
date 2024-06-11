package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws IOException {
        FromStringDeserializer<File> fileDeserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        DeserializationContext ctxt = new TestContext();
        File result = fileDeserializer.deserialize(new JsonFactory(), ctxt, "path/to/file.txt");
        assertEquals("path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testURLDeserialization() throws IOException {
        FromStringDeserializer<URL> urlDeserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        DeserializationContext ctxt = new TestContext();
        URL result = urlDeserializer.deserialize(new JsonFactory(), ctxt, "http://example.com");
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testURIDeserialization() throws IOException {
        FromStringDeserializer<URI> uriDeserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        DeserializationContext ctxt = new TestContext();
        URI result = uriDeserializer.deserialize(new JsonFactory(), ctxt, "https://example.com/path/to/resource");
        assertEquals("https://example.com/path/to/resource", result.toString());
    }

    @Test
    public void testClassDeserialization() throws IOException {
        FromStringDeserializer<Class<?>> classDeserializer = (FromStringDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(String.class);
        DeserializationContext ctxt = new TestContext();
        Class<?> result = classDeserializer.deserialize(new JsonFactory(), ctxt, "java.lang.String");
        assertEquals(String.class, result);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        FromStringDeserializer<JavaType> javaTypeDeserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
        DeserializationContext ctxt = new TestContext();
        JavaType result = javaTypeDeserializer.deserialize(new JsonFactory(), ctxt, "java.util.List<java.lang.Integer>");
        assertEquals(JavaType.constructType(JavaType.getCollectionType(JavaType.getTypeFactory(), JavaType.getTypeFactory().constructType(Integer.class))), result);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        FromStringDeserializer<Currency> currencyDeserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        DeserializationContext ctxt = new TestContext();
        Currency result = currencyDeserializer.deserialize(new JsonFactory(), ctxt, "USD");
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        FromStringDeserializer<Pattern> patternDeserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        DeserializationContext ctxt = new TestContext();
        Pattern result = patternDeserializer.deserialize(new JsonFactory(), ctxt, "\\d{3}-\\d{2}-\\d{3}");
        assertEquals(Pattern.compile("\\d{3}-\\d{2}-\\d{3}"), result);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        FromStringDeserializer<Locale> localeDeserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        DeserializationContext ctxt = new TestContext();
        Locale result = localeDeserializer.deserialize(new JsonFactory(), ctxt, "en_US");
        assertEquals(Locale.US, result);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        FromStringDeserializer<Charset> charsetDeserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        DeserializationContext ctxt = new TestContext();
        Charset result = charsetDeserializer.deserialize(new JsonFactory(), ctxt, "UTF-8");
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        FromStringDeserializer<TimeZone> timeZoneDeserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        DeserializationContext ctxt = new TestContext();
        TimeZone result = timeZoneDeserializer.deserialize(new JsonFactory(), ctxt, "UTC");
        assertEquals(TimeZone.getTimeZone("UTC"), result);
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        FromStringDeserializer<InetAddress> inetAddressDeserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        DeserializationContext ctxt = new TestContext();
        InetAddress result = inetAddressDeserializer.deserialize(new JsonFactory(), ctxt, "192.168.1.1");
        assertEquals("192.168.1.1", result.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        DeserializationContext ctxt = new TestContext();
        InetSocketAddress result = inetSocketAddressDeserializer.deserialize(new JsonFactory(), ctxt, "[255.255.255.255]:80");
        assertEquals("[255.255.255.255]:80", result.toString());
    }

    @Test
    public void testEmptyStringDeserializationFile() throws IOException {
        FromStringDeserializer<File> fileDeserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        DeserializationContext ctxt = new TestContext();
        File result = fileDeserializer.deserializeFromEmptyString(ctxt);
        assertNull(result);
    }

    @Test
    public void testEmptyStringDeserializationURL() throws IOException {
        FromStringDeserializer<URL> urlDeserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        DeserializationContext ctxt = new TestContext();
        URL result = urlDeserializer.deserializeFromEmptyString(ctxt);
        assertNull(result);
    }

    @Test
    public void testEmptyStringDeserializationURI() throws IOException {
        FromStringDeserializer<URI> uriDeserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        DeserializationContext ctxt = new TestContext();
        URI result = uriDeserializer.deserializeFromEmptyString(ctxt);
        assertEquals(URI.create(""), result);
    }

    @Test
    public void testEmptyStringDeserializationClass() throws IOException {
        FromStringDeserializer<Class<?>> classDeserializer = (FromStringDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(String.class);
        DeserializationContext ctxt = new TestContext();
        Class<?> result = classDeserializer.deserializeFromEmptyString(ctxt);
        assertNull(result);
    }

    @Test
    public void testEmptyStringDeserializationJavaType() throws IOException {
        FromStringDeserializer<JavaType> javaTypeDeserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
        DeserializationContext ctxt = new TestContext();
        JavaType result = javaTypeDeserializer.deserializeFromEmptyString(ctxt);
        assertEquals(JavaType.constructType(Void.TYPE), result);
    }

    @Test
    public void testEmptyStringDeserializationCurrency() throws IOException {
        FromStringDeserializer<Currency> currencyDeserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        DeserializationContext ctxt = new TestContext();
        Currency result = currencyDeserializer.deserializeFromEmptyString(ctxt);
        assertNull(result);
    }

    @Test
    public void testEmptyStringDeserializationPattern() throws IOException {
        FromStringDeserializer<Pattern> patternDeserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        DeserializationContext ctxt = new TestContext();
        Pattern result = patternDeserializer.deserializeFromEmptyString(ctxt);
        assertNull(result);
    }

    @Test
    public void testEmptyStringDeserializationLocale() throws IOException {
        FromStringDeserializer<Locale> localeDeserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        DeserializationContext ctxt = new TestContext();
        Locale result = localeDeserializer.deserializeFromEmptyString(ctxt);
        assertEquals(Locale.ROOT, result);
    }

    @Test
    public void testEmptyStringDeserializationCharset() throws IOException {
        FromStringDeserializer<Charset> charsetDeserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        DeserializationContext ctxt = new TestContext();
        Charset result = charsetDeserializer.deserializeFromEmptyString(ctxt);
        assertEquals(Charsets.UTF_8, result);
    }

    @Test
    public void testEmptyStringDeserializationTimeZone() throws IOException {
        FromStringDeserializer<TimeZone> timeZoneDeserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        DeserializationContext ctxt = new TestContext();
        TimeZone result = timeZoneDeserializer.deserializeFromEmptyString(ctxt);
        assertEquals(TimeZone.getDefault(), result);
    }

    @Test
    public void testEmptyStringDeserializationInetAddress() throws IOException {
        FromStringDeserializer<InetAddress> inetAddressDeserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        DeserializationContext ctxt = new TestContext();
        InetAddress result = inetAddressDeserializer.deserializeFromEmptyString(ctxt);
        assertEquals(InetAddress.getByName(""), result);
    }

    @Test
    public void testEmptyStringDeserializationInetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        DeserializationContext ctxt = new TestContext();
        InetSocketAddress result = inetSocketAddressDeserializer.deserializeFromEmptyString(ctxt);
        assertNull(result);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public JsonParser _currentElement(JsonParser p) {
            return p;
        }

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }
    }
}