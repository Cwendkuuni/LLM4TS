import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    @Test
    public void testEmpty() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] args = new String[0];

        CommandLine commandLine = parser.parse(options, args);

        assertEquals(0, commandLine.getArgs().size());
        assertFalse(commandLine.hasOption("option"));
        assertNull(commandLine.getOptionValue("option"));
    }

    @Test
    public void testSimpleOptions() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] args = new String[]{"-a", "1", "--option", "value"};

        options.addOption("a", true, "A value");
        options.addOption("option", true, "An option value");

        CommandLine commandLine = parser.parse(options, args);

        assertEquals(1, commandLine.getArgs().size());
        assertTrue(commandLine.hasOption("a"));
        assertEquals("1", commandLine.getOptionValue("a"));
        assertTrue(commandLine.hasOption("option"));
        assertEquals("value", commandLine.getOptionValue("option"));
    }

    @Test
    public void testSimpleArguments() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] args = new String[]{"arg1", "arg2"};

        CommandLine commandLine = parser.parse(options, args);

        assertEquals(2, commandLine.getArgs().size());
        assertFalse(commandLine.hasOption("option"));
        assertNull(commandLine.getOptionValue("option"));
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] args = new String[]{"--missing"};

        options.addOption("option", true, "An option value");

        parser.parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testMissingValue() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] args = new String[]{"-a"};

        options.addOption("a", true, "A value");

        parser.parse(options, args);
    }

    @Test
    public void testMultipleValues() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] args = new String[]{"--option", "value1", "--option", "value2"};

        options.addOption("option", true, "An option value");

        CommandLine commandLine = parser.parse(options, args);

        assertEquals(1, commandLine.getArgs().size());
        assertTrue(commandLine.hasOption("option"));
        assertSame(options.getOption("option"), commandLine.getOption("option"));
        assertEquals("[value1, value2]", commandLine.getOptionValues("option").toString());
    }

    @Test
    public void testRequiredOptions() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] args = new String[]{"-a"};

        options.addOption("a", true, "A required option");
        options.addRequiredOption("option", true, "A required option");

        try
        {
            parser.parse(options, args);
            fail();
        }
        catch (MissingOptionException e)
        {
            List<String> missingOptions = e.getMissingOptions();
            assertEquals(2, missingOptions.size());
            assertTrue(missingOptions.contains("option"));
            assertTrue(missingOptions.contains("a"));
        }
    }

    @Test
    public void testRequiredGroups() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] args = new String[]{"--group"};

        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(true);
        group.addOption(new Option("a", true, "A required option"));
        options.addOptionGroup(group);

        try
        {
            parser.parse(options, args);
            fail();
        }
        catch (MissingOptionException e)
        {
            List<String> missingOptions = e.getMissingOptions();
            assertEquals(1, missingOptions.size());
            assertEquals("group", missingOptions.get(0));
        }
    }

    @Test
    public void testComplexOptions() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] args = new String[]{"--option1=value1", "--option2=value2", "arg1", "--option3=value3", "arg2"};

        options.addOption("option1", true, "Option 1 with value");
        options.addOption("option2", true, "Option 2 with value");
        options.addOption("option3", true, "Option 3 with value");

        CommandLine commandLine = parser.parse(options, args);

        assertEquals(3, commandLine.getArgs().size());
        assertTrue(commandLine.hasOption("option1"));
        assertEquals("value1", commandLine.getOptionValue("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertEquals("value2", commandLine.getOptionValue("option2"));
        assertTrue(commandLine.hasOption("option3"));
        assertEquals("value3", commandLine.getOptionValue("option3"));
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(2));
    }
}