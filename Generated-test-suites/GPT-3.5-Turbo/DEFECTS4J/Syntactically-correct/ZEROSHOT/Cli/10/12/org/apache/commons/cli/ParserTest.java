package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        options = new Options();
    }

    @Test
    public void testParse_DefaultOptions() throws ParseException {
        String[] arguments = {};

        CommandLine result = parser.parse(options, arguments);

        assertNotNull(result);
        assertTrue(result.getOptions().isEmpty());
    }

    @Test
    public void testParse_ArgumentWithoutOption() throws ParseException {
        String[] arguments = {"argument"};

        CommandLine result = parser.parse(options, arguments);

        assertNotNull(result);
        assertTrue(result.getOptions().isEmpty());
        assertArrayEquals(new String[]{"argument"}, result.getArgs());
    }

    @Test
    public void testParse_ArgumentWithOption() throws ParseException {
        String[] arguments = {"-o", "optionValue", "argument"};

        options.addOption("o", false, "Option");

        CommandLine result = parser.parse(options, arguments);

        assertNotNull(result);
        assertTrue(result.hasOption("o"));
        assertEquals("optionValue", result.getOptionValue("o"));
        assertArrayEquals(new String[]{"argument"}, result.getArgs());
    }

    @Test
    public void testParse_ArgumentWithRequiredOption() throws ParseException {
        String[] arguments = {"-o", "optionValue", "argument"};

        Option option = new Option("o", false, "Required Option");
        option.setRequired(true);
        options.addOption(option);

        CommandLine result = parser.parse(options, arguments);

        assertNotNull(result);
        assertTrue(result.hasOption("o"));
        assertEquals("optionValue", result.getOptionValue("o"));
        assertArrayEquals(new String[]{"argument"}, result.getArgs());
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_RequiredOptionMissing() throws ParseException {
        String[] arguments = {"argument"};

        Option option = new Option("o", false, "Required Option");
        option.setRequired(true);
        options.addOption(option);

        parser.parse(options, arguments);
    }

    @Test
    public void testParse_ArgumentWithAlternativeOption() throws ParseException {
        String[] arguments = {"-o", "optionValue", "argument"};

        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o", false, "Option 1");
        Option option2 = new Option("a", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        CommandLine result = parser.parse(options, arguments);

        assertNotNull(result);
        assertTrue(result.hasOption("o"));
        assertFalse(result.hasOption("a"));
        assertEquals("optionValue", result.getOptionValue("o"));
        assertArrayEquals(new String[]{"argument"}, result.getArgs());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption() throws ParseException {
        String[] arguments = {"-x", "argument"};

        parser.parse(options, arguments);
    }

    @Test
    public void testParse_Properties() throws ParseException {
        String[] arguments = {"argument"};

        Option option = new Option("o", true, "Option");
        options.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("o", "optionValue");

        CommandLine result = parser.parse(options, arguments, properties);

        assertNotNull(result);
        assertTrue(result.hasOption("o"));
        assertEquals("optionValue", result.getOptionValue("o"));
        assertArrayEquals(new String[]{"argument"}, result.getArgs());
    }

    @Test
    public void testParse_StopAtNonOption() throws ParseException {
        String[] arguments = {"-o", "optionValue", "argument"};

        Option option = new Option("o", true, "Option");
        options.addOption(option);

        CommandLine result = parser.parse(options, arguments, true);

        assertNotNull(result);
        assertTrue(result.hasOption("o"));
        assertEquals("optionValue", result.getOptionValue("o"));
        assertArrayEquals(new String[]{"argument"}, result.getArgs());
    }

    @Test
    public void testParse_StopAtNonOption_NonOptionArgument() throws ParseException {
        String[] arguments = {"-o", "optionValue", "--", "nonOptionArgument"};

        Option option = new Option("o", true, "Option");
        options.addOption(option);

        CommandLine result = parser.parse(options, arguments, true);

        assertNotNull(result);
        assertTrue(result.hasOption("o"));
        assertEquals("optionValue", result.getOptionValue("o"));
        assertArrayEquals(new String[]{"--", "nonOptionArgument"}, result.getArgs());
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_ArgumentMissing() throws ParseException {
        String[] arguments = {"-o"};

        Option option = new Option("o", true, "Option");
        options.addOption(option);

        parser.parse(options, arguments);
    }

    @Test
    public void testParse_ArgumentWithOptionalOption() throws ParseException {
        String[] arguments = {"-o", "optionValue"};

        Option option = new Option("o", true, "Optional Option");
        options.addOption(option);

        CommandLine result = parser.parse(options, arguments);

        assertNotNull(result);
        assertTrue(result.hasOption("o"));
        assertEquals("optionValue", result.getOptionValue("o"));
        assertArrayEquals(new String[0], result.getArgs());
    }
}