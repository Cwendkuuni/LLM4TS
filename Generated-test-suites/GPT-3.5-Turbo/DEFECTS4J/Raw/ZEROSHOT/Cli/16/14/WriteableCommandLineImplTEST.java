import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private Option option1;
    private Option option2;
    private Option option3;
    private Option option4;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        option1 = new Option("opt1");
        option2 = new Option("opt2");
        option3 = new Option("opt3");
        option4 = new Option("opt4");

        commandLine = new WriteableCommandLineImpl(option1, null);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
        commandLine.addOption(option4);
    }

    @Test
    public void testAddOption() {
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
        assertTrue(commandLine.getOptions().contains(option3));
        assertTrue(commandLine.getOptions().contains(option4));
    }

    @Test
    public void testAddValue() {
        assertFalse(commandLine.hasOption(option1));

        commandLine.addValue(option1, "value1");
        assertTrue(commandLine.hasOption(option1));

        List<String> values = commandLine.getUndefaultedValues(option1);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.addValue(option1, "value2");
        values = commandLine.getUndefaultedValues(option1);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        assertFalse(commandLine.hasOption(option1));

        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.hasOption(option1));

        Boolean value = commandLine.getSwitch(option1, false);
        assertTrue(value);

        commandLine.addSwitch(option1, false);
        value = commandLine.getSwitch(option1, true);
        assertFalse(value);
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option1));

        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        Option retrievedOption = commandLine.getOption("opt1");
        assertEquals(option1, retrievedOption);

        retrievedOption = commandLine.getOption("opt2");
        assertEquals(option2, retrievedOption);

        retrievedOption = commandLine.getOption("opt3");
        assertEquals(option3, retrievedOption);

        retrievedOption = commandLine.getOption("opt4");
        assertEquals(option4, retrievedOption);
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");

        List<String> values = commandLine.getValues(option1, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option1, defaultValues);

        values = commandLine.getValues(option1, defaultValues);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        values = commandLine.getValues(option2, null);
        assertEquals(0, values.size());
        
        values = commandLine.getValues(option2, defaultValues);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");

        List<String> values = commandLine.getUndefaultedValues(option1);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        values = commandLine.getUndefaultedValues(option2);
        assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option1, true);

        Boolean value = commandLine.getSwitch(option1, null);
        assertTrue(value);

        value = commandLine.getSwitch(option2, null);
        assertNull(value);

        commandLine.setDefaultSwitch(option2, true);

        value = commandLine.getSwitch(option2, null);
        assertTrue(value);
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(option1, "property1", "value1");

        String value = commandLine.getProperty(option1, "property1", null);
        assertEquals("value1", value);

        value = commandLine.getProperty(option2, "property1", "defaultValue");
        assertEquals("defaultValue", value);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");

        String value = commandLine.getProperty(option1, "property1", null);
        assertEquals("value1", value);

        commandLine.addProperty("property2", "value2");

        value = commandLine.getProperty(option1, "property2", null);
        assertEquals("value2", value);
    }

    @Test
    public void testGetPropertyUsingDefaultOption() {
        commandLine.addProperty(option1, "property1", "value1");

        String value = commandLine.getProperty("property1", null);
        assertEquals("value1", value);

        value = commandLine.getProperty("property2", "defaultValue");
        assertEquals("defaultValue", value);
    }

    @Test
    public void testAddPropertyUsingDefaultOption() {
        commandLine.addProperty("property1", "value1");

        String value = commandLine.getProperty(option1, "property1", null);
        assertEquals("value1", value);

        commandLine.addProperty("property2", "value2");

        value = commandLine.getProperty(option1, "property2", null);
        assertEquals("value2", value);
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty(option1, "property1", "value1");
        commandLine.addProperty(option1, "property2", "value2");

        Set<String> properties = commandLine.getProperties(option1);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));

        properties = commandLine.getProperties(option2);
        assertEquals(0, properties.size());
    }

    @Test
    public void testGetPropertiesUsingDefaultOption() {
        commandLine.addProperty(option1, "property1", "value1");
        commandLine.addProperty(option1, "property2", "value2");

        Set<String> properties = commandLine.getProperties();
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-opt1"));
        assertTrue(commandLine.looksLikeOption("--opt1"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option2, "value2");
        commandLine.addValue(option3, "value3");

        String toString = commandLine.toString();
        assertEquals("value1 value2 value3", toString);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();
        assertEquals(4, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));
        assertTrue(options.contains(option4));
    }

    @Test
    public void testGetOptionTriggers() {
        Set<String> triggers = commandLine.getOptionTriggers();
        assertEquals(4, triggers.size());
        assertTrue(triggers.contains("opt1"));
        assertTrue(triggers.contains("opt2"));
        assertTrue(triggers.contains("opt3"));
        assertTrue(triggers.contains("opt4"));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option1, defaultValues);

        List<String> values = commandLine.getValues(option1, null);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));

        commandLine.setDefaultValues(option1, null);

        values = commandLine.getValues(option1, null);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
        
        commandLine.setDefaultValues(option1, new ArrayList<>());

        values = commandLine.getValues(option1, null);
        assertEquals(0, values.size());
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, false);

        Boolean value = commandLine.getSwitch(option1, null);
        assertFalse(value);

        commandLine.setDefaultSwitch(option1, true);

        value = commandLine.getSwitch(option1, null);
        assertTrue(value);

        commandLine.setDefaultSwitch(option1, null);

        value = commandLine.getSwitch(option1, null);
        assertNull(value);
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();
        assertEquals(0, normalised.size());
    }
}