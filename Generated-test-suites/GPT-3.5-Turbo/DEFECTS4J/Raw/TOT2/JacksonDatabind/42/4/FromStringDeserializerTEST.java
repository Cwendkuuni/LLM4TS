import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testTypes() {
        Class<?>[] types = FromStringDeserializer.types();
        assertNotNull(types);
        assertEquals(12, types.length);
    }

    @Test
    public void testFindDeserializer() {
        Std deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertNotNull(deserializer);
    }

    @Test
    public void testDeserialize() {
        // Test typical string input
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale locale = (Locale) deserializer._deserialize("en_US", null);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        
        // Test empty string input
        deserializer = new FromStringDeserializer.Std(String.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = (TimeZone) deserializer._deserializeFromEmptyString();
        assertEquals("GMT", timeZone.getID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalid() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 100);
        deserializer._deserialize("invalid", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDeserializerInvalid() {
        Std deserializer = FromStringDeserializer.findDeserializer(String.class);
    }
}