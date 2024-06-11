package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_File() throws IOException {
        FromStringDeserializer<File> deserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        Assert.assertEquals(new File("test.txt"), deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_URL() throws IOException {
        FromStringDeserializer<URL> deserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        Assert.assertEquals(new URL("http://example.com"), deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_URI() throws IOException {
        FromStringDeserializer<URI> deserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        Assert.assertEquals(new URI("http://example.com"), deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_Class() throws IOException {
        FromStringDeserializer<Class<?>> deserializer = (FromStringDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(String.class);
        Assert.assertEquals(String.class, deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_JavaType() throws IOException {
        FromStringDeserializer<JavaType> deserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType javaType = getTestContext().getTypeFactory().constructType(String.class);
        Assert.assertEquals(javaType, deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_Currency() throws IOException {
        FromStringDeserializer<Currency> deserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        Assert.assertEquals(Currency.getInstance("USD"), deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_Pattern() throws IOException {
        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        Assert.assertEquals(Pattern.compile("\\d{3}-\\d{2}-\\d{3}"), deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_Locale() throws IOException {
        FromStringDeserializer<Locale> deserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        Assert.assertEquals(Locale.US, deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_Charset() throws IOException {
        FromStringDeserializer<Charset> deserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        Assert.assertEquals(Charset.forName("UTF-8"), deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_TimeZone() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        Assert.assertEquals(TimeZone.getTimeZone("UTC"), deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_InetAddress() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress inetAddress = InetAddress.getByName("192.168.1.1");
        Assert.assertEquals(inetAddress, deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_InetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        Assert.assertEquals(new InetSocketAddress("192.168.1.1", 80), deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_StringBuilder() throws IOException {
        FromStringDeserializer<StringBuilder> deserializer = (FromStringDeserializer<StringBuilder>) FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder stringBuilder = new StringBuilder();
        Assert.assertEquals(stringBuilder, deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_EmptyString() throws IOException {
        FromStringDeserializer<String> deserializer = (FromStringDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
        Assert.assertEquals("", deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_EmptyArray() throws IOException {
        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Object[].class);
        assertNull(deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_EmbeddedObject_Class() throws IOException {
        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
        Object obj = new Object() {};
        JsonParser parser = getTestParser();
        parser.setValue(obj);
        Object deserialized = deserializer.deserialize(parser, getTestContext());
        assertEquals(obj, deserialized);
    }

    @Test
    public void testDeserialize_InvalidFormat_URL() throws IOException {
        FromStringDeserializer<URL> deserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        Assert.assertThrows(InvalidFormatException.class, () -> deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_InvalidFormat_URI() throws IOException {
        FromStringDeserializer<URI> deserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        Assert.assertThrows(InvalidFormatException.class, () -> deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_InvalidFormat_Currency() throws IOException {
        FromStringDeserializer<Currency> deserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        Assert.assertThrows(IllegalArgumentException.class, () -> deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_InvalidFormat_Pattern() throws IOException {
        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        Assert.assertThrows(PatternSyntaxException.class, () -> deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_InvalidFormat_Locale() throws IOException {
        FromStringDeserializer<Locale> deserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        Assert.assertThrows(MissingResourceException.class, () -> deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_InvalidFormat_Charset() throws IOException {
        FromStringDeserializer<Charset> deserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        Assert.assertThrows(UnknownCharsetException.class, () -> deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_InvalidFormat_TimeZone() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        Assert.assertThrows(UnknownTimeZoneException.class, () -> deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_InvalidFormat_InetAddress() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        Assert.assertThrows(UnknownHostException.class, () -> deserializer.deserialize(getTestParser(), getTestContext()));
    }

    @Test
    public void testDeserialize_InvalidFormat_InetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        Assert.assertThrows(UnknownHostException.class, () -> deserializer.deserialize(getTestParser(), getTestContext()));
    }

    private JsonParser getTestParser() {
        return new TestJsonFactory().createParser("{}");
    }

    private DeserializationContext getTestContext() {
        return new TestJsonFactory().createParser("{}").getCodec().treeAsTokens(new ObjectMapper().readTree("{}")).context();
    }
}