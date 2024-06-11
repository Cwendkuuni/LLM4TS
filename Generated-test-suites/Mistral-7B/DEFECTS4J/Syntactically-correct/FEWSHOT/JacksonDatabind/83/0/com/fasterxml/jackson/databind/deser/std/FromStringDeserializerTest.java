package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<File> deserializer = findDeserializer(File.class);
        String fileString = "/path/to/file.txt";
        File expectedFile = new File("/path/to/file.txt");
        File actualFile = mapper.readValue(TextNode.valueOf(fileString).asText(), deserializer);
        assertEquals(expectedFile, actualFile);
    }

    @Test
    public void testDeserializeURL() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> deserializer = findDeserializer(URL.class);
        String urlString = "http://example.com";
        URL expectedUrl = new URL("http://example.com");
        URL actualUrl = mapper.readValue(TextNode.valueOf(urlString).asText(), deserializer);
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    public void testDeserializeURI() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> deserializer = findDeserializer(URI.class);
        String uriString = "http://example.com";
        URI expectedUri = new URI("http://example.com");
        URI actualUri = mapper.readValue(TextNode.valueOf(uriString).asText(), deserializer);
        assertEquals(expectedUri, actualUri);
    }

    @Test
    public void testDeserializeClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Class<?>> deserializer = findDeserializer(Class.class);
        String className = "java.lang.String";
        Class<?> expectedClass = String.class;
        Class<?> actualClass = mapper.readValue(TextNode.valueOf(className).asText(), deserializer);
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void testDeserializeJavaType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<JavaType> deserializer = findDeserializer(JavaType.class);
        String className = "java.util.List<java.lang.String>";
        JavaType expectedType = new TypeReference<List<String>>() {}.getType();
        JavaType actualType = mapper.readValue(TextNode.valueOf(className).asText(), deserializer);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Currency> deserializer = findDeserializer(Currency.class);
        String currencyString = "USD";
        Currency expectedCurrency = Currency.getInstance("USD");
        Currency actualCurrency = mapper.readValue(TextNode.valueOf(currencyString).asText(), deserializer);
        assertEquals(expectedCurrency, actualCurrency);
    }

    @Test
    public void testDeserializePattern() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Pattern> deserializer = findDeserializer(Pattern.class);
        String patternString = "\\w+@\\w+\\.\\w+";
        Pattern expectedPattern = Pattern.compile(patternString);
        Pattern actualPattern = mapper.readValue(TextNode.valueOf(patternString).asText(), deserializer);
        assertEquals(expectedPattern, actualPattern);
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> deserializer = findDeserializer(Locale.class);
        String localeString = "en_US";
        Locale expectedLocale = Locale.forLanguageTag("en_US");
        Locale actualLocale = mapper.readValue(TextNode.valueOf(localeString).asText(), deserializer);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Charset> deserializer = findDeserializer(Charset.class);
        String charsetString = "UTF-8";
        Charset expectedCharset = Charset.forName(charsetString);
        Charset actualCharset = mapper.readValue(TextNode.valueOf(charsetString).asText(), deserializer);
        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<TimeZone> deserializer = findDeserializer(TimeZone.class);
        String timeZoneString = "UTC";
        TimeZone expectedTimeZone = TimeZone.getTimeZone("UTC");
        TimeZone actualTimeZone = mapper.readValue(TextNode.valueOf(timeZoneString).asText(), deserializer);
        assertEquals(expectedTimeZone, actualTimeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetAddress> deserializer = findDeserializer(InetAddress.class);
        String ipAddressString = "192.168.1.1";
        InetAddress expectedIpAddress = InetAddress.getByName("192.168.1.1");
        InetAddress actualIpAddress = mapper.readValue(TextNode.valueOf(ipAddressString).asText(), deserializer);
        assertEquals(expectedIpAddress, actualIpAddress);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> deserializer = findDeserializer(InetSocketAddress.class);
        String socketAddressString = "[255.255.255.255]:8080";
        InetSocketAddress expectedSocketAddress = new InetSocketAddress("255.255.255.255", 8080);
        InetSocketAddress actualSocketAddress = mapper.readValue(TextNode.valueOf(socketAddressString).asText(), deserializer);
        assertEquals(expectedSocketAddress, actualSocketAddress);
    }

    @Test
    public void testDeserializeStringBuilder() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<StringBuilder> deserializer = findDeserializer(StringBuilder.class);
        String stringBuilderString = "Hello, World!";
        StringBuilder expectedStringBuilder = new StringBuilder("Hello, World!");
        StringBuilder actualStringBuilder = mapper.readValue(TextNode.valueOf(stringBuilderString).asText(), deserializer);
        assertEquals(expectedStringBuilder, actualStringBuilder);
    }

    @Test
    public void testDeserializeEmptyString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<StringBuilder> deserializer = findDeserializer(StringBuilder.class);
        String emptyString = "";
        StringBuilder expectedEmptyString = new StringBuilder();
        StringBuilder actualEmptyString = mapper.readValue(TextNode.valueOf(emptyString).asText(), deserializer);
        assertEquals(expectedEmptyString, actualEmptyString);
    }

    @Test
    public void testDeserializeNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<StringBuilder> deserializer = findDeserializer(StringBuilder.class);
        String nullString = null;
        StringBuilder expectedNullString = null;
        StringBuilder actualNullString = mapper.readValue(TextNode.valueOf(nullString).asText(), deserializer);
        assertEquals(expectedNullString, actualNullString);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> deserializer = findDeserializer(Locale.class);
        String invalidLocaleString = "en_US_Invalid";
        mapper.readValue(TextNode.valueOf(invalidLocaleString).asText(), deserializer);
    }

    @Test(expected = MalformedURLException.class)
    public void testDeserializeMalformedURL() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> deserializer = findDeserializer(URL.class);
        String malformedURLString = "invalid:/url";
        mapper.readValue(TextNode.valueOf(malformedURLString).asText(), deserializer);
    }

    @Test
    public void testDeserializeEmptyArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Object> deserializer = findDeserializer(Object.class);
        String emptyArrayString = "[]";
        Object actualEmptyArray = mapper.readValue(TextNode.valueOf(emptyArrayString).asText(), deserializer);
        assertNull(actualEmptyArray);
    }

    @Test
    public void testDeserializeArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Object> deserializer = findDeserializer(Object.class);
        String arrayString = "[1, 2, 3]";
        ObjectMapper mapper2 = new ObjectMapper();
        ObjectMapper.DefaultTyping typeFinder = ObjectMapper.DefaultTyping.INSTANCE;
        List<Integer> expectedArray = new ArrayList<>();
        expectedArray.add(1);
        expectedArray.add(2);
        expectedArray.add(3);
        JavaType expectedType = mapper2.getTypeFactory().constructArrayType(Integer.class);
        Object actualArray = mapper.readValue(TextNode.valueOf(arrayString).asText(), deserializer);
        assertEquals(expectedType, actualArray.getClass().getComponentType().getComponentType());
        List<?> actualList = mapper.readValue(TextNode.valueOf(arrayString).asText(), new TypeReference<List<Object>>() {}.getType());
        assertEquals(expectedArray, actualList);
    }

    @Test
    public void testDeserializeEmbeddedObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Object> deserializer = findDeserializer(Object.class);
        String embeddedObjectString = "{\"name\": \"John\", \"age\": 30}";
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
        Date date = new Date();
        ObjectMapper.DefaultTyping typeFinder = ObjectMapper.DefaultTyping.INSTANCE;
        String expectedJsonString = "{\"name\": \"John\", \"age\": 30, \"birthday\": \"2022-03-17 10:30:00 UTC\"}";
        Object expectedObject = new Object() {{
            put("name", "John");
            put("age", 30);
            put("birthday", dateFormat.format(date));
        }};
        Object actualObject = mapper.readValue(TextNode.valueOf(embeddedObjectString).asText(), deserializer);
        assertEquals(expectedObject.getClass(), actualObject.getClass());
        ObjectMapper mapper2 = new ObjectMapper();
        mapper2.registerModule(new ParamNameModule());
        mapper2.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectMapper serializer = mapper2.builder()
                .writerWithDefaultPrettyPrinter()
                .build();
        String actualJsonString = serializer.writeValueAsString(actualObject);
        assertEquals(expectedJsonString, actualJsonString);
    }

    private static FromStringDeserializer findDeserializer(Class<?> clazz) {
        return (FromStringDeserializer<?>) FromStringDeserializer.findDeserializer(clazz);
    }
}