
import org.apache.commons.cli.Option;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Properties;

public class ParserTest {

    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        options = new Options();

        // Add options to the Options object
        options.addOption(Option.builder("a").longOpt("add").argName("add").required(false).hasArg().desc("Add element").build());
        options.addOption(Option.builder("r").longOpt("remove").argName("remove").required(false).hasArg().desc("Remove element").build());
    }

    @Test
    public void testParse_AddOption() throws ParseException {
        String[] args = {"-a", "element"};

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertEquals("element", cmd.getOptionValue("a"));
    }

    @Test
    public void testParse_RemoveOption() throws ParseException {
        String[] args = {"-r", "element"};

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("r"));
        assertEquals("element", cmd.getOptionValue("r"));
    }

    @Test
    public void testParse_MultipleOptions() throws ParseException {
        String[] args = {"-a", "element1", "-r", "element2"};

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertEquals("element1", cmd.getOptionValue("a"));

        assertTrue(cmd.hasOption("r"));
        assertEquals("element2", cmd.getOptionValue("r"));
    }

    @Test
    public void testParse_NoOptions() throws ParseException {
        String[] args = {};

        CommandLine cmd = parser.parse(options, args);

        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("r"));
    }

    @Test(expected = ParseException.class)
    public void testParse_UnknownOption() throws ParseException {
        String[] args = {"-q", "element"};

        parser.parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testParse_MissingArgument() throws ParseException {
        String[] args = {"-a"};

        parser.parse(options, args);
    }

    @Test
    public void testParse_Properties() throws ParseException {
        String[] args = {};

        Properties properties = new Properties();
        properties.setProperty("a", "element");

        CommandLine cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("a"));
        assertEquals("element", cmd.getOptionValue("a"));
    }

    @Test
    public void testParse_StopAtNonOption() throws ParseException {
        String[] args = {"-a", "element", "nonOption", "-r", "nonOption"};

        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.hasOption("a"));
        assertEquals("element", cmd.getOptionValue("a"));

        assertTrue(cmd.getArgList().contains("nonOption"));
        assertFalse(cmd.hasOption("r"));
    }

    @Test
    public void testParse_StopAtNonOption_NoOptionsBeforeNonOption() throws ParseException {
        String[] args = {"nonOption", "-r", "element"};

        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.getArgList().contains("nonOption"));

        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("r"));
    }
}

