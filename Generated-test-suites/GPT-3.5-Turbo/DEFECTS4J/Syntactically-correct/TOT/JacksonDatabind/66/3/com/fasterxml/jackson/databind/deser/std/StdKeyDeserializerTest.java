import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer stringDeser;
    private StdKeyDeserializer intDeser;
    private StdKeyDeserializer enumDeser;

    @Before
    public void setup() {
        stringDeser = StdKeyDeserializer.forType(String.class);
        intDeser = StdKeyDeserializer.forType(Integer.class);
        enumDeser = StdKeyDeserializer.forType(Enum.class);
    }
    
    @Test
    public void testStringDeserializeKey_WhenValidString_ThenReturnStringKey() {
        String key = "test key";
        DeserializationContext ctxt = new DeserializationContext();
        
        Object result = stringDeser.deserializeKey(key, ctxt);
        
        assertEquals(key, result);
    }
    
    @Test
    public void testStringDeserializeKey_WhenNullString_ThenReturnNullKey() {
        String key = null;
        DeserializationContext ctxt = new DeserializationContext();
        
        Object result = stringDeser.deserializeKey(key, ctxt);
        
        assertNull(result);
    }
    
    @Test
    public void testStringDeserializeKey_WhenEmptyString_ThenReturnEmptyStringKey() {
        String key = "";
        DeserializationContext ctxt = new DeserializationContext();
        
        Object result = stringDeser.deserializeKey(key, ctxt);
        
        assertEquals(key, result);
    }
    
    @Test
    public void testIntDeserializeKey_WhenValidIntString_ThenReturnIntegerKey() {
        String key = "123";
        DeserializationContext ctxt = new DeserializationContext();
        
        Object result = intDeser.deserializeKey(key, ctxt);
        
        assertTrue(result instanceof Integer);
        assertEquals(Integer.parseInt(key), result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIntDeserializeKey_WhenInvalidIntString_ThenThrowIllegalArgumentException() {
        String key = "abc";
        DeserializationContext ctxt = new DeserializationContext();
        
        intDeser.deserializeKey(key, ctxt);
    }
    
    @Test
    public void testEnumDeserializeKey_WhenValidEnumString_ThenReturnEnumValue() {
        String key = "VALUE1";
        DeserializationContext ctxt = new DeserializationContext();
        
        Object result = enumDeser.deserializeKey(key, ctxt);
        
        assertTrue(result instanceof Enum);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEnumDeserializeKey_WhenInvalidEnumString_ThenThrowIllegalArgumentException() {
        String key = "VALUE3";
        DeserializationContext ctxt = new DeserializationContext();
        
        enumDeser.deserializeKey(key, ctxt);
    }
}