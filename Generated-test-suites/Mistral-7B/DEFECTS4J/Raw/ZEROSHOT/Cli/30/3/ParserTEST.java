import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    private Parser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOptionGroup(new OptionGroup("group", "Group") {{
            addOption("g", false, "Option G");
            addOption("h", false, "Option H");
        }});
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @After
    public void tearDown()
    {
        parser = null;
        options = null;
    }

    @Test
    public void testParse() throws ParseException
    {
        String[] arguments = {"-a", "--b=2", "-c=3", "--group.h"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals("Option A is set to true", true, cmd.hasOption("a"));
        assertEquals("Option B is set to 2", 2, cmd.getOptionValue("b", "default"));
        assertEquals("Option C is set to 3", 3, cmd.getOptionValue("c", 0).length());
        assertFalse("Option D is not set", cmd.hasOption("d"));
        assertFalse("Option E is not set", cmd.hasOption("e"));
        assertNull("Option F has no value", cmd.getOptionValue("f"));
        assertFalse("Option G is set to false", cmd.hasOption("g"));
        assertTrue("Option H is set to false", cmd.hasOption("group.h"));
        assertEquals("Size of args is 3", 3, cmd.getArgs().length);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException
    {
        String[] arguments = {"-a"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingArgument() throws ParseException
    {
        String[] arguments = {"--b"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException
    {
        String[] arguments = {"--i"};
        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        String[] arguments = {"--c"};
        parser.parse(options, arguments, null, true);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredGroupOption() throws ParseException
    {
        String[] arguments = {"--group.g"};
        parser.parse(options, arguments, null, true);
    }
}