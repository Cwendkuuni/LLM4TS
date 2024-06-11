import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testFlatten() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        Options options = new Options();
        String[] arguments = {"-f", "--foo", "bar"};
        try {
            String[] flattenedArguments = parser.flatten(options, arguments, true);
            assertEquals(0, flattenedArguments.length);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParse() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        Options options = new Options();
        String[] arguments = {"-f", "--foo", "bar"};
        try {
            CommandLine cmdLine = parser.parse(options, arguments);
            assertTrue(cmdLine.hasOption("-f"));
            assertTrue(cmdLine.hasOption("--foo"));
            assertEquals("bar", cmdLine.getOptionValue("--foo"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessProperties() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        Options options = new Options();
        options.addOption("f", true, "file");
        
        Properties properties = new Properties();
        properties.setProperty("f", "test.txt");
        
        try {
            CommandLine cmdLine = parser.parse(options, null, properties);
            assertTrue(cmdLine.hasOption("f"));
            assertEquals("test.txt", cmdLine.getOptionValue("f"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckRequiredOptions() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        Options options = new Options();
        Option option = new Option("f", true, "file");
        option.setRequired(true);
        options.addOption(option);
        
        try {
            parser.setOptions(options);
            CommandLine cmdLine = new CommandLine();
            parser.checkRequiredOptions();
        } catch (MissingOptionException e) {
            assertEquals(1, e.getMissingOptions().size());
            assertEquals("f", e.getMissingOptions().get(0).getKey());
        }
    }

    @Test
    public void testProcessArgs() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        Options options = new Options();
        Option option = new Option("f", true, "file");
        options.addOption(option);
        
        ListIterator<String> iter = Arrays.asList("test.txt").listIterator();
        
        try {
            parser.processArgs(option, iter);
            assertEquals("test.txt", option.getValues()[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessOption() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        Options options = new Options();
        Option option = new Option("f", true, "file");
        options.addOption(option);
        
        ListIterator<String> iter = Arrays.asList("-f", "test.txt").listIterator();
        
        try {
            parser.processOption("-f", iter);
            assertTrue(option.hasArg());
            assertEquals("test.txt", option.getValue());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}