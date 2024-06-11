package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 *
 * @author John Doe
 */
public class PosixParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer value");
        options.addOption("c", 2, "Option C with a short value");
        options.addOption("d", "Default is true", "Option D with a boolean value");
        options.addOption("e", "ellow, --e is alias for option --f");
        options.addOption("f", "Option F");
    }

    

    /**
     * Test for flatten method with no options and no arguments.
     */
    @Test
    public void testFlattenNoOptionsNoArguments() {
        PosixParser parser = new PosixParser();
        String[] args = new String[0];
        String[] expected = new String[0];
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test for flatten method with single option.
     */
    @Test
    public void testFlattenSingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--a"};
        String[] expected = new String[]{"--a"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test for flatten method with an option with an argument.
     */
    @Test
    public void testFlattenOptionWithArgument() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--a", "argument"};
        String[] expected = new String[]{"--a", "argument"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test for flatten method with multiple options.
     */
    @Test
    public void testFlattenMultipleOptions() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--a", "--b", "--c=123", "--d", "--e", "--f"};
        String[] expected = new String[]{"--a", "--b", "--c=123", "--d", "--e", "--f"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test for flatten method with long option.
     */
    @Test
    public void testFlattenLongOption() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--option-d"};
        String[] expected = new String[]{"--option-d"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test for flatten method with option alias.
     */
    @Test
    public void testFlattenOptionAlias() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--e"};
        String[] expected = new String[]{"--e"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test for flatten method with burst token.
     */
    @Test
    public void testFlattenBurstToken() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--abc"};
        String[] expected = new String[]{"--a", "--b", "--c"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test for flatten method with burst token and argument.
     */
    @Test
    public void testFlattenBurstTokenWithArgument() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--abc=123"};
        String[] expected = new String[]{"--a", "--b=123"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test for flatten method with stopAtNonOption.
     */
    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--a", "argument", "nonOption"};
        String[] expected = new String[]{"--a", "argument"};
        String[] actual = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test for flatten method with multiple options and stopAtNonOption.
     */
    @Test
    public void testFlattenMultipleOptionsWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--a", "--b", "--c=123", "--d", "--e", "--f", "nonOption"};
        String[] expected = new String[]{"--a", "--b", "--c=123"};
        String[] actual = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test for flatten method with invalid option.
     */
    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--invalidOption"};
        parser.flatten(options, args, false);
    }

    
}