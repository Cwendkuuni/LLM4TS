package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.test.BaseMapTestConfig;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializeFile() throws IOException, JsonProcessingException {
        FromStringDeserializer<File> deserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        String fileString = "path/to/file.txt";
        File expectedFile = new File("path/to/file.txt");
        File deserializedFile = deserializer.deserialize(objectMapper.getFactory().createParser(fileString), objectMapper.getDeserializationContext());
        assertEquals(expectedFile, deserializedFile);
    }

    @Test
    public void testDeserializeURL() throws IOException, JsonProcessingException {
        FromStringDeserializer<URL> deserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        String urlString = "http://example.com";
        URL expectedUrl = new URL("http://example.com");
        URL deserializedUrl = deserializer.deserialize(objectMapper.getFactory().createParser(urlString), objectMapper.getDeserializationContext());
        assertEquals(expectedUrl, deserializedUrl);
    }

    @Test
    public void testDeserializeURI() throws IOException, JsonProcessingException {
        FromStringDeserializer<URI> deserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        String uriString = "http://example.com/path";
        URI expectedUri = new URI("http", "example.com", "/path", null);
        URI deserializedUri = deserializer.deserialize(objectMapper.getFactory().createParser(uriString), objectMapper.getDeserializationContext());
        assertEquals(expectedUri, deserializedUri);
    }

    @Test
    public void testDeserializeClass() throws IOException, JsonProcessingException {
        FromStringDeserializer<String> deserializer = (FromStringDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
        String className = "java.lang.String";
        String stringValue = "Test String";
        String deserializedString = deserializer.deserialize(objectMapper.getFactory().createParser(className + ": " + stringValue), objectMapper.getDeserializationContext()).getValueAsString();
        assertEquals(stringValue, deserializedString);
    }

    @Test
    public void testDeserializeJavaType() throws IOException, JsonProcessingException {
        FromStringDeserializer<JavaType> deserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType expectedJavaType = objectMapper.getTypeFactory().constructType(String.class);
        JavaType deserializedJavaType = deserializer.deserialize(objectMapper.getFactory().createParser("java.lang.String"), objectMapper.getDeserializationContext());
        assertEquals(expectedJavaType, deserializedJavaType);
    }

    @Test
    public void testDeserializeCurrency() throws IOException, JsonProcessingException {
        FromStringDeserializer<Currency> deserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        String currencyString = "EUR";
        Currency expectedCurrency = Currency.getInstance(Locale.US, new TimeZone("UTC"), null);
        expectedCurrency = expectedCurrency.getInstance(Locale.US, new TimeZone("UTC"), null); // EUR is ambiguous
        expectedCurrency = expectedCurrency.getInstance(Locale.US, new TimeZone("UTC"), null); // EUR is ambiguous again
        expectedCurrency = expectedCurrency.getInstance(new Locale("de", "DE"), new TimeZone("Europe/Berlin"));
        Currency deserializedCurrency = deserializer.deserialize(objectMapper.getFactory().createParser(currencyString), objectMapper.getDeserializationContext());
        assertEquals(expectedCurrency, deserializedCurrency);
    }

    @Test
    public void testDeserializePattern() throws IOException, JsonProcessingException {
        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        String patternString = "\\d+";
        Pattern expectedPattern = Pattern.compile(patternString);
        Pattern deserializedPattern = deserializer.deserialize(objectMapper.getFactory().createParser(patternString), objectMapper.getDeserializationContext());
        assertEquals(expectedPattern, deserializedPattern);
    }

    @Test
    public void testDeserializeLocale() throws IOException, JsonProcessingException {
        FromStringDeserializer<Locale> deserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale deserializedLocale = deserializer.deserialize(objectMapper.getFactory().createParser(localeString), objectMapper.getDeserializationContext());
        assertEquals(expectedLocale, deserializedLocale);

        localeString = "de_DE";
        expectedLocale = new Locale("de", "DE");
        expectedLocale = new Locale("de", "DE", "Europe/Berlin");
        deserializedLocale = deserializer.deserialize(objectMapper.getFactory().createParser(localeString), objectMapper.getDeserializationContext());
        assertEquals(expectedLocale, deserializedLocale);
    }

    @Test
    public void testDeserializeCharset() throws IOException, JsonProcessingException {
        FromStringDeserializer<Charset> deserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        String charsetString = "UTF-8";
        Charset expectedCharset = Charset.forName(charsetString);
        Charset deserializedCharset = deserializer.deserialize(objectMapper.getFactory().createParser(charsetString), objectMapper.getDeserializationContext());
        assertEquals(expectedCharset, deserializedCharset);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException, JsonProcessingException {
        FromStringDeserializer<TimeZone> deserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        String timeZoneString = "Europe/Berlin";
        TimeZone expectedTimeZone = TimeZone.getTimeZone(timeZoneString);
        TimeZone deserializedTimeZone = deserializer.deserialize(objectMapper.getFactory().createParser(timeZoneString), objectMapper.getDeserializationContext());
        assertEquals(expectedTimeZone, deserializedTimeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException, JsonProcessingException {
        FromStringDeserializer<InetAddress> deserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        String inetAddressString = "192.168.0.1";
        InetAddress expectedInetAddress = InetAddress.getByName(inetAddressString);
        InetAddress deserializedInetAddress = deserializer.deserialize(objectMapper.getFactory().createParser(inetAddressString), objectMapper.getDeserializationContext());
        assertEquals(expectedInetAddress, deserializedInetAddress);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException, JsonProcessingException {
        FromStringDeserializer<InetSocketAddress> deserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String inetSocketAddressString = "[255.255.255.255]:80";
        InetSocketAddress expectedInetSocketAddress = new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 80);
        InetSocketAddress deserializedInetSocketAddress = deserializer.deserialize(objectMapper.getFactory().createParser(inetSocketAddressString), objectMapper.getDeserializationContext());
        assertEquals(expectedInetSocketAddress, deserializedInetSocketAddress);

        inetSocketAddressString = "example.com:80";
        expectedInetSocketAddress = new InetSocketAddress("example.com", 80);
        deserializedInetSocketAddress = deserializer.deserialize(objectMapper.getFactory().createParser(inetSocketAddressString), objectMapper.getDeserializationContext());
        assertEquals(expectedInetSocketAddress, deserializedInetSocketAddress);
    }

    @Test
    public void testDeserializeStringBuilder() throws IOException, JsonProcessingException {
        FromStringDeserializer<StringBuilder> deserializer = (FromStringDeserializer<StringBuilder>) FromStringDeserializer.findDeserializer(StringBuilder.class);
        String stringBuilderString = "Test";
        StringBuilder expectedStringBuilder = new StringBuilder("Test");
        StringBuilder deserializedStringBuilder = deserializer.deserialize(objectMapper.getFactory().createParser(stringBuilderString), objectMapper.getDeserializationContext());
        assertEquals(expectedStringBuilder.toString(), deserializedStringBuilder.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNull() throws IOException, JsonProcessingException {
        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
        String nullString = null;
        deserializer.deserialize(objectMapper.getFactory().createParser(nullString), objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidURL() throws IOException, JsonProcessingException {
        FromStringDeserializer<URL> deserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        String invalidURLString = "invalid_url";
        deserializer.deserialize(objectMapper.getFactory().createParser(invalidURLString), objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidLocale() throws IOException, JsonProcessingException {
        FromStringDeserializer<Locale> deserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        String invalidLocaleString = "InvalidLocale";
        deserializer.deserialize(objectMapper.getFactory().createParser(invalidLocaleString), objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidTimeZone() throws IOException, JsonProcessingException {
        FromStringDeserializer<TimeZone> deserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        String invalidTimeZoneString = "InvalidTimeZone";
        deserializer.deserialize(objectMapper.getFactory().createParser(invalidTimeZoneString), objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidCurrency() throws IOException, JsonProcessingException {
        FromStringDeserializer<Currency> deserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        String invalidCurrencyString = "InvalidCurrency";
        deserializer.deserialize(objectMapper.getFactory().createParser(invalidCurrencyString), objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidPattern() throws IOException, JsonProcessingException {
        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        String invalidPatternString = "InvalidPattern";
        deserializer.deserialize(objectMapper.getFactory().createParser(invalidPatternString), objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidCharset() throws IOException, JsonProcessingException {
        FromStringDeserializer<Charset> deserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        String invalidCharsetString = "InvalidCharset";
        deserializer.deserialize(objectMapper.getFactory().createParser(invalidCharsetString), objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidInetAddress() throws IOException, JsonProcessingException {
        FromStringDeserializer<InetAddress> deserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        String invalidInetAddressString = "InvalidAddress";
        deserializer.deserialize(objectMapper.getFactory().createParser(invalidInetAddressString), objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidInetSocketAddress() throws IOException, JsonProcessingException {
        FromStringDeserializer<InetSocketAddress> deserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String invalidInetSocketAddressString = "[InvalidAddress]:80";
        deserializer.deserialize(objectMapper.getFactory().createParser(invalidInetSocketAddressString), objectMapper.getDeserializationContext());

        invalidInetSocketAddressString = "example.com:InvalidPort";
        deserializer.deserialize(objectMapper.getFactory().createParser(invalidInetSocketAddressString), objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyString() throws IOException, JsonProcessingException {
        FromStringDeserializer<Object> deserializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
        String emptyString = "";
        deserializer.deserialize(objectMapper.getFactory().createParser(emptyString), objectMapper.getDeserializationContext());
    }
}