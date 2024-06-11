import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private Option option;
    private List arguments;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        option = new OptionBuilder()
            .withName("option")
            .withDescription("Example option")
            .create();

        arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");

        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option subOption = new OptionBuilder()
            .withName("subOption")
            .withDescription("Example sub option")
            .create();

        commandLine.addOption(subOption);

        assertTrue(commandLine.hasOption(subOption));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option));

        Option nonExistingOption = new OptionBuilder()
            .withName("nonExistingOption")
            .withDescription("Example non-existing option")
            .create();

        assertFalse(commandLine.hasOption(nonExistingOption));
    }

    @Test
    public void testGetOption() {
        Option retrievedOption = commandLine.getOption("option");

        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = commandLine.getProperty("property");

        assertNull(property);
    }

    @Test
    public void testAddProperty() {
        String propertyValue = "propertyValue";

        commandLine.addProperty(option, "property", propertyValue);

        String retrievedValue = commandLine.getProperty(option, "property", null);
        assertEquals(propertyValue, retrievedValue);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String defaultValue = "defaultValue";

        String property = commandLine.getProperty(option, "property", defaultValue);
        assertEquals(defaultValue, property);
    }

    @Test
    public void testGetProperties() {
        Set properties = commandLine.getProperties(option);
        assertNotNull(properties);
        assertEquals(0, properties.size());
    }

    @Test
    public void testGetPropertiesDefaultOption() {
        Set properties = commandLine.getProperties();
        assertNotNull(properties);
        assertEquals(0, properties.size());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));

        assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testToString() {
        String stringRepresentation = commandLine.toString();
        assertNotNull(stringRepresentation);
    }

    @Test
    public void testGetOptions() {
        List options = commandLine.getOptions();
        assertNotNull(options);
        assertEquals(1, options.size());
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Set triggers = commandLine.getOptionTriggers();
        assertNotNull(triggers);
        assertEquals(1, triggers.size());
        assertTrue(triggers.contains("option"));
    }

    @Test
    public void testSetDefaultValues() {
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");

        commandLine.setDefaultValues(option, defaultValues);

        List retrievedDefaultValues = commandLine.getValues(option, null);
        assertEquals(1, retrievedDefaultValues.size());
        assertTrue(retrievedDefaultValues.contains("defaultValue1"));
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = Boolean.TRUE;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean retrievedDefaultSwitch = commandLine.getSwitch(option, null);
        assertEquals(defaultSwitch, retrievedDefaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        List normalised = commandLine.getNormalised();
        assertNotNull(normalised);
        assertEquals(2, normalised.size());
        assertTrue(normalised.contains("arg1"));
        assertTrue(normalised.contains("arg2"));
    }
}