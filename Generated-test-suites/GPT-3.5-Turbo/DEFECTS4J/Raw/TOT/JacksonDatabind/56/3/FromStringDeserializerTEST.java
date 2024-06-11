import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URL;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FromStringDeserializerTest {
    
    @Test
    public void testStdFileDeserialization() throws Exception {
        String filePath = "test.txt";
        
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File expectedFile = new File(filePath);
        File actualFile = (File) deserializer._deserialize(filePath, null);
        
        Assert.assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
    }
    
    @Test
    public void testStdURLDeserialization() throws Exception {
        String urlString = "http://www.example.com";
        
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL expectedURL = new URL(urlString);
        URL actualURL = (URL) deserializer._deserialize(urlString, null);
        
        Assert.assertEquals(expectedURL.toString(), actualURL.toString());
    }
    
    @Test
    public void testStdURIDeserialization() throws Exception {
        String uriString = "http://www.example.com";
        
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI expectedURI = new URI(uriString);
        URI actualURI = (URI) deserializer._deserialize(uriString, null);
        
        Assert.assertEquals(expectedURI.toString(), actualURI.toString());
    }
    
    @Test
    public void testStdClassDeserialization() throws Exception {
        String className = "java.lang.String";
        
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> expectedClass = String.class;
        Class<?> actualClass = (Class<?>) deserializer._deserialize(className, null);
        
        Assert.assertEquals(expectedClass, actualClass);
    }
    
    @Test
    public void testStdCurrencyDeserialization() throws Exception {
        String currencyCode = "USD";
        
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency expectedCurrency = Currency.getInstance(currencyCode);
        Currency actualCurrency = (Currency) deserializer._deserialize(currencyCode, null);
        
        Assert.assertEquals(expectedCurrency, actualCurrency);
    }
    
    @Test
    public void testStdPatternDeserialization() throws Exception {
        String regex = ".*";
        
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern expectedPattern = Pattern.compile(regex);
        Pattern actualPattern = (Pattern) deserializer._deserialize(regex, null);
        
        Assert.assertEquals(expectedPattern.pattern(), actualPattern.pattern());
    }
    
    @Test
    public void testStdLocaleDeserialization() throws Exception {
        String localeString = "en_US";
        
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = (Locale) deserializer._deserialize(localeString, null);
        
        Assert.assertEquals(expectedLocale.toString(), actualLocale.toString());
    }
    
    @Test
    public void testStdTimeZoneDeserialization() throws Exception {
        String timeZoneID = "America/New_York";
        
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone expectedTimeZone = TimeZone.getTimeZone(timeZoneID);
        TimeZone actualTimeZone = (TimeZone) deserializer._deserialize(timeZoneID, null);
        
        Assert.assertEquals(expectedTimeZone.getID(), actualTimeZone.getID());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStdDeserialization() throws Exception {
        String invalidString = "invalid";
        
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 100);
        deserializer._deserialize(invalidString, null);
    }
    
    @Test(expected = InvalidFormatException.class)
    public void testInvalidStdInetSocketAddressDeserialization() throws Exception {
        String invalidSocketAddress = "invalid";
        
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        deserializer._deserialize(invalidSocketAddress, null);
    }
    
    @Test
    public void testStdInetSocketAddressDeserializationWithPort() throws Exception {
        String inetSocketAddress = "10.0.0.1:8080";
        
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress expectedInetSocketAddress = new InetSocketAddress("10.0.0.1", 8080);
        InetSocketAddress actualInetSocketAddress = (InetSocketAddress) deserializer._deserialize(inetSocketAddress, null);
        
        Assert.assertEquals(expectedInetSocketAddress.getAddress(), actualInetSocketAddress.getAddress());
        Assert.assertEquals(expectedInetSocketAddress.getPort(), actualInetSocketAddress.getPort());
    }
    
    @Test
    public void testStdInetSocketAddressDeserializationWithoutPort() throws Exception {
        String inetSocketAddress = "10.0.0.1";
        
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress expectedInetSocketAddress = new InetSocketAddress("10.0.0.1", 0);
        InetSocketAddress actualInetSocketAddress = (InetSocketAddress) deserializer._deserialize(inetSocketAddress, null);
        
        Assert.assertEquals(expectedInetSocketAddress.getAddress(), actualInetSocketAddress.getAddress());
        Assert.assertEquals(expectedInetSocketAddress.getPort(), actualInetSocketAddress.getPort());
    }
    
}