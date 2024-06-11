package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Random;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 10;
        String result = RandomStringUtils.random(count);
        assertEquals(count, result.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 10;
        String result = RandomStringUtils.randomAscii(count);
        assertEquals(count, result.length());

        for (char c : result.toCharArray()) {
            assertTrue((int) c >= 32 && (int) c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 10;
        String result = RandomStringUtils.randomAlphabetic(count);
        assertEquals(count, result.length());

        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 10;
        String result = RandomStringUtils.randomAlphanumeric(count);
        assertEquals(count, result.length());

        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 10;
        String result = RandomStringUtils.randomNumeric(count);
        assertEquals(count, result.length());

        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomStartEnd() {
        int count = 10;
        int start = 65; // 'A'
        int end = 90; // 'Z'
        String result = RandomStringUtils.random(count, start, end, true, false);
        assertEquals(count, result.length());

        for (char c : result.toCharArray()) {
            assertTrue((int) c >= start && (int) c <= end);
        }
    }

    @Test
    public void testRandomChars() {
        int count = 10;
        char[] chars = new char[] {'A', 'B', 'C', 'D', 'E'};
        String result = RandomStringUtils.random(count, chars);
        assertEquals(count, result.length());

        for (char c : result.toCharArray()) {
            assertTrue(new String(chars).contains(String.valueOf(c)));
        }
    }

}