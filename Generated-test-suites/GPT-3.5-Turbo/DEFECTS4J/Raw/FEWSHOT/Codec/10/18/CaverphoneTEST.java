import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CaverphoneTest {
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Case 1: txt is null
        assertEquals("1111111111", caverphone.caverphone(null));
        
        // Case 2: txt is empty
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Case 3: txt contains non-alphabet characters
        assertEquals("cough", caverphone.caverphone("cough2!@#$%")); 
        assertEquals("rough", caverphone.caverphone("rou2f*(`-+"));
        assertEquals("tough", caverphone.caverphone("tou `~!@#$%fh")); 
        assertEquals("enough", caverphone.caverphone("enou22@##~fh")); 
        assertEquals("trough", caverphone.caverphone("t----rou-g-h"));
        assertEquals("gn", caverphone.caverphone("gn")); 
        
        // Case 4: txt starts with consonants
        assertEquals("coufk", caverphone.caverphone("coufhkas"));
        assertEquals("roufk", caverphone.caverphone("roughkaz"));
        assertEquals("toufk", caverphone.caverphone("toughkaz"));
        assertEquals("enoufk", caverphone.caverphone("enoughkaz"));
        assertEquals("troufk", caverphone.caverphone("troughkaz"));
        assertEquals("2nk", caverphone.caverphone("gnkaz")); 
        
        // Case 5: txt starts with vowel
        assertEquals("Akfk", caverphone.caverphone("ahkas"));
        assertEquals("Akfk", caverphone.caverphone("ehkaz"));
        assertEquals("Akfk", caverphone.caverphone("ihkaz"));
        assertEquals("Akfk", caverphone.caverphone("ohkaz"));
        assertEquals("Akfk", caverphone.caverphone("uhkaz"));
        
        // Case 6: txt contains replacements
        assertEquals("sk2ft", caverphone.caverphone("soft"));
        assertEquals("kake", caverphone.caverphone("cake"));
        assertEquals("seke", caverphone.caverphone("ceceke"));
        assertEquals("sy", caverphone.caverphone("cy"));
        assertEquals("2ch", caverphone.caverphone("tch"));
        assertEquals("kk", caverphone.caverphone("cc"));
        assertEquals("kk", caverphone.caverphone("qq"));    
        assertEquals("kk", caverphone.caverphone("xx"));
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
        assertEquals("3", caverphone.caverphone("i"));
        assertEquals("3", caverphone.caverphone("o"));
        assertEquals("3", caverphone.caverphone("u"));
        assertEquals("y", caverphone.caverphone("j"));
        assertEquals("Y3", caverphone.caverphone("y"));
        assertEquals("A", caverphone.caverphone("y3"));
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
        assertEquals("2", caverphone.caverphone("w"));
        assertEquals("A", caverphone.caverphone("h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r3"));
        assertEquals("3", caverphone.caverphone("r"));
        assertEquals("L3", caverphone.caverphone("l3"));
        assertEquals("3", caverphone.caverphone("l"));
        assertEquals("2", caverphone.caverphone("l"));
        
        // Case 7: txt contains removals
        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3")); 
        
        // Case 8: txt ends with "w"
        assertEquals("A3", caverphone.caverphone("wy"));
        assertEquals("A", caverphone.caverphone("w")); 
        
        // Case 9: txt ends with consonant
        assertEquals("atakA30000", caverphone.caverphone("atakas"));
        
        // Case 10: txt ends with vowel
        assertEquals("Akf", caverphone.caverphone("ahk"));
        assertEquals("Akf", caverphone.caverphone("ehk"));
        assertEquals("Akf", caverphone.caverphone("ihk"));
        assertEquals("Akf", caverphone.caverphone("ohk"));
        assertEquals("Akf", caverphone.caverphone("uhk"));       
    }
    
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Case 1: pObject is not a String
        try {
            caverphone.encode(123);
            assertFalse("Should have thrown EncoderException", true);
        } catch (EncoderException e) {
            assertTrue(true);
        }
        
        // Case 2: pObject is a String
        String encodedString = caverphone.encode("example");
        assertEquals(caverphone.caverphone("example"), encodedString);
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Case 1: caverphones are equal
        assertTrue(caverphone.isCaverphoneEqual("example1", "example1"));
        assertTrue(caverphone.isCaverphoneEqual("example2", "example2"));
        
        // Case 2: caverphones are not equal
        assertFalse(caverphone.isCaverphoneEqual("example3", "example4"));
        assertFalse(caverphone.isCaverphoneEqual("example5", "example6"));
    }
    
}