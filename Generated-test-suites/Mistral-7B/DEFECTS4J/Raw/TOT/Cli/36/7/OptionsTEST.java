import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionsTest
{
    @Test
    public void testSimpleOption()
    {
        Options options = new Options();
        options.addOption("a", "example", false, "An example option.");

        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"-a"});
            assertTrue(cmd.hasOption("a"));
            assertFalse(cmd.hasOption("b"));
            assertEquals("An example option.", cmd.getOptionValue("a"));
            assertNull(cmd.getOptionValue("b"));
        }
        catch (ParseException e)
        {
            // should not get here
            throw new AssertionError("Unexpected exception.", e);
        }
    }

    @Test
    public void testOptionWithValue()
    {
        Options options = new Options();
        options.addOption("a", "example", true, "An example option with a value.");

        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"-a", "value"});
            assertTrue(cmd.hasOption("a"));
            assertEquals("value", cmd.getOptionValue("a"));
            assertNull(cmd.getOptionValue("b"));
            assertFalse(cmd.hasOption("b"));
        }
        catch (ParseException e)
        {
            // should not get here
            throw new AssertionError("Unexpected exception.", e);
        }

        try
        {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"-a"});
            assertFalse(cmd.hasOption("a"));
            assertNull(cmd.getOptionValue("a"));
            assertNull(cmd.getOptionValue("b"));
            assertFalse(cmd.hasOption("b"));
        }
        catch (ParseException e)
        {
            // expected
            assertEquals("The option 'a' requires an argument.", e.getMessage());
        }
    }

    @Test
    public void testOptionGroupName()
    {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        options.addOptionGroup(group);

        group.addOption("a", true, "An example option in group.");

        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"--group", "-a"});
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("group"));
            assertTrue(cmd.hasOptions());
            assertEquals(1, cmd.getOptions().size());
            assertSame(group.getOptions().get(0), cmd.getOption("a"));
        }
        catch (ParseException e)
        {
            // should not get here
            throw new AssertionError("Unexpected exception.", e);
        }

        try
        {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"--group", "--help"});
            // expected
            assertEquals("group: An example option in group.", cmd.getOptionValue("group"));
        }
        catch (ParseException e)
        {
            // expected
            assertTrue(e.getMessage().contains("--help"));
        }
    }

    @Test
    public void testMissingOption()
    {
        Options options = new Options();
        options.addOption("a", true, "An example option.");

        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"-b"});
            assertFalse(cmd.hasOption("a"));
            assertNull(cmd.getOptionValue("a"));
            assertFalse(cmd.hasOption("b"));
            assertNull(cmd.getOptionValue("b"));
        }
        catch (ParseException e)
        {
            // expected
            assertEquals("Unrecognized option: b", e.getMessage());
        }
    }

    @Test
    public void testLongOptionWithValue()
    {
        Options options = new Options();
        options.addOption("exampleOption", true, "An example option with a long name and value.");

        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"--exampleOption", "value"});
            assertTrue(cmd.hasOption("exampleOption"));
            assertEquals("value", cmd.getOptionValue("exampleOption"));
            assertNull(cmd.getOptionValue("b"));
            assertFalse(cmd.hasOption("b"));
        }
        catch (ParseException e)
        {
            // should not get here
            throw new AssertionError("Unexpected exception.", e);
        }

        try
        {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"--exampleOption"});
            assertFalse(cmd.hasOption("exampleOption"));
            assertNull(cmd.getOptionValue("exampleOption"));
            assertNull(cmd.getOptionValue("b"));
            assertFalse(cmd.hasOption("b"));
            assertFalse(cmd.hasOptions());
        }
        catch (ParseException e)
        {
            // expected
            assertEquals("The option 'exampleOption' requires an argument.", e.getMessage());
        }
    }
}