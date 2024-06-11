import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
    }
    
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[0];
        
        CommandLine result = parser.parse(options, arguments);
        
        assertNotNull(result);
    }
    
    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[0];
        Properties properties = new Properties();
        
        CommandLine result = parser.parse(options, arguments, properties);
        
        assertNotNull(result);
    }
    
    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[0];
        
        CommandLine result = parser.parse(options, arguments, true);
        
        assertNotNull(result);
    }
    
    @Test
    public void testParseWithPropertiesAndStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[0];
        Properties properties = new Properties();
        
        CommandLine result = parser.parse(options, arguments, properties, true);
        
        assertNotNull(result);
    }
    
    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("test", true, "test option");
        ListIterator iterator = Arrays.asList("value1", "value2").listIterator();
        
        parser.processArgs(option, iterator);
        
        String[] values = option.getValues();
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsWithMissingArgument() throws ParseException {
        Option option = new Option("test", true, "test option");
        ListIterator iterator = new ArrayList<String>().listIterator();
        
        parser.processArgs(option, iterator);
    }
    
    @Test
    public void testProcessOption() throws ParseException {
        Option option = new Option("test", true, "test option");
        ListIterator iterator = Arrays.asList("value1").listIterator();
        
        parser.processOption("test", iterator);
        
        CommandLine cmd = parser.getCommand();
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("test"));
        Option resultOption = cmd.getOption("test");
        assertNotNull(resultOption);
        String[] values = resultOption.getValues();
        assertNotNull(values);
        assertEquals(1, values.length);
        assertEquals("value1", values[0]);
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionWithUnrecognizedOption() throws ParseException {
        ListIterator iterator = Arrays.asList("value1").listIterator();
        
        parser.processOption("unrecognized", iterator);
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testProcessOptionWithMissingArgument() throws ParseException {
        Option option = new Option("test", true, "test option");
        ListIterator iterator = new ArrayList<String>().listIterator();
        
        parser.processOption("test", iterator);
    }
    
    @Test
    public void testSetOptions() {
        Options options = new Options();
        options.addOption(new Option("test", true, "test option"));
        
        parser.setOptions(options);
        
        Options result = parser.getOptions();
        assertNotNull(result);
        assertEquals(1, result.getOptions().size());
        assertTrue(result.hasOption("test"));
    }
    
    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        options.addOption(new Option("test", true, "test option").isRequired());
        
        parser.setOptions(options);
        
        List result = parser.getRequiredOptions();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("test"));
    }
    
    @Test
    public void testCheckRequiredOptions() throws MissingOptionException {
        Options options = new Options();
        options.addOption(new Option("test", true, "test option").isRequired());
        
        parser.setOptions(options);
        
        try {
            parser.checkRequiredOptions();
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            List result = e.getMissingOptions();
            assertNotNull(result);
            assertEquals(1, result.size());
            assertTrue(result.contains("test"));
        }
    }
}