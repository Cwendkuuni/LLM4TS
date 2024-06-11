
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private Option option1;
    private Option option2;
    private Argument argument1;
    private CommandLineImpl commandLine;
    private List<String> arguments;

    @Before
    public void setUp() {
        option1 = new OptionBuilder().withShortName("o1").withLongName("option1").create();
        option2 = new OptionBuilder().withShortName("o2").withLongName("option2").create();
        argument1 = new ArgumentBuilder().create();
        arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(option1, arguments);
    }

    // Test case for addOption() method
    @Test
    public void testAddOption() {
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
    }

    // Test case for addValue() method
    @Test
    public void testAddValue() {
        commandLine.addValue(argument1, "value1");
        List<String> values = commandLine.getValues(argument1);
        assertTrue(values.contains("value1"));
    }

    // Test case for addSwitch() method
    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));
    }

    // Test case for hasOption() method
    @Test
    public void testHasOption() {
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
    }

    // Test case for getOption() method
    @Test
    public void testGetOption() {
        commandLine.addOption(option1);
        assertEquals(option1, commandLine.getOption("o1"));
    }

    // Test case for getValues() method
    @Test
    public void testGetValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.addValue(argument1, "value1");
        List<String> values = commandLine.getValues(argument1, defaultValues);
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("default"));
    }

    // Test case for getUndefaultedValues() method
    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(argument1, "value1");
        List<String> values = commandLine.getUndefaultedValues(argument1);
        assertTrue(values.contains("value1"));
    }

    // Test case for getSwitch() method
    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));
    }

    // Test case for getProperty() method
    @Test
    public void testGetProperty() {
        commandLine.addProperty("property1", "value1");
        assertEquals("value1", commandLine.getProperty("property1"));
    }

    // Test case for addProperty() method
    @Test
    public void testAddProperty() {
        commandLine.addProperty(option1, "property1", "value1");
        assertEquals("value1", commandLine.getProperty(option1, "property1", "defaultValue"));
    }

    // Test case for getProperty() method with default value
    @Test
    public void testGetPropertyWithDefault() {
        assertEquals("defaultValue", commandLine.getProperty("property1", "defaultValue"));
    }

    // Test case for getProperties() method
    @Test
    public void testGetProperties() {
        commandLine.addProperty(option1, "property1", "value1");
        assertEquals("value1", commandLine.getProperties(option1).iterator().next());
    }

    // Test case for looksLikeOption() method
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o1"));
        assertFalse(commandLine.looksLikeOption("value1"));
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument1, "value2");
        commandLine.addValue(argument1, "value3");
        assertEquals("value1 value2 value3", commandLine.toString());
    }

    // Test case for getOptions() method
    @Test
    public void testGetOptions() {
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptions().size());
    }

    // Test case for getOptionTriggers() method
    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptionTriggers().size());
    }

    // Test case for setDefaultValues() method
    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.setDefaultValues(argument1, defaultValues);
        List<String> values = commandLine.getValues(argument1);
        assertTrue(values.contains("default"));
    }

    // Test case for setDefaultSwitch() method
    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));
    }

    // Test case for getNormalised() method
    @Test
    public void testGetNormalised() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument1, "value2");
        assertEquals(2, commandLine.getNormalised().size());
    }
}

