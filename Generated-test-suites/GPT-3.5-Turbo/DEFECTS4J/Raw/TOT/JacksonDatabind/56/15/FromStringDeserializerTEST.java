import static org.junit.Assert.*;

import java.io.File;
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

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_File_Successfully() {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        String filePath = "C:/test.txt";
        
        File file = (File) fileDeserializer.deserialize(filePath, null);
        
        assertNotNull(file);
        assertEquals(filePath, file.getPath());
    }
    
    @Test
    public void testDeserialize_URL_Successfully() {
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        String urlString = "http://www.test.com";
        
        URL url = (URL) urlDeserializer.deserialize(urlString, null);
        
        assertNotNull(url);
        assertEquals(urlString, url.toString());
    }
    
    @Test
    public void testDeserialize_URI_Successfully() {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        String uriString = "http://www.test.com";
        
        URI uri = (URI) uriDeserializer.deserialize(uriString, null);
        
        assertNotNull(uri);
        assertEquals(uriString, uri.toString());
    }
    
    @Test
    public void testDeserialize_Class_Successfully() {
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        String className = "java.lang.String";
        
        Class<?> clazz = (Class<?>) classDeserializer.deserialize(className, null);
        
        assertNotNull(clazz);
        assertEquals(className, clazz.getName());
    }
    
    @Test
    public void testDeserialize_Currency_Successfully() {
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        String currencyCode = "USD";
        
        Currency currency = (Currency) currencyDeserializer.deserialize(currencyCode, null);
        
        assertNotNull(currency);
        assertEquals(currencyCode, currency.getCurrencyCode());
    }
    
    @Test
    public void testDeserialize_Pattern_Successfully() {
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        String patternString = "\\d+";
        
        Pattern pattern = (Pattern) patternDeserializer.deserialize(patternString, null);
        
        assertNotNull(pattern);
        assertEquals(patternString, pattern.pattern());
    }
    
    @Test
    public void testDeserialize_Locale_Successfully() {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        String localeString = "en_US";
        
        Locale locale = (Locale) localeDeserializer.deserialize(localeString, null);
        
        assertNotNull(locale);
        assertEquals(localeString, locale.toString());
    }
    
    @Test
    public void testDeserialize_Charset_Successfully() {
        FromStringDeserializer.Std charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        String charsetName = "UTF-8";
        
        Charset charset = (Charset) charsetDeserializer.deserialize(charsetName, null);
        
        assertNotNull(charset);
        assertEquals(charsetName, charset.name());
    }
    
    @Test
    public void testDeserialize_TimeZone_Successfully() {
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        String timeZoneId = "Europe/London";
        
        TimeZone timeZone = (TimeZone) timeZoneDeserializer.deserialize(timeZoneId, null);
        
        assertNotNull(timeZone);
        assertEquals(timeZoneId, timeZone.getID());
    }
    
    @Test
    public void testDeserialize_InetAddress_Successfully() {
        FromStringDeserializer.Std inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        String inetAddress = "127.0.0.1";
        
        InetAddress address = (InetAddress) inetAddressDeserializer.deserialize(inetAddress, null);
        
        assertNotNull(address);
        assertEquals(inetAddress, address.getHostAddress());
    }
    
    @Test
    public void testDeserialize_InetSocketAddress_Successfully() {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        String inetSocketAddress = "127.0.0.1:8080";
        String expectedHost = "127.0.0.1";
        int expectedPort = 8080;
        
        InetSocketAddress socketAddress = (InetSocketAddress) inetSocketAddressDeserializer.deserialize(inetSocketAddress, null);
        
        assertNotNull(socketAddress);
        assertEquals(expectedHost, socketAddress.getHostString());
        assertEquals(expectedPort, socketAddress.getPort());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_InvalidType_ThrowsException() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        
        deserializer.deserialize("test", null);
    }

}