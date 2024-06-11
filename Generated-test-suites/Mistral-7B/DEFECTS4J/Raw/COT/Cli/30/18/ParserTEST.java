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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    @Test
    public void testSetOptions() throws ParseException
    {
        Options options = new Options();
        Parser parser = new TestParser();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() throws ParseException
    {
        Options options = new Options();
        Parser parser = new TestParser();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() throws ParseException
    {
        Options options = new Options();
        Parser parser = new TestParser();
        parser.setOptions(options);
        List<String> requiredOptions = parser.getRequiredOptions();
        assertTrue(requiredOptions.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFlattenWithNullOptions() throws ParseException
    {
        Parser parser = new TestParser();
        String[] arguments = new String[0];
        parser.flatten(null, arguments, false);
    }

    @Test
    public void testFlattenWithEmptyArguments() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        String[] arguments = new String[0];
        String[] result = parser.flatten(options, arguments, false);
        assertTrue(Arrays.equals(new String[0], result));
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().isEmpty());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        String[] arguments = {"--", "invalid"};
        parser.parse(options, arguments, false);
    }

    @Test
    public void testParseWithSingleOption() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "defaultValue");
        options.addOption(opt);
        String[] arguments = {"-a"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("a", option.getOpt());
        assertTrue(option.isSet());
        assertEquals("defaultValue", option.getValue());
        assertFalse(cmd.hasArg());
    }

    @Test
    public void testParseWithSingleValueOption() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "defaultValue");
        options.addOption(opt);
        String[] arguments = {"-a", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("a", option.getOpt());
        assertTrue(option.isSet());
        assertEquals("value", option.getValue());
        assertFalse(cmd.hasArg());
    }

    @Test
    public void testParseWithMultipleValueOption() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "defaultValue");
        opt.setArgName("arg");
        opt.setValueSeparator(';');
        options.addOption(opt);
        String[] arguments = {"-a", "value1", "--", "-a", "value2"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("a", option.getOpt());
        assertTrue(option.isSet());
        String[] values = (String[]) option.getValue();
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertFalse(cmd.hasArg());
    }

    @Test
    public void testParseWithOptionalValueOption() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "defaultValue");
        opt.setRequired(false);
        options.addOption(opt);
        String[] arguments = {"-a"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("a", option.getOpt());
        assertFalse(option.isSet());
        assertEquals(null, option.getValue());
        assertFalse(cmd.hasArg());
        arguments = new String[]{"-a", "value"};
        cmd = parser.parse(options, arguments);
        option = optionsList.get(0);
        assertTrue(option.isSet());
        assertEquals("value", option.getValue());
        assertFalse(cmd.hasArg());
    }

    @Test
    public void testParseWithLongOption() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option opt = new Option("long-option", "long-arg", true, "defaultValue");
        options.addOption(opt);
        String[] arguments = {"--long-option"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("long-option", option.getOpt());
        assertTrue(option.isSet());
        assertEquals("defaultValue", option.getValue());
        assertFalse(cmd.hasArg());
    }

    @Test
    public void testParseWithOneArgOption() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "defaultValue");
        opt.setArgName("arg");
        options.addOption(opt);
        String[] arguments = {"-a", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("a", option.getOpt());
        assertTrue(option.isSet());
        assertEquals("value", option.getValue());
        assertTrue(cmd.hasArg());
        assertEquals("value", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithMultipleArgsOption() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "defaultValue");
        opt.setArgName("args");
        opt.setValuesSeparated(true);
        options.addOption(opt);
        String[] arguments = {"-a", "arg1", "arg2", "--"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("a", option.getOpt());
        assertTrue(option.isSet());
        String[] values = (String[]) option.getValue();
        assertEquals(2, values.length);
        assertEquals("arg1", values[0]);
        assertEquals("arg2", values[1]);
        assertTrue(cmd.hasArg());
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgumentOption() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "defaultValue");
        opt.setArgName("arg");
        options.addOption(opt);
        String[] arguments = {"-a"};
        parser.parse(options, arguments, false);
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "defaultValue");
        opt.setArgName("arg");
        options.addOption(opt);
        Properties properties = new Properties();
        properties.setProperty("a", "value");
        CommandLine cmd = parser.parse(options, new String[0], properties, false);
        Option option = cmd.getOptions().get(0);
        assertEquals("a", option.getOpt());
        assertTrue(option.isSet());
        assertEquals("value", option.getValue());
        assertFalse(cmd.hasArg());
    }

    @Test
    public void testParseWithRequiredProperties() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option opt = new Option("a", "arg", true, "defaultValue");
        opt.setArgName("arg");
        options.addRequiredOption(opt);
        Properties properties = new Properties();
        CommandLine cmd = parser.parse(options, new String[0], properties, false);
        Option option = cmd.getOptions().get(0);
        assertEquals("a", option.getOpt());
        assertTrue(option.isSet());
        assertNull(option.getValue());
        assertFalse(cmd.hasArg());
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException
    {
        Parser parser = new TestParser();
        Options options = new Options();
        Option opt1 = new Option("a", "arg1");
        opt1.setRequired(true);
        Option opt2 = new Option("b", "arg2");
        opt2.setRequired(true);
        options.addOption(opt1);
        options.addOption(opt2);
        parser.parse(options, new String[0], null, false);
    }

    @Test
    public void testProcessArgs() throws ParseException
    {
        Parser parser = new TestParser();
        Option opt = new Option("a", "arg", true, "defaultValue");
        opt.setArgName("arg");
        parser.processArgs(opt, null);
    }

    @Test(expected = ParseException.class)
    public void testProcessOptionWithNoArg() throws ParseException
    {
        Parser parser = new TestParser();
        Option opt = new Option("a", "arg");
        ListIterator iterator = null;
        parser.processOption(opt, iterator);
    }

    @Test(expected = ParseException.class)
    public void testProcessOptionWithInvalidArg() throws ParseException
    {
        Parser parser = new TestParser();
        Option opt = new Option("a", "arg");
        ListIterator iterator = (ListIterator) Arrays.asList("invalid").iterator();
        parser.processOption(opt, iterator);
    }

    @Test
    public void testProcessOption() throws ParseException
    {
        Parser parser = new TestParser();
        Option opt = new Option("a", "arg");
        ListIterator iterator = (ListIterator) Arrays.asList("-a", "--").iterator();
        parser.processOption(opt, iterator);
    }

    @Test
    public void testUpdateRequiredOptions() throws ParseException
    {
        Parser parser = new TestParser();
        Option opt = new Option("a", "arg");
        opt.setRequired(true);
        parser.updateRequiredOptions(opt);
        assertTrue(parser.getRequiredOptions().isEmpty());
    }

    private static class TestParser extends Parser
    {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException
        {
            return super.flatten(opts, arguments, stopAtNonOption);
        }
    }
}