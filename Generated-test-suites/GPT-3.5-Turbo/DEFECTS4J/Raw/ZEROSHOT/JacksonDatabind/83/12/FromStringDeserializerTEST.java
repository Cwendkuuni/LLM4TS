package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;

public class FromStringDeserializerTest {
    
    private static class TestDeserializer extends FromStringDeserializer<Object> {
        
        public TestDeserializer(Class<?> vc) {
            super(vc);
        }
        
        protected Object _deserialize(String value, DeserializationContext ctxt) {
            return null;
        }
    }
    
    private FromStringDeserializer<Object> deserializer;
    private JsonParser jsonParser;
    private DeserializationContext context;
    
    @Before
    public void setup() {
        deserializer = new TestDeserializer(Object.class);
        jsonParser = null; // create an instance of JsonParser
        context = null; // create an instance of DeserializationContext
    }
    
    @Test
    public void testDeserialize() throws Exception {
        Object result = deserializer.deserialize(jsonParser, context);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_EmptyString() throws Exception {
        Object result = deserializer._deserializeFromEmptyString();
        assertNull(result);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserialize_UnexpectedToken() throws Exception {
        deserializer.deserialize(jsonParser, context);
    }
    
    @Test
    public void testDeserialize_EmbeddedToken() throws Exception {
        Object result = deserializer.deserialize(jsonParser, context);
        assertNull(result);
    }
    
    @Test
    public void testFindDeserializer() {
        Std result = FromStringDeserializer.findDeserializer(Object.class);
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeFromFile() throws Exception {
        Std fileDeserializer = new Std(File.class, Std.STD_FILE);
        File result = (File) fileDeserializer._deserialize("test.txt", context);
        assertEquals("test.txt", result.getName());
    }
    
    @Test
    public void testDeserializeFromURL() throws Exception {
        Std urlDeserializer = new Std(URL.class, Std.STD_URL);
        URL result = (URL) urlDeserializer._deserialize("http://example.com", context);
        assertEquals("http://example.com", result.toString());
    }
    
    @Test
    public void testDeserializeFromURI() throws Exception {
        Std uriDeserializer = new Std(URI.class, Std.STD_URI);
        URI result = (URI) uriDeserializer._deserialize("http://example.com/path", context);
        assertEquals("http://example.com/path", result.toString());
    }
    
    @Test
    public void testDeserializeFromClass() throws Exception {
        Std classDeserializer = new Std(Class.class, Std.STD_CLASS);
        Class<?> result = (Class<?>) classDeserializer._deserialize("com.example.TestClass", context);
        assertEquals("com.example.TestClass", result.getName());
    }
    
    @Test
    public void testDeserializeFromJavaType() throws Exception {
        Std javaTypeDeserializer = new Std(JavaType.class, Std.STD_JAVA_TYPE);
        JavaType result = (JavaType) javaTypeDeserializer._deserialize("java.util.List<String>", context);
        assertEquals("java.util.List<java.lang.String>", result.toCanonical());
    }
    
    @Test
    public void testDeserializeFromCurrency() throws Exception {
        Std currencyDeserializer = new Std(Currency.class, Std.STD_CURRENCY);
        Currency result = (Currency) currencyDeserializer._deserialize("USD", context);
        assertEquals("USD", result.getCurrencyCode());
    }
    
    @Test
    public void testDeserializeFromPattern() throws Exception {
        Std patternDeserializer = new Std(Pattern.class, Std.STD_PATTERN);
        Pattern result = (Pattern) patternDeserializer._deserialize("\\d+", context);
        assertEquals("\\d+", result.pattern());
    }
    
    @Test
    public void testDeserializeFromLocale() throws Exception {
        Std localeDeserializer = new Std(Locale.class, Std.STD_LOCALE);
        Locale result = (Locale) localeDeserializer._deserialize("en_US", context);
        assertEquals("en_US", result.toString());
    }
    
    @Test
    public void testDeserializeFromCharset() throws Exception {
        Std charsetDeserializer = new Std(Charset.class, Std.STD_CHARSET);
        Charset result = (Charset) charsetDeserializer._deserialize("UTF-8", context);
        assertEquals("UTF-8", result.name());
    }
    
    @Test
    public void testDeserializeFromTimeZone() throws Exception {
        Std timeZoneDeserializer = new Std(TimeZone.class, Std.STD_TIME_ZONE);
        TimeZone result = (TimeZone) timeZoneDeserializer._deserialize("America/Los_Angeles", context);
        assertEquals("America/Los_Angeles", result.getID());
    }
    
    @Test
    public void testDeserializeFromInetAddress() throws Exception {
        Std inetAddressDeserializer = new Std(InetAddress.class, Std.STD_INET_ADDRESS);
        InetAddress result = (InetAddress) inetAddressDeserializer._deserialize("localhost", context);
        assertEquals("localhost", result.getHostName());
    }
    
    @Test(expected = MalformedURLException.class)
    public void testDeserializeFromURL_MalformedURL() throws Exception {
        Std urlDeserializer = new Std(URL.class, Std.STD_URL);
        urlDeserializer._deserialize("htt://example.com", context);
    }
    
    @Test(expected = MalformedURLException.class)
    public void testDeserializeFromURI_MalformedURI() throws Exception {
        Std uriDeserializer = new Std(URI.class, Std.STD_URI);
        uriDeserializer._deserialize("htt://example.com/path", context);
    }
    
    @Test
    public void testDeserializeFromInetSocketAddress_IPv4() throws Exception {
        Std inetSocketAddressDeserializer = new Std(InetSocketAddress.class, Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress result = (InetSocketAddress) inetSocketAddressDeserializer._deserialize("localhost:8080", context);
        assertEquals("localhost", result.getHostName());
        assertEquals(8080, result.getPort());
    }
    
    @Test
    public void testDeserializeFromInetSocketAddress_IPv6() throws Exception {
        Std inetSocketAddressDeserializer = new Std(InetSocketAddress.class, Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress result = (InetSocketAddress) inetSocketAddressDeserializer._deserialize("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:8080", context);
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", result.getHostString());
        assertEquals(8080, result.getPort());
    }
    
    @Test
    public void testDeserializeFromStringBuilder() throws Exception {
        Std stringBuilderDeserializer = new Std(StringBuilder.class, Std.STD_STRING_BUILDER);
        StringBuilder result = (StringBuilder) stringBuilderDeserializer._deserialize("test", context);
        assertEquals("test", result.toString());
    }
}