package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for FromStringDeserializer.
 */
public class FromStringDeserializerTest {

    @Test
    public void testDeserializeUrl() throws JsonProcessingException {
        JsonParser parser = createParser("http://example.com");
        FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = urlDeserializer.deserialize(parser, new DeserializationContext(new ObjectMapper()));
        assertEquals("http://example.com", url.toExternalForm());
    }

    @Test
    public void testDeserializeUri() throws JsonProcessingException {
        JsonParser parser = createParser("http://example.com/path/to/resource?queryString#fragment");
        FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = uriDeserializer.deserialize(parser, new DeserializationContext(new ObjectMapper()));
        assertEquals("http://example.com/path/to/resource?queryString#fragment", uri.toASCIIString());
    }

    @Test
    public void testDeserializeClass() throws JsonProcessingException {
        JsonParser parser = createParser("java.lang.String");
        FromStringDeserializer<Class<?>> classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> clazz = classDeserializer.deserialize(parser, new DeserializationContext(new ObjectMapper()));
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserializeJavaType() throws JsonProcessingException {
        JsonParser parser = createParser("java.lang.String[]");
        FromStringDeserializer<JavaType> javaTypeDeserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType javaType = javaTypeDeserializer.deserialize(parser, new DeserializationContext(new ObjectMapper()));
        assertEquals(java.lang.String[].class, javaType.getType());
    }

    @Test
    public void testDeserializeCurrency() throws JsonProcessingException {
        JsonParser parser = createParser("USD");
        FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency currency = currencyDeserializer.deserialize(parser, new DeserializationContext(new ObjectMapper()));
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test
    public void testDeserializePattern() throws JsonProcessingException {
        JsonParser parser = createParser("/(abc)+/");
        FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern pattern = patternDeserializer.deserialize(parser, new DeserializationContext(new ObjectMapper()));
        assertEquals("/(abc)+/", pattern.pattern());
    }

    @Test
    public void testDeserializeLocale() throws JsonProcessingException {
        JsonParser parser = createParser("en_US");
        FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = localeDeserializer.deserialize(parser, new DeserializationContext(new ObjectMapper()));
        assertEquals(Locale.US, locale);
    }

    @Test
    public void testDeserializeCharset() throws JsonProcessingException {
        JsonParser parser = createParser("UTF-8");
        FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset charset = charsetDeserializer.deserialize(parser, new DeserializationContext(new ObjectMapper()));
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testDeserializeTimeZone() throws JsonProcessingException {
        JsonParser parser = createParser("America/Los_Angeles");
        FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = timeZoneDeserializer.deserialize(parser, new DeserializationContext(new ObjectMapper()));
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), timeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws JsonProcessingException {
        JsonParser parser = createParser("192.168.1.1");
        FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress inetAddress = inetAddressDeserializer.deserialize(parser, new DeserializationContext(new ObjectMapper()));
        assertEquals(InetAddress.getByName("192.168.1.1"), inetAddress);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws JsonProcessingException {
        JsonParser parser = createParser("192.168.1.1:80");
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = inetSocketAddressDeserializer.deserialize(parser, new DeserializationContext(new ObjectMapper()));
        assertEquals(new InetSocketAddress("192.168.1.1", 80), inetSocketAddress);

        parser = createParser("[2001:db8:85a3:0:0:8a2e:370:7334]:8080");
        inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        inetSocketAddress = inetSocketAddressDeserializer.deserialize(parser, new DeserializationContext(new ObjectMapper()));
        assertEquals(new InetSocketAddress(InetAddress.getByAddress(new byte[]{0x20, 0x01, 0xdb, 0x8, 0x85, 0xa3, 0x0, 0x0, 0x8a, 0x2e, 0x37, 0x0, 0x73, 0x34, 0x0, 0x73, 0x34}), 8080), inetSocketAddress);
    }

    @Test
    public void testDeserializeEmptyString() throws JsonProcessingException {
        JsonParser parser = createParser("");
        FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = urlDeserializer.deserialize(parser, new DeserializationContext(new ObjectMapper()));
        assertNull(url);

        parser = createParser("http://");
        urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        url = urlDeserializer.deserialize(parser, new DeserializationContext(new ObjectMapper()));
        assertNull(url);

        parser = createParser("URI:");
        FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = uriDeserializer.deserialize(parser, new DeserializationContext(new ObjectMapper()));
        assertNull(uri);
    }

    private JsonParser createParser(String text) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new ByteArrayInputStream(text.getBytes()));
    }
}