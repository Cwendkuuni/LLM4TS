import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new ParserImpl();
        options = new Options();
    }

    @Test
    public void testParse_noOptions() throws ParseException {
        String[] args = {};
        CommandLine cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().length == 0);
    }

    @Test
    public void testParse_singleOption() throws ParseException {
        String[] args = {"-o"};
        options.addOption("o", false, "option o");
        CommandLine cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("o"));
    }

    @Test
    public void testParse_multipleOptions() throws ParseException {
        String[] args = {"-o", "-a", "-b"};
        options.addOption("o", false, "option o");
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        CommandLine cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("o"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParse_argumentValue() throws ParseException {
        String[] args = {"-o", "value"};
        options.addOption("o", true, "option o with argument");
        CommandLine cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_missingArgumentValue() throws ParseException {
        String[] args = {"-o"};
        options.addOption("o", true, "option o with argument");
        CommandLine cmd = parser.parse(options, args);
    }

    @Test
    public void testParse_optionalArgumentValue() throws ParseException {
        String[] args = {"-o", "value"};
        options.addOption(Option.builder("o")
            .hasArg()
            .optionalArg(true)
            .desc("option o with optional argument")
            .build());
        CommandLine cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o"));
    }

    @Test
    public void testParse_optionalArgumentValue_missingValue() throws ParseException {
        String[] args = {"-o"};
        options.addOption(Option.builder("o")
            .hasArg()
            .optionalArg(true)
            .desc("option o with optional argument")
            .build());
        CommandLine cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("o"));
        assertEquals(null, cmd.getOptionValue("o"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_unrecognizedOption() throws ParseException {
        String[] args = {"-x"};
        CommandLine cmd = parser.parse(options, args);
    }

    @Test
    public void testParse_stopAtNonOption() throws ParseException {
        String[] args = {"-o", "value", "nonOption"};
        options.addOption("o", true, "option o with argument");
        CommandLine cmd = parser.parse(options, args, true);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o"));
        assertTrue(cmd.getArgs().length == 1);
        assertEquals("nonOption", cmd.getArgs()[0]);
    }

    @Test
    public void testParse_properties() throws ParseException {
        String[] args = {"-o", "value"};
        options.addOption("o", true, "option o with argument");
        Properties properties = new Properties();
        properties.setProperty("p", "property");
        CommandLine cmd = parser.parse(options, args, properties);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o"));
        assertTrue(cmd.hasOption("p"));
        assertEquals("property", cmd.getOptionValue("p"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_requiredOption_missing() throws ParseException {
        String[] args = {};
        options.addOption(Option.builder("o")
            .required(true)
            .build());
        CommandLine cmd = parser.parse(options, args);
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_requiredOption_missingMultiple() throws ParseException {
        String[] args = {};
        options.addOption(Option.builder("o")
            .required(true)
            .build());
        options.addOption(Option.builder("a")
            .required(true)
            .build());
        CommandLine cmd = parser.parse(options, args);
    }

    @Test
    public void testParse_optionGroup() throws ParseException {
        String[] args = {"-o"};
        options.addOptionGroup(OptionGroup.builder()
            .addOption(Option.builder("o").build())
            .addOption(Option.builder("a").build())
            .build());
        CommandLine cmd = parser.parse(options, args);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("o"));
        assertFalse(cmd.hasOption("a"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_optionGroupWithArgument_missing() throws ParseException {
        String[] args = {"-o"};
        options.addOptionGroup(OptionGroup.builder()
            .addOption(Option.builder("o").build())
            .addOption(Option.builder("a")
                .hasArg()
                .build())
            .build());
        CommandLine cmd = parser.parse(options, args);
    }

}