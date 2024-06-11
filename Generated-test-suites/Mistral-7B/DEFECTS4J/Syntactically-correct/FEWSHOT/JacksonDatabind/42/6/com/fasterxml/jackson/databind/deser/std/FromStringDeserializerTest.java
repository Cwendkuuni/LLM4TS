package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        JsonParser jp = createParser("\"/path/to/file.txt\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertEquals(new File("/path/to/file.txt"), deserializer.deserialize(jp, new TestContext()));
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        JsonParser jp = createParser("\"http://example.com\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        assertEquals(new URL("http://example.com"), deserializer.deserialize(jp, new TestContext()));
    }

    @Test
    public void testDeserializeUri() throws IOException {
        JsonParser jp = createParser("\"uri:/path/to/resource\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        assertEquals(new URI("uri:/path/to/resource"), deserializer.deserialize(jp, new TestContext()));
    }

    @Test
    public void testDeserializeClass() throws IOException {
        JsonParser jp = createParser("\"com.example.MyClass\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);

        Class<?> clazz = deserializer.deserialize(jp, new TestContext()).asSubTypeOf(Class.class);
        assertEquals(Class.forName("com.example.MyClass"), clazz);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        JsonParser jp = createParser("\"com.example.MyClass\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(JavaType.class);

        JavaType javaType = deserializer.deserialize(jp, new TestContext());
        assertEquals(JavaType.constructFromCanonical("com.example.MyClass"), javaType);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        JsonParser jp = createParser("\"USD\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);

        Currency currency = deserializer.deserialize(jp, new TestContext());
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        JsonParser jp = createParser("\"/[a-z]+\\.txt$/\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);

        Pattern pattern = deserializer.deserialize(jp, new TestContext());
        assertInstanceOf(Pattern.class, pattern);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        JsonParser jp = createParser("\"en_US\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);

        Locale locale = deserializer.deserialize(jp, new TestContext());
        assertEquals(Locale.US, locale);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        JsonParser jp = createParser("\"UTF-8\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);

        Charset charset = deserializer.deserialize(jp, new TestContext());
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        JsonParser jp = createParser("\"America/New_York\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);

        TimeZone timeZone = deserializer.deserialize(jp, new TestContext());
        assertEquals(TimeZone.getTimeZone("America/New_York"), timeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        JsonParser jp = createParser("\"192.168.1.1\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);

        InetAddress inetAddress = deserializer.deserialize(jp, new TestContext());
        assertEquals(InetAddress.getByName("192.168.1.1"), inetAddress);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        JsonParser jp = createParser("\"[255.255.255.255]:80\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);

        InetSocketAddress inetSocketAddress = deserializer.deserialize(jp, new TestContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 80), inetSocketAddress);
    }

    @Test
    public void testDeserializeEmptyStringForUri() throws IOException {
        JsonParser jp = createParser("\"\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);

        URI uri = deserializer.deserialize(jp, new TestContext());
        assertEquals(URI.create(""), uri);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnsupportedType() throws IOException {
        JsonParser jp = createParser("\"java.util.ArrayList\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(ArrayList.class);
        deserializer.deserialize(jp, new TestContext());
    }

    @Test
    public void testDeserializeNull() throws IOException {
        JsonParser jp = createParser("\"\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertNull(deserializer.deserialize(jp, new TestContext()));
    }

    @Test
    public void testDeserializeEmptyStringForOtherTypes() throws IOException {
        JsonParser jp = createParser("\"\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        assertNull(deserializer.deserialize(jp, new TestContext()));
    }

    private JsonParser createParser(String content) throws IOException {
        StringReader reader = new StringReader(content);
        ObjectCodec objectMapper = new ObjectMapper();
        return objectMapper.getFactory().createParser(reader);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public DeserializationFeature[] getEnabledFeatures() {
            return new DeserializationFeature[0];
        }

        @Override
        public DeserializationFeature getFeature(String name) {
            return null;
        }

        @Override
        public void reportBadDefault(Class<?> clazz) {
            // Do nothing
        }

        @Override
        public void reportBadTypeId(Class<?> clazz, String name) {
            // Do nothing
        }

        @Override
        public void reportError(MessageBase msg) {
            throw new RuntimeException(msg.getMessage());
        }

        @Override
        public void reportWarning(MessageBase msg) {
            // Do nothing
        }
    }
}