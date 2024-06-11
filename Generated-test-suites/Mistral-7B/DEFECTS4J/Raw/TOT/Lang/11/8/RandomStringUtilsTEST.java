package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    @Test
    public void testRandomStringAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(32, (int) c); // Minimum ASCII value
            assertEquals(126, (int) c); // Maximum ASCII value
        }
    }

    @Test
    public void testRandomStringAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(Character.isAlphabetic(c), true);
        }
    }

    @Test
    public void testRandomStringAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(Character.isAlphabetic(c) || Character.isDigit(c), true);
        }
    }

    @Test
    public void testRandomStringNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(Character.isDigit(c), true);
        }
    }

    @Test
    public void testRandomStringWithRange() {
        String randomString = RandomStringUtils.random(10, 65, 90);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(Character.isAlphabetic(c), true);
            assertEquals(65 <= (int) c && (int) c <= 90, true);
        }
    }

    @Test
    public void testRandomStringWithLettersAndNumbers() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertEquals(Character.isAlphabetic(c) || Character.isDigit(c), true);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringEmptyCharsArray() {
        RandomStringUtils.random(10, (char[]) null);
    }
}