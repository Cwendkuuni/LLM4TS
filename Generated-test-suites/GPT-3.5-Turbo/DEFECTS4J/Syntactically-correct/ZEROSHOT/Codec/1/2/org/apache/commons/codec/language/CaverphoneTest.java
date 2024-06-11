import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {
    
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Test case 1
        String txt1 = "hello";
        String expected1 = "HLL1111111";
        String result1 = caverphone.caverphone(txt1);
        assertEquals(expected1, result1);
        
        // Test case 2
        String txt2 = "world";
        String expected2 = "WLRT1111111";
        String result2 = caverphone.caverphone(txt2);
        assertEquals(expected2, result2);
        
        // Test case 3
        String txt3 = "testing";
        String expected3 = "TSTNK111111";
        String result3 = caverphone.caverphone(txt3);
        assertEquals(expected3, result3);
    }
    
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Test case 1
        String txt1 = "hello";
        String expected1 = "HLL1111111";
        Object result1 = caverphone.encode(txt1);
        assertTrue(result1 instanceof String);
        assertEquals(expected1, (String) result1);
        
        // Test case 2
        String txt2 = "world";
        String expected2 = "WLRT1111111";
        Object result2 = caverphone.encode(txt2);
        assertTrue(result2 instanceof String);
        assertEquals(expected2, (String) result2);
        
        // Test case 3
        String txt3 = "testing";
        String expected3 = "TSTNK111111";
        Object result3 = caverphone.encode(txt3);
        assertTrue(result3 instanceof String);
        assertEquals(expected3, (String) result3);
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test case 1
        String str1 = "hello";
        String str2 = "Hello";
        boolean expected1 = true;
        boolean result1 = caverphone.isCaverphoneEqual(str1, str2);
        assertEquals(expected1, result1);
        
        // Test case 2
        String str3 = "world";
        String str4 = "World";
        boolean expected2 = true;
        boolean result2 = caverphone.isCaverphoneEqual(str3, str4);
        assertEquals(expected2, result2);
        
        // Test case 3
        String str5 = "testing";
        String str6 = "Testing";
        boolean expected3 = true;
        boolean result3 = caverphone.isCaverphoneEqual(str5, str6);
        assertEquals(expected3, result3);
    }
}