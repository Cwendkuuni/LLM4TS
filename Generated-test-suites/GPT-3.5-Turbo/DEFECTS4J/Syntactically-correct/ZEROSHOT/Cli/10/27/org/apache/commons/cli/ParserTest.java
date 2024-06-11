import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
    }
    
    @After
    public void tearDown() {
        parser = null;
    }
    
    @Test
    public void testSetOptions() {
        Options options = new Options();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }
    
    @Test
    public void testGetOptions() {
        Options options = new Options();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }
    
    @Test
    public void testGetRequiredOptions() {
        List requiredOptions = new ArrayList();
        parser.setRequiredOptions(requiredOptions);
        assertSame(requiredOptions, parser.getRequiredOptions());
    }
    
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[] {"arg1", "arg2"};
        CommandLine result = parser.parse(options, arguments);
        assertNotNull(result);
        assertEquals(0, result.getOptions().length);
        assertEquals(2, result.getArgs().length);
        assertArrayEquals(arguments, result.getArgs());
    }
    
    @Test
    public void testParseWithOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        String[] arguments = new String[] {"-a", "-b", "value"};
        CommandLine result = parser.parse(options, arguments);
        assertNotNull(result);
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("b"));
    }
    
    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsMissingOption() throws MissingOptionException {
        List requiredOptions = new ArrayList();
        requiredOptions.add("a");
        parser.setRequiredOptions(requiredOptions);
        parser.checkRequiredOptions();
    }
    
    @Test
    public void testCheckRequiredOptionsNoMissingOption() throws MissingOptionException {
        List requiredOptions = new ArrayList();
        parser.setRequiredOptions(requiredOptions);
        parser.checkRequiredOptions();
    }
    
    @Test
    public void testProcessArgs() throws ParseException {
        Option opt = new Option("a", true, "option a");
        ListIterator iter = Arrays.asList(new String[] {"value1", "value2"}).listIterator();
        parser.processArgs(opt, iter);
        String[] expectedValues = new String[] {"value1", "value2"};
        assertArrayEquals(expectedValues, opt.getValues());
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Option opt = new Option("a", true, "option a");
        ListIterator iter = Arrays.asList(new String[0]).listIterator();
        parser.processArgs(opt, iter);
    }
    
    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        Option opt = new Option("a", false, "option a");
        options.addOption(opt);
        ListIterator iter = Arrays.asList(new String[] {"-a"}).listIterator();
        parser.setOptions(options);
        parser.processOption("-a", iter);
        assertTrue(parser.getCommandLine().hasOption("a"));
    }

}