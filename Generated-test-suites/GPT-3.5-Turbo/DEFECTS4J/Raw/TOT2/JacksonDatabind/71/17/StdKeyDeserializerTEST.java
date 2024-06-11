import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey_ReturnNull() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertNull(deser.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_ValidStringKey_ReturnString() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("Test", deser.deserializeKey("Test", null));
    }

    @Test
    public void testDeserializeKey_EmptyStringKey_ReturnEmptyString() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("", deser.deserializeKey("", null));
    }

    @Test
    public void testParse_IntKey_ReturnIntegerValue() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(5, deser._parseInt("5"));
    }

    @Test
    public void testParse_LongKey_ReturnLongValue() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertEquals(123456789L, deser._parseLong("123456789"));
    }

    @Test
    public void testParse_DoubleKey_ReturnDoubleValue() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(3.14, deser._parseDouble("3.14"), 0.001);
    }

    @Test
    public void testParseBoolean_TrueKey_ReturnTrue() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(true, deser._parse("true", null));
    }

    @Test
    public void testParseBoolean_FalseKey_ReturnFalse() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(false, deser._parse("false", null));
    }

    @Test
    public void testParseBoolean_InvalidKey_ThrowException() {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertThrows(JsonMappingException.class, () -> deser._parse("invalid", null));
    }

}