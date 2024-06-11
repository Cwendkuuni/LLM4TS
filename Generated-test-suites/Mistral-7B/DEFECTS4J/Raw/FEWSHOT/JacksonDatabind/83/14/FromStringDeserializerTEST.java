package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File expected = new File("test.txt");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        File result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected.getCanonicalPath(), result.getCanonicalPath());
    }

    @Test
    public void testDeserializeUrl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL expected = new URL("http://example.com");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        URL result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testDeserializeUri() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI expected = new URI("http://example.com");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        URI result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testDeserializeClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(String.class);
        Class<?> expected = String.class;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getName());
        Class<?> result = (Class<?>) deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected.getName(), result.getName());
    }

    @Test
    public void testDeserializeJavaType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType expected = mapper.getTypeFactory().constructType(String.class);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        JavaType result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected.getRawClass(), result.getRawClass());
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency expected = Currency.getInstance("USD");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getCurrencyCode());
        Currency result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected.getCurrencyCode(), result.getCurrencyCode());
    }

    @Test
    public void testDeserializePattern() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern expected = Pattern.compile("\\d+");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.pattern());
        Pattern result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected.pattern(), result.pattern());
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale expected = new Locale("en", "GB");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        Locale result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset expected = Charset.forName("UTF-8");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.name());
        Charset result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected.name(), result.name());
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone expected = TimeZone.getTimeZone("UTC");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getID());
        TimeZone result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected.getID(), result.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress expected = InetAddress.getByName("192.168.1.1");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.getHostAddress());
        InetAddress result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected.getHostAddress(), result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress expected = new InetSocketAddress("192.168.1.1", 80);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        InetSocketAddress result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected.getAddress().getHostAddress(), result.getAddress().getHostAddress());
        assertEquals(expected.getPort(), result.getPort());
    }

    @Test
    public void testDeserializeStringBuilder() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder expected = new StringBuilder("Hello, World!");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);
        StringBuilder result = deserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testEmptyStringDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String json = "\"\"";
        String result = stringDeserializer.deserialize(mapper.getFactory().createParser(json), new TestContext()).toString();
        assertEquals("", result);

        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        json = "";
        Locale resultLocale = localeDeserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals(Locale.ROOT, resultLocale);

        FromStringDeserializer<StringBuilder> stringBuilderDeserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        json = "";
        StringBuilder resultStringBuilder = stringBuilderDeserializer.deserialize(mapper.getFactory().createParser(json), new TestContext());
        assertEquals("", resultStringBuilder.toString());
    }

    @Test
    public void testDeserializeEmbeddedObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\":\"java.net.URI\",\"value\":\"http://example.com\"}";
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        Object readTree = mapper.readTree(json);
        URI result = deserializer.deserializeEmbedded(readTree, new TestContext());
        assertEquals(new URI("http://example.com"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnexpectedObjectType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\":\"java.lang.Integer\",\"value\":\"test\"}";
        FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        Object readTree = mapper.readTree(json);
        deserializer.deserializeEmbedded(readTree, new TestContext());
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public Object findValueInstantiator(Class<?> valueType) {
            return null;
        }
    }
}