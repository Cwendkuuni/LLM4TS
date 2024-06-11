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
            assertTrue(c >= 32 && c <= 127);
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
    public void testRandomWithCharacters() {
        int count = 10;
        char[] chars = {'a', 'b', 'c'};
        String result = RandomStringUtils.random(count, chars);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(c == 'a' || c == 'b' || c == 'c');
        }
    }
    
    @Test
    public void testRandomWithStartAndEnd() {
        int count = 10;
        int start = 10;
        int end = 20;
        String result = RandomStringUtils.random(count, start, end, true, false);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetter(c) && c >= start && c < end);
        }
    }
    
    @Test
    public void testRandomWithLettersAndNumbers() {
        int count = 10;
        boolean letters = true;
        boolean numbers = true;
        String result = RandomStringUtils.random(count, letters, numbers);
        assertEquals(count, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
}