package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(5);
        assertEquals(5, result.length());
    }

    @Test
    public void testRandomAscii() {
        String result = RandomStringUtils.randomAscii(5);
        assertEquals(5, result.length());
        assertTrue(result.matches("[\\x20-\\x7F]+"));
    }

    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(5);
        assertEquals(5, result.length());
        assertTrue(result.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(5);
        assertEquals(5, result.length());
        assertTrue(result.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(5);
        assertEquals(5, result.length());
        assertTrue(result.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String result = RandomStringUtils.random(5, true, true);
        assertEquals(5, result.length());
        assertTrue(result.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomWithStartAndEnd() {
        String result = RandomStringUtils.random(5, 32, 127, true, false);
        assertEquals(5, result.length());
        assertTrue(result.matches("[\\x20-\\x7F]+"));
    }

    @Test
    public void testRandomWithCharsArray() {
        char[] chars = new char[] {'a', 'b', 'c', 'd', 'e'};
        String result = RandomStringUtils.random(5, chars);
        assertEquals(5, result.length());
        assertTrue(result.matches("[abcde]+"));
    }

    @Test
    public void testRandomWithCharsString() {
        String result = RandomStringUtils.random(5, "abcde");
        assertEquals(5, result.length());
        assertTrue(result.matches("[abcde]+"));
    }

}