import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
    }

    @Test
    public void testSetOptions() {
        Options options = new Options();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("f", "foo", false, "foo option");
        option.setRequired(true);
        options.addOption(option);
        parser.setOptions(options);
        assertEquals(1, parser.getRequiredOptions().size());
        assertTrue(parser.getRequiredOptions().contains(option.getKey()));
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        Option option = new Option("f", "foo", false, "foo option");
        options.addOption(option);
        String[] arguments = {"-f"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("f"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws MissingOptionException {
        Options options = new Options();
        Option option = new Option("f", "foo", false, "foo option");
        option.setRequired(true);
        options.addOption(option);
        String[] arguments = {};
        parser.parse(options, arguments);
        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("o", "option", true, "option with value");
        ListIterator iter = new ArrayList<String>().listIterator();
        iter.add("value");
        parser.processArgs(option, iter);
        assertEquals("value", option.getValue());
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("f", "foo", false, "foo option");
        options.addOption(option);
        ListIterator iter = new ArrayList<String>().listIterator();
        iter.add("f");
        parser.processOption("f", iter);
        assertTrue(parser.parse(options, new String[]{"-f"}).hasOption("f"));
    }
}