import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));
        commandLine.addOption(new Option("opt2", false, "s"));

        List options = commandLine.getOptions();
        Map nameToOption = commandLine.getNameToOption();

        assertEquals(2, options.size());
        assertEquals(Option.class, options.get(0).getClass());
        assertEquals("opt", options.get(0).getLongOpt());
        assertEquals("opt", options.get(0).getShortOpt());
        assertEquals("opt", nameToOption.get("opt"));
        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getValues(Option.class, "opt"));
        assertEquals(false, commandLine.hasOption(Option.class, "opt2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));
        commandLine.addOption(new Option("opt", false, "s"));
        commandLine.addOption(new Option("opt", false, "s"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));
        commandLine.addValue(new Option("opt"), "value1");

        List values = commandLine.getValues(Option.class, "opt");

        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));
        commandLine.addSwitch(new Option("opt"), true);

        Boolean switchValue = commandLine.getSwitch(new Option("opt"), false);

        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));

        assertTrue(commandLine.hasOption(new Option("opt")));
        assertFalse(commandLine.hasOption(new Option("opt2")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));
        commandLine.addOption(new Option("opt2", false, "s"));

        Option option = commandLine.getOption("opt");
        Option option2 = commandLine.getOption("opt2");

        assertSame(option, Option.class, "opt");
        assertSame(option2, Option.class, "opt2");
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));
        commandLine.addValue(new Option("opt"), "value1");
        commandLine.addValue(new Option("opt"), "value2");

        List values = commandLine.getValues(Option.class, "opt");

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));
        commandLine.addSwitch(new Option("opt"), true);

        Boolean switchValue = commandLine.getSwitch(new Option("opt"), false);

        assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));
        commandLine.addProperty("property", "value");

        Properties properties = commandLine.getProperties();

        assertEquals("value", properties.getProperty("property"));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));
        commandLine.addProperty("property", "value");

        String propertyValue = commandLine.getProperty("property");

        assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        Set properties = commandLine.getProperties();

        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));

        String commandLineString = commandLine.toString();

        List normalised = commandLine.getNormalised();
        String expected = String.join(" ", normalised);

        assertEquals(expected, commandLineString);
    }

    @Test
    public void testGetOptionsList() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));
        commandLine.addOption(new Option("opt2", false, "s"));

        List options = commandLine.getOptions();

        assertEquals(2, options.size());
    }

    @Test
    public void testGetOptionTriggersSet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));
        commandLine.addOption(new Option("opt2", false, "s"));

        Set optionTriggers = commandLine.getOptionTriggers();

        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.containsAll(Arrays.asList("opt", "opt2")));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));

        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        commandLine.setDefaultValues(new Option("opt"), defaultValues);

        List values = commandLine.getValues(Option.class, "opt");

        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));

        commandLine.setDefaultSwitch(new Option("opt"), true);

        Boolean switchValue = commandLine.getSwitch(new Option("opt"), false);

        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalisedList() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Arrays.asList("arg1", "arg2"));

        List normalised = commandLine.getNormalised();

        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }

    @Test
    public void testEmptyWriteableCommandLine() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option());

        assertNull(commandLine.toString());
        assertTrue(commandLine.getOptions().isEmpty());
        assertTrue(commandLine.getOptionTriggers().isEmpty());
        assertTrue(commandLine.getProperties().isEmpty());
    }
}