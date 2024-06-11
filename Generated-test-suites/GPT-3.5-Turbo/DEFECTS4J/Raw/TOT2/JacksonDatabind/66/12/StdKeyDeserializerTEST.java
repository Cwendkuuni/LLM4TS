import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, String.class);
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_InvalidKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(2, Integer.class);
        assertEquals(Integer.valueOf(10), deserializer.deserializeKey("10", null));
    }

    @Test
    public void testInvalidKeyRepresentation() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(3, Long.class);
        assertEquals(Long.valueOf(100), deserializer.deserializeKey("100", null));
    }

    @Test
    public void testInvalidKeyRepresentation_Exception() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(4, Character.class);
        assertEquals(Character.valueOf('A'), deserializer.deserializeKey("A", null));
    }

    @Test
    public void testParseDate() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(5, Date.class);
        assertEquals(new Date(), deserializer._parse("2021-08-15", null));
    }

    @Test
    public void testParseDate_Exception() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(6, Calendar.class);
        assertNull(deserializer._parse("2021-08-15", null));
    }

}