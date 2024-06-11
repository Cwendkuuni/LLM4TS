import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void testParseWithNoArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[0];
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals(0, commandLine.getArgs().size());
    }

    @Test
    public void testParseWithSimpleArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--input", "file.txt"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals(1, commandLine.getArgs().size());
        assertEquals("file.txt", commandLine.getArgs().get(0));
    }

    @Test
    public void testParseWithOptionalArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--input", "file.txt", "--output"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals(2, commandLine.getArgs().size());
        assertNull(commandLine.getOptionValue("input"));
        assertEquals(null, commandLine.getOptionValue("output"));
    }

    @Test
    public void testParseWithRequiredArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");
        String[] arguments = {"--output", "output.txt"};
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithMultipleRequiredArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");
        options.addRequiredOption("o", "output", true, "Output file");
        String[] arguments = {"--output", "output.txt"};
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithArgumentsAndOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("i", "input", true, "Input file");
        options.addOption("o", "output", true, "Output file");
        String[] arguments = {"--input", "input.txt", "--output", "output.txt"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals(0, commandLine.getArgs().size());
        assertEquals("input.txt", commandLine.getOptionValue("input"));
        assertEquals("output.txt", commandLine.getOptionValue("output"));
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--invalid"};
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithLongOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("long-input", "long-input", true, "Long input file");
        String[] arguments = {"--long-input", "long-input.txt"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals(0, commandLine.getArgs().size());
        assertEquals("long-input.txt", commandLine.getOptionValue("long-input"));
    }

    @Test
    public void testParseWithShortOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("i", "input", true, "Input file");
        String[] arguments = {"-i", "input.txt"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals(0, commandLine.getArgs().size());
        assertEquals("input.txt", commandLine.getOptionValue("input"));
    }

    @Test
    public void testParseWithMixedOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("i", "input", true, "Input file");
        options.addOption("o", "output", true, "Output file");
        String[] arguments = {"--input", "input.txt", "-o", "output.txt"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals(0, commandLine.getArgs().size());
        assertEquals("input.txt", commandLine.getOptionValue("input"));
        assertEquals("output.txt", commandLine.getOptionValue("output"));
    }

    @Test
    public void testParseWithBooleanOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("v", "verbose", false, "Print verbose output");
        String[] arguments = {"--verbose"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("verbose"));
        assertFalse(commandLine.getBooleanOptionValue("verbose"));
        arguments = new String[]{"--verbose", "--verbose"};
        commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("verbose"));
        assertTrue(commandLine.getBooleanOptionValue("verbose"));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "Group");
        group.setRequired(false);
        options.addOptionGroup(group);
        group.addOption("g1", true, "Option 1 in group");
        group.addOption("g2", true, "Option 2 in group");
        String[] arguments = {"--g"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("g"));
        assertFalse(commandLine.hasOption("g1"));
        assertFalse(commandLine.hasOption("g2"));
        group.setSelected(null);
        assertNull(commandLine.getOptionValue("g"));
        group.setSelected(new Option[]{group.getOption("g1")});
        commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("g1"));
        assertFalse(commandLine.hasOption("g2"));
        assertNull(commandLine.getOptionValue("g"));
    }

    @Test
    public void testParseWithNoOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--input", "input.txt"};
        assertThrows(ParseException.class, () -> parser.parse(options, arguments));
    }
}