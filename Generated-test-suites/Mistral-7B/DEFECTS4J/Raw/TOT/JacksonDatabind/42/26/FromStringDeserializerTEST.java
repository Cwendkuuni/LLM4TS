package com.fasterxml.jackson.databind.deser.std.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.util.TestUtils;
import com.fasterxml.jackson.databind.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @BeforeMethod
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testFileDeserialization() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(File.class);
        JsonParser parser = mapper.getFactory().createParser("\"/foo/bar.txt\"");
        File result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(result);
        Assert.assertTrue(result.exists());
    }

    @Test
    public void testURLDeserialization() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URL.class);
        JsonParser parser = mapper.getFactory().createParser("\"http://example.com/\"");
        URL result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.openConnection());
    }

    @Test
    public void testURIDeserialization() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URI.class);
        JsonParser parser = mapper.getFactory().createParser("\"http://example.com/foo#bar\"");
        URI result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getScheme(), "http");
        Assert.assertEquals(result.getPath(), "/foo#bar");
    }

    @Test
    public void testClassDeserialization() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class);
        JsonParser parser = mapper.getFactory().createParser("\"java.lang.String\"");
        String result = deserializer.deserialize(parser, mapper.getDeserializationContext()).toString();
        Assert.assertEquals(result, "java.lang.String");
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(JavaType.class);
        JsonParser parser = mapper.getFactory().createParser("\"com.fasterxml.jackson.databind.JavaType\"");
        JavaType result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getRawClass(), JavaType.class);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Currency.class);
        JsonParser parser = mapper.getFactory().createParser("\"USD\"");
        Currency result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getCurrencyCode(), "USD");
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Pattern.class);
        JsonParser parser = mapper.getFactory().createParser("\"/[a-zA-Z]+\\/g\"");
        Pattern result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(result);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Locale.class);
        JsonParser parser = mapper.getFactory().createParser("\"en_US\"");
        Locale result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getLanguage(), "en");
        Assert.assertEquals(result.getCountry(), "US");

        parser = mapper.getFactory().createParser("\"en_UK_POSIX\"");
        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getLanguage(), "en");
        Assert.assertEquals(result.getCountry(), "UK");
        Assert.assertEquals(result.getVariant(), "POSIX");
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Charset.class);
        JsonParser parser = mapper.getFactory().createParser("\"UTF-8\"");
        Charset result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.name(), "UTF-8");
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(TimeZone.class);
        JsonParser parser = mapper.getFactory().createParser("\"America/New_York\"");
        TimeZone result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getID(), "America/New_York");
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(InetAddress.class);
        JsonParser parser = mapper.getFactory().createParser("\"127.0.0.1\"");
        InetAddress result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getHostAddress(), "127.0.0.1");

        parser = mapper.getFactory().createParser("\"::1\"");
        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getHostAddress(), "0:0:0:0:0:0:0:1");

        parser = mapper.getFactory().createParser("\"192.168.0.1:8080\"");
        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof InetSocketAddress);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) result;
        Assert.assertEquals(inetSocketAddress.getAddress().getHostAddress(), "192.168.0.1");
        Assert.assertEquals(inetSocketAddress.getPort(), 8080);
    }

    @Test
    public void testInvalidDeserialization() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Integer.class);
        JsonParser parser = mapper.getFactory().createParser("\"foo\"");
        try {
            deserializer.deserialize(parser, mapper.getDeserializationContext());
            Assert.fail();
        } catch (InvalidFormatException e) {
            Assert.assertEquals(e.getMessage(), "not a valid textual representation for Integer (for InputStream \"java.io.StringReader@5e988a35\")");
        }
    }

    @Test
    public void testNullDeserialization() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class);
        JsonParser parser = mapper.getFactory().createParser("\"null\"");
        String result = deserializer.deserialize(parser, mapper.getDeserializationContext()).toString();
        Assert.assertEquals(result, null);
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class);
        JsonParser parser = mapper.getFactory().createParser("\"\"");
        String result = deserializer.deserialize(parser, mapper.getDeserializationContext()).toString();
        Assert.assertEquals(result, "");
    }

    @Test
    public void testEmbeddedObjectDeserialization() throws IOException {
        JsonFactory factory = mapper.getFactory();
        ObjectNode node = factory.createObjectNode();
        node.put("value", "java.lang.String:Hello World");
        JsonParser parser = factory.createParser(node);

        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class);
        Object result = deserializer.deserializeEmbedded(parser.readValueAsTree(), mapper.getDeserializationContext());
        Assert.assertEquals(result, "Hello World");
    }
}