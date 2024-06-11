import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_String() throws Exception {
        StdKeyDeserializer stringKD = StdKeyDeserializer.StringKD.forType(String.class);
        
        String key = "test";
        
        Object result = stringKD.deserializeKey(key, null);
        
        assertEquals(key, result);
    }
    
    @Test
    public void testDeserializeKey_Integer() throws Exception {
        StdKeyDeserializer integerKD = StdKeyDeserializer.forType(Integer.class);
        
        String key = "123";
        
        Object result = integerKD.deserializeKey(key, null);
        
        assertEquals(Integer.valueOf(key), result);
    }
    
    @Test
    public void testDeserializeKey_Long_Overflow() throws Exception {
        StdKeyDeserializer longKD = StdKeyDeserializer.forType(Long.class);
        
        String key = "100000000000000000000000000000000000000";
        
        try {
            longKD.deserializeKey(key, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("For input string: \"" + key + "\"", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeKey_Character() throws Exception {
        StdKeyDeserializer characterKD = StdKeyDeserializer.forType(Character.class);
        
        String key = "a";
        
        Object result = characterKD.deserializeKey(key, null);
        
        assertEquals(Character.valueOf(key.charAt(0)), result);
    }
    
    @Test
    public void testDeserializeKey_Double() throws Exception {
        StdKeyDeserializer doubleKD = StdKeyDeserializer.forType(Double.class);
        
        String key = "3.14";
        
        Object result = doubleKD.deserializeKey(key, null);
        
        assertEquals(Double.valueOf(key), result);
    }
    
    @Test
    public void testDeserializeKey_Locale() throws Exception {
        StdKeyDeserializer localeKD = StdKeyDeserializer.forType(Locale.class);
        
        String key = "en_US";
        
        Object result = localeKD.deserializeKey(key, null);
        
        assertEquals(Locale.forLanguageTag(key.replace('_', '-')), result);
    }
    
    @Test
    public void testDeserializeKey_Currency_Unknown() throws Exception {
        StdKeyDeserializer currencyKD = StdKeyDeserializer.forType(Currency.class);
        
        String key = "XYZ";
        
        try {
            currencyKD.deserializeKey(key, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown currency code: " + key, e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeKey_Date() throws Exception {
        StdKeyDeserializer dateKD = StdKeyDeserializer.forType(Date.class);
        
        String key = "2020-01-01T00:00:00Z";
        
        Object result = dateKD.deserializeKey(key, null);
        
        // Compare the string representation because Date does not override equals method
        assertEquals(key, result.toString());
    }
    
    @Test
    public void testDeserializeKey_Enum() throws Exception {
        StdKeyDeserializer enumKD = new StdKeyDeserializer.EnumKD(EnumResolver.constructUnsafe(EnumClass.class, null), null);
        
        String key = "VALUE1";
        
        Object result = enumKD.deserializeKey(key, null);
        
        assertEquals(EnumClass.VALUE1, result);
    }
    
}

enum EnumClass {
    VALUE1,
    VALUE2,
    VALUE3
}