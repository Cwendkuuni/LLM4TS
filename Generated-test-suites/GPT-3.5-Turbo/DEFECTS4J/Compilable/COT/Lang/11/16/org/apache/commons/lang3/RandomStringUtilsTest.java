package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {
    
    @Test
    public void testRandom() {
        String random = RandomStringUtils.random(10);
        assertNotNull(random);
        assertEquals(10, random.length());
    }

    @Test
    public void testRandomAscii() {
        String randomAscii = RandomStringUtils.randomAscii(10);
        assertNotNull(randomAscii);
        assertEquals(10, randomAscii.length());
        assertTrue(isAscii(randomAscii));
    }
    
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabetic = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomAlphabetic);
        assertEquals(10, randomAlphabetic.length());
        assertTrue(isAlphabetic(randomAlphabetic));
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomAlphanumeric);
        assertEquals(10, randomAlphanumeric.length());
        assertTrue(isAlphanumeric(randomAlphanumeric));
    }
    
    @Test
    public void testRandomNumeric() {
        String randomNumeric = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomNumeric);
        assertEquals(10, randomNumeric.length());
        assertTrue(isNumeric(randomNumeric));
    }
    
    @Test
    public void testRandomWithCustomChars() {
        String random = RandomStringUtils.random(10, "abcdef");
        assertNotNull(random);
        assertEquals(10, random.length());
        assertTrue(isCustomChars(random, "abcdef"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        RandomStringUtils.random(10, new char[]{});
    }
    
    private boolean isAscii(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) > 127) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isAlphabetic(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isLetter(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isAlphanumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isLetterOrDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isCustomChars(String str, String chars) {
        for (int i = 0; i < str.length(); i++) {
            if (chars.indexOf(str.charAt(i)) == -1) {
                return false;
            }
        }
        return true;
    }
}