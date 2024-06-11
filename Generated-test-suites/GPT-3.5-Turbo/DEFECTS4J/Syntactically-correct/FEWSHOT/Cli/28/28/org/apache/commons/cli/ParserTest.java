import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        String[] arguments = {"-a", "-b", "value"};

        Parser parser = new DefaultParser();

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] arguments = {"-a"};

        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] arguments = {"-a", "non-option"};

        Parser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments, true);

        assertTrue(cmd.hasOption("a"));
        assertEquals(1, cmd.getArgList().size());
        assertEquals("non-option", cmd.getArgList().get(0));
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", true, "option a");

        ListIterator iter = Arrays.asList("arg1", "arg2").listIterator();

        Parser parser = new DefaultParser();

        parser.processArgs(option, iter);

        assertEquals(2, option.getValues().length);
        assertEquals("arg1", option.getValue(0));
        assertEquals("arg2", option.getValue(1));
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "option a");

        ListIterator iter = Arrays.asList("-a", "value").listIterator();

        Parser parser = new DefaultParser();

        CommandLine cmd = new CommandLine();

        parser.processOption("-a", iter);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }
}