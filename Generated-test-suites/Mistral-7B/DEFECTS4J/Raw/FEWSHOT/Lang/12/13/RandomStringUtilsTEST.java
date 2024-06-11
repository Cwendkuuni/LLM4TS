package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        // Test random string generation without any specifications
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        // Test random string generation with specified length and character set
        String alphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertNotNull(alphanumericString);
        assertEquals(5, alphanumericString.length());
        assertArrayEquals(new char[]{'1', 'a', 'B', '5', 'z'}, alphanumericString.toCharArray());

        // Test random string generation with specified length, range, and character set
        String customString = RandomStringUtils.random("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10);
        assertNotNull(customString);
        assertEquals(10, customString.length());
        assertArrayEquals(new char[]{'D', 'C', 'M', 'O', 'J', 'X', 'S', 'V', '9', 'E'}, customString.toCharArray());
    }

    @Test
    public void testRandomStringWithCustomRandom() {
        // Test random string generation with custom random instance
        Random customRandom = new Random(1L);
        String customRandomString = RandomStringUtils.random(10, customRandom);
        assertEquals(RandomStringUtils.random(10, customRandom), customRandomString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLength() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharacterSet() {
        RandomStringUtils.random(5, new char[0]);
    }
}