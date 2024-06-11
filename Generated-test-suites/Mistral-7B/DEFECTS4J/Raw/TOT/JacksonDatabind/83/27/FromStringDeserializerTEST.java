package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class FromStringDeserializerTest {

    private static ObjectMapper mapper = new JsonMapper();

    @Test
    public void testFileDeserialization() throws IOException {
        String json = "\"/path/to/file.txt\"";
        FromStringDeserializer deserializer = findDeserializer(File.class);
        File file = (File) deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertNotNull(file);
        Assert.assertEquals("/path/to/file.txt", file.getAbsolutePath());
    }

    @Test
    public void testUrlDeserialization() throws IOException {
        String json = "\"http://example.com:8080/test\"";
        FromStringDeserializer deserializer = findDeserializer(URL.class);
        URL url = (URL) deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertNotNull(url);
        Assert.assertEquals("http", url.getProtocol());
        Assert.assertEquals("example.com", url.getHost());
        Assert.assertEquals(8080, url.getPort());
        Assert.assertEquals("/test", url.getPath());
    }

    @Test
    public void testUriDeserialization() throws IOException {
        String json = "\"http://example.com:8080/test?param1=value1&param2=value2#fragment\"";
        FromStringDeserializer deserializer = findDeserializer(URI.class);
        URI uri = (URI) deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertNotNull(uri);
        Assert.assertEquals("http", uri.getScheme());
        Assert.assertEquals("example.com", uri.getHost());
        Assert.assertEquals(8080, uri.getPort());
        Assert.assertEquals("/test", uri.getPath());
        Assert.assertEquals("param1=value1&param2=value2", uri.getQuery());
        Assert.assertEquals("fragment", uri.getFragment());
    }

    @Test
    public void testClassDeserialization() throws IOException, ClassNotFoundException {
        String json = "\"java.lang.String\"";
        FromStringDeserializer deserializer = findDeserializer(String.class);
        String string = (String) deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertNotNull(string);
        Assert.assertEquals("java.lang.String", string.getClass().getName());
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        String json = "\"com.example.MyClass\"";
        FromStringDeserializer deserializer = findDeserializer(JavaType.class);
        JavaType javaType = (JavaType) deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertNotNull(javaType);
        Assert.assertEquals(MyClass.class, javaType.getRawClass());
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        String json = "\"USD\"";
        FromStringDeserializer deserializer = findDeserializer(Currency.class);
        Currency currency = (Currency) deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertNotNull(currency);
        Assert.assertEquals("USD", currency.getCurrencyCode());
        Assert.assertEquals("United States Dollar", currency.getDisplayName(Locale.US));
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        String json = "\"/[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*\"";
        FromStringDeserializer deserializer = findDeserializer(Pattern.class);
        Pattern pattern = (Pattern) deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertNotNull(pattern);
        // Test pattern matcher
        Assert.assertTrue(pattern.matcher("example.com").matches());
        Assert.assertTrue(pattern.matcher("www.example.com").matches());
        Assert.assertTrue(pattern.matcher("subdomain.example.com").matches());
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        String json = "\"en_US\"";
        FromStringDeserializer deserializer = findDeserializer(Locale.class);
        Locale locale = (Locale) deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertNotNull(locale);
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("US", locale.getCountry());

        json = "\"en_US_POSIX\"";
        locale = (Locale) deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertNotNull(locale);
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals(null, locale.getCountry());
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        String json = "\"UTF-8\"";
        FromStringDeserializer deserializer = findDeserializer(Charset.class);
        Charset charset = (Charset) deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertNotNull(charset);
        Assert.assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        String json = "\"UTC\"";
        FromStringDeserializer deserializer = findDeserializer(TimeZone.class);
        TimeZone timeZone = (TimeZone) deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertNotNull(timeZone);
        Assert.assertEquals("UTC", timeZone.getID());
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        String json = "\"192.168.1.1\"";
        FromStringDeserializer deserializer = findDeserializer(InetAddress.class);
        InetAddress inetAddress = (InetAddress) deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertNotNull(inetAddress);
        Assert.assertEquals("192.168.1.1", inetAddress.getHostAddress());

        json = "[::1]";
        inetAddress = (InetAddress) deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertNotNull(inetAddress);
        Assert.assertEquals("0:0:0:0:0:0:0:1", inetAddress.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        String json = "[\"192.168.1.1\", 80]";
        FromStringDeserializer deserializer = findDeserializer(InetSocketAddress.class);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertNotNull(inetSocketAddress);
        Assert.assertEquals("192.168.1.1", inetSocketAddress.getAddress().getHostAddress());
        Assert.assertEquals(80, inetSocketAddress.getPort());

        json = "[\"127.0.0.1\", 8080]";
        inetSocketAddress = (InetSocketAddress) deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertNotNull(inetSocketAddress);
        Assert.assertEquals("127.0.0.1", inetSocketAddress.getAddress().getHostAddress());
        Assert.assertEquals(8080, inetSocketAddress.getPort());

        json = "[\"0:0:0:0:0:0:0:1\", 8080]";
        inetSocketAddress = (InetSocketAddress) deserializer.deserialize(mapper.getFactory().createParser(json), null);
        Assert.assertNotNull(inetSocketAddress);
        Assert.assertEquals("0:0:0:0:0:0:0:1", inetSocketAddress.getAddress().getHostAddress());
        Assert.assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        FromStringDeserializer deserializer = findDeserializer(StringBuilder.class);
        StringBuilder stringBuilder = (StringBuilder) deserializer.deserialize(mapper.getFactory().createParser(""), null);
        Assert.assertNotNull(stringBuilder);
        Assert.assertEquals("", stringBuilder.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidStringDeserialization() throws IOException {
        String json = "\"invalid\"";
        FromStringDeserializer deserializer = findDeserializer(String.class);
        deserializer.deserialize(mapper.getFactory().createParser(json), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testArrayDeserialization() throws IOException {
        String json = "[\"invalid\"]";
        FromStringDeserializer deserializer = findDeserializer(String.class);
        deserializer.deserialize(mapper.getFactory().createParser(json), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testEmbeddedObjectDeserialization() throws IOException {
        String json = "{\"class\":\"java.util.Date\", \"value\":\"2022-12-31\"}";
        FromStringDeserializer deserializer = findDeserializer(Date.class);
        deserializer._deserializeEmbedded(mapper.parse(json), null);
    }

    private static FromStringDeserializer findDeserializer(Class<?> rawType) {
        return (FromStringDeserializer) FromStringDeserializer.findDeserializer(rawType);
    }
}