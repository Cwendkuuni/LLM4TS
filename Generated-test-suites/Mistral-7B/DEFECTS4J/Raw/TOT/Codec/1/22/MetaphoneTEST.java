package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * MetaphoneTest class.
 */
public class MetaphoneTest {

    /**
     * Test method for {@link Metaphone#metaphone(String)}
     */
    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        String testString1 = "Banana";
        String testString2 = "Apple";
        String testString3 = "Kangaroo";
        String testString4 = "Metaphone Algorithm Test";
        
        // Test with typical use cases
        assertEquals("BA", metaphone.metaphone(testString1));
        assertEquals("AE PP", metaphone.metaphone(testString2));
        assertEquals("KN GR", metaphone.metaphone(testString3));
        
        // Test with edge cases
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("", metaphone.metaphone(""));
        assertEquals("A", metaphone.metaphone("Aaa"));
        assertEquals("AE", metaphone.metaphone("AeioU"));
        assertEquals("AE PP", metaphone.metaphone("Apple"));
        assertEquals("KN G", metaphone.metaphone("Kang"));
        assertEquals("A T", metaphone.metaphone("At"));
        
        // Test with error scenarios
        try {
            metaphone.metaphone(null);
            fail("Expected EncoderException for null input");
        } catch (EncoderException e) {
            // Expected
        }
        try {
            metaphone.metaphone("");
            fail("Expected EncoderException for empty input");
        } catch (EncoderException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link Metaphone#isMetaphoneEqual(String, String)}
     */
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        String testString1 = "Banana";
        String testString2 = "Apple";
        String testString3 = "Banana";
        String testString4 = "Kangaroo";
        String testString5 = "Apple";

        assertTrue(metaphone.isMetaphoneEqual(testString1, testString3));
        assertFalse(metaphone.isMetaphoneEqual(testString1, testString2));
        assertFalse(metaphone.isMetaphoneEqual(testString1, testString4));
        assertFalse(metaphone.isMetaphoneEqual(testString2, testString3));
        assertTrue(metaphone.isMetaphoneEqual(testString2, testString5));
    }

    /**
     * Test method for {@link Metaphone#encode(String)}
     */
    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        String testString1 = "Banana";
        String testString2 = "Apple";
        String testString3 = "Kangaroo";
        String testString4 = "Metaphone Algorithm Test";

        assertEquals("BA", metaphone.encode(testString1));
        assertEquals("AE PP", metaphone.encode(testString2));
        assertEquals("KN G", metaphone.encode(testString3));
        assertEquals("M T", metaphone.encode(testString4));
    }
}