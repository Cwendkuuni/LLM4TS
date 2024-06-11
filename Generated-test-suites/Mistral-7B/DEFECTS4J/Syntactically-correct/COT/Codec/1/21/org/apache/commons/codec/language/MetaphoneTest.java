package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphoneInstance() {
        Metaphone metaphone = new Metaphone();
        assertNotNull(metaphone);
    }

    @Test
    public void testEmptyString() {
        Metaphone metaphone = new Metaphone();
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        String charTest = "A";
        assertEquals(charTest, metaphone.metaphone(charTest));
    }

    @Test
    public void testBasicStrings() {
        Metaphone metaphone = new Metaphone();
        String testCases[] = {"APPLE", "BANANA", "CAT", "DOG", "ELEPHANT", "FISH"};
        for (String s : testCases) {
            assertEquals(metaphone.metaphone(s), metaphone.metaphone(s.toUpperCase()));
            String metaphoneCode = metaphone.metaphone(s);
            System.out.println(s + " -> " + metaphoneCode);
        }
    }

    @Test
    public void testInitialKGAndPN() {
        Metaphone metaphone = new Metaphone();
        String testCases[] = {"APPLE", "APPLEK", "APPLEKN", "APPLEP", "APPLEPN", "BANANA", "BANANAP", "BANANAPN"};
        for (String s : testCases) {
            String expected = "";
            switch (s.charAt(1)) {
                case 'K':
                    expected = "K";
                    break;
                case 'N':
                    expected = "K";
                    break;
                case 'P':
                    expected = "P";
                    break;
            }
            assertEquals(expected, metaphone.metaphone(s).charAt(0));
        }
    }

    @Test
    public void testInitialAEAndWH() {
        Metaphone metaphone = new Metaphone();
        String testCases[] = {"APPLE", "APPE", "APPLEA", "APPLEAE", "APPLEWH", "APPLEWHA"};
        for (String s : testCases) {
            assertEquals(s.substring(1), metaphone.metaphone(s).substring(1));
            if (s.charAt(1) == 'E') {
                assertEquals("A", metaphone.metaphone(s).charAt(0));
            } else {
                assertEquals("W", metaphone.metaphone(s).charAt(0));
            }
        }
    }

    @Test
    public void testWr() {
        Metaphone metaphone = new Metaphone();
        String testCases[] = {"WRONG", "WRONGR"};
        for (String s : testCases) {
            assertEquals("R", metaphone.metaphone(s).charAt(0));
        }
    }

    @Test
    public void testInitialX() {
        Metaphone metaphone = new Metaphone();
        String testCases[] = {"X", "APPLEX", "SIX", "SIXTY"};
        for (String s : testCases) {
            if (s.length() == 1) {
                assertEquals("S", metaphone.metaphone(s));
            } else {
                assertEquals(metaphone.metaphone(s.substring(1)), metaphone.metaphone(s));
                assertEquals("S", metaphone.metaphone(s).charAt(0));
            }
        }
    }

    @Test
    public void testInitialKAndH() {
        Metaphone metaphone = new Metaphone();
        String testCases[] = {"CHICKEN", "CHICKENH", "CHICKENHH"};
        for (String s : testCases) {
            if (s.length() > 1 && s.charAt(1) == 'H') {
                assertEquals("K", metaphone.metaphone(s).charAt(0));
            } else {
                assertEquals(metaphone.metaphone(s.substring(1)), metaphone.metaphone(s));
            }
        }
    }

    @Test(expected = EncoderException.class)
    public void testNullArgument() {
        Metaphone metaphone = new Metaphone();
        metaphone.metaphone(null);
    }

    @Test
    public void testEncoderInterface() {
        Metaphone metaphone = new Metaphone();
        String testString = "APPLE";
        assertEquals(metaphone.metaphone(testString), metaphone.encode(testString));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        String testCases[] = {"APPLE", "BANANA", "APPLE", "BANANA"};
        for (int i = 0; i < testCases.length; i++) {
            String s1 = testCases[i];
            for (int j = 0; j < testCases.length; j++) {
                String s2 = testCases[j];
                if (i != j) {
                    assertFalse(metaphone.isMetaphoneEqual(s1, s2));
                } else {
                    assertTrue(metaphone.isMetaphoneEqual(s1, s2));
                }
            }
        }
    }
}