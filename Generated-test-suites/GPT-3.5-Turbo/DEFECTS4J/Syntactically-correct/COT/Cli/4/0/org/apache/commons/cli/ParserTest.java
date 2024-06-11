
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParseWithNoOptions() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[] {"arg1", "arg2"};
        Parser parser = new ParserImpl();

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(arguments.length, cmd.getArgList().size());
        assertEquals(arguments[0], cmd.getArgList().get(0));
        assertEquals(arguments[1], cmd.getArgList().get(1));
    }

    @Test
    public void testParseWithOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("v").longOpt("verbose").build());
        String[] arguments = new String[] {"-v"};
        Parser parser = new ParserImpl();

        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("v"));
        assertTrue(cmd.hasOption("verbose"));
        assertEquals(1, cmd.getArgList().size());
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        Options options = new Options();
        Option option = Option.builder("f").longOpt("file").required().hasArg().build();
        options.addOption(option);
        String[] arguments = new String[] {"-f", "file.txt"};
        Parser parser = new ParserImpl();

        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("file"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[0];
        Parser parser = new ParserImpl();

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(0, cmd.getArgList().size());
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option option = Option.builder("f").longOpt("file").required().hasArg().build();
        options.addOption(option);
        String[] arguments = new String[] {};
        Parser parser = new ParserImpl();

        parser.parse(options, arguments); // Should throw ParseException
    }

    @Test
    public void testParseWithNonOptionArguments() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("v").longOpt("verbose").build());
        String[] arguments = new String[] {"arg1", "-v", "arg2"};
        Parser parser = new ParserImpl();

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getArgList().size());
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));
        assertTrue(cmd.hasOption("v"));
        assertTrue(cmd.hasOption("verbose"));
    }
}
