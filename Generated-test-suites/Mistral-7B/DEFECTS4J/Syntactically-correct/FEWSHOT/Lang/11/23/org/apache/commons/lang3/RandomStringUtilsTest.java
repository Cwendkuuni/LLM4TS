package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertEquals(5, randomString.length());
        assertNotNull(randomString);
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(5);
        assertEquals(5, randomString.length());
        assertNotNull(randomString);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(randomString.charAt(i) >= 32 && randomString.charAt(i) <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, randomString.length());
        assertNotNull(randomString);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, randomString.length());
        assertNotNull(randomString);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isLetterOrDigit(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(5);
        assertEquals(5, randomString.length());
        assertNotNull(randomString);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isDigit(randomString.charAt(i)));
        }
    }

    @Test
    public void testRandomWithCustomCharacterSet() {
        RandomStringUtils.random(5, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharacterSet() {
        RandomStringUtils.random(5, new char[0]);
    }

    @Test
    public void testRandomWithCustomCharacterSetAndRange() {
        int start = 97;
        int end = 122;
        String randomString = RandomStringUtils.random(5, start, end, false, false, null, RANDOM);
        assertEquals(5, randomString.length());
        assertNotNull(randomString);
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(randomString.charAt(i) >= start && randomString.charAt(i) <= end);
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(5, true, true, null, RANDOM);
        assertEquals(5, randomString.length());
        assertNotNull(randomString);
        for (int i = 0; i < randomString.length(); i++) {
            if (Character.isLetter(randomString.charAt(i))) {
                assertTrue(Character.isUpperCase(randomString.charAt(i)) || Character.isLowerCase(randomString.charAt(i)));
            } else {
                assertTrue(Character.isDigit(randomString.charAt(i)));
            }
        }
    }

    @Test
    public void testRandomWithCustomCharacterSetAndLimitSurrogates() {
        String randomString = RandomStringUtils.random(5, true, true, null, null, null, RANDOM);
        assertEquals(5, randomString.length());
        assertNotNull(randomString);
        for (int i = 0; i < randomString.length(); i++) {
            if (Character.isSurrogate(randomString.charAt(i))) {
                if (i == 0 || i == randomString.length() - 1) {
                    continue;
                }
                assertFalse(Character.isSurrogate(randomString.charAt(i - 1)));
                assertFalse(Character.isSurrogate(randomString.charAt(i + 1)));
            }
        }
    }

    @Test
    public void testRandomWithCustomCharacterSetAndLimitSurrogatesAndRange() {
        String randomString = RandomStringUtils.random(5, true, true, null, null, null, RANDOM);
        int start = 56320;
        int end = 56400;
        String randomStringWithSurrogates = RandomStringUtils.random(5, true, true, null, null, null, RANDOM);
        int indexOfSurrogatePair = -1;
        for (int i = 0; i < randomStringWithSurrogates.length(); i++) {
            if (Character.isSurrogate(randomStringWithSurrogates.charAt(i))) {
                indexOfSurrogatePair = i;
                break;
            }
        }
        // Surrogate pair should be present
        assertNotNull(indexOfSurrogatePair);
        // Check if valid surrogate pair and not within the range
        char highSurrogate = randomStringWithSurrogates.charAt(indexOfSurrogatePair - 1);
        char lowSurrogate = randomStringWithSurrogates.charAt(indexOfSurrogatePair);
        assertFalse(Character.isSurrogate(highSurrogate));
        assertFalse(Character.isSurrogate(lowSurrogate));
        assertFalse(Character.isSurrogate((char) (highSurrogate & 0xFFFF) | ((char) (lowSurrogate & 0xFFFF) << 10)));
        assertFalse(Character.isSurrogate((char) (lowSurrogate & 0xFFFF) | ((char) (highSurrogate & 0xFFFF) << 10)));
        assertFalse(highSurrogate >= start && highSurrogate <= end);
        assertFalse(lowSurrogate >= start && lowSurrogate <= end);
        // Replace the surrogate pair with valid characters within the range
        randomString = randomString.substring(0, indexOfSurrogatePair) + new String(new char[]{(char) (start + RANDOM.nextInt(end - start + 1)), (char) (start + RANDOM.nextInt(end - start + 1))}) + randomString.substring(indexOfSurrogatePair + 2);
        assertEquals(5, randomString.length());
        assertNotNull(randomString);
        for (int i = 0; i < randomString.length(); i++) {
            if (i == 0 || i == randomString.length() - 1) {
                continue;
            }
            if (Character.isSurrogate(randomString.charAt(i))) {
                fail("A surrogate character was generated within the string.");
            }
            assertTrue(randomString.charAt(i) >= start && randomString.charAt(i) <= end);
        }
    }

    @Test
    public void testRandomWithLettersAndNumbersAndCustomCharacterSet() {
        String customCharacterSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String randomString = RandomStringUtils.random(5, true, true, customCharacterSet.toCharArray(), RANDOM);
        assertEquals(5, randomString.length());
        assertNotNull(randomString);
        for (int i = 0; i < randomString.length(); i++) {
            if (Character.isLetter(randomString.charAt(i))) {
                assertTrue(Character.isUpperCase(randomString.charAt(i)) || Character.isLowerCase(randomString.charAt(i)));
            } else {
                assertTrue(Character.isDigit(randomString.charAt(i)));
            }
            assertTrue(customCharacterSet.indexOf(randomString.charAt(i)) != -1);
        }
    }

    @Test
    public void testRandomWithCustomCharacterSetWithRepeatedCharacter() {
        String customCharacterSet = "ABC";
        String randomString = RandomStringUtils.random(5, customCharacterSet.toCharArray(), RANDOM);
        int count = 0;
        for (int i = 0; i < randomString.length(); i++) {
            if (randomString.charAt(i) == customCharacterSet.charAt(0)) {
                count++;
            }
        }
        assertTrue(count > 1);
    }

    @Test
    public void testRandomWithCustomCharacterSetWithRepeatedCharacterAndLength() {
        String customCharacterSet = "ABC";
        int length = 5;
        int expectedLength = length;
        while (expectedLength > 0) {
            String randomString = RandomStringUtils.random(length, customCharacterSet.toCharArray(), RANDOM);
            int count = 0;
            for (int i = 0; i < randomString.length(); i++) {
                if (randomString.charAt(i) == customCharacterSet.charAt(0)) {
                    count++;
                }
            }
            if (count == 1) {
                expectedLength--;
            }
        }
        fail("A random string was generated without the specified character.");
    }

    @Test
    public void testRandomWithCustomCharacterSetWithRepeatedCharacterAndRange() {
        String customCharacterSet = "ABC";
        int start = 0;
        int end = 1;
        String randomString = RandomStringUtils.random(5, customCharacterSet.toCharArray(), start, end, RANDOM);
        int count = 0;
        for (int i = 0; i < randomString.length(); i++) {
            if (randomString.charAt(i) == customCharacterSet.charAt(start % customCharacterSet.length())) {
                count++;
            }
        }
        assertTrue(count > 1);
    }

    @Test
    public void testRandomWithCustomCharacterSetWithRepeatedCharacterAndRangeAndLength() {
        String customCharacterSet = "ABC";
        int length = 5;
        int start = 0;
        int end = 1;
        String randomString = RandomStringUtils.random(length, customCharacterSet.toCharArray(), start, end, RANDOM);
        int count = 0;
        for (int i = 0; i < randomString.length(); i++) {
            if (randomString.charAt(i) == customCharacterSet.charAt(start % customCharacterSet.length())) {
                count++;
            }
        }
        assertTrue(count > 1);
        String randomStringWithNoRepeatedCharacter = "";
        while (randomStringWithNoRepeatedCharacter.length() < length) {
            char c = customCharacterSet.charAt(RANDOM.nextInt(customCharacterSet.length()));
            randomStringWithNoRepeatedCharacter += c;
            if (randomStringWithNoRepeatedCharacter.charAt(0) == randomStringWithNoRepeatedCharacter.charAt(1)) {
                continue;
            }
            if (randomStringWithNoRepeatedCharacter.charAt(randomStringWithNoRepeatedCharacter.length() - 1) == randomStringWithNoRepeatedCharacter.charAt(randomStringWithNoRepeatedCharacter.length() - 2)) {
                continue;
            }
            randomString = randomStringWithNoRepeatedCharacter;
        }
        String expectedString = randomString;
        String actualString = RandomStringUtils.random(length, customCharacterSet.toCharArray(), start, end, RANDOM);
        assertArrayEquals(expectedString.toCharArray(), actualString.toCharArray());
    }
}