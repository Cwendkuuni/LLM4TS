import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.*;

public class WriteableCommandLineTest {

    WriteableCommandLine commandLine;

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
        commandLine.addValue(option, "value");
        assertTrue(commandLine.hasValue("test"));
        assertEquals("value", commandLine.getValue("test"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionImpl("test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List values = commandLine.getUndefaultedValues(option);
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("test");
        List defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues("test"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("test");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch("test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwiceShouldThrowException() {
        Option option = new OptionImpl("test");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("test");
        commandLine.setDefaultSwitch(option, true);
        assertEquals(true, commandLine.getDefaultSwitch("test"));
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionImpl("test");
        commandLine.addProperty(option, "property1", "value1");
        assertEquals("value1", commandLine.getProperty("test", "property1"));
    }

    @Test
    public void testAddDefaultProperty() {
        commandLine.addProperty("property2", "value2");
        assertEquals("value2", commandLine.getDefaultProperty("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
        assertFalse(commandLine.looksLikeOption("test"));
    }

    @Test
    public void testSetAndGetCurrentOption() {
        Option option = new OptionImpl("test");
        commandLine.setCurrentOption(option);
        assertEquals(option, commandLine.getCurrentOption());
    }

}