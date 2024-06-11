
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("-a", "description");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption("-a"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("-a", "description");
        commandLine.addOption(option);

        commandLine.addValue(option, "value");

        assertEquals("value", commandLine.getValues(option).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("-a", "description");
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(2, commandLine.getUndefaultedValues(option).size());
        assertTrue(commandLine.getUndefaultedValues(option).contains("value1"));
        assertTrue(commandLine.getUndefaultedValues(option).contains("value2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("-a", "description");
        commandLine.addOption(option);

        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));

        assertEquals(2, commandLine.getValues(option).size());
        assertTrue(commandLine.getValues(option).contains("default1"));
        assertTrue(commandLine.getValues(option).contains("default2"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("-a", "description");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.isSwitchEnabled(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("-a", "description");
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.isSwitchEnabled(option));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("-a", "description");
        commandLine.addOption(option);

        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        assertEquals("value1", commandLine.getProperty(option, "property1"));
        assertEquals("value2", commandLine.getProperty(option, "property2"));
    }

    @Test
    public void testAddPropertyToDefault() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        assertEquals("value1", commandLine.getProperty(null, "property1"));
        assertEquals("value2", commandLine.getProperty(null, "property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-a"));
        assertTrue(commandLine.looksLikeOption("--option"));
    }
}
