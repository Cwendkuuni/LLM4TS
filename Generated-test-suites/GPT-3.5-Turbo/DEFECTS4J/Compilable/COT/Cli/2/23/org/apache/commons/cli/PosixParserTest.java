package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();

        // Test case 1: No options
        Options options1 = new Options();
        String[] arguments1 = new String[0];
        String[] expected1 = new String[0];
        assertArrayEquals(expected1, parser.flatten(options1, arguments1, false));

        // Test case 2: Single option without arguments
        Options options2 = new Options();
        Option option2 = new Option("a", false, "Test option");
        options2.addOption(option2);
        String[] arguments2 = new String[] {"-a"};
        String[] expected2 = new String[] {"-a"};
        assertArrayEquals(expected2, parser.flatten(options2, arguments2, false));

        // Test case 3: Single option with arguments
        Options options3 = new Options();
        Option option3 = new Option("b", true, "Test option");
        options3.addOption(option3);
        String[] arguments3 = new String[] {"-b", "arg1", "arg2"};
        String[] expected3 = new String[] {"-b", "arg1", "arg2"};
        assertArrayEquals(expected3, parser.flatten(options3, arguments3, false));

        // Test case 4: Stop at non-option
        Options options4 = new Options();
        Option option4 = new Option("c", true, "Test option");
        options4.addOption(option4);
        String[] arguments4 = new String[] {"-c", "arg1", "arg2", "non-option"};
        String[] expected4 = new String[] {"-c", "arg1", "arg2", "non-option"};
        assertArrayEquals(expected4, parser.flatten(options4, arguments4, true));
    }
}
