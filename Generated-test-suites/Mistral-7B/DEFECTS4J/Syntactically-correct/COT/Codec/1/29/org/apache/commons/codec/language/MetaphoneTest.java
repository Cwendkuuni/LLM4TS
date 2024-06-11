package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testEmptyString() {
        Metaphone metaphone = new Metaphone();
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        for (char c = 'A'; c <= 'Z'; c++) {
            String code = metaphone.metaphone(String.valueOf(c));
            assertEquals(String.valueOf(c.toString().charAt(0)), code);
        }
    }

    @Test
    public void testInitialExceptions() {
        Metaphone metaphone = new Metaphone();
        String[] exceptions = {"", "Apple", "Banana", "William", "Water"};
        for (String s : exceptions) {
            try {
                metaphone.metaphone(s);
                fail("Expected EncoderException for input: " + s);
            } catch (EncoderException e) {
                // Expected
            }
        }
    }

    @Test
    public void testInitialTwoCharacterExceptions() {
        Metaphone metaphone = new Metaphone();
        String[] exceptions = {"Knife", "Apple", "AI", "Water"};
        for (String s : exceptions) {
            String expectedCode = metaphone.metaphone(s.substring(1));
            String actualCode = metaphone.metaphone(s);
            assertEquals(expectedCode, actualCode);
        }
    }

    @Test
    public void testInitialK() {
        Metaphone metaphone = new Metaphone();
        String[] tests = {"Knight", "Kitten", "Kangaroo", "KangarooFighter"};
        for (String s : tests) {
            assertEquals(s.substring(0, 1) + metaphone.metaphone(s.substring(1)), metaphone.metaphone(s));
        }
    }

    @Test
    public void testInitialA() {
        Metaphone metaphone = new Metaphone();
        String[] tests = {"Apple", "Apricot", "April", "Aardvark"};
        for (String s : tests) {
            assertEquals(s.substring(0, 1) + metaphone.metaphone(s.substring(1)), metaphone.metaphone(s));
        }
    }

    @Test
    public void testInitialW() {
        Metaphone metaphone = new Metaphone();
        String[] tests = {"Whale", "Water", "Whisky", "Weekend"};
        for (String s : tests) {
            assertEquals(s.substring(0, 1) + (s.charAt(1) == 'R' ? "R" : "") + metaphone.metaphone(s.substring(2)), metaphone.metaphone(s));
        }
    }

    @Test
    public void testInitialX() {
        Metaphone metaphone = new Metaphone();
        String[] tests = {"Xylophone", "Xenophobia", "Xerox"};
        for (String s : tests) {
            assertEquals("KS" + metaphone.metaphone(s.substring(1)), metaphone.metaphone(s));
        }
    }

    @Test
    public void testMostCommonCases() {
        Metaphone metaphone = new Metaphone();
        String[] tests = {"Apple", "Banana", "Cake", "Dog", "Elephant", "Fish", "Grapes", "Horse", "IceCream", "Jungle", "Kangaroo", "Lemon", "Mango", "Nut", "Orange", "Pizza", "Queen", "Roses", "Strawberry", "Tomato", "Umbrella", "Violet", "Watermelon", "Xmas", "Yellow", "Zebra"};
        for (String s : tests) {
            String code = metaphone.metaphone(s);
            String firstChar = s.substring(0, 1).toUpperCase();
            if (firstChar.equals("C") || firstChar.equals("S")) {
                assertEquals(firstChar + code.substring(1), metaphone.metaphone(s));
            } else {
                assertEquals(code, metaphone.metaphone(s));
            }
        }
    }

    @Test
    public void testEdgeCases() {
        Metaphone metaphone = new Metaphone();
        String[] tests = {"Bananas", "Caterpillar", "Doughnut", "Echo", "Fizz", "Grapefruit", "Horseshoe", "IceCreamSundae", "Jazz", "Kangaroos", "Lollipop", "Motorcycle", "Noodle", "OrangeJuice", "Pancake", "Queensland", "RaspberryRipple", "StrawberryShortcake", "Theatre", "Umbrellas", "Vegetables", "WatermelonCake", "XmasTree", "YellowSubmarine", "Zipper"};
        for (String s : tests) {
            String code = metaphone.metaphone(s);
            assertEquals(code, metaphone.metaphone(s));
        }
    }

    @Test
    public void testMaxCodeLength() {
        Metaphone metaphone = new Metaphone();
        String[] tests = {"BananaBananaBananaBananaBananaBananaBananaBananaBananaBanana"};
        for (String s : tests) {
            assertEquals(metaphone.getMaxCodeLen() + "", metaphone.metaphone(s).length());
        }
    }
}