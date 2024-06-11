import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        File expected = new File("test.txt");
        File actual = new FromStringDeserializer.Std(Std.STD_FILE).deserialize(new JsonParser(), new DeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeURL() throws IOException {
        URL expected = new URL("http://www.example.com");
        URL actual = new FromStringDeserializer.Std(Std.STD_URL).deserialize(new JsonParser(), new DeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeURI() throws IOException {
        URI expected = URI.create("http://www.example.com");
        URI actual = new FromStringDeserializer.Std(Std.STD_URI).deserialize(new JsonParser(), new DeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        Class<?> expected = getClass();
        Class<?> actual = new FromStringDeserializer.Std(Std.STD_CLASS).deserialize(new JsonParser(), new DeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        JavaType expected = new JavaType();
        JavaType actual = new FromStringDeserializer.Std(Std.STD_JAVA_TYPE).deserialize(new JsonParser(), new DeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        Currency expected = Currency.getInstance("USD");
        Currency actual = new FromStringDeserializer.Std(Std.STD_CURRENCY).deserialize(new JsonParser(), new DeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        Pattern expected = Pattern.compile("\\d+");
        Pattern actual = new FromStringDeserializer.Std(Std.STD_PATTERN).deserialize(new JsonParser(), new DeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        Locale expected = new Locale("en", "US");
        Locale actual = new FromStringDeserializer.Std(Std.STD_LOCALE).deserialize(new JsonParser(), new DeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        Charset expected = Charset.forName("UTF-8");
        Charset actual = new FromStringDeserializer.Std(Std.STD_CHARSET).deserialize(new JsonParser(), new DeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        TimeZone expected = TimeZone.getTimeZone("PST");
        TimeZone actual = new FromStringDeserializer.Std(Std.STD_TIME_ZONE).deserialize(new JsonParser(), new DeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        InetAddress expected = InetAddress.getByName("localhost");
        InetAddress actual = new FromStringDeserializer.Std(Std.STD_INET_ADDRESS).deserialize(new JsonParser(), new DeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        InetSocketAddress actual = new FromStringDeserializer.Std(Std.STD_INET_SOCKET_ADDRESS).deserialize(new JsonParser(), new DeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeStringBuilder() throws IOException {
        StringBuilder expected = new StringBuilder("test");
        StringBuilder actual = new FromStringDeserializer.Std(Std.STD_STRING_BUILDER).deserialize(new JsonParser(), new DeserializationContext());
        assertEquals(expected, actual);
    }

}