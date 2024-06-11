import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Currency;
import java.util.TimeZone;
import java.util.regex.Pattern;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.io.File;

public class FromStringDeserializerTest {

    // Test for deserialize method
    @Test
    public void testDeserialize() throws IOException {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
            @Override
            protected String _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return value.toUpperCase();
            }
        };

        JsonParser parser = null;
        DeserializationContext context = null;

        // Typical Use Case
        assertEquals("HELLO", deserializer.deserialize(parser, context));
    }

    // Test for _deserialize method
    @Test
    public void test_deserialize() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(String.class, 0) {

            @Override
            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
                if (value.isEmpty()) {
                    throw new IllegalArgumentException("Value cannot be empty");
                }
                return value.toUpperCase();
            }
        };

        DeserializationContext context = null;

        // Typical Use Case
        assertEquals("HELLO", stdDeserializer._deserialize("hello", context));

        // Error Scenario
        try {
            stdDeserializer._deserialize("", context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Value cannot be empty", e.getMessage());
        }
    }

    // Test for _deserializeFromEmptyString method
    @Test
    public void test_deserializeFromEmptyString() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(String.class, 0) {

            @Override
            protected Object _deserializeFromEmptyString() throws IOException {
                return "EMPTY";
            }
        };

        // Typical Use Case
        assertEquals("EMPTY", stdDeserializer._deserializeFromEmptyString());
    }

    // Test for STD_FILE kind
    @Test
    public void test_STD_FILE() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(File.class, 1);

        // Typical Use Case
        assertEquals(File.class, stdDeserializer._valueClass);
        assertEquals(new File("path"), stdDeserializer._deserialize("path", null));
    }

    // Test for STD_URL kind
    @Test
    public void test_STD_URL() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(URL.class, 2);

        // Typical Use Case
        assertEquals(URL.class, stdDeserializer._valueClass);
        assertEquals(new URL("http://www.example.com"), stdDeserializer._deserialize("http://www.example.com", null));
    }

    // Test for STD_URI kind
    @Test
    public void test_STD_URI() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(URI.class, 3);

        // Typical Use Case
        assertEquals(URI.class, stdDeserializer._valueClass);
        assertEquals(URI.create("http://www.example.com"), stdDeserializer._deserialize("http://www.example.com", null));
    }

    // Test for STD_URL kind
    @Test
    public void test_STD_CLASS() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Class.class, 4);

        DeserializationContext context = null;

        // Typical Use Case
        assertEquals(Class.class, stdDeserializer._valueClass);
        assertEquals(String.class, stdDeserializer._deserialize("java.lang.String", context));
    }

    // Test for STD_JAVA_TYPE kind
    @Test
    public void test_STD_JAVA_TYPE() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Class.class, 5);

        DeserializationContext context = null;

        // Typical Use Case
        assertEquals(Class.class, stdDeserializer._valueClass);
        assertEquals(String.class, stdDeserializer._deserialize("java.lang.String", context));
    }

    // Test for STD_CURRENCY kind
    @Test
    public void test_STD_CURRENCY() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Currency.class, 6);

        // Typical Use Case
        assertEquals(Currency.class, stdDeserializer._valueClass);
        assertEquals(Currency.getInstance("USD"), stdDeserializer._deserialize("USD", null));
    }

    // Test for STD_PATTERN kind
    @Test
    public void test_STD_PATTERN() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Pattern.class, 7);

        // Typical Use Case
        assertEquals(Pattern.class, stdDeserializer._valueClass);
        assertEquals(Pattern.compile("[a-z]+"), stdDeserializer._deserialize("[a-z]+", null));
    }

    // Test for STD_LOCALE kind
    @Test
    public void test_STD_LOCALE() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Locale.class, 8);

        // Typical Use Case
        assertEquals(Locale.class, stdDeserializer._valueClass);
        assertEquals(new Locale("en"), stdDeserializer._deserialize("en", null));
    }

    // Test for STD_CHARSET kind
    @Test
    public void test_STD_CHARSET() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Charset.class, 9);

        // Typical Use Case
        assertEquals(Charset.class, stdDeserializer._valueClass);
        assertEquals(Charset.forName("UTF-8"), stdDeserializer._deserialize("UTF-8", null));
    }

    // Test for STD_TIME_ZONE kind
    @Test
    public void test_STD_TIME_ZONE() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(TimeZone.class, 10);

        // Typical Use Case
        assertEquals(TimeZone.class, stdDeserializer._valueClass);
        assertEquals(TimeZone.getTimeZone("GMT"), stdDeserializer._deserialize("GMT", null));
    }

    // Test for STD_INET_ADDRESS kind
    @Test
    public void test_STD_INET_ADDRESS() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(InetAddress.class, 11);

        // Typical Use Case
        assertEquals(InetAddress.class, stdDeserializer._valueClass);
        assertEquals(InetAddress.getByName("localhost"), stdDeserializer._deserialize("localhost", null));
    }

    // Test for STD_INET_SOCKET_ADDRESS kind
    @Test
    public void test_STD_INET_SOCKET_ADDRESS() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);

        // Typical Use Case
        assertEquals(InetSocketAddress.class, stdDeserializer._valueClass);
        assertEquals(new InetSocketAddress("localhost", 8080), stdDeserializer._deserialize("localhost:8080", null));
    }
    
    // Test for STD_STRING_BUILDER kind
    @Test
    public void test_STD_STRING_BUILDER() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(StringBuilder.class, 13);

        // Typical Use Case
        assertEquals(StringBuilder.class, stdDeserializer._valueClass);
        assertEquals(new StringBuilder("Hello"), stdDeserializer._deserialize("Hello", null));
    }
}