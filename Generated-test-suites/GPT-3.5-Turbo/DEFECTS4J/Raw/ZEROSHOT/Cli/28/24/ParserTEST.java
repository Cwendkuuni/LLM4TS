import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
        assertNotNull(parser.getRequiredOptions());
    }
    
    @Test
    public void testParse() throws Exception {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").longOpt("bar").build());
        options.addRequiredOption(Option.builder("c").build());
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        
        String[] args = {"-a", "--bar", "-c", "value"};
        CommandLine cmd = parser.parse(options, args);
        
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertEquals("value", cmd.getOptionValue("c"));
    }
    
    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_missingOption() throws Exception {
        Options options = new Options();
        options.addRequiredOption(Option.builder("a").build());
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        
        parser.setOptions(options);
        parser.checkRequiredOptions();
    }
    
    @Test
    public void testProcessArgs() throws Exception {
        Option option = Option.builder("a").build();
        ListIterator<String> iterator = Arrays.asList("value1", "value2", "value3").listIterator();
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        parser.processArgs(option, iterator);
        
        assertEquals("value1", option.getValues()[0]);
        assertEquals("value2", option.getValues()[1]);
        assertEquals("value3", option.getValues()[2]);
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_missingArgument() throws Exception {
        Option option = Option.builder("a").hasArg().build();
        ListIterator<String> iterator = Arrays.asList().listIterator();
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        parser.processArgs(option, iterator);
    }
    
    @Test
    public void testProcessOption() throws Exception {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());
        
        OptionGroup group = new OptionGroup();
        group.addOption(Option.builder("d").build());
        group.addOption(Option.builder("e").build());
        options.addOptionGroup(group);
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        String arg = "-a";
        ListIterator<String> iterator = Arrays.asList(arg).listIterator();
        CommandLine cmd = new CommandLine();
        parser.setOptions(options);
        parser.processOption(arg, iterator);
        
        assertTrue(cmd.hasOption("a"));
        
        arg = "-d";
        iterator = Arrays.asList(arg).listIterator();
        parser.processOption(arg, iterator);
        
        assertTrue(cmd.hasOption("d"));
        assertFalse(cmd.hasOption("a"));
    }
    
}