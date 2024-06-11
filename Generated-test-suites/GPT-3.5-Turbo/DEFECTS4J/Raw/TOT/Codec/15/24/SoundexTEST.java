import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Before;
import org.junit.Test;

public class SoundexTest {

    private Soundex soundex;

    @Before
    public void setup() {
        soundex = new Soundex();
    }

    @Test
    public void testEncode_WithValidString_ShouldReturnEncodedString() {
        String encoded = soundex.encode("Apache Commons"); // typical use case
        
        assertEquals("A120", encoded);
    }
    
    @Test
    public void testEncode_WithEmptyString_ShouldReturnEmptyString() {
        String encoded = soundex.encode(""); // edge case
        
        assertEquals("", encoded);
    }
    
    @Test
    public void testEncode_WithNullString_ShouldReturnNull() {
        String encoded = soundex.encode(null); // edge case
        
        assertNull(encoded);
    }
    
    @Test
    public void testEncode_WithNonAlphabeticString_ShouldThrowEncoderException() {
        try {
            soundex.encode("123"); // error scenario
            fail("EncoderException not thrown");
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Soundex encode is not of type java.lang.String", e.getMessage());
        }
    }
    
    @Test
    public void testDifference_WithSimilarStrings_ShouldReturnSimilarityCount() throws EncoderException {
        int similarity = soundex.difference("Apache Commons", "Apache Commonz"); // typical use case
        
        assertEquals(3, similarity);
    }
    
    @Test
    public void testDifference_WithDifferentStrings_ShouldReturnSimilarityCount() throws EncoderException {
        int similarity = soundex.difference("Apache Commons", "Google Guava"); // typical use case
        
        assertEquals(0, similarity);
    }
    
    @Test
    public void testDifference_WithEmptyString_ShouldReturnSimilarityCount() throws EncoderException {
        int similarity = soundex.difference("", ""); // edge case
        
        assertEquals(0, similarity);
    }
    
    @Test
    public void testDifference_WithNullStrings_ShouldThrowEncoderException() throws EncoderException {
        try {
            soundex.difference(null, null); // edge case
            fail("EncoderException not thrown");
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Soundex encode is not of type java.lang.String", e.getMessage());
        }
    }
}