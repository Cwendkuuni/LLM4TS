import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    private Options options;
    private CommandLineParser parser;
    private CommandLine cmd;
    
    @Before
    public void setup() {
        options = new Options();
        parser = new DefaultParser();
        cmd = null;
    }
    
    @Test
    public void testParse_NullOptions_NullArgs() throws ParseException {
        String[] args = null;
        cmd = parser.parse(null, args);
        assertNull(cmd);
    }
    
    @Test
    public void testParse_NullArgs() throws ParseException {
        String[] args = null;
        cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.getArgs().length == 0);
    }
    
    @Test
    public void testParse_EmptyArgs() throws ParseException {
        String[] args = {};
        cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.getArgs().length == 0);
    }
    
    @Test
    public void testParse_HelpOption() throws ParseException {
        options.addOption(Option.builder("h").longOpt("help").desc("Print help").build());
        String[] args = {"-h"};
        cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.getOptions().hasOption("h"));
        assertNull(cmd.getOptionValue("h"));
        assertTrue(cmd.getArgList().isEmpty());
    }
    
    @Test
    public void testParse_OptionWithArgument() throws ParseException {
        options.addOption(Option.builder("v").longOpt("version").desc("Print version").hasArg().build());
        String[] args = {"-v", "1.0"};
        cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.getOptions().hasOption("v"));
        assertEquals(cmd.getOptionValue("v"), "1.0");
        assertTrue(cmd.getArgList().isEmpty());
    }
    
    @Test
    public void testParse_StopAtNonOption() throws ParseException {
        options.addOption(Option.builder("v").longOpt("version").desc("Print version").build());
        options.addOption(Option.builder("f").longOpt("file").desc("Input file").hasArg().build());
        options.addOption(Option.builder("o").longOpt("output").desc("Output file").hasArg().build());
        String[] args = {"-v", "-f", "input.txt", "arg1", "--", "-o", "output.txt"};
        cmd = parser.parse(options, args, true);
        assertNotNull(cmd);
        assertTrue(cmd.getOptions().hasOption("v"));
        assertTrue(cmd.getOptions().hasOption("f"));
        assertEquals(cmd.getOptionValue("f"), "input.txt");
        assertTrue(cmd.getArgList().contains("arg1"));
        assertTrue(cmd.getArgList().contains("-o"));
        assertTrue(cmd.getArgList().contains("output.txt"));
    }
    
    @Test
    public void testParse_Properties() throws ParseException {
        options.addOption(Option.builder("X").desc("Set property").hasArg().build());
        String[] args = {}; 
        Properties props = new Properties();
        props.setProperty("X", "value");
        cmd = parser.parse(options, args, props);
        assertNotNull(cmd);
        assertTrue(cmd.getOptions().hasOption("X"));
        assertEquals(cmd.getOptionValue("X"), "value");
        assertTrue(cmd.getArgs().length == 0);
    }
    
    @Test
    public void testParse_MissingRequiredOption() {
        options.addOption(Option.builder("r").longOpt("required").desc("Required option").build());
        String[] args = {}; 
        try {
            cmd = parser.parse(options, args);
        } catch (MissingOptionException e) {
            assertEquals(e.getMessage(), "Missing required option: r");
        } catch (ParseException e) {
            fail("Unexpected exception");
        }
    }
    
    @Test
    public void testProcessArgs() throws ParseException {
        Option option = Option.builder("a").longOpt("arg").desc("Argument option").hasArgs().build();
        cmd = new CommandLine();
        ListIterator<String> iterator = Arrays.asList("value1", "value2").listIterator();
        parser.processArgs(option, iterator);
        assertEquals(option.getValues().length, 2);
        assertEquals(option.getValues()[0], "value1");
        assertEquals(option.getValues()[1], "value2");
    }
    
    @Test
    public void testProcessOption_InvalidOption() {
        String arg = "-x";
        ListIterator<String> iterator = Arrays.asList(arg).listIterator();
        try {
            parser.processOption(arg, iterator);
            fail("Expected UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            assertEquals(e.getMessage(), "Unrecognized option: -x");
        } catch (ParseException e) {
            fail("Unexpected exception");
        }
    }
}