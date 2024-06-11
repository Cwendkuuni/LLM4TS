import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for org.apache.commons.cli.CommandLine.
 */
public class CommandLineTest {

    private CommandLine commandLine;
    private Options options;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        options = new Options();
        commandLine = new CommandLine();
    }

    /**
     * Tears down the test environment.
     */
    @After
    public void tearDown() {
        commandLine = null;
        options = null;
    }

    /**
     * Test that the hasOption method returns false for an unknown option.
     */
    @Test
    public void testHasOptionUnknownOption() {
        assertFalse(commandLine.hasOption("unknownOption"));
    }

    /**
     * Test that the hasOption method returns true for a known option.
     */
    @Test
    public void testHasOptionKnownOption() {
        Option option = new Option("option", true, false, "optionValue");
        options.addOption(option);
        commandLine.parse(options.parse(new String[]{"--option", "optionValue"}));
        assertTrue(commandLine.hasOption("option"));
    }

    /**
     * Test that the getOptionValue method returns the value of an option with a single value.
     */
    @Test
    public void testGetOptionValueSingleValue() throws ParseException {
        Option option = new Option("option", true, false, "optionValue");
        options.addOption(option);
        commandLine.parse(options.parse(new String[]{"--option", "optionValue"}));
        assertSame("optionValue", commandLine.getOptionValue("option"));
    }

    /**
     * Test that the getOptionValue method returns null for an option without a value.
     */
    @Test
    public void testGetOptionValueNoValue() throws ParseException {
        Option option = new Option("option", false, false, null);
        options.addOption(option);
        commandLine.parse(options.parse(new String[]{"--option"}));
        assertNull(commandLine.getOptionValue("option"));
    }

    /**
     * Test that the getOptionValue method returns the value of an option with multiple values.
     */
    @Test
    public void testGetOptionValueMultipleValues() throws ParseException {
        Option option = new Option("option", true, true, null);
        options.addOption(option);
        commandLine.parse(options.parse(new String[]{"--option", "value1", "--option", "value2"}));
        String[] values = commandLine.getOptionValues("option");
        assertSame(values.length, 2);
        assertSame("value1", values[0]);
        assertSame("value2", values[1]);
    }

    /**
     * Test that the getOptionValues method returns the values of an option with multiple values.
     */
    @Test
    public void testGetOptionValuesMultipleValues() throws ParseException {
        Option option = new Option("option", true, true, null);
        options.addOption(option);
        commandLine.parse(options.parse(new String[]{"--option", "value1", "--option", "value2"}));
        String[] values = commandLine.getOptionValues("option");
        assertSame(values.length, 2);
        assertSame("value1", values[0]);
        assertSame("value2", values[1]);
    }

    /**
     * Test that the getOptionValues method returns null for an option without values.
     */
    @Test
    public void testGetOptionValuesNoValues() throws ParseException {
        Option option = new Option("option", false, false, null);
        options.addOption(option);
        commandLine.parse(options.parse(new String[]{"--option"}));
        assertNull(commandLine.getOptionValues("option"));
    }

    /**
     * Test that the getArgList method returns the unparsed arguments.
     */
    @Test
    public void testGetArgList() throws ParseException {
        options.addOption(new Option("option", false, false, null));
        commandLine.parse(options.parse(new String[]{"--option", "arg1", "arg2"}));
        List argList = commandLine.getArgList();
        assertSame(argList.size(), 2);
        assertSame("arg1", argList.get(0));
        assertSame("arg2", argList.get(1));
    }

    /**
     * Test that the getArgs method returns the unparsed arguments as an array.
     */
    @Test
    public void testGetArgs() throws ParseException {
        options.addOption(new Option("option", false, false, null));
        commandLine.parse(options.parse(new String[]{"--option", "arg1", "arg2"}));
        String[] args = commandLine.getArgs();
        assertSame(args.length, 2);
        assertSame("arg1", args[0]);
        assertSame("arg2", args[1]);
    }

}