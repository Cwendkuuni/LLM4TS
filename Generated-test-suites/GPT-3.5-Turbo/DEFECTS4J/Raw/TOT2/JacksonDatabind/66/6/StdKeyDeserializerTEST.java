import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_Boolean() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", null));
        assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", null));
    }

    @Test
    public void testDeserializeKey_Byte() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        assertEquals(Byte.valueOf((byte) 100), deserializer.deserializeKey("100", null));
    }

    @Test
    public void testDeserializeKey_Short() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
        assertEquals(Short.valueOf((short) 1000), deserializer.deserializeKey("1000", null));
    }

    @Test
    public void testDeserializeKey_Char() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
        assertEquals(Character.valueOf('A'), deserializer.deserializeKey("A", null));
    }

    @Test
    public void testDeserializeKey_Int() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(1000, deserializer.deserializeKey("1000", null));
    }

    @Test
    public void testDeserializeKey_Long() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertEquals(100000L, deserializer.deserializeKey("100000", null));
    }

    @Test
    public void testDeserializeKey_Float() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
        assertEquals(3.14f, deserializer.deserializeKey("3.14", null));
    }

    @Test
    public void testDeserializeKey_Double() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(3.14159, deserializer.deserializeKey("3.14159", null));
    }

    @Test
    public void testDeserializeKey_Locale() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        assertNotNull(deserializer.deserializeKey("en_US", null));
    }

    @Test
    public void testDeserializeKey_Currency() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class);
        assertNotNull(deserializer.deserializeKey("USD", null));
    }

    @Test
    public void testDeserializeKey_Date() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        assertNotNull(deserializer.deserializeKey("2022-01-01", null));
    }

    @Test
    public void testDeserializeKey_Calendar() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
        assertNotNull(deserializer.deserializeKey("2022-01-01", null));
    }

    @Test
    public void testDeserializeKey_URI() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        assertNotNull(deserializer.deserializeKey("http://www.example.com", null));
    }

    @Test
    public void testDeserializeKey_URL() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        assertNotNull(deserializer.deserializeKey("http://www.example.com", null));
    }

    @Test
    public void testDeserializeKey_Class() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class);
        assertNotNull(deserializer.deserializeKey("java.lang.String", null));
    }

    @Test
    public void testDeserializeKey_UnknownEnum() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, Enum.class);
        assertNull(deserializer.deserializeKey("UNKNOWN", null));
    }
}