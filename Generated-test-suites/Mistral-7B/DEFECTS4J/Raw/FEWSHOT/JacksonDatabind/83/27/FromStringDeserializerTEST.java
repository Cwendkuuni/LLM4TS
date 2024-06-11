package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"/path/to/file.txt\"";
        File result = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"http://example.com\"";
        URL result = (URL) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"http://example.com/path?query=test#fragment\"";
        URI result = (URI) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertEquals("http://example.com/path?query=test#fragment", result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"java.lang.String\"";
        Class<?> result = (Class<?>) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"java.lang.Integer\"";
        JavaType result = mapper.getTypeFactory().constructType(jsonString);
        assertEquals(Integer.class, result.getRawClass());
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"EUR\"";
        Currency result = (Currency) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"/[a-z]+\\.txt\"";
        Pattern result = (Pattern) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertNotNull(result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"en_US\"";
        Locale result = (Locale) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertEquals(Locale.US, result);

        jsonString = "\"en-US\"";
        result = (Locale) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertEquals(Locale.US, result);

        jsonString = "\"en_US_POSIX\"";
        result = (Locale) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertEquals(Locale.US, result);

        jsonString = "\"en_US.POSIX\"";
        result = (Locale) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertEquals(Locale.US, result);

        jsonString = "\"en_US.POSIX,UTF-8\"";
        result = (Locale) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertEquals(new Locale("en", "US", ""), result);
        assertEquals("UTF-8", result.getLanguageTags()[1]);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"UTF-8\"";
        Charset result = (Charset) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"America/New_York\"";
        TimeZone result = (TimeZone) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertEquals(TimeZone.getTimeZone("America/New_York"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"192.168.1.1\"";
        InetAddress result = (InetAddress) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertNotNull(result);
        assertEquals("192.168.1.1", result.getHostAddress());

        jsonString = "\"255.255.255.255\"";
        result = (InetAddress) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertNotNull(result);
        assertEquals("255.255.255.255", result.getHostAddress());

        jsonString = "\"::1\"";
        result = (InetAddress) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertNotNull(result);
        assertEquals("0:0:0:0:0:0:0:1", result.getHostAddress());

        jsonString = "\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]\"";
        result = (InetAddress) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertNotNull(result);
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334", result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"[192.168.1.1]:1234\"";
        InetSocketAddress result = (InetSocketAddress) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertNotNull(result);
        assertEquals("192.168.1.1", result.getAddress().getHostAddress());
        assertEquals(1234, result.getPort());

        jsonString = "\"192.168.1.1:12345\"";
        result = (InetSocketAddress) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertNotNull(result);
        assertEquals("192.168.1.1", result.getAddress().getHostAddress());
        assertEquals(12345, result.getPort());

        jsonString = "\"[::1]:5678\"";
        result = (InetSocketAddress) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertNotNull(result);
        assertEquals("0:0:0:0:0:0:0:1", result.getAddress().getHostAddress());
        assertEquals(5678, result.getPort());

        jsonString = "\"[2001:0db8:85a3:0:0:8a2e:370:7334]:1234\"";
        result = (InetSocketAddress) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertNotNull(result);
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334", result.getAddress().getHostAddress());
        assertEquals(1234, result.getPort());
    }

    @Test
    public void testDeserializeStringBuilder() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"Hello, world!\"";
        StringBuilder result = (StringBuilder) deserializer.deserialize(mapper.getFactory().createParser(jsonString), new TestContext());
        assertNotNull(result);
        assertEquals("Hello, world!", result.toString());
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public Object handleUnexpectedToken(Class<?> desiredType, JsonParser p) throws IOException {
            throw new JsonMappingException("Unexpected token: " + p.getCurrentToken());
        }
    }
}