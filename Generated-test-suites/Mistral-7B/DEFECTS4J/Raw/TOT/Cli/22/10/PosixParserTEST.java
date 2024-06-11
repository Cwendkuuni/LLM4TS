package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest
{
    /**
     * Test method for PosixParser.flatten(Options, String[], boolean).
     */
    @Test
    public void testFlatten()
    {
        // Test case 1: Simple options
        Options options1 = new Options();
        options1.addOption("a", true, "Option A");
        options1.addOption("b", 1, "Option B");
        String[] args1 = {"-a", "--b", "value"};
        String[] result1 = new PosixParser().flatten(options1, args1, false);
        Assert.assertArrayEquals(new String[]{"-a", "--b", "value"}, result1);

        // Test case 2: Options with arguments
        Options options2 = new Options();
        options2.addOption("c", true, "Option C");
        options2.addOption("d", 2, "Option D");
        String[] args2 = {"-c", "--d", "arg1", "arg2"};
        String[] result2 = new PosixParser().flatten(options2, args2, false);
        Assert.assertArrayEquals(new String[]{"-c", "--d", "arg1", "arg2"}, result2);

        // Test case 3: Long options
        Options options3 = new Options();
        options3.addOption("e", "no-log", false, "Disable logging");
        options3.addOption("f", "help", true, "Display help");
        String[] args3 = {"--no-log", "--help"};
        String[] result3 = new PosixParser().flatten(options3, args3, false);
        Assert.assertArrayEquals(new String[]{"--no-log", "--help"}, result3);

        // Test case 4: Invalid option
        Options options4 = new Options();
        String[] args4 = {"--invalid"};
        try
        {
            new PosixParser().flatten(options4, args4, false);
            Assert.fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }

        // Test case 5: Stop at non-option
        Options options5 = new Options();
        options5.addOption("g", true, "Option G");
        String[] args5 = {"-g", "value", "--x"};
        String[] result5 = new PosixParser().flatten(options5, args5, true);
        Assert.assertArrayEquals(new String[]{"-g", "--x"}, result5);

        // Test case 6: Bursting
        Options options6 = new Options();
        options6.addOption("h", false, "Option H");
        options6.addOption("i", 1, "Option I");
        String[] args6 = {"-hi", "arg"};
        String[] result6 = new PosixParser().flatten(options6, args6, false);
        Assert.assertArrayEquals(new String[]{"-h", "--i", "arg"}, result6);
    }
}