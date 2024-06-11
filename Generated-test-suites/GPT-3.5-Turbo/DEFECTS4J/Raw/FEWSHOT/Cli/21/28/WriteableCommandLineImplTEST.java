import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;
    private Option option1;
    private Option option2;

    @Before
    public void setUp() {
        option1 = OptionBuilder.withName("option1").create();
        option2 = OptionBuilder.withName("option2").withArgument(true).create();
        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<String>());
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option2, "value1");
        List<String> values = commandLine.getValues(option2, null);
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
        assertFalse(commandLine.hasOption(option1));
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option1);
        Option result = commandLine.getOption("option1");
        assertEquals(option1, result);
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(option2, "value1");
        List<String> values = commandLine.getValues(option2, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option2, "value1");
        List<String> values = commandLine.getUndefaultedValues(option2);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option1, true);
        Boolean switchValue = commandLine.getSwitch(option1, null);
        assertEquals(true, switchValue);
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(option1, "property1", "value1");
        String propertyValue = commandLine.getProperty(option1, "property1", null);
        assertEquals("value1", propertyValue);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option1, "property1", "value1");
        String propertyValue = commandLine.getProperty(option1, "property1", null);
        assertEquals("value1", propertyValue);
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        commandLine.addProperty(option1, "property1", "value1");
        String propertyValue = commandLine.getProperty(option1, "property2", "default");
        assertEquals("default", propertyValue);
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty(option1, "property1", "value1");
        Set<String> properties = commandLine.getProperties(option1);
        assertEquals(1, properties.size());
        assertTrue(properties.contains("property1"));
    }

    @Test
    public void testGetPropertiesWithDefaultOption() {
        commandLine.addProperty("property1", "value1");
        Set<String> properties = commandLine.getProperties();
        assertEquals(1, properties.size());
        assertTrue(properties.contains("property1"));
    }
}