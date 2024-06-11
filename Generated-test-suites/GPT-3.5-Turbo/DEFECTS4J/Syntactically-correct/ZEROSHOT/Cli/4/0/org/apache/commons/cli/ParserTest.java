import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() throws ParseException{
        Options options = new Options();
        options.addOption("h", "help", false, "display help information");
        options.addOption("v", "version", false, "display version information");
        options.addOption("a", "add", true, "add an item");
        options.addOption("r", "remove", true, "remove an item");

        String[] args = {"-h", "-a", "item"};

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption){
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("h"));
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("r"));

        assertTrue(cmd.getOptionValue("a").equals("item"));
    }

    @Test
    public void testProcessArgs() throws ParseException{
        Option opt = new Option("a", "add", true, "add an item");

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption){
                return arguments;
            }
        };

        ListIterator iterator = Arrays.asList("item1", "item2").listIterator();
        parser.processArgs(opt, iterator);

        String[] values = opt.getValues();
        assertEquals(values.length, 2);
        assertEquals(values[0], "item1");
        assertEquals(values[1], "item2");
    }

    @Test
    public void testProcessOption() throws ParseException{
        Option option = new Option("h", "help", false, "display help information");

        Options options = new Options();
        options.addOption(option);

        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption){
                return arguments;
            }
        };

        ListIterator iterator = Arrays.asList("-h").listIterator();
        parser.processOption("-h", iterator);

        CommandLine cmd = parser.parse(options, new String[0]);

        assertTrue(cmd.hasOption("h"));
    }
}