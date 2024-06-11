package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        String randomStringAscii = RandomStringUtils.randomAscii(10);
        assertNotNull(randomStringAscii);
        assertEquals(10, randomStringAscii.length());

        String randomStringAlphabetic = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomStringAlphabetic);
        assertEquals(10, randomStringAlphabetic.length());

        String randomStringAlphanumeric = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomStringAlphanumeric);
        assertEquals(10, randomStringAlphanumeric.length());

        String randomStringNumeric = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomStringNumeric);
        assertEquals(10, randomStringNumeric.length());
    }

    @Test
    public void testRandomWithStartAndEnd() {
        String randomString = RandomStringUtils.random(10, 'a', 'z');
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(randomString.charAt(i) >= 'a' && randomString.charAt(i) <= 'z');
        }

        String randomStringWithStartAndEnd = RandomStringUtils.random(10, 'a', 'e');
        assertNotNull(randomStringWithStartAndEnd);
        assertEquals(10, randomStringWithStartAndEnd.length());

        for (int i = 0; i < randomStringWithStartAndEnd.length(); i++) {
            assertTrue(randomStringWithStartAndEnd.charAt(i) >= 'a' && randomStringWithStartAndEnd.charAt(i) <= 'e');
        }

        String randomStringWithOnlyLetters = RandomStringUtils.random(10, true, false);
        assertNotNull(randomStringWithOnlyLetters);
        assertEquals(10, randomStringWithOnlyLetters.length());

        for (int i = 0; i < randomStringWithOnlyLetters.length(); i++) {
            assertTrue(Character.isLetter(randomStringWithOnlyLetters.charAt(i)));
        }

        String randomStringWithOnlyNumbers = RandomStringUtils.random(10, false, true);
        assertNotNull(randomStringWithOnlyNumbers);
        assertEquals(10, randomStringWithOnlyNumbers.length());

        for (int i = 0; i < randomStringWithOnlyNumbers.length(); i++) {
            assertTrue(Character.isDigit(randomStringWithOnlyNumbers.charAt(i)));
        }

        String randomStringWithLettersAndNumbers = RandomStringUtils.random(10, true, true);
        assertNotNull(randomStringWithLettersAndNumbers);
        assertEquals(10, randomStringWithLettersAndNumbers.length());

        for (int i = 0; i < randomStringWithLettersAndNumbers.length(); i++) {
            if (Character.isLetter(randomStringWithLettersAndNumbers.charAt(i))) {
                assertTrue(Character.isLetter(randomStringWithLettersAndNumbers.charAt(i)));
            } else {
                assertTrue(Character.isDigit(randomStringWithLettersAndNumbers.charAt(i)));
            }
        }
    }

    @Test
    public void testRandomWithCustomCharSet() {
        String customCharSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-={}[]|\\:;\"<>,.?/";
        String randomString = RandomStringUtils.random(10, customCharSet.toCharArray());
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(customCharSet.indexOf(randomString.charAt(i)) != -1);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharSet() {
        RandomStringUtils.random(1, (char[]) null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithInvalidCharSet() {
        String invalidCharSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789!@#$%^&*()_+-={}[]|\\:;\"<>,.";
        String randomString = RandomStringUtils.random(1, invalidCharSet.toCharArray());
    }

    @Test
    public void testRandomWithCustomRandom() {
        Random customRandom = new Random();
        String randomString = RandomStringUtils.random(10, 'a', 'z', false, false, null, customRandom);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(randomString.charAt(i) >= 'a' && randomString.charAt(i) <= 'z');
        }

        String randomStringWithCustomRandom = RandomStringUtils.random(10, 'a', 'z', false, false, null, customRandom);
        assertNotNull(randomStringWithCustomRandom);
        assertEquals(10, randomStringWithCustomRandom.length());

        for (int i = 0; i < randomStringWithCustomRandom.length(); i++) {
            assertTrue(randomStringWithCustomRandom.charAt(i) >= 'a' && randomStringWithCustomRandom.charAt(i) <= 'z');
        }
    }
}