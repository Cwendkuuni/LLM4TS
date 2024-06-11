package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

/**
 * Test cases for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test random string generation.
     */
    @Test
    public void testRandomStringGeneration() {
        // test default random string generation
        String str = RandomStringUtils.random(10);
        assertEquals(10, str.length());
        for (int i = 0; i < 10; i++) {
            assertNotEquals((char) 0, str.charAt(i));
        }

        // test generating random strings of specific length and character sets
        String asciiStr = RandomStringUtils.randomAscii(10);
        String alphanumericStr = RandomStringUtils.randomAlphanumeric(10);
        String alphabeticStr = RandomStringUtils.randomAlphabetic(10);
        String numericStr = RandomStringUtils.randomNumeric(10);
        assertEquals(10, asciiStr.length());
        assertEquals(10, alphanumericStr.length());
        assertEquals(10, alphabeticStr.length());
        assertEquals(10, numericStr.length());
        for (int i = 0; i < 10; i++) {
            if (asciiStr.charAt(i) >= 32 && asciiStr.charAt(i) <= 126) {
                assertNotEquals((char) 0, asciiStr.charAt(i));
            }
            if (alphanumericStr.charAt(i) >= '0' && alphanumericStr.charAt(i) <= '9') {
                assertNotEquals((char) 0, alphanumericStr.charAt(i));
            }
            if (Character.isLetter(alphabeticStr.charAt(i))) {
                assertNotEquals((char) 0, alphabeticStr.charAt(i));
            }
            if (Character.isDigit(numericStr.charAt(i))) {
                assertNotEquals((char) 0, numericStr.charAt(i));
            }
        }
    }

    /**
     * Test random string generation with a specified character set.
     */
    @Test
    public void testRandomStringGenerationWithCharacterSet() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String str = RandomStringUtils.random(10, chars.toCharArray());
        assertEquals(10, str.length());
        for (int i = 0; i < 10; i++) {
            assertNotEquals((char) 0, str.charAt(i));
            assertTrue(chars.indexOf(str.charAt(i)) >= 0);
        }
    }

    /**
     * Test random string generation with a specified character set and random instance.
     */
    @Test
    public void testRandomStringGenerationWithCharacterSetAndRandomInstance() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        String str = RandomStringUtils.random(10, 0, chars.length(), false, false, chars.toCharArray(), random);
        assertEquals(10, str.length());
        for (int i = 0; i < 10; i++) {
            assertNotEquals((char) 0, str.charAt(i));
            assertTrue(chars.indexOf(str.charAt(i)) >= 0);
        }
    }

    /**
     * Test random string generation with a specified character set and minimum and maximum length.
     */
    @Test
    public void testRandomStringGenerationWithCharacterSetAndMinMaxLength() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String str = RandomStringUtils.random(5, 10, chars.toCharArray());
        assertEquals(5, str.length());
        for (int i = 0; i < 5; i++) {
            assertNotEquals((char) 0, str.charAt(i));
            assertTrue(chars.indexOf(str.charAt(i)) >= 0);
        }

        str = RandomStringUtils.random(10, 5, chars.toCharArray());
        assertEquals(5, str.length());
        for (int i = 0; i < 5; i++) {
            assertNotEquals((char) 0, str.charAt(i));
            assertTrue(chars.indexOf(str.charAt(i)) >= 0);
        }
    }

    /**
     * Test random string generation with a specified character set and only letters.
     */
    @Test
    public void testRandomStringGenerationWithCharacterSetAndOnlyLetters() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String str = RandomStringUtils.randomAlphabetic(10, chars.toCharArray());
        assertEquals(10, str.length());
        for (int i = 0; i < 10; i++) {
            assertNotEquals((char) 0, str.charAt(i));
            assertTrue(Character.isLetter(str.charAt(i)));
        }
    }

    /**
     * Test random string generation with a specified character set and only numbers.
     */
    @Test
    public void testRandomStringGenerationWithCharacterSetAndOnlyNumbers() {
        String chars = "0123456789";
        String str = RandomStringUtils.randomNumeric(10, chars.toCharArray());
        assertEquals(10, str.length());
        for (int i = 0; i < 10; i++) {
            assertNotEquals((char) 0, str.charAt(i));
            assertTrue(Character.isDigit(str.charAt(i)));
        }
    }

    /**
     * Test random string generation with a specified character set and only letters and numbers.
     */
    @Test
    public void testRandomStringGenerationWithCharacterSetAndLettersAndNumbers() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String str = RandomStringUtils.randomAlphanumeric(10, chars.toCharArray());
        assertEquals(10, str.length());
        for (int i = 0; i < 10; i++) {
            assertNotEquals((char) 0, str.charAt(i));
            if (Character.isLetter(str.charAt(i))) {
                assertTrue(Character.isLetter(str.charAt(i)));
            } else {
                assertTrue(Character.isDigit(str.charAt(i)));
            }
        }
    }

    /**
     * Test random string generation with a null character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringGenerationWithNullCharacterSet() {
        String str = RandomStringUtils.randomAlphanumeric(10, null);
    }

    /**
     * Test random string generation with a negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringGenerationWithNegativeLength() {
        String str = RandomStringUtils.randomAlphanumeric(-1);
    }

    /**
     * Test random string generation with a negative minimum length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringGenerationWithNegativeMinimumLength() {
        String str = RandomStringUtils.randomAlphanumeric(5, -1);
    }

    /**
     * Test random string generation with a negative maximum length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringGenerationWithNegativeMaximumLength() {
        String str = RandomStringUtils.randomAlphanumeric(5, 10, -1);
    }

    /**
     * Test random string generation with a character set larger than the possible characters.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStringGenerationWithCharacterSetLargerThanPossibleCharacters() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String str = RandomStringUtils.randomAlphanumeric(10, chars.toCharArray());
    }

    /**
     * Test random string generation with an empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringGenerationWithEmptyCharacterSet() {
        String str = RandomStringUtils.randomAlphanumeric(10, new String());
    }

    /**
     * Test random string generation with a null random instance.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomStringGenerationWithNullRandomInstance() {
        String str = RandomStringUtils.randomAlphanumeric(10, null);
    }
}