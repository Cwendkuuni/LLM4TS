import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {
    
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        
        String[] arguments = {"-a", "-b", "value"};
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        CommandLine cmd = parser.parse(options, arguments);
        
        assert(cmd.hasOption("a"));
        assert(cmd.hasOption("b"));
        assert(cmd.getOptionValue("b").equals("value"));
    }
    
    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", true, "Option A");
        ListIterator iterator = Arrays.asList("value1", "value2", "value3").listIterator();
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        parser.processArgs(option, iterator);
        
        assert(option.getValues().length == 3);
        assert(option.getValue(0).equals("value1"));
        assert(option.getValue(1).equals("value2"));
        assert(option.getValue(2).equals("value3"));
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Option option = new Option("a", true, "Option A");
        ListIterator iterator = Arrays.asList().listIterator();
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        parser.processArgs(option, iterator);
    }
    
    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        
        String arg = "a";
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        CommandLine cmd = new CommandLine();
        ListIterator iterator = Arrays.asList().listIterator();
        
        parser.processOption(arg, iterator);
        
        assert(cmd.hasOption("a"));
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        
        String arg = "c";
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        CommandLine cmd = new CommandLine();
        ListIterator iterator = Arrays.asList().listIterator();
        
        parser.processOption(arg, iterator);
    }
    
    @Test
    public void testProcessProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        
        Properties properties = new Properties();
        properties.setProperty("a", "value1");
        properties.setProperty("b", "value2");
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        CommandLine cmd = parser.parse(options, null, properties);
        
        assert(cmd.getOptionValue("a").equals("value1"));
        assert(cmd.getOptionValue("b").equals("value2"));
    }
    
    @Test
    public void testCheckRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("a", "Option A");
        options.addRequiredOption("b", "Option B");
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        CommandLine cmd = parser.parse(options, null);
    }
}