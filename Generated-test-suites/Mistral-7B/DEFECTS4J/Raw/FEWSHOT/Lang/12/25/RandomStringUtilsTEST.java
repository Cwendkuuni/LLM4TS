package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class RandomStringUtilsTest {

    private int count;
    private boolean letters;
    private boolean numbers;
    private boolean expectedRandomStringIsEmpty;

    public RandomStringUtilsTest(int count, boolean letters, boolean numbers, boolean expectedRandomStringIsEmpty) {
        this.count = count;
        this.letters = letters;
        this.numbers = numbers;
        this.expectedRandomStringIsEmpty = expectedRandomStringIsEmpty;
    }

    @Parameterized.Parameters(name = "{index}: random({0}, letters={1}, numbers={2})")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0, false, false, true},
                {1, false, false, false},
                {1, true, false, false},
                {1, false, true, false},
                {1, true, true, false},
                {10, false, false, false},
                {10, true, false, false},
                {10, false, true, false},
                {10, true, true, false},
                {10, false, false, true},
                {10, true, false, true},
                {10, false, true, true},
                {10, true, true, true},
                {40, true, true, false},
                {-1, false, false, false},
                {-1, false, false, true},
                {-1, true, false, false},
                {-1, true, false, true},
                {-1, true, true, false},
                {-1, true, true, true},
                {Integer.MAX_VALUE, false, false, false},
                {Integer.MAX_VALUE, false, false, true},
                {Integer.MAX_VALUE, true, false, false},
                {Integer.MAX_VALUE, true, false, true},
                {Integer.MAX_VALUE, true, true, false},
                {Integer.MAX_VALUE, true, true, true},
                {Integer.MIN_VALUE, false, false, false},
                {Integer.MIN_VALUE, false, false, true},
                {Integer.MIN_VALUE, true, false, false},
                {Integer.MIN_VALUE, true, false, true},
                {Integer.MIN_VALUE, true, true, false},
                {Integer.MIN_VALUE, true, true, true},
        });
    }

    @Test
    public void testRandomString() {
        String randomString = RandomStringUtils.random(count, letters, numbers);

        if (expectedRandomStringIsEmpty) {
            assertNull(randomString);
        } else {
            assertNotNull(randomString);
            int length = randomString.length();
            for (int i = 0; i < length; i++) {
                char c = randomString.charAt(i);
                boolean isLetter = (letters && Character.isLetter(c));
                boolean isNumber = (numbers && Character.isDigit(c));
                if (isLetter ^ letters || isNumber ^ numbers) {
                    throw new AssertionError("Unexpected character found in random string: " + c);
                }
            }
            if (letters && !Character.isLetter(randomString.charAt(0))) {
                throw new AssertionError("Unexpected first character in random string: " + randomString.charAt(0));
            }
        }
    }

    @Test
    public void testRandomStringASCII() {
        String randomString = RandomStringUtils.randomAscii(count);

        assertTrue(randomString.matches("[\\x20-\\x7E]*"));
    }

    @Test
    public void testRandomStringAlphabetic() {
        String randomString = RandomStringUtils.randomAlphabetic(count);

        String expected = "";
        for (int i = 0; i < count; i++) {
            expected += (char) (RandomStringUtils.RANDOM.nextInt(26) + 'a');
        }

        assertEquals(expected, randomString);
    }

    @Test
    public void testRandomStringAlphanumeric() {
        String randomString = RandomStringUtils.randomAlphanumeric(count);

        String expected = "";
        for (int i = 0; i < count; i++) {
            char next = (char) (RandomStringUtils.RANDOM.nextInt(2) == 0 ? (char) (RandomStringUtils.RANDOM.nextInt(26) + 'a') : (char) (RandomStringUtils.RANDOM.nextInt(10) + '0'));
            expected += next;
        }

        assertEquals(expected, randomString);
    }

    @Test
    public void testRandomStringNumeric() {
        String randomString = RandomStringUtils.randomNumeric(count);

        String expected = "";
        for (int i = 0; i < count; i++) {
            expected += (char) ('0' + RandomStringUtils.RANDOM.nextInt(10));
        }

        assertEquals(expected, randomString);
    }

    @Test
    public void testRandomStringCustom() {
        String randomString = RandomStringUtils.random(count, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");

        String expected = "";
        for (int i = 0; i < count; i++) {
            expected += RandomStringUtils.RANDOM.nextInt(36) < 10 ? (char) ('0' + RandomStringUtils.RANDOM.nextInt(10)) : (char) (RandomStringUtils.RANDOM.nextInt(26) + 'a');
        }

        assertEquals(expected, randomString);
    }

    @Test
    public void testRandomStringCustomEmpty() {
        String randomString = RandomStringUtils.random(0, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");

        assertNull(randomString);
    }

    @Test
    public void testRandomStringCustomNull() {
        try {
            RandomStringUtils.random(1, null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}