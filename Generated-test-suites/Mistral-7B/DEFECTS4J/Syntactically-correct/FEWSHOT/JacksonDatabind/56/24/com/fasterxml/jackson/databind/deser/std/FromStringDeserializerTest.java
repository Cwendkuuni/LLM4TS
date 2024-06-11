package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        JsonNode node = mapper.readTree("\"/path/to/file.txt\"");
        File file = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertTrue(file.exists());
    }

    @Test
    public void testDeserializeUrl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        JsonNode node = mapper.readTree("\"http://example.com/path\"");
        URL url = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertEquals("http://example.com/path", url.toString());
    }

    @Test
    public void testDeserializeUri() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        JsonNode node = mapper.readTree("\"uri://example.com/path\"");
        URI uri = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertEquals("uri://example.com/path", uri.toString());
    }

    @Test
    public void testDeserializeClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, FromStringDeserializer.Std.STD_CLASS);
        JsonNode node = mapper.readTree("\"java.lang.String\"");
        Class<?> clazz = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserializeJavaType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JsonNode node = mapper.readTree("\"java.lang.String\"");
        JavaType javaType = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        JsonNode node = mapper.readTree("\"EUR\"");
        Currency currency = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertEquals(Currency.getInstance("EUR"), currency);
    }

    @Test
    public void testDeserializePattern() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        JsonNode node = mapper.readTree("\"/[a-z]+\\.txt$\"");
        Pattern pattern = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertEquals("/[a-z]+\\.txt$", pattern.pattern());
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        JsonNode node = mapper.readTree("\"en_US\"");
        Locale locale = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertEquals(Locale.US, locale);

        node = mapper.readTree("\"en_US_POSIX\"");
        locale = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertEquals(Locale.US, locale);

        node = mapper.readTree("\"en_GB_POSIX\"");
        locale = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertEquals(Locale.UK, locale);
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        JsonNode node = mapper.readTree("\"UTF-8\"");
        Charset charset = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        JsonNode node = mapper.readTree("\"America/Los_Angeles\"");
        TimeZone timeZone = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), timeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        JsonNode node = mapper.readTree("\"192.168.1.1\"");
        InetAddress inetAddress = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertEquals(InetAddress.getByName("192.168.1.1"), inetAddress);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        JsonNode node = mapper.readTree("[\"192.168.1.1\", 80]");
        InetSocketAddress inetSocketAddress = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 80), inetSocketAddress);

        node = mapper.readTree("[\"::1\", 80]");
        inetSocketAddress = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("::1"), 80), inetSocketAddress);

        node = mapper.readTree("[\"192.168.1.1\", 8080]");
        inetSocketAddress = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 8080), inetSocketAddress);

        node = mapper.readTree("[\"::1\", 8080]");
        inetSocketAddress = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("::1"), 8080), inetSocketAddress);
    }

    @Test
    public void testDeserializeArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, FromStringDeserializer.Std.STD_STRING);
        JsonNode node = mapper.readTree("[\"foo\", \"bar\"]");
        Object result = deserializer.deserialize(mapper.getFactory().createParser(node), new TestContext());
        assertArrayEquals(new String[]{"foo", "bar"}, (String[]) result);
    }

    @Test
    public void testDeserializeEmbeddedObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"@class\":\"java.lang.String\", \"value\":\"Hello World\"}");
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, FromStringDeserializer.Std.STD_STRING);
        Object result = deserializer.deserializeEmbedded(node, new TestContext());
        assertEquals("Hello World", result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedObjectInvalidType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"@class\":\"java.util.Date\", \"value\":\"Hello World\"}");
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, FromStringDeserializer.Std.STD_STRING);
        deserializer.deserializeEmbedded(node, new TestContext());
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public DeserializationFeature[] getEnabled(DeserializationFeature[] features) {
            return new DeserializationFeature[] { DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS };
        }

        @Override
        public JsonFactory getFactory() {
            return mapper.getFactory();
        }
    }
}