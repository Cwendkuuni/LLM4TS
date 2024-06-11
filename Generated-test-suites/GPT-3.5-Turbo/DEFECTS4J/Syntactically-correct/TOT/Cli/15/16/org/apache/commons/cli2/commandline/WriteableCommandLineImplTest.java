import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new OptionBuilder().withLongName("root").create();
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withLongName("test").create();
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        Option option = new ArgumentBuilder().withMaximum(2).create();
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(value1);
        expectedValues.add(value2);

        List<String> actualValues = commandLine.getValues(option, null);
        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withLongName("switch").create();
        boolean switchValue = true;
        commandLine.addSwitch(option, switchValue);

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_AlreadySet() {
        Option option = new OptionBuilder().withLongName("switch").create();
        boolean switchValue1 = true;
        boolean switchValue2 = false;
        commandLine.addSwitch(option, switchValue1);
        commandLine.addSwitch(option, switchValue2); // should throw exception
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withLongName("test").create();
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetValue() {
        Option option = new ArgumentBuilder().withMaximum(2).create();
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(value1);
        expectedValues.add(value2);

        List<String> actualValues = commandLine.getValues(option, null);
        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new ArgumentBuilder().withMaximum(2).create();
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(value1);
        expectedValues.add(value2);

        List<String> actualValues = commandLine.getUndefaultedValues(option);
        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withLongName("switch").create();
        boolean switchValue = true;
        commandLine.addSwitch(option, switchValue);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetSwitch_DefaultValue() {
        Option option = new OptionBuilder().withLongName("switch").create();
        boolean defaultValue = false;

        assertEquals(defaultValue, commandLine.getSwitch(option, defaultValue));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        Option option = new OptionBuilder().withLongName("option").create();
        commandLine.addProperty(option, property, value);

        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetProperty_DefaultValue() {
        String property = "property";
        String defaultValue = "default";

        assertEquals(defaultValue, commandLine.getProperty(property, defaultValue));
    }

    @Test
    public void testGetProperties() {
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        Option option = new OptionBuilder().withLongName("option").create();
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);

        assertTrue(commandLine.getProperties(option).contains(property1));
        assertTrue(commandLine.getProperties(option).contains(property2));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-t";
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        List<String> arguments = new ArrayList<>();
        arguments.add(arg1);
        arguments.add(arg2);
        commandLine = new WriteableCommandLineImpl(new OptionBuilder().withLongName("root").create(), arguments);

        String expectedToString = arg1 + " " + arg2;
        assertEquals(expectedToString, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder().withLongName("option1").create();
        Option option2 = new OptionBuilder().withLongName("option2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        String trigger1 = "trigger1";
        String trigger2 = "trigger2";
        Option option1 = new OptionBuilder().withLongName("option1").withTrigger(trigger1).create();
        Option option2 = new OptionBuilder().withLongName("option2").withTrigger(trigger2).create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.getOptionTriggers().contains(trigger1));
        assertTrue(commandLine.getOptionTriggers().contains(trigger2));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withLongName("option").create();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultValues_Null() {
        Option option = new OptionBuilder().withLongName("option").create();
        List<String> defaultValues = null;
        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withLongName("switch").create();
        boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertEquals(defaultSwitch, commandLine.getSwitch(option, false));
    }

    @Test
    public void testSetDefaultSwitch_Null() {
        Option option = new OptionBuilder().withLongName("switch").create();
        Boolean defaultSwitch = null;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        List<String> arguments = new ArrayList<>();
        arguments.add(arg1);
        arguments.add(arg2);
        commandLine = new WriteableCommandLineImpl(new OptionBuilder().withLongName("root").create(), arguments);

        assertTrue(commandLine.getNormalised().contains(arg1));
        assertTrue(commandLine.getNormalised().contains(arg2));
    }
}