
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", false, "option description");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("o"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getValue(option));
    }

    @Test
    public void testGetArgumentValues() {
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> argumentValues = commandLine.getArgumentValues(option);
        assertEquals(2, argumentValues.size());
        assertEquals("value1", argumentValues.get(0));
        assertEquals("value2", argumentValues.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "option", true, "option description");
        commandLine.addOption(option);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> argumentValues = commandLine.getArgumentValues(option);
        assertEquals(2, argumentValues.size());
        assertEquals("default1", argumentValues.get(0));
        assertEquals("default2", argumentValues.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("o", "option", false, "option description");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("o", "option", false, "option description");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("key", "value");
        assertEquals("value", commandLine.getProperty("key"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
}