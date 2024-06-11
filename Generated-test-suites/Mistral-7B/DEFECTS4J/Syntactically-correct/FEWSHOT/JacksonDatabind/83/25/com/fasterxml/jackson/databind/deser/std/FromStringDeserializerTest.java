package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

import java.io.*;
import java.net.URI;
import java.util.Locale;

public class FromStringDeserializerTest {

    @Test
    public void testStdDeserializeFile() throws IOException {
        JsonParser p = createParser("\"/path/to/file.txt\"");
        FromStringDeserializer<File> deserializer = Std.findDeserializer(File.class);
        File result = deserializer.deserialize(p, new TestContext());
        assertEquals("/path/to/file.txt", result.getPath());
    }

    @Test
    public void testStdDeserializeURL() throws IOException {
        JsonParser p = createParser("\"http://example.com\"");
        FromStringDeserializer<URL> deserializer = Std.findDeserializer(URL.class);
        URL result = deserializer.deserialize(p, new TestContext());
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testStdDeserializeURI() throws IOException {
        JsonParser p = createParser("\"http://example.com/path/to\"");
        FromStringDeserializer<URI> deserializer = Std.findDeserializer(URI.class);
        URI result = deserializer.deserialize(p, new TestContext());
        assertEquals("http://example.com/path/to", result.toString());
    }

    @Test
    public void testStdDeserializeClass() throws IOException, ClassNotFoundException {
        JsonParser p = createParser("\"java.lang.String\"");
        FromStringDeserializer<Class<?>> deserializer = Std.findDeserializer(Class.class);
        Class<?> result = deserializer.deserialize(p, new TestContext());
        assertEquals(String.class, result);
    }

    @Test
    public void testStdDeserializeJavaType() throws IOException {
        JsonParser p = createParser("\"com.example.MyClass\"");
        FromStringDeserializer<JavaType> deserializer = Std.findDeserializer(JavaType.class);
        JavaType result = deserializer.deserialize(p, new TestContext());
        assertEquals(JavaType.constructType(MyClass.class), result);
    }

    @Test
    public void testStdDeserializeCurrency() throws IOException {
        JsonParser p = createParser("\"USD\"");
        FromStringDeserializer<Currency> deserializer = Std.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(p, new TestContext());
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testStdDeserializePattern() throws IOException {
        JsonParser p = createParser("\"\\w+\\.\\w+\"");
        FromStringDeserializer<Pattern> deserializer = Std.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(p, new TestContext());
        assertEquals(Pattern.compile("\\w+\\.\\w+"), result);
    }

    @Test
    public void testStdDeserializeLocale() throws IOException {
        JsonParser p = createParser("\"en_US\"");
        FromStringDeserializer<Locale> deserializer = Std.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(p, new TestContext());
        assertEquals(Locale.US, result);

        p = createParser("\"en_US_POSIX\"");
        result = deserializer.deserialize(p, new TestContext());
        assertEquals(Locale.US, result);

        p = createParser("\"en-US\"");
        result = deserializer.deserialize(p, new TestContext());
        assertEquals(Locale.US, result);

        p = createParser("\"en_US-poetic\"");
        result = deserializer.deserialize(p, new TestContext());
        assertEquals(new Locale("en", "US", "poetic"), result);
    }

    @Test
    public void testStdDeserializeCharset() throws IOException {
        JsonParser p = createParser("\"UTF-8\"");
        FromStringDeserializer<Charset> deserializer = Std.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(p, new TestContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testStdDeserializeTimeZone() throws IOException {
        JsonParser p = createParser("\"America/New_York\"");
        FromStringDeserializer<TimeZone> deserializer = Std.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(p, new TestContext());
        assertEquals(TimeZone.getTimeZone("America/New_York"), result);
    }

    @Test
    public void testStdDeserializeInetAddress() throws IOException {
        JsonParser p = createParser("\"192.168.1.1\"");
        FromStringDeserializer<InetAddress> deserializer = Std.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(p, new TestContext());
        assertEquals("192.168.1.1", result.getHostAddress());

        p = createParser("\"2001:0db8:85a3:0000:0000:8a2e:0370:7334\"");
        result = deserializer.deserialize(p, new TestContext());
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334", result.getHostAddress());
    }

    @Test
    public void testStdDeserializeInetSocketAddress() throws IOException {
        JsonParser p = createParser("\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80\"");
        FromStringDeserializer<InetSocketAddress> deserializer = Std.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(p, new TestContext());
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());

        p = createParser("\"192.168.1.1:80\"");
        result = deserializer.deserialize(p, new TestContext());
        assertEquals("192.168.1.1", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());
    }

    @Test
    public void testStdDeserializeStringBuilder() throws IOException {
        JsonParser p = createParser("\"hello world\"");
        FromStringDeserializer<StringBuilder> deserializer = Std.findDeserializer(StringBuilder.class);
        StringBuilder result = deserializer.deserialize(p, new TestContext());
        assertEquals("hello world", result.toString());
    }

    @Test
    public void testStdDeserializeEmptyString() throws IOException {
        JsonParser p = createParser("\"\"");
        FromStringDeserializer<Object> deserializer = Std.findDeserializer(Object.class);
        Object result = deserializer.deserialize(p, new TestContext());
        assertNull(result);

        p = createParser("\"\"");
        deserializer = Std.findDeserializer(String.class);
        result = deserializer.deserialize(p, new TestContext());
        assertEquals("", result);

        p = createParser("\"\"");
        deserializer = Std.findDeserializer(StringBuilder.class);
        result = deserializer.deserialize(p, new TestContext());
        assertEquals("", result.toString());

        p = createParser("\"\"");
        deserializer = Std.findDeserializer(URI.class);
        result = deserializer.deserialize(p, new TestContext());
        assertEquals(URI.create(""), result);

        p = createParser("\"\"");
        deserializer = Std.findDeserializer(Locale.class);
        result = deserializer.deserialize(p, new TestContext());
        assertEquals(Locale.ROOT, result);

        p = createParser("\"\"");
        deserializer = Std.findDeserializer(Charset.class);
        result = deserializer.deserialize(p, new TestContext());
        assertEquals(Charset.forName(""), result);

        p = createParser("\"\"");
        deserializer = Std.findDeserializer(TimeZone.class);
        result = deserializer.deserialize(p, new TestContext());
        assertEquals(TimeZone.getDefault(), result);

        p = createParser("\"\"");
        deserializer = Std.findDeserializer(InetAddress.class);
        result = deserializer.deserialize(p, new TestContext());
        assertNull(result);

        p = createParser("\"\"");
        deserializer = Std.findDeserializer(InetSocketAddress.class);
        result = deserializer.deserialize(p, new TestContext());
        assertNull(result);
    }

    @Test
    public void testStdDeserializeNull() throws IOException {
        JsonParser p = createParser("\"null\"");
        FromStringDeserializer<Object> deserializer = Std.findDeserializer(Object.class);
        Object result = deserializer.deserialize(p, new TestContext());
        assertNull(result);

        p = createParser("\"null\"");
        deserializer = Std.findDeserializer(String.class);
        result = deserializer.deserialize(p, new TestContext());
        assertEquals("", result);

        p = createParser("\"null\"");
        deserializer = Std.findDeserializer(StringBuilder.class);
        result = deserializer.deserialize(p, new TestContext());
        assertEquals("", result.toString());

        p = createParser("\"null\"");
        deserializer = Std.findDeserializer(URI.class);
        result = deserializer.deserialize(p, new TestContext());
        assertEquals(URI.create(""), result);

        p = createParser("\"null\"");
        deserializer = Std.findDeserializer(Locale.class);
        result = deserializer.deserialize(p, new TestContext());
        assertEquals(Locale.ROOT, result);

        p = createParser("\"null\"");
        deserializer = Std.findDeserializer(Charset.class);
        result = deserializer.deserialize(p, new TestContext());
        assertEquals(Charset.forName(""), result);

        p = createParser("\"null\"");
        deserializer = Std.findDeserializer(TimeZone.class);
        result = deserializer.deserialize(p, new TestContext());
        assertEquals(TimeZone.getDefault(), result);

        p = createParser("\"null\"");
        deserializer = Std.findDeserializer(InetAddress.class);
        result = deserializer.deserialize(p, new TestContext());
        assertNull(result);

        p = createParser("\"null\"");
        deserializer = Std.findDeserializer(InetSocketAddress.class);
        result = deserializer.deserialize(p, new TestContext());
        assertNull(result);
    }

    @Test
    public void testStdDeserializeArray() throws IOException {
        JsonParser p = createParser("[\"1\", \"2\", \"3\"]");
        FromStringDeserializer<Integer[]> deserializer = Std.findDeserializer(Integer[].class);
        Integer[] result = deserializer.deserialize(p, new TestContext());
        assertArrayEquals(new Integer[]{1, 2, 3}, result);
    }

    @Test
    public void testStdDeserializeEmptyArray() throws IOException {
        JsonParser p = createParser("[]");
        FromStringDeserializer<Integer[]> deserializer = Std.findDeserializer(Integer[].class);
        Integer[] result = deserializer.deserialize(p, new TestContext());
        assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testStdDeserializeEmbeddedObject() throws IOException {
        String json = "{\"embedded\":{\"myClass\":\"MyClassValue\"}}";
        JsonParser p = createParser(json);

        FromStringDeserializer<MyClass> deserializer = Std.findDeserializer(MyClass.class);
        MyClass result = deserializer.deserialize(p, new TestContext());
        assertEquals("MyClassValue", result.getValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testStdDeserializeInvalidType() throws IOException {
        JsonParser p = createParser("\"MyClass\"");
        FromStringDeserializer<String> deserializer = Std.findDeserializer(String.class);
        deserializer.deserialize(p, new TestContext());
        fail("Expected JsonMappingException not thrown");
    }

    private JsonParser createParser(String content) throws IOException {
        StringReader reader = new StringReader(content);
        ObjectCodec codec = new ObjectMapper().findValueCodec(JavaType.constructType(String.class));
        return codec.treeParser(reader);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public void handleUnexpectedToken(Class<?> type, JsonParser p) throws IOException {
            throw new JsonMappingException(p, "Unexpected token: " + p.getCurrentName());
        }
    }

    static class MyClass {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}