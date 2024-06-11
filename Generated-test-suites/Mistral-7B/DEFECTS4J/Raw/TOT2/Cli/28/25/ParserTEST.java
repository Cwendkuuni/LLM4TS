import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    @Test
    public void testParseSimple() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a"});

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertNull(cmd.getOptionValue("b"));
        assertNull(cmd.getArgs());
    }

    @Test
    public void testParseWithValue() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "valueA"});

        assertTrue(cmd.hasOption("a"));
        assertEquals("valueA", cmd.getOptionValue("a"));
        assertNull(cmd.getArgs());
    }

    @Test
    public void testParseWithArg() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A value");
        options.addOption("b", "optionB", true, "Option B value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "valueA", "-b", "valueB"});

        assertTrue(cmd.hasOption("a"));
        assertEquals("valueA", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("valueB", cmd.getOptionValue("b"));
        assertNull(cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-b"});

        // Unrecognized option: b
    }

    @Test(expected = ParseException.class)
    public void testParseMissingValue() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a"});

        // Missing value for option: a
    }

    @Test
    public void testParseWithArgParsing() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "valueA=1,2,3"});

        assertTrue(cmd.hasOption("a"));
        assertEquals("1,2,3", cmd.getOptionValue("a"));
        assertNull(cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidValue() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "valueA=invalid"});

        // The option value "invalid" has an invalid format
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A value", true);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0]);

        // Missing required option: a
    }

    @Test
    public void testParseWithRequiredOptionValue() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A value", true);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--optionA"});

        assertTrue(cmd.hasOption("a"));
        assertEquals("", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithLongOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("longOptionA", "optionALong", false, "Long Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--optionALong"});

        assertTrue(cmd.hasOption("longOptionA"));
        assertFalse(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithGroup() throws ParseException
    {
        Options options = new Options();
        OptionGroup group = new OptionGroup("myGroup", "My Group");
        group.addOption(new Option("a", "optionA", true, "Option A value"));
        group.addOption(new Option("b", "optionB", true, "Option B value"));
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--myGroup", "valueA", "valueB"});

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("valueA", cmd.getOptionValue("a"));
        assertEquals("valueB", cmd.getOptionValue("b"));
        assertNull(cmd.getArgs());
    }
}