package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(10);
        assertEquals(10, result.length());
    }

    @Test
    public void testRandomAscii() {
        String result = RandomStringUtils.randomAscii(10);
        assertEquals(10, result.length());
        for(char c : result.toCharArray()){
            assertTrue((int)c >= 32 && (int)c <= 127);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, result.length());
        assertTrue(result.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, result.length());
        assertTrue(result.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(10);
        assertEquals(10, result.length());
        assertTrue(result.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String result = RandomStringUtils.random(10, true, true);
        assertEquals(10, result.length());
        assertTrue(result.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomWithCustomCharacters() {
        char[] chars = {'A', 'B', 'C'};
        String result = RandomStringUtils.random(10, chars);
        assertEquals(10, result.length());
        assertTrue(result.matches("[" + new String(chars) + "]+"));
    }

    @Test
    public void testRandomWithStringOfCharacters() {
        String chars = "ABC";
        String result = RandomStringUtils.random(10, chars);
        assertEquals(10, result.length());
        assertTrue(result.matches("[" + chars + "]+"));
    }

    @Test
    public void testRandomWithStartAndEnd() {
        String result = RandomStringUtils.random(10, 65, 90, true, false);
        assertEquals(10, result.length());
        for(char c : result.toCharArray()){
            assertTrue((int)c >= 65 && (int)c <= 90);
        }
    }
}