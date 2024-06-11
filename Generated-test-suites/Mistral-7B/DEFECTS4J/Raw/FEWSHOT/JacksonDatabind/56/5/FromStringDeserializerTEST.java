package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> T deserialize(Class<T> clazz, String input) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(new StringReader(input));
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StdScalarDeserializer<T> deserializer = (StdScalarDeserializer<T>) findDeserializer(clazz);
        return deserializer.deserialize(parser, ctxt);
    }

    @Test
    public void testDeserializeFile() throws IOException {
        File file = deserialize(File.class, "path/to/file.txt");
        assertEquals("path/to/file.txt", file.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws IOException {
        URL url = deserialize(URL.class, "http://example.com");
        assertEquals("http://example.com", url.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException {
        URI uri = deserialize(URI.class, "http://example.com/path/to/file.txt");
        assertEquals("http://example.com/path/to/file.txt", uri.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        Class<?> clazz = deserialize(Class.class, "java.lang.String.class");
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        JavaType javaType = deserialize(JavaType.class, "com.example.MyClass");
        assertEquals(JavaType.class, javaType.getClass());
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        Currency currency = deserialize(Currency.class, "USD");
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        Pattern pattern = deserialize(Pattern.class, "/foo/bar/baz/");
        assertInstanceOf(Pattern.class, pattern);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        Locale locale = deserialize(Locale.class, "en_US");
        assertEquals(Locale.US, locale);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        Charset charset = deserialize(Charset.class, "US-ASCII");
        assertEquals(Charset.forName("US-ASCII"), charset);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        TimeZone timeZone = deserialize(TimeZone.class, "America/New_York");
        assertEquals(TimeZone.getTimeZone("America/New_York"), timeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        InetAddress inetAddress = deserialize(InetAddress.class, "192.168.1.1");
        assertEquals("192.168.1.1", inetAddress.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        InetSocketAddress inetSocketAddress = deserialize(InetSocketAddress.class, "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80");
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(80, inetSocketAddress.getPort());

        inetSocketAddress = deserialize(InetSocketAddress.class, "localhost:8080");
        assertEquals("localhost", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        Object result;

        // Test for non-supported classes
        result = deserialize(Integer.class, "");
        assertNull(result);

        result = deserialize(Double.class, "");
        assertNull(result);

        result = deserialize(Boolean.class, "");
        assertNull(result);

        // Test for supported classes
        result = deserialize(File.class, "");
        assertInstanceOf(File.class, result);

        result = deserialize(URL.class, "");
        assertInstanceOf(URL.class, result);

        result = deserialize(URI.class, "");
        assertInstanceOf(URI.class, result);

        result = deserialize(Class.class, "");
        assertInstanceOf(Class.class, result);

        result = deserialize(JavaType.class, "");
        assertInstanceOf(JavaType.class, result);

        result = deserialize(Currency.class, "");
        assertInstanceOf(Currency.class, result);

        result = deserialize(Pattern.class, "");
        assertInstanceOf(Pattern.class, result);

        result = deserialize(Locale.class, "");
        assertInstanceOf(Locale.class, result);

        result = deserialize(Charset.class, "");
        assertInstanceOf(Charset.class, result);

        result = deserialize(TimeZone.class, "");
        assertInstanceOf(TimeZone.class, result);

        result = deserialize(InetAddress.class, "");
        assertInstanceOf(InetAddress.class, result);

        result = deserialize(InetSocketAddress.class, "");
        assertInstanceOf(InetSocketAddress.class, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidInput() throws IOException {
        deserialize(Object.class, "invalid_input");
    }

    private static class StringReader implements Reader {
        private final String text;

        public StringReader(String text) {
            this.text = text;
        }

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            int length = text.length();
            if (len > length) {
                len = length;
            }
            text.getChars(off, off + len);
            return len;
        }

        @Override
        public boolean ready() throws IOException {
            return true;
        }

        @Override
        public void close() throws IOException {
            // no-op
        }
    }
}