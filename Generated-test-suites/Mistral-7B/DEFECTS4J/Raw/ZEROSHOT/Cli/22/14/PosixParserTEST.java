package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for {@link PosixParser}
 */
public class PosixParserTest
{
    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     */
    @Test
    public void testFlatten()
    {
        // Test case 1: Empty argument list
        Options options1 = new Options();
        String[] arguments1 = new String[0];
        Parser parser1 = new PosixParser();
        String[] result1 = parser1.flatten(options1, arguments1, false);
        Assert.assertArrayEquals(new String[0], result1);

        // Test case 2: Options with no argument
        Options options2 = new Options();
        options2.addOption("o", "option", false, "Description of option o.");
        String[] arguments2 = new String[]{"--option"};
        Parser parser2 = new PosixParser();
        String[] result2 = parser2.flatten(options2, arguments2, false);
        Assert.assertArrayEquals(new String[]{"--option"}, result2);

        // Test case 3: Options with argument
        Options options3 = new Options();
        options3.addOption("a", "argument", true, "Description of argument a.");
        String[] arguments3 = new String[]{"--argument", "test"};
        Parser parser3 = new PosixParser();
        String[] result3 = parser3.flatten(options3, arguments3, false);
        Assert.assertArrayEquals(new String[]{"--argument", "test"}, result3);

        // Test case 4: Multiple options
        Options options4 = new Options();
        options4.addOption("o", "option1", false, "Description of option1.");
        options4.addOption("o", "option2", false, "Description of option2.");
        String[] arguments4 = new String[]{"--option1", "--option2"};
        Parser parser4 = new PosixParser();
        String[] result4 = parser4.flatten(options4, arguments4, false);
        Assert.assertArrayEquals(new String[]{"--option1", "--option2"}, result4);

        // Test case 5: Long option with argument
        Options options5 = new Options();
        options5.addOption("l", "longOption", true, "Description of long option.");
        String[] arguments5 = new String[]{"--longOption", "value"};
        Parser parser5 = new PosixParser();
        String[] result5 = parser5.flatten(options5, arguments5, false);
        Assert.assertArrayEquals(new String[]{"--longOption", "value"}, result5);

        // Test case 6: Short option with argument
        Options options6 = new Options();
        options6.addOption("s", "shortOption", true, "Description of short option.");
        String[] arguments6 = new String[]{"-s", "value"};
        Parser parser6 = new PosixParser();
        String[] result6 = parser6.flatten(options6, arguments6, false);
        Assert.assertArrayEquals(new String[]{"-s", "value"}, result6);

        // Test case 7: Mixed short and long options
        Options options7 = new Options();
        options7.addOption("s", "shortOption1", false, "Description of short option1.");
        options7.addOption("l", "longOption1", false, "Description of long option1.");
        options7.addOption("s", "shortOption2", true, "Description of short option2.");
        String[] arguments7 = new String[]{"--longOption1", "-s", "value"};
        Parser parser7 = new PosixParser();
        String[] result7 = parser7.flatten(options7, arguments7, false);
        Assert.assertArrayEquals(new String[]{"--longOption1", "-s", "value"}, result7);

        // Test case 8: Multiple options with arguments
        Options options8 = new Options();
        options8.addOption("a", "arg1", true, "Description of arg1.");
        options8.addOption("b", "arg2", true, "Description of arg2.");
        String[] arguments8 = new String[]{"--arg1", "test1", "--arg2", "test2"};
        Parser parser8 = new PosixParser();
        String[] result8 = parser8.flatten(options8, arguments8, false);
        Assert.assertArrayEquals(new String[]{"--arg1", "test1", "--arg2", "test2"}, result8);

        // Test case 9: Stop at non-option
        Options options9 = new Options();
        options9.addOption("s", "shortOption", false, "Description of short option.");
        String[] arguments9 = new String[]{"--value", "-s", "test"};
        Parser parser9 = new PosixParser();
        String[] result9 = parser9.flatten(options9, arguments9, true);
        Assert.assertArrayEquals(new String[]{"--value"}, result9);

        // Test case 10: Bursting
        Options options10 = new Options();
        options10.addOption("x", "optionX");
        String[] arguments10 = new String[]{"-x", "value1", "-x", "value2"};
        Parser parser10 = new PosixParser();
        String[] result10 = parser10.flatten(options10, arguments10, false);
        Assert.assertArrayEquals(new String[]{"-x", "value1", "-x", "value2"}, result10);

        // Test case 11: Bursting with stop at non-option
        Options options11 = new Options();
        options11.addOption("x", "optionX");
        String[] arguments11 = new String[]{"-x", "value1", "-x", "value2", "--value"};
        Parser parser11 = new PosixParser();
        String[] result11 = parser11.flatten(options11, arguments11, true);
        Assert.assertArrayEquals(new String[]{"--value"}, result11);
    }
}