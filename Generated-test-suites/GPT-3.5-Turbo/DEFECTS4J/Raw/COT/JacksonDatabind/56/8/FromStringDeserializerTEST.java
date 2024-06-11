
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.*;
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
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_file() throws IOException {
        String input = "/path/to/file.txt";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File expected = new File(input);
        File result = (File) deserializer._deserialize(input, null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_URL() throws IOException {
        String input = "http://www.example.com";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL expected = new URL(input);
        URL result = (URL) deserializer._deserialize(input, null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_URI() throws IOException {
        String input = "http://www.example.com";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI expected = URI.create(input);
        URI result = (URI) deserializer._deserialize(input, null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_Class() throws IOException {
        String input = "java.lang.String";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> expected = String.class;
        Class<?> result = (Class<?>) deserializer._deserialize(input, null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_JavaType() throws IOException {
        String input = "java.util.List<java.lang.String>";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType expected = ClassUtil.fromString(input);
        JavaType result = (JavaType) deserializer._deserialize(input, null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_Currency() throws IOException {
        String input = "USD";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency expected = Currency.getInstance(input);
        Currency result = (Currency) deserializer._deserialize(input, null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_Pattern() throws IOException {
        String input = "\\d+";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern expected = Pattern.compile(input);
        Pattern result = (Pattern) deserializer._deserialize(input, null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_Locale() throws IOException {
        String input = "en_US";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale expected = new Locale(input);
        Locale result = (Locale) deserializer._deserialize(input, null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_Charset() throws IOException {
        String input = "UTF-8";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset expected = Charset.forName(input);
        Charset result = (Charset) deserializer._deserialize(input, null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_TimeZone() throws IOException {
        String input = "America/Los_Angeles";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone expected = TimeZone.getTimeZone(input);
        TimeZone result = (TimeZone) deserializer._deserialize(input, null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_InetAddress() throws IOException {
        String input = "127.0.0.1";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress expected = InetAddress.getByName(input);
        InetAddress result = (InetAddress) deserializer._deserialize(input, null);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_InetSocketAddress() throws IOException {
        String input = "localhost:8080";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        InetSocketAddress result = (InetSocketAddress) deserializer._deserialize(input, null);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_invalidType() throws IOException {
        String input = "invalid";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, -1);
        deserializer._deserialize(input, null);
    }

    @Test
    public void testDeserialize_emptyString_URI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI expected = URI.create("");
        URI result = (URI) deserializer._deserializeFromEmptyString();
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_emptyString_Locale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale expected = Locale.ROOT;
        Locale result = (Locale) deserializer._deserializeFromEmptyString();
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_unknownType() throws IOException {
        String input = "unknown";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, -1);
        try {
            deserializer._deserialize(input, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }
}

