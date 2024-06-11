package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(10);
        assertNotNull(result);
        assertEquals(10, result.length());
    }

    @Test
    public void testRandomAscii() {
        String result = RandomStringUtils.randomAscii(10);
        assertNotNull(result);
        assertEquals(10, result.length());
    }

    @Test
    public void testRandomAlphabetic() {
        String result = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(result);
        assertEquals(10, result.length());
        assertTrue(result.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomAlphanumeric() {
        String result = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(result);
        assertEquals(10, result.length());
        assertTrue(result.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomNumeric() {
        String result = RandomStringUtils.randomNumeric(10);
        assertNotNull(result);
        assertEquals(10, result.length());
        assertTrue(result.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        String result = RandomStringUtils.random(10, true, true);
        assertNotNull(result);
        assertEquals(10, result.length());
        assertTrue(result.matches("[a-zA-Z0-9]+"));
    }

    @Test
    public void testRandomWithLettersOnly() {
        String result = RandomStringUtils.random(10, true, false);
        assertNotNull(result);
        assertEquals(10, result.length());
        assertTrue(result.matches("[a-zA-Z]+"));
    }

    @Test
    public void testRandomWithNumbersOnly() {
        String result = RandomStringUtils.random(10, false, true);
        assertNotNull(result);
        assertEquals(10, result.length());
        assertTrue(result.matches("[0-9]+"));
    }

    @Test
    public void testRandomWithCustomChars() {
        String result = RandomStringUtils.random(10, 65, 90, false, false);
        assertNotNull(result);
        assertEquals(10, result.length());
        assertTrue(result.matches("[A-Z]+"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        RandomStringUtils.random(10, new char[]{});
    }

    @Test
    public void testRandomWithStringOfChars() {
        String result = RandomStringUtils.random(10, "abc");
        assertNotNull(result);
        assertEquals(10, result.length());
        assertTrue(result.matches("[abc]+"));
    }
}