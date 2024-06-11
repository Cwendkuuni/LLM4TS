import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option opt1 = new Option("o", "option1", true, "Option 1");
        Option opt2 = new Option("o", "option2", true, "Option 2");
        options.addOption(opt1);
        options.addOption(opt2);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        List<Option> expected = Arrays.asList(opt1, opt2);
        assertEquals(expected, parser.getRequiredOptions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullOptions() throws ParseException {
        Parser parser = new DefaultParser();
        parser.parse(null, new String[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, new String[0]);
    }

    @Test
    public void testParseWithSimpleArguments() throws ParseException {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Option");
        options.addOption(opt);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"-o", "value1", "--"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("option"));
        assertEquals("value1", cmd.getOptionValue("option"));
    }

    @Test
    public void testParseWithMultipleArguments() throws ParseException {
        Options options = new Options();
        Option opt1 = new Option("o", "option1", true, "Option 1");
        Option opt2 = new Option("o", "option2", true, "Option 2");
        options.addOption(opt1);
        options.addOption(opt2);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"-o", "value1", "--", "-o", "value2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("option1"));
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals("value2", cmd.getOptionValue("option2"));
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Option");
        options.addOption(opt);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"-u", "value1", "--"};
        try {
            parser.parse(options, arguments);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Unrecognized option: u", e.getMessage());
        }
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Option");
        options.addOption(opt);
        options.addRequiredOption(opt);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"--"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Option");
        options.addOption(opt);
        Properties properties = new Properties();
        properties.setProperty("option", "value");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, null, properties);
        assertTrue(cmd.hasOption("option"));
        assertEquals("value", cmd.getOptionValue("option"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Option");
        options.addOption(opt);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"-o", "value1", "-a", "value2", "--"};
        CommandLine cmd = parser.parse(options, arguments, true);
        assertTrue(cmd.hasOption("option"));
        assertEquals("value1", cmd.getOptionValue("option"));
        assertTrue(cmd.getArgs().contains("value2"));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        Option opt1 = new Option("o1", "option1", true, "Option 1");
        Option opt2 = new Option("o2", "option2", true, "Option 2");
        group.addOption(opt1);
        group.addOption(opt2);
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"--group", "-o1", "value1", "-o2", "value2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("option1"));
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals("value2", cmd.getOptionValue("option2"));
        assertTrue(cmd.getArgs().isEmpty());
    }
}