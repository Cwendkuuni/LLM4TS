import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testSimpleParse() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "A help message");
        options.addOption("b", false, "B help message");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals(1, cmd.getOptionValue("a"));
        assertFalse(cmd.hasOption("c"));
        assertNull(cmd.getOptionValue("c"));
        assertEquals(2, cmd.getOptionValues("b").length);

        // test if an option with no value can be added
        Option noValueOption = options.addOption("noValue", false, "Help message for noValue option");
        parser = new DefaultParser();
        parser.setOptions(options);
        cmd = parser.parse(options, new String[0]);
        assertTrue(cmd.hasOption("noValue"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException
    {
        String[] args = new String[]{"-c", "1"};
        Options options = new Options();
        options.addOption("a", true, "A help message");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, args);
        fail("Expected ParseException");
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException
    {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "A help message");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, args);
        fail("Expected MissingArgumentException");
    }

    @Test
    public void testMultipleValues() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "-a", "3"};
        Options options = new Options();
        options.addOption("a", true, "A help message");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args);

        List<String> values = cmd.getOptionValues("a");
        assertEquals(2, values.size());
        assertEquals("1", values.get(0));
        assertEquals("3", values.get(1));
    }

    @Test
    public void testGroup() throws ParseException
    {
        String[] args = new String[]{"--mygroup", "-g", "1", "--mygroup", "-g", "2"};
        Options options = new Options();
        OptionGroup myGroup = new OptionGroup("mygroup", "My group");
        myGroup.setRequired(true);
        Option optionG = myGroup.addOption("g", true, "A help message for group g");

        options.addOptionGroup(myGroup);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOptionGroup(myGroup.getKey()));
        assertSame(myGroup, cmd.getOptionGroup(myGroup.getKey()));
        assertTrue(cmd.hasOption("g"));
        assertEquals(2, cmd.getOptionValues("g").length);
        assertEquals("1", cmd.getOptionValues("g")[0]);
        assertEquals("2", cmd.getOptionValues("g")[1]);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredGroup() throws ParseException
    {
        String[] args = new String[]{"-x"};
        Options options = new Options();
        OptionGroup myGroup = new OptionGroup("mygroup", "My group");
        myGroup.setRequired(true);
        options.addOptionGroup(myGroup);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, args);
        fail("Expected MissingOptionException");
    }
}