package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.BaseApiMap;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.test.BaseTest;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest extends BaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializeFile() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(File.class);
        String input = "path/to/file.txt";
        File expected = new File(input);
        File deserialized = (File) serializer.deserialize(getTestParser(), getContext());
        Assert.assertEquals(expected, deserialized);
    }

    @Test
    public void testDeserializeURL() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(URL.class);
        String input = "http://test.com";
        URL expected = new URL(input);
        URL deserialized = (URL) serializer.deserialize(getTestParser(), getContext());
        Assert.assertEquals(expected, deserialized);
    }

    @Test
    public void testDeserializeURI() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(URI.class);
        String input = "http://test.com/path/to";
        URI expected = new URI(input);
        URI deserialized = (URI) serializer.deserialize(getTestParser(), getContext());
        Assert.assertEquals(expected, deserialized);
    }

    @Test
    public void testDeserializeClass() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(String.class);
        String input = "java.lang.String";
        Class<?> expected = String.class;
        Class<?> deserialized = (Class<?>) serializer.deserialize(getTestParser(), getContext());
        Assert.assertEquals(expected, deserialized);
    }

    @Test
    public void testDeserializeJavaType() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(JavaType.class);
        String input = "com.fasterxml.jackson.databind.type.SimpleType$InstanceType[Ljava/lang/String;";
        JavaType expected = objectMapper.getTypeFactory().constructType(String[].class);
        JavaType deserialized = (JavaType) serializer.deserialize(getTestParser(), getContext());
        Assert.assertEquals(expected, deserialized);
    }

    @Test
    public void testDeserializeCurrency() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Currency.class);
        String input = "USD";
        Currency expected = Currency.getInstance(Locale.US);
        Currency deserialized = (Currency) serializer.deserialize(getTestParser(), getContext());
        Assert.assertEquals(expected, deserialized);
    }

    @Test
    public void testDeserializePattern() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Pattern.class);
        String input = "([a-z]+)\\.json$";
        Pattern expected = Pattern.compile(input);
        Pattern deserialized = (Pattern) serializer.deserialize(getTestParser(), getContext());
        Assert.assertEquals(expected, deserialized);
    }

    @Test
    public void testDeserializeLocale() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Locale.class);
        String input = "es_ES";
        Locale expected = new Locale("es", "ES");
        Locale deserialized = (Locale) serializer.deserialize(getTestParser(), getContext());
        Assert.assertEquals(expected, deserialized);
    }

    @Test
    public void testDeserializeCharset() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Charset.class);
        String input = "UTF-8";
        Charset expected = Charset.forName(input);
        Charset deserialized = (Charset) serializer.deserialize(getTestParser(), getContext());
        Assert.assertEquals(expected, deserialized);
    }

    @Test
    public void testDeserializeTimeZone() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        String input = "America/Los_Angeles";
        TimeZone expected = TimeZone.getTimeZone(input);
        TimeZone deserialized = (TimeZone) serializer.deserialize(getTestParser(), getContext());
        Assert.assertEquals(expected, deserialized);
    }

    @Test
    public void testDeserializeInetAddress() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        String input = "192.168.0.1";
        InetAddress expected = InetAddress.getByName(input);
        InetAddress deserialized = (InetAddress) serializer.deserialize(getTestParser(), getContext());
        Assert.assertEquals(expected, deserialized);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String input = "[192.168.0.1]:80";
        InetSocketAddress expected = new InetSocketAddress("192.168.0.1", 80);
        InetSocketAddress deserialized = (InetSocketAddress) serializer.deserialize(getTestParser(), getContext());
        Assert.assertEquals(expected, deserialized);
    }

    @Test
    public void testDeserializeStringBuilder() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        String input = "Hello, World!";
        StringBuilder expected = new StringBuilder(input);
        StringBuilder deserialized = (StringBuilder) serializer.deserialize(getTestParser(), getContext());
        Assert.assertEquals(expected, deserialized);
    }

    @Test
    public void testDeserializeEmptyStringFile() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(File.class);
        String input = "";
        File deserialized = (File) serializer.deserialize(getTestParserEmptyString(), getContext());
        Assert.assertNull(deserialized);
    }

    @Test
    public void testDeserializeEmptyStringURL() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(URL.class);
        String input = "";
        URL deserialized = (URL) serializer.deserialize(getTestParserEmptyString(), getContext());
        Assert.assertNull(deserialized);
    }

    @Test
    public void testDeserializeEmptyStringURI() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(URI.class);
        String input = "";
        URI deserialized = (URI) serializer.deserialize(getTestParserEmptyString(), getContext());
        Assert.assertNull(deserialized);
    }

    @Test
    public void testDeserializeEmptyStringClass() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(String.class);
        String input = "";
        String deserialized = (String) serializer.deserialize(getTestParserEmptyString(), getContext());
        Assert.assertEquals("", deserialized);
    }

    @Test
    public void testDeserializeEmptyStringJavaType() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(JavaType.class);
        String input = "";
        JavaType deserialized = (JavaType) serializer.deserialize(getTestParserEmptyString(), getContext());
        Assert.assertNull(deserialized);
    }

    @Test
    public void testDeserializeEmptyStringCurrency() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Currency.class);
        String input = "";
        Currency deserialized = (Currency) serializer.deserialize(getTestParserEmptyString(), getContext());
        Assert.assertNull(deserialized);
    }

    @Test
    public void testDeserializeEmptyStringPattern() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Pattern.class);
        String input = "";
        Pattern deserialized = (Pattern) serializer.deserialize(getTestParserEmptyString(), getContext());
        Assert.assertNull(deserialized);
    }

    @Test
    public void testDeserializeEmptyStringLocale() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Locale.class);
        String input = "";
        Locale deserialized = (Locale) serializer.deserialize(getTestParserEmptyString(), getContext());
        Assert.assertEquals(Locale.ROOT, deserialized);
    }

    @Test
    public void testDeserializeEmptyStringCharset() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Charset.class);
        String input = "";
        Charset deserialized = (Charset) serializer.deserialize(getTestParserEmptyString(), getContext());
        Assert.assertNull(deserialized);
    }

    @Test
    public void testDeserializeEmptyStringTimeZone() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        String input = "";
        TimeZone deserialized = (TimeZone) serializer.deserialize(getTestParserEmptyString(), getContext());
        Assert.assertEquals(TimeZone.getDefault(), deserialized);
    }

    @Test
    public void testDeserializeEmptyStringInetAddress() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        String input = "";
        InetAddress deserialized = (InetAddress) serializer.deserialize(getTestParserEmptyString(), getContext());
        Assert.assertNull(deserialized);
    }

    @Test
    public void testDeserializeEmptyStringInetSocketAddress() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String input = "";
        InetSocketAddress deserialized = (InetSocketAddress) serializer.deserialize(getTestParserEmptyString(), getContext());
        Assert.assertNull(deserialized);
    }

    @Test
    public void testDeserializeEmptyStringStringBuilder() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        String input = "";
        StringBuilder deserialized = (StringBuilder) serializer.deserialize(getTestParserEmptyString(), getContext());
        Assert.assertEquals("", deserialized.toString());
    }

    @Test
    public void testDeserializeArray() throws JsonMappingException, IOException {
        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Integer[].class);
        String input = "[1, 2, 3]";
        Integer[] expected = new Integer[]{1, 2, 3};
        Integer[] deserialized = (Integer[]) serializer.deserialize(getTestParserArray(), getContext());
        Assert.assertArrayEquals(expected, deserialized);
    }

    @Test
    public void testDeserializeEmbeddedObject() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper embeddedObjectMapper = ObjectMapper.simpleBuilder()
                .withoutFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
        JsonNode node = objectMapper.readTree("{\"value\": \"Hello, World!\"}");
        String input = node.toString();
        String expected = "Hello, World!";

        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(String.class);
        String deserialized = (String) serializer.deserializeEmbedded(getTestEmbeddedObject(node), getContext());
        Assert.assertEquals(expected, deserialized);
    }

    private JsonParser getTestParser() throws IOException {
        String json = "{\"file\": \"path/to/file.txt\", \"url\": \"http://test.com\", " +
                "\"uri\": \"http://test.com/path/to\", \"class\": \"java.lang.String\", " +
                "\"javaType\": \"com.fasterxml.jackson.databind.type.SimpleType$InstanceType[Ljava/lang/String;]\", " +
                "\"currency\": \"USD\", \"pattern\": \"([a-z]+)\\.json$\", \"locale\": \"es_ES\", " +
                "\"charset\": \"UTF-8\", \"timeZone\": \"America/Los_Angeles\", \"inetAddress\": \"192.168.0.1\", " +
                "\"inetSocketAddress\": \"[192.168.0.1]:80\", \"stringBuilder\": \"Hello, World!\"}";
        return objectMapper.getFactory().createParser(json);
    }

    private JsonParser getTestParserEmptyString() throws IOException {
        String json = "{\"file\": \"\", \"url\": \"\", \"uri\": \"\", \"class\": \"\", " +
                "\"javaType\": null, \"currency\": null, \"pattern\": null, \"locale\": \"\", " +
                "\"charset\": null, \"timeZone\": null, \"inetAddress\": null, " +
                "\"inetSocketAddress\": null, \"stringBuilder\": \"\"}";
        return objectMapper.getFactory().createParser(json);
    }

    private JsonParser getTestParserArray() throws IOException {
        String json = "[1, 2, 3]";
        return objectMapper.getFactory().createParser(json);
    }

    private Object getTestEmbeddedObject(JsonNode node) throws IOException {
        return BaseApiMap.TreeToValue(node, getContext());
    }

    private DeserializationContext getContext() {
        return objectMapper.getDeserializationContext();
    }
}