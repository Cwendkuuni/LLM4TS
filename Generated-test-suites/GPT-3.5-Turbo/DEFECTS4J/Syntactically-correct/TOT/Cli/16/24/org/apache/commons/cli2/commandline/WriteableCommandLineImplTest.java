import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addValue(option, "value");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(true, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        assertNull(commandLine.getOption("option"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("defaultValue", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(true, switchValue);
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty("property", "value");
        String propertyValue = commandLine.getProperty("property");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addProperty(option, "property", "value");
        Set properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertTrue(properties.contains("property"));
    }

    @Test
    public void testGetPropertyWithDefault() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        String propertyValue = commandLine.getProperty(option, "property", "defaultValue");
        assertEquals("defaultValue", propertyValue);
    }

    @Test
    public void testGetProperties() {
        Set properties = commandLine.getProperties();
        assertEquals(0, properties.size());
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("argument"));
        assertTrue(commandLine.looksLikeOption("-option"));
    }

    @Test
    public void testToString() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);
        List arguments = new ArrayList();
        arguments.add("argument");
        commandLine = new WriteableCommandLineImpl(option, arguments);
        String commandLineString = commandLine.toString();
        assertEquals("argument", commandLineString);
    }

    @Test
    public void testGetOptions() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);
        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(1, optionTriggers.size());
        assertTrue(optionTriggers.contains("option"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("defaultValue", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(true, switchValue);
    }

    @Test
    public void testGetNormalised() {
        List normalised = commandLine.getNormalised();
        assertEquals(0, normalised.size());
    }
}