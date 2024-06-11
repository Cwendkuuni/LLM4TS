import org.junit.Test;
import org.apache.commons.cli.*;

import java.util.Properties;

public class ParserTest {

    private Options options;
    private Parser parser;
    
    public ParserTest() {
        options = new Options();
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
    }
    
    @Test
    public void testParse() throws ParseException {
        options.addOption("a", "optionA", false, "Option A");
        options.addRequiredOption("b", "optionB", true, "Option B");
        
        String[] arguments = {"-a", "-b", "valueB"};
        
        CommandLine commandLine = parser.parse(options, arguments);
        
        assert(commandLine.hasOption("a"));
        assert(commandLine.hasOption("optionA"));
        assert(commandLine.hasOption("b"));
        assert(commandLine.hasOption("optionB"));
        assert(commandLine.getOptionValue("b").equals("valueB"));
    }

    @Test
    public void testProcessProperties() {
        options.addOption("a", "optionA", false, "Option A");
        
        Properties properties = new Properties();
        properties.setProperty("a", "true");
        
        CommandLine commandLine = new CommandLine();
        parser.processProperties(properties);
        
        assert(commandLine.hasOption("a"));
        assert(commandLine.hasOption("optionA"));
    }

    @Test
    public void testCheckRequiredOptions() throws ParseException {
        options.addOption("a", "optionA", false, "Option A");
        options.addRequiredOption("b", "optionB", true, "Option B");
        
        String[] arguments = {"-a"};
        
        try {
            parser.parse(options, arguments);
        } catch (MissingOptionException e) {
            assert(e.getMissingOptions().size() == 1);
            assert(e.getMissingOptions().get(0).equals("b"));
        }
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("o", "option", true, "Option");
        
        String[] arguments = {"value1", "value2"};
        
        ListIterator iterator = java.util.Arrays.asList(arguments).listIterator();
        
        parser.processArgs(option, iterator);
        
        assert(option.getValues().length == 2);
        assert(option.getValue(0).equals("value1"));
        assert(option.getValue(1).equals("value2"));
    }

    @Test
    public void testProcessOption() throws ParseException {
        options.addOption("a", "optionA", false, "Option A");
        
        String arg = "a";
        
        ListIterator iterator = java.util.Arrays.asList(new String[0]).listIterator();
        
        parser.processOption(arg, iterator);
        
        CommandLine commandLine = new CommandLine();
        assert(commandLine.hasOption("a"));
        assert(commandLine.hasOption("optionA"));
    }
    
}