import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
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
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getValue("test"));
    }

    @Test
    public void testGetArgumentValues() {
        Option option = new OptionImpl("test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertArrayEquals(new String[] {"value1", "value2"}, commandLine.getArgumentValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("test");
        commandLine.addOption(option);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertArrayEquals(new String[] {"default1", "default2"}, commandLine.getArgumentValues(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        Option option = new OptionImpl("test");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));
        commandLine.addSwitch(option, false); // Should throw an exception since the switch has already been added
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("test");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        assertEquals("value1", commandLine.getProperty("property1"));
        assertEquals(null, commandLine.getProperty("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
}