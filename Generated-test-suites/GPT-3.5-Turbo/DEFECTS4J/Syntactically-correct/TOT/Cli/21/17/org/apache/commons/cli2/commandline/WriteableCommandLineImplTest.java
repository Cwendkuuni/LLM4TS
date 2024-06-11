import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new Option("o", "option", false, "An example option");

        List<String> arguments = new ArrayList<>();
        arguments.add("-o");

        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option newOption = new Option("n", "newOption", false, "A new option");

        commandLine.addOption(newOption);

        assertTrue(commandLine.hasOption(newOption));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "option", true, "An example option");

        commandLine.addValue(option, "value");

        List<String> values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch", false, "An example switch");

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", false, "An example option");

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption("o");

        assertNotNull(option);
        assertEquals("o", option.getShortName());
        assertEquals("option", option.getLongName());
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "option", true, "An example option");

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");

        commandLine.setDefaultValues(option, defaultValues);
        commandLine.addValue(option, "value");

        List<String> values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("default", values.get(0));
        assertEquals("value", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "option", true, "An example option");

        commandLine.addValue(option, "value");

        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("s", "switch", false, "An example switch");

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = commandLine.getProperty("property");
        assertNull(property);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");

        String property = commandLine.getProperty("property");
        assertEquals("value", property);
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        assertNotNull(commandLine.getProperties());
        assertEquals(2, commandLine.getProperties().size());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("arg"));
    }

    @Test
    public void testToString() {
        assertEquals("-o", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertNotNull(commandLine.getOptions());
        assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        assertNotNull(commandLine.getOptionTriggers());
        assertEquals(1, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "option", true, "An example option");

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");

        commandLine.setDefaultValues(option, defaultValues);

        List<String> values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("default", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("s", "switch", false, "An example switch");

        commandLine.setDefaultSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        assertNotNull(commandLine.getNormalised());
        assertEquals(1, commandLine.getNormalised().size());
    }
}