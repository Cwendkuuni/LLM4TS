import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Test empty string
        assertEquals("1111111111", caverphone.caverphone(""));

        // Test lowercase conversion
        assertEquals("1111111111", caverphone.caverphone("ABCDEFG"));

        // Test removing non-alphabetic characters
        assertEquals("bcdfghjklmnprstvw3y3", caverphone.caverphone("1234#@!$%^&*()"));

        //Test replacing specified patterns
        assertEquals("k3", caverphone.caverphone("c"));
        assertEquals("k3", caverphone.caverphone("q"));
        assertEquals("k3", caverphone.caverphone("x"));
        assertEquals("fk", caverphone.caverphone("v"));
        assertEquals("dt", caverphone.caverphone("dg"));
        assertEquals("sio", caverphone.caverphone("tio"));
        assertEquals("sia", caverphone.caverphone("tia"));
        assertEquals("t", caverphone.caverphone("d"));
        assertEquals("f3", caverphone.caverphone("ph"));
        assertEquals("p", caverphone.caverphone("b"));
        assertEquals("s2", caverphone.caverphone("sh"));
        assertEquals("s", caverphone.caverphone("z"));
        assertEquals("A3", caverphone.caverphone("a"));
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
        assertEquals("2", caverphone.caverphone("w"));
        assertEquals("A", caverphone.caverphone("h"));
        assertEquals("2", caverphone.caverphone("r"));
        assertEquals("2", caverphone.caverphone("l"));

        // Test removal of specified characters
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3"));
        
        // Test final encoded string
        assertEquals("bcdfghjklmnprstvw3y3", caverphone.caverphone("abcdefghi1234#@!$%^&*()"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Test encoding an object that is not a String
        try {
            caverphone.encode(1234);
            fail("EncoderException should have been thrown");
        } catch (EncoderException e) {
            // Exception expected
        }

        // Test encoding a string
        assertEquals("bcdfghjklmnprstvw3y3", caverphone.encode("abcdefghi1234#@!$%^&*()"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Test identical caverphones
        assertTrue(caverphone.isCaverphoneEqual("abcdefghi1234#@!$%^&*()", "abcdefghi1234#@!$%^&*()"));

        // Test different caverphones
        assertFalse(caverphone.isCaverphoneEqual("abcdefghi1234#@!$%^&*()", "1234#@!$%^&*()abcdefghi"));
    }

}