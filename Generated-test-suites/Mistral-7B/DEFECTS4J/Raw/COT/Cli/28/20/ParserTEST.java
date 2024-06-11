import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    @Test
    public void testSetOptions()
    {
        Options options = new Options();
        Parser parser = new StubParser();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @Test(expected = NullPointerException.class)
    public void testSetOptionsNull()
    {
        Parser parser = new StubParser();
        parser.setOptions(null);
    }

    @Test
    public void testGetOptions()
    {
        Options options = new Options();
        Parser parser = new StubParser();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions()
    {
        Options options = new Options();
        List requiredOptions = Arrays.asList("option1", "option2");
        options.setRequiredOptions(requiredOptions);
        Parser parser = new StubParser();
        parser.setOptions(options);
        List parsedRequiredOptions = parser.getRequiredOptions();
        assertEquals(2, parsedRequiredOptions.size());
        assertEquals("option1", parsedRequiredOptions.get(0));
        assertEquals("option2", parsedRequiredOptions.get(1));
    }

    @Test
    public void testFlattenEmptyArguments() throws ParseException
    {
        Parser parser = new StubParser();
        String[] arguments = new String[0];
        String[] flattenedArguments = parser.flatten(getOptions(), arguments, false);
        assertArrayEquals(new String[0], flattenedArguments);
    }

    @Test
    public void testFlattenSingleArgument() throws ParseException
    {
        Parser parser = new StubParser();
        String[] arguments = {"argument"};
        String[] flattenedArguments = parser.flatten(getOptions(), arguments, false);
        assertArrayEquals(new String[]{"argument"}, flattenedArguments);
    }

    @Test
    public void testFlattenDoubleArgument() throws ParseException
    {
        Parser parser = new StubParser();
        String[] arguments = {"argument1", "argument2"};
        String[] flattenedArguments = parser.flatten(getOptions(), arguments, false);
        assertArrayEquals(new String[]{"argument1", "argument2"}, flattenedArguments);
    }

    @Test
    public void testFlattenSingleOption() throws ParseException
    {
        Parser parser = new StubParser();
        String[] arguments = {"-option"};
        String[] flattenedArguments = parser.flatten(getOptions(), arguments, false);
        assertArrayEquals(new String[]{"-option"}, flattenedArguments);
    }

    @Test
    public void testFlattenDoubleOption() throws ParseException
    {
        Parser parser = new StubParser();
        String[] arguments = {"-option1", "-option2"};
        String[] flattenedArguments = parser.flatten(getOptions(), arguments, false);
        assertArrayEquals(new String[]{"-option1", "-option2"}, flattenedArguments);
    }

    @Test
    public void testFlattenSingleOptionWithValue() throws ParseException
    {
        Parser parser = new StubParser();
        String[] arguments = {"--option=value"};
        String[] flattenedArguments = parser.flatten(getOptions(), arguments, false);
        assertArrayEquals(new String[]{"--option", "value"}, flattenedArguments);
    }

    @Test
    public void testFlattenDoubleOptionWithValues() throws ParseException
    {
        Parser parser = new StubParser();
        String[] arguments = {"--option1=value1", "--option2=value2"};
        String[] flattenedArguments = parser.flatten(getOptions(), arguments, false);
        assertArrayEquals(new String[]{"--option1", "value1", "--option2", "value2"}, flattenedArguments);
    }

    @Test(expected = ParseException.class)
    public void testFlattenSingleNonOption() throws ParseException
    {
        Parser parser = new StubParser();
        String[] arguments = {"non-option"};
        parser.flatten(getOptions(), arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenSingleNonOptionStopAtNonOption() throws ParseException
    {
        Parser parser = new StubParser();
        String[] arguments = {"non-option"};
        parser.flatten(getOptions(), arguments, true);
    }

    @Test
    public void testParseEmptyArguments() throws ParseException
    {
        Parser parser = new StubParser();
        CommandLine commandLine = parser.parse(getOptions(), new String[0][], null, false);
        assertEquals(0, commandLine.getArgs().size());
        assertEquals(0, commandLine.getOptions().size());
    }

    @Test
    public void testParseSingleArgument() throws ParseException
    {
        Parser parser = new StubParser();
        CommandLine commandLine = parser.parse(getOptions(), new String[]{"argument"}, null, false);
        assertEquals(1, commandLine.getArgs().size());
        assertEquals("argument", commandLine.getArgs().get(0));
        assertEquals(0, commandLine.getOptions().size());
    }

    @Test
    public void testParseSingleOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("option", "desc", true, "d");
        options.addOption(option);
        Parser parser = new StubParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"-option"}, null, false);
        assertEquals(0, commandLine.getArgs().size());
        assertTrue(commandLine.hasOption("option"));
        assertFalse(commandLine.hasOption("non-option"));
    }

    @Test
    public void testParseSingleOptionWithValue() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("option", "desc", true, "d");
        options.addOption(option);
        Parser parser = new StubParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"--option=value"}, null, false);
        assertEquals(0, commandLine.getArgs().size());
        assertTrue(commandLine.hasOption("option"));
        assertEquals("value", commandLine.getOptionValue("option"));
        assertNull(commandLine.getOptionValue("non-option"));
    }

    @Test
    public void testParseMultipleOptions() throws ParseException
    {
        Options options = new Options();
        Option option1 = new Option("option1", "desc1", true, "d1");
        Option option2 = new Option("option2", "desc2", true, "d2");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new StubParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"-option1", "-option2"}, null, false);
        assertEquals(0, commandLine.getArgs().size());
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertFalse(commandLine.hasOption("non-option"));
    }

    @Test
    public void testParseMultipleOptionsWithValues() throws ParseException
    {
        Options options = new Options();
        Option option1 = new Option("option1", "desc1", true, "d1");
        Option option2 = new Option("option2", "desc2", true, "d2");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new StubParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"--option1=value1", "--option2=value2"}, null, false);
        assertEquals(0, commandLine.getArgs().size());
        assertTrue(commandLine.hasOption("option1"));
        assertEquals("value1", commandLine.getOptionValue("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertEquals("value2", commandLine.getOptionValue("option2"));
        assertNull(commandLine.getOptionValue("non-option"));
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");
        Parser parser = new StubParser();
        CommandLine commandLine = parser.parse(getOptions(), new String[0][], properties, false);
        assertEquals(0, commandLine.getArgs().size());
        assertTrue(commandLine.hasOption("option1"));
        assertEquals("value1", commandLine.getOptionValue("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertEquals("value2", commandLine.getOptionValue("option2"));
        assertFalse(commandLine.hasOption("non-option"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException
    {
        Options options = new Options();
        List requiredOptions = Arrays.asList("option1", "option2");
        options.setRequiredOptions(requiredOptions);
        Parser parser = new StubParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[0][], null, false);
        throw new AssertionError("MissingOptionException not thrown");
    }

    @Test
    public void testProcessProperties()
    {
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");
        Parser parser = new StubParser();
        parser.processProperties(properties);
        Option option1 = new Option("option1", "desc1", true, "d1");
        Option option2 = new Option("option2", "desc2", true, "d2");
        assertEquals(2, parser.getOptions().size());
        assertEquals(option1, parser.getOptions().get(0));
        assertEquals(option2, parser.getOptions().get(1));
    }

    @Test
    public void testCheckRequiredOptions() throws ParseException
    {
        Options options = new Options();
        List requiredOptions = Arrays.asList("option1", "option2");
        options.setRequiredOptions(requiredOptions);
        Parser parser = new StubParser();
        parser.setOptions(options);
        parser.parse(options, new String[0][], null, false);
        assertFalse(parser.getRequiredOptions().isEmpty());
        parser.checkRequiredOptions();
        throw new AssertionError("MissingOptionException not thrown");
    }

    private static Options getOptions()
    {
        Options options = new Options();
        return options;
    }

    private static class StubParser extends Parser
    {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException
        {
            return super.flatten(opts, arguments, stopAtNonOption);
        }
    }
}