
import static org.junit.Assert.*;

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

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserializer() throws IOException {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File expectedFile = new File("test.txt");
        assertEquals(expectedFile, fileDeserializer._deserialize("test.txt", null));
    }

    @Test
    public void testURLDeserializer() throws IOException {
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL expectedURL = new URL("https://www.example.com");
        assertEquals(expectedURL, urlDeserializer._deserialize("https://www.example.com", null));
    }

    @Test
    public void testURIDeserializer() throws IOException {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI expectedURI = new URI("https://www.example.com");
        assertEquals(expectedURI, uriDeserializer._deserialize("https://www.example.com", null));
    }

    @Test
    public void testClassDeserializer() throws IOException, ClassNotFoundException {
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> expectedClass = Class.forName("java.lang.Object");
        assertEquals(expectedClass, classDeserializer._deserialize("java.lang.Object", null));
    }

    @Test
    public void testJavaTypeDeserializer() throws IOException {
        FromStringDeserializer.Std javaTypeDeserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType expectedJavaType = JavaType.constructUnchecked(Object.class);
        assertEquals(expectedJavaType, javaTypeDeserializer._deserialize("java.lang.Object", null));
    }

    @Test
    public void testCurrencyDeserializer() throws IOException {
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency expectedCurrency = Currency.getInstance("USD");
        assertEquals(expectedCurrency, currencyDeserializer._deserialize("USD", null));
    }

    @Test
    public void testPatternDeserializer() throws IOException {
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        String regex = "[0-9]+";
        Pattern expectedPattern = Pattern.compile(regex);
        assertEquals(expectedPattern, patternDeserializer._deserialize(regex, null));
    }

    @Test
    public void testLocaleDeserializer() throws IOException {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale expectedLocale = new Locale("en", "US");
        assertEquals(expectedLocale, localeDeserializer._deserialize("en_US", null));
    }

    @Test
    public void testCharsetDeserializer() throws IOException {
        FromStringDeserializer.Std charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset expectedCharset = Charset.forName("UTF-8");
        assertEquals(expectedCharset, charsetDeserializer._deserialize("UTF-8", null));
    }

    @Test
    public void testTimeZoneDeserializer() throws IOException {
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone expectedTimeZone = TimeZone.getTimeZone("GMT");
        assertEquals(expectedTimeZone, timeZoneDeserializer._deserialize("GMT", null));
    }

    @Test
    public void testInetAddressDeserializer() throws IOException {
        FromStringDeserializer.Std inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress expectedInetAddress = InetAddress.getByName("localhost");
        assertEquals(expectedInetAddress, inetAddressDeserializer._deserialize("localhost", null));
    }

    @Test
    public void testInetSocketAddressDeserializer() throws IOException {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress expectedInetSocketAddress = new InetSocketAddress("localhost", 8080);
        assertEquals(expectedInetSocketAddress, inetSocketAddressDeserializer._deserialize("localhost:8080", null));
    }
}

