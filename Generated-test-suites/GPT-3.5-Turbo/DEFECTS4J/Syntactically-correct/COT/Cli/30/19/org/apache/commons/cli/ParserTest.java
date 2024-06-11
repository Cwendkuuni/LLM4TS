import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[] {"-a", "arg1", "-b", "arg2"};
        
        Parser parser = new ParserImpl();
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("arg2", result[3]);
    }
    
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = new String[] {"-a", "-b", "value"};

        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }
    
    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = new String[] {"-a"};

        Properties properties = new Properties();
        properties.setProperty("b", "value");

        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, arguments, properties);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }
    
    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.getOption("a").setRequired(true);
        options.getOption("b").setRequired(true);
        options.getOption("c").setRequired(true);

        String[] arguments = new String[] {"-a", "-b", "value"};

        Parser parser = new ParserImpl();
        parser.parse(options, arguments);
    }
    
    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", true, "Option A");

        ListIterator<String> iterator = Arrays.asList(new String[] {"value1", "value2"}).listIterator();

        Parser parser = new ParserImpl();
        parser.processArgs(option, iterator);

        String[] values = option.getValues();

        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsWithMissingArgument() throws ParseException {
        Option option = new Option("a", true, "Option A");

        ListIterator<String> iterator = Arrays.asList(new String[0]).listIterator();

        Parser parser = new ParserImpl();
        parser.processArgs(option, iterator);
    }
    
    @Test
    public void testProcessOption() throws ParseException {
        Option option = new Option("a", true, "Option A");

        ListIterator<String> iterator = Arrays.asList(new String[] {"value"}).listIterator();

        Parser parser = new ParserImpl();
        CommandLine cmd = new CommandLine();
        parser.processOption("a", iterator, cmd);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionWithUnrecognizedOption() throws ParseException {
        Option option = new Option("a", true, "Option A");

        ListIterator<String> iterator = Arrays.asList(new String[0]).listIterator();

        Parser parser = new ParserImpl();
        CommandLine cmd = new CommandLine();
        parser.processOption("b", iterator, cmd);
    }
    
    @Test
    public void testUpdateRequiredOptions() throws ParseException {
        Option optionA = new Option("a", false, "Option A");
        Option optionB = new Option("b", true, "Option B");
        Option optionC = new Option("c", true, "Option C");
        optionB.setRequired(true);
        optionC.setRequired(true);

        List<Options> optionGroups = new ArrayList<>();
        OptionGroup group = new OptionGroup();
        group.addOption(optionA);
        group.addOption(optionB);
        optionGroups.add(group);

        Options options = new Options();
        options.addOption(optionA);
        options.addOption(optionC);
        options.addOptionGroup(group);

        Parser parser = new ParserImpl();
        parser.updateRequiredOptions(optionA);

        assertFalse(optionB.isRequired());
        assertFalse(group.isRequired());
        assertTrue(optionC.isRequired());
    }
}

