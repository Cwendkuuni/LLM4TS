import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    @Test
    public void testParseEmptyArguments() throws ParseException
    {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = {};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseSingleArgument() throws ParseException
    {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = {"arg"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getArgs().get(0));
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseMultipleArguments() throws ParseException
    {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = {"arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseOptionWithoutValue() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Description of option A");
        Parser parser = new DefaultParser();
        String[] arguments = {"-a"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("optionA"));
    }

    @Test
    public void testParseOptionWithValue() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Description of option A");
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "valueA"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("optionA"));
        assertEquals("valueA", cmd.getOptionValue("optionA"));
    }

    @Test
    public void testParseMultipleOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Description of option A");
        options.addOption("b", "optionB", false, "Description of option B");
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "-b"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("optionA"));
        assertTrue(cmd.hasOption("optionB"));
    }

    @Test
    public void testParseOptionWithValueAndArguments() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Description of option A");
        options.addOption("b", "optionB", true, "Description of option B");
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "valueA", "--", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
        assertTrue(cmd.hasOption("optionA"));
        assertEquals("valueA", cmd.getOptionValue("optionA"));
        assertTrue(cmd.hasOption("optionB"));
    }

    @Test
    public void testParseMissingOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Description of option A");
        Parser parser = new DefaultParser();
        String[] arguments = {"-b"};
        assertThrows(ParseException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseInvalidOption() throws ParseException
    {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = {"--invalid"};
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseMissingRequiredOption() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("a", "optionA", false, "Description of option A");
        Parser parser = new DefaultParser();
        String[] arguments = {};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseMultipleMissingRequiredOptions() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("a", "optionA", false, "Description of option A");
        options.addRequiredOption("b", "optionB", false, "Description of option B");
        Parser parser = new DefaultParser();
        String[] arguments = {};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseMissingRequiredOptionWithOtherOptions() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("a", "optionA", false, "Description of option A");
        options.addOption("b", "optionB", false, "Description of option B");
        Parser parser = new DefaultParser();
        String[] arguments = {"-b"};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseProperty() throws ParseException
    {
        Options options = new Options();
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");
        CommandLine cmd = parser.parse(options, null, properties);
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("optionA"));
        assertEquals("valueA", cmd.getOptionValue("optionA"));
    }

    @Test
    public void testParsePropertyWithArguments() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Description of option A");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA --arg1 arg2");
        CommandLine cmd = parser.parse(options, null, properties);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
        assertTrue(cmd.hasOption("optionA"));
        assertEquals("valueA", cmd.getOptionValue("optionA"));
    }

    @Test
    public void testParseMultipleProperties() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Description of option A");
        options.addOption("b", "optionB", true, "Description of option B");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA --arg1 arg2");
        properties.setProperty("optionB", "valueB --arg3 arg4");
        CommandLine cmd = parser.parse(options, null, properties);
        assertEquals(4, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
        assertEquals("arg3", cmd.getArgs().get(2));
        assertEquals("arg4", cmd.getArgs().get(3));
        assertTrue(cmd.hasOption("optionA"));
        assertEquals("valueA", cmd.getOptionValue("optionA"));
        assertTrue(cmd.hasOption("optionB"));
        assertEquals("valueB", cmd.getOptionValue("optionB"));
    }
}