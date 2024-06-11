
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer();

        // Test cases for TYPE_BOOLEAN
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true"));
        assertEquals(Boolean.FALSE, deserializer.deserializeKey("false"));

        // Test cases for TYPE_BYTE
        assertEquals(Byte.valueOf((byte) 100), deserializer.deserializeKey("100"));

        // Test cases for TYPE_SHORT
        assertEquals(Short.valueOf((short) 1000), deserializer.deserializeKey("1000"));

        // Test cases for TYPE_CHAR
        assertEquals(new Character('a'), deserializer.deserializeKey("a"));

        // Test cases for TYPE_INT
        assertEquals(Integer.valueOf(10000), deserializer.deserializeKey("10000"));

        // Test cases for TYPE_LONG
        assertEquals(Long.valueOf(100000L), deserializer.deserializeKey("100000"));

        // Test cases for TYPE_FLOAT
        assertEquals(Float.valueOf(3.14f), deserializer.deserializeKey("3.14"));

        // Test cases for TYPE_DOUBLE
        assertEquals(Double.valueOf(3.14159), deserializer.deserializeKey("3.14159"));

        // Test cases for TYPE_LOCALE
        assertEquals(Locale.US, deserializer.deserializeKey("en_US"));

        // Test cases for TYPE_CURRENCY
        assertEquals(Currency.getInstance("USD"), deserializer.deserializeKey("USD"));

        // Test cases for TYPE_DATE
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");
        assertEquals(date, deserializer.deserializeKey("2020-01-01"));

        // Test cases for TYPE_CALENDAR
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        assertEquals(calendar, deserializer.deserializeKey("2020-01-01"));

        // Test cases for TYPE_UUID
        UUID uuid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        assertEquals(uuid, deserializer.deserializeKey("38400000-8cf0-11bd-b23e-10b96e4ef00d"));

        // Test cases for TYPE_URI
        URI uri = new URI("https://www.example.com");
        assertEquals(uri, deserializer.deserializeKey("https://www.example.com"));

        // Test cases for TYPE_URL
        URL url = new URL("https://www.example.com");
        assertEquals(url, deserializer.deserializeKey("https://www.example.com"));

        // Test cases for TYPE_CLASS
        assertEquals(String.class, deserializer.deserializeKey("java.lang.String"));

        // Test cases for TYPE_BYTE_ARRAY
        assertArrayEquals("test".getBytes(), deserializer.deserializeKey("test"));
    }
}
