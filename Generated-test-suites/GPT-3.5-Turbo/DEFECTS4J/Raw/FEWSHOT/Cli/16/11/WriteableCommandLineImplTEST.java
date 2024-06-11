import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLineImpl;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class WriteableCommandLineImplTest {
    private Option rootOption;
    private List arguments;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        rootOption = new Option("--root");
        arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("--example");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument("value");
        commandLine.addValue(argument, "exampleValue");
        List values = commandLine.getValues(argument, null);
        assertEquals(1, values.size());
        assertEquals("exampleValue", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("-s");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("--example");
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("--example");
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("--example"));
    }

    @Test
    public void testGetValues() {
        Argument argument = new Argument("value");
        commandLine.addValue(argument, "exampleValue");
        List values = commandLine.getValues(argument, null);
        assertEquals(1, values.size());
        assertEquals("exampleValue", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument = new Argument("value");
        commandLine.addValue(argument, "exampleValue");
        List values = commandLine.getUndefaultedValues(argument);
        assertEquals(1, values.size());
        assertEquals("exampleValue", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("--example");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        String property = "example";
        String value = "exampleValue";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        String property = "example";
        String value = "exampleValue";
        Option option = new PropertyOption();
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        String property = "example";
        String defaultValue = "defaultValue";
        assertNull(commandLine.getProperty(property, defaultValue));
    }

    @Test
    public void testGetProperties() {
        String property = "example";
        String value = "exampleValue";
        commandLine.addProperty(property, value);
        assertEquals(1, commandLine.getProperties().size());
        assertTrue(commandLine.getProperties().contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "--example";
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        Argument argument = new Argument("value");
        String value = "exampleValue";
        commandLine.addValue(argument, value);
        assertEquals(value, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("--example");
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("--example");
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("--example"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("--example");
        List defaults = new ArrayList();
        defaults.add("defaultValue");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("defaultValue", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("--example");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Argument argument = new Argument("value");
        String value = "exampleValue";
        commandLine.addValue(argument, value);
        List normalised = commandLine.getNormalised();
        assertEquals(1, normalised.size());
        assertEquals(value, normalised.get(0));
    }
}