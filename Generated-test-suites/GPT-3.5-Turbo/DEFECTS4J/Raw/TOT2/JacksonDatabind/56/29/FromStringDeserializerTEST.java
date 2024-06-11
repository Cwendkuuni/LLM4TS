import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {
    
    @Test
    public void testTypesMethod() {
        FromStringDeserializer<?> deserializer = new FromStringDeserializer.Std(Object.class);
        Class<?>[] types = deserializer.types();
        assertNotNull(types);
        assertEquals(12, types.length);
    }
    
    @Test
    public void testFindDeserializerForFile() {
        Std std = (Std) FromStringDeserializer.findDeserializer(File.class);
        assertNotNull(std);
        assertEquals(1, std._kind);
    }
    
    @Test
    public void testFindDeserializerForURL() {
        Std std = (Std) FromStringDeserializer.findDeserializer(URL.class);
        assertNotNull(std);
        assertEquals(2, std._kind);
    }
    
    @Test
    public void testDeserializeString() {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(Object.class);
        String value = "test";
        assertNotNull(std._deserialize(value, null));
    }
    
    @Test
    public void testDeserializeEmptyStringForURL() {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(URL.class);
        assertNotNull(std._deserializeFromEmptyString());
    }
    
    @Test
    public void testDeserializeEmbeddedObject() {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(Object.class);
        try {
            std._deserializeEmbedded(new Object(), null);
        } catch (Exception e) {
            assertTrue(e instanceof JsonProcessingException);
        }
    }
    
    @Test
    public void testDeserializeFromEmptyStringForURI() {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(URI.class);
        assertNotNull(std._deserializeFromEmptyString());
    }
    
    @Test
    public void testDeserializeInvalidValue() {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(Object.class);
        try {
            std._deserialize("invalid", null);
        } catch (Exception e) {
            assertTrue(e instanceof JsonMappingException);
        }
    }
    
    @Test
    public void testDeserializeFromEmptyString() {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(Object.class);
        assertNull(std._deserializeFromEmptyString());
    }
    
}