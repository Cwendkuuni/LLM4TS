import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testParse() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");
        options.addOptionGroup(new OptionGroup("group1", "Group 1") {{
            addOption("c", false, "Option C in Group 1");
            addOption("d", true, "Option D in Group 1, which is required");
        }});
        options.addOption("e", null, "Option E with an optional string argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-a", "--b=10", "--c", "--d", "--e=test"};
        CommandLine cmd = parser.parse(args);

        // check the parsed command line
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals(10, cmd.getOptionValue("b", Integer.class));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertTrue(cmd.hasOption("e"));
        assertEquals("test", cmd.getOptionValue("e"));
        assertNull(cmd.getOptionValue("nonexistent"));

        // check that all required options are present
        assertTrue(cmd.hasOption("d"));
        assertFalse(getOptions(parser).getOptionGroup("group1").isSelected());
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("d", true, "Option D, which is required");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-a"};
        parser.parse(args);
        fail("Expected ParseException when required option 'd' is missing");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"--x", "-a"};
        parser.parse(args);
        fail("Expected UnrecognizedOptionException when unrecognized option 'x' is encountered");
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] args = {"-a", "--b"};
        parser.parse(args);
        fail("Expected MissingArgumentException when argument for option 'b' is missing");
    }

    // getter for the options in the Parser under test
    private Options getOptions(Parser parser)
    {
        return parser.getOptions();
    }
}