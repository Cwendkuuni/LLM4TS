package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        Option option1 = new Option("f", false, "Option 1");
        Option option2 = new Option("o", true, "Option 2");
        Option option3 = new Option("a", true, "Option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        String[] arguments = new String[] {"-f", "-o", "output.txt", "-a", "arg1", "arg2"};
        String[] expected = new String[] {"-f", "-o", "output.txt", "-a", "arg1", "arg2"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }
}