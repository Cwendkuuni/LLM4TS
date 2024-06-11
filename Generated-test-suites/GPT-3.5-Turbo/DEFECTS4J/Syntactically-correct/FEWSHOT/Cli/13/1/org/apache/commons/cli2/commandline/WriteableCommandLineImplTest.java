package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("root");
        commandLine = new WriteableCommandLineImpl(rootOption, null);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("testOption"));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument("testArgument");
        String value = "testValue";

        commandLine.addValue(argument, value);

        assertTrue(commandLine.hasOption(argument));
        assertEquals(value, commandLine.getValues(argument, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testSwitch");
        boolean value = true;

        commandLine.addSwitch(option, value);

        assertTrue(commandLine.hasOption(option));
        assertEquals(value, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option("otherOption")));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("testOption"));
        assertNull(commandLine.getOption("otherOption"));
    }

    @Test
    public void testGetValues() {
        Argument argument = new Argument("testArgument");
        String value1 = "value1";
        String value2 = "value2";

        commandLine.addValue(argument, value1);
        commandLine.addValue(argument, value2);

        assertEquals(value1, commandLine.getValues(argument, null).get(0));
        assertEquals(value2, commandLine.getValues(argument, null).get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("testSwitch");
        boolean value = true;

        commandLine.addSwitch(option, value);

        assertEquals(value, commandLine.getSwitch(option, null));
        assertNull(commandLine.getSwitch(new Option("otherSwitch"), null));
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property, null));
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String value = "testValue";

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property, null));
        assertNull(commandLine.getProperty("otherProperty", null));
    }

    @Test
    public void testLooksLikeOption() {
        Option option1 = new Option("prefix1");
        Option option2 = new Option("prefix2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.looksLikeOption("prefix1Value"));
        assertTrue(commandLine.looksLikeOption("prefix2Value"));
        assertFalse(commandLine.looksLikeOption("otherValue"));
    }

    @Test
    public void testToString() {
        String argument1 = "arg1";
        String argument2 = "arg2";

        commandLine.addArgument(argument1);
        commandLine.addArgument(argument2);

        assertEquals(argument1 + " " + argument2, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("trigger1");
        Option option2 = new Option("trigger2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("trigger1"));
        assertTrue(commandLine.getOptionTriggers().contains("trigger2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        String defaultValue = "defaultValue";

        commandLine.setDefaultValues(option, null);

        assertNull(commandLine.getValues(option, null));

        commandLine.setDefaultValues(option, Collections.singletonList(defaultValue));

        assertEquals(defaultValue, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testSwitch");
        boolean defaultValue = true;

        commandLine.setDefaultSwitch(option, null);

        assertNull(commandLine.getSwitch(option, null));

        commandLine.setDefaultSwitch(option, defaultValue);

        assertEquals(defaultValue, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        String argument1 = "arg1";
        String argument2 = "arg2";

        commandLine.addArgument(argument1);
        commandLine.addArgument(argument2);

        assertEquals(2, commandLine.getNormalised().size());
        assertTrue(commandLine.getNormalised().contains(argument1));
        assertTrue(commandLine.getNormalised().contains(argument2));
    }
}