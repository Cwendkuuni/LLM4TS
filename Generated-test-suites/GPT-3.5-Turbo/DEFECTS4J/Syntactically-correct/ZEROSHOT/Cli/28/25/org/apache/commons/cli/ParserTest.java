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
                return new String[0];
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
    public void testGetOptions() {
        Options options = new Options();
        parser.setOptions(options);

        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        parser.setOptions(options);

        assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
    }

    @Test
    public void testParse1() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"arg1", "arg2"};
        CommandLine cmd = new CommandLine();
        CommandLine result = parser.parse(options, arguments);

        assertEquals(cmd, result);
    }

    @Test
    public void testParse2() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"arg1", "arg2"};
        Properties properties = new Properties();
        CommandLine cmd = new CommandLine();
        CommandLine result = parser.parse(options, arguments, properties);

        assertEquals(cmd, result);
    }

    @Test
    public void testParse3() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"arg1", "arg2"};
        boolean stopAtNonOption = false;
        CommandLine cmd = new CommandLine();
        CommandLine result = parser.parse(options, arguments, stopAtNonOption);

        assertEquals(cmd, result);
    }

    @Test
    public void testParse4() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"arg1", "arg2"};
        Properties properties = new Properties();
        boolean stopAtNonOption = false;
        CommandLine cmd = new CommandLine();
        CommandLine result = parser.parse(options, arguments, properties, stopAtNonOption);

        assertEquals(cmd, result);
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        parser.processProperties(properties);
    }

    @Test
    public void testCheckRequiredOptions() throws MissingOptionException {
        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option opt = new Option("o", "option", true, "option description");
        ListIterator iter = new ArrayList<String>().listIterator();
        parser.processArgs(opt, iter);
    }

    @Test
    public void testProcessOption() throws ParseException {
        String arg = "option";
        ListIterator iter = new ArrayList<String>().listIterator();
        parser.processOption(arg, iter);
    }
}