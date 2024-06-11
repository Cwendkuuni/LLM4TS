import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;
    private String property;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
        option = new OptionImpl("test");
        property = "property";
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value");
        assertEquals(expectedValues, commandLine.getValues("test"));
    }

    @Test
    public void testGetValues() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        assertEquals(expectedValues, commandLine.getValues("test"));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues("test"));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertEquals(true, commandLine.getSwitch("test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertEquals(true, commandLine.getDefaultSwitch("test"));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(property, "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
    }
}