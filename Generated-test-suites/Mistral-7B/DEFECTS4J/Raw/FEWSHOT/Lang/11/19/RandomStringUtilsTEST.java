package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        // Test random method with default characters
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());

        // Test random method with specified character set and length
        String randomStringAlphanumeric = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomStringAlphanumeric);
        assertEquals(10, randomStringAlphanumeric.length());
        assertArrayEquals(new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'}, randomStringAlphanumeric.toCharArray());

        // Test random method with specified character set, start and end indices
        String randomStringAlphanumericSubset = RandomStringUtils.randomAlphanumeric(10, 'A', 'F');
        assertNotNull(randomStringAlphanumericSubset);
        assertEquals(10, randomStringAlphanumericSubset.length());
        assertArrayEquals(new char[]{'A', 'B', 'C', 'D', 'E'}, randomStringAlphanumericSubset.toCharArray());

        // Test random method with specified character set, start and end indices, and seed
        Random randomSeed = new Random(1L);
        String randomStringAlphanumericSeeded = RandomStringUtils.randomAlphanumeric(10, 'A', 'F', null, randomSeed);
        assertEquals(RandomStringUtils.randomAlphanumeric(10, 'A', 'F'), randomStringAlphanumericSeeded);

        // Test random method with specified character set, length, and seed
        String randomStringAlphanumericSeededLength = RandomStringUtils.randomAlphanumeric(10, 0, 10, true, true, null, randomSeed);
        assertEquals(RandomStringUtils.randomAlphanumeric(10, 0, 10, true, true, null, randomSeed), randomStringAlphanumericSeededLength);

        // Test random method with specified character set, length, start and end indices, and seed
        char[] chars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'};
        String randomStringCustomSet = RandomStringUtils.random(10, chars, randomSeed);
        assertEquals(RandomStringUtils.random(10, chars, randomSeed), randomStringCustomSet);

        // Test random method with specified character set, length, start and end indices, and seed, negative count
        try {
            RandomStringUtils.random(-1, chars, randomSeed);
            fail("Expected IllegalArgumentException with negative count");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test random method with specified character set, length, start and end indices, and seed, null character array
        try {
            RandomStringUtils.random(10, null, randomSeed);
            fail("Expected IllegalArgumentException with null character array");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test random method with specified character set, length, start and end indices, and seed, empty character array
        try {
            RandomStringUtils.random(0, new char[0], randomSeed);
            fail("Expected IllegalArgumentException with empty character array");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test random method with null input and default settings
        String randomStringNull = RandomStringUtils.random(null);
        assertNull(randomStringNull);

        // Test random method with empty input and default settings
        String randomStringEmpty = RandomStringUtils.random(0);
        assertEquals("", randomStringEmpty);

        // Test random method with specified character set and empty input
        String randomStringEmptyCustomSet = RandomStringUtils.random(0, chars);
        assertEquals("", randomStringEmptyCustomSet);

        // Test random method with specified character set and negative length
        try {
            RandomStringUtils.random(-1, chars);
            fail("Expected IllegalArgumentException with negative count");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test random method with null character array and default settings
        String randomStringNullChars = RandomStringUtils.random(null);
        assertNull(randomStringNullChars);

        // Test random method with empty character array and default settings
        String randomStringEmptyChars = RandomStringUtils.random(new char[0]);
        assertEquals("", randomStringEmptyChars);

        // Test random method with empty character array and specified length
        String randomStringEmptyLength = RandomStringUtils.random(10, new char[0]);
        assertEquals("", randomStringEmptyLength);

        // Test random method with null character array and specified length
        try {
            RandomStringUtils.random(10, null);
            fail("Expected IllegalArgumentException with null character array");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test random method with character array subset and negative length
        try {
            RandomStringUtils.random(10, chars, 0, chars.length - 1, false, false, randomSeed);
            fail("Expected IllegalArgumentException with negative count");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test random method with character array subset and empty subset
        try {
            RandomStringUtils.random(10, chars, 0, 0, false, false, randomSeed);
            fail("Expected IllegalArgumentException with empty character array subset");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test random method with character array subset, length, and null random
        try {
            RandomStringUtils.random(10, chars, 0, chars.length - 1, false, false, null);
            fail("Expected IllegalArgumentException with null random instance");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test random method with character array subset and empty subset, and null random
        try {
            RandomStringUtils.random(10, chars, 0, 0, false, false, null);
            fail("Expected IllegalArgumentException with empty character array subset and null random instance");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test random method with character array subset, length, start and end indices, and null random
        try {
            RandomStringUtils.random(10, 'A', 'Z', null, randomSeed);
            fail("Expected IllegalArgumentException with null random instance");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test random method with character array subset, length, start and end indices, and empty subset, and null random
        try {
            RandomStringUtils.random(10, 'A', 'A', null, randomSeed);
            fail("Expected IllegalArgumentException with empty character array subset and null random instance");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test random method with character array subset, length, start and end indices, and null random, and negative count
        try {
            RandomStringUtils.random(-1, 'A', 'Z', null, randomSeed);
            fail("Expected IllegalArgumentException with negative count");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test random method with character array subset, length, start and end indices, and null random, and empty subset
        try {
            RandomStringUtils.random(0, 'A', 'A', null, randomSeed);
            fail("Expected IllegalArgumentException with empty character array subset and null random instance");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}