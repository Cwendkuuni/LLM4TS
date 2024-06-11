package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SoundexTest {

    private final String input;
    private final String expectedOutput;

    public SoundexTest(String input, String expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    @Parameterized.Parameters(name = "{index}: soundex({input}) = {expectedOutput}")
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
                {"apple", "A133"},
                {"Apple", "A133"},
                {"apples", "A133"},
                {"Apple's", "A133"},
                {"Apricot", "A161"},
                {"Apricots", "A161"},
                {"Banana", "B135"},
                {"Bananas", "B135"},
                {"Cherry", "CH62"},
                {"Cherries", "CH62"},
                {"Elephant", "EL136"},
                {"Elephants", "EL136"},
                {"Grapes", "GR140"},
                {"Grapefruit", "GR140"},
                {"Hippopotamus", "HH144"},
                {"Hippopotamuses", "HH144"},
                {"India", "IN331"},
                {"Indian", "IN331"},
                {"Orange", "OR250"},
                {"Oranges", "OR250"},
                {"Octopus", "OK513"},
                {"Octopuses", "OK513"},
                {"Potato", "P202"},
                {"Potatoes", "P202"},
                {"Quarter", "K020"},
                {"Quarters", "K020"},
                {"Raspberry", "RS201"},
                {"Raspberries", "RS201"},
                {"Strawberry", "S234"},
                {"Strawberries", "S234"},
                {"Turkey", "T162"},
                {"Turkeys", "T162"},
                {"Unicorn", "UN150"},
                {"Unicorns", "UN150"},
                {"Watermelon", "WA230"},
                {"Watermelons", "WA230"},
                {"Xylophone", "X145"},
                {"Xylophones", "X145"},
                {"Zebra", "Z135"},
                {"Zebras", "Z135"},
                {"", ""},
                {"", ""},
                {"123", ""},
                {"123", ""},
                {"       ", ""},
                {"       ", ""},
                {"--foo", ""},
                {"--foo", ""},
                {"--foo--", ""},
                {"--foo--", ""},
                {"--foo--bar--", ""},
                {"--foo--bar--", ""},
                {"ABCDEFGHIJKLMNOPQRSTUVWXYZ", ""},
                {"ABCDEFGHIJKLMNOPQRSTUVWXYZ", ""}
        });
    }

    @Test
    public void testSoundex() throws EncoderException {
        String output = new Soundex().soundex(input);
        assertEquals(expectedOutput, output);
    }
}

@RunWith(Parameterized.class)
public class SoundexWithCustomMappingTest {

    private final String input;
    private final String customMapping;
    private final String expectedOutput;

    public SoundexWithCustomMappingTest(String input, String customMapping, String expectedOutput) {
        this.input = input;
        this.customMapping = customMapping;
        this.expectedOutput = expectedOutput;
    }

    @Parameterized.Parameters(name = "{index}: soundex({input}, {customMapping}) = {expectedOutput}")
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
                {"apple", "AE123", "A133"},
                {"Apple", "AE123", "A133"},
                {"apples", "AE123", "A133"},
                {"Apple's", "AE123", "A133"},
                {"Apricot", "AF123", "A161"},
                {"Apricots", "AF123", "A161"},
                {"Banana", "BE123", "B135"},
                {"Bananas", "BE123", "B135"},
                {"Cherry", "CE123", "CH62"},
                {"Cherries", "CE123", "CH62"},
                {"Elephant", "EE123", "EL136"},
                {"Elephants", "EE123", "EL136"},
                {"Grapes", "GE123", "GR140"},
                {"Grapefruit", "GE123", "GR140"},
                {"Hippopotamus", "HE123", "HH144"},
                {"Hippopotamuses", "HE123", "HH144"},
                {"India", "ID123", "IN331"},
                {"Indian", "ID123", "IN331"},
                {"Orange", "OE123", "OR250"},
                {"Oranges", "OE123", "OR250"},
                {"Octopus", "OT123", "OK513"},
                {"Octopuses", "OT123", "OK513"},
                {"Potato", "PT123", "P202"},
                {"Potatoes", "PT123", "P202"},
                {"Quarter", "QT123", "K020"},
                {"Quarters", "QT123", "K020"},
                {"Raspberry", "RE123", "RS201"},
                {"Raspberries", "RE123", "RS201"},
                {"Strawberry", "SR123", "S234"},
                {"Strawberries", "SR123", "S234"},
                {"Turkey", "TY123", "T162"},
                {"Turkeys", "TY123", "T162"},
                {"Unicorn", "UN123", "UN150"},
                {"Unicorns", "UN123", "UN150"},
                {"Watermelon", "WA123", "WA230"},
                {"Watermelons", "WA123", "WA230"},
                {"Xylophone", "XE123", "X145"},
                {"Xylophones", "XE123", "X145"},
                {"Zebra", "ZE123", "Z135"},
                {"Zebras", "ZE123", "Z135"},
                {"", "", ""},
                {"", "", ""},
                {"123", "", ""},
                {"123", "", ""},
                {"       ", "", ""},
                {"       ", "", ""},
                {"--foo", "", ""},
                {"--foo", "", ""},
                {"--foo--", "", ""},
                {"--foo--", "", ""},
                {"--foo--bar--", "", ""},
                {"--foo--bar--", "", ""},
                {"ABCDEFGHIJKLMNOPQRSTUVWXYZ", "", ""},
                {"ABCDEFGHIJKLMNOPQRSTUVWXYZ", "", ""}
        });
    }

    @Test
    public void testSoundexWithCustomMapping() throws EncoderException {
        Soundex soundex = new Soundex(customMapping.toCharArray());
        String output = soundex.soundex(input);
        assertEquals(expectedOutput, output);
    }
}

@RunWith(Parameterized.class)
public class SoundexDifferenceTest {

    private final String s1;
    private final String s2;
    private final int expectedOutput;

    public SoundexDifferenceTest(String s1, String s2, int expectedOutput) {
        this.s1 = s1;
        this.s2 = s2;
        this.expectedOutput = expectedOutput;
    }

    @Parameterized.Parameters(name = "{index}: soundex.difference({s1}, {s2}) = {expectedOutput}")
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
                {"apple", "Apple", 3},
                {"Apple", "apple", 3},
                {"apples", "Apple's", 3},
                {"Apple's", "apples", 3},
                {"Apricot", "Apricots", 3},
                {"Apricots", "Apricot", 3},
                {"Banana", "Bananas", 3},
                {"Bananas", "Banana", 3},
                {"Cherry", "Cherries", 3},
                {"Cherries", "Cherry", 3},
                {"Elephant", "Elephants", 3},
                {"Elephants", "Elephant", 3},
                {"Grapes", "Grapefruit", 2},
                {"Grapefruit", "Grapes", 2},
                {"Hippopotamus", "Hippopotamuses", 2},
                {"Hippopotamuses", "Hippopotamus", 2},
                {"India", "Indian", 3},
                {"Indian", "India", 3},
                {"Orange", "Oranges", 3},
                {"Oranges", "Orange", 3},
                {"Octopus", "Octopuses", 3},
                {"Octopuses", "Octopus", 3},
                {"Potato", "Potatoes", 3},
                {"Potatoes", "Potato", 3},
                {"Quarter", "Quarters", 3},
                {"Quarters", "Quarter", 3},
                {"Raspberry", "Raspberries", 3},
                {"Raspberries", "Raspberry", 3},
                {"Strawberry", "Strawberries", 3},
                {"Strawberries", "Strawberry", 3},
                {"Turkey", "Turkeys", 3},
                {"Turkeys", "Turkey", 3},
                {"Unicorn", "Unicorns", 3},
                {"Unicorns", "Unicorn", 3},
                {"Watermelon", "Watermelons", 3},
                {"Watermelons", "Watermelon", 3},
                {"Xylophone", "Xylophones", 3},
                {"Xylophones", "Xylophone", 3},
                {"Zebra", "Zebras", 3},
                {"Zebras", "Zebra", 3},
                {"", "", 0},
                {"", "", 0},
                {"123", "", 0},
                {"123", "", 0},
                {"       ", "", 0},
                {"       ", "", 0},
                {"--foo", "--foo--", 4},
                {"--foo--", "--foo", 4},
                {"--foo--bar--", "--foo--bar--", 4},
                {"--foo--bar--", "--foo--bar--", 4},
                {"ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 4},
                {"ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 4}
        });
    }

    @Test
    public void testDifference() throws EncoderException {
        int output = new Soundex().difference(s1, s2);
        assertEquals(expectedOutput, output);
    }
}