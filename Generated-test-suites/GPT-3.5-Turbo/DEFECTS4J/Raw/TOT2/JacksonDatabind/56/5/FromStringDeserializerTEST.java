import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testTypes() {
        Class<?>[] expectedTypes = {
            File.class,
            URL.class,
            URI.class,
            Class.class,
            JavaType.class,
            Currency.class,
            Pattern.class,
            Locale.class,
            Charset.class,
            TimeZone.class,
            InetAddress.class,
            InetSocketAddress.class
        };

        assertArrayEquals(expectedTypes, FromStringDeserializer.types());
    }

    @Test
    public void testFindDeserializer() {
        assertEquals(FromStringDeserializer.Std.STD_FILE, FromStringDeserializer.findDeserializer(File.class)._kind);
        assertEquals(FromStringDeserializer.Std.STD_URL, FromStringDeserializer.findDeserializer(URL.class)._kind);
        assertEquals(FromStringDeserializer.Std.STD_URI, FromStringDeserializer.findDeserializer(URI.class)._kind);
        assertEquals(FromStringDeserializer.Std.STD_CLASS, FromStringDeserializer.findDeserializer(Class.class)._kind);
        assertEquals(FromStringDeserializer.Std.STD_JAVA_TYPE, FromStringDeserializer.findDeserializer(JavaType.class)._kind);
        assertEquals(FromStringDeserializer.Std.STD_CURRENCY, FromStringDeserializer.findDeserializer(Currency.class)._kind);
        assertEquals(FromStringDeserializer.Std.STD_PATTERN, FromStringDeserializer.findDeserializer(Pattern.class)._kind);
        assertEquals(FromStringDeserializer.Std.STD_LOCALE, FromStringDeserializer.findDeserializer(Locale.class)._kind);
        assertEquals(FromStringDeserializer.Std.STD_CHARSET, FromStringDeserializer.findDeserializer(Charset.class)._kind);
        assertEquals(FromStringDeserializer.Std.STD_TIME_ZONE, FromStringDeserializer.findDeserializer(TimeZone.class)._kind);
        assertEquals(FromStringDeserializer.Std.STD_INET_ADDRESS, FromStringDeserializer.findDeserializer(InetAddress.class)._kind);
        assertEquals(FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS, FromStringDeserializer.findDeserializer(InetSocketAddress.class)._kind);
        assertNull(FromStringDeserializer.findDeserializer(String.class));
    }

    // Add more test cases for other methods

}