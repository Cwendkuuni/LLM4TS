import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class GroupImplTest {

    private Options options;
    private CommandLine commandLine;

    @Before
    public void setUp() {
        // create test options
        options = new Options();
        options.addOption(OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withValueSeparator('=')
                .isRequired()
                .withDescription("A long option")
                .create('L'));
        options.addOption(OptionBuilder.withLongOpt("noArg")
                .hasArg(false)
                .isRequired(false)
                .withDescription("An optional no-arg option")
                .create('N'));
        options.addOption(OptionBuilder.withLongOpt("arg")
                .hasArg(true)
                .isRequired(false)
                .withDescription("An optional argumented option")
                .create('A'));
        options.addGroup(new GroupImpl(Arrays.asList(options.getOption("arg")),
                "group",
                "A group with an argumented option",
                1,
                2));
    }

    @Test
    public void testBasicUsage() throws ParseException {
        commandLine = new CommandLine("-L foo=bar -N -A baz");
        final HelpFormatter formatter = new HelpFormatter();
        options.parse(commandLine);
        formatter.printHelp("Test", options);

        // test basic usage of each option
        assertEquals("foo", commandLine.getOptionValue("longOpt"));
        assertFalse(commandLine.hasOption("noArg"));
        assertEquals("baz", commandLine.getOptionValue("arg"));
        assertTrue(commandLine.hasOption("group.arg"));
        assertEquals(2, commandLine.getArgs().size());

        // test parsing of group
        assertEquals("group", commandLine.getOptionValues("group")[0]);
        assertNull(commandLine.getOptionValues("group")[1]);
        assertEquals("A group with an argumented option", commandLine.getOptionDescription("group"));
        assertEquals(1, commandLine.getOptionValues("group.arg").length);
        assertEquals("baz", commandLine.getOptionValues("group.arg")[0]);
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException {
        commandLine = new CommandLine("-L foo=bar");
        options.parse(commandLine);
        fail("Should throw ParseException for missing option");
    }

    @Test(expected = ParseException.class)
    public void testMissingGroup() throws ParseException {
        commandLine = new CommandLine("-L foo=bar -A");
        options.parse(commandLine);
        fail("Should throw ParseException for missing group");
    }

    @Test(expected = ParseException.class)
    public void testTooFewOptions() throws ParseException {
        commandLine = new CommandLine("-L foo=bar");
        options.parse(commandLine);
        fail("Should throw ParseException for too few options");
    }

    @Test(expected = ParseException.class)
    public void testTooManyOptions() throws ParseException {
        commandLine = new CommandLine("-L foo=bar -L baz=qux");
        options.parse(commandLine);
        fail("Should throw ParseException for too many options");
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        commandLine = new CommandLine("-X");
        options.parse(commandLine);
        fail("Should throw ParseException for invalid option");
    }

    @Test(expected = ParseException.class)
    public void testInvalidGroup() throws ParseException {
        commandLine = new CommandLine("-X");
        options.parse(commandLine);
        fail("Should throw ParseException for invalid group");
    }

    @Test public void testUsageHelp() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);
    }
}