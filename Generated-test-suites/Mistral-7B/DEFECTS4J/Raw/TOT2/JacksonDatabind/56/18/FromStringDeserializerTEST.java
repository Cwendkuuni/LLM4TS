package com.example.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

/**
 * Test class for FromStringDeserializer.
 */
public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Test deserialize method for File class.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeFile() throws IOException {
        String json = "{\"className\":\"File\",\"value\":\"/path/to/file.txt\"}";
        FromStringDeserializer<File> deserializer = new FromStringDeserializer<File>(File.class);
        File file = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals("/path/to/file.txt", file.getAbsolutePath());
    }

    /**
     * Test deserialize method for URL class.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeURL() throws IOException {
        String json = "{\"className\":\"URL\",\"value\":\"http://example.com\"}";
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class);
        URL url = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals("http://example.com", url.toString());
    }

    /**
     * Test deserialize method for URI class.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeURI() throws IOException {
        String json = "{\"className\":\"URI\",\"value\":\"http://example.com/path/to/resource\"}";
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer<URI>(URI.class);
        URI uri = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals("http://example.com/path/to/resource", uri.toString());
    }

    /**
     * Test deserialize method for Class class.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void testDeserializeClass() throws IOException, ClassNotFoundException {
        String json = "{\"className\":\"Class\",\"value\":\"java.lang.String\"}";
        FromStringDeserializer<Class<?>> deserializer = new FromStringDeserializer<Class<?>>(Class.class);
        Class<?> clazz = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals(String.class, clazz);
    }

    /**
     * Test deserialize method for JavaType class.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeJavaType() throws IOException {
        String json = "{\"className\":\"JavaType\",\"value\":\"com.example.MyClass\"}";
        FromStringDeserializer<JavaType> deserializer = new FromStringDeserializer<JavaType>(JavaType.class);
        JavaType javaType = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals(JavaType.constructType(MyClass.class), javaType);
    }

    /**
     * Test deserialize method for Currency class.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeCurrency() throws IOException {
        String json = "{\"className\":\"Currency\",\"value\":\"USD\"}";
        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer<Currency>(Currency.class);
        Currency currency = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals(Currency.getInstance("USD"), currency);
    }

    /**
     * Test deserialize method for Pattern class.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializePattern() throws IOException {
        String json = "{\"className\":\"Pattern\",\"value\":\"\\w+@\\w+\\.\\w+\"}";
        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer<Pattern>(Pattern.class);
        Pattern pattern = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertTrue(pattern.matcher("test@example.com").matches());
    }

    /**
     * Test deserialize method for Locale class.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeLocale() throws IOException {
        String json = "{\"className\":\"Locale\",\"value\":\"en_US\"}";
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<Locale>(Locale.class);
        Locale locale = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals("en_US", locale.toString());
    }

    /**
     * Test deserialize method for Charset class.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeCharset() throws IOException {
        String json = "{\"className\":\"Charset\",\"value\":\"UTF-8\"}";
        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer<Charset>(Charset.class);
        Charset charset = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals(StandardCharsets.UTF_8, charset);
    }

    /**
     * Test deserialize method for TimeZone class.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeTimeZone() throws IOException {
        String json = "{\"className\":\"TimeZone\",\"value\":\"America/New_York\"}";
        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
        TimeZone timeZone = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertEquals("America/New_York", timeZone.getID());
    }

    /**
     * Test deserialize method for InetAddress class.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeInetAddress() throws IOException {
        String jsonIPv4 = "{\"className\":\"InetAddress\",\"value\":\"192.168.1.1\"}";
        String jsonIPv6 = "{\"className\":\"InetAddress\",\"value\":\"2001:0db8:85a3:0000:0000:8a2e:0370:7334\"}";

        FromStringDeserializer<InetAddress> deserializerIPv4 = new FromStringDeserializer<InetAddress>(InetAddress.class);
        FromStringDeserializer<InetAddress> deserializerIPv6 = new FromStringDeserializer<InetAddress>(InetAddress.class);

        InetAddress ipv4 = deserializerIPv4.deserialize(mapper.getFactory().createParser(jsonIPv4), mapper.getContext());
        InetAddress ipv6 = deserializerIPv6.deserialize(mapper.getFactory().createParser(jsonIPv6), mapper.getContext());

        assertEquals("192.168.1.1", ipv4.getHostAddress());
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", ipv6.getHostAddress());
    }

    /**
     * Test deserialize method for InetSocketAddress class.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        String jsonIPv4 = "{\"className\":\"InetSocketAddress\",\"value\":\"192.168.1.1:80\"}";
        String jsonIPv6 = "{\"className\":\"InetSocketAddress\",\"value\":\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80\"}";

        FromStringDeserializer<InetSocketAddress> deserializerIPv4 = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
        FromStringDeserializer<InetSocketAddress> deserializerIPv6 = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);

        InetSocketAddress ipv4 = deserializerIPv4.deserialize(mapper.getFactory().createParser(jsonIPv4), mapper.getContext());
        InetSocketAddress ipv6 = deserializerIPv6.deserialize(mapper.getFactory().createParser(jsonIPv6), mapper.getContext());

        assertEquals("192.168.1.1", ipv4.getAddress().getHostAddress());
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", ipv6.getAddress().getHostAddress());
        assertEquals(80, ipv4.getPort());
        assertEquals(80, ipv6.getPort());
    }

    /**
     * Test deserialize method for empty string.
     *
     * @throws IOException
     */
    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyString() throws IOException {
        String json = "{}";
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
    }

    /**
     * Test deserialize method for null.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeNull() throws IOException {
        String json = "null";
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        Object result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
        assertNull(result);
    }

    /**
     * Test deserialize method for unknown class.
     *
     * @throws IOException
     */
    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownClass() throws IOException {
        String json = "{\"className\":\"UnknownClass\",\"value\":\"unknown\"}";
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
    }

    /**
     * Test _deserializeFromEmptyString method.
     *
     * @throws IOException
     */
    @Test
    public void testDeserializeFromEmptyString() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        Object result = deserializer._deserializeFromEmptyString();
        assertNull(result);
    }

}