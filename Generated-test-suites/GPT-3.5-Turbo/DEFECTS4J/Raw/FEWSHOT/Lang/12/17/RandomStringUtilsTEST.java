package org.apache.commons.lang3;

import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {
    @Test
    public void testRandom() {
        // test for count = 0
        assertEquals("", RandomStringUtils.random(0));
        
        // test for count > 0
        assertEquals(5, RandomStringUtils.random(5).length());
        assertEquals(10, RandomStringUtils.random(10).length());
    }
    
    @Test
    public void testRandomAscii() {
        // test for count = 0
        assertEquals("", RandomStringUtils.randomAscii(0));
        
        // test for count > 0
        assertEquals(5, RandomStringUtils.randomAscii(5).length());
        assertEquals(10, RandomStringUtils.randomAscii(10).length());
    }
    
    @Test
    public void testRandomAlphabetic() {
        // test for count = 0
        assertEquals("", RandomStringUtils.randomAlphabetic(0));
        
        // test for count > 0
        assertEquals(5, RandomStringUtils.randomAlphabetic(5).length());
        assertEquals(10, RandomStringUtils.randomAlphabetic(10).length());
    }
    
    @Test
    public void testRandomAlphanumeric() {
        // test for count = 0
        assertEquals("", RandomStringUtils.randomAlphanumeric(0));
        
        // test for count > 0
        assertEquals(5, RandomStringUtils.randomAlphanumeric(5).length());
        assertEquals(10, RandomStringUtils.randomAlphanumeric(10).length());
    }
    
    @Test
    public void testRandomNumeric() {
        // test for count = 0
        assertEquals("", RandomStringUtils.randomNumeric(0));
        
        // test for count > 0
        assertEquals(5, RandomStringUtils.randomNumeric(5).length());
        assertEquals(10, RandomStringUtils.randomNumeric(10).length());
    }
    
    @Test
    public void testRandomWithChars() {
        // test for count = 0
        assertEquals("", RandomStringUtils.random(0, "abc"));
        
        // test for count > 0
        assertEquals(5, RandomStringUtils.random(5, "abc").length());
        assertEquals(10, RandomStringUtils.random(10, "abc").length());
    }
    
    @Test
    public void testRandomWithCharRange() {
        // test for count > 0
        assertEquals(5, RandomStringUtils.random(5, 'a', 'z').length());
        assertEquals(10, RandomStringUtils.random(10, 'a', 'z').length());
    }
}