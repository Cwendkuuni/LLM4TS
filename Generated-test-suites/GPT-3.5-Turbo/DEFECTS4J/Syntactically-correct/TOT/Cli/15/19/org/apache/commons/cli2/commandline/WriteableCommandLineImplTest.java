import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setUp() {
        option1 = new Option("-a");
        option2 = new Option("-b");
        option3 = new Option("-c");
    }

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addOption(option2);
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        List<String> values = commandLine.getValues(option1, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addOption(option1);
        Option retrievedOption = commandLine.getOption("-a");
        assertEquals(option1, retrievedOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        List<String> values = commandLine.getValues(option1, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        List<String> values = commandLine.getUndefaultedValues(option1);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        assertEquals("value1", commandLine.getProperty("property1"));
        assertEquals("value2", commandLine.getProperty("property2"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addProperty(option1, "property1", "value1");
        commandLine.addProperty(option1, "property2", "value2");
        assertEquals("value1", commandLine.getProperty(option1, "property1", null));
        assertEquals("value2", commandLine.getProperty(option1, "property2", null));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addProperty(option1, "property1", "value1");
        commandLine.addProperty(option1, "property2", "value2");
        List<String> properties = commandLine.getProperties(option1);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        assertTrue(commandLine.looksLikeOption("-a"));
        assertFalse(commandLine.looksLikeOption("value1"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option2, "value2");
        commandLine.addValue(option3, "value3");
        assertEquals("-a value1 -b value2 -c value3", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
        List<Option> options = commandLine.getOptions();
        assertEquals(3, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
        List<String> triggers = commandLine.getOptionTriggers();
        assertEquals(3, triggers.size());
        assertTrue(triggers.contains("-a"));
        assertTrue(triggers.contains("-b"));
        assertTrue(triggers.contains("-c"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option1, defaults);
        assertEquals(defaults, commandLine.getValues(option1, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.setDefaultSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = new ArrayList<>();
        arguments.add("-a");
        arguments.add("value1");
        arguments.add("-b");
        arguments.add("value2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, arguments);
        List<String> normalised = commandLine.getNormalised();
        assertEquals(4, normalised.size());
        assertEquals("-a", normalised.get(0));
        assertEquals("value1", normalised.get(1));
        assertEquals("-b", normalised.get(2));
        assertEquals("value2", normalised.get(3));
    }

}