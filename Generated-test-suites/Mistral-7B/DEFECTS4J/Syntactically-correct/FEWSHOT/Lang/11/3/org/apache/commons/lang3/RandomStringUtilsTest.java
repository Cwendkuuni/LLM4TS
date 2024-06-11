package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Random;

@RunWith(Parameterized.class)
public class RandomStringUtilsTest {

    private int count;
    private boolean letters;
    private boolean numbers;
    private char[] chars;
    private Random random;

    public RandomStringUtilsTest(int count, boolean letters, boolean numbers, char[] chars, Random random) {
        this.count = count;
        this.letters = letters;
        this.numbers = numbers;
        this.chars = chars;
        this.random = random;
    }

    @Parameters
    public static Object[] data() {
        Object[] data = new Object[8][5];

        data[0] = new Object[]{5, false, false, null, new Random()};
        data[1] = new Object[]{10, true, false, null, new Random()};
        data[2] = new Object[]{15, false, true, null, new Random()};
        data[3] = new Object[]{10, true, true, null, new Random()};
        data[4] = new Object[]{5, false, false, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray(), new Random()};
        data[5] = new Object[]{10, true, true, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray(), new Random()};
        data[6] = new Object[]{5, true, false, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(), new Random()};
        data[7] = new Object[]{10, false, true, "0123456789".toCharArray(), new Random()};

        return data;
    }

    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(count, letters, numbers);
        assertNotNull(randomString);
        assertEquals(count, randomString.length());

        if (letters && numbers) {
            assertArrayEquals("Expected random string to have letters and digits",
                    new String(new char[]{'a', 'b', '0', 'c', '5', 'e', 'f', 'g'}).toCharArray(),
                    randomString.subSequence(0, 8).toCharArray());
        } else if (letters) {
            assertArrayEquals("Expected random string to have letters",
                    new String(new char[]{'a', 'b', 'c', 'd', 'e', 'f'}).toCharArray(),
                    randomString.subSequence(0, 6).toCharArray());
        } else if (numbers) {
            assertArrayEquals("Expected random string to have digits",
                    new String(new char[]{'0', '1', '2', '3', '4', '5'}).toCharArray(),
                    randomString.subSequence(0, 6).toCharArray());
        } else {
            assertArrayEquals("Expected random string to have no letters or digits",
                    new String(new char[]{'!', '.', '@'}).toCharArray(),
                    randomString.subSequence(0, 3).toCharArray());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-5, letters, numbers, chars, random);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomEmptyCharsArray() {
        RandomStringUtils.random(5, letters, numbers, null, random);
    }

}