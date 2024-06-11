import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Basic test case
        String result = caverphone.caverphone("hello");
        assertEquals("HLL1111111", result);
        
        // Edge cases and exception handling
        // Test with null input
        result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
        
        // Test with empty string input
        result = caverphone.caverphone("");
        assertEquals("1111111111", result);
        
        // Test with special characters in input
        result = caverphone.caverphone("!@#$%^&*()");
        assertEquals("1111111111", result);
        
        // Test with different cases in input
        result = caverphone.caverphone("hElLo");
        assertEquals("HLL1111111", result);
        
        // Test with all valid characters in input
        result = caverphone.caverphone("abcdefghijklmnopqrstuvwxyz");
        assertEquals("1111111111", result);
        
        // Test with start options
        result = caverphone.caverphone("cough");
        assertEquals("C2F1111111", result);
        
        result = caverphone.caverphone("rough");
        assertEquals("R2F1111111", result);
        
        result = caverphone.caverphone("tough");
        assertEquals("T2F1111111", result);
        
        result = caverphone.caverphone("enough");
        assertEquals("ENOU1111111", result);
        
        result = caverphone.caverphone("trough");
        assertEquals("TROU2F1111", result);
        
        result = caverphone.caverphone("gn");
        assertEquals("2N11111111", result);
        
        result = caverphone.caverphone("mb");
        assertEquals("M211111111", result);
        
        // Test with replacements
        result = caverphone.caverphone("cq");
        assertEquals("2Q11111111", result);
        
        result = caverphone.caverphone("ci");
        assertEquals("SI11111111", result);
        
        result = caverphone.caverphone("ce");
        assertEquals("SE11111111", result);
        
        result = caverphone.caverphone("cy");
        assertEquals("SY11111111", result);
        
        result = caverphone.caverphone("tch");
        assertEquals("2CH1111111", result);
        
        result = caverphone.caverphone("c");
        assertEquals("K111111111", result);
        
        result = caverphone.caverphone("q");
        assertEquals("K111111111", result);
        
        result = caverphone.caverphone("x");
        assertEquals("K111111111", result);
        
        result = caverphone.caverphone("v");
        assertEquals("F111111111", result);
        
        result = caverphone.caverphone("dg");
        assertEquals("2G11111111", result);
        
        result = caverphone.caverphone("tio");
        assertEquals("SIO1111111", result);
        
        result = caverphone.caverphone("tia");
        assertEquals("SIA1111111", result);
        
        result = caverphone.caverphone("d");
        assertEquals("T111111111", result);
        
        result = caverphone.caverphone("ph");
        assertEquals("FH11111111", result);
        
        result = caverphone.caverphone("b");
        assertEquals("P111111111", result);
        
        result = caverphone.caverphone("sh");
        assertEquals("S211111111", result);
        
        result = caverphone.caverphone("z");
        assertEquals("S111111111", result);
        
        result = caverphone.caverphone("j");
        assertEquals("Y111111111", result);
        
        result = caverphone.caverphone("y3");
        assertEquals("Y311111111", result);
        
        result = caverphone.caverphone("y");
        assertEquals("3111111111", result);
        
        result = caverphone.caverphone("3gh3");
        assertEquals("3KH3111111", result);
        
        result = caverphone.caverphone("gh");
        assertEquals("2211111111", result);
        
        result = caverphone.caverphone("g");
        assertEquals("K111111111", result);
        
        result = caverphone.caverphone("s+");
        assertEquals("S111111111", result);
        
        result = caverphone.caverphone("t+");
        assertEquals("T111111111", result);
        
        result = caverphone.caverphone("p+");
        assertEquals("P111111111", result);
        
        result = caverphone.caverphone("k+");
        assertEquals("K111111111", result);
        
        result = caverphone.caverphone("f+");
        assertEquals("F111111111", result);
        
        result = caverphone.caverphone("m+");
        assertEquals("M111111111", result);
        
        result = caverphone.caverphone("n+");
        assertEquals("N111111111", result);
        
        result = caverphone.caverphone("w3");
        assertEquals("W311111111", result);
        
        result = caverphone.caverphone("wh3");
        assertEquals("WH31111111", result);
        
        result = caverphone.caverphone("w");
        assertEquals("2111111111", result);
        
        result = caverphone.caverphone("h");
        assertEquals("A111111111", result);
        
        result = caverphone.caverphone("r3");
        assertEquals("R311111111", result);
        
        result = caverphone.caverphone("r");
        assertEquals("2111111111", result);
        
        result = caverphone.caverphone("l3");
        assertEquals("L311111111", result);
        
        result = caverphone.caverphone("l");
        assertEquals("2111111111", result);
        
        // Test with removals
        result = caverphone.caverphone("2");
        assertEquals("1111111111", result);
        
        result = caverphone.caverphone("3$");
        assertEquals("A111111111", result);
        
        result = caverphone.caverphone("3");
        assertEquals("1111111111", result);
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        
        // Basic test case
        String result = caverphone.encode("hello");
        assertEquals("HLL1111111", result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test case with equal caverphones
        boolean result = caverphone.isCaverphoneEqual("hello", "hElLo");
        assertTrue(result);
        
        // Test case with different caverphones
        result = caverphone.isCaverphoneEqual("hello", "world");
        assertFalse(result);
    }

}