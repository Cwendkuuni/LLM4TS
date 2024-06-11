package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
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
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {
    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        
        File file = (File) fileDeserializer._deserialize("test.txt", null);
        assertEquals("test.txt", file.getName());
        
        assertNull(fileDeserializer._deserialize(null, null));
    }
    
    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        
        URL url = (URL) urlDeserializer._deserialize("https://www.example.com", null);
        assertEquals("https", url.getProtocol());
        assertEquals("www.example.com", url.getHost());
        
        assertNull(urlDeserializer._deserialize(null, null));
    }
    
    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        
        URI uri = (URI) uriDeserializer._deserialize("http://www.example.com", null);
        assertEquals("http", uri.getScheme());
        assertEquals("www.example.com", uri.getHost());
        
        assertNull(uriDeserializer._deserialize(null, null));
    }
    
    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        
        Class<?> clazz = (Class<?>) classDeserializer._deserialize("java.lang.String", null);
        assertEquals(String.class, clazz);
        
        assertNull(classDeserializer._deserialize(null, null));
    }
    
    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer.Std javaTypeDeserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        
        // Test implementation is omitted since JavaType is a complex class and requires a lot of setup
        
        assertNull(javaTypeDeserializer._deserialize(null, null));
    }
    
    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        
        Currency currency = (Currency) currencyDeserializer._deserialize("USD", null);
        assertEquals("USD", currency.getCurrencyCode());
        
        assertNull(currencyDeserializer._deserialize(null, null));
    }
    
    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        
        Pattern pattern = (Pattern) patternDeserializer._deserialize("\\d+", null);
        assertEquals("\\d+", pattern.pattern());
        
        assertNull(patternDeserializer._deserialize(null, null));
    }
    
    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        
        Locale locale = (Locale) localeDeserializer._deserialize("en", null);
        assertEquals("en", locale.getLanguage());
        
        locale = (Locale) localeDeserializer._deserialize("en_US", null);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        
        assertNull(localeDeserializer._deserialize(null, null));
    }
    
    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer.Std charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        
        Charset charset = (Charset) charsetDeserializer._deserialize("UTF-8", null);
        assertEquals("UTF-8", charset.name());
        
        assertNull(charsetDeserializer._deserialize(null, null));
    }
    
    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        
        TimeZone timeZone = (TimeZone) timeZoneDeserializer._deserialize("America/Los_Angeles", null);
        assertEquals("America/Los_Angeles", timeZone.getID());
        
        assertNull(timeZoneDeserializer._deserialize(null, null));
    }
    
    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer.Std inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        
        InetAddress inetAddress = (InetAddress) inetAddressDeserializer._deserialize("127.0.0.1", null);
        assertEquals("127.0.0.1", inetAddress.getHostAddress());
        
        assertNull(inetAddressDeserializer._deserialize(null, null));
    }
    
    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        
        InetSocketAddress inetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer._deserialize("localhost:8080", null);
        assertEquals("localhost", inetSocketAddress.getHostName());
        assertEquals(8080, inetSocketAddress.getPort());
        
        inetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer._deserialize("[::1]:8080", null);
        assertEquals("::1", inetSocketAddress.getHostName());
        assertEquals(8080, inetSocketAddress.getPort());
        
        assertNull(inetSocketAddressDeserializer._deserialize(null, null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeUnknownType() throws IOException {
        FromStringDeserializer.Std unknownTypeDeserializer = new FromStringDeserializer.Std(String.class, -1);
        
        unknownTypeDeserializer._deserialize("test", null);
    }
}