import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        int count = 5;
        String randomString = RandomStringUtils.random(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
    }

    @Test
    public void testRandomAscii() {
        int count = 5;
        String randomString = RandomStringUtils.randomAscii(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(c >= 32 && c <= 126);
        }
    }

    @Test
    public void testRandomAlphabetic() {
        int count = 5;
        String randomString = RandomStringUtils.randomAlphabetic(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c));
        }
    }

    @Test
    public void testRandomAlphanumeric() {
        int count = 5;
        String randomString = RandomStringUtils.randomAlphanumeric(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c));
        }
    }

    @Test
    public void testRandomNumeric() {
        int count = 5;
        String randomString = RandomStringUtils.randomNumeric(count);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomEmptyChars() {
        int count = 5;
        String randomString = RandomStringUtils.random(count, (String) null);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomWithStartEnd() {
        char start = 'a';
        char end = 'z';
        int count = 5;
        String randomString = RandomStringUtils.random(count, start, end, false, false, null, RANDOM);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            assertTrue(start <= c && c <= end);
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomInvalidCharsRange() {
        RandomStringUtils.random(1, 'z', 'a', false, false, null, RANDOM);
    }

    @Test
    public void testRandomWithLettersNumbers() {
        boolean letters = true;
        boolean numbers = true;
        int count = 5;
        String randomString = RandomStringUtils.random(count, letters, numbers);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
        for (char c : randomString.toCharArray()) {
            if (letters) {
                assertTrue(Character.isAlphabetic(c) || c == '\uD800' || c == '\uDBFF');
            }
            if (numbers) {
                assertTrue(Character.isDigit(c) || c == '\uD800' || c == '\uDBFF');
            }
        }
    }

    @Test
    public void testRandomWithEmptyCharsArray() {
        int count = 5;
        String randomString = RandomStringUtils.random(count, (char[]) null);
        assertEquals("", randomString);
    }

    @Test
    public void testRandomWithCustomRandom() {
        int count = 5;
        Random customRandom = new Random(123);
        String randomString = RandomStringUtils.random(count, 0, 0, false, false, null, customRandom);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());
    }

}