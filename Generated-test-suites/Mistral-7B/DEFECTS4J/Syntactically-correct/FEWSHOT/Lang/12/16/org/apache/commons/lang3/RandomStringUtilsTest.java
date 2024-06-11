package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

@RunWith(Parameterized.class)
public class RandomStringUtilsTest {

    private int count;
    private boolean letters;
    private boolean numbers;
    private char[] expectedChars;

    public RandomStringUtilsTest(int count, boolean letters, boolean numbers, char[] expectedChars) {
        this.count = count;
        this.letters = letters;
        this.numbers = numbers;
        this.expectedChars = expectedChars;
    }

    @Parameterized.Parameters(name = "{index}: randomString({0}, {1}, {2}) = {3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {5, false, false, new char[0]},
            {5, true, false, new char[]{'a', 'b', 'c', 'd', 'e'}},
            {5, false, true, new char[]{'0', '1', '2', '3', '4'}},
            {5, true, true, new char[]{'a', '0', '1', 'b', '2'}},
            {5, false, false, null},
            {5, true, false, new char[]{'a', 'b', 'c', 'd', 'e', 'f'}},
            {5, false, true, new char[]{'0', '1', '2', '3', '4', '5'}},
            {5, true, true, new char[]{'a', '0', '1', 'b', '2', '3'}},
            {10, true, true, new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'}}
        });
    }

    @Test
    public void testRandomString() {
        String actual = RandomStringUtils.random(count, letters, numbers);
        if (expectedChars != null) {
            assertArrayEquals(expectedChars, actual.toCharArray());
        } else {
            if (letters) {
                assertTrue(actual.matches("^[a-zA-Z]+$"));
            }
            if (numbers) {
                assertTrue(actual.matches("^[0-9]+$"));
            }
            assertNotNull(actual);
            assertFalse(actual.isEmpty());
        }
    }
}