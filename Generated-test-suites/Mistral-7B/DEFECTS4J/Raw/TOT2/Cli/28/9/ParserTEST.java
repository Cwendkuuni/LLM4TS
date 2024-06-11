import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testParseWithValidOptions() throws ParseException
    {
        // given
        String[] args = new String[]{"-a", "1", "-b", "2", "--optionC", "3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options, args, null);

        // then
        assertNotNull(cmd);
        assertEquals(args.length, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals("3", cmd.getOptionValue("c"));
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        // given
        String[] args = new String[]{"-a", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "requiredB");

        Parser parser = new DefaultParser();

        // when, then
        try
        {
            parser.parse(options, args, null);
            fail("A MissingOptionException should have been thrown.");
        }
        catch (MissingOptionException e)
        {
            // expected
            assertEquals("Missing required options: [b]", e.getMessage());
        }
    }

    @Test
    public void testParseWithMissingOptionalOption() throws ParseException
    {
        // given
        String[] args = new String[]{"--optionC", "3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options, args, null);

        // then
        assertTrue(cmd.hasOption("c"));
        assertNull(cmd.getOptionValue("c"));
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        // given
        String[] args = new String[]{"--invalidOption", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();

        // when, then
        try
        {
            parser.parse(options, args, null);
            fail("An UnrecognizedOptionException should have been thrown.");
        }
        catch (UnrecognizedOptionException e)
        {
            // expected
            assertEquals("Unrecognized option: invalidOption", e.getMessage());
        }
    }

    @Test
    public void testParseWithMixedLongAndShortOptions() throws ParseException
    {
        // given
        String[] args = new String[]{"-a", "1", "--optionB", "2", "-b", "3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options, args, null);

        // then
        assertEquals(args.length, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals("3", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithValuesFromProperties() throws ParseException
    {
        // given
        String[] args = new String[]{"--optionC", "3"};
        Properties properties = new Properties();
        properties.setProperty("optionC", "4");

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options, args, properties, false);

        // then
        assertTrue(cmd.hasOption("c"));
        assertEquals("4", cmd.getOptionValue("c"));
    }
}