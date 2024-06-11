import org.junit.Before;
import org.junit.Test;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("root");
        List arguments = Collections.singletonList("argument");

        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue_argumentOption() {
        Argument argument = new Argument();
        commandLine.addValue(argument, "value");

        assertTrue(commandLine.hasOption(argument));
        assertEquals(1, commandLine.getUndefaultedValues(argument).size());
        assertEquals("value", commandLine.getUndefaultedValues(argument).get(0));
    }

    @Test
    public void testAddValue_nonArgumentOption() {
        Option option = new Option("testOption");
        commandLine.addValue(option, "value");

        assertTrue(commandLine.hasOption(option));
        assertEquals(1, commandLine.getUndefaultedValues(option).size());
        assertEquals("value", commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_alreadySet() {
        Option option = new Option("testOption");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        assertFalse(commandLine.hasOption(option));

        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("testOption");

        assertNull(commandLine.getOption("testOption"));

        commandLine.addOption(option);
        assertNotNull(commandLine.getOption("testOption"));
        assertEquals(option, commandLine.getOption("testOption"));
    }

    @Test
    public void testGetValues_existingOption() {
        Option option = new Option("testOption");
        List<String> defaultValues = Collections.singletonList("defaultValue");
        List<String> values = Collections.singletonList("value");

        commandLine.setDefaultValues(option, defaultValues);
        commandLine.addValue(option, "value");

        assertEquals(defaultValues, commandLine.getValues(option, Collections.emptyList()));
        assertEquals(values, commandLine.getValues(option, defaultValues));
    }

    @Test
    public void testGetValues_nonExistingOption() {
        Option option = new Option("testOption");
        List<String> defaultValues = Collections.singletonList("defaultValue");

        assertEquals(defaultValues, commandLine.getValues(option, defaultValues));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("testOption");
        List<String> values = Collections.singletonList("value");

        commandLine.addValue(option, "value");

        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch_existingOption() {
        Option option = new Option("testOption");
        Boolean defaultValue = Boolean.TRUE;

        commandLine.setDefaultSwitch(option, defaultValue);
        commandLine.addSwitch(option, false);

        assertTrue(commandLine.getSwitch(option, defaultValue));
        assertFalse(commandLine.getSwitch(option, !defaultValue));
    }

    @Test
    public void testGetSwitch_nonExistingOption() {
        Option option = new Option("testOption");
        Boolean defaultValue = Boolean.TRUE;

        assertEquals(defaultValue, commandLine.getSwitch(option, defaultValue));
        assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String value = "testValue";

        assertNull(commandLine.getProperty(property));

        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));

        commandLine.addProperty(property, "newValue");
        assertEquals("newValue", commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";

        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testGetProperty_existingOption() {
        Option option = new Option("testOption");
        String property = "testProperty";
        String value = "testValue";

        assertNull(commandLine.getProperty(option, property, null));

        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));

        commandLine.addProperty(option, property, "newValue");
        assertEquals("newValue", commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetProperty_nonExistingOption() {
        Option option = new Option("testOption");
        String property = "testProperty";
        String defaultValue = "defaultValue";

        assertEquals(defaultValue, commandLine.getProperty(option, property, defaultValue));
        assertNull(commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetProperties_existingOption() {
        Option option = new Option("testOption");
        String property = "testProperty";
        String value = "testValue";

        commandLine.addProperty(option, property, value);

        assertNotNull(commandLine.getProperties(option));
        assertEquals(Collections.singleton(property), commandLine.getProperties(option));
    }

    @Test
    public void testGetProperties_nonExistingOption() {
        Option option = new Option("testOption");

        assertNotNull(commandLine.getProperties(option));
        assertEquals(Collections.emptySet(), commandLine.getProperties(option));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-testOption"));
        assertFalse(commandLine.looksLikeOption("testArgument"));
    }

    @Test
    public void testToString() {
        Option option = new Option("testOption");

        assertEquals("argument", commandLine.toString());

        commandLine.addOption(option);
        commandLine.addValue(option, "value");

        assertEquals("argument testOption=value", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("testOption");

        assertNotNull(commandLine.getOptions());
        assertFalse(commandLine.getOptions().contains(option));

        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("testOption");

        assertNotNull(commandLine.getOptionTriggers());
        assertFalse(commandLine.getOptionTriggers().contains("testOption"));

        commandLine.addOption(option);

        assertTrue(commandLine.getOptionTriggers().contains("testOption"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");

        assertNull(commandLine.getValues(option, null));

        List<String> defaultValues = Collections.singletonList("defaultValue");
        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getValues(option, Collections.emptyList()));

        commandLine.setDefaultValues(option, null);
        assertNull(commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");

        assertNull(commandLine.getSwitch(option, null));

        Boolean defaultSwitch = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertEquals(defaultSwitch, commandLine.getSwitch(option, null));

        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        List expected = Collections.singletonList("argument");

        assertEquals(expected, commandLine.getNormalised());
    }
}