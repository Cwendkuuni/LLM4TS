package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.exc.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File file = deserializer.deserialize(mapper.getFactory().createParser("path/to/file.txt"), mapper.getDeserializationContext());
        assertEquals("path/to/file.txt", file.getAbsolutePath());
    }

    @Test
    public void testDeserializeUrl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL url = deserializer.deserialize(mapper.getFactory().createParser("http://example.com"), mapper.getDeserializationContext());
        assertEquals("http", url.getProtocol());
        assertEquals("/example.com", url.getPath());
    }

    @Test
    public void testDeserializeUri() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI uri = deserializer.deserialize(mapper.getFactory().createParser("http://example.com/path/to/file.txt"), mapper.getDeserializationContext());
        assertEquals("http", uri.getScheme());
        assertEquals("/path/to/file.txt", uri.getPath());
    }

    @Test
    public void testDeserializeClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> clazz = deserializer.deserialize(mapper.getFactory().createParser("java.lang.String"), mapper.getDeserializationContext());
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserializeJavaType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType deserialized = deserializer.deserialize(mapper.getFactory().createParser("java.lang.String[]"), mapper.getDeserializationContext());
        assertEquals(javaType, deserialized);
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency currency = deserializer.deserialize(mapper.getFactory().createParser("EUR"), mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("EUR"), currency);
    }

    @Test
    public void testDeserializePattern() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern pattern = deserializer.deserialize(mapper.getFactory().createParser("\\d+"), mapper.getDeserializationContext());
        assertNotNull(pattern);
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale locale = deserializer.deserialize(mapper.getFactory().createParser("en_US"), mapper.getDeserializationContext());
        assertEquals("en_US", locale.toString());

        locale = deserialializer.deserialize(mapper.getFactory().createParser("en_US_POSIX"), mapper.getDeserializationContext());
        assertEquals("en_US_POSIX", locale.toString());

        locale = deserialializer.deserialize(mapper.getFactory().createParser("en_US_POSIX.utf8"), mapper.getDeserializationContext());
        assertEquals("en_US_POSIX.utf8", locale.toString());
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset charset = deserializer.deserialize(mapper.getFactory().createParser("UTF-8"), mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone timeZone = deserializer.deserialize(mapper.getFactory().createParser("UTC"), mapper.getDeserializationContext());
        assertEquals("UTC", timeZone.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress inetAddress = deserializer.deserialize(mapper.getFactory().createParser("192.168.1.1"), mapper.getDeserializationContext());
        assertEquals("192.168.1.1", inetAddress.getHostAddress());

        inetAddress = deserializer.deserialize(mapper.getFactory().createParser("2001:0db8:85a3:0000:0000:8a2e:0370:7334"), mapper.getDeserializationContext());
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334", inetAddress.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);

        InetSocketAddress inetSocketAddress = deserializer.deserialize(mapper.getFactory().createParser("[2001:0db8:85a3:0:0:8a2e:370:7334]:8080"), mapper.getDeserializationContext());
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());

        inetSocketAddress = deserializer.deserialize(mapper.getFactory().createParser("192.168.1.1:8080"), mapper.getDeserializationContext());
        assertEquals("192.168.1.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test
    public void testEmptyStringDeserializationFile() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File file = deserializer.deserialize(_emptyStringToParser(), mapper.getDeserializationContext());
        assertNull(file);
    }

    @Test
    public void testEmptyStringDeserializationUrl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL url = deserializer.deserialize(_emptyStringToParser(), mapper.getDeserializationContext());
        assertNull(url);
    }

    @Test
    public void testEmptyStringDeserializationUri() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI uri = deserializer.deserialize(_emptyStringToParser(), mapper.getDeserializationContext());
        assertNotNull(uri);
        assertEquals("", uri.toString());
    }

    @Test
    public void testEmptyStringDeserializationClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> clazz = deserializer.deserialize(_emptyStringToParser(), mapper.getDeserializationContext());
        assertNull(clazz);
    }

    @Test
    public void testEmptyStringDeserializationJavaType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType deserialized = deserializer.deserialize(_emptyStringToParser(), mapper.getDeserializationContext());
        assertEquals(javaType, deserialized);
    }

    @Test
    public void testEmptyStringDeserializationCurrency() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency currency = deserializer.deserialize(_emptyStringToParser(), mapper.getDeserializationContext());
        assertNull(currency);
    }

    @Test
    public void testEmptyStringDeserializationPattern() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern pattern = deserializer.deserialize(_emptyStringToParser(), mapper.getDeserializationContext());
        assertNull(pattern);
    }

    @Test
    public void testEmptyStringDeserializationLocale() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale locale = deserializer.deserialize(_emptyStringToParser(), mapper.getDeserializationContext());
        assertNull(locale);
    }

    @Test
    public void testEmptyStringDeserializationCharset() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset charset = deserializer.deserialize(_emptyStringToParser(), mapper.getDeserializationContext());
        assertNull(charset);
    }

    @Test
    public void testEmptyStringDeserializationTimeZone() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone timeZone = deserializer.deserialize(_emptyStringToParser(), mapper.getDeserializationContext());
        assertNull(timeZone);
    }

    @Test
    public void testEmptyStringDeserializationInetAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress inetAddress = deserializer.deserialize(_emptyStringToParser(), mapper.getDeserializationContext());
        assertNull(inetAddress);
    }

    @Test
    public void testEmptyStringDeserializationInetSocketAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress inetSocketAddress = deserializer.deserialize(_emptyStringToParser(), mapper.getDeserializationContext());
        assertNull(inetSocketAddress);
    }

    private JsonParser _emptyStringToParser() throws IOException {
        return new BaseJsonParser();
    }
}