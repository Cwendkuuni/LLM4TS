import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "a flag option");
        options.addOption("v", true, "a value option");

        String[] args = new String[]{"-f", "-v", "value"};

        CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("f"));
        assertNotNull(commandLine.getOptionValue("v"));
        assertEquals("value", commandLine.getOptionValue("v"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "a flag option");
        options.addOption("v", true, "a value option");

        Properties properties = new Properties();
        properties.setProperty("v", "value");

        String[] args = new String[]{"-f"};

        CommandLine commandLine = parser.parse(options, args, properties);

        assertTrue(commandLine.hasOption("f"));
        assertNotNull(commandLine.getOptionValue("v"));
        assertEquals("value", commandLine.getOptionValue("v"));
    }

    @Test
    public void testParseRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("r")
                .required()
                .build());

        String[] args = new String[]{"-r", "value"};

        CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("r"));
        assertNotNull(commandLine.getOptionValue("r"));
        assertEquals("value", commandLine.getOptionValue("r"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("r")
                .required()
                .build());

        String[] args = new String[]{};

        parser.parse(options, args);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = Option.builder("a")
                .hasArgs()
                .build();

        ListIterator<String> iterator = List.of("arg1", "arg2").listIterator();

        parser.processArgs(option, iterator);

        assertTrue(option.hasArg());
        assertEquals(2, option.getValues().length);
        assertEquals("arg1", option.getValue(0));
        assertEquals("arg2", option.getValue(1));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Option option = Option.builder("a")
                .hasArgs()
                .build();

        ListIterator<String> iterator = List.of().listIterator();

        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("o")
                .hasArg()
                .build());

        ListIterator<String> iterator = List.of("-o", "value").listIterator();

        parser.processOption("-o", iterator);

        assertTrue(options.hasOption("o"));

        CommandLine commandLine = parser.getCmd();
        assertTrue(commandLine.hasOption("o"));
        assertEquals("value", commandLine.getOptionValue("o"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("o")
                .hasArg()
                .build());

        ListIterator<String> iterator = List.of("-o", "value").listIterator();

        parser.processOption("-a", iterator);
    }

    @Test
    public void testCheckRequiredOptions() throws MissingOptionException {
        Options options = new Options();
        Option requiredOption1 = Option.builder("r1").required().build();
        Option requiredOption2 = Option.builder("r2").required().build();

        options.addOption(requiredOption1);
        options.addOption(requiredOption2);

        List<Option> requiredOptions = options.getRequiredOptions();

        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);

        requiredOptions.clear();
        requiredOptions.add(requiredOption1);

        try {
            parser.checkRequiredOptions();
        } catch (MissingOptionException e) {
            assertEquals("r2", e.getMessage());
        }
    }
}