import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Test empty string
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Test lowercase letters
        assertEquals("km32", caverphone.caverphone("Km"));
        
        // Test removing non-alphabetic characters
        assertEquals("432", caverphone.caverphone("abc#$%^"));
        
        // Other test cases
        assertEquals("k32f", caverphone.caverphone("cough"));
        assertEquals("r246f", caverphone.caverphone("rough"));
        assertEquals("t248f", caverphone.caverphone("tough"));
        assertEquals("n24f", caverphone.caverphone("enough"));
        assertEquals("t24f", caverphone.caverphone("trough"));
        assertEquals("43n", caverphone.caverphone("gn"));
        assertEquals("m2", caverphone.caverphone("mb"));
        
        // More test cases
        assertEquals("2q", caverphone.caverphone("cq"));
        assertEquals("si", caverphone.caverphone("ci"));
        assertEquals("se", caverphone.caverphone("ce"));
        assertEquals("sy", caverphone.caverphone("cy"));
        assertEquals("2ch", caverphone.caverphone("tch"));
        assertEquals("k", caverphone.caverphone("c"));
        assertEquals("k", caverphone.caverphone("q"));
        assertEquals("k", caverphone.caverphone("x"));
        assertEquals("f", caverphone.caverphone("v"));
        assertEquals("2g", caverphone.caverphone("dg"));
        assertEquals("sio", caverphone.caverphone("tio"));
        assertEquals("sia", caverphone.caverphone("tia"));
        assertEquals("t", caverphone.caverphone("d"));
        assertEquals("fh", caverphone.caverphone("ph"));
        assertEquals("p", caverphone.caverphone("b"));
        assertEquals("s2", caverphone.caverphone("sh"));
        assertEquals("s", caverphone.caverphone("z"));
        assertEquals("A", caverphone.caverphone("a"));
        assertEquals("3", caverphone.caverphone("e"));
        assertEquals("y", caverphone.caverphone("j"));
        assertEquals("Y3", caverphone.caverphone("y3"));
        assertEquals("A", caverphone.caverphone("y"));
        assertEquals("3", caverphone.caverphone("y"));
        assertEquals("3kh3", caverphone.caverphone("3gh3"));
        assertEquals("22", caverphone.caverphone("gh"));
        assertEquals("k", caverphone.caverphone("g"));
        assertEquals("S", caverphone.caverphone("s"));
        assertEquals("T", caverphone.caverphone("t"));
        assertEquals("P", caverphone.caverphone("p"));
        assertEquals("K", caverphone.caverphone("k"));
        assertEquals("F", caverphone.caverphone("f"));
        assertEquals("M", caverphone.caverphone("m"));
        assertEquals("N", caverphone.caverphone("n"));
        assertEquals("W3", caverphone.caverphone("w3"));
        assertEquals("Wh3", caverphone.caverphone("wh3"));
        assertEquals("3", caverphone.caverphone("w"));
        assertEquals("A", caverphone.caverphone("h"));
        assertEquals("2", caverphone.caverphone("r"));
        assertEquals("3", caverphone.caverphone("r"));
        assertEquals("2", caverphone.caverphone("l"));
        assertEquals("3", caverphone.caverphone("l"));
        assertEquals("A", caverphone.caverphone("h"));
        assertEquals("632f", caverphone.caverphone("why"));
        assertEquals("2", caverphone.caverphone("w"));
        assertEquals("3", caverphone.caverphone("w"));
        
        // Test maximum length of 10 characters
        assertEquals("k32ftskcrk", caverphone.caverphone("caverphone"));
    }
    
    @Test(expected=EncoderException.class)
    public void testEncode_ObjectNotString() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }
    
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("km32", caverphone.encode("Km"));
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        assertTrue(caverphone.isCaverphoneEqual("caverphone", "caverphone"));
        assertFalse(caverphone.isCaverphoneEqual("caverphone", "caverphones"));
    }

}