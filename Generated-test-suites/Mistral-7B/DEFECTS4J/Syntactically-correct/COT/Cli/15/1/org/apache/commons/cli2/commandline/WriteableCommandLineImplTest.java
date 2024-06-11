import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "opt1", false, "o1");
        writeableCommandLine.addOption(option);
        List options = writeableCommandLine.getOptions();
        Option optionFromList = (Option) options.get(0);
        assertEquals(option, optionFromList);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "opt1", false, "o1");
        writeableCommandLine.addOption(option);
        Option optionFromMap = writeableCommandLine.getOption("option1");
        assertEquals(option, optionFromMap);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("switchOption"), new ArrayList<>());
        Option option = new Option("switchOption", null, false, null);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "opt1", false, "o1");
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("switchOption"), new ArrayList<>());
        Option option = new Option("switchOption", null, false, null);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetSwitchWithDefaultValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("switchOption"), new ArrayList<>());
        Option option = new Option("switchOption", null, false, null);
        writeableCommandLine.addSwitch(option, false);
        Boolean switchValue = writeableCommandLine.getSwitch(option, true);
        assertFalse(switchValue);
    }

    @Test
    public void testGetValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1", true, true, "value1"), new ArrayList<>());
        writeableCommandLine.addValue(writeableCommandLine.getOption("option1"), "testValue");
        List values = writeableCommandLine.getValues(writeableCommandLine.getOption("option1"), null);
        assertEquals("testValue", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", true, true, "value1");
        writeableCommandLine.addOption(option);
        List values = writeableCommandLine.getUndefaultedValues(option);
        assertNull(values);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", true, true, "optionProperty");
        writeableCommandLine.addProperty(option, "propertyValue");
        Map properties = writeableCommandLine.getProperties(option);
        assertEquals("propertyValue", properties.get("propertyValue"));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", true, true, "optionProperty");
        writeableCommandLine.addProperty(option, "propertyValue");
        String propertyValue = writeableCommandLine.getProperty(option, "propertyNotFound");
        assertEquals("propertyValue", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", true, true, "optionProperty1");
        writeableCommandLine.addProperty(option, "propertyValue1");
        Option option2 = new Option("option1", true, true, "optionProperty2");
        writeableCommandLine.addProperty(option2, "propertyValue2");
        Set properties = writeableCommandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertTrue(properties.contains("optionProperty1"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        writeableCommandLine.addValue(writeableCommandLine.getOption("option1"), "value1");
        writeableCommandLine.addValue(writeableCommandLine.getOption("option1"), "value2");
        String commandLineString = writeableCommandLine.toString();
        String expectedCommandLineString = "value1 value2";
        assertEquals(expectedCommandLineString, commandLineString);
    }
}