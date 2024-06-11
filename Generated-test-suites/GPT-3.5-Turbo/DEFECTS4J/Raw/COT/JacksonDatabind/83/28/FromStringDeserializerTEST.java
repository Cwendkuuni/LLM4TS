import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserializer() {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = (File) fileDeserializer._deserialize("/path/to/file", null);
        assertEquals(new File("/path/to/file"), file);
    }

    @Test
    public void testURLDeserializer() {
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = (URL) urlDeserializer._deserialize("http://www.example.com", null);
        assertEquals("http://www.example.com", url.toString());
    }

    @Test
    public void testURIDeserializer() {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = (URI) uriDeserializer._deserialize("http://www.example.com", null);
        assertEquals(URI.create("http://www.example.com"), uri);
    }

    @Test
    public void testClassDeserializer() {
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> clazz = (Class<?>) classDeserializer._deserialize("com.example.MyClass", null);
        assertEquals(com.example.MyClass.class, clazz);
    }

    @Test
    public void testJavaTypeDeserializer() {
        FromStringDeserializer.Std javaTypeDeserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType javaType = (JavaType) javaTypeDeserializer._deserialize("com.example.MyClass", null);
        assertEquals(JavaType.class, javaType.getClass());
    }

    @Test
    public void testCurrencyDeserializer() {
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency currency = (Currency) currencyDeserializer._deserialize("USD", null);
        assertEquals("USD", currency.getCurrencyCode());
    }

    @Test
    public void testPatternDeserializer() {
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern pattern = (Pattern) patternDeserializer._deserialize("\\d+", null);
        assertEquals("\\d+", pattern.pattern());
    }

    @Test
    public void testLocaleDeserializer() {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = (Locale) localeDeserializer._deserialize("en-US", null);
        assertEquals(Locale.US, locale);
    }

    @Test
    public void testCharsetDeserializer() {
        FromStringDeserializer.Std charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset charset = (Charset) charsetDeserializer._deserialize("UTF-8", null);
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testTimeZoneDeserializer() {
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = (TimeZone) timeZoneDeserializer._deserialize("America/New_York", null);
        assertEquals(TimeZone.getTimeZone("America/New_York"), timeZone);
    }

    @Test
    public void testInetAddressDeserializer() throws UnknownHostException {
        FromStringDeserializer.Std inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress inetAddress = (InetAddress) inetAddressDeserializer._deserialize("127.0.0.1", null);
        assertEquals(InetAddress.getByName("127.0.0.1"), inetAddress);
    }

    @Test
    public void testInetSocketAddressDeserializer() throws UnknownHostException {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer._deserialize("127.0.0.1:8080", null);
        assertEquals(new InetSocketAddress("127.0.0.1", 8080), inetSocketAddress);
    }

    @Test
    public void testStringBuilderDeserializer() {
        FromStringDeserializer.Std stringBuilderDeserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        StringBuilder stringBuilder = (StringBuilder) stringBuilderDeserializer._deserialize("test", null);
        assertEquals("test", stringBuilder.toString());
    }
}