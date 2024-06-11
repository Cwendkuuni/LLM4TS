package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GnuParserTest {
    @Test
    public void testFlatten() {
        // Test case 1: Arguments contain no options
        Options options1 = new Options();
        String[] arguments1 = {"arg1", "arg2", "arg3"};
        String[] expected1 = {"arg1", "arg2", "arg3"};
        String[] actual1 = GnuParser.flatten(options1, arguments1, false);
        assertArrayEquals(expected1, actual1);

        // Test case 2: Arguments contain an option that does not exist in the Options object
        Options options2 = new Options();
        options2.addOption("a", "optionA", false, "Option A");
        String[] arguments2 = {"arg1", "-b", "arg2"};
        String[] expected2 = {"arg1", "-b", "arg2"};
        String[] actual2 = GnuParser.flatten(options2, arguments2, false);
        assertArrayEquals(expected2, actual2);

        // Test case 3: Arguments contain an option that exists in the Options object
        Options options3 = new Options();
        options3.addOption("a", "optionA", false, "Option A");
        String[] arguments3 = {"arg1", "-a", "arg2"};
        String[] expected3 = {"arg1", "-a", "arg2"};
        String[] actual3 = GnuParser.flatten(options3, arguments3, false);
        assertArrayEquals(expected3, actual3);

        // Test case 4: Arguments contain a long option with value
        Options options4 = new Options();
        options4.addOption("f", "file", true, "File option");
        String[] arguments4 = {"arg1", "--file=file.txt", "arg2"};
        String[] expected4 = {"arg1", "--file=file.txt", "arg2"};
        String[] actual4 = GnuParser.flatten(options4, arguments4, false);
        assertArrayEquals(expected4, actual4);

        // Test case 5: Arguments contain a long option without value
        Options options5 = new Options();
        options5.addOption("f", "file", false, "File option");
        String[] arguments5 = {"arg1", "--file", "arg2"};
        String[] expected5 = {"arg1", "--file", "arg2"};
        String[] actual5 = GnuParser.flatten(options5, arguments5, false);
        assertArrayEquals(expected5, actual5);

        // Test case 6: Arguments contain a mix of options and non-options
        Options options6 = new Options();
        options6.addOption("a", "optionA", false, "Option A");
        options6.addOption("b", "optionB", false, "Option B");
        String[] arguments6 = {"arg1", "-a", "-b", "arg2"};
        String[] expected6 = {"arg1", "-a", "-b", "arg2"};
        String[] actual6 = GnuParser.flatten(options6, arguments6, false);
        assertArrayEquals(expected6, actual6);

        // Test case 7: Arguments contain the stopAtNonOption flag set to true
        Options options7 = new Options();
        options7.addOption("a", "optionA", false, "Option A");
        options7.addOption("b", "optionB", false, "Option B");
        String[] arguments7 = {"arg1", "-a", "-b", "arg2"};
        String[] expected7 = {"arg1", "-a", "-b"};
        String[] actual7 = GnuParser.flatten(options7, arguments7, true);
        assertArrayEquals(expected7, actual7);

        // Test case 8: Arguments contain the "--" indication to stop processing options
        Options options8 = new Options();
        options8.addOption("a", "optionA", false, "Option A");
        options8.addOption("b", "optionB", false, "Option B");
        String[] arguments8 = {"arg1", "-a", "--", "-b", "arg2"};
        String[] expected8 = {"arg1", "-a", "--", "-b", "arg2"};
        String[] actual8 = GnuParser.flatten(options8, arguments8, false);
        assertArrayEquals(expected8, actual8);
    }

    private void assertArrayEquals(String[] expected, String[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}