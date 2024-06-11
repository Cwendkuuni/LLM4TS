import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("r", "root", false, "Root option");
        List<String> arguments = Arrays.asList("arg1", "arg2");
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("t", "test", false, "Test option");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument("a", "argument", false, "Argument option");
        commandLine.addValue(option, "value");

        List<String> values = commandLine.getValues(option, null);

        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch", true, "Switch option");
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);

        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", false, "Test option");
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", false, "Test option");
        commandLine.addOption(option);

        Option returnedOption = commandLine.getOption("o");
        assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Argument("a", "argument", false, "Argument option");
        commandLine.addValue(option, "value");

        List<String> values = commandLine.getValues(option, null);

        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("s", "switch", true, "Switch option");
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);

        assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("key", "value");

        String propertyValue = commandLine.getProperty("key", null);

        assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty("key", "value");

        String propertyValue = commandLine.getProperty("key", null);

        assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty("key1", "value1");
        commandLine.addProperty("key2", "value2");

        assertEquals(2, commandLine.getProperties().size());
        assertTrue(commandLine.getProperties().contains("key1"));
        assertTrue(commandLine.getProperties().contains("key2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("arg1"));
        assertTrue(commandLine.looksLikeOption("-r"));
    }

    @Test
    public void testToString() {
        String expectedResult = "arg1 arg2";
        String actualResult = commandLine.toString();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();

        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("o1"));
        assertTrue(commandLine.getOptionTriggers().contains("o2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Argument("a", "argument", false, "Argument option");
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));

        List<String> values = commandLine.getValues(option, null);

        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("s", "switch", true, "Switch option");
        commandLine.setDefaultSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);

        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();

        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}