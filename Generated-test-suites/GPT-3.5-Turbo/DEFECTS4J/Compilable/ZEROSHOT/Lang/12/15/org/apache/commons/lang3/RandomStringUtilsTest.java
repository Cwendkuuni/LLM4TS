package org.apache.commons.lang3;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 10;
        String result = RandomStringUtils.random(count);
        assertNotNull(result);
        assertEquals(count, result.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 10;
        String result = RandomStringUtils.randomAscii(count);
        assertNotNull(result);
        assertEquals(count, result.length());
        for (int i = 0; i < count; i++) {
            assertTrue(result.charAt(i) >= 32 && result.charAt(i) <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String result = RandomStringUtils.randomAlphabetic(count);
        assertNotNull(result);
        assertEquals(count, result.length());
        for (int i = 0; i < count; i++) {
            assertTrue(Character.isLetter(result.charAt(i)));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String result = RandomStringUtils.randomAlphanumeric(count);
        assertNotNull(result);
        assertEquals(count, result.length());
        for (int i = 0; i < count; i++) {
            assertTrue(Character.isLetterOrDigit(result.charAt(i)));
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 10;
        String result = RandomStringUtils.randomNumeric(count);
        assertNotNull(result);
        assertEquals(count, result.length());
        for (int i = 0; i < count; i++) {
            assertTrue(Character.isDigit(result.charAt(i)));
        }
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        int count = 10;
        boolean letters = true;
        boolean numbers = true;
        String result = RandomStringUtils.random(count, letters, numbers);
        assertNotNull(result);
        assertEquals(count, result.length());
        for (int i = 0; i < count; i++) {
            assertTrue(Character.isLetterOrDigit(result.charAt(i)));
        }
    }

    @Test
    public void testRandomWithStartAndEnd() {
        int count = 10;
        int start = 65;
        int end = 90;
        boolean letters = true;
        boolean numbers = false;
        String result = RandomStringUtils.random(count, start, end, letters, numbers);
        assertNotNull(result);
        assertEquals(count, result.length());
        for (int i = 0; i < count; i++) {
            assertTrue(result.charAt(i) >= start && result.charAt(i) <= end);
        }
    }

    @Test
    public void testRandomWithChars() {
        int count = 10;
        char[] chars = {'A', 'B', 'C'};
        String result = RandomStringUtils.random(count, chars);
        assertNotNull(result);
        assertEquals(count, result.length());
        for (int i = 0; i < count; i++) {
            assertTrue(isCharInArray(result.charAt(i), chars));
        }
    }

    @Test
    public void testRandomWithStringChars() {
        int count = 10;
        String chars = "ABC";
        String result = RandomStringUtils.random(count, chars);
        assertNotNull(result);
        assertEquals(count, result.length());
        for (int i = 0; i < count; i++) {
            assertTrue(isCharInArray(result.charAt(i), chars.toCharArray()));
        }
    }

    private boolean isCharInArray(char ch, char[] array) {
        for (char c : array) {
            if (ch == c) {
                return true;
            }
        }
        return false;
    }
}