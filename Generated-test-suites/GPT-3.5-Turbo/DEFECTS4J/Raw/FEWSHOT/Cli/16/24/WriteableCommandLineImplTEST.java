package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Argument();
        Option option2 = new Argument();
        Option option3 = new Argument();
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        Option rootOption = new Option();
        rootOption.addTrigger("root");
        rootOption.addAlias("r");
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
    }

    @Test
    public void testAddOption() {
        Option option4 = new Argument();
        assertFalse(commandLine.hasOption(option4));
        commandLine.addOption(option4);
        assertTrue(commandLine.hasOption(option4));
    }

    @Test
    public void testAddValue() {
        Option option1 = new Argument();
        Object value1 = "value1";
        assertNull(commandLine.getValues(option1, null));
        commandLine.addValue(option1, value1);
        assertEquals(value1, commandLine.getValues(option1, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option1 = new Argument();
        Option option2 = new Argument();
        assertFalse(commandLine.getSwitch(option1, null));
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, null));
        commandLine.addSwitch(option2, false);
        assertFalse(commandLine.getSwitch(option2, null));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Argument();
        Option option2 = new Argument();
        assertFalse(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option2));
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option2));
        commandLine.addOption(option2);
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("option1"));
        assertNull(commandLine.getOption("option2"));
        Option option1 = new Argument();
        Option option2 = new Argument();
        option1.addTrigger("option1");
        option2.addTrigger("option2");
        commandLine.addOption(option1);
        assertEquals(option1, commandLine.getOption("option1"));
        assertNull(commandLine.getOption("option2"));
        commandLine.addOption(option2);
        assertEquals(option2, commandLine.getOption("option2"));
    }

    @Test
    public void testGetValues() {
        Option option1 = new Argument();
        Option option2 = new Argument();
        Object value1 = "value1";
        List defaultValues1 = new ArrayList();
        defaultValues1.add("defaultValue1");
        List defaultValues2 = new ArrayList();
        defaultValues2.add("defaultValue2");
        assertNull(commandLine.getValues(option1, null));
        assertEquals(defaultValues1, commandLine.getValues(option1, defaultValues1));
        commandLine.addValue(option1, value1);
        assertEquals(value1, commandLine.getValues(option1, defaultValues1).get(0));
        assertNull(commandLine.getValues(option2, null));
        assertEquals(defaultValues2, commandLine.getValues(option2, defaultValues2));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option1 = new Argument();
        Object value1 = "value1";
        assertNull(commandLine.getUndefaultedValues(option1));
        commandLine.addValue(option1, value1);
        assertEquals(value1, commandLine.getUndefaultedValues(option1).get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option1 = new Argument();
        Option option2 = new Argument();
        assertFalse(commandLine.getSwitch(option1, null));
        assertTrue(commandLine.getSwitch(option1, true));
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, null));
        assertFalse(commandLine.getSwitch(option2, null));
        assertTrue(commandLine.getSwitch(option2, true));
    }

    @Test
    public void testGetProperty() {
        String property1 = "property1";
        assertNull(commandLine.getProperty(property1));
        Option option1 = new Argument();
        Option option2 = new Argument();
        commandLine.addProperty(option1, property1, "value1");
        assertEquals("value1", commandLine.getProperty(option1, property1, null));
        assertNull(commandLine.getProperty(option1, "property2", null));
        commandLine.addProperty("property2", "value2");
        assertEquals("value2", commandLine.getProperty(option2, "property2", null));
        assertNull(commandLine.getProperty(option2, property1, null));
    }

    @Test
    public void testGetProperties() {
        assertNull(commandLine.getProperties(option1));
        assertNotNull(commandLine.getProperties());
        Option option1 = new Argument();
        Option option2 = new Argument();
        String property1 = "property1";
        String value1 = "value1";
        commandLine.addProperty(option1, property1, value1);
        assertEquals(Collections.singleton(property1), commandLine.getProperties(option1));
        assertEquals(Collections.singleton(property1), commandLine.getProperties());
        Option option3 = new Argument();
        assertNull(commandLine.getProperties(option3));
        assertNull(commandLine.getProperties());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("option"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("param"));
    }

    @Test
    public void testToString() {
        assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        List options = commandLine.getOptions();
        assertEquals(3, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));
    }

    @Test
    public void testGetOptionTriggers() {
        Set triggers = commandLine.getOptionTriggers();
        assertEquals(3, triggers.size());
        assertTrue(triggers.contains("root"));
        assertTrue(triggers.contains("r"));
        assertTrue(triggers.contains(option1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option1 = new Argument();
        List defaultValues1 = new ArrayList();
        defaultValues1.add("defaultValue1");
        List defaultValues2 = new ArrayList();
        defaultValues2.add("defaultValue2");
        commandLine.setDefaultValues(option1, defaultValues1);
        assertEquals(defaultValues1, commandLine.getValues(option1, null));
        commandLine.setDefaultValues(option1, defaultValues2);
        assertEquals(defaultValues2, commandLine.getValues(option1, null));
        commandLine.setDefaultValues(option1, null);
        assertNull(commandLine.getValues(option1, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option1 = new Argument();
        assertFalse(commandLine.getSwitch(option1, null));
        commandLine.setDefaultSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, null));
        commandLine.setDefaultSwitch(option1, false);
        assertFalse(commandLine.getSwitch(option1, null));
        commandLine.setDefaultSwitch(option1, null);
        assertFalse(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(2, commandLine.getNormalised().size());
        assertTrue(commandLine.getNormalised().contains("arg1"));
        assertTrue(commandLine.getNormalised().contains("arg2"));
    }
}