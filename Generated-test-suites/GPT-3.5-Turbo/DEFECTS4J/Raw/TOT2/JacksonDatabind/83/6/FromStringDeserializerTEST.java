import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testTypes() {
        Class<?>[] types = FromStringDeserializer.types();
        assertNotNull(types);
        assertEquals(13, types.length);
    }

    @Test
    public void testFindDeserializer() {
        Std deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertNotNull(deserializer);
        assertEquals(Std.STD_FILE, deserializer._kind);
        assertEquals(File.class, deserializer._valueClass);
    }

    @Test
    public void testDeserialize() {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(File.class, Std.STD_FILE);
        DeserializationContext ctxt = new DeserializationContext(null);
        
        try {
            stdDeserializer._deserialize("test.txt", ctxt);
        } catch (IOException e) {
            fail("IOException thrown unexpectedly");
        }
    }

    @Test
    public void testDeserializeEmbedded() {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(File.class, Std.STD_FILE);
        DeserializationContext ctxt = new DeserializationContext(null);
        
        try {
            stdDeserializer._deserializeEmbedded("test", ctxt);
            fail("Exception not thrown for conversion failure");
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testDeserializeFromEmptyString() {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(File.class, Std.STD_FILE);
        
        try {
            stdDeserializer._deserializeFromEmptyString();
        } catch (IOException e) {
            fail("IOException thrown unexpectedly");
        }
    }
}