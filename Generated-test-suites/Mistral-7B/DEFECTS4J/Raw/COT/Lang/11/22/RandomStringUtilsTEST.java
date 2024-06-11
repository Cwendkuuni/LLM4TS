package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for RandomStringUtils.
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue((Character.isAlphabetic(c) || Character.isDigit(c)));
        }
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomEmptyChars() {
        String randomString = RandomStringUtils.random(1, new char[0]);
        assertNull(randomString);
    }

    @Test
    public void testRandomCustomChars() {
        String randomString = RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(randomString.chars().anyMatch(ch -> ch == c));
        }
    }

    @Test
    public void testRandomStartEnd() {
        String randomString = RandomStringUtils.random(10, 'a', 'z');
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) && Character.isLowerCase(c) && Character.isDefined(c));
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomInvalidStartEnd() {
        RandomStringUtils.random(1, 'a', 'z'.length() + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomInvalidChars() {
        RandomStringUtils.random(1, new char[0]);
    }

    @Test
    public void testRandomWithCustomRandom() {
        Random customRandom = new Random(12345);
        String randomString = RandomStringUtils.random(10, 'a', 'z', customRandom);
        String staticRandomString = RandomStringUtils.random(10, 'a', 'z');
        assertNotNull(randomString);
        assertTrue(randomString.length() == 10);
        assertTrue(randomString.compareTo(staticRandomString) != 0);
    }
}