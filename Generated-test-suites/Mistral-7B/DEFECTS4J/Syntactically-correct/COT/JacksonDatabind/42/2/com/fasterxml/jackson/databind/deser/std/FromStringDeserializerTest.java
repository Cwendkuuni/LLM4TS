package com.example.test.jackson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.Std;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @InjectMocks
    private Std fileDeserializer = new Std(File.class, Std.STD_FILE);
    @InjectMocks
    private Std urlDeserializer = new Std(URL.class, Std.STD_URL);
    @InjectMocks
    private Std uriDeserializer = new Std(URI.class, Std.STD_URI);
    @InjectMocks
    private Std classDeserializer = new Std(Class.class, Std.STD_CLASS);
    @InjectMocks
    private Std javaTypeDeserializer = new Std(JavaType.class, Std.STD_JAVA_TYPE);
    @InjectMocks
    private Std currencyDeserializer = new Std(Currency.class, Std.STD_CURRENCY);
    @InjectMocks
    private Std patternDeserializer = new Std(Pattern.class, Std.STD_PATTERN);
    @InjectMocks
    private Std localeDeserializer = new Std(Locale.class, Std.STD_LOCALE);
    @InjectMocks
    private Std charsetDeserializer = new Std(Charset.class, Std.STD_CHARSET);
    @InjectMocks
    private Std timeZoneDeserializer = new Std(TimeZone.class, Std.STD_TIME_ZONE);
    @InjectMocks
    private Std inetAddressDeserializer = new Std(InetAddress.class, Std.STD_INET_ADDRESS);
    @InjectMocks
    private Std inetSocketAddressDeserializer = new Std(InetSocketAddress.class, Std.STD_INET_SOCKET_ADDRESS);

    private ObjectMapper objectMapper;

    public FromStringDeserializerTest() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testFileDeserialize() throws IOException {
        String fileString = "/path/to/file.txt";
        File expectedFile = new File(fileString);
        File resultFile = fileDeserializer.deserialize(createJsonParser("{\"value\":\"" + fileString + "\"}"), createContext());
        assertEquals(expectedFile, resultFile);
    }

    @Test
    public void testUrlDeserialize() throws IOException {
        String urlString = "http://example.com";
        URL expectedUrl = new URL(urlString);
        URL resultUrl = urlDeserializer.deserialize(createJsonParser("{\"value\":\"" + urlString + "\"}"), createContext());
        assertEquals(expectedUrl, resultUrl);
    }

    @Test
    public void testUriDeserialize() throws IOException {
        String uriString = "http://example.com/path/to?";
        URI expectedUri = new URI(uriString);
        URI resultUri = uriDeserializer.deserialize(createJsonParser("{\"value\":\"" + uriString + "\"}"), createContext());
        assertEquals(expectedUri, resultUri);
    }

    @Test
    public void testClassDeserialize() throws IOException {
        String className = "java.lang.String";
        Class<?> expectedClass = Class.forName(className);
        Class<?> resultClass = classDeserializer.deserialize(createJsonParser("{\"value\":\"" + className + "\"}"), createContext());
        assertEquals(expectedClass, resultClass);
    }

    @Test
    public void testJavaTypeDeserialize() throws IOException {
        String javaTypeCanonicalName = "java.util.List<java.lang.String>";
        JavaType expectedJavaType = objectMapper.getTypeFactory().constructType(javaTypeCanonicalName);
        JavaType resultJavaType = javaTypeDeserializer.deserialize(createJsonParser("{\"value\":\"" + javaTypeCanonicalName + "\"}"), createContext());
        assertEquals(expectedJavaType, resultJavaType);
    }

    @Test
    public void testCurrencyDeserialize() throws IOException {
        String currencyCode = "USD";
        Currency expectedCurrency = Currency.getInstance(currencyCode);
        Currency resultCurrency = currencyDeserializer.deserialize(createJsonParser("{\"value\":\"" + currencyCode + "\"}"), createContext());
        assertEquals(expectedCurrency, resultCurrency);
    }

    @Test
    public void testPatternDeserialize() throws IOException {
        String regex = "[a-zA-Z0-9]+";
        Pattern expectedPattern = Pattern.compile(regex);
        Pattern resultPattern = patternDeserializer.deserialize(createJsonParser("{\"value\":\"" + regex + "\"}"), createContext());
        assertEquals(expectedPattern, resultPattern);
    }

    @Test
    public void testLocaleDeserialize() throws IOException {
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale resultLocale = localeDeserializer.deserialize(createJsonParser("{\"value\":\"" + localeString + "\"}"), createContext());
        assertEquals(expectedLocale, resultLocale);
    }

    @Test
    public void testCharsetDeserialize() throws IOException {
        String charsetName = "UTF-8";
        Charset expectedCharset = Charset.forName(charsetName);
        Charset resultCharset = charsetDeserializer.deserialize(createJsonParser("{\"value\":\"" + charsetName + "\"}"), createContext());
        assertEquals(expectedCharset, resultCharset);
    }

    @Test
    public void testTimeZoneDeserialize() throws IOException {
        String timeZoneId = "America/Los_Angeles";
        TimeZone expectedTimeZone = TimeZone.getTimeZone(timeZoneId);
        TimeZone resultTimeZone = timeZoneDeserializer.deserialize(createJsonParser("{\"value\":\"" + timeZoneId + "\"}"), createContext());
        assertEquals(expectedTimeZone, resultTimeZone);
    }

    @Test
    public void testInetAddressDeserialize() throws IOException {
        String ipAddress = "192.168.1.1";
        InetAddress expectedAddress = InetAddress.getByName(ipAddress);
        InetAddress resultAddress = inetAddressDeserializer.deserialize(createJsonParser("{\"value\":\"" + ipAddress + "\"}"), createContext());
        assertEquals(expectedAddress, resultAddress);
    }

    @Test
    public void testInetSocketAddressDeserialize() throws IOException {
        String ipAddress = "192.168.1.1";
        int port = 8080;
        InetSocketAddress expectedSocketAddress = new InetSocketAddress(InetAddress.getByName(ipAddress), port);
        InetSocketAddress resultSocketAddress = inetSocketAddressDeserializer.deserialize(createJsonParser("{\"value\":\"" + ipAddress + ":" + port + "\"}"), createContext());
        assertEquals(expectedSocketAddress, resultSocketAddress);
    }

    @Test(expected = JsonMappingException.class)
    public void testEmptyStringDeserialize() throws IOException {
        String emptyString = "";
        classDeserializer.deserialize(createJsonParser("{\"value\":\"" + emptyString + "\"}"), createContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testEmptyJsonDeserialize() throws IOException {
        JsonParser jsonParser = createJsonParser("{}");
        fileDeserializer.deserialize(jsonParser, createContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonDeserialize() throws IOException {
        JsonParser jsonParser = createJsonParser("{\"value\":[1, 2, 3]}");
        fileDeserializer.deserialize(jsonParser, createContext());
    }

    private JsonParser createJsonParser(String jsonString) throws IOException {
        return objectMapper.getFactory().createParser(jsonString);
    }

    private DeserializationContext createContext() {
        return objectMapper.getDeserializationContext();
    }
}