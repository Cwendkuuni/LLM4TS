package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RandomStringUtilsTest {
    @Test
    public void testRandom() {
        assertEquals(5, RandomStringUtils.random(5).length());
        assertEquals(10, RandomStringUtils.random(10).length());
        assertEquals(0, RandomStringUtils.random(0).length());
    }

    @Test
    public void testRandomAscii() {
        assertEquals(5, RandomStringUtils.randomAscii(5).length());
        assertEquals(10, RandomStringUtils.randomAscii(10).length());
        assertEquals(0, RandomStringUtils.randomAscii(0).length());
    }
    
    @Test
    public void testRandomAlphabetic() {
        assertEquals(5, RandomStringUtils.randomAlphabetic(5).length());
        assertEquals(10, RandomStringUtils.randomAlphabetic(10).length());
        assertEquals(0, RandomStringUtils.randomAlphabetic(0).length());
    }
    
    @Test
    public void testRandomAlphanumeric() {
        assertEquals(5, RandomStringUtils.randomAlphanumeric(5).length());
        assertEquals(10, RandomStringUtils.randomAlphanumeric(10).length());
        assertEquals(0, RandomStringUtils.randomAlphanumeric(0).length());
    }
    
    @Test
    public void testRandomNumeric() {
        assertEquals(5, RandomStringUtils.randomNumeric(5).length());
        assertEquals(10, RandomStringUtils.randomNumeric(10).length());
        assertEquals(0, RandomStringUtils.randomNumeric(0).length());
    }
    
    @Test
    public void testRandomWithLettersAndNumbers() {
        assertEquals(5, RandomStringUtils.random(5, true, true).length());
        assertEquals(10, RandomStringUtils.random(10, true, true).length());
        assertEquals(0, RandomStringUtils.random(0, true, true).length());
    }
    
    @Test
    public void testRandomWithChars() {
        assertEquals(5, RandomStringUtils.random(5, "abc").length());
        assertEquals(10, RandomStringUtils.random(10, "abc").length());
        assertEquals(0, RandomStringUtils.random(0, "abc").length());
    }
    
    @Test
    public void testRandomWithCharRange() {
        assertEquals(5, RandomStringUtils.random(5, 97, 100, false, false).length());
        assertEquals(10, RandomStringUtils.random(10, 97, 100, false, false).length());
        assertEquals(0, RandomStringUtils.random(0, 97, 100, false, false).length());
    }
}