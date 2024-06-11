package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.*;

import java.io.*;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer<File> deserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        assertEquals(new File("test.txt"), deserializer.deserialize(TestContext.parser(), TestContext.context()));
    }

    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer<URL> deserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        assertEquals(new URL("http://test.com"), deserializer.deserialize(TestContext.parser(), TestContext.context()));
    }

    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer<URI> deserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        assertEquals(new URI("http://test.com"), deserializer.deserialize(TestContext.parser(), TestContext.context()));
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer<Class<?>> deserializer = (FromStringDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(Class.class);
        assertEquals(String.class, deserializer.deserialize(TestContext.parser(), TestContext.context()));
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer<JavaType> deserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType javaType = TestContext.context().getTypeFactory().constructType(String.class.getName());
        assertEquals(javaType, deserializer.deserialize(TestContext.parser(), TestContext.context()));
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer<Currency> deserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        assertEquals(Currency.getInstance("USD"), deserializer.deserialize(TestContext.parser(), TestContext.context()));
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        assertEquals(Pattern.compile("\\d+"), deserializer.deserialize(TestContext.parser(), TestContext.context()));
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer<Locale> deserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        assertEquals(Locale.US, deserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(Locale.forLanguageTag("fr_FR"), deserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(Locale.forLanguageTag("en_US-u-ca-zulu"), deserializer.deserialize(TestContext.parser(), TestContext.context()));
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer<Charset> deserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        assertEquals(Charset.forName("UTF-8"), deserializer.deserialize(TestContext.parser(), TestContext.context()));
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        assertEquals(TimeZone.getTimeZone("UTC"), deserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(TimeZone.getTimeZone("Europe/London"), deserializer.deserialize(TestContext.parser(), TestContext.context()));
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        assertEquals(InetAddress.getByName("127.0.0.1"), deserializer.deserialize(TestContext.parser(), TestContext.context()));
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        assertEquals(new InetSocketAddress("127.0.0.1", 8080), deserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(new InetSocketAddress("[::1]", 8080), deserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(new InetSocketAddress("127.0.0.1:8080"), deserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(new InetSocketAddress("[::1]:8080"), deserializer.deserialize(TestContext.parser(), TestContext.context()));
    }

    @Test
    public void testDeserializeStringBuilder() throws IOException {
        FromStringDeserializer<StringBuilder> deserializer = (FromStringDeserializer<StringBuilder>) FromStringDeserializer.findDeserializer(StringBuilder.class);
        assertEquals(new StringBuilder("test"), deserializer.deserialize(TestContext.parser(), TestContext.context()));
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        FromStringDeserializer<String> stringDeserializer = new FromStringDeserializer<String>(String.class);
        FromStringDeserializer<File> fileDeserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        FromStringDeserializer<URL> urlDeserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        FromStringDeserializer<URI> uriDeserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        FromStringDeserializer<Class<?>> classDeserializer = (FromStringDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(Class.class);
        FromStringDeserializer<JavaType> javaTypeDeserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
        FromStringDeserializer<Currency> currencyDeserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        FromStringDeserializer<Pattern> patternDeserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        FromStringDeserializer<Locale> localeDeserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        FromStringDeserializer<Charset> charsetDeserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        FromStringDeserializer<TimeZone> timeZoneDeserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        FromStringDeserializer<InetAddress> inetAddressDeserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        FromStringDeserializer<StringBuilder> stringBuilderDeserializer = (FromStringDeserializer<StringBuilder>) FromStringDeserializer.findDeserializer(StringBuilder.class);

        assertEquals("", stringDeserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(null, fileDeserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(null, urlDeserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(null, uriDeserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(null, classDeserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(null, javaTypeDeserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(Currency.getInstance(""), currencyDeserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(null, patternDeserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(Locale.ROOT, localeDeserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(Charset.forName(""), charsetDeserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(TimeZone.getDefault(), timeZoneDeserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(InetAddress.getByName(""), inetAddressDeserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(null, inetSocketAddressDeserializer.deserialize(TestContext.parser(), TestContext.context()));
        assertEquals(new StringBuilder(), stringBuilderDeserializer.deserialize(TestContext.parser(), TestContext.context()));
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        assertEquals(TestContext.context().createArrayNode(), deserializer.deserialize(TestContext.parser(), TestContext.context()));
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        String json = "{\"name\": \"test\"}";
        ObjectNode objectNode = TestContext.mapper().readValue(json, ObjectNode.class);
        assertEquals(new TestObject("test"), deserializeEmbeddedObject(TestObject.class, objectNode));
    }

    private static <T> T deserializeEmbeddedObject(Class<T> clazz, ObjectNode node) throws IOException {
        return TestContext.parser().readValue(node.traverse(), clazz);
    }

    private static class TestContext {
        public static ObjectMapper mapper() {
            return new ObjectMapper();
        }

        public static JsonParser parser() {
            return mapper().getFactory().createParser("");
        }

        public static DeserializationContext context() throws JsonProcessingException {
            return mapper().getDeserializationContext();
        }
    }
}

class TestObject {
    private String name;

    public TestObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}