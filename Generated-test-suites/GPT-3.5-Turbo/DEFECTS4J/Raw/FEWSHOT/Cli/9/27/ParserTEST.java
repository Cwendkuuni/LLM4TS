
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

public class ParserTest {
    private Parser parser;
    private Options options;
    private Option opt1;
    private Option opt2;
    
    @Before
    public void setup() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        options = new Options();
        opt1 = new Option("a", "option-a", false, "Option A");
        opt2 = new Option("b", "option-b", true, "Option B");
        options.addOption(opt1);
        options.addOption(opt2);
    }

    @Test
    public void testParse() throws ParseException {
        String[] args = {"-a", "-b", "value"};
        CommandLine commandLine = parser.parse(options, args);
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals("value", commandLine.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testMissingArgument() throws ParseException {
        String[] args = {"-a", "-b"};
        parser.parse(options, args);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        Option opt3 = new Option("c", "option-c", true, "Option C");
        opt3.setRequired(true);
        options.addOption(opt3);
        
        String[] args = {"-a", "-b", "value"};
        parser.parse(options, args);
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("a", "1");
        properties.setProperty("b", "true");
        properties.setProperty("c", "value");
        
        parser.processProperties(properties);
        assertTrue(parser.getCommandLine().hasOption("a"));
        assertEquals("1", parser.getCommandLine().getOptionValue("a"));
        assertTrue(parser.getCommandLine().hasOption("b"));
        assertEquals("true", parser.getCommandLine().getOptionValue("b"));
        assertTrue(parser.getCommandLine().hasOption("c"));
        assertEquals("value", parser.getCommandLine().getOptionValue("c"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgumentInProcessArgs() throws ParseException {
        ListIterator<String> iterator = Arrays.asList("-b").listIterator();
        parser.processArgs(opt2, iterator);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        ListIterator<String> iterator = Arrays.asList("-b", "value").listIterator();
        parser.processArgs(opt2, iterator);
        assertEquals("value", opt2.getValue());
    }
}
