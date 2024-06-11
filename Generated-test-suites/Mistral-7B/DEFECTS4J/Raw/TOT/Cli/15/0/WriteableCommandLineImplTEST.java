import org.apache.commons.cli2.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl(new Option("t", "test", true, "test value"), Arrays.asList("testArg"));
        option = new Option("o", "output", true, "output value");
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "value1");
        assertEquals(1, commandLine.getValues(option, null).size());
        assertEquals("value1", commandLine.getValues(option, null).get(0));

        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getValues(option, null).size());
        assertEquals("value1", commandLine.getValues(option, null).get(0));
        assertEquals("value2", commandLine.getValues(option, null).get(1));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        assertSame(option, commandLine.getOption("o"));
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        values = commandLine.getValues(option, null);
        assertEquals(4, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("defaultValue1", values.get(2));
        assertEquals("defaultValue2", values.get(3));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option, "value1");
        List unfaultedValues = commandLine.getUndefaultedValues(option);
        assertEquals(1, unfaultedValues.size());
        assertEquals("value1", unfaultedValues.get(0));

        commandLine.addValue(option, "value2");
        unfaultedValues = commandLine.getUndefaultedValues(option);
        assertEquals(1, unfaultedValues.size());
        assertEquals("value2", unfaultedValues.get(0));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(option, "property", "propertyValue");
        assertEquals("propertyValue", commandLine.getProperty(option, "property"));
        assertEquals("propertyValue", commandLine.getProperty(option, "property", "defaultValue"));
    }

    @Test
    public void testAddProperty() {
        Map<String, String> properties = new HashMap<>();
        commandLine = new WriteableCommandLineImpl(new Option(), Arrays.asList());
        commandLine.addProperty(option, "property1", "propertyValue1");
        commandLine.addProperty(option, "property2", "propertyValue2");
        properties = (Map) commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertEquals("propertyValue1", properties.get("property1"));
        assertEquals("propertyValue2", properties.get("property2"));

        commandLine.setDefaultValues(option, null);
        properties = (Map) commandLine.getProperties(option);
        assertEquals(0, properties.size());
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty(option, "property1", "propertyValue1");
        commandLine.addProperty(option, "property2", "propertyValue2");
        Map<String, String> properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertEquals("propertyValue1", properties.get("property1"));
        assertEquals("propertyValue2", properties.get("property2"));
    }

    @Test
    public void testToString() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addSwitch(option, true);

        String expected = "arg1 arg2 -o value1 -t test --test true";
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testGetOptionsList() {
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }
}