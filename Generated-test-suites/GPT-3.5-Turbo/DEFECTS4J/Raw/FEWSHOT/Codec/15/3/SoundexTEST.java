import static org.junit.Assert.*;

public class SoundexTest {
    
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        
        // Test cases with US_ENGLISH_MAPPING_STRING
        assertEquals("S530", soundex.soundex("Smith"));
        assertEquals("A016", soundex.soundex("Adams"));
        assertEquals("A330", soundex.soundex("Anderson"));
        assertEquals("J250", soundex.soundex("Johnson"));
        assertEquals("E251", soundex.soundex("Ellison"));
        assertEquals("R163", soundex.soundex("Roberts"));
        
        // Test cases with custom mapping
        char[] customMapping = "012301230123".toCharArray();
        Soundex soundexCustom = new Soundex(customMapping);
        assertEquals("S10", soundexCustom.soundex("Smith"));
        assertEquals("A00", soundexCustom.soundex("Adams"));
        assertEquals("A20", soundexCustom.soundex("Anderson"));
        assertEquals("J02", soundexCustom.soundex("Johnson"));
        assertEquals("E13", soundexCustom.soundex("Ellison"));
        assertEquals("R01", soundexCustom.soundex("Roberts"));
        
        // Test case with empty string
        assertEquals("", soundex.soundex(""));
        assertEquals("", soundexCustom.soundex(""));
        
        // Test case with null string
        assertNull(soundex.soundex(null));
        assertNull(soundexCustom.soundex(null));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        
        // Test cases with US_ENGLISH_MAPPING_STRING
        assertEquals(0, soundex.difference("Smith", "Jones"));
        assertEquals(4, soundex.difference("Adams", "Addams"));
        assertEquals(1, soundex.difference("Anderson", "Andersen"));
        assertEquals(3, soundex.difference("Johnson", "Johnsons"));
        assertEquals(2, soundex.difference("Ellison", "Ellision"));
        assertEquals(2, soundex.difference("Roberts", "Robinson"));
        
        // Test cases with custom mapping
        char[] customMapping = "012301230123".toCharArray();
        Soundex soundexCustom = new Soundex(customMapping);
        assertEquals(0, soundexCustom.difference("Smith", "Jones"));
        assertEquals(1, soundexCustom.difference("Adams", "Addams"));
        assertEquals(2, soundexCustom.difference("Anderson", "Andersen"));
        assertEquals(3, soundexCustom.difference("Johnson", "Johnsons"));
        assertEquals(3, soundexCustom.difference("Ellison", "Ellision"));
        assertEquals(4, soundexCustom.difference("Roberts", "Robinson"));
        
        // Test case with empty strings
        assertEquals(0, soundex.difference("", ""));
        assertEquals(0, soundexCustom.difference("", ""));
        
        // Test case with null strings
        try {
            soundex.difference(null, "Smith");
            fail("Should throw EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
        
        try {
            soundexCustom.difference("Adams", null);
            fail("Should throw EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
    }
}