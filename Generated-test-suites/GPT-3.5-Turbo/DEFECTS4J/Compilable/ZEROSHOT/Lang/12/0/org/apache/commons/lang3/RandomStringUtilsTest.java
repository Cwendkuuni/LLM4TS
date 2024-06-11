package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 5;
        String randomString = RandomStringUtils.random(count);
        assertEquals(count, randomString.length());
    }
    
    @Test
    public void testRandomAscii() {
        int count = 5;
        String randomAsciiString = RandomStringUtils.randomAscii(count);
        assertEquals(count, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            assertTrue(randomAsciiString.charAt(i) >= 32 && randomAsciiString.charAt(i) <= 127);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        int count = 5;
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
        assertEquals(count, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            assertTrue(Character.isLetter(randomAlphabeticString.charAt(i)));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        int count = 5;
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
        assertEquals(count, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            assertTrue(Character.isLetterOrDigit(randomAlphanumericString.charAt(i)));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        int count = 5;
        String randomNumericString = RandomStringUtils.randomNumeric(count);
        assertEquals(count, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            assertTrue(Character.isDigit(randomNumericString.charAt(i)));
        }
    }
    
    @Test
    public void testRandomWithLettersAndNumbers() {
        int count = 5;
        boolean letters = true;
        boolean numbers = true;
        String randomString = RandomStringUtils.random(count, letters, numbers);
        assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(Character.isLetterOrDigit(randomString.charAt(i)));
        }
    }
    
    @Test
    public void testRandomWithStartAndEnd() {
        int count = 5;
        int start = 1;
        int end = 5;
        String randomString = RandomStringUtils.random(count, start, end, false, false, null);
        assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(randomString.charAt(i) >= start && randomString.charAt(i) <= end);
        }
    }
    
    @Test
    public void testRandomWithCharArray() {
        int count = 5;
        char[] chars = {'a', 'b', 'c', 'd', 'e'};
        String randomString = RandomStringUtils.random(count, chars);
        assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(chars.toString().contains(String.valueOf(randomString.charAt(i))));
        }
    }
    
    @Test
    public void testRandomWithEmptyCharArray() {
        int count = 5;
        char[] chars = {};
        try {
            RandomStringUtils.random(count, chars);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testRandomWithString() {
        int count = 5;
        String chars = "abcde";
        String randomString = RandomStringUtils.random(count, chars);
        assertEquals(count, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue(chars.contains(String.valueOf(randomString.charAt(i))));
        }
    }
    
    @Test
    public void testRandomWithEmptyString() {
        int count = 5;
        String chars = "";
        try {
            RandomStringUtils.random(count, chars);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

}