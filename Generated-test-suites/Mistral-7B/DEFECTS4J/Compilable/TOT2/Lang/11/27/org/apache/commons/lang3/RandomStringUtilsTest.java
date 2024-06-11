package org.apache.commons.lang3;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length of the generated random string should be equal to the specified count", count, randomString.length());
        // Checking for basic functionality
        assertNotNull("Generated random string should not be null", randomString);
        // Testing edge cases and exception handling scenarios
        try {
            RandomStringUtils.random(-1);
            fail("Should have thrown IllegalArgumentException for negative count");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        try {
            RandomStringUtils.random((int) Long.MAX_VALUE);
            fail("Should have thrown ArrayIndexOutOfBoundsException for Integer.MAX_VALUE");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testRandomAscii() {
        int count = 10;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals("Length of the generated random Ascii string should be equal to the specified count", count, randomAsciiString.length());
        // Checking for basic functionality
        assertNotNull("Generated random Ascii string should not be null", randomAsciiString);
        // Testing edge cases and exception handling scenarios
        try {
            RandomStringUtils.randomAscii(-1);
            fail("Should have thrown IllegalArgumentException for negative count");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        try {
            RandomStringUtils.randomAscii((int) Long.MAX_VALUE);
            fail("Should have thrown ArrayIndexOutOfBoundsException for Integer.MAX_VALUE");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    // Test other methods (randomAlphabetic, randomAlphanumeric, randomNumeric, random, random, random, random) similarly

    
    // Test methods here
    
}