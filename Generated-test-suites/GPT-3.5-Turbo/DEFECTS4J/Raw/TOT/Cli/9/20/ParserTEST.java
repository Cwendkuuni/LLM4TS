import org.junit.*;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;
    private CommandLine cmd;
    
    @Before
    public void setUp(){
        parser = new ParserImpl();
        options = new Options();
        cmd = new CommandLine();
    }
    
    @Test
    public void testParse_DefaultOptions() throws ParseException {
        String[] arguments = {"-a", "-b", "value"};
        
        Option optionA = new Option("a", false, "Option A");
        Option optionB = Option.builder("b")
                               .hasArg()
                               .argName("arg")
                               .desc("Option B")
                               .build();
        
        options.addOption(optionA);
        options.addOption(optionB);
        
        cmd = parser.parse(options, arguments);
        
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }
    
    @Test
    public void testParse_StopAtNonOption() throws ParseException {
        String[] arguments = {"-a", "-b", "--", "arg1", "arg2"};
        
        Option optionA = new Option("a", false, "Option A");
        Option optionB = Option.builder("b")
                               .hasArg()
                               .argName("arg")
                               .desc("Option B")
                               .build();
        
        options.addOption(optionA);
        options.addOption(optionB);
        
        cmd = parser.parse(options, arguments, true);
        
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }
    
    @Test(expected = MissingOptionException.class)
    public void testParse_MissingRequiredOption() throws ParseException {
        String[] arguments = {"-a"};
        
        Option optionA = Option.builder("a")
                               .required()
                               .desc("Option A")
                               .build();
        
        options.addOption(optionA);
        
        cmd = parser.parse(options, arguments);
    }
    
    @Test
    public void testParse_WithProperties() throws ParseException {
        String[] arguments = {"-a"};
        
        Option optionA = Option.builder("a")
                               .desc("Option A")
                               .build();
        
        options.addOption(optionA);
        
        Properties properties = new Properties();
        properties.setProperty("a", "true");
        
        cmd = parser.parse(options, arguments, properties);
        
        assertTrue(cmd.hasOption("a"));
    }
    
    @Test
    public void testProcessArgs_SingleValueOption() throws ParseException {
        Option option = Option.builder("a")
                              .hasArg()
                              .argName("arg")
                              .desc("Option A")
                              .build();
        
        ListIterator iterator = Arrays.asList("value").listIterator();
        
        parser.processArgs(option, iterator);
        
        assertEquals("value", option.getValue());
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_SingleValueOptionNoValue() throws ParseException {
        Option option = Option.builder("a")
                              .hasArg()
                              .argName("arg")
                              .desc("Option A")
                              .build();
        
        ListIterator iterator = Arrays.asList().listIterator();
        
        parser.processArgs(option, iterator);
    }
    
    @Test
    public void testProcessArgs_MultipleValueOption() throws ParseException {
        Option option = Option.builder("a")
                              .hasArgs()
                              .argName("arg")
                              .desc("Option A")
                              .build();
        
        ListIterator iterator = Arrays.asList("value1", "value2").listIterator();
        
        parser.processArgs(option, iterator);
        
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }
    
    @Test
    public void testProcessArgs_MultipleValueOptionStopParsing() throws ParseException {
        Option option = Option.builder("a")
                              .hasArgs()
                              .argName("arg")
                              .desc("Option A")
                              .build();
        
        ListIterator iterator = Arrays.asList("-b", "value1", "-c", "value2").listIterator();
        
        parser.processArgs(option, iterator);
        
        assertArrayEquals(new String[]{"value1", "-b", "value1", "-c", "value2"}, option.getValues());
    }
    
    @Test
    public void testProcessOption_ValidOption() throws ParseException {
        Option option = Option.builder("a")
                              .desc("Option A")
                              .build();
        
        ListIterator iterator = Arrays.asList("-a").listIterator();
        
        parser.processOption("a", iterator);
        
        assertTrue(cmd.hasOption("a"));
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_InvalidOption() throws ParseException {
        Option option = Option.builder("a")
                              .desc("Option A")
                              .build();
        
        ListIterator iterator = Arrays.asList("-b").listIterator();
        
        parser.processOption("b", iterator);
    }
    
    @Test
    public void testProcessProperties() {
        Option optionA = Option.builder("a")
                              .desc("Option A")
                              .build();
        
        options.addOption(optionA);
        
        Properties properties = new Properties();
        properties.setProperty("a", "true");
        
        parser.processProperties(properties);
        
        assertTrue(cmd.hasOption("a"));
    }
    
    @Test
    public void testCheckRequiredOptions_RequiredOptionsPresent() throws MissingOptionException {
        Option optionA = Option.builder("a")
                              .required()
                              .desc("Option A")
                              .build();
        
        Option optionB = Option.builder("b")
                              .required()
                              .desc("Option B")
                              .build();
        
        options.addOption(optionA);
        options.addOption(optionB);
        
        getRequiredOptions().add(optionA.getKey());
        getRequiredOptions().add(optionB.getKey());
        
        parser.checkRequiredOptions();
        
        assertEquals(0, getRequiredOptions().size());
    }
    
    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_RequiredOptionsMissing() throws MissingOptionException {
        Option optionA = Option.builder("a")
                              .required()
                              .desc("Option A")
                              .build();
        
        Option optionB = Option.builder("b")
                              .required()
                              .desc("Option B")
                              .build();
        
        options.addOption(optionA);
        options.addOption(optionB);
        
        getRequiredOptions().add(optionA.getKey());
        getRequiredOptions().add(optionB.getKey());
        
        parser.checkRequiredOptions();
    }
}