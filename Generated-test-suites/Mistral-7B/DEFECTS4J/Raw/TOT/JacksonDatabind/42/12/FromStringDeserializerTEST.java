package com.example.junit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFileDeserialization() throws IOException, JsonMappingException {
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL, false);
        StdDeserializer fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        String json = "\"/path/to/file.txt\"";
        File result = mapper.readerWith(File.class).readValue(json);
        Assert.assertEquals(new File("/path/to/file.txt"), result);
    }

    @Test
    public void testURLDeserialization() throws IOException, JsonMappingException {
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL, false);
        StdDeserializer urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        String json = "\"http://example.com\"";
        URL result = mapper.readerWith(URL.class).readValue(json);
        Assert.assertEquals(new URL("http://example.com"), result);
    }

    @Test
    public void testURIDeserialization() throws IOException, JsonMappingException, URISyntaxException {
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL, false);
        mapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true);
        StdDeserializer uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        String json = "[\"http://example.com\", \"/path/to/resource\"]";
        URI[] results = mapper.readerForArrayOf(URI.class).readValue(json);
        Assert.assertEquals(URI.create("http://example.com"), results[0]);
        Assert.assertEquals(URI.create("/path/to/resource"), results[1]);
    }

    @Test
    public void testClassDeserialization() throws IOException, JsonMappingException {
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL, false);
        StdDeserializer classDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String json = "\"java.util.Locale\"";
        Class<?> result = mapper.readerWith(Class.class).readValue(json);
        Assert.assertEquals(Locale.class, result);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException, JsonMappingException {
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL, false);
        StdDeserializer javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        String json = "\"com.example.MyClass\"";
        JavaType result = mapper.getTypeFactory().constructType(json);
        Assert.assertEquals(JavaType.class, result.getClass());
    }

    @Test
    public void testCurrencyDeserialization() throws IOException, JsonMappingException {
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL, false);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_TEXT_VERSION, false);
        StdDeserializer currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        String json = "\"EUR\"";
        Currency result = mapper.readerWith(Currency.class).readValue(json);
        Assert.assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testPatternDeserialization() throws IOException, JsonMappingException {
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL, false);
        StdDeserializer patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        String json = "\"/[a-z]+\\.txt$/i\"";
        Pattern result = mapper.readerWith(Pattern.class).readValue(json);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.matcher("hello.txt").matches());
    }

    @Test
    public void testLocaleDeserialization() throws IOException, JsonMappingException {
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL, false);
        StdDeserializer localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        String json = "\"en_US\"";
        Locale result = mapper.readerWith(Locale.class).readValue(json);
        Assert.assertEquals(new Locale("en", "US"), result);
    }

    @Test
    public void testCharsetDeserialization() throws IOException, JsonMappingException {
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL, false);
        StdDeserializer charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        String json = "\"UTF-8\"";
        Charset result = mapper.readerWith(Charset.class).readValue(json);
        Assert.assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException, JsonMappingException {
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL, false);
        StdDeserializer timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        String json = "\"America/Los_Angeles\"";
        TimeZone result = mapper.readerWith(TimeZone.class).readValue(json);
        Assert.assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testInetAddressDeserialization() throws IOException, JsonMappingException {
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL, false);
        StdDeserializer inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        String json = "\"192.168.1.1\"";
        InetAddress result = mapper.readerWith(InetAddress.class).readValue(json);
        Assert.assertEquals(InetAddress.getByName("192.168.1.1"), result);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException, JsonMappingException {
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL, false);
        StdDeserializer inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String json = "{\"host\":\"192.168.1.1\",\"port\":1234}";
        InetSocketAddress result = mapper.readerWith(InetSocketAddress.class).readValue(json);
        Assert.assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 1234), result);
    }

    @Test
    public void testInvalidDeserialization() throws IOException, JsonMappingException {
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL, false);
        StdDeserializer invalidDeserializer = FromStringDeserializer.findDeserializer(Integer.class);
        String json = "\"invalid\"";
        try {
            Integer result = mapper.readerWith(Integer.class).readValue(json);
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Assert.assertEquals("Not a valid textual representation for integer: invalid", e.getMessage());
        }
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException, JsonMappingException {
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_STRING_AS_NULL, false);
        StdDeserializer emptyStringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String json = "\"\"";
        String result = mapper.readerWith(String.class).readValue(json);
        Assert.assertNull(result);
    }
}