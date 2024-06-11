import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.codec.EncoderException;
import org.junit.Before;
import org.junit.Test;

public class CaverphoneTest {
    private Caverphone caverphone;
    
    @Before
    public void setUp() {
        caverphone = new Caverphone();
    }
    
    @Test
    public void testCaverphone() {
        // Empty input should return "1111111111"
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Input with numbers should return "1111111111" as numbers are removed
        assertEquals("1111111111", caverphone.caverphone("12345"));
        
        // Input with special characters should return "1111111111" as non-alphabetic characters are removed
        assertEquals("1111111111", caverphone.caverphone("!@#$%^&*()"));
        
        // Input with mixed case should be converted to lowercase
        assertEquals("1111111111", caverphone.caverphone("cAveRphoNe"));
        
        // Specific replacements should be applied according to the algorithm
        assertEquals("krtfn", caverphone.caverphone("craig"));
        assertEquals("smthsnlk", caverphone.caverphone("smithsonlake"));
        assertEquals("lgtn", caverphone.caverphone("leighton"));
        
        // The first ten characters should be taken as the code
        assertEquals("1111111111", caverphone.caverphone("thisisaverylongword"));
        assertEquals("knghtsbrdg", caverphone.caverphone("knightsbridge"));
    }
    
    @Test(expected = EncoderException.class)
    public void testEncode_ObjectNotString() throws EncoderException {
        caverphone.encode(new Object());
    }
    
    @Test
    public void testEncode() {
        // Encoding a string should call the caverphone method
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("smthsnlk", caverphone.encode("smithsonlake"));
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual("abc", "ABC"));
        assertFalse(caverphone.isCaverphoneEqual("abc", "def"));
    }
}