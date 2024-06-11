import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Parser.
 */
public class ParserTest
{
    @Test
    public void testParseEmpty() throws ParseException
    {
        Options options = new Options();
        Parser parser = new DefaultParser();

        String[] args = {};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertEquals(0, cmd.getArgs().size());
        assertNull(parser.getOptions());
    }

    @Test
    public void testParseSimple() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value");

        Parser parser = new DefaultParser();
        String[] args = {"--a"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a"));
    }

    @Test
    public void testParseTwoOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value");
        options.addOption("b", true, "Another option with a boolean value");

        Parser parser = new DefaultParser();
        String[] args = {"--a", "--b"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.getOptionValue("a"));
        assertTrue(cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithArgs() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value");

        Parser parser = new DefaultParser();
        String[] args = {"--a", "one", "two", "three"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertEquals(3, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a"));
        assertEquals("one", cmd.getArgs().get(0));
        assertEquals("two", cmd.getArgs().get(1));
        assertEquals("three", cmd.getArgs().get(2));
    }

    @Test
    public void testParseWithOptionalArg() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value");
        options.addOption("b", 1, "An option with an integer argument");

        Parser parser = new DefaultParser();
        String[] args = {"--a", "--b", "3"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertEquals(1, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("3", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value", true);

        Parser parser = new DefaultParser();
        String[] args = {"--b"};
        try
        {
            parser.parse(options, args, null, false);
            assertFalse("Expected MissingOptionException", true);
        }
        catch (MissingOptionException ex)
        {
            List<String> missing = ex.getMissingOptions();
            assertEquals(1, missing.size());
            assertEquals("option 'a' is required", ex.getMessage());
            assertEquals("a", missing.get(0));
        }
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value");

        Parser parser = new DefaultParser();
        String[] args = {"--x"};
        try
        {
            parser.parse(options, args, null, false);
            assertFalse("Expected UnrecognizedOptionException", true);
        }
        catch (UnrecognizedOptionException ex)
        {
            assertEquals("Unrecognized option: x", ex.getMessage());
        }
    }

    @Test
    public void testParseWithDashedOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value");

        Parser parser = new DefaultParser();
        String[] args = {"--no-a"};
        cmd = parser.parse(options, args, null, false);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("long-option-a", true, "An option with a boolean value");

        Parser parser = new DefaultParser();
        String[] args = {"--long-option-a"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("long-option-a"));
        assertTrue(cmd.getOptionValue("long-option-a"));
    }

    @Test
    public void testParseWithLongAndShortOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value");
        options.addOption("long-option-a", true, "Another option with a boolean value");

        Parser parser = new DefaultParser();
        String[] args = {"--a", "--long-option-a"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("long-option-a"));
        assertTrue(cmd.getOptionValue("a"));
        assertTrue(cmd.getOptionValue("long-option-a"));
    }

    @Test
    public void testParseWithLongOptionsAndArguments() throws ParseException
    {
        Options options = new Options();
        options.addOption("option", true, "An option with an argument");

        Parser parser = new DefaultParser();
        String[] args = {"--option", "value"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option"));
        assertEquals("value", cmd.getOptionValue("option"));
    }

    @Test
    public void testParseWithLongOptionsAndMultipleArguments() throws ParseException
    {
        Options options = new Options();
        options.addOption("option", true, "An option with multiple arguments");

        Parser parser = new DefaultParser();
        String[] args = {"--option", "arg1", "arg2", "arg3"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertEquals(args.length - 1, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option"));
        assertEquals("arg1 arg2 arg3", cmd.getOptionValue("option"));
    }

    @Test
    public void testParseWithOptionGroups() throws ParseException
    {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.addOption(new Option("a", true, "Option A"));
        group.addOption(new Option("b", true, "Option B"));
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        String[] args = {"--group1", "--a"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("group1"));
        assertTrue(cmd.getOptionValue("a"));
        assertFalse(cmd.getOptionValue("b"));
        assertEquals("group1", cmd.getOptionValue("group1"));
    }
}