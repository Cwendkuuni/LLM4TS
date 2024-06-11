import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserializer() {
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        String filePath = "path/to/file.txt";

        File expectedFile = new File(filePath);
        File actualFile = (File) fileDeserializer.deserialize(filePath, null);

        assertEquals(expectedFile, actualFile);
    }

    @Test
    public void testURLDeserializer() {
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        String urlString = "https://www.example.com";

        URL expectedURL = new URL(urlString);
        URL actualURL = (URL) urlDeserializer.deserialize(urlString, null);

        assertEquals(expectedURL, actualURL);
    }

    @Test
    public void testURIDeserializer() {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        String uriString = "https://www.example.com";

        URI expectedURI = URI.create(uriString);
        URI actualURI = (URI) uriDeserializer.deserialize(uriString, null);

        assertEquals(expectedURI, actualURI);
    }

    @Test
    public void testClassDeserializer() {
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        String className = "java.lang.String";

        Class<?> expectedClass = String.class;
        Class<?> actualClass = (Class<?>) classDeserializer.deserialize(className, null);

        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void testJavaTypeDeserializer() {
        FromStringDeserializer.Std javaTypeDeserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        String javaTypeName = "java.lang.String";

        JavaType expectedJavaType = new JavaType(String.class);
        JavaType actualJavaType = (JavaType) javaTypeDeserializer.deserialize(javaTypeName, null);

        assertEquals(expectedJavaType, actualJavaType);
    }

    @Test
    public void testCurrencyDeserializer() {
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        String currencyCode = "USD";

        Currency expectedCurrency = Currency.getInstance(currencyCode);
        Currency actualCurrency = (Currency) currencyDeserializer.deserialize(currencyCode, null);

        assertEquals(expectedCurrency, actualCurrency);
    }

    @Test
    public void testPatternDeserializer() {
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        String regex = "\\d+";

        Pattern expectedPattern = Pattern.compile(regex);
        Pattern actualPattern = (Pattern) patternDeserializer.deserialize(regex, null);

        assertEquals(expectedPattern.pattern(), actualPattern.pattern());
    }

    @Test
    public void testLocaleDeserializer() {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        String localeString = "en_US";

        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = (Locale) localeDeserializer.deserialize(localeString, null);

        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testCharsetDeserializer() {
        FromStringDeserializer.Std charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        String charsetName = "UTF-8";

        Charset expectedCharset = Charset.forName(charsetName);
        Charset actualCharset = (Charset) charsetDeserializer.deserialize(charsetName, null);

        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testTimeZoneDeserializer() {
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        String timeZoneId = "GMT";

        TimeZone expectedTimeZone = TimeZone.getTimeZone(timeZoneId);
        TimeZone actualTimeZone = (TimeZone) timeZoneDeserializer.deserialize(timeZoneId, null);

        assertEquals(expectedTimeZone, actualTimeZone);
    }

    @Test
    public void testInetAddressDeserializer() {
        FromStringDeserializer.Std inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        String ipAddress = "192.0.2.1";

        InetAddress expectedInetAddress = InetAddress.getByName(ipAddress);
        InetAddress actualInetAddress = (InetAddress) inetAddressDeserializer.deserialize(ipAddress, null);

        assertArrayEquals(expectedInetAddress.getAddress(), actualInetAddress.getAddress());
    }

    @Test
    public void testInetSocketAddressDeserializer() {
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        String socketAddress = "localhost:8080";

        InetSocketAddress expectedInetSocketAddress = new InetSocketAddress("localhost", 8080);
        InetSocketAddress actualInetSocketAddress = (InetSocketAddress) inetSocketAddressDeserializer.deserialize(socketAddress, null);

        assertEquals(expectedInetSocketAddress, actualInetSocketAddress);
    }

}