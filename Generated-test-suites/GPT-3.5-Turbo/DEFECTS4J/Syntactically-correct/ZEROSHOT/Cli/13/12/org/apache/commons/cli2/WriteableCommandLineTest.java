import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new OptionImpl("test");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionImpl("test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(2, commandLine.getValues(option).size());
        assertTrue(commandLine.getValues(option).contains("value1"));
        assertTrue(commandLine.getValues(option).contains("value2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("test");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("test");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("test");
        commandLine.setDefaultSwitch(option, false);

        assertEquals(false, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        assertEquals("value1", commandLine.getProperty("property1"));
        assertEquals("value2", commandLine.getProperty("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
        assertTrue(commandLine.looksLikeOption("--test"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
}