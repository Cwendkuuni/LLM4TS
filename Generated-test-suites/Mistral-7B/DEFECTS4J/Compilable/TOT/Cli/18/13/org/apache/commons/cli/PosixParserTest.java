package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    /**
     * Test case for flatten method with no options
     */
    @Test
    public void testFlattenNoOptions() {
        String[] args = new String[]{"test", "arg1", "arg2"};

        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"test", "arg1", "arg2"}, flattenedArgs);
    }

    /**
     * Test case for flatten method with single option
     */
    @Test
    public void testFlattenSingleOption() {
        String[] args = new String[]{"--option", "value"};

        Options options = new Options();
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--option", "value"}, flattenedArgs);
    }

    /**
     * Test case for flatten method with multiple options
     */
    @Test
    public void testFlattenMultipleOptions() {
        String[] args = new String[]{"--option1", "--option2", "value"};

        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--option1", "--option2", "value"}, flattenedArgs);
    }

    /**
     * Test case for flatten method with mixed options and arguments
     */
    @Test
    public void testFlattenMixedOptionsAndArguments() {
        String[] args = new String[]{"--option1", "value1", "--option2=value2", "arg"};

        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--option1", "value1", "--option2", "value2", "arg"}, flattenedArgs);
    }

    /**
     * Test case for flatten method with long option
     */
    @Test
    public void testFlattenLongOption() {
        String[] args = new String[]{"--long-option", "value"};

        Options options = new Options();
        options.addOption("longOption", true, "l");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option", "value"}, flattenedArgs);
    }

    /**
     * Test case for flatten method with long option with argument
     */
    @Test
    public void testFlattenLongOptionWithArgument() {
        String[] args = new String[]{"--long-option=value"};

        Options options = new Options();
        options.addOption("longOption", true, "l");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option", "value"}, flattenedArgs);
    }

    /**
     * Test case for flatten method with invalid option
     */
    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = new String[]{"--invalidOption"};

        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    /**
     * Test case for flatten method with stopAtNonOption set to true
     */
    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = new String[]{"--option", "value", "arg1", "arg2"};

        Options options = new Options();
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--option", "value"}, flattenedArgs);
    }

    /**
     * Test case for flatten method with multiple non-options
     */
    @Test
    public void testFlattenMultipleNonOptions() {
        String[] args = new String[]{"arg1", "arg2"};

        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, flattenedArgs);
    }
}