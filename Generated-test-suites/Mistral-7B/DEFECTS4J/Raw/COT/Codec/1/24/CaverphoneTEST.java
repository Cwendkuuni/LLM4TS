package org.apache.commons.codec.language;

import static org.junit.Assert.*;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

/**
 * JUnit test cases for the Caverphone class
 */
public class CaverphoneTest {

    /**
     * Test instance creation
     */
    @Test
    public void testInstanceCreation() {
        new Caverphone();
    }

    /**
     * Test caverphone method with null input
     */
    @Test(expected = EncoderException.class)
    public void testCaverphoneNullInput() {
        new Caverphone().caverphone(null);
    }

    /**
     * Test caverphone method with empty input
     */
    @Test
    public void testCaverphoneEmptyInput() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    /**
     * Test basic functionality of caverphone method
     */
    @Test
    public void testBasicCaverphoneFunctionality() {
        Caverphone caverphone = new Caverphone();
        assertEquals("532634262213", caverphone.caverphone("example"));
    }

    /**
     * Test edge case: caverphone method with all lowercase letters
     */
    @Test
    public void testCaverphoneAllLowercase() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2634262213", caverphone.caverphone("example lowercase"));
    }

    /**
     * Test edge case: caverphone method with letters and digits
     */
    @Test
    public void testCaverphoneLettersAndDigits() {
        Caverphone caverphone = new Caverphone();
        assertEquals("263426221311", caverphone.caverphone("example123"));
    }

    /**
     * Test edge case: caverphone method with only digits
     */
    @Test
    public void testCaverphoneOnlyDigits() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone("1234567890"));
    }

    /**
     * Test edge case: caverphone method with special characters
     */
    @Test(expected = EncoderException.class)
    public void testCaverphoneSpecialCharacters() {
        Caverphone caverphone = new Caverphone();
        caverphone.caverphone("example@example.com");
    }
}