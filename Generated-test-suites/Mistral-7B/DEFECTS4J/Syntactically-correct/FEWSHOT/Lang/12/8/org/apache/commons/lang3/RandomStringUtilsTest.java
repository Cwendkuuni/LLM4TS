package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class RandomStringUtilsParamTest {

    private int count;
    private boolean letters;
    private boolean numbers;
    private char[] chars;
    private Random random;
    private String expected;

    public RandomStringUtilsParamTest(int count, boolean letters, boolean numbers, char[] chars, String expected, Random random) {
        this.count = count;
        this.letters = letters;
        this.numbers = numbers;
        this.chars = chars;
        this.expected = expected;
        this.random = random;
    }

    @Parameterized.Parameters(name = "count={0}, letters={1}, numbers={2}, chars={3}")
    public static Iterable<Object[]> data() {
        return new IteratorDataBuilder()
                .add(10, true, true, null, "0wXlMpVgjKbEhNqRtSdZa", new Random())
                .add(2, false, false, null, "", new Random())
                .add(15, false, false, null, "123456789012345", new Random())
                .add(13, true, false, null, "abcdefghijabcdefg", new Random())
                .add(11, true, true, null, "0wXlMpVgjKbEhNqRtSdZa0wX", new Random())
                .add(5, true, true, "abcdef".toCharArray(), "abcde", new Random())
                .add(12, true, true, "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(), "ABCDEFGHIJKLMNOPQRSTUVWXYZ", new Random())
                .add(1, true, false, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray(), "A", new Random())
                .add(10, false, true, "1234567890".toCharArray(), "1234567890", new Random())
                .add(20, true, true, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray(), "RfQqWwLvPpIbQkGjXpMnGhJlEzTzCsNyUzIa", new Random())
                .add(1, true, false, new char[] { 'A' }, "A", new Random())
                .add(1, true, false, new char[] { 'z' }, "", new Random())
                .add(1, false, true, new char[] { '1' }, "1", new Random())
                .add(1, false, true, new char[] { '0' }, "", new Random())
                .add(1, false, false, null, "", new Random())
                .add(1, false, false, new char[] { '0' }, "", new Random())
                .add(1, false, false, new char[] { 'A' }, "", new Random())
                .build();
    }

    @Test
    public void testRandom() {
        String result = RandomStringUtils.random(count, letters, numbers, chars, random);
        if (expected != null) {
            assertEquals(expected, result);
        } else {
            assertNotNull(result);
        }
        if (chars != null) {
            assertTrue(result.matches(".*[\\p{javaLowerCase}].*"));
            assertTrue(result.matches(".*[\\p{javaUpperCase}].*"));
            if (numbers) {
                assertTrue(result.matches(".*\\d.*"));
            } else {
                assertFalse(result.matches(".*\\d.*"));
            }
        }
    }
}

class IteratorDataBuilder {
    private IteratorData data = new IteratorData();

    IteratorDataBuilder add(int count, boolean letters, boolean numbers, char[] chars, String expected, Random random) {
        data.addTestData(count, letters, numbers, chars, expected, random);
        return this;
    }

    IteratorData build() {
        return data;
    }
}

class IteratorData {
    private String testName;
    private int count;
    private boolean letters;
    private boolean numbers;
    private char[] chars;
    private String expected;
    private Random random;

    void addTestData(int count, boolean letters, boolean numbers, char[] chars, String expected, Random random) {
        this.testName = String.format("count=%d, letters=%b, numbers=%b, chars=%s, expected=%s", count, letters, numbers, chars != null ? chars.toString() : "null", expected);
        this.count = count;
        this.letters = letters;
        this.numbers = numbers;
        this.chars = chars;
        this.expected = expected;
        this.random = random;
    }

    @Override
    public String toString() {
        return testName;
    }

    void setTestData(int index, String testData) {
        String[] parts = testData.split(",");
        this.count = Integer.parseInt(parts[0]);
        this.letters = Boolean.parseBoolean(parts[1]);
        this.numbers = Boolean.parseBoolean(parts[2]);
        this.chars = parts[3].length() > 0 ? parts[3].toCharArray() : null;
        this.expected = parts[4];
    }

    public void testRandom() {
        String result = RandomStringUtils.random(count, letters, numbers, chars, random);
        if (expected != null) {
            assertEquals(expected, result);
        } else {
            assertNotNull(result);
        }
        if (chars != null) {
            assertTrue(result.matches(".*[\\p{javaLowerCase}].*"));
            assertTrue(result.matches(".*[\\p{javaUpperCase}].*"));
            if (numbers) {
                assertTrue(result.matches(".*\\d.*"));
            } else {
                assertFalse(result.matches(".*\\d.*"));
            }
        }
    }

    public void setUp() {
        testData = new String[] { "10,true,true,null,0wXlMpVgjKbEhNqRtSdZa,0wXlMpVgjKbEhNqRtSdZa",
                                    "2,false,false,null, ,",
                                    "15,false,false,null,123456789012345,123456789012345",
                                    "13,true,false,null,abcdefghijabcdefg,abcdefghijabcdefg",
                                    "11,true,true,null,0wXlMpVgjKbEhNqRtSdZa0wX,0wXlMpVgjKbEhNqRtSdZa0wX",
                                    "5,true,true,abcdef.toCharArray(),abcde,abcdef.toCharArray()",
                                    "12,true,true,ABCDEFGHIJKLMNOPQRSTUVWXYZ.toCharArray(),ABCDEFGHIJKLMNOPQRSTUVWXYZ,ABCDEFGHIJKLMNOPQRSTUVWXYZ.toCharArray()",
                                    "1,true,false,ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.toCharArray()[0],A",
                                    "10,false,true,1234567890.toCharArray(),1234567890,1234567890.toCharArray()",
                                    "20,true,true,ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.toCharArray(),RfQqWwLvPpIbQkGjXpMnGhJlEzTzCsNyUzIa,RfQqWwLvPpIbQkGjXpMnGhJlEzTzCsNyUzIa" };
    }

    String[] testData;
}