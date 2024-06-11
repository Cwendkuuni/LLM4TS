import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.resource.ResourceHelper;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private static Option option;
    private static WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        option = new Argument();
        commandLine = new WriteableCommandLineImpl(option, null);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        String value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        boolean switchValue = true;
        commandLine.addSwitch(option, switchValue);
        assertEquals(switchValue, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        assertEquals(value1, commandLine.getValues(option, null).get(0));
        assertEquals(value2, commandLine.getValues(option, null).get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        String value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testGetSwitch() {
        boolean switchValue = true;
        assertEquals(switchValue, commandLine.getSwitch(option, switchValue));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testGetProperties() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertTrue(commandLine.getProperties(option).contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        String prefix = "-";
        String trigger = prefix + "trigger";
        option.getPrefixes().add(prefix);
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        String arg1 = "argument1";
        String arg2 = "argument2";
        commandLine.addOption(option);
        commandLine.getNormalised().add(arg1);
        commandLine.getNormalised().add(arg2);
        assertEquals(arg1 + " " + arg2, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option);
        assertTrue(commandLine.getOptionTriggers().contains(option.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        String defaultValue = "default";
        commandLine.setDefaultValues(option, null);
        assertNull(commandLine.getValues(option, null));
        commandLine.setDefaultValues(option, Collections.singletonList(defaultValue));
        assertEquals(defaultValue, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        boolean defaultSwitchValue = true;
        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.getSwitch(option, null));
        commandLine.setDefaultSwitch(option, defaultSwitchValue);
        assertEquals(defaultSwitchValue, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        String arg = "argument";
        commandLine.getNormalised().add(arg);
        assertTrue(commandLine.getNormalised().contains(arg));
    }
}