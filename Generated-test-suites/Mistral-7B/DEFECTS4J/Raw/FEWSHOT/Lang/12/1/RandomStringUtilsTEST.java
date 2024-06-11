package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        for (int i = 0; i < 10; i++) {
            String randomStr = RandomStringUtils.random(5);
            assertNotNull(randomStr);
            assertEquals(randomStr.length(), 5);
        }
    }

    @Test
    public void testRandomAsciiString() {
        for (int i = 0; i < 10; i++) {
            String randomStr = RandomStringUtils.randomAscii(5);
            assertNotNull(randomStr);
            assertEquals(randomStr.length(), 5);
            for (int j = 0; j < randomStr.length(); j++) {
                char c = randomStr.charAt(j);
                assertTrue(c >= 32 && c <= 126);
            }
        }
    }

    @Test
    public void testRandomAlphabeticString() {
        for (int i = 0; i < 10; i++) {
            String randomStr = RandomStringUtils.randomAlphabetic(5);
            assertNotNull(randomStr);
            assertEquals(randomStr.length(), 5);
            for (int j = 0; j < randomStr.length(); j++) {
                char c = randomStr.charAt(j);
                assertTrue(Character.isAlphabetic(c));
            }
        }
    }

    @Test
    public void testRandomAlphanumericString() {
        for (int i = 0; i < 10; i++) {
            String randomStr = RandomStringUtils.randomAlphanumeric(5);
            assertNotNull(randomStr);
            assertEquals(randomStr.length(), 5);
            for (int j = 0; j < randomStr.length(); j++) {
                char c = randomStr.charAt(j);
                assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomNumericString() {
        for (int i = 0; i < 10; i++) {
            String randomStr = RandomStringUtils.randomNumeric(5);
            assertNotNull(randomStr);
            assertEquals(randomStr.length(), 5);
            for (int j = 0; j < randomStr.length(); j++) {
                char c = randomStr.charAt(j);
                assertTrue(Character.isDigit(c));
            }
        }
    }

    @Test
    public void testRandomStringWithRange() {
        for (int i = 0; i < 10; i++) {
            String randomStr = RandomStringUtils.random("abcdef", 5);
            assertNotNull(randomStr);
            assertEquals(randomStr.length(), 5);
            for (int j = 0; j < randomStr.length(); j++) {
                char c = randomStr.charAt(j);
                assertTrue(c >= 'a' && c <= 'f');
            }
        }
    }

    @Test
    public void testRandomStringWithCustomChars() {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < 10; i++) {
            String randomStr = RandomStringUtils.random(5, chars.toCharArray());
            assertNotNull(randomStr);
            assertEquals(randomStr.length(), 5);
            for (int j = 0; j < randomStr.length(); j++) {
                char c = randomStr.charAt(j);
                assertTrue(chars.indexOf(c) != -1);
            }
        }
    }
}