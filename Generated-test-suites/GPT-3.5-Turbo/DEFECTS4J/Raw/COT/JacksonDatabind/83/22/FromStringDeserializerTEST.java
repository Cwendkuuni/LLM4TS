
import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserializer() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = (File)deserializer.deserialize(null, null);
        assertNull(file);
    }

    @Test
    public void testURLDeserializer() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = (URL)deserializer.deserialize(null, null);
        assertNull(url);
    }

    @Test
    public void testURIDeserializer() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = (URI)deserializer.deserialize(null, null);
        assertNull(uri);
    }

    @Test
    public void testClassDeserializer() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> cl = (Class<?>)deserializer.deserialize(null, null);
        assertNull(cl);
    }

    @Test
    public void testJavaTypeDeserializer() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, FromStringDeserializer.Std.STD_JAVA_TYPE);
        JavaType javaType = (JavaType)deserializer.deserialize(null, null);
        assertNull(javaType);
    }

    @Test
    public void testCurrencyDeserializer() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency currency = (Currency)deserializer.deserialize(null, null);
        assertNull(currency);
    }

    @Test
    public void testPatternDeserializer() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern pattern = (Pattern)deserializer.deserialize(null, null);
        assertNull(pattern);
    }

    @Test
    public void testLocaleDeserializer() throws IOException
    {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = (Locale)deserializer.deserialize(null, null);
        assertNull(locale);
    }

    @Test
    public void testCharsetDeserializer() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);
        Charset charset = (Charset)deserializer.deserialize(null, null);
        assertNull(charset);
    }

    @Test
    public void testTimeZoneDeserializer() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = (TimeZone)deserializer.deserialize(null, null);
        assertNull(timeZone);
    }

    @Test
    public void testInetAddressDeserializer() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);
        InetAddress inetAddress = (InetAddress)deserializer.deserialize(null, null);
        assertNull(inetAddress);
    }

    @Test
    public void testInetSocketAddressDeserializer() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);
        InetSocketAddress inetSocketAddress = (InetSocketAddress)deserializer.deserialize(null, null);
        assertNull(inetSocketAddress);
    }

    @Test
    public void testStringBuilderDeserializer() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);
        StringBuilder stringBuilder = (StringBuilder)deserializer.deserialize(null, null);
        assertNull(stringBuilder);
    }

    @Test
    public void testDeserializeInvalidStringValue() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        try {
            deserializer.deserialize(new JsonParser() {
                @Override
                public String getValueAsString() {
                    return "invalid file path";
                }
            }, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = (URI)deserializer.deserialize(new JsonParser() {
            @Override
            public String getValueAsString() {
                return "";
            }
        }, null);
        assertEquals(new URI(""), uri);
    }

    @Test
    public void testDeserializeLocaleWithOneArgument() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = (Locale)deserializer.deserialize(new JsonParser() {
            @Override
            public String getValueAsString() {
                return "en";
            }
        }, null);
        assertEquals(new Locale("en"), locale);
    }

    @Test
    public void testDeserializeLocaleWithTwoArguments() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = (Locale)deserializer.deserialize(new JsonParser() {
            @Override
            public String getValueAsString() {
                return "en_US";
            }
        }, null);
        assertEquals(new Locale("en", "US"), locale);
    }

}
