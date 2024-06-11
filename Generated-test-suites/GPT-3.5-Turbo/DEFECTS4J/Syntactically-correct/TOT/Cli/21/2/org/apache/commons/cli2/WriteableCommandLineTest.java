import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl();
        option = new OptionImpl("test");
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getValues(option);
        assertEquals(values.size(), 2);
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> undefaultedValues = commandLine.getUndefaultedValues(option);
        assertEquals(undefaultedValues.size(), 2);
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.addOption(option);
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getValues(option);
        List<String> undefaultedValues = commandLine.getUndefaultedValues(option);
        assertEquals(values.size(), 2);
        assertEquals(undefaultedValues.size(), 0);
        assertTrue(values.containsAll(defaultValues));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasSwitch(option));
        assertTrue(commandLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.hasSwitch(option));
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property1", "value1");
        assertTrue(commandLine.hasProperty(option, "property1"));
        assertEquals(commandLine.getProperty(option, "property1"), "value1");
    }

    @Test
    public void testAddDefaultProperty() {
        commandLine.addDefaultProperty("property1", "value1");
        assertTrue(commandLine.hasDefaultProperty("property1"));
        assertEquals(commandLine.getDefaultProperty("property1"), "value1");
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testSetCurrentOption() {
        Option option1 = new OptionImpl("option1");
        Option option2 = new OptionImpl("option2");

        commandLine.setCurrentOption(option1);
        assertEquals(commandLine.getCurrentOption(), option1);

        commandLine.setCurrentOption(option2);
        assertEquals(commandLine.getCurrentOption(), option2);
    }
}