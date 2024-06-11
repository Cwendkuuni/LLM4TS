
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_File_Success() throws IOException {
        String json = "\"/test/file\"";
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File result = (File) deserializer._deserialize(json, null);
        assertEquals("/test/file", result.getPath());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_File_EmptyString() throws IOException {
        String json = "\"\"";
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File result = (File) deserializer._deserialize(json, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_File_InvalidFormat() throws IOException {
        String json = "\"\\//\\/\\\"";
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File result = (File) deserializer._deserialize(json, null);
    }

    @Test
    public void testDeserialize_URL_Success() throws IOException {
        String json = "\"http://www.example.com\"";
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL result = (URL) deserializer._deserialize(json, null);
        assertEquals("http://www.example.com", result.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_URL_EmptyString() throws IOException {
        String json = "\"\"";
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL result = (URL) deserializer._deserialize(json, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_URL_InvalidFormat() throws IOException {
        String json = "\"htt\\\\";
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL result = (URL) deserializer._deserialize(json, null);
    }

    @Test
    public void testDeserialize_URI_Success() throws IOException {
        String json = "\"http://www.example.com\"";
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI result = (URI) deserializer._deserialize(json, null);
        assertEquals("http://www.example.com", result.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_URI_EmptyString() throws IOException {
        String json = "\"\"";
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI result = (URI) deserializer._deserialize(json, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_URI_InvalidFormat() throws IOException {
        String json = "\"htt\\\\";
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI result = (URI) deserializer._deserialize(json, null);
    }

    @Test
    public void testDeserialize_Locale_Success() throws IOException {
        String json = "\"en-US\"";
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale result = (Locale) deserializer._deserialize(json, null);
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
        assertEquals("en-US", result.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_Locale_EmptyString() throws IOException {
        String json = "\"\"";
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale result = (Locale) deserializer._deserialize(json, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_Locale_InvalidFormat() throws IOException {
        String json = "\"en_US\"";
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale result = (Locale) deserializer._deserialize(json, null);
    }

    @Test
    public void testDeserialize_StringBuilder_Success() throws IOException {
        String json = "\"test string\"";
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        StringBuilder result = (StringBuilder) deserializer._deserialize(json, null);
        assertEquals("test string", result.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_StringBuilder_EmptyString() throws IOException {
        String json = "\"\"";
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        StringBuilder result = (StringBuilder) deserializer._deserialize(json, null);
    }

    @Test
    public void testDeserialize_StringBuilder_NullString() throws IOException {
        String json = "null";
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        StringBuilder result = (StringBuilder) deserializer._deserialize(json, null);
        assertNull(result);
    }

}
