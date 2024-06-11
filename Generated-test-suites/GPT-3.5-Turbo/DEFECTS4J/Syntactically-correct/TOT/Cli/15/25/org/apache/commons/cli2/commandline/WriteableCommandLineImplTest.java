import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option.Builder("opt1").create();
        Option option2 = new Option.Builder("opt2").create();

        commandLine = new WriteableCommandLineImpl(option1, null);
        commandLine.addOption(option2);
    }

    @Test
    public void testAddOption() {
        Option option3 = new Option.Builder("opt3").create();

        commandLine.addOption(option3);

        assertTrue(commandLine.hasOption(option3));
    }

    @Test
    public void testGetValue() {
        Option option = new Option.Builder("opt").create();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getValues(option, null);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option.Builder("opt").create();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getUndefaultedValues(option);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option.Builder("opt").create();
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);

        assertNotNull(switchValue);
        assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = new Option.Builder("opt").create();
        commandLine.addProperty(option, "property", "value");

        String propertyValue = commandLine.getProperty(option, "property", null);

        assertNotNull(propertyValue);
        assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperty_DefaultValue() {
        Option option = new Option.Builder("opt").create();

        String propertyValue = commandLine.getProperty(option, "property", "default");

        assertNotNull(propertyValue);
        assertEquals("default", propertyValue);
    }

    @Test
    public void testLookLikeOption() {
        assertTrue(commandLine.looksLikeOption("-opt1"));
        assertTrue(commandLine.looksLikeOption("--opt2"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        assertEquals("", commandLine.toString());

        Option option = new Option.Builder("opt").create();
        commandLine.addValue(option, "value");

        assertEquals("value", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();

        assertEquals(2, options.size());
        assertEquals("opt1", options.get(0).getPreferredName());
        assertEquals("opt2", options.get(1).getPreferredName());
    }

    @Test
    public void testGetOptionTriggers() {
        Set<String> optionTriggers = commandLine.getOptionTriggers();

        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("opt1"));
        assertTrue(optionTriggers.contains("opt2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option.Builder("opt").create();
        List<String> defaults = Arrays.asList("default1", "default2");

        commandLine.setDefaultValues(option, defaults);

        List<String> defaultValues = commandLine.getValues(option, null);

        assertEquals(2, defaultValues.size());
        assertEquals("default1", defaultValues.get(0));
        assertEquals("default2", defaultValues.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option.Builder("opt").create();
        commandLine.setDefaultSwitch(option, true);

        Boolean defaultSwitch = commandLine.getSwitch(option, null);

        assertNotNull(defaultSwitch);
        assertTrue(defaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option.Builder("opt").create();
        List<String> arguments = Arrays.asList("arg1", "arg2");

        commandLine = new WriteableCommandLineImpl(option, arguments);

        List<String> normalised = commandLine.getNormalised();

        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}