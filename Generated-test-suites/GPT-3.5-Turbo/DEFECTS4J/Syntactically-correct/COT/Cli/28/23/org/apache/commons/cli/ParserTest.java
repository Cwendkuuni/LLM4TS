import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {
    
    private Parser parser;
    private Options options;
    
    @Before
    public void setUp() {
        parser = new TestParser();
        options = new Options();
    }
    
    @Test
    public void testParse() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[]{"-a", "value"});
        assertNotNull(cmd);
    }
    
    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("a", "value");
        CommandLine cmd = parser.parse(options, new String[]{"-b"}, properties);
        assertNotNull(cmd);
    }
    
    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        CommandLine cmd = parser.parse(options, new String[]{"-a", "value", "nonOption"}, true);
        assertNotNull(cmd);
    }
    
    @Test
    public void testParseWithPropertiesAndStopAtNonOption() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("a", "value");
        CommandLine cmd = parser.parse(options, new String[]{"-b", "nonOption"}, properties, true);
        assertNotNull(cmd);
    }
    
    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws MissingOptionException {
        parser.checkRequiredOptions();
    }
    
    @Test
    public void testProcessArgs() throws ParseException {
        Option opt = OptionBuilder.withArgName("arg")
                                 .hasArg()
                                 .create("option");
        ListIterator iter = Arrays.asList(new String[]{"value"}).listIterator();
        parser.processArgs(opt, iter);
        assertTrue(opt.getValues().length > 0);
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingValue() throws ParseException {
        Option opt = OptionBuilder.withArgName("arg")
                                 .hasArg()
                                 .create("option");
        ListIterator iter = Collections.emptyList().listIterator();
        parser.processArgs(opt, iter);
    }
    
    @Test
    public void testProcessOption() throws ParseException {
        Option opt = OptionBuilder.withArgName("arg")
                                 .hasArg()
                                 .create("option");
        ListIterator iter = Arrays.asList(new String[]{"-option", "value"}).listIterator();
        parser.processOption("-option", iter);
        assertTrue(cmd.hasOption("option"));
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        Option opt = OptionBuilder.withArgName("arg")
                                 .hasArg()
                                 .create("option");
        ListIterator iter = Collections.emptyList().listIterator();
        parser.processOption("-option", iter);
    }
}

class TestParser extends Parser {

    protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return new String[0];
    }
}