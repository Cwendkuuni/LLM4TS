package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.regex.Pattern;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
        assertTrue(Pattern.matches("[a-zA-Z0-9]{10}", randomString));
    }

    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomString.length());
        assertTrue(Pattern.matches("[!-~]{10}", randomString));
    }

    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomString.length());
        assertTrue(Pattern.matches("[a-zA-Z]{10}", randomString));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomString.length());
        assertTrue(Pattern.matches("[a-zA-Z0-9]{10}", randomString));
    }

    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomString.length());
        assertTrue(Pattern.matches("[0-9]{10}", randomString));
    }

    @Test
    public void testRandomWithSpecificChars() {
        String randomString = RandomStringUtils.random(10, new char[]{'a', 'b', 'c'});
        assertEquals(10, randomString.length());
        assertTrue(Pattern.matches("[abc]{10}", randomString));
    }

    @Test
    public void testRandomWithRange() {
        String randomString = RandomStringUtils.random(10, 'a', 'z', true, false);
        assertEquals(10, randomString.length());
        assertTrue(Pattern.matches("[a-z]{10}", randomString));
    }

    @Test
    public void testRandomWithLettersOnly() {
        String randomString = RandomStringUtils.random(10, true, false);
        assertEquals(10, randomString.length());
        assertTrue(Pattern.matches("[a-zA-Z]{10}", randomString));
    }

    @Test
    public void testRandomWithNumbersOnly() {
        String randomString = RandomStringUtils.random(10, false, true);
        assertEquals(10, randomString.length());
        assertTrue(Pattern.matches("[0-9]{10}", randomString));
    }

    @Test
    public void testRandomWithEmptyChars() {
        try {
            String randomString = RandomStringUtils.random(10, new char[]{});
        } catch (IllegalArgumentException e) {
            assertEquals("The provided chars array is empty.", e.getMessage());
        }
    }

    @Test
    public void testRandomWithNegativeCount() {
        try {
            String randomString = RandomStringUtils.random(-10);
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length -10 is less than 0.", e.getMessage());
        }
    }

    @Test
    public void testRandomWithNullChars() {
        String randomString = RandomStringUtils.random(10, (char[]) null);
        assertEquals(10, randomString.length());
        assertTrue(Pattern.matches("[a-zA-Z0-9]{10}", randomString));
    }

    @Test
    public void testRandomWithNullSource() {
        String randomString = RandomStringUtils.random(10, "abc");
        assertEquals(10, randomString.length());
        assertTrue(Pattern.matches("[abc]{10}", randomString));
    }

    @Test
    public void testRandomWithEmptySource() {
        try {
            String randomString = RandomStringUtils.random(10, "");
        } catch (IllegalArgumentException e) {
            assertEquals("The string containing the set of characters to use is empty.", e.getMessage());
        }
    }

}