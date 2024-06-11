import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.Test;
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
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertNull(deserializer.deserialize(null, null));
        assertEquals(new File("test.txt"), deserializer.deserialize(mockParser("test.txt"), mockContext()));
    }

    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        assertNull(deserializer.deserialize(null, null));
        assertEquals(new URL("http://www.example.com"), deserializer.deserialize(mockParser("http://www.example.com"), mockContext()));
    }

    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertNull(deserializer.deserialize(null, null));
        assertEquals(URI.create("http://www.example.com"), deserializer.deserialize(mockParser("http://www.example.com"), mockContext()));
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        assertNull(deserializer.deserialize(null, null));
        assertEquals(String.class, deserializer.deserialize(mockParser("java.lang.String"), mockContext()));
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        assertNull(deserializer.deserialize(null, null));
        assertEquals(JavaType.class, deserializer.deserialize(mockParser("java.util.List"), mockContext()));
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        assertNull(deserializer.deserialize(null, null));
        assertEquals(Currency.getInstance("USD"), deserializer.deserialize(mockParser("USD"), mockContext()));
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        assertNull(deserializer.deserialize(null, null));
        assertEquals(Pattern.compile("[a-z]+"), deserializer.deserialize(mockParser("[a-z]+"), mockContext()));
    }

    @Test
    public void testDeserializeLocaleWithSingleArgument() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertNull(deserializer.deserialize(null, null));
        assertEquals(new Locale("en"), deserializer.deserialize(mockParser("en"), mockContext()));
    }

    @Test
    public void testDeserializeLocaleWithTwoArguments() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        assertNull(deserializer.deserialize(null, null));
        assertEquals(new Locale("en", "US"), deserializer.deserialize(mockParser("en-US"), mockContext()));
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        assertNull(deserializer.deserialize(null, null));
        assertEquals(Charset.forName("UTF-8"), deserializer.deserialize(mockParser("UTF-8"), mockContext()));
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        assertNull(deserializer.deserialize(null, null));
        assertEquals(TimeZone.getTimeZone("America/New_York"), deserializer.deserialize(mockParser("America/New_York"), mockContext()));
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        assertNull(deserializer.deserialize(null, null));
        assertEquals(InetAddress.getByName("localhost"), deserializer.deserialize(mockParser("localhost"), mockContext()));
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        assertNull(deserializer.deserialize(null, null));
        assertEquals(new InetSocketAddress("localhost", 8080), deserializer.deserialize(mockParser("localhost:8080"), mockContext()));
    }

    @Test
    public void testDeserializeStringBuilder() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        assertNull(deserializer.deserialize(null, null));
        assertEquals(new StringBuilder("test"), deserializer.deserialize(mockParser("test"), mockContext()));
    }

    @Test
    public void testDeserializeInvalidValue() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        JsonParser parser = mockParser("invalid");
        DeserializationContext context = mockContext();
        when(parser.getValueAsString()).thenReturn("invalid");
        when(context.weirdStringException("invalid", File.class, "not a valid textual representation")).thenReturn(new InvalidFormatException(parser, "not a valid textual representation", "invalid", File.class));
        try {
            deserializer.deserialize(parser, context);
            fail("Expected InvalidFormatException");
        } catch (InvalidFormatException e) {
            assertEquals("not a valid textual representation, problem: For input string: \"invalid\"", e.getMessage());
        }
    }

    private JsonParser mockParser(String value) {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(value);
        return parser;
    }

    private DeserializationContext mockContext() {
        return mock(DeserializationContext.class);
    }
}