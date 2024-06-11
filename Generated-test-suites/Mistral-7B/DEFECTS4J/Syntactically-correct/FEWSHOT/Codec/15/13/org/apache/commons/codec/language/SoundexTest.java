package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testSoundexUSEnglish() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input[] = {"apple", "appel", "application", "approach", "approve", "April", "April's", "April's Approach",
                "April's Approval", "April's Approval's", "April's Approval's Application", "April's Approval's Application's",
                "April's Approval's Application's Approval"};
        String expectedOutput[] = {"A123", "A123", "A123000", "A260", "A260", "A161", "A161000", "A161000A123",
                "A161000A123A000", "A161000A123A000A123", "A161000A123A000A123A000", "A161000A123A000A123A000"};
        for (int i = 0; i < input.length; i++) {
            String output = soundex.encode(input[i]);
            assertEquals("Expected the soundex encoding of '" + input[i] + "' to be '" + expectedOutput[i] + "'",
                    expectedOutput[i], output);
        }
    }

    @Test
    public void testSoundexCustomMapping() {
        char customMapping[] = {'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4'};
        Soundex soundex = new Soundex(customMapping);
        String input[] = {"apple", "appel", "application", "approach", "approve", "April", "April's", "April's Approach",
                "April's Approval", "April's Approval's", "April's Approval's Application", "April's Approval's Application's",
                "April's Approval's Application's Approval"};
        String expectedOutput[] = {"B123", "B123", "B123000", "C260", "C260", "D161", "D161000", "D161000B123",
                "D161000B123B000", "D161000B123B000B123", "D161000B123B000B123B000", "D161000B123B000B123B000"};
        for (int i = 0; i < input.length; i++) {
            String output = soundex.encode(input[i]);
            assertEquals("Expected the soundex encoding of '" + input[i] + "' to be '" + expectedOutput[i] + "'",
                    expectedOutput[i], output);
        }
    }

    @Test(expected = EncoderException.class)
    public void testSoundexNullInput() {
        Soundex soundex = Soundex.US_ENGLISH;
        soundex.encode(null);
    }

    @Test
    public void testSoundexEmptyInput() {
        Soundex soundex = Soundex.US_ENGLISH;
        assertEquals("Expected the soundex encoding of an empty string to be an empty string", "", soundex.encode(""));
    }

    @Test
    public void testSoundexDifference() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input1 = "Apple";
        String input2 = "Apricot";
        int difference = soundex.difference(input1, input2);
        assertEquals("Expected the difference between 'Apple' and 'Apricot' to be 0", 0, difference);

        input1 = "Application";
        input2 = "Approval";
        difference = soundex.difference(input1, input2);
        assertEquals("Expected the difference between 'Application' and 'Approval' to be 1", 1, difference);

        input1 = "April's Approval's Application's Approval";
        input2 = "April's Approval's Application's Application";
        difference = soundex.difference(input1, input2);
        assertEquals("Expected the difference between 'April's Approval's Application's Approval' and 'April's Approval's Application's Application' to be 1", 1, difference);
    }

}