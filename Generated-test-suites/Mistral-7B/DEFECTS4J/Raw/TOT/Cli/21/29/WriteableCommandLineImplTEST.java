package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLine commandLine;
    private Option option1;
    private Option option2;

    @Before
    public void setup() {
        option1 = new Option("o", "option1")
                .required(false)
                .longOpt("long-option1")
                .valueSeparator('=')
                .valuesSeparator(',')
                .desc("Option description for option1");

        option2 = new Option("s", "option2")
                .required(false)
                .longOpt("long-option2")
                .desc("Option description for option2");

        commandLine = new WriteableCommandLineImpl(new Option("test", "Test"), Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
    }

    @Test
    public void testAddOption() {
        // Test adding an option with no values
        commandLine.addOption(option1);
        assertTrue("Option 'option1' should be present after adding it", commandLine.hasOption(option1));
        assertEquals("The number of options should be 2", 2, commandLine.getOptions().size());

        // Test adding an option with a value
        commandLine.addValue(option1, "value1");
        assertEquals("The value of the option should be 'value1'", "value1", commandLine.getValues(option1, null).get(0));

        // Test adding an option with multiple values
        commandLine.addValue(option1, "value2");
        commandLine.addValue(option1, "value3");
        List values = commandLine.getValues(option1, null);
        assertEquals("The number of values for the option should be 3", 3, values.size());
        assertEquals("The first value of the option should be 'value1'", "value1", values.get(0));
        assertEquals("The second value of the option should be 'value2'", "value2", values.get(1));
        assertEquals("The third value of the option should be 'value3'", "value3", values.get(2));

        // Test adding a switch option
        commandLine.addSwitch(option1, true);
        assertTrue("The switch for option 'option1' should be set to true", commandLine.getSwitch(option1, false));

        // Test adding the same option twice
        commandLine.addOption(option1);
        assertEquals("The number of options should still be 2", 2, commandLine.getOptions().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        // Test adding the same option twice
        commandLine.addOption(option1);
        commandLine.addOption(option1);
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption("o");
        assertEquals("The option retrieved by the short name should be 'option1'", option1, option);
        option = commandLine.getOption("long-option1");
        assertEquals("The option retrieved by the long name should be 'option1'", option1, option);
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        List values = commandLine.getValues(option1, null);
        assertEquals("The number of values for the option should be 2", 2, values.size());
        assertEquals("The first value of the option should be 'value1'", "value1", values.get(0));
        assertEquals("The second value of the option should be 'value2'", "value2", values.get(1));

        // Test getting values for an option without any values
        values = commandLine.getValues(option2, null);
        assertTrue("The list of values for an option without values should be empty", values.isEmpty());

        // Test getting values for an option with a single value
        commandLine.addValue(option2, "value3");
        values = commandLine.getValues(option2, null);
        assertEquals("The number of values for the option should be 1", 1, values.size());
        assertEquals("The value of the option should be 'value3'", "value3", values.get(0));

        // Test getting values for an option with multiple values
        commandLine.addValue(option2, "value4");
        commandLine.addValue(option2, "value5");
        values = commandLine.getValues(option2, null);
        assertEquals("The number of values for the option should be 3", 3, values.size());
        assertEquals("The first value of the option should be 'value3'", "value3", values.get(0));
        assertEquals("The second value of the option should be 'value4'", "value4", values.get(1));
        assertEquals("The third value of the option should be 'value5'", "value5", values.get(2));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        List values = commandLine.getUndefaultedValues(option1);
        assertEquals("The number of values for the option should be 2", 2, values.size());
        assertEquals("The first value of the option should be 'value1'", "value1", values.get(0));
        assertEquals("The second value of the option should be 'value2'", "value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option1, false);
        assertFalse("The switch for option 'option1' should be false by default", commandLine.getSwitch(option1, true));
        commandLine.addSwitch(option1, true);
        assertTrue("The switch for option 'option1' should be true after setting it", commandLine.getSwitch(option1, false));

        // Test getting a switch for an option that doesn't have one
        assertEquals("The switch for an option without a switch should be null", null, commandLine.getSwitch(option2, false));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(option1, "property1", "value1");
        assertEquals("The property value for option 'option1' should be 'value1'", "value1", commandLine.getProperty(option1, "property1"));
        assertEquals("The property value for option 'option1' should be null for a non-existent property", null, commandLine.getProperty(option1, "non-existent-property"));
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty(option1, "property1", "value1");
        commandLine.addProperty(option1, "property2", "value2");
        Set properties = commandLine.getProperties(option1);
        assertEquals("The number of properties for the option should be 2", 2, properties.size());
        assertTrue("The property 'property1' should be present", properties.contains("property1"));
        assertTrue("The property 'property2' should be present", properties.contains("property2"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.addValue(option2, "value3");
        String result = commandLine.toString();
        String expected = "Test [--option1 VALUE1, --option1 VALUE2, --option2 VALUE3]";
        assertEquals("The command line string should match the expected string", expected, result);
    }

    @Test
    public void testHasOption() {
        // Test having an option
        commandLine.addOption(option1);
        assertTrue("Option 'option1' should be present after adding it", commandLine.hasOption(option1));

        // Test not having an option
        Option nonExistentOption = new Option("non-existent-option");
        assertFalse("Option 'non-existent-option' should not be present", commandLine.hasOption(nonExistentOption));
    }
}