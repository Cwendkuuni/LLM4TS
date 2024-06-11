package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class CaverphoneTest {

    private static final String[] EMPTY_ARRAY = new String[0];

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneNullString() {
        Caverphone caverphone = new Caverphone();
        assertNull(caverphone.caverphone(null));
    }

    @Test
    public void testCaverphoneLowerCase() {
        Caverphone caverphone = new Caverphone();

        String[] testCases = {
                "foo",
                "BaR FoO",
                "abCdEfG"
        };

        for (String testCase : testCases) {
            assertEquals("testCaseCa", caverphone.caverphone(testCase));
        }
    }

    @Test
    public void testCaverphoneHandleStartOptions() {
        Caverphone caverphone = new Caverphone();

        String[] testCases = {
                "cough",
                "rough",
                "tough",
                "enough",
                "trough",
                "gn"
        };

        for (String testCase : testCases) {
            assertEquals(caverphone.handleStartOptions(testCase), caverphone.caverphone(testCase).substring(0, 2));
        }
    }

    @Test
    public void testCaverphoneHandleReplacements() {
        Caverphone caverphone = new Caverphone();

        String[] testCases = {
                "cq",
                "ci",
                "ce",
                "cy",
                "tch",
                "c",
                "q",
                "x",
                "v",
                "dg",
                "tio",
                "tia",
                "d",
                "ph",
                "b",
                "sh",
                "z",
                "A",
                "j",
                "y",
                "gh",
                "g",
                "s+",
                "t+",
                "p+",
                "k+",
                "f+",
                "m+",
                "n+",
                "w3",
                "w",
                "h",
                "r3",
                "r",
                "l3",
                "l",
                "3gh3",
                "gh",
                "g",
                "s",
                "t+",
                "p+",
                "k+",
                "f+",
                "m+",
                "n+",
                "W3",
                "w",
                "Wh3",
                "w",
                "3"
        };

        for (String testCase : testCases) {
            assertEquals(caverphone.handleReplacements(testCase), caverphone.caverphone(testCase).substring(2, caverphone.caverphone(testCase).length() - 11));
        }
    }

    @Test
    public void testCaverphoneHandleRemovals() {
        Caverphone caverphone = new Caverphone();

        String[] testCases = {
                "2",
                "3$",
                "3"
        };

        for (String testCase : testCases) {
            assertEquals(caverphone.handleRemovals(caverphone.caverphone("")), caverphone.caverphone("").substring(0, caverphone.caverphone("").length() - 11));
        }
    }

    @Test
    public void testCaverphonePutTenOnEnd() {
        Caverphone caverphone = new Caverphone();

        String testCase = "testTestCase";
        String expected = testCase.substring(0, 7) + "111111" + "1111";

        assertEquals(expected, caverphone.caverphone(testCase).substring(0, 16));
    }

    @Test
    public void testCaverphoneFirstSixCharacters() {
        Caverphone caverphone = new Caverphone();

        String[] testCases = {
                "test",
                "test1",
                "test12"
        };

        for (String testCase : testCases) {
            assertEquals(testCase.substring(0, 6), caverphone.caverphone(testCase).substring(0, 6));
        }
    }

    @Test
    public void testCaverphoneEncode() {
        Caverphone caverphone = new Caverphone();

        String testCase = "testTestCase";
        assertEquals(caverphone.caverphone(testCase), caverphone.encode(testCase));
    }

    @Test
    public void testCaverphoneEncodeException() {
        Caverphone caverphone = new Caverphone();

        assertThrows(EncoderException.class, () -> caverphone.encode(123));
    }

    @Test
    public void testCaverphoneIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        String testCase1 = "test1";
        String testCase2 = "test2";

        assertTrue(caverphone.isCaverphoneEqual(testCase1, testCase1));
        assertFalse(caverphone.isCaverphoneEqual(testCase1, testCase2));
    }
}