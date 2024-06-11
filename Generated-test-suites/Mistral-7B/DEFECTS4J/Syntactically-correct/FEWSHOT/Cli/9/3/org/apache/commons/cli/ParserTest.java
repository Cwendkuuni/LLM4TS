package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        options = new Options();
        parser = new DefaultParser();
    }

    @Test
    public void testParseNoArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseSingleArgument() throws ParseException {
        String[] arguments = {"arg1"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().size() == 1);
        assertEquals("arg1", cmd.getArgs().get(0));
    }

    @Test
    public void testParseMultipleArguments() throws ParseException {
        String[] arguments = {"arg1", "arg2", "arg3"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().size() == 3);
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
        assertEquals("arg3", cmd.getArgs().get(2));
    }

    @Test
    public void testParseWithSingleValueOption() throws ParseException {
        Option option = new Option("o", "option", true, "defaultValue");
        options.addOption(option);
        String[] arguments = {"-o"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.getOptions().size() == 1);
        Option optionResult = cmd.getOptionNamed("option");
        assertNotNull(optionResult);
        assertTrue(optionResult.getValue().equals("defaultValue"));
    }

    @Test
    public void testParseWithMultiValueOption() throws ParseException {
        Option option = new Option("o", "option", true, null);
        options.addOption(option);
        String[] arguments = {"-o", "value1", "-o", "value2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.getOptions().size() == 1);
        Option optionResult = cmd.getOptionNamed("option");
        assertNotNull(optionResult);
        List<String> values = optionResult.getValues();
        assertTrue(values.size() == 2);
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingValueForOptionalOption() throws ParseException {
        Option option = new Option("o", "option", true, null);
        options.addOption(option);
        String[] arguments = {"-o"};
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        String[] arguments = {"-x"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidBooleanOption() throws ParseException {
        Option option = new Option("o", "option", false, "true");
        options.addOption(option);
        String[] arguments = {"-o", "false"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Option option = new Option("o", "option", true, null);
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("option", "value");
        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertNotNull(cmd);
        assertTrue(cmd.getOptions().size() == 1);
        Option optionResult = cmd.getOptionNamed("option");
        assertNotNull(optionResult);
        assertTrue(optionResult.getValue().equals("value"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Option option1 = new Option("o1", "option1", true, null);
        Option option2 = new Option("o2", "option2", true, null);
        options.addOptions(Arrays.asList(option1, option2));
        String[] arguments = new String[0];
        try {
            parser.parse(options, arguments);
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            List<Option> requiredOptions = options.getOptions();
            assertTrue(requiredOptions.size() == 2);
            assertTrue(requiredOptions.get(0).isRequired());
            assertTrue(requiredOptions.get(1).isRequired());
        }
    }

    @Test
    public void testParseWithOptionalGroup() throws ParseException {
        OptionGroup group = new OptionGroup("g", "group");
        group.setRequired(false);
        Option option1 = new Option("o1", "option1", true, null);
        Option option2 = new Option("o2", "option2", true, null);
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        String[] arguments = {"--g"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getOptions().size() == 2);
        Option option1Result = cmd.getOptionNamed("option1");
        Option option2Result = cmd.getOptionNamed("option2");
        assertTrue(option1Result != null);
        assertTrue(option2Result != null);
        assertFalse(group.isSelected());

        String[] arguments2 = {"--g", "-o1"};
        CommandLine cmd2 = parser.parse(options, arguments2);
        assertTrue(cmd2.getOptions().size() == 1);
        Option option1Result2 = cmd2.getOptionNamed("option1");
        assertTrue(option1Result2 != null);
        assertTrue(group.isSelected());
    }

    @Test
    public void testParseWithRequiredGroup() throws ParseException {
        OptionGroup group = new OptionGroup("g", "group");
        group.setRequired(true);
        Option option1 = new Option("o1", "option1", true, null);
        Option option2 = new Option("o2", "option2", true, null);
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        String[] arguments = new String[0];
        try {
            parser.parse(options, arguments);
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            List<Option> requiredOptions = options.getOptions();
            assertTrue(requiredOptions.size() == 2);
            assertTrue(requiredOptions.get(0).isRequired());
            assertTrue(requiredOptions.get(1).isRequired());
            List<OptionGroup> requiredGroups = options.getOptionGroups();
            assertTrue(requiredGroups.size() == 1);
            assertTrue(requiredGroups.get(0).isSelected());
        }
    }

    private void assertEquals(String expected, Option option) {
        assertEquals(expected, option.getLongOpt());
        assertEquals(expected, option.getKey());
    }

    private void assertEquals(String expected, String actual) {
        assertEquals(expected, actual);
    }
}