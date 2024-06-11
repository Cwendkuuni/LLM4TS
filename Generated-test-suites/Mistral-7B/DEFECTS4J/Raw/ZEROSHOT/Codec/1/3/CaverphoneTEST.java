package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Random;

/**
 * Test class for Caverphone encoder
 */
public class CaverphoneTest {

    private Random random = new Random();

    /**
     * Test encoder with various inputs
     */
    @Test
    public void testCaverphone() {
        Caverphone encoder = new Caverphone();

        // Test empty string
        assertEquals("1111111111", encoder.caverphone(""));

        // Test single character
        for (char c = 'a'; c <= 'z'; c++) {
            String input = Character.toString(c);
            String output = encoder.caverphone(input);
            System.out.println("Input: " + input + ", Output: " + output);
            assertEquals("A" + repeat("2", output.length() - 1), output);
        }

        // Test some common words
        String[] words = {"apple", "banana", "cat", "dog", "elephant", "hello", "yes"};
        for (String word : words) {
            String input = word;
            String output = encoder.caverphone(input);
            System.out.println("Input: " + input + ", Output: " + output);
            String expected = getExpectedCaverphone(input);
            assertEquals(expected, output);
        }

        // Test random strings
        for (int i = 0; i < 10; i++) {
            int length = random.nextInt(20) + 1;
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < length; j++) {
                sb.append((char) ('a' + random.nextInt(26)));
            }
            String input = sb.toString();
            String output = encoder.caverphone(input);
            System.out.println("Input: " + input + ", Output: " + output);
            String expected = getExpectedCaverphone(input);
            assertEquals(expected, output);
        }
    }

    /**
     * Helper method to generate expected caverphone codes for given strings
     *
     * @param input String to generate caverphone code for
     * @return Expected caverphone code for the given string
     */
    private String getExpectedCaverphone(String input) {
        String expected = input.toLowerCase();
        // Handle various start options
        expected = expected.replaceAll("^cough", "cou2f");
        expected = expected.replaceAll("^rough", "rou2f");
        expected = expected.replaceAll("^tough", "tou2f");
        expected = expected.replaceAll("^enough", "enou2f");
        expected = expected.replaceAll("^trough", "trou2f");
        expected = expected.replaceAll("^gn", "2n");
        expected = expected.replaceAll("^mb", "m2");

        // Handle replacements
        expected = expected.replaceAll("cq", "2q");
        expected = expected.replaceAll("ci", "si");
        expected = expected.replaceAll("ce", "se");
        expected = expected.replaceAll("cy", "sy");
        expected = expected.replaceAll("tch", "2ch");
        expected = expected.replaceAll("c", "k");
        expected = expected.replaceAll("q", "k");
        expected = expected.replaceAll("x", "k");
        expected = expected.replaceAll("v", "f");
        expected = expected.replaceAll("dg", "2g");
        expected = expected.replaceAll("tio", "sio");
        expected = expected.replaceAll("tia", "sia");
        expected = expected.replaceAll("d", "t");
        expected = expected.replaceAll("ph", "fh");
        expected = expected.replaceAll("b", "p");
        expected = expected.replaceAll("sh", "s2");
        expected = expected.replaceAll("z", "s");
        expected = expected.replaceAll("^[aeiou]", "A");
        expected = expected.replaceAll("[aeiou]", "3");
        expected = expected.replaceAll("j", "y");
        expected = expected.replaceAll("^y3", "Y3");
        expected = expected.replaceAll("^y", "A");
        expected = expected.replaceAll("y", "3");
        expected = expected.replaceAll("3gh3", "3kh3");
        expected = expected.replaceAll("gh", "22");
        expected = expected.replaceAll("g", "k");
        expected = expected.replaceAll("s+", "S");
        expected = expected.replaceAll("t+", "T");
        expected = expected.replaceAll("p+", "P");
        expected = expected.replaceAll("k+", "K");
        expected = expected.replaceAll("f+", "F");
        expected = expected.replaceAll("m+", "M");
        expected = expected.replaceAll("n+", "N");
        expected = expected.replaceAll("w3", "W3");
        //expected = expected.replaceAll("wy", "Wy");
        expected = expected.replaceAll("wh3", "Wh3");
        expected = expected.replaceAll("w$", "3");
        //expected = expected.replaceAll("why", "Why");
        expected = expected.replaceAll("w", "2");
        expected = expected.replaceAll("^h", "A");
        expected = expected.replaceAll("h", "2");
        expected = expected.replaceAll("r3", "R3");
        expected = expected.replaceAll("r$", "3");
        //expected = expected.replaceAll("ry", "Ry");
        expected = expected.replaceAll("r", "2");
        expected = expected.replaceAll("l3", "L3");
        expected = expected.replaceAll("l$", "3");
        //expected = expected.replaceAll("ly", "Ly");
        expected = expected.replaceAll("l", "2");
        //expected = expected.replaceAll("j", "y");
        //expected = expected.replaceAll("y3", "Y3");
        //expected = expected.replaceAll("y", "2");

        // Handle removals
        expected = expected.replaceAll("2", "");
        expected = expected.replaceAll("3$", "A");
        expected = expected.replaceAll("3", "");

        // put ten 1s on the end
        expected = expected.substring(0, 10) + repeat("1", 12);

        return expected;
    }

    /**
     * Helper method to generate a repeated string
     *
     * @param s String to repeat
     * @param n Number of repetitions
     * @return String consisting of the given string repeated n times
     */
    private String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Helper method to generate a repeated string
     *
     * @param s String to repeat
     * @return String consisting of the given string repeated once
     */
    private String repeat(String s) {
        return repeat(s, 1);
    }
}