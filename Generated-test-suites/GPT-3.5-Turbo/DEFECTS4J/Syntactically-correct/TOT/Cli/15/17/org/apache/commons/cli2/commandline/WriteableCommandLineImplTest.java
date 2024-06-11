import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private Option option1;
    private Option option2;
    private Option option3;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        option1 = new Option.Builder("option1").create();
        option2 = new Option.Builder("option2").create();
        option3 = new Option.Builder("option3").create();
        commandLine = new WriteableCommandLineImpl(option1, Collections.singletonList("argument1"));
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option2);
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option1, "value1");
        List<String> values = commandLine.getValues(option1, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);
        Boolean switchValue = commandLine.getSwitch(option1, null);
        assertEquals(true, switchValue);
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        Option retrievedOption = commandLine.getOption("option1");
        assertEquals(option1, retrievedOption);
    }

    @Test
    public void testGetValues() {
        List<String> defaultValues = Collections.singletonList("defaultValue");
        List<String> values = commandLine.getValues(option1, defaultValues);
        assertEquals(1, values.size());
        assertEquals("defaultValue", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option1, "value1");
        List<String> values = commandLine.getUndefaultedValues(option1);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Boolean switchValue = commandLine.getSwitch(option1, false);
        assertEquals(false, switchValue);
    }

    @Test
    public void testGetProperty() {
        String propertyValue = commandLine.getProperty("property1");
        assertNull(propertyValue);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option1, "property1", "value1");
        String propertyValue = commandLine.getProperty(option1, "property1", null);
        assertEquals("value1", propertyValue);
    }

    @Test
    public void testGetProperty_DefaultValue() {
        String propertyValue = commandLine.getProperty(option1, "property1", "defaultValue");
        assertEquals("defaultValue", propertyValue);
    }

    @Test
    public void testGetProperties() {
        Set<String> properties = commandLine.getProperties(option1);
        assertEquals(0, properties.size());
    }

    @Test
    public void testGetProperty_AllProperties() {
        commandLine.addProperty(option1, "property1", "value1");
        commandLine.addProperty(option1, "property2", "value2");
        Set<String> properties = commandLine.getProperties(option1);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option1"));
        assertFalse(commandLine.looksLikeOption("argument1"));
    }

    @Test
    public void testToString() {
        String str = commandLine.toString();
        assertEquals("argument1", str);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertTrue(options.contains(option1));
    }

    @Test
    public void testGetOptionTriggers() {
        Set<String> triggers = commandLine.getOptionTriggers();
        assertEquals(1, triggers.size());
        assertTrue(triggers.contains("option1"));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = Collections.singletonList("defaultValue");
        commandLine.setDefaultValues(option1, defaultValues);
        List<String> values = commandLine.getValues(option1, null);
        assertEquals(defaultValues, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, true);
        Boolean switchValue = commandLine.getSwitch(option1, null);
        assertEquals(true, switchValue);
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();
        assertEquals(1, normalised.size());
        assertEquals("argument1", normalised.get(0));
    }
}