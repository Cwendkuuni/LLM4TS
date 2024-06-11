package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandomInt() {
        int count = 5;
        String randomString = RandomStringUtils.random(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 5;
        String randomString = RandomStringUtils.randomAscii(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character is ASCII", (c >= 32 && c <= 126));
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 5;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character is alphabetic", Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 5;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character is alphanumeric", Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 5;
        String randomString = RandomStringUtils.randomNumeric(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character is numeric", Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithSetRangeAndLetters() {
        int count = 5;
        int start = 33;
        int end = 126;
        boolean letters = true;
        String randomString = RandomStringUtils.random(count, start, end, letters, false);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());

        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Character is in set", (c >= start && c < end) || (letters && Character.isLetter(c)) || (!letters && Character.isDigit(c)));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptySet() {
        RandomStringUtils.random(1, new char[0]);
    }

}