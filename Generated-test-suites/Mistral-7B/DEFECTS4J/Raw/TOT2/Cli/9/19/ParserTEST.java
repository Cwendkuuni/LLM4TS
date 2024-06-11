import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    private CommandLineParser parser;
    private Options options;

    @Before
    public void setup() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOptionGroup(new OptionGroup("group", "Options in group")
                .addOption("c", false, "Option C")
                .addOption("d", false, "Option D"));
        parser = new DefaultParser();
    }

    @Test
    public void testParseWithNoArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments, false);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(true, cmd.hasOption("a"));
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException {
        String[] arguments = {"arg"};
        CommandLine cmd = parser.parse(options, arguments, false);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getArgs().get(0));
        assertEquals(true, cmd.hasOption("a"));
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        String[] arguments = {"-a"};
        CommandLine cmd = parser.parse(options, arguments, false);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(true, cmd.hasOption("a"));
    }

    @Test
    public void testParseWithSingleOptionAndArgument() throws ParseException {
        String[] arguments = {"-a", "arg"};
        CommandLine cmd = parser.parse(options, arguments, false);
        assertEquals(0, cmd.getArgs().size());
        assertEquals("arg", cmd.getOptionValues("a")[0]);
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        String[] arguments = {"-a", "-b", "arg"};
        CommandLine cmd = parser.parse(options, arguments, false);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getArgs().get(0));
        assertEquals(true, cmd.hasOption("a"));
        assertEquals(1, cmd.getOptionValues("b").length);
        assertEquals("arg", cmd.getOptionValues("b")[0]);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        String[] arguments = {"-c"};
        parser.parse(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingArgumentForOption() throws ParseException {
        String[] arguments = {"-b"};
        parser.parse(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        String[] arguments = {"-x"};
        parser.parse(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMultipleInvalidOptions() throws ParseException {
        String[] arguments = {"-x", "-y"};
        parser.parse(options, arguments, false);
    }

    @Test
    public void testParseWithGroupOption() throws ParseException {
        String[] arguments = {"--group"};
        CommandLine cmd = parser.parse(options, arguments, false);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(true, cmd.hasOption("group"));
        assertEquals(true, options.getOptionGroup("group").isSelected());
    }

    @Test
    public void testParseWithGroupOptionAndOtherOptions() throws ParseException {
        String[] arguments = {"--group", "-a", "-b", "arg"};
        CommandLine cmd = parser.parse(options, arguments, false);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getArgs().get(0));
        assertEquals(true, cmd.hasOption("a"));
        assertEquals(1, cmd.getOptionValues("b").length);
        assertEquals("arg", cmd.getOptionValues("b")[0]);
        assertEquals(true, options.getOptionGroup("group").isSelected());
    }

    @Test
    public void testParseWithMultipleGroupOptions() throws ParseException {
        String[] arguments = {"--group1", "--group2"};
        CommandLine cmd = parser.parse(options, arguments, false);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(true, options.getOptionGroup("group1").isSelected());
        assertEquals(true, options.getOptionGroup("group2").isSelected());
    }
}