package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link RandomStringUtils} class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for {@link RandomStringUtils#random(int)} method.
     */
    @Test
    public void testRandomInt() {
        // Test with default character set
        String randomString = RandomStringUtils.random(10);
        assertFalse(randomString.isEmpty());
        assertTrue(randomString.length() == 10);

        // Test with character set [a-z]
        String randomStringAlphabetic = RandomStringUtils.randomAlphabetic(10);
        assertFalse(randomStringAlphabetic.isEmpty());
        assertTrue(randomStringAlphabetic.length() == 10);

        // Test with character set [0-9]
        String randomStringNumeric = RandomStringUtils.randomNumeric(10);
        assertFalse(randomStringNumeric.isEmpty());
        assertTrue(randomStringNumeric.length() == 10);

        // Test with character set [a-zA-Z0-9]
        String randomStringAlphanumeric = RandomStringUtils.randomAlphanumeric(10);
        assertFalse(randomStringAlphanumeric.isEmpty());
        assertTrue(randomStringAlphanumeric.length() == 10);

        // Test with custom character set
        char[] customChars = {'a', 'b', 'c'};
        String randomStringCustom = RandomStringUtils.random(10, customChars);
        assertFalse(randomStringCustom.isEmpty());
        assertTrue(randomStringCustom.length() == 10);

        for (int i = 0; i < randomStringCustom.length(); i++) {
            assertEquals(customChars[RANDOM.nextInt(customChars.length)], randomStringCustom.charAt(i));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int)} method.
     */
    @Test
    public void testRandomIntRange() {
        // Test with start = 0, end = 10
        String randomString = RandomStringUtils.random(10, 0, 10);
        assertFalse(randomString.isEmpty());
        assertTrue(randomString.length() == 10);

        // Test with start = 1, end = 10
        String randomStringSubset = RandomStringUtils.random(10, 1, 10);
        assertFalse(randomStringSubset.isEmpty());
        assertTrue(randomStringSubset.length() == 10);

        // Test with start = 0, end = 2
        String randomStringSubsetEmpty = RandomStringUtils.random(10, 0, 2);
        assertTrue(randomStringSubsetEmpty.isEmpty());

        // Test with start = 1, end = 0
        String randomStringSubsetInvalid = RandomStringUtils.random(10, 1, 0);
        assertTrue(randomStringSubsetInvalid.isEmpty());

        // Test with start = Integer.MIN_VALUE, end = Integer.MAX_VALUE
        String randomStringAllChars = RandomStringUtils.random(10, Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertFalse(randomStringAllChars.isEmpty());
        assertTrue(randomStringAllChars.length() == 10);
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean)} method.
     */
    @Test
    public void testRandomIntBoolean() {
        // Test with includeSurrogates = false
        String randomStringNoSurrogates = RandomStringUtils.random(10, 0, 0, false, false, null, RANDOM);
        assertFalse(randomStringNoSurrogates.isEmpty());
        assertTrue(randomStringNoSurrogates.length() == 10);

        // Test with includeSurrogates = true
        String randomStringWithSurrogates = RandomStringUtils.random(10, 0, 0, true, false, null, RANDOM);
        assertFalse(randomStringWithSurrogates.isEmpty());
        assertTrue(randomStringWithSurrogates.length() == 10);

        // Test for edge case with empty set
        String randomStringEmptySet = RandomStringUtils.random(10, Integer.MIN_VALUE, Integer.MAX_VALUE, true, false, null, RANDOM);
        assertFalse(randomStringEmptySet.isEmpty());
        assertTrue(randomStringEmptySet.length() > 0);
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean)} method.
     */
    @Test
    public void testRandomIntBooleanBoolean() {
        // Test with includeAlphanumeric = false, includeNumeric = false
        String randomStringNoAlphanumeric = RandomStringUtils.random(10, 0, 0, false, false, null, RANDOM);
        assertFalse(randomStringNoAlphanumeric.isEmpty());
        assertTrue(randomStringNoAlphanumeric.length() == 10);

        // Test with includeAlphanumeric = true, includeNumeric = false
        String randomStringAlphabeticOnly = RandomStringUtils.random(10, 0, 0, true, false, null, RANDOM);
        assertFalse(randomStringAlphabeticOnly.isEmpty());
        assertTrue(randomStringAlphabeticOnly.length() == 10);

        // Test with includeAlphanumeric = false, includeNumeric = true
        String randomStringNumericOnly = RandomStringUtils.random(10, 0, 0, false, true, null, RANDOM);
        assertFalse(randomStringNumericOnly.isEmpty());
        assertTrue(randomStringNumericOnly.length() == 10);

        // Test with includeAlphanumeric = true, includeNumeric = true
        String randomStringAlphanumericNumeric = RandomStringUtils.random(10, 0, 0, true, true, null, RANDOM);
        assertFalse(randomStringAlphanumericNumeric.isEmpty());
        assertTrue(randomStringAlphanumericNumeric.length() == 10);
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} method.
     */
    @Test
    public void testRandomIntBooleanBooleanCharArrayRandom() {
        // Test with includeAlphanumeric = true, includeNumeric = true, and custom character set
        char[] customChars = {'a', 'b', 'c'};
        String randomStringCustom = RandomStringUtils.random(10, 0, 0, true, true, customChars, RANDOM);
        assertFalse(randomStringCustom.isEmpty());
        assertTrue(randomStringCustom.length() == 10);

        for (int i = 0; i < randomStringCustom.length(); i++) {
            assertEquals(customChars[RANDOM.nextInt(customChars.length)], randomStringCustom.charAt(i));
        }
    }

    /**
     * Test for {@link RandomStringUtils#random(int, int, boolean, boolean, char[], Random)} method with empty custom character set
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomIntBooleanBooleanCharArrayEmpty() {
        String randomStringEmpty = RandomStringUtils.random(10, 0, 0, true, true, null, RANDOM);
        fail("Expected IllegalArgumentException: The CharSequence must not be empty.");
    }
}