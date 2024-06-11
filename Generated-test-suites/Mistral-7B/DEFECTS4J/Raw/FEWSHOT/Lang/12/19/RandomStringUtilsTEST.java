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
            for (int j = 0; j < randomString.length(); j++) {
                assert(Character.isAlphabetic(randomString.charAt(j)) || Character.isDigit(randomString.charAt(j)));
            }
        }
    }

    @Test
    public void testRandomAscii() {
        for (int i = 0; i < 10; i++) {
            String randomAsciiString = RandomStringUtils.randomAscii(5);
            assertNotNull(randomAsciiString);
            assertEquals(5, randomAsciiString.length());
            for (int j = 0; j < randomAsciiString.length(); j++) {
                assert(Character.isISOControl(randomAsciiString.charAt(j)) || Character.isWhitespace(randomAsciiString.charAt(j)) || Character.isAlphabetic(randomAsciiString.charAt(j)) || Character.isDigit(randomAsciiString.charAt(j)) || Character.isGraphic(randomAsciiString.charAt(j)) || Character.isHighSurrogate(randomAsciiString.charAt(j)) || Character.isLowSurrogate(randomAsciiString.charAt(j)));
            }
        }
    }

    @Test
    public void testRandomAlphabetic() {
        for (int i = 0; i < 10; i++) {
            String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
            assertNotNull(randomAlphabeticString);
            assertEquals(5, randomAlphabeticString.length());
            for (int j = 0; j < randomAlphabeticString.length(); j++) {
                assert(Character.isAlphabetic(randomAlphabeticString.charAt(j)));
            }
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        for (int i = 0; i < 10; i++) {
            String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
            assertNotNull(randomAlphanumericString);
            assertEquals(5, randomAlphanumericString.length());
            for (int j = 0; j < randomAlphanumericString.length(); j++) {
                assert(Character.isAlphabetic(randomAlphanumericString.charAt(j)) || Character.isDigit(randomAlphanumericString.charAt(j)));
            }
        }
    }

    @Test
    public void testRandomNumeric() {
        for (int i = 0; i < 10; i++) {
            String randomNumericString = RandomStringUtils.randomNumeric(5);
            assertNotNull(randomNumericString);
            assertEquals(5, randomNumericString.length());
            for (int j = 0; j < randomNumericString.length(); j++) {
                assert(Character.isDigit(randomNumericString.charAt(j)));
            }
        }
    }

    @Test
    public void testRandomWithCustomSet() {
        String customSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
        String randomString = RandomStringUtils.random(5, customSet.toCharArray());
        assertNotNull(randomString);
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assert(customSet.indexOf(randomString.charAt(i)) != -1);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidCount() {
        RandomStringUtils.random(-5);
    }

    @Test
    public void testRandomWithSeed() {
        Random random = new Random(123);
        String randomString = RandomStringUtils.random(5, random);
        RandomStringUtils.random(5, random);
        RandomStringUtils.random(5, random);
        String randomString2 = RandomStringUtils.random(5, random);
        assertArrayEquals(randomString.toCharArray(), randomString2.toCharArray());
    }
}