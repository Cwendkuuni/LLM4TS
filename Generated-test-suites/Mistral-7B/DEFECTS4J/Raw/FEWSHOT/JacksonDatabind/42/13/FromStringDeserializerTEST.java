package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.Std;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testStdDeserializer() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test deserialization of simple types
        String jsonFile = "{\"className\":\"java.io.File\",\"value\":\"test.txt\"}";
        Std fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = mapper.readValue(jsonFile, fileDeserializer.getType());
        assertEquals("test.txt", result.getName());

        String jsonUrl = "{\"className\":\"java.net.URL\",\"value\":\"http://www.example.com\"}";
        Std urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL urlResult = mapper.readValue(jsonUrl, urlDeserializer.getType());
        assertEquals("http://www.example.com", urlResult.toString());

        String jsonUri = "{\"className\":\"java.net.URI\",\"value\":\"http://www.example.com/path/to/file.txt\"}";
        Std uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI uriResult = mapper.readValue(jsonUri, uriDeserializer.getType());
        assertEquals("http://www.example.com/path/to/file.txt", uriResult.toString());

        String jsonClass = "{\"className\":\"java.lang.Class\",\"value\":\"java.util.Date\"}";
        Std classDeserializer = FromStringDeserializer.findDeserializer(Class.class);
        Class<?> clazzResult = mapper.readValue(jsonClass, classDeserializer.getType());
        assertEquals(Date.class, clazzResult);

        String jsonJavaType = "{\"className\":\"com.fasterxml.jackson.databind.JavaType\",\"value\":\"java.lang.Integer\"}";
        Std javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType javaTypeResult = mapper.readValue(jsonJavaType, javaTypeDeserializer.getType());
        assertEquals(Integer.class, javaTypeResult.getRawClass());

        String jsonCurrency = "{\"className\":\"java.util.Currency\",\"value\":\"USD\"}";
        Std currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency currencyResult = mapper.readValue(jsonCurrency, currencyDeserializer.getType());
        assertEquals(Currency.getInstance("USD"), currencyResult);

        String jsonPattern = "{\"className\":\"java.util.regex.Pattern\",\"value\":\"\\w+\\.\\w+@\\w+\\.\\w+\"}";
        Std patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern patternResult = mapper.readValue(jsonPattern, patternDeserializer.getType());
        assertEquals(Pattern.compile("\\w+\\.\\w+@\\w+\\.\\w+"), patternResult);

        String jsonLocale = "{\"className\":\"java.util.Locale\",\"value\":\"en_US\"}";
        Std localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale localeResult = mapper.readValue(jsonLocale, localeDeserializer.getType());
        assertEquals(Locale.US, localeResult);

        String jsonCharset = "{\"className\":\"java.nio.charset.Charset\",\"value\":\"UTF-8\"}";
        Std charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset charsetResult = mapper.readValue(jsonCharset, charsetDeserializer.getType());
        assertEquals(Charset.forName("UTF-8"), charsetResult);

        String jsonTimeZone = "{\"className\":\"java.util.TimeZone\",\"value\":\"America/Los_Angeles\"}";
        Std timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone timeZoneResult = mapper.readValue(jsonTimeZone, timeZoneDeserializer.getType());
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), timeZoneResult);

        String jsonInetAddress = "{\"className\":\"java.net.InetAddress\",\"value\":\"127.0.0.1\"}";
        Std inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress inetAddressResult = mapper.readValue(jsonInetAddress, inetAddressDeserializer.getType());
        assertEquals(InetAddress.getLocalHost(), inetAddressResult);

        String jsonInetSocketAddress = "{\"className\":\"java.net.InetSocketAddress\",\"value\":\"127.0.0.1:80\"}";
        Std inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress inetSocketAddressResult = mapper.readValue(jsonInetSocketAddress, inetSocketAddressDeserializer.getType());
        assertEquals(new InetSocketAddress(InetAddress.getLocalHost(), 80), inetSocketAddressResult);

        String jsonNull = "{\"className\":\"java.lang.String\",\"value\":\"null\"}";
        Std nullDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String nullResult = mapper.readValue(jsonNull, nullDeserializer.getType());
        assertNull(nullResult);

        String jsonEmptyString = "{\"className\":\"java.lang.String\",\"value\":\"\"}";
        Std emptyStringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String emptyStringResult = mapper.readValue(jsonEmptyString, emptyStringDeserializer.getType());
        assertNull(emptyStringResult);

        // Test invalid JSON input
        String jsonInvalidValue = "{\"className\":\"java.net.URL\",\"value\":\"invalid_value\"}";
        try {
            mapper.readValue(jsonInvalidValue, URL.class);
            fail("Expected JsonMappingException for invalid value");
        } catch (JsonMappingException e) {
            assertEquals("Cannot construct instance of java.net.URL from String value (null) using default constructor", e.getMessage());
        }

        String jsonEmptyClass = "{\"className\":\"java.lang.String\",\"value\":\"java.lang.Class\"}";
        try {
            mapper.readValue(jsonEmptyClass, Class.class);
            fail("Expected JsonMappingException for empty class name");
        } catch (JsonMappingException e) {
            assertEquals("Class name 'java.lang.Class' used in JSON text but not found in the classpath", e.getMessage());
        }

        String jsonUnknownClass = "{\"className\":\"java.lang.String\",\"value\":\"com.unknown.UnknownClass\"}";
        try {
            mapper.readValue(jsonUnknownClass, Class.class);
            fail("Expected JsonMappingException for unknown class");
        } catch (JsonMappingException e) {
            assertEquals("Class com.unknown.UnknownClass not found in classpath", e.getMessage());
        }
    }
}