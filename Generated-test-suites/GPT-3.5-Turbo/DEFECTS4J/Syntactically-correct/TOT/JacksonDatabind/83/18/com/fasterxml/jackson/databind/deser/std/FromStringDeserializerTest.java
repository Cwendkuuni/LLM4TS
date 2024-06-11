import static org.junit.Assert.*;
import java.io.*;
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
import org.junit.Test;

public class FromStringDeserializerTest {
    
    @Test
    public void testDeserialize_File() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = new File("test.txt");
        
        try {
            File result = (File) deserializer.deserialize(p, ctxt);
            assertEquals(file.getAbsolutePath(), result.getAbsolutePath());
        }catch(Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDeserialize_URL() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = new URL("https://www.example.com");
        
        try {
            URL result = (URL) deserializer.deserialize(p, ctxt);
            assertEquals(url.toString(), result.toString());
        }catch(Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDeserialize_URI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = URI.create("https://www.example.com");
        
        try {
            URI result = (URI) deserializer.deserialize(p, ctxt);
            assertEquals(uri.toString(), result.toString());
        }catch(Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDeserialize_Class() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> clazz = String.class;
        
        try {
            Class<?> result = (Class<?>) deserializer.deserialize(p, ctxt);
            assertEquals(clazz, result);
        }catch(Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDeserialize_JavaType() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType javaType = JavaType.fromClass(String.class);
        
        try {
            JavaType result = (JavaType) deserializer.deserialize(p, ctxt);
            assertEquals(javaType, result);
        }catch(Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDeserialize_Currency() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency currency = Currency.getInstance("USD");
        
        try {
            Currency result = (Currency) deserializer.deserialize(p, ctxt);
            assertEquals(currency, result);
        }catch(Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDeserialize_Pattern() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern pattern = Pattern.compile("[a-z]+");
        
        try {
            Pattern result = (Pattern) deserializer.deserialize(p, ctxt);
            assertEquals(pattern, result);
        }catch(Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDeserialize_Locale() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = new Locale("en", "US");
        
        try {
            Locale result = (Locale) deserializer.deserialize(p, ctxt);
            assertEquals(locale.toString(), result.toString());
        }catch(Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDeserialize_Charset() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset charset = Charset.forName("UTF-8");
        
        try {
            Charset result = (Charset) deserializer.deserialize(p, ctxt);
            assertEquals(charset, result);
        }catch(Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDeserialize_TimeZone() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = TimeZone.getTimeZone("GMT+0");
        
        try {
            TimeZone result = (TimeZone) deserializer.deserialize(p, ctxt);
            assertEquals(timeZone, result);
        }catch(Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDeserialize_InetAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress inetAddress = InetAddress.getByName("localhost");
        
        try {
            InetAddress result = (InetAddress) deserializer.deserialize(p, ctxt);
            assertEquals(inetAddress, result);
        }catch(Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDeserialize_InetSocketAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
        
        try {
            InetSocketAddress result = (InetSocketAddress) deserializer.deserialize(p, ctxt);
            assertEquals(inetSocketAddress.toString(), result.toString());
        }catch(Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDeserialize_StringBuilder() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        StringBuilder builder = new StringBuilder("testing");
        
        try {
            StringBuilder result = (StringBuilder) deserializer.deserialize(p, ctxt);
            assertEquals(builder.toString(), result.toString());
        }catch(Exception e) {
            fail("Exception thrown");
        }
    }
}