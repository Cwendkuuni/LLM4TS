
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
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

public class FromStringDeserializerTest {
  
    @Test
    public void testFileDeserialize() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertEquals(new File("test.txt"), deserializer.deserialize(null, null, "test.txt"));
    }
    
    @Test
    public void testURLDeserialize() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        assertEquals(new URL("http://www.example.com"), deserializer.deserialize(null, null, "http://www.example.com"));
    }
    
    @Test
    public void testURIDeserialize() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(URI.create("http://www.example.com"), deserializer.deserialize(null, null, "http://www.example.com"));
    }
    
    @Test
    public void testClassDeserialize() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        assertEquals(String.class, deserializer.deserialize(null, null, "java.lang.String"));
    }

    @Test
    public void testJavaTypeDeserialize() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        assertEquals(JavaType.class, deserializer.deserialize(null, null, "com.fasterxml.jackson.databind.JavaType"));
    }
    
    @Test
    public void testCurrencyDeserialize() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        assertEquals(Currency.getInstance("USD"), deserializer.deserialize(null, null, "USD"));
    }
    
    @Test
    public void testPatternDeserialize() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        assertEquals(Pattern.compile("\\d+"), deserializer.deserialize(null, null, "\\d+"));
    }
    
    @Test
    public void testLocaleDeserialize() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertEquals(new Locale("en", "US"), deserializer.deserialize(null, null, "en_US"));
    }
    
    @Test
    public void testCharsetDeserialize() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        assertEquals(Charset.forName("UTF-8"), deserializer.deserialize(null, null, "UTF-8"));
    }
    
    @Test
    public void testTimeZoneDeserialize() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        assertEquals(TimeZone.getTimeZone("PST"), deserializer.deserialize(null, null, "PST"));
    }
    
    @Test
    public void testInetAddressDeserialize() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        assertEquals(InetAddress.getByName("www.google.com"), deserializer.deserialize(null, null, "www.google.com"));
    }
    
    @Test
    public void testInetSocketAddressDeserialize() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        assertEquals(new InetSocketAddress("www.google.com", 80), deserializer.deserialize(null, null, "www.google.com:80"));
    }
}

