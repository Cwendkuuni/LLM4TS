import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;


public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_File_success() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File expected = new File("test.txt");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserialize_File_error() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File expected = new File("test.txt");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertFalse(expected.equals(stdDeserializer.deserialize(jp, ctxt)));
    }

    @Test
    public void testDeserialize_URL_success() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL expected = new URL("https://www.example.com");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserialize_URI_success() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI expected = new URI("https://www.example.com");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserialize_Class_success() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class expected = String.class;
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserialize_JavaType_success() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType expected = new JavaType(String.class);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_JavaType_error() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType expected = new JavaType(null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserialize_Currency_success() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency expected = Currency.getInstance("USD");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_Currency_error() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency expected = Currency.getInstance("ABC");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserialize_Pattern_success() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern expected = Pattern.compile(".+");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_Pattern_error() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern expected = Pattern.compile("[a-z+");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserialize_Locale_success() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale expected = new Locale("en_US");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserialize_Charset_success() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset expected = Charset.forName("UTF-8");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserialize_TimeZone_success() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone expected = TimeZone.getTimeZone("PST");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserialize_InetAddress_success() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress expected = InetAddress.getLocalHost();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }

    @Test(expected = java.net.UnknownHostException.class)
    public void testDeserialize_InetAddress_error() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress expected = InetAddress.getByName("invalidhost");
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserialize_InetSocketAddress_success() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_InetSocketAddress_error() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress expected = new InetSocketAddress("invalidhost", 8080);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(expected, stdDeserializer.deserialize(jp, ctxt));
    }
}