import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.*;
import java.net.URL;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import com.fasterxml.jackson.databind.JavaType;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_file() throws IOException {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        File result = fileDeserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_url() throws IOException {
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        URL result = urlDeserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_uri() throws IOException {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        URI result = uriDeserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_class() throws IOException {
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Class<?> result = classDeserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_javaType() throws IOException {
        FromStringDeserializer.Std javaTypeDeserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        JavaType result = javaTypeDeserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_currency() throws IOException {
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Currency result = currencyDeserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_pattern() throws IOException {
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Pattern result = patternDeserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_locale() throws IOException {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Locale result = localeDeserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_charset() throws IOException {
        FromStringDeserializer.Std charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Charset result = charsetDeserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_timeZone() throws IOException {
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        TimeZone result = timeZoneDeserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_inetAddress() throws IOException {
        FromStringDeserializer.Std inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        InetAddress result = inetAddressDeserializer.deserialize(jp, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserialize_inetSocketAddress() throws IOException {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        InetSocketAddress result = inetSocketAddressDeserializer.deserialize(jp, ctxt);
        assertNull(result);
    }
}