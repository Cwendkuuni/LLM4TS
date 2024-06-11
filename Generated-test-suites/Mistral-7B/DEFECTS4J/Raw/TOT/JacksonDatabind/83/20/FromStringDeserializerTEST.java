package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFileDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        Assert.assertEquals(new File("testFile.txt"), deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"File\", \"value\":\"testFile.txt\"}"), deserializer.getDeserializationContext()));
    }

    @Test
    public void testURLDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        Assert.assertEquals(new URL("http://test.com"), deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"URL\", \"value\":\"http://test.com\"}"), deserializer.getDeserializationContext()));
    }

    @Test
    public void testURIDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        Assert.assertEquals(new URI("http://test.com"), deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"URI\", \"value\":\"http://test.com\"}"), deserializer.getDeserializationContext()));
    }

    @Test
    public void testClassDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        Assert.assertEquals(String.class, deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"Class\", \"value\":\"java.lang.String\"}"), deserializer.getDeserializationContext()).getClass());
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String[].class);
        JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
        Assert.assertEquals(javaType, deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"JavaType\", \"value\":\"[Ljava.lang.String;\"}"), deserializer.getDeserializationContext()).getType());
    }

    @Test
    public void testCurrencyDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Assert.assertEquals(Currency.getInstance("CHF"), deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"Currency\", \"value\":\"CHF\"}"), deserializer.getDeserializationContext()));
    }

    @Test
    public void testPatternDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Assert.assertNotNull(deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"Pattern\", \"value\":\"\\w+\\.\\w+\"}"), deserializer.getDeserializationContext()));
    }

    @Test
    public void testLocaleDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Assert.assertEquals(Locale.US, deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"Locale\", \"value\":\"en_US\"}"), deserializer.getDeserializationContext()));
        Assert.assertEquals(Locale.ITALY, deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"Locale\", \"value\":\"it_IT\"}"), deserializer.getDeserializationContext()));
        Assert.assertEquals(Locale.ROOT, deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"Locale\", \"value\":\"\"}"), deserializer.getDeserializationContext()));
    }

    @Test
    public void testCharsetDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Assert.assertEquals(StandardCharsets.UTF_8, deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"Charset\", \"value\":\"UTF-8\"}"), deserializer.getDeserializationContext()));
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        Assert.assertEquals(TimeZone.getTimeZone("UTC"), deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"TimeZone\", \"value\":\"UTC\"}"), deserializer.getDeserializationContext()));
    }

    @Test
    public void testInetAddressDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        Assert.assertEquals(InetAddress.getByName("localhost"), deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"InetAddress\", \"value\":\"localhost\"}"), deserializer.getDeserializationContext()));
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        Assert.assertEquals(new InetSocketAddress("localhost", 8080), deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"InetSocketAddress\", \"value\":\"localhost:8080\"}"), deserializer.getDeserializationContext()));
        Assert.assertEquals(new InetSocketAddress("[::1]", 8080), deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"InetSocketAddress\", \"value\":\"[::1]:8080\"}"), deserializer.getDeserializationContext()));
        Assert.assertEquals(new InetSocketAddress("localhost", 0), deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"InetSocketAddress\", \"value\":\"localhost:0\"}"), deserializer.getDeserializationContext()));
    }

    @Test
    public void testStringBuilderDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        Assert.assertNotNull(deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"StringBuilder\", \"value\":\"\"}"), deserializer.getDeserializationContext()));
    }

    @Test
    public void testInvalidInput() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        try {
            deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"String\", \"value\":\"invalidInput\"}"), deserializer.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("not a valid textual representation, problem: invalidInput (class java.lang.String, expected type class java.lang.String)", e.getMessage());
        }
    }

    @Test
    public void testArrayDeserialization() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(int[].class);
        Assert.assertArrayEquals(new int[]{1, 2, 3}, deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"int[]\", \"value\":\"[1,2,3]\"}"), deserializer.getDeserializationContext()));
    }

    @Test
    public void testEmbeddedObjectDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"className\":\"StringBuilder\", \"value\":{\"className\":\"String\", \"value\":\"test\"}}";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder result = deserializer.deserialize(objectMapper.getFactory().createParser(json), deserializer.getDeserializationContext());
        Assert.assertEquals("test", result.toString());
    }

    @Test
    public void testNullInput() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Assert.assertEquals(Locale.ROOT, deserializer.deserialize(mapper.getFactory().createParser("{\"className\":\"Locale\", \"value\":\"null\"}"), deserializer.getDeserializationContext()));
    }
}