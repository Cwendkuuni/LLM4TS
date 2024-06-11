import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {
    
    @Test
    public void testDeserializeKey_String() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringKD(String.class);
        
        String key = "testKey";
        String expected = key;
        
        Object result = keyDeserializer.deserializeKey(key, null);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeKey_Int() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringKD(Integer.class);
        
        String key = "42";
        Integer expected = 42;
        
        Object result = keyDeserializer.deserializeKey(key, null);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeKey_Enum() throws Exception {
        EnumResolver enumResolver = new EnumResolver(MyEnum.class, new Enum[]{MyEnum.VALUE1, MyEnum.VALUE2, MyEnum.VALUE3});
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.EnumKD(enumResolver, null);
        
        String key = "VALUE2";
        MyEnum expected = MyEnum.VALUE2;
        
        Object result = keyDeserializer.deserializeKey(key, null);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeserializeKey_InvalidEnum() throws Exception {
        EnumResolver enumResolver = new EnumResolver(MyEnum.class, new Enum[]{MyEnum.VALUE1, MyEnum.VALUE2, MyEnum.VALUE3});
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.EnumKD(enumResolver, null);
        
        String key = "INVALID";
        String expectedErrorMessage = "not one of values excepted for Enum class: [VALUE1, VALUE2, VALUE3]";
        
        try {
            keyDeserializer.deserializeKey(key, null);
            fail("Expected exception not thrown");
        } catch (RuntimeException e) {
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeKey_NullEnum() throws Exception {
        EnumResolver enumResolver = new EnumResolver(MyEnum.class, new Enum[]{MyEnum.VALUE1, MyEnum.VALUE2, MyEnum.VALUE3});
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.EnumKD(enumResolver, null);
        
        String key = null;
        String expectedErrorMessage = "not a valid representation";
        
        try {
            keyDeserializer.deserializeKey(key, null);
            fail("Expected exception not thrown");
        } catch (RuntimeException e) {
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
    
    private enum MyEnum {
        VALUE1, VALUE2, VALUE3
    }
}