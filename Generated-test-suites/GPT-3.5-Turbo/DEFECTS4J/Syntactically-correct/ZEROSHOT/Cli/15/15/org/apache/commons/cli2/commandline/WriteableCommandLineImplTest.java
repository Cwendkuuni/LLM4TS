import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("root");
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        Object value = "value";
        commandLine.addValue(option, value);
        List valueList = commandLine.getValues(option, null);
        assertEquals(1, valueList.size());
        assertEquals(value, valueList.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(value, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test");
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test");
        Option returnedOption = commandLine.getOption("test");
        assertNull(returnedOption);
        commandLine.addOption(option);
        returnedOption = commandLine.getOption("test");
        assertSame(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test");
        List defaultValues = new ArrayList();
        defaultValues.add("default");
        List returnedValueList = commandLine.getValues(option, defaultValues);
        assertEquals(defaultValues, returnedValueList);
        Object value = "value";
        commandLine.addValue(option, value);
        returnedValueList = commandLine.getValues(option, defaultValues);
        assertEquals(1, returnedValueList.size());
        assertEquals(value, returnedValueList.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test");
        Object value = "value";
        commandLine.addValue(option, value);
        List returnedValueList = commandLine.getUndefaultedValues(option);
        assertEquals(1, returnedValueList.size());
        assertEquals(value, returnedValueList.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test");
        Boolean defaultValue = true;
        Boolean returnedSwitchValue = commandLine.getSwitch(option, defaultValue);
        assertEquals(defaultValue, returnedSwitchValue);
        boolean value = false;
        commandLine.addSwitch(option, value);
        returnedSwitchValue = commandLine.getSwitch(option, defaultValue);
        assertEquals(value, returnedSwitchValue);
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String defaultValue = "default";
        String returnedPropertyValue = commandLine.getProperty(property, defaultValue);
        assertEquals(defaultValue, returnedPropertyValue);
        Option option = new PropertyOption();
        commandLine.addProperty(option, property, "value");
        returnedPropertyValue = commandLine.getProperty(option, property, defaultValue);
        assertEquals("value", returnedPropertyValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new PropertyOption();
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        String returnedPropertyValue = commandLine.getProperty(option, property, null);
        assertEquals(value, returnedPropertyValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new PropertyOption();
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        List returnedProperties = new ArrayList(commandLine.getProperties(option));
        assertEquals(2, returnedProperties.size());
        assertTrue(returnedProperties.contains("property1"));
        assertTrue(returnedProperties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("test"));
        Option option = new Option("test");
        commandLine.addOption(option);
        assertTrue(commandLine.looksLikeOption("test"));
    }

    @Test
    public void testToString() {
        Option option = new Option("test");
        Object value = "value";
        commandLine.addValue(option, value);
        String returnedString = commandLine.toString();
        assertEquals("value", returnedString);
    }

    @Test
    public void testGetOptions() {
        List returnedOptions = commandLine.getOptions();
        assertTrue(returnedOptions.isEmpty());
        Option option = new Option("test");
        commandLine.addOption(option);
        returnedOptions = commandLine.getOptions();
        assertEquals(1, returnedOptions.size());
        assertSame(option, returnedOptions.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        Set returnedOptionTriggers = commandLine.getOptionTriggers();
        assertTrue(returnedOptionTriggers.isEmpty());
        Option option = new Option("test");
        commandLine.addOption(option);
        returnedOptionTriggers = commandLine.getOptionTriggers();
        assertEquals(1, returnedOptionTriggers.size());
        assertTrue(returnedOptionTriggers.contains("test"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        List defaultValues = new ArrayList();
        defaultValues.add("default");
        commandLine.setDefaultValues(option, defaultValues);
        List returnedDefaultValues = new ArrayList(commandLine.getValues(option, null));
        assertEquals(defaultValues, returnedDefaultValues);
        commandLine.setDefaultValues(option, null);
        assertEquals(0, commandLine.getValues(option, null).size());
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean returnedDefaultSwitch = commandLine.getSwitch(option, null);
        assertEquals(defaultSwitch, returnedDefaultSwitch);
        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        List returnedNormalised = commandLine.getNormalised();
        assertTrue(returnedNormalised.isEmpty());
        Option option = new Option("test");
        Object value = "value";
        commandLine.addValue(option, value);
        returnedNormalised = commandLine.getNormalised();
        assertEquals(1, returnedNormalised.size());
        assertEquals(value, returnedNormalised.get(0));
    }
}