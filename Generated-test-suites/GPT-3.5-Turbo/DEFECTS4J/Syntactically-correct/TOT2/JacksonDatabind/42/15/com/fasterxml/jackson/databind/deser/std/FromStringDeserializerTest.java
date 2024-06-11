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
        assertEquals(Std.STD_FILE, deserializer._kind);
    }

    @Test
    public void testDeserialize_EmptyString_ReturnsNull() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        assertNull(deserializer._deserializeFromEmptyString());
    }

    @Test
    public void testDeserialize_EmbeddedObject() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, 0);
        assertThrows(IOException.class, 
            () -> deserializer._deserializeEmbedded(new Object(), new DeserializationContext(null, null)));
    }

    @Test
    public void testDeserialize_TimeZone() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone timeZone = (TimeZone) deserializer._deserialize("GMT", new DeserializationContext(null, null));
        assertNotNull(timeZone);
        assertEquals("GMT", timeZone.getID());
    }

    @Test
    public void testDeserializeFromEmptyString_URI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, FromStringDeserializer.Std.STD_URI);
        Object result = deserializer._deserializeFromEmptyString();
        assertNotNull(result);
        assertTrue(result instanceof URI);
        assertEquals("", ((URI) result).toString());
    }
}