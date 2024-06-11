package org.apache.commons.lang3;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandomString() {
        int count = 10;
        String randomString = RandomStringUtils.random(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertFalse("Random character should not be the same as previous one", randomString.charAt(i) == randomString.charAt(i + 1));
        }
        
        // Add test cases for edge cases and exception handling scenarios here
        
    }

    @Test
    public void testRandomAsciiString() {
        int count = 10;
        String randomString = RandomStringUtils.randomAscii(count);
        assertEquals("Length should match", count, randomString.length());
        for (int i = 0; i < count; i++) {
            assertFalse("Random character should not be the same as previous one", randomString.charAt(i) == randomString.charAt(i + 1));
            assertTrue("Random character should be an ASCII character", (randomString.charAt(i) >= 0x20 && randomString.charAt(i) <= 0x7E));
        }
        
        // Add test cases for edge cases and exception handling scenarios for randomAscii method here
        
    }

    // Add more test methods for other methods in the RandomStringUtils class
}