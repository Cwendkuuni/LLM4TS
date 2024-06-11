import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        parser = new DefaultParser();
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments, true);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithBasicOptions() throws ParseException {
        options.addOption("o", "option", true, "option description");
        String[] arguments = new String[]{"-o", "value"};
        CommandLine cmd = parser.parse(options, arguments, true);
        assertNotNull(cmd);
        assertEquals("value", cmd.getOptionValue("option"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        options.addOption("o1", "option1", true, "option1 description");
        options.addOption("o2", "option2", true, "option2 description");
        String[] arguments = new String[]{"-o1", "value1", "-o2", "value2"};
        CommandLine cmd = parser.parse(options, arguments, true);
        assertNotNull(cmd);
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertEquals("value2", cmd.getOptionValue("option2"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        options.addOption("r", "requiredOption", true, "requiredOption description");
        options.addRequiredOption("r");
        String[] arguments = new String[0];
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments, true));
        arguments = new String[]{"-r"};
        CommandLine cmd = parser.parse(options, arguments, true);
        assertNotNull(cmd);
        assertNotNull(cmd.getOptionValue("requiredOption"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        options.addOptions(Arrays.asList(
                new Option("p", "property", true, "property description"),
                new Option("p", "property2", true, "property2 description")
        ));
        Properties properties = new Properties();
        properties.setProperty("property", "value1");
        properties.setProperty("property2", "value2");
        CommandLine cmd = parser.parse(options, new String[0], properties, true);
        assertNotNull(cmd);
        assertEquals("value1", cmd.getOptionValue("property"));
        assertEquals("value2", cmd.getOptionValue("property2"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        options.addOption("s", "stopAtNonOption", false, "stopAtNonOption description");
        String[] arguments = new String[]{"-s", "value", "nonOption"};
        CommandLine cmd = parser.parse(options, arguments, true);
        assertNotNull(cmd);
        assertEquals("value", cmd.getOptionValue("stopAtNonOption"));
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals("nonOption", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        OptionGroup group = new OptionGroup("group", "group description");
        group.addOption(new Option("g1", "optionGroup1", true, "optionGroup1 description"));
        group.addOption(new Option("g2", "optionGroup2", true, "optionGroup2 description"));
        options.addOptionGroup(group);
        options.addRequiredOption("g1");
        String[] arguments = new String[0];
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments, true));
        arguments = new String[]{"-g1"};
        CommandLine cmd = parser.parse(options, arguments, true);
        assertNotNull(cmd);
        assertEquals(group, cmd.getOptionGroup());
        assertNull(cmd.getOptionValue("g2"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        options.addOption("i", "invalidOption", true, "invalidOption description");
        String[] arguments = new String[]{"-i", "value"};
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments, true));
    }
}