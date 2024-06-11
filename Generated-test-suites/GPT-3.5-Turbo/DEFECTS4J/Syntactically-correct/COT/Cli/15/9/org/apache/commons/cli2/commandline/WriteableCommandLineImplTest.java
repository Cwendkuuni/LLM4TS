
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new OptionBuilder().withName("option").create();
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withName("option").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionBuilder().withName("option").create();
        Object value = "value";
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, null);
        assertEquals(values.get(0), value);
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withName("option").create();
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(switchValue, Boolean.valueOf(value));
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withName("option").create();
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption("option");
        assertEquals(option.getName(), "option");
    }

    @Test
    public void testGetValues() {
        Option option = new OptionBuilder().withName("option").create();
        List defaultValues = new ArrayList();
        Object value = "value";
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, defaultValues);
        assertEquals(values.get(0), value);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionBuilder().withName("option").create();
        Object value = "value";
        commandLine.addValue(option, value);
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(values.get(0), value);
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withName("option").create();
        Boolean defaultValue = Boolean.FALSE;
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, defaultValue);
        assertEquals(switchValue, Boolean.TRUE);
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        String propertyValue = commandLine.getProperty(property);
        assertEquals(propertyValue, value);
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        Set properties = commandLine.getProperties();
        assertTrue(properties.contains(property));
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String property = "property";
        String defaultValue = "defaultValue";
        String propertyValue = commandLine.getProperty(property, defaultValue);
        assertEquals(propertyValue, defaultValue);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
    }

    @Test
    public void testToString() {
        String argument = "argument";
        commandLine.getNormalised().add(argument);
        String stringRepresentation = commandLine.toString();
        assertEquals(stringRepresentation, argument);
    }

    @Test
    public void testGetOptions() {
        List options = commandLine.getOptions();
        assertEquals(options.size(), 1);
    }

    @Test
    public void testGetOptionTriggers() {
        Set optionTriggers = commandLine.getOptionTriggers();
        assertTrue(optionTriggers.contains("option"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withName("option").create();
        List defaults = new ArrayList();
        commandLine.setDefaultValues(option, defaults);
        List defaultValues = commandLine.getValues(option, null);
        assertEquals(defaultValues, defaults);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withName("option").create();
        Boolean defaultSwitch = Boolean.FALSE;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(switchValue, defaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        List normalised = commandLine.getNormalised();
        assertEquals(normalised.size(), 0);
    }
}
