package org.apache.commons.lang3;

import static org.junit.Assert.*;
import java.util.Random;

import org.junit.Test;

public class RandomStringUtilsTest {
    
    @Test
    public void testRandom() {
        assertEquals("", RandomStringUtils.random(0));
        assertEquals("abcde", RandomStringUtils.random(5));
        assertEquals("abcde12345", RandomStringUtils.random(10));
        assertEquals("abcde12345", RandomStringUtils.random(10));
    }
    
    @Test
    public void testRandomAscii() {
        assertEquals("", RandomStringUtils.randomAscii(0));
        assertEquals("abcde", RandomStringUtils.randomAscii(5));
        assertEquals("!@#$%^", RandomStringUtils.randomAscii(6));
        assertEquals("abcdef!@#$%^12345", RandomStringUtils.randomAscii(16));
    }
    
    @Test
    public void testRandomAlphabetic() {
        assertEquals("", RandomStringUtils.randomAlphabetic(0));
        assertEquals("abcde", RandomStringUtils.randomAlphabetic(5));
        assertEquals("abcdef", RandomStringUtils.randomAlphabetic(6));
        assertEquals("abcdefg", RandomStringUtils.randomAlphabetic(7));
    }
    
    @Test
    public void testRandomAlphanumeric() {
        assertEquals("", RandomStringUtils.randomAlphanumeric(0));
        assertEquals("abcde", RandomStringUtils.randomAlphanumeric(5));
        assertEquals("abcdef", RandomStringUtils.randomAlphanumeric(6));
        assertEquals("abcdefg", RandomStringUtils.randomAlphanumeric(7));
    }
    
    @Test
    public void testRandomNumeric() {
        assertEquals("", RandomStringUtils.randomNumeric(0));
        assertEquals("12345", RandomStringUtils.randomNumeric(5));
        assertEquals("123456", RandomStringUtils.randomNumeric(6));
        assertEquals("1234567", RandomStringUtils.randomNumeric(7));
    }
    
    @Test
    public void testRandomStartEnd() {
        assertEquals("", RandomStringUtils.random(0, 0, 0, false, false, null, new Random()));
        assertEquals("abcde", RandomStringUtils.random(5, 97, 123, false, false, null, new Random()));
        assertEquals("ABCDE", RandomStringUtils.random(5, 65, 91, false, false, null, new Random()));
        assertEquals("abcdeABCDE", RandomStringUtils.random(10, 65, 123, false, false, null, new Random()));
    }
    
    @Test
    public void testRandomChars() {
        assertEquals("", RandomStringUtils.random(0, null));
        assertEquals("abcde", RandomStringUtils.random(5, "abcde"));
        assertEquals("ABCDE", RandomStringUtils.random(5, "ABCDE"));
        assertEquals("abcdeABCDE", RandomStringUtils.random(10, "abcdeABCDE"));
    }
    
    @Test
    public void testRandomCharsArray() {
        assertEquals("", RandomStringUtils.random(0, (char[]) null));
        assertEquals("abcde", RandomStringUtils.random(5, new char[] {'a','b','c','d','e'}));
        assertEquals("ABCDE", RandomStringUtils.random(5, new char[] {'A','B','C','D','E'}));
        assertEquals("abcdeABCDE", RandomStringUtils.random(10, new char[] {'a','b','c','d','e','A','B','C','D','E'}));
    }
}