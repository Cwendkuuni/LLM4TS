import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import java.util.Properties;

public class ParserTest {
    
    private Options options;
    private CommandLineParser parser;
    
    @Before
    public void setup() {
        options = new Options();
        parser = new DefaultParser();
    }
    
    @Test
    public void testParse() throws ParseException {
        String[] args = "-a".split("\\s");
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }
    
    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = "-a".split("\\s");
        Properties properties = new Properties();
        properties.setProperty("a", "true");
        CommandLine cmd = parser.parse(options, args, properties);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }
    
    @Test
    public void testParseStopAtNonOption() throws ParseException {
        String[] args = "-a -- -b".split("\\s");
        CommandLine cmd = parser.parse(options, args, true);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertTrue(cmd.getArgList().contains("-b"));
    }
    
    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", true, "option a");
        option.setArgs(2);
        String[] args = {"value1", "value2"};
        ListIterator iterator = Arrays.asList(args).listIterator();
        parser.processArgs(option, iterator);
        assertEquals(option.getValue(0), "value1");
        assertEquals(option.getValue(1), "value2");
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsWithMissingArg() throws ParseException {
        Option option = new Option("a", true, "option a");
        option.setArgs(2);
        String[] args = {"value1"};
        ListIterator iterator = Arrays.asList(args).listIterator();
        parser.processArgs(option, iterator);
    }
    
    @Test
    public void testProcessOption() throws ParseException {
        Option option = new Option("a", false, "option a");
        String arg = "a";
        ListIterator iterator = Arrays.asList(arg).listIterator();
        parser.processOption(arg, iterator);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionWithUnrecognizedOption() throws ParseException {
        String arg = "-c";
        ListIterator iterator = Arrays.asList(arg).listIterator();
        parser.processOption(arg, iterator);
    }
    
    @Test
    public void testUpdateRequiredOptions() throws ParseException {
        Option option = new Option("a", true, "option a");
        option.setRequired(true);
        options.addOption(option);
        
        parser.updateRequiredOptions(option);
        
        assertFalse(parser.getRequiredOptions().contains(option.getKey()));
    }
}