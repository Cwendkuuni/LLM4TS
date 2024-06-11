import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
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
    
    //Test File.class
    @Test
    public void testDeserializeFile() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            File file = (File) deserializer.deserialize(JsonParser.NULL, context);
            assertNull(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Test URL.class
    @Test
    public void testDeserializeURL() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, 2);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            URL url = (URL) deserializer.deserialize(JsonParser.NULL, context);
            assertNull(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Test URI.class
    @Test
    public void testDeserializeURI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            URI uri = (URI) deserializer.deserialize(JsonParser.NULL, context);
            assertNull(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Test Class.class
    @Test
    public void testDeserializeClass() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 4);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            Class clazz = (Class) deserializer.deserialize(JsonParser.NULL, context);
            assertNull(clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Test JavaType.class
    @Test
    public void testDeserializeJavaType() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, 5);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            JavaType javaType = (JavaType) deserializer.deserialize(JsonParser.NULL, context);
            assertNull(javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Test Currency.class
    @Test
    public void testDeserializeCurrency() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            Currency currency = (Currency) deserializer.deserialize(JsonParser.NULL, context);
            assertNull(currency);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Test Pattern.class
    @Test
    public void testDeserializePattern() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            Pattern pattern = (Pattern) deserializer.deserialize(JsonParser.NULL, context);
            assertNull(pattern);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Test Locale.class
    @Test
    public void testDeserializeLocale() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            Locale locale = (Locale) deserializer.deserialize(JsonParser.NULL, context);
            assertNull(locale);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Test Charset.class
    @Test
    public void testDeserializeCharset() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, 9);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            Charset charset = (Charset) deserializer.deserialize(JsonParser.NULL, context);
            assertNull(charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Test TimeZone.class
    @Test
    public void testDeserializeTimeZone() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 10);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            TimeZone timeZone = (TimeZone) deserializer.deserialize(JsonParser.NULL, context);
            assertNull(timeZone);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Test InetAddress.class
    @Test
    public void testDeserializeInetAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, 11);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            InetAddress inetAddress = (InetAddress) deserializer.deserialize(JsonParser.NULL, context);
            assertNull(inetAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Test InetSocketAddress.class
    @Test
    public void testDeserializeInetSocketAddress() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            InetSocketAddress socketAddress = (InetSocketAddress) deserializer.deserialize(JsonParser.NULL, context);
            assertNull(socketAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Test for empty string
    @Test
    public void testDeserializeEmptyString() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            URI uri = (URI) deserializer.deserialize(JsonParser.NULL, context);
            assertEquals("", uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Test for weird String
    @Test
    public void testDeserializeWeirdString() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            File file = (File) deserializer.deserialize(JsonParser.NULL, context);
            assertNull(file);
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("not a valid textual representation"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}