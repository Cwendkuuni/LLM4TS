package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test creating a random string of a specified length.
     */
    @Test
    public void testRandomString() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(i);
            Assert.assertEquals("Random string of length " + i, i, randomString.length());
        }
    }

    /**
     * Test creating a random string of a specified length with specified characters.
     */
    @Test
    public void testRandomStringWithCharacters() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(i, characters.toCharArray());
            Assert.assertEquals("Random string of length " + i + " with characters [" + characters + "]", i, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                Assert.assertTrue("Random string [" + randomString + "] does not contain an invalid character", characters.indexOf(randomString.charAt(j)) != -1);
            }
        }
    }

    /**
     * Test creating a random string of a specified length with ASCII characters only.
     */
    @Test
    public void testRandomStringAscii() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAscii(i);
            Assert.assertEquals("Random string of length " + i + " with ASCII characters only", i, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                Assert.assertTrue("Random string [" + randomString + "] contains an invalid character", Character.isASCII(randomString.charAt(j)));
            }
        }
    }

    /**
     * Test creating a random string of a specified length with alphabetic characters only.
     */
    @Test
    public void testRandomStringAlphabetic() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphabetic(i);
            Assert.assertEquals("Random string of length " + i + " with alphabetic characters only", i, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                Assert.assertTrue("Random string [" + randomString + "] contains an invalid character", Character.isAlphabetic(randomString.charAt(j)));
            }
        }
    }

    /**
     * Test creating a random string of a specified length with alphanumeric characters only.
     */
    @Test
    public void testRandomStringAlphanumeric() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomAlphanumeric(i);
            Assert.assertEquals("Random string of length " + i + " with alphanumeric characters only", i, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                Assert.assertTrue("Random string [" + randomString + "] contains an invalid character", Character.isAlphabetic(randomString.charAt(j)) || Character.isDigit(randomString.charAt(j)));
            }
        }
    }

    /**
     * Test creating a random string of a specified length with numeric characters only.
     */
    @Test
    public void testRandomStringNumeric() {
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.randomNumeric(i);
            Assert.assertEquals("Random string of length " + i + " with numeric characters only", i, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                Assert.assertTrue("Random string [" + randomString + "] contains an invalid character", Character.isDigit(randomString.charAt(j)));
            }
        }
    }

    /**
     * Test creating a random string with specified start and end characters.
     */
    @Test
    public void testRandomStringWithStartEnd() {
        String characters = "0123456789ABCDEF";
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(i, characters.charAt(0), characters.charAt(characters.length() - 1));
            Assert.assertEquals("Random string of length " + i + " with start and end characters [" + characters.charAt(0) + " and " + characters.charAt(characters.length() - 1) + "]", i, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                Assert.assertTrue("Random string [" + randomString + "] contains an invalid character", characters.indexOf(randomString.charAt(j)) != -1);
                Assert.assertTrue("Random string [" + randomString + "] contains a character outside of the start and end range", (characters.indexOf(randomString.charAt(j)) >= characters.indexOf(characters.charAt(0)) && characters.indexOf(randomString.charAt(j)) <= characters.indexOf(characters.charAt(characters.length() - 1))));
            }
        }
    }

    /**
     * Test creating a random string with specified start and end characters and a custom random generator.
     */
    @Test
    public void testRandomStringWithStartEndAndCustomRandomGenerator() {
        String characters = "0123456789ABCDEF";
        Random customRandom = new Random();
        for (int i = 0; i < 10; i++) {
            String randomString = RandomStringUtils.random(i, characters.charAt(0), characters.charAt(characters.length() - 1), false, false, null, customRandom);
            Assert.assertEquals("Random string of length " + i + " with start and end characters [" + characters.charAt(0) + " and " + characters.charAt(characters.length() - 1) + "] and custom random generator", i, randomString.length());
            for (int j = 0; j < randomString.length(); j++) {
                Assert.assertTrue("Random string [" + randomString + "] contains an invalid character", characters.indexOf(randomString.charAt(j)) != -1);
                Assert.assertTrue("Random string [" + randomString + "] contains a character outside of the start and end range", (characters.indexOf(randomString.charAt(j)) >= characters.indexOf(characters.charAt(0)) && characters.indexOf(randomString.charAt(j)) <= characters.indexOf(characters.charAt(characters.length() - 1))));
            }
        }
    }
}