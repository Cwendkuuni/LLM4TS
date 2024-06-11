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
        String random = RandomStringUtils.randomAscii(10);
        assertNotNull(random);
        assertEquals(10, random.length());
        for (char c : random.toCharArray()) {
            assertTrue(c >= 32 && c <= 127);
        }
    }
    
    @Test
    public void testRandomAlphabetic() {
        String random = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(random);
        assertEquals(10, random.length());
        for (char c : random.toCharArray()) {
            assertTrue(Character.isLetter(c));
        }
    }
    
    @Test
    public void testRandomAlphanumeric() {
        String random = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(random);
        assertEquals(10, random.length());
        for (char c : random.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }
    
    @Test
    public void testRandomNumeric() {
        String random = RandomStringUtils.randomNumeric(10);
        assertNotNull(random);
        assertEquals(10, random.length());
        for (char c : random.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    public void testRandomWithStartEnd() {
        String random = RandomStringUtils.random(10, 'A', 'Z', true, false);
        assertNotNull(random);
        assertEquals(10, random.length());
        for (char c : random.toCharArray()) {
            assertTrue(c >= 'A' && c <= 'Z');
        }
    }
    
    @Test
    public void testRandomWithChars() {
        String random = RandomStringUtils.random(10, "ABCDEF");
        assertNotNull(random);
        assertEquals(10, random.length());
        for (char c : random.toCharArray()) {
            assertTrue(c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E' || c == 'F');
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        String random = RandomStringUtils.random(-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyChars() {
        String random = RandomStringUtils.random(10, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNullChars() {
        String random = RandomStringUtils.random(10, (String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharArray() {
        String random = RandomStringUtils.random(10, new char[]{});
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomWithInvalidRange() {
        String random = RandomStringUtils.random(10, 100, 50, true, false);
    }
}