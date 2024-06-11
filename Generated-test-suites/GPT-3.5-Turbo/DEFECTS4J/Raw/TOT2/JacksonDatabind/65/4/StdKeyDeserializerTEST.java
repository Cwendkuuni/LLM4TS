import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKeyWithNullKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        try {
            assertNull(keyDeserializer.deserializeKey(null, null));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testDeserializeKeyWithString() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        try {
            assertEquals("test", keyDeserializer.deserializeKey("test", null));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testDeserializeKeyWithInvalidKey() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        try {
            keyDeserializer.deserializeKey("", null);
            fail("IOException should be thrown for empty key");
        } catch (IOException e) {
            // success
        }
    }

    @Test
    public void testDeserializeKeyWithUnknownEnumValuesAsNull() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, EnumExample.class);
        DeserializationContext ctxt = new DeserializationContext();
        ctxt.getConfig().enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        try {
            assertNull(keyDeserializer.deserializeKey("UNKNOWN", ctxt));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}