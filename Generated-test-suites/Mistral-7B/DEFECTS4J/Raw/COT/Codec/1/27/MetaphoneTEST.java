package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Metaphone test file
 */
public class MetaphoneTest {

    

    @Test
    public void testEncoderInstance() {
        Metaphone encoder = new Metaphone();
        assertNotNull(encoder);
    }

    @Test
    public void testEmptyString() {
        Metaphone encoder = new Metaphone();
        String encoded = encoder.metaphone("");
        assertEquals("", encoded);
    }

    @Test
    public void testSingleCharacter() {
        Metaphone encoder = new Metaphone();
        String singleChar = "A";
        String encoded = encoder.metaphone(singleChar);
        assertEquals(singleChar.toUpperCase(), encoded);
    }

    @Test
    public void testSimpleString() {
        Metaphone encoder = new Metaphone();
        String simpleString = "apple";
        String encoded = encoder.metaphone(simpleString);
        assertEquals("A0P1L2E", encoded);
    }

    @Test
    public void testComplexString() {
        Metaphone encoder = new Metaphone();
        String complexString = "Banana";
        String encoded = encoder.metaphone(complexString);
        assertEquals("A0P1N1A2", encoded);
    }

    @Test
    public void testSpecialCharacters() {
        Metaphone encoder = new Metaphone();
        String stringWithSpecial = "Basketball";
        String encoded = encoder.metaphone(stringWithSpecial);
        assertEquals("B2S3K3T4B4", encoded);
    }

    @Test(expected = EncoderException.class)
    public void testNullString() {
        Metaphone encoder = new Metaphone();
        encoder.metaphone(null);
    }

    @Test(expected = EncoderException.class)
    public void testEmptyArrayString() {
        Metaphone encoder = new Metaphone();
        encoder.metaphone(new String[0]);
    }

    @Test
    public void testEncodeObject() {
        Metaphone encoder = new Metaphone();
        String string = "test";
        assertEquals(encoder.metaphone(string), encoder.encode(string));
    }

    @Test
    public void testIsMetaphoneEqualTrue() {
        Metaphone encoder = new Metaphone();
        String str1 = "test";
        String str2 = "test";
        assertTrue(encoder.isMetaphoneEqual(str1, str2));
    }

    @Test
    public void testIsMetaphoneEqualFalse() {
        Metaphone encoder = new Metaphone();
        String str1 = "test";
        String str2 = "test1";
        assertFalse(encoder.isMetaphoneEqual(str1, str2));
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone encoder = new Metaphone();
        encoder.setMaxCodeLen(5);
        assertEquals(5, encoder.getMaxCodeLen());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxCodeLenLessThanZero() {
        Metaphone encoder = new Metaphone();
        encoder.setMaxCodeLen(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testIsPreviousCharNull() {
        StringBuffer string = new StringBuffer();
        Metaphone encoder = new Metaphone();
        encoder.isPreviousChar(string, -1, 'C');
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIsPreviousCharIndexOutOfBounds() {
        StringBuffer string = new StringBuffer("test");
        Metaphone encoder = new Metaphone();
        encoder.isPreviousChar(string, 4, 'C');
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIsNextCharIndexOutOfBounds() {
        StringBuffer string = new StringBuffer("test");
        Metaphone encoder = new Metaphone();
        encoder.isNextChar(string, 4, 'C');
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testRegionMatchStringIndexOutOfBounds() {
        StringBuffer string = new StringBuffer("test");
        Metaphone encoder = new Metaphone();
        encoder.regionMatch(string, 4, "test");
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testRegionMatchStringNegativeIndex() {
        StringBuffer string = new StringBuffer("test");
        Metaphone encoder = new Metaphone();
        encoder.regionMatch(string, -1, "test");
    }

    
}