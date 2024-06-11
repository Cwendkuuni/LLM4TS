
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl writeableCommandLine;

    @Before
    public void setUp() {
        Option option = new Argument();
        List arguments = new ArrayList<>();
        arguments.add("argument1");

        writeableCommandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @After
    public void tearDown() {
        writeableCommandLine = null;
    }

    @Test
    public void testAddOption() {
        Option option = new Option("opt1", "option1", true, "Option 1");

        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();
        Object value = "value1";

        writeableCommandLine.addValue(option, value);

        List<Object> values = writeableCommandLine.getValues(option, null);
        assertTrue(values.contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("opt1", "option1", false, "Option 1");
        boolean value = true;

        writeableCommandLine.addSwitch(option, value);

        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("opt1", "option1", true, "Option 1");

        assertFalse(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        String trigger = "opt1";
        Option option = new Option(trigger, "option1", true, "Option 1");

        writeableCommandLine.addOption(option);

        Option returnedOption = writeableCommandLine.getOption(trigger);
        assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Argument();
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");

        List<Object> argumentValues = new ArrayList<>();
        argumentValues.add("value1");

        writeableCommandLine.setDefaultValue(option, defaultValues);
        writeableCommandLine.addValue(option, argumentValues.get(0));

        List<Object> values = writeableCommandLine.getValues(option, null);
        assertTrue(values.containsAll(argumentValues) && values.containsAll(defaultValues));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument();
        Object value = "value1";

        writeableCommandLine.addValue(option, value);

        List<Object> values = writeableCommandLine.getUndefaultedValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("opt1", "option1", false, "Option 1");
        boolean defaultValue = true;

        writeableCommandLine.setDefaultSwitch(option, defaultValue);

        boolean value = writeableCommandLine.getSwitch(option, false);
        assertEquals(defaultValue, value);
    }

    @Test
    public void testGetProperty() {
        String property = "property1";
        String value = "value1";

        writeableCommandLine.addProperty(property, value);

        String returnedValue = writeableCommandLine.getProperty(property, null);
        assertEquals(value, returnedValue);
    }

    @Test
    public void testAddProperty() {
        String property = "property1";
        String value = "value1";

        writeableCommandLine.addProperty(property, value);

        String returnedValue = writeableCommandLine.getProperty(property, null);
        assertEquals(value, returnedValue);
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        String property = "property1";
        String defaultValue = "defaultValue";

        String returnedValue = writeableCommandLine.getProperty(property, defaultValue);
        assertEquals(defaultValue, returnedValue);
    }

    @Test
    public void testAddPropertyForOption() {
        Option option = new Option("opt1", "option1", true, "Option 1");
        String property = "property1";
        String value = "value1";

        writeableCommandLine.addProperty(option, property, value);

        String returnedValue = writeableCommandLine.getProperty(option, property, null);
        assertEquals(value, returnedValue);
    }

    @Test
    public void testGetPropertiesForOption() {
        Option option = new Option("opt1", "option1", true, "Option 1");
        String property = "property1";
        String value = "value1";

        writeableCommandLine.addProperty(option, property, value);

        assertTrue(writeableCommandLine.getProperties(option).contains(property));
    }

    @Test
    public void testGetProperties() {
        String property = "property1";
        String value = "value1";

        writeableCommandLine.addProperty(property, value);

        assertTrue(writeableCommandLine.getProperties().contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "opt1";
        assertTrue(writeableCommandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        assertEquals("argument1", writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("opt1", "option1", true, "Option 1");

        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        String trigger = "opt1";
        Option option = new Option(trigger, "option1", true, "Option 1");

        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.getOptionTriggers().contains(trigger));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Argument();
        List<Object> values = new ArrayList<>();
        values.add("default1");

        writeableCommandLine.setDefaultValue(option, values);

        List<Object> defaultValues = writeableCommandLine.getDefaultValues(option, null);
        assertTrue(defaultValues.containsAll(values));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("opt1", "option1", false, "Option 1");
        boolean defaultValue = false;

        writeableCommandLine.setDefaultSwitch(option, defaultValue);

        boolean value = writeableCommandLine.getDefaultSwitch(option, true);
        assertEquals(defaultValue, value);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Argument();
        List arguments = new ArrayList<>();
        arguments.add("argument1");

        writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        List argumentList = writeableCommandLine.getNormalised();
        assertTrue(argumentList.containsAll(arguments));
    }
}

