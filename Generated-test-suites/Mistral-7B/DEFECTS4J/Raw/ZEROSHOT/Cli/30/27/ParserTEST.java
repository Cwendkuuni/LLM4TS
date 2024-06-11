import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    private Parser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", "value", true, "Option D");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")).addOption("e", true);
        options.addOptionGroup(new OptionGroup("group2", "Group 2")).addOption("f", true);
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @Test
    public void testParse_EmptyArgs() throws ParseException
    {
        String[] args = new String[0];
        CommandLine cmd = parser.parse(options, args, null, false);
        assertTrue(cmd.getArgs().size() == 0);
    }

    @Test
    public void testParse_SingleArg() throws ParseException
    {
        String[] args = {"--a"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParse_SingleArgWithValue() throws ParseException
    {
        String[] args = {"--d", "test"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertTrue(cmd.hasOption("d"));
        assertEquals("test", cmd.getOptionValue("d"));
    }

    @Test
    public void testParse_MultipleArgs() throws ParseException
    {
        String[] args = {"--a", "--b", "arg1", "--c"};
        CommandLine cmd = parser.parse(options, args, null, false);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("e")); // Not in this group
        assertFalse(cmd.hasOption("f")); // Not in this group
        assertTrue(cmd.getArgs().size() == 1);
        assertEquals("arg1", cmd.getArgs().get(0));
    }

    @Test(expected = ParseException.class)
    public void testParse_MissingOption() throws ParseException
    {
        String[] args = {"--x"};
        parser.parse(options, args, null, false); // Should throw an exception
    }

    @Test(expected = ParseException.class)
    public void testParse_MissingValue() throws ParseException
    {
        String[] args = {"--d"};
        parser.parse(options, args, null, false); // Should throw an exception
    }

    @Test
    public void testParse_Properties() throws ParseException
    {
        Properties props = new Properties();
        props.setProperty("a", "true");
        props.setProperty("d", "test");
        CommandLine cmd = parser.parse(options, new String[0], props, false);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("d"));
        assertEquals("test", cmd.getOptionValue("d"));
    }

    @Test(expected = ParseException.class)
    public void testParse_UnrecognizedOption() throws ParseException
    {
        String[] args = {"--x"};
        parser.parse(options, args, null, false); // Should throw an exception
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_MissingRequiredOption() throws ParseException
    {
        String[] args = {"--b"};
        parser.parse(options, args, null, false); // Should throw an exception
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_MissingRequiredGroupOption() throws ParseException
    {
        String[] args = {"--a"};
        parser.parse(options, args, null, false); // Should throw an exception
    }
}