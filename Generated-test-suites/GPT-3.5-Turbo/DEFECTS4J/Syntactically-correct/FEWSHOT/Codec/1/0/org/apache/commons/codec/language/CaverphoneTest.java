import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Test;

public class CaverphoneTest {
    
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Testing empty string
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Testing lowercase input
        assertEquals("K1111", caverphone.caverphone("k"));
        assertEquals("KA111", caverphone.caverphone("ka"));
        assertEquals("K31311", caverphone.caverphone("kaaooaa"));
        
        // Testing uppercase input
        assertEquals("K1111", caverphone.caverphone("K"));
        assertEquals("KA111", caverphone.caverphone("KA"));
        assertEquals("K31311", caverphone.caverphone("KAAOOAA"));
        
        // Testing mixed case input
        assertEquals("K1111", caverphone.caverphone("k"));
        assertEquals("KA111", caverphone.caverphone("Ka"));
        assertEquals("K31311", caverphone.caverphone("kAaOoAa"));
        
        // Testing removing non A-Z characters
        assertEquals("K31311", caverphone.caverphone("kA!@#$%^&*(a)"));
        
        // Testing start options
        assertEquals("caarf", caverphone.caverphone("coughaarf"));
        assertEquals("r2faaf", caverphone.caverphone("roughaaf"));
        assertEquals("t2akaaf", caverphone.caverphone("toughaaf"));
        assertEquals("enou2faaf", caverphone.caverphone("enoughaaf"));
        assertEquals("trou2foug", caverphone.caverphone("troughfough"));
        assertEquals("2naaf", caverphone.caverphone("gnaaf"));
        assertEquals("maaf", caverphone.caverphone("mbaaf"));
        
        // Testing replacements
        assertEquals("ki2", caverphone.caverphone("cqi"));
        assertEquals("si", caverphone.caverphone("ci"));
        assertEquals("se", caverphone.caverphone("ce"));
        assertEquals("sy", caverphone.caverphone("cy"));
        assertEquals("2choo", caverphone.caverphone("tchoo"));
        assertEquals("k", caverphone.caverphone("c"));
        assertEquals("k", caverphone.caverphone("q"));
        assertEquals("k", caverphone.caverphone("x"));
        assertEquals("f", caverphone.caverphone("v"));
        assertEquals("2g", caverphone.caverphone("dg"));
        assertEquals("sioo", caverphone.caverphone("tioo"));
        assertEquals("siaa", caverphone.caverphone("tiaa"));
        assertEquals("t", caverphone.caverphone("d"));
        assertEquals("fh", caverphone.caverphone("ph"));
        assertEquals("p", caverphone.caverphone("b"));
        assertEquals("s2", caverphone.caverphone("sh"));
        assertEquals("s", caverphone.caverphone("z"));
        assertEquals("Ap", caverphone.caverphone("ap"));
        assertEquals("3p", caverphone.caverphone("ep"));
        assertEquals("3p", caverphone.caverphone("ip"));
        assertEquals("3p", caverphone.caverphone("op"));
        assertEquals("3p", caverphone.caverphone("up"));
        assertEquals("3p", caverphone.caverphone("wp"));
        assertEquals("ki", caverphone.caverphone("ki"));
        assertEquals("3g3", caverphone.caverphone("3gh3"));
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
        assertEquals("2A", caverphone.caverphone("h"));
        assertEquals("2", caverphone.caverphone("r3"));
        assertEquals("2", caverphone.caverphone("r"));
        assertEquals("2", caverphone.caverphone("l3"));
        assertEquals("2", caverphone.caverphone("l"));
        
        // Testing removals
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3"));
        
        // Testing truncation
        assertEquals("K1111", caverphone.caverphone("kAAAAAAA"));
        
        // Testing complex input
        assertEquals("31MK", caverphone.caverphone("3m3itmonk"));
        assertEquals("T2KA", caverphone.caverphone("tOughKa"));
        assertEquals("WH3MBLEY", caverphone.caverphone("wh310mbLEy"));
    }
    
    @Test
    public void testEncode() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        
        // Testing empty string
        assertEquals("1111111111", caverphone.encode(""));
        
        // Testing encoding
        assertEquals("K1111", caverphone.encode("k"));
        assertEquals("KA111", caverphone.encode("ka"));
        assertEquals("K31311", caverphone.encode("kaaooaa"));
        
        // Testing exception for non-string input
        boolean exceptionThrown = false;
        try {
            caverphone.encode(123);
        } catch (EncoderException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Testing equality
        assertTrue(caverphone.isCaverphoneEqual("k", "K"));
        assertEquals(caverphone.caverphone("k"), caverphone.caverphone("K"));
        
        // Testing inequality
        assertFalse(caverphone.isCaverphoneEqual("k", "ka"));
        assertNotEquals(caverphone.caverphone("k"), caverphone.caverphone("ka"));
    }
}