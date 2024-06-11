import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;
    private Argument argument;

    @Before
    public void setup() {
        option = new Option("o", "option", false, "Option");
        argument = new Argument("arg", true, "Argument");
        commandLine = new WriteableCommandLineImpl(option, Collections.singletonList("value"));
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(argument, "value");
        List<String> values = commandLine.getValues(argument, null);
        assertEquals(Collections.singletonList("value"), values);
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option retrievedOption = commandLine.getOption("o");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        List<String> values = commandLine.getValues(option, null);
        assertEquals(Collections.singletonList("value"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(Collections.singletonList("value"), values);
    }

    @Test
    public void testGetSwitch() {
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertNull(switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = commandLine.getProperty("property");
        assertNull(property);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(new PropertyOption(), "property", "value");
        String property = commandLine.getProperty(new PropertyOption(), "property", null);
        assertEquals("value", property);
    }

    @Test
    public void testGetPropertyWithDefault() {
        String property = commandLine.getProperty(new PropertyOption(), "property", "default");
        assertEquals("default", property);
    }

    @Test
    public void testGetProperties() {
        Set<String> properties = commandLine.getProperties(new PropertyOption());
        assertTrue(properties.isEmpty());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o"));
    }

    @Test
    public void testToString() {
        String stringRepresentation = commandLine.toString();
        assertEquals("value", stringRepresentation);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();
        assertEquals(Collections.singletonList(option), options);
    }

    @Test
    public void testGetOptionTriggers() {
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        assertTrue(optionTriggers.contains("o"));
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.setDefaultValues(argument, Collections.singletonList("default"));
        List<String> values = commandLine.getValues(argument, null);
        assertEquals(Collections.singletonList("default"), values);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();
        assertEquals(Collections.singletonList("value"), normalised);
    }
}