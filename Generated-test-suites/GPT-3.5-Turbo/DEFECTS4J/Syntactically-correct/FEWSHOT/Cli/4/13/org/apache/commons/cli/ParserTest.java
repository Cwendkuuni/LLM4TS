import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    
    private Options options;
    private Parser parser;
    
    public ParserTest() {
        options = new Options();
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
    }
    
    @Test
    public void testParse() throws ParseException {
        String[] arguments = {"-f", "file.txt"};
        
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        
        CommandLine cmd = parser.parse(options, arguments);
        
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
    }
    
    @Test
    public void testParseWithProperties() throws ParseException {
        String[] arguments = {"-f"};
        
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        
        Properties properties = new Properties();
        properties.setProperty("file", "file.txt");
        
        CommandLine cmd = parser.parse(options, arguments, properties);
        
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
    }
    
    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] arguments = {"-f", "file.txt", "--", "arg1", "arg2"};
        
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        
        CommandLine cmd = parser.parse(options, arguments, true);
        
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));
    }
    
    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("f", "file", true, "input file");
        option.setArgs(2);
        options.addOption(option);
        
        ListIterator<String> iterator = Arrays.asList("file1.txt", "file2.txt").listIterator();
        
        parser.processArgs(option, iterator);
        assertTrue(option.hasValues());
        assertEquals("file1.txt", option.getValue(0));
        assertEquals("file2.txt", option.getValue(1));
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArg() throws ParseException {
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        
        ListIterator<String> iterator = Arrays.asList().listIterator();
        
        parser.processArgs(option, iterator);
    }
    
    @Test
    public void testProcessOption() throws ParseException {
        String arg = "-f";
        
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        
        ListIterator<String> iterator = Arrays.asList("file.txt").listIterator();
        
        parser.processOption(arg, iterator);
        
        assertTrue(parser.getCommandLine().hasOption("f"));
        assertEquals("file.txt", parser.getCommandLine().getOptionValue("f"));
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        String arg = "-x";
        
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        
        ListIterator<String> iterator = Arrays.asList().listIterator();
        
        parser.processOption(arg, iterator);
    }
    
    @Test
    public void testProcessProperties() {
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        
        Properties properties = new Properties();
        properties.setProperty("file", "file.txt");
        
        parser.processProperties(properties);
        
        assertTrue(parser.getCommandLine().hasOption("f"));
        assertEquals("file.txt", parser.getCommandLine().getOptionValue("f"));
    }
    
    @Test
    public void testCheckRequiredOptions() throws MissingOptionException {
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        options.addRequiredOption(option);
        
        parser.checkRequiredOptions();
    }
    
    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsMissingOption() throws MissingOptionException {
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        options.addRequiredOption(option);
        
        parser.getRequiredOptions().remove(option);
        
        parser.checkRequiredOptions();
    }
    
}