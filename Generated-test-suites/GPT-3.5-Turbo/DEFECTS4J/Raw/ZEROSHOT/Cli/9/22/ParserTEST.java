import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    @Test
    public void testParse() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = {"-a", "-b", "value"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        try {
            CommandLine cmd = parser.parse(options, args);

            assertTrue(cmd.hasOption("a"));
            assertEquals("value", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }
    
    @Test
    public void testProcessArgs() {
        Options options = new Options();
        options.addOption(Option.builder("f").numberOfArgs(2).optionalArg(true).build());

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        ListIterator<String> iterator = Arrays.asList("-f", "value1", "value2").listIterator();
        Option option = options.getOption("f");

        try {
            parser.processArgs(option, iterator);

            assertEquals("value1", option.getValue(0));
            assertEquals("value2", option.getValue(1));
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }
    
    @Test
    public void testProcessOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        ListIterator<String> iterator = Arrays.asList("-a").listIterator();

        try {
            parser.processOption("-a", iterator);

            assertTrue(cmd.hasOption("a"));
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }
}