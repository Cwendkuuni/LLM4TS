package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;

import org.junit.Test;

/**
 * Test class for {@link Caverphone}
 */
public class CaverphoneTest {

    /**
     * Test instantiating new {@link Caverphone}
     */
    @Test
    public void testInstantiation() {
        Caverphone caverphone = new Caverphone();
        assertNotNull(caverphone);
    }

    /**
     * Test encoding a null String
     */
    @Test(expected = NullPointerException.class)
    public void testNullString() {
        Caverphone caverphone = new Caverphone();
        caverphone.caverphone(null);
    }

    /**
     * Test encoding an empty String
     */
    @Test
    public void testEmptyString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    /**
     * Test encoding a String with only A-Z characters
     */
    @Test
    public void testStringWithOnlyLetters() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A123TKFMSP", caverphone.caverphone("The quick brown fox jumps over the lazy dog"));
    }

    /**
     * Test encoding a String with letters and numbers
     */
    @Test
    public void testStringWithLettersAndNumbers() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A123TKFMSP111111", caverphone.caverphone("The quick brown fox jumps over the lazy dog 12345"));
    }

    /**
     * Test encoding a String with special characters
     */
    @Test
    public void testStringWithSpecialCharacters() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A2F2M2N3P2W3L3", caverphone.caverphone("The qu!ck brown fox jumps o!ver the lazy d#g"));
    }

    /**
     * Test encoding a String with multiple occurrences of the same letter
     */
    @Test
    public void testStringWithMultipleOccurrencesOfTheSameLetter() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A1M1S1F1M1S1F1M1S1", caverphone.caverphone("The quick brown fox jumps over the lazy fox"));
    }

    /**
     * Test encoding a String with the same code as another String
     */
    @Test
    public void testSameStringsHaveTheSameCode() {
        Caverphone caverphone = new Caverphone();
        assertEquals(caverphone.caverphone("The quick brown fox jumps over the lazy dog"), caverphone.caverphone("The quick brown fox jumps over the lazy dog"));
    }

    /**
     * Test encoding two different Strings with different codes
     */
    @Test
    public void testDifferentStringsHaveDifferentCodes() {
        Caverphone caverphone = new Caverphone();
        assertFalse(caverphone.isCaverphoneEqual("The quick brown fox jumps over the lazy dog", "The quick brown fox jumps over the cat"));
    }

    /**
     * Test encoding a String with the start option "enough"
     */
    @Test
    public void testStartOptionEnough() {
        Caverphone caverphone = new Caverphone();
        assertEquals("enou2f111111", caverphone.caverphone("The quick brown fox jumps over the enough dog"));
    }

    /**
     * Test encoding a String with the start options "enough" and "trough"
     */
    @Test
    public void testStartOptionsEnoughAndTrough() {
        Caverphone caverphone = new Caverphone();
        assertEquals("enou2ftrou2f111111", caverphone.caverphone("The quick brown fox jumps over the enough trough dog"));
    }

    /**
     * Test encoding a String with the start options "enough" and the "y" character
     */
    @Test
    public void testStartOptionsEnoughAndY() {
        Caverphone caverphone = new Caverphone();
        assertEquals("enou2fA111111", caverphone.caverphone("The quick brown fox jumps over the enough y dog"));
    }

    /**
     * Test encoding a String with the start option "y3"
     */
    @Test
    public void testStartOptionY3() {
        Caverphone caverphone = new Caverphone();
        assertEquals("Y3111111111", caverphone.caverphone("The quick brown fox jumps over the y3 dog"));
    }

    /**
     * Test encoding a String with the start option "y"
     */
    @Test
    public void testStartOptionY() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A1111111111", caverphone.caverphone("The quick brown fox jumps over the y dog"));
    }

    /**
     * Test encoding a String with the "j" character
     */
    @Test
    public void testStringWithJCharacter() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A123TKFMSP2y111111", caverphone.caverphone("The quick brown fox jumps over the lazy jaguar"));
    }

    /**
     * Test encoding a String with the "wy" sequence
     */
    @Test
    public void testStringWithWySequence() {
        Caverphone caverphone = new Caverphone();
        assertEquals("Why111111", caverphone.caverphone("The quick brown fox jumps over the why dog"));
    }

    /**
     * Test encoding a String with the "why" sequence
     */
    @Test
    public void testStringWithWhySequence() {
        Caverphone caverphone = new Caverphone();
        assertEquals("Why111111", caverphone.caverphone("The quick brown fox jumps over the why dog"));
    }

    /**
     * Test encoding a String with the "wh3" sequence
     */
    @Test
    public void testStringWithWh3Sequence() {
        Caverphone caverphone = new Caverphone();
        assertEquals("Wh3111111111", caverphone.caverphone("The quick brown fox jumps over the wh3 dog"));
    }

    /**
     * Test encoding a String with the "wh" sequence
     */
    @Test
    public void testStringWithWhSequence() {
        Caverphone caverphone = new Caverphone();
        assertEquals("Wh1111111111", caverphone.caverphone("The quick brown fox jumps over the wh dog"));
    }

    /**
     * Test encoding a String with the "h" character
     */
    @Test
    public void testStringWithHCharacter() {
        Caverphone caverphone = new Caverphone();
        assertEquals("HA111111", caverphone.caverphone("The quick brown fox jumps over the lazy hog"));
    }

    /**
     * Test encoding a String with the "H" character
     */
    @Test
    public void testStringWithHUpperCaseCharacter() {
        Caverphone caverphone = new Caverphone();
        assertEquals("HA111111", caverphone.caverphone("The quick brown fox jumps over the lazy Hog"));
    }

    /**
     * Test encoding a String with the "r3" sequence
     */
    @Test
    public void testStringWithR3Sequence() {
        Caverphone caverphone = new Caverphone();
        assertEquals("R3111111111", caverphone.caverphone("The quick brown fox jumps over the r3 dog"));
    }

    /**
     * Test encoding a String with the "r" character
     */
    @Test
    public void testStringWithRCharacter() {
        Caverphone caverphone = new Caverphone();
        assertEquals("R2111111111", caverphone.caverphone("The quick brown fox jumps over the lazy dog"));
    }

    /**
     * Test encoding a String with the "l3" sequence
     */
    @Test
    public void testStringWithL3Sequence() {
        Caverphone caverphone = new Caverphone();
        assertEquals("L3111111111", caverphone.caverphone("The quick brown fox jumps over the l3 dog"));
    }

    /**
     * Test encoding a String with the "l" character
     */
    @Test
    public void testStringWithLCharacter() {
        Caverphone caverphone = new Caverphone();
        assertEquals("L2111111111", caverphone.caverphone("The quick brown fox jumps over the lazy dog"));
    }

    /**
     * Test encoding a String with the "w" character
     */
    @Test
    public void testStringWithWCharacter() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2111111111", caverphone.caverphone("The quick brown fox jumps over the lazy dog"));
    }

    /**
     * Test encoding a String with the "W3" sequence
     */
    @Test
    public void testStringWithW3Sequence() {
        Caverphone caverphone = new Caverphone();
        assertEquals("W3111111111", caverphone.caverphone("The quick brown fox jumps over the w3 dog"));
    }
}