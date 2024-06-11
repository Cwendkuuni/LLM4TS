
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testAbstractParser_parse_Options_StringArray() throws Exception {
        Options options = new Options();
        String[] arguments = new String[]{};
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        CommandLine result = parser.parse(options, arguments);
        
        assertNotNull(result);
    }

    @Test
    public void testAbstractParser_parse_Options_StringArray_Properties() throws Exception {
        Options options = new Options();
        String[] arguments = new String[]{};
        Properties properties = new Properties();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        CommandLine result = parser.parse(options, arguments, properties);
        
        assertNotNull(result);
    }

    @Test
    public void testAbstractParser_parse_Options_StringArray_boolean() throws Exception {
        Options options = new Options();
        String[] arguments = new String[]{};
        boolean stopAtNonOption = false;
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        CommandLine result = parser.parse(options, arguments, stopAtNonOption);
        
        assertNotNull(result);
    }

    @Test
    public void testAbstractParser_parse_Options_StringArray_Properties_boolean() throws Exception {
        Options options = new Options();
        String[] arguments = new String[]{};
        Properties properties = new Properties();
        boolean stopAtNonOption = false;
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        CommandLine result = parser.parse(options, arguments, properties, stopAtNonOption);
        
        assertNotNull(result);
    }

    @Test
    public void testAbstractParser_processProperties_Properties() throws Exception {
        Properties properties = new Properties();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        parser.processProperties(properties);
    }

    @Test(expected = MissingOptionException.class)
    public void testAbstractParser_checkRequiredOptions() throws Exception {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        parser.checkRequiredOptions();
    }

    @Test
    public void testAbstractParser_processArgs_Option_ListIterator() throws Exception {
        Option opt = new Option("t", true, "Test option");
        ListIterator iter = Arrays.asList("value1", "value2", "value3").listIterator();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        parser.processArgs(opt, iter);
    }

    @Test
    public void testAbstractParser_processOption_String_ListIterator() throws Exception {
        String arg = "-t";
        ListIterator iter = Collections.singletonList("value").listIterator();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        parser.processOption(arg, iter);
    }
}
