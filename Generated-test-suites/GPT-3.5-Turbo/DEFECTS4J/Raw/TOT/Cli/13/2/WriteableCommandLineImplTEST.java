import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new OptionBuilder().withName("test-option").create();
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withName("test-option").create();
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
        assertNotNull(commandLine.getOption("test-option"));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionBuilder().withName("test-option").create();
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(2, commandLine.getValues(option, new ArrayList<>()).size());
        assertEquals("value1", commandLine.getValues(option, new ArrayList<>()).get(0));
        assertEquals("value2", commandLine.getValues(option, new ArrayList<>()).get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withName("test-option").create();
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withName("test-option").create();
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withName("test-option").create();
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("test-option"));
    }

    @Test
    public void testGetValues() {
        Option option = new OptionBuilder().withName("test-option").create();
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(2, commandLine.getValues(option, new ArrayList<>()).size());
        assertEquals("value1", commandLine.getValues(option, new ArrayList<>()).get(0));
        assertEquals("value2", commandLine.getValues(option, new ArrayList<>()).get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withName("test-option").create();
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("test-property", "property-value");

        assertEquals("property-value", commandLine.getProperty("test-property", ""));
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("argument"));

        Option option = new OptionBuilder().withName("test-option").withPrefixes("-").create();
        commandLine.addOption(option);

        assertTrue(commandLine.looksLikeOption("-test-option"));
    }

    @Test
    public void testToString() {
        Option option1 = new OptionBuilder().withName("test-option1").create();
        Option option2 = new OptionBuilder().withName("test-option2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option2, "value2");

        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new OptionBuilder().withName("test-option").create();
        commandLine.addOption(option);

        assertTrue(commandLine.getOptionTriggers().contains("test-option"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withName("test-option").create();
        commandLine.addOption(option);

        commandLine.setValues(option, new ArrayList<>());
        assertEquals(0, commandLine.getValues(option, new ArrayList<>()).size());

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default-value");
        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(1, commandLine.getValues(option, new ArrayList<>()).size());
        assertEquals("default-value", commandLine.getValues(option, new ArrayList<>()).get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withName("test-option").create();
        commandLine.addOption(option);

        commandLine.setSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, true));

        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Option option = new OptionBuilder().withName("test-option").create();
        commandLine.addOption(option);

        commandLine.addValue(option, "value");

        assertEquals(1, commandLine.getNormalised().size());
        assertEquals("value", commandLine.getNormalised().get(0));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty("test-property", "property-value");

        assertEquals("property-value", commandLine.getProperty("test-property", ""));
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty("test-property1", "property-value1");
        commandLine.addProperty("test-property2", "property-value2");

        assertEquals(2, commandLine.getProperties().size());
        assertTrue(commandLine.getProperties().contains("test-property1"));
        assertTrue(commandLine.getProperties().contains("test-property2"));
    }
}