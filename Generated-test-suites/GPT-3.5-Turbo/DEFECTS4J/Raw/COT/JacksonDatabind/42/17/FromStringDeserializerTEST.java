import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FromStringDeserializerTest {

    @Mock
    private JsonParser jp;

    @Mock
    private DeserializationContext ctxt;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        String filePath = "path/to/file.txt";
        when(jp.getValueAsString()).thenReturn(filePath);

        File result = deserializer.deserialize(jp, ctxt);

        assertEquals(new File(filePath), result);
    }
    
    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        String urlString = "http://example.com";
        when(jp.getValueAsString()).thenReturn(urlString);

        URL result = deserializer.deserialize(jp, ctxt);

        assertEquals(new URL(urlString), result);
    }
    
    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        String uriString = "http://example.com";
        when(jp.getValueAsString()).thenReturn(uriString);

        URI result = deserializer.deserialize(jp, ctxt);

        assertEquals(URI.create(uriString), result);
    }
    
    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        String className = "java.util.List";
        when(jp.getValueAsString()).thenReturn(className);

        Class<?> result = deserializer.deserialize(jp, ctxt);

        assertEquals(java.util.List.class, result);
    }
    
    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        String javaTypeString = "java.util.List";
        when(jp.getValueAsString()).thenReturn(javaTypeString);

        JavaType result = deserializer.deserialize(jp, ctxt);

        assertEquals(ctxt.getTypeFactory().constructFromCanonical(javaTypeString), result);
    }
    
    @Test
    public void testDeserializeCurrencyValid() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        String currencyCode = "USD";
        when(jp.getValueAsString()).thenReturn(currencyCode);

        Currency result = deserializer.deserialize(jp, ctxt);

        assertEquals(Currency.getInstance(currencyCode), result);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserializeCurrencyInvalid() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        String invalidCurrencyCode = "AAA";
        when(jp.getValueAsString()).thenReturn(invalidCurrencyCode);

        deserializer.deserialize(jp, ctxt);
    }
    
    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        String patternString = ".*";
        when(jp.getValueAsString()).thenReturn(patternString);

        Pattern result = deserializer.deserialize(jp, ctxt);

        assertEquals(Pattern.compile(patternString), result);
    }
    
    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        String localeString = "en_US";
        when(jp.getValueAsString()).thenReturn(localeString);

        Locale result = deserializer.deserialize(jp, ctxt);

        assertEquals(new Locale("en", "US"), result);
    }
    
    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        String charsetName = "UTF-8";
        when(jp.getValueAsString()).thenReturn(charsetName);

        Charset result = deserializer.deserialize(jp, ctxt);

        assertEquals(Charset.forName(charsetName), result);
    }
    
    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        String timeZoneId = "GMT+01:00";
        when(jp.getValueAsString()).thenReturn(timeZoneId);

        TimeZone result = deserializer.deserialize(jp, ctxt);

        assertEquals(TimeZone.getTimeZone(timeZoneId), result);
    }
    
    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        String ip = "127.0.0.1";
        when(jp.getValueAsString()).thenReturn(ip);

        InetAddress result = deserializer.deserialize(jp, ctxt);

        assertEquals(InetAddress.getByName(ip), result);
    }
    
    @Test
    public void testDeserializeInetSocketAddressValid() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        String address = "127.0.0.1:8080";
        when(jp.getValueAsString()).thenReturn(address);

        InetSocketAddress result = deserializer.deserialize(jp, ctxt);

        assertEquals(new InetSocketAddress("127.0.0.1", 8080), result);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserializeInetSocketAddressInvalid() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        String address = "[::1]";
        when(jp.getValueAsString()).thenReturn(address);

        deserializer.deserialize(jp, ctxt);
    }
    
    @Test
    public void testDeserializeUnknown() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, -1); // Unknown type
        String value = "unknown";
        when(jp.getValueAsString()).thenReturn(value);

        Object result = deserializer.deserialize(jp, ctxt);

        assertNull(result);
    }
    
}