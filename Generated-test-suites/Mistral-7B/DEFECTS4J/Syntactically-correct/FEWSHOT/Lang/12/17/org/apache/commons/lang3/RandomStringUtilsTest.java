package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String random = RandomStringUtils.random(5);
        assertNotNull(random);
        assertTrue(random.length() == 5);

        random = RandomStringUtils.randomAscii(5);
        assertNotNull(random);
        assertTrue(random.length() == 5);
        for (char c : random.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }

        random = RandomStringUtils.randomAlphabetic(5);
        assertNotNull(random);
        assertTrue(random.length() == 5);
        for (char c : random.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }

        random = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(random);
        assertTrue(random.length() == 5);
        for (char c : random.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                assertTrue(Character.isLetter(c));
            } else if (Character.isDigit(c)) {
                assertTrue(Character.isDigit(c));
            }
        }

        random = RandomStringUtils.randomNumeric(5);
        assertNotNull(random);
        assertTrue(random.length() == 5);
        for (char c : random.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithLimit() {
        String random = RandomStringUtils.random(10, 'a', 'z');
        assertNotNull(random);
        assertTrue(random.length() == 10);
        for (char c : random.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
            assertTrue(c >= 'a' && c <= 'z');
        }

        random = RandomStringUtils.random(10, '0', '9');
        assertNotNull(random);
        assertTrue(random.length() == 10);
        for (char c : random.toCharArray()) {
            assertTrue(Character.isDigit(c));
            assertTrue(c >= '0' && c <= '9');
        }

        random = RandomStringUtils.random(10, 'a', 'z', false, true);
        assertNotNull(random);
        assertTrue(random.length() == 10);
        for (char c : random.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
            assertTrue(c >= 'a' && c <= 'z');
            if (c >= 56320 && c <= 57343) {
                assertTrue(random.indexOf(c) > 0);
                assertTrue(random.indexOf(Character.highSurrogate(c)) > 0);
            }
        }

        random = RandomStringUtils.random(10, '0', '9', false, false);
        assertNotNull(random);
        assertTrue(random.length() == 10);
        for (char c : random.toCharArray()) {
            assertTrue(Character.isDigit(c));
            assertTrue(c >= '0' && c <= '9');
        }
    }

    @Test
    public void testRandomWithCustomChars() {
        String random = RandomStringUtils.random(5, new char[]{'a', 'b', 'c'});
        assertNotNull(random);
        assertTrue(random.length() == 5);
        for (char c : random.toCharArray()) {
            assertTrue(new String(new char[]{c}).equals("a")
                    || new String(new char[]{c}).equals("b")
                    || new String(new char[]{c}).equals("c"));
        }

        random = RandomStringUtils.random(5, new char[]{'a', 'b'});
        assertNotNull(random);
        assertTrue(random.length() == 5);
        for (char c : random.toCharArray()) {
            assertTrue(new String(new char[]{c}).equals("a")
                    || new String(new char[]{c}).equals("b"));
        }

        random = RandomStringUtils.random(5, new char[0]);
        assertNotNull(random);
        assertTrue(random.length() == 5);
        for (char c : random.toCharArray()) {
            assertTrue(Character.isLetter(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithCustomRandom() {
        Random random = new Random(10);
        String randomString = RandomStringUtils.random(5, 0, 10, false, false, null, random);
        assertTrue(randomString.length() == 5);
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= '0' && c <= '9');
        }

        randomString = RandomStringUtils.random(5, 0, 55295, false, false, null, random);
        assertTrue(randomString.length() == 5);
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= ' ' && c <= 'z');
        }

        randomString = RandomStringUtils.random(5, 56320, 57343, false, false, null, random);
        assertTrue(randomString.length() >= 5);
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 56320 && c <= 57343);
            for (int i = 0; i < randomString.length(); i++) {
                if (c == randomString.charAt(i)) {
                    continue;
                }
                if (Character.isHighSurrogate(c) && Character.isLowSurrogate(randomString.charAt(i))) {
                    continue;
                }
                if (Character.isLowSurrogate(c) && Character.isHighSurrogate(randomString.charAt(i))) {
                    continue;
                }
                assertFalse(randomString.indexOf(c) > 0);
                assertFalse(randomString.indexOf(Character.highSurrogate(c)) > 0);
                assertFalse(randomString.indexOf(Character.lowSurrogate(c)) > 0);
            }
        }

        randomString = RandomStringUtils.random(5, 'a', 'z', false, false, null, random);
        assertTrue(randomString.length() == 5);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
            assertTrue(c >= 'a' && c <= 'z');
        }

        randomString = RandomStringUtils.random(5, '0', '9', false, false, null, random);
        assertTrue(randomString.length() == 5);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
            assertTrue(c >= '0' && c <= '9');
        }
    }

    @Test
    public void testRandomWithEmptyCustomChars() {
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(5, new char[0]));
    }

    @Test
    public void testRandomWithNegativeCount() {
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(-1));
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(-1, 0, 0, false, false, null, RandomStringUtils.RANDOM));
    }

    @Test
    public void testRandomWithNegativeStart() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> RandomStringUtils.random(5, -1, 0, false, false, null, RandomStringUtils.RANDOM));
    }

    @Test
    public void testRandomWithNegativeEnd() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> RandomStringUtils.random(5, 0, -1, false, false, null, RandomStringUtils.RANDOM));
    }

    @Test
    public void testRandomWithNegativeStartAndEnd() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> RandomStringUtils.random(5, -1, -1, false, false, null, RandomStringUtils.RANDOM));
    }
}