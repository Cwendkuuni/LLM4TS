package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class RandomStringUtilsTest {
    @Test
    public void testRandom() {
        assertNotNull(RandomStringUtils.random(10));
        assertEquals(10, RandomStringUtils.random(10).length());
    }
    
    @Test
    public void testRandomAscii() {
        assertNotNull(RandomStringUtils.randomAscii(10));
        assertEquals(10, RandomStringUtils.randomAscii(10).length());
    }
    
    @Test
    public void testRandomAlphabetic() {
        assertNotNull(RandomStringUtils.randomAlphabetic(10));
        assertEquals(10, RandomStringUtils.randomAlphabetic(10).length());
    }
    
    @Test
    public void testRandomAlphanumeric() {
        assertNotNull(RandomStringUtils.randomAlphanumeric(10));
        assertEquals(10, RandomStringUtils.randomAlphanumeric(10).length());
    }
    
    @Test
    public void testRandomNumeric() {
        assertNotNull(RandomStringUtils.randomNumeric(10));
        assertEquals(10, RandomStringUtils.randomNumeric(10).length());
    }
    
    @Test
    public void testRandomWithLettersAndNumbers() {
        assertNotNull(RandomStringUtils.random(10, true, true));
        assertEquals(10, RandomStringUtils.random(10, true, true).length());
    }
    
    @Test
    public void testRandomWithCharsArray() {
        char[] chars = {'a', 'b', 'c', '1', '2', '3'};
        assertNotNull(RandomStringUtils.random(10, chars));
        assertEquals(10, RandomStringUtils.random(10, chars).length());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-10);
    }
    
    @Test
    public void testRandomWithNullChars() {
        assertNotNull(RandomStringUtils.random(10, null));
        assertEquals(10, RandomStringUtils.random(10, null).length());
    }
    
    @Test
    public void testRandomWithEmptyChars() {
        assertNotNull(RandomStringUtils.random(10, ""));
        assertEquals(10, RandomStringUtils.random(10, "").length());
    }
    
    @Test
    public void testRandomWithStringChars() {
        assertNotNull(RandomStringUtils.random(10, "abc123"));
        assertEquals(10, RandomStringUtils.random(10, "abc123").length());
    }
}