package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10);
            assertNotNull(randomString);
            assertEquals(10, randomString.length());
        }
    }

    @Test
    public void testRandomAsciiString() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAscii(10);
            assertNotNull(randomString);
            assertEquals(10, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                int c = randomString.charAt(j);
                assert(c >= 32 && c <= 126);
            }
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphabetic(10);
            assertNotNull(randomString);
            assertEquals(10, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                int c = randomString.charAt(j);
                assert(Character.isAlphabetic((char) c));
            }
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphanumeric(10);
            assertNotNull(randomString);
            assertEquals(10, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                int c = randomString.charAt(j);
                assert(Character.isAlphabetic((char) c) || Character.isDigit((char) c));
            }
        }
    }

    @Test
    public void testRandomNumericString() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomNumeric(10);
            assertNotNull(randomString);
            assertEquals(10, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                int c = randomString.charAt(j);
                assert(Character.isDigit((char) c));
            }
        }
    }

    @Test
    public void testRandomStringWithRange() {
        char[] chars = {'a', 'b', 'c', 'd'};
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, 0, chars.length, false, false, chars, new Random());
            assertNotNull(randomString);
            assertEquals(10, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                assert(Arrays.binarySearch(chars, randomString.charAt(j)) >= 0);
            }
        }
    }

    @Test
    public void testRandomStringWithLettersAndNumbers() {
        char[] chars = {'a', 'b', 'c', 'd', '0', '1', '2', '3', '4', '5'};
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(10, 0, chars.length, true, true, chars, new Random());
            assertNotNull(randomString);
            assertEquals(10, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                int c = randomString.charAt(j);
                assert(Character.isAlphabetic((char) c) || Character.isDigit((char) c) || c == 32);
            }
        }
    }
}