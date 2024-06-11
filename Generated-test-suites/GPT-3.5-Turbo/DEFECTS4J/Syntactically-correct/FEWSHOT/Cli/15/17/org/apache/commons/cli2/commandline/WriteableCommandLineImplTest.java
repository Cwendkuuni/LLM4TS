package org.apache.commons.cli2.commandline;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option("o1");
        Option option2 = new Option("o2");
        Argument argument1 = new Argument();

        commandLine = new WriteableCommandLineImpl(option1, Collections.singletonList("arg1"));

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option2, "value2");

        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);

        commandLine.addProperty(option1, "property1", "value1");
        commandLine.addProperty(option2, "property2", "value2");
    }

    @Test
    public void testAddOption() {
        Option option3 = new Option("o3");
        commandLine.addOption(option3);

        assertTrue(commandLine.hasOption(option3));
    }

    @Test
    public void testAddValue() {
        Option option3 = new Option("o3");
        commandLine.addValue(option3, "value3");

        assertEquals(Collections.singletonList("value3"), commandLine.getValues(option3, null));
    }

    @Test
    public void testAddSwitch() {
        Option option3 = new Option("o3");
        commandLine.addSwitch(option3, true);

        assertTrue(commandLine.getSwitch(option3, null));
    }

    @Test
    public void testHasOption() {
        Option option3 = new Option("o3");
        commandLine.addOption(option3);

        assertTrue(commandLine.hasOption(option3));
    }

    @Test
    public void testGetOption() {
        Option option3 = new Option("o3");
        commandLine.addOption(option3);

        assertEquals(option3, commandLine.getOption("o3"));
    }

    @Test
    public void testGetValues() {
        assertEquals(Collections.singletonList("value1"), commandLine.getValues(option1, null));
        assertEquals(Collections.singletonList("value2"), commandLine.getValues(option2, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        assertEquals(Collections.singletonList("value1"), commandLine.getUndefaultedValues(option1));
        assertEquals(Collections.singletonList("value2"), commandLine.getUndefaultedValues(option2));
    }

    @Test
    public void testGetSwitch() {
        assertTrue(commandLine.getSwitch(option1, null));
        assertFalse(commandLine.getSwitch(option2, null));
    }

    @Test
    public void testGetProperty() {
        assertEquals("value1", commandLine.getProperty(option1, "property1", null));
        assertEquals("value2", commandLine.getProperty(option2, "property2", null));
    }

    @Test
    public void testAddProperty() {
        Option option3 = new Option("o3");
        commandLine.addProperty(option3, "property3", "value3");

        assertEquals("value3", commandLine.getProperty(option3, "property3", null));
    }

    @Test
    public void testGetPropertyWithDefault() {
        assertEquals("default1", commandLine.getProperty("property1", "default1"));
        assertEquals("default2", commandLine.getProperty("property2", "default2"));
    }

    @Test
    public void testGetProperties() {
        assertEquals(Collections.singleton("property1"), commandLine.getProperties(option1));
        assertEquals(Collections.singleton("property2"), commandLine.getProperties(option2));
    }

    @Test
    public void testGetPropertiesWithDefaultOption() {
        assertEquals(Collections.singleton("property1"), commandLine.getProperties());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o1"));
        assertFalse(commandLine.looksLikeOption("arg1"));
    }

    @Test
    public void testToString() {
        assertEquals("arg1", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        assertEquals(2, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("o1"));
        assertTrue(commandLine.getOptionTriggers().contains("o2"));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option1, defaults);

        assertEquals(defaults, commandLine.getValues(option1, null));
    }

    @Test
    public void testSetDefaultValuesWithNull() {
        List<String> defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option1, defaults);
        commandLine.setDefaultValues(option1, null);

        assertNull(commandLine.getValues(option1, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, true);

        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testSetDefaultSwitchWithNull() {
        commandLine.setDefaultSwitch(option1, true);
        commandLine.setDefaultSwitch(option1, null);

        assertNull(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(Collections.singletonList("arg1"), commandLine.getNormalised());
    }
}