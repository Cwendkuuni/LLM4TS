import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new ParserImpl();
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "This is an example option");
        options.addOption("b", true, "This is another example option");

        String[] arguments = {"-f", "value1", "-b", "value2"};

        CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertEquals("value1", cmd.getOptionValue("f"));
        assertEquals("value2", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "This is an example option");

        String[] arguments = {"-x"};

        parser.parse(options, arguments);
    }

    @Test
    public void testParseRequiredOption() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("r", true, "This is a required option");

        String[] arguments = {"-r", "value"};

        CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertEquals("value", cmd.getOptionValue("r"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("r", true, "This is a required option");

        String[] arguments = {};

        parser.parse(options, arguments);
    }

    @Test
    public void testParseStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "This is an example option");

        String[] arguments = {"-f", "-v", "value"};

        CommandLine cmd = parser.parse(options, arguments, true);

        assertNotNull(cmd);
        assertEquals(1, cmd.getArgs().length);
        assertEquals("-v", cmd.getArgs()[0]);
    }

    @Test
    public void testParseProperties() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "This is an example option");
        options.addOption("b", true, "This is another example option");

        Properties properties = new Properties();
        properties.setProperty("f", "true");
        properties.setProperty("b", "value");

        String[] arguments = {"-x"};

        CommandLine cmd = parser.parse(options, arguments, properties);

        assertNotNull(cmd);
        assertEquals("true", cmd.getOptionValue("f"));
        assertEquals("value", cmd.getOptionValue("b"));
    }
}