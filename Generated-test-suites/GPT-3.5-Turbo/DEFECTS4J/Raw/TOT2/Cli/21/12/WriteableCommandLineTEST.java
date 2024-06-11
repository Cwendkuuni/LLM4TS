import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        Object value = "value";
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option).contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test");
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        commandLine.addValue(option, "value1");
        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        List defaultValues = new ArrayList();
        defaultValues.add("default");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        Option option = new Option("test");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("test");
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getPropertyValue(option, "property"));
    }

    @Test
    public void testAddPropertyToDefault() {
        commandLine.addProperty("defaultProperty", "defaultValue");
        assertEquals("defaultValue", commandLine.getDefaultPropertyValue("defaultProperty"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testSetAndGetCurrentOption() {
        Option option = new Option("test");
        commandLine.setCurrentOption(option);
        assertEquals(option, commandLine.getCurrentOption());
    }
}