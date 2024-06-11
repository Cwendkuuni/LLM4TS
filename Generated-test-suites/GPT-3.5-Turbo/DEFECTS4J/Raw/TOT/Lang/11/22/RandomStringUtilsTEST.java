
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {
    
    // Random
    //-----------------------------------------------------------------------
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }
    
    @Test
    public void testRandomAscii() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }
    
    @Test
    public void testRandomCustom() {
        char[] chars = {'a', 'b', 'c'};
        String randomString = RandomStringUtils.random(10, chars);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c == 'a' || c == 'b' || c == 'c');
        }
    }
    
    // Additional Test Cases
    //-----------------------------------------------------------------------
    @Test
    public void testRandomWithNegativeCount() {
        try {
            RandomStringUtils.random(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Requested random string length -1 is less than 0.", e.getMessage());
        }
    }
    
    @Test
    public void testRandomWithEmptyCharSet() {
        try {
            RandomStringUtils.random(10, new char[]{});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The chars array must not be empty", e.getMessage());
        }
    }

    @Test
    public void testRandomWithInvalidCharSetBounds() {
        try {
            RandomStringUtils.random(10, 'c', 'a', false, false);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("c > a", e.getMessage());
        }
    }
    
    @Test
    public void testRandomWithInvalidCharSetBoundsAndCharsArray() {
        try {
            RandomStringUtils.random(10, 'a', 'c', false, false, new char[]{'a'});
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("1 > 0", e.getMessage());
        }
    }

}

