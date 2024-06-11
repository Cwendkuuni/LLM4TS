package com.fasterxml.jackson.databind.deser.std.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FromStringDeserializerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFileDeserialization() throws IOException {
        Std fileDeserializer = Std.findDeserializer(File.class);
        File file = new File("test.txt");
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(file);

        File deserializedFile = fileDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(file, deserializedFile);
    }

    @Test
    public void testUrlDeserialization() throws IOException {
        Std urlDeserializer = Std.findDeserializer(URL.class);
        String jsonString = "http://www.example.com";

        URL deserializedUrl = urlDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(new URL("http://www.example.com"), deserializedUrl);
    }

    @Test
    public void testUriDeserialization() throws IOException {
        Std uriDeserializer = Std.findDeserializer(URI.class);
        String jsonString = "http://www.example.com/path/to/file.txt";

        URI deserializedUri = uriDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(new URI("http://www.example.com/path/to/file.txt"), deserializedUri);
    }

    @Test
    public void testClassDeserialization() throws IOException {
        Std classDeserializer = Std.findDeserializer(String.class);
        String jsonString = "java.lang.String";

        String deserializedClass = (String) classDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals("java.lang.String", deserializedClass);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        Std currencyDeserializer = Std.findDeserializer(Currency.class);
        String jsonString = "EUR";

        Currency deserializedCurrency = currencyDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("EUR"), deserializedCurrency);
    }

    @Test
    public void testInvalidPatternDeserialization() throws IOException {
        Std patternDeserializer = Std.findDeserializer(Pattern.class);

        String jsonString = "^[A-Za-z]+$\\$"; // invalid regex pattern

        assertThrows(JsonMappingException.class, () -> patternDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext()));
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        Std localeDeserializer = Std.findDeserializer(Locale.class);
        String jsonString = "en_US";

        Locale deserializedLocale = localeDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(Locale.US, deserializedLocale);

        jsonString = "en-US_POSIX";
        deserializedLocale = localeDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(Locale.US, deserializedLocale);

        jsonString = "en_US_POSIX";
        deserializedLocale = localeDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(Locale.US, deserializedLocale);

        jsonString = "en_US.UTF-8";
        deserializedLocale = localeDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(Locale.US, deserializedLocale);

        jsonString = "en_US.UTF-8@currency=USD";
        deserializedLocale = localeDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(Locale.US, deserializedLocale);
        assertEquals(Currency.getInstance("USD"), deserializedLocale.getCurrency());

        jsonString = "en_US.UTF-8_POSIX";
        deserializedLocale = localeDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(Locale.US, deserializedLocale);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        Std charsetDeserializer = Std.findDeserializer(Charset.class);
        String jsonString = "UTF-8";

        Charset deserializedCharset = charsetDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), deserializedCharset);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        Std timeZoneDeserializer = Std.findDeserializer(TimeZone.class);
        String jsonString = "America/Los_Angeles";

        TimeZone deserializedTimeZone = timeZoneDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), deserializedTimeZone);
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        Std inetAddressDeserializer = Std.findDeserializer(InetAddress.class);
        String jsonString = "192.168.1.1";

        InetAddress deserializedInetAddress = inetAddressDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(InetAddress.getByName("192.168.1.1"), deserializedInetAddress);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        Std inetSocketAddressDeserializer = Std.findDeserializer(InetSocketAddress.class);
        String jsonString = "[255.255.255.255]:8080";

        InetSocketAddress deserializedInetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 8080), deserializedInetSocketAddress);

        jsonString = "192.168.1.1:8080";
        deserializedInetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 8080), deserializedInetSocketAddress);

        jsonString = "192.168.1.1:8080/path";
        deserializedInetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertNull(deserializedInetSocketAddress);
    }

    @Test
    public void testStringBuilderDeserialization() throws IOException {
        Std stringBuilderDeserializer = Std.findDeserializer(StringBuilder.class);
        String jsonString = "new StringBuilder(\"Test\")";

        StringBuilder deserializedStringBuilder = (StringBuilder) stringBuilderDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(new StringBuilder("Test"), deserializedStringBuilder);
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        Std<StringBuilder> stringBuilderDeserializer = Std.findDeserializer(StringBuilder.class);
        String jsonString = "\"\"";

        StringBuilder deserializedStringBuilder = stringBuilderDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext());
        assertEquals(new StringBuilder(), deserializedStringBuilder);
    }

    @Test
    public void testEmptyJsonObjectDeserialization() throws IOException {
        Std<String> stringDeserializer = Std.findDeserializer(String.class);

        String jsonString = "{}";
        assertNull(stringDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext()));
    }

    @Test
    public void testEmptyJsonArrayDeserialization() throws IOException {
        Std<Object[]> objectArrayDeserializer = Std.findDeserializer(Object[].class);

        String jsonString = "[]";
        assertNull(objectArrayDeserializer.deserialize(mapper.readTree(jsonString), mapper.getDeserializationContext()));
    }

    @Test
    public void testUnexpectedTokenDeserialization() throws IOException {
        Std<Object> objectDeserializer = Std.findDeserializer(Object.class);

        String jsonString = "{\"name\":\"John\", \"age\":30, \"address\":[\"123 Main St\", \"San Francisco, CA 94111\"]}";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonProcessingException e = assertThrows(JsonProcessingException.class,
                () -> objectDeserializer.deserialize(objectMapper.readTree(jsonString), mapper.getDeserializationContext()));
        assertEquals("Unexpected token (START_OBJECT), expecting null", e.getMessage());
    }
}