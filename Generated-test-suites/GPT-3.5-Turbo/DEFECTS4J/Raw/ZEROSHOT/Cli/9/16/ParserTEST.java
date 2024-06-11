import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
        assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
    }

    @Test
    public void testParse() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };

        Options options = new Options();
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"});

        assertEquals(0, cmd.getOptions().size());
        assertEquals(2, cmd.getArgList().size());

        assertTrue(cmd.getArgList().contains("arg1"));
        assertTrue(cmd.getArgList().contains("arg2"));
    }

    @Test
    public void testProcessProperties() {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };

        Properties properties = new Properties();
        properties.setProperty("key1", "value1");

        Options options = new Options();
        options.addOption(Option.builder("key1").hasArg().build());
        options.addOption(Option.builder("key2").hasArg().build());

        CommandLine cmd = new CommandLine();

        parser.setOptions(options);
        parser.processProperties(properties);

        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.hasOption("key1"));
        assertEquals("value1", cmd.getOptionValue("key1"));

        assertFalse(cmd.hasOption("key2"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws MissingOptionException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };

        Options options = new Options();
        options.addOption(Option.builder("key1").isRequired().build());
        options.addOption(Option.builder("key2").isRequired().build());
        options.addOption(Option.builder("key3").isRequired().build());

        parser.setOptions(options);
        parser.checkRequiredOptions();
    }
}