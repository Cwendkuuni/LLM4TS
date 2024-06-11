package com.example.junit; // replace with your test package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @InjectMocks
    private FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
    @InjectMocks
    private FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
    @InjectMocks
    private FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
    @InjectMocks
    private FromStringDeserializer<Class<?>> classDeserializer = new FromStringDeserializer<Class<?>>(Class.class);
    @InjectMocks
    private FromStringDeserializer<JavaType> javaTypeDeserializer = new FromStringDeserializer<JavaType>(JavaType.class);
    @InjectMocks
    private FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
    @InjectMocks
    private FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer<Pattern>(Pattern.class);
    @InjectMocks
    private FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
    @InjectMocks
    private FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
    @InjectMocks
    private FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
    @InjectMocks
    private FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
    @InjectMocks
    private FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private DeserializationContext deserializationContext;

    @Test
    public void testDeserializeFile() throws IOException {
        String fileString = "path/to/file.txt";
        File expectedFile = new File("path/to/file.txt");
        File resultFile = fileDeserializer.deserialize(mockParser(), deserializationContext);
        assertEquals(expectedFile, resultFile);
    }

    @Test
    public void testDeserializeURL() throws IOException {
        String urlString = "http://example.com";
        URL expectedUrl = new URL("http://example.com");
        URL resultUrl = urlDeserializer.deserialize(mockParser(), deserializationContext);
        assertEquals(expectedUrl, resultUrl);
    }

    @Test
    public void testDeserializeURI() throws IOException {
        String uriString = "http://example.com/path/to";
        URI expectedUri = new URI("http://example.com/path/to");
        URI resultUri = uriDeserializer.deserialize(mockParser(), deserializationContext);
        assertEquals(expectedUri, resultUri);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        String className = "java.lang.String";
        Class<?> expectedClass = String.class;
        Class<?> resultClass = classDeserializer.deserialize(mockParser(), deserializationContext);
        assertEquals(expectedClass, resultClass);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        String typeName = "java.util.List";
        JavaType expectedJavaType = new TypeFactory().constructType(java.util.List.class);
        JavaType resultJavaType = javaTypeDeserializer.deserialize(mockParser(), deserializationContext);
        assertEquals(expectedJavaType, resultJavaType);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        String currencyString = "USD";
        Currency expectedCurrency = Currency.getInstance("USD");
        Currency resultCurrency = currencyDeserializer.deserialize(mockParser(), deserializationContext);
        assertEquals(expectedCurrency, resultCurrency);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        String patternString = "\\d+";
        Pattern expectedPattern = Pattern.compile("\\d+");
        Pattern resultPattern = patternDeserializer.deserialize(mockParser(), deserializationContext);
        assertEquals(expectedPattern, resultPattern);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale resultLocale = localeDeserializer.deserialize(mockParser(), deserializationContext);
        assertEquals(expectedLocale, resultLocale);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        String charsetString = "UTF-8";
        Charset expectedCharset = Charset.forName("UTF-8");
        Charset resultCharset = charsetDeserializer.deserialize(mockParser(), deserializationContext);
        assertEquals(expectedCharset, resultCharset);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        String timeZoneString = "America/Los_Angeles";
        TimeZone expectedTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
        TimeZone resultTimeZone = timeZoneDeserializer.deserialize(mockParser(), deserializationContext);
        assertEquals(expectedTimeZone, resultTimeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        String inetAddressString = "127.0.0.1";
        InetAddress expectedInetAddress = InetAddress.getByName("127.0.0.1");
        InetAddress resultInetAddress = inetAddressDeserializer.deserialize(mockParser(), deserializationContext);
        assertEquals(expectedInetAddress, resultInetAddress);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        String inetSocketAddressString = "[255.255.255.255]:80";
        InetSocketAddress expectedInetSocketAddress = new InetSocketAddress("255.255.255.255", 80);
        InetSocketAddress resultInetSocketAddress = inetSocketAddressDeserializer.deserialize(mockParser(), deserializationContext);
        assertEquals(expectedInetSocketAddress, resultInetSocketAddress);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        String emptyString = "";
        Object result = fileDeserializer.deserializeFromEmptyString();
        assertNull(result);
    }

    @Test
    public void testDeserializeNull() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        Mockito.when(parser.getValueAsString()).thenReturn(null);
        Object result = fileDeserializer.deserialize(parser, deserializationContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeUnexpectedToken() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        Mockito.when(parser.getValueAsString()).thenReturn("42");
        assertThrows(JsonMappingException.class, () -> fileDeserializer.deserialize(parser, deserializationContext));
    }

    @Test
    public void testDeserializeUnexpectedType() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        Object result = fileDeserializer.deserialize(parser, deserializationContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeInvalidFormat() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        Mockito.when(parser.getValueAsString()).thenReturn("InvalidFormat");
        assertThrows(InvalidFormatException.class, () -> fileDeserializer.deserialize(parser, deserializationContext));
    }

    @Test
    public void testDeserializeIllegalArgument() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        Mockito.when(parser.getValueAsString()).thenReturn("NotAValidType");
        assertThrows(IllegalArgumentException.class, () -> fileDeserializer.deserialize(parser, deserializationContext));
    }

    @Before
    public void setup() throws IOException {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        deserializationContext = objectMapper.getDeserializationContext();
    }

    private JsonParser mockParser() throws IOException {
        return Mockito.mock(JsonParser.class);
    }

}