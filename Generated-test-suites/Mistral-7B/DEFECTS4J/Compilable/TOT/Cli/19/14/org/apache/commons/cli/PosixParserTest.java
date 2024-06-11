package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for the PosixParser class.
 */
@RunWith(JUnit4.class)
public class PosixParserTest {

    /**
     * Test flattening with valid options.
     */
    @Test
    public void testFlatteningValidOptions() throws ParseException {
        String[] args = {"--option1", "--option2=value", "--option3=value1 value2", "--option4=value1 value2 value3"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", true, "Option 3");
        options.addOption("option4", true, "Option 4");

        String[] expected = {"option1", "option2", "option3", "value", "option4", "value1", "value2", "value3"};
        String[] result = new PosixParser().flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flattening with valid options and stopAtNonOption set to true.
     */
    @Test
    public void testFlatteningValidOptionsStopAtNonOption() throws ParseException {
        String[] args = {"--option1", "--option2=value", "--option3=value1 value2", "--option4=value1 value2 value3", "nonOption"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", true, "Option 3");
        options.addOption("option4", true, "Option 4");

        String[] expected = {"option1", "option2", "value", "nonOption"};
        String[] result = new PosixParser().flatten(options, args, true);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flattening with invalid options.
     */
    @Test(expected = ParseException.class)
    public void testFlatteningInvalidOptions() throws ParseException {
        String[] args = {"--invalidOption", "--option1=value", "--option2=value1 value2", "--option4=value1 value2 value3"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", true, "Option 3");
        options.addOption("option4", true, "Option 4");

        new PosixParser().flatten(options, args, false);
    }

    /**
     * Test flattening with mixed options and arguments.
     */
    @Test
    public void testFlatteningMixedOptionsAndArguments() throws ParseException {
        String[] args = {"--option1", "--option2=value", "--option3=value1 value2", "--option4=value1 value2 value3", "argument1", "argument2"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", true, "Option 3");
        options.addOption("option4", true, "Option 4");

        String[] expected = {"option1", "option2", "value", "option3", "value1", "value2", "value3", "argument1", "argument2"};
        String[] result = new PosixParser().flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flattening with mixed options and arguments and stopAtNonOption set to true.
     */
    @Test
    public void testFlatteningMixedOptionsAndArgumentsStopAtNonOption() throws ParseException {
        String[] args = {"--option1", "--option2=value", "--option3=value1 value2", "--option4=value1 value2 value3", "argument1", "argument2", "nonOption"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", true, "Option 3");
        options.addOption("option4", true, "Option 4");

        String[] expected = {"option1", "option2", "value", "nonOption"};
        String[] result = new PosixParser().flatten(options, args, true);

        Assert.assertArrayEquals(expected, result);
    }
}