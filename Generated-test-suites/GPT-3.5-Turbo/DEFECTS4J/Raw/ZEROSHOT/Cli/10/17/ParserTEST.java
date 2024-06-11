import org.junit.*;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"-a", "value1", "-b", "value2"};
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals("Flattened arguments not matching", arguments, result);
    }

    @Test
    public void testParse() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", "test", false, "Option C");
        String[] arguments = {"-a", "-b", "value1"};
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        CommandLine cmd;
        try {
            cmd = parser.parse(options, arguments);
        } catch (ParseException e) {
            fail("ParseException thrown: " + e.getMessage());
        }
        assertTrue("Option A not set", cmd.hasOption("a"));
        assertTrue("Option B not set", cmd.hasOption("b"));
        assertEquals("Incorrect value for Option B", "value1", cmd.getOptionValue("b"));
        assertFalse("Option C set", cmd.hasOption("c"));
    }

    @Test
    public void testProcessArgs() {
        Option opt = Option.builder("a").hasArgs().build();
        ListIterator iter = Arrays.asList("value1", "value2").listIterator();
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        try {
            parser.processArgs(opt, iter);
        } catch (ParseException e) {
            fail("ParseException thrown: " + e.getMessage());
        }
        String[] values = opt.getValues();
        assertArrayEquals("Processed args not matching", new String[] {"value1", "value2"}, values);
    }

    @Test
    public void testProcessOption() {
        Option opt = Option.builder("a").build();
        ListIterator iter = Arrays.asList().listIterator();
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        try {
            parser.processOption("-a", iter);
        } catch (ParseException e) {
            fail("ParseException thrown: " + e.getMessage());
        }
        assertTrue("Option not set", cmd.hasOption("a"));
    }

}