import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Test with empty string
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Test with lowercase string
        assertEquals("kfr2n", caverphone.caverphone("cavern"));
        
        // Test with uppercase string
        assertEquals("kfr2n", caverphone.caverphone("CAVERN"));
        
        // Test with string containing non-alphabetic characters
        assertEquals("r2m", caverphone.caverphone("123Caverphone"));
        
        // Test with string containing only vowels
        assertEquals("AAAAAA", caverphone.caverphone("aeiou"));
        
        // Test with string containing only consonants
        assertEquals("KKKKKKKTFM", caverphone.caverphone("bcdfghjklmnpqrstvwxyz"));
        
        // Test with string containing special start options
        assertEquals("kfr2n", caverphone.caverphone("coughern"));
        
        // Test with string containing replacements
        assertEquals("plKTs", caverphone.caverphone("plagues"));
        
        // Test with string containing removals
        assertEquals("plkt", caverphone.caverphone("plague"));
        
        // Test with string length less than 10
        assertEquals("plKTs", caverphone.caverphone("plague s"));  
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Test with empty string
        assertEquals("1111111111", caverphone.encode(""));
        
        // Test with lowercase string
        assertEquals("kfr2n", caverphone.encode("cavern"));
        
        // Test with uppercase string
        assertEquals("kfr2n", caverphone.encode("CAVERN"));
        
        // Test with string containing non-alphabetic characters
        assertEquals("r2m", caverphone.encode("123Caverphone"));
        
        // Test with string containing only vowels
        assertEquals("AAAAAA", caverphone.encode("aeiou"));
        
        // Test with string containing only consonants
        assertEquals("KKKKKKKTFM", caverphone.encode("bcdfghjklmnpqrstvwxyz"));
        
        // Test with string containing special start options
        assertEquals("kfr2n", caverphone.encode("coughern"));
        
        // Test with string containing replacements
        assertEquals("plKTs", caverphone.encode("plagues"));
        
        // Test with string containing removals
        assertEquals("plkt", caverphone.encode("plague"));
        
        // Test with string length less than 10
        assertEquals("plKTs", caverphone.encode("plague s"));  
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test with equal strings
        assertTrue(caverphone.isCaverphoneEqual("cavern", "cavern"));
        
        // Test with different strings
        assertFalse(caverphone.isCaverphoneEqual("cavern", "plague"));
    }
}