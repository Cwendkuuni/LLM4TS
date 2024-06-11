package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(5);
        assertEquals(5, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(5);
        assertTrue(randomAsciiString.matches("[ -~]+")); // Check if string only contains printable ASCII characters
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
        assertTrue(randomAlphabeticString.matches("[a-zA-Z]+")); // Check if string only contains alphabetic characters
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
        assertTrue(randomAlphanumericString.matches("[a-zA-Z0-9]+")); // Check if string only contains alphanumeric characters
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(5);
        assertTrue(randomNumericString.matches("[0-9]+")); // Check if string only contains numeric characters
    }

    @Test
    public void testRandomWithCustomRange() {
        String randomString = RandomStringUtils.random(5, 65, 90, true, false); // Only uppercase alphabetic characters
        assertTrue(randomString.matches("[A-Z]+")); 
    }

    @Test
    public void testRandomWithCustomCharacters() {
        String randomString = RandomStringUtils.random(5, new char[] {'a', 'b', 'c', 'd', 'e'});
        assertTrue(randomString.matches("[abcde]+")); // Check if string only contains the specified characters
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeLength() {
        RandomStringUtils.random(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharArray() {
        RandomStringUtils.random(5, new char[] {});
    }

}