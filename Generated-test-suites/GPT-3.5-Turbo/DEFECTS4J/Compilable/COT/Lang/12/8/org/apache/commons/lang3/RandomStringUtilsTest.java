package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        assertEquals(5, RandomStringUtils.random(5).length());
    }

    @Test
    public void testRandomAscii() {
        assertEquals(5, RandomStringUtils.randomAscii(5).length());
    }

    @Test
    public void testRandomAlphabetic() {
        assertEquals(5, RandomStringUtils.randomAlphabetic(5).length());
    }

    @Test
    public void testRandomAlphanumeric() {
        assertEquals(5, RandomStringUtils.randomAlphanumeric(5).length());
    }

    @Test
    public void testRandomNumeric() {
        assertEquals(5, RandomStringUtils.randomNumeric(5).length());
    }

    @Test
    public void testRandomWithLettersAndNumbers() {
        assertEquals(5, RandomStringUtils.random(5, true, true).length());
    }

    @Test
    public void testRandomWithLetters() {
        assertEquals(5, RandomStringUtils.random(5, true, false).length());
    }

    @Test
    public void testRandomWithNumbers() {
        assertEquals(5, RandomStringUtils.random(5, false, true).length());
    }

    @Test
    public void testRandomWithCharSet() {
        assertEquals(5, RandomStringUtils.random(5, "abcd1234").length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithNegativeCount() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomWithEmptyCharSet() {
        assertEquals(5, RandomStringUtils.random(5, "").length());
    }

    @Test
    public void testRandomWithNullCharSet() {
        assertEquals(5, RandomStringUtils.random(5, (String) null).length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyCharsArray() {
        RandomStringUtils.random(5, new char[] {});
    }

    @Test
    public void testRandomWithNullCharsArray() {
        assertEquals(5, RandomStringUtils.random(5, (char[]) null).length());
    }

}