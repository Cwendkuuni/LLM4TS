import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use cases
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("shkkhprt", caverphone.caverphone("SCHMIDT"));
        
        // Edge cases
        assertEquals("aaaaaa1111", caverphone.caverphone("aaaaaa")); // txt length < 6
        assertEquals("wwwwwwwwww", caverphone.caverphone("y")); // txt length = 1
        assertEquals("1212121212", caverphone.caverphone("bear")); // txt length > 6
        
        // Error scenarios
        // Remove characters other than A-Z
        assertEquals("aaaaaa1111", caverphone.caverphone("123*** aa #$@! aa")); 
        
        // Test replacements
        assertEquals("aaaaaa1111", caverphone.caverphone("evidence"));
        assertEquals("sehc100000", caverphone.caverphone("cache"));
        assertEquals("sssy3x1000", caverphone.caverphone("syntax"));
        
        // Test handling of start and end options
        assertEquals("co2f111111", caverphone.caverphone("cough"));
        assertEquals("ca2ft11111", caverphone.caverphone("coughed"));
        assertEquals("2nazzzzzzz", caverphone.caverphone("gnats"));
        assertEquals("rm2aaaaaaa", caverphone.caverphone("warm"));
        assertEquals("wh31111111", caverphone.caverphone("white"));
        
        // Test encoding and isCaverphoneEqual methods
        assertTrue(caverphone.isCaverphoneEqual("hello", "hello"));
        assertFalse(caverphone.isCaverphoneEqual("cat", "dog"));
    }
}