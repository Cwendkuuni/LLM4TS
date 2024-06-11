import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
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
        Option option = new Option("option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();
        Object value = "value";
        commandLine.addValue(option, value);
        assertTrue(commandLine.getUndefaultedValues(option).contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("switch");
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertEquals(value, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option");
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option");
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        Option option = new Argument();
        List<Object> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(values, commandLine.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument();
        List<Object> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("switch");
        boolean defaultValue = true;
        assertTrue(commandLine.getSwitch(option, defaultValue));
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
        Option option = new Option("option");
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("option");
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);
        assertEquals(2, commandLine.getProperties(option).size());
    }

    @Test
    public void testGetProperties_all() {
        Option option = new Option("option");
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);
        assertEquals(2, commandLine.getProperties().size());
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("trigger"));
    }

    @Test
    public void testToString() {
        Option option1 = new Argument();
        Option option2 = new Argument();
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addValue(option1, value1);
        commandLine.addValue(option2, value2);
        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("option");
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("option");
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option");
        List<Object> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        assertEquals(defaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(0, commandLine.getNormalised().size());
    }
}