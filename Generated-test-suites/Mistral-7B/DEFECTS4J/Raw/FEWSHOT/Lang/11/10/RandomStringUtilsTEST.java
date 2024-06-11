package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(5);
            assertNotNull(randomString);
            assertEquals(5, randomString.length());
        }
    }

    @Test
    public void testRandomAscii() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAscii(5);
            assertNotNull(randomString);
            assertEquals(5, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                int randomChar = randomString.charAt(j);
                assertTrue(randomChar >= 32 && randomChar <= 126);
            }
        }
    }

    @Test
    public void testRandomAlphabetic() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphabetic(5);
            assertNotNull(randomString);
            assertEquals(5, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                int randomChar = randomString.charAt(j);
                assertTrue(Character.isAlphabetic((char) randomChar));
            }
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphanumeric(5);
            assertNotNull(randomString);
            assertEquals(5, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                int randomChar = randomString.charAt(j);
                assertTrue(Character.isAlphabetic((char) randomChar) || Character.isDigit((char) randomChar));
            }
        }
    }

    @Test
    public void testRandomNumeric() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomNumeric(5);
            assertNotNull(randomString);
            assertEquals(5, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                int randomChar = randomString.charAt(j);
                assertTrue(Character.isDigit((char) randomChar));
            }
        }
    }

    @Test
    public void testRandomWithRange() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random("a-z", 5);
            assertNotNull(randomString);
            assertEquals(5, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                int randomChar = randomString.charAt(j);
                assertTrue(Character.isAlphabetic((char) randomChar));
            }
        }
    }

    @Test
    public void testRandomWithCustomCharSet() {
        char[] chars = {'a', 'b', '1', '2', '3'};
        String randomString = RandomStringUtils.random(chars, 5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(findIndex(chars, randomChar) != -1);
        }
    }

    @Test
    public void testRandomWithCustomCharSetAndNumbers() {
        char[] chars = {'a', 'b', '1', '2', '3'};
        String randomString = RandomStringUtils.randomAlphanumeric(chars, 5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(findIndex(chars, randomChar) != -1 || Character.isDigit(randomChar));
        }
    }

    @Test
    public void testRandomWithCustomCharSetAndLettersOnly() {
        char[] chars = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.randomAlphabetic(chars, 5);
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char randomChar = randomString.charAt(i);
            assertTrue(Character.isAlphabetic(randomChar));
        }
    }

    private static int findIndex(char[] chars, char target) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == target) {
                return i;
            }
        }
        return -1;
    }
}