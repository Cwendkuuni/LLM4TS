package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption(OptionBuilder.withLongOpt("file").withDescription("file option").hasArg().create("f"));

        PosixParser parser = new PosixParser();

        // Test case 1: Single hyphen -
        String[] arguments1 = new String[] { "-" };
        String[] expectedOutput1 = new String[] { "-" };
        assertArrayEquals(expectedOutput1, parser.flatten(options, arguments1, false));

        // Test case 2: Double hyphen --
        String[] arguments2 = new String[] { "--" };
        String[] expectedOutput2 = new String[] { "--" };
        assertArrayEquals(expectedOutput2, parser.flatten(options, arguments2, false));

        // Test case 3: Single character option
        String[] arguments3 = new String[] { "-a" };
        String[] expectedOutput3 = new String[] { "-a" };
        assertArrayEquals(expectedOutput3, parser.flatten(options, arguments3, false));

        // Test case 4: Single character option with argument
        String[] arguments4 = new String[] { "-f", "test.txt" };
        String[] expectedOutput4 = new String[] { "-f", "test.txt" };
        assertArrayEquals(expectedOutput4, parser.flatten(options, arguments4, false));

        // Test case 5: Multiple character option
        String[] arguments5 = new String[] { "--file" };
        String[] expectedOutput5 = new String[] { "--file" };
        assertArrayEquals(expectedOutput5, parser.flatten(options, arguments5, false));

        // Test case 6: Multiple character option with argument
        String[] arguments6 = new String[] { "--file=test.txt" };
        String[] expectedOutput6 = new String[] { "--file", "test.txt" };
        assertArrayEquals(expectedOutput6, parser.flatten(options, arguments6, false));

        // Test case 7: Stop at non-option
        String[] arguments7 = new String[] { "-a", "abc", "-f", "-b", "123" };
        String[] expectedOutput7 = new String[] { "-a", "abc", "--", "-f", "-b", "123" };
        assertArrayEquals(expectedOutput7, parser.flatten(options, arguments7, true));
    }
}