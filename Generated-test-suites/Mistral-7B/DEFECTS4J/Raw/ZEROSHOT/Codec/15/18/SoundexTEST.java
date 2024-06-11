package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING_STRING;
import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING;

/**
 * Test class for Soundex class.
 */
public class SoundexTest {

    @Test
    public void testUS_ENGLISH_MAPPING() {
        // Test US_ENGLISH_MAPPING constant
        String usEnglishMappingString = US_ENGLISH_MAPPING_STRING;
        assertEquals(usEnglishMappingString, new String(US_ENGLISH_MAPPING));
    }

    @Test
    public void testUS_ENGLISH_SoundexInstance() {
        // Test US_ENGLISH Soundex instance
        Soundex soundexUSEnglish = Soundex.US_ENGLISH;
        assertNotNull(soundexUSEnglish);
        assertEquals(US_ENGLISH_MAPPING, soundexUSEnglish.getSoundexMapping());
    }

    @Test
    public void testCustomSoundexInstance() {
        // Test custom Soundex instance
        char[] customMapping = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
        };
        Soundex soundexCustom = new Soundex(customMapping);
        assertNotSame(US_ENGLISH_MAPPING, soundexCustom.getSoundexMapping());
    }

    @Test
    public void testStringDifference() throws EncoderException {
        // Test String difference
        Soundex soundex = new Soundex();

        String str1 = "John Smith";
        String str2 = "Jane Doe";
        String str3 = "John Doe";
        String str4 = "John Smith";
        String str5 = "SmitH, John";

        int difference = soundex.difference(str1, str2);
        assertEquals(0, difference);

        difference = soundex.difference(str2, str3);
        assertEquals(3, difference);

        difference = soundex.difference(str1, str3);
        assertEquals(3, difference);

        difference = soundex.difference(str1, str4);
        assertEquals(4, difference);

        difference = soundex.difference(str1, str5);
        assertEquals(3, difference);
    }

    @Test
    public void testEncode() throws EncoderException {
        // Test String encode
        Soundex soundex = new Soundex();

        String str1 = "John Smith";
        String soundexCode1 = soundex.encode(str1);
        assertEquals("JS000", soundexCode1);

        String str2 = "Jane Doe";
        String soundexCode2 = soundex.encode(str2);
        assertEquals("JN000", soundexCode2);

        String str3 = "SmitH, John";
        String soundexCode3 = soundex.encode(str3);
        assertEquals("SM000", soundexCode3);

        // Test null input
        try {
            soundex.encode(null);
            fail("Should have thrown EncoderException for null input");
        } catch (EncoderException e) {
            // expected
        }
    }

    @Test
    public void testSoundexString() {
        // Test soundex method for String
        Soundex soundex = new Soundex();

        String str1 = "John Smith";
        String soundexCode1 = soundex.soundex(str1);
        assertEquals("JS000", soundexCode1);

        String str2 = "Jane Doe";
        String soundexCode2 = soundex.soundex(str2);
        assertEquals("JN000", soundexCode2);

        String str3 = "SmitH, John";
        String soundexCode3 = soundex.soundex(str3);
        assertEquals("SM000", soundexCode3);
    }

    @Test
    public void testGetMappingCode() {
        // Test getMappingCode method
        Soundex soundex = new Soundex();

        String str = "John Smith";

        char code1 = soundex.getMappingCode(str, 0);
        assertEquals('J', code1);
        assertEquals('J', soundex.map('J'));

        code1 = soundex.getMappingCode(str, 1);
        assertEquals('S', code1);
        assertEquals('S', soundex.map('S'));

        code1 = soundex.getMappingCode(str, 2);
        assertEquals('M', code1);
        assertEquals('M', soundex.map('M'));

        code1 = soundex.getMappingCode(str, 3);
        assertEquals('ITH', soundex.getMappingCode(str, 4).toString());
        assertEquals('I', soundex.map('I'));
        assertEquals('T', soundex.map('H'));
        assertEquals('H', soundex.map('TH'));
    }
}