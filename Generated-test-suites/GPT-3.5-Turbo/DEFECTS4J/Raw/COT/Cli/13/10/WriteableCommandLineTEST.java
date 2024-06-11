
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import java.util.List;
import java.util.ArrayList;

public class WriteableCommandLineTest {
    private WriteableCommandLine commandLine;
    private Option option1;
    private Option option2;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
        option1 = new Option("option1");
        option2 = new Option("option2");
    }

    @Test
    public void testAddOption() {
        assertEquals(0, commandLine.getOptions().size());

        commandLine.addOption(option1);
        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option1));

        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testAddValue() {
        assertNull(commandLine.getValue(option1));

        commandLine.addValue(option1, "value1");
        assertEquals("value1", commandLine.getValue(option1));

        commandLine.addValue(option2, "value2");
        assertEquals("value2", commandLine.getValue(option2));
    }

    @Test
    public void testGetArgumentValues() {
        List<String> values = new ArrayList<String>();
        values.add("value1");
        values.add("value2");

        assertEquals(0, commandLine.getArgumentValues(option1).size());

        commandLine.setArgumentValues(option1, values);
        assertEquals(2, commandLine.getArgumentValues(option1).size());
        assertTrue(commandLine.getArgumentValues(option1).contains("value1"));
        assertTrue(commandLine.getArgumentValues(option1).contains("value2"));
    }

    @Test
    public void testSetDefaultValues() {
        assertNull(commandLine.getDefaultValues(option1));

        commandLine.setDefaultValues(option1, new ArrayList<String>());
        assertNotNull(commandLine.getDefaultValues(option1));
        assertEquals(0, commandLine.getDefaultValues(option1).size());

        List<String> defaultValues = new ArrayList<String>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option1, defaultValues);
        assertEquals(2, commandLine.getDefaultValues(option1).size());
        assertTrue(commandLine.getDefaultValues(option1).contains("default1"));
        assertTrue(commandLine.getDefaultValues(option1).contains("default2"));
    }

    @Test
    public void testAddSwitch() {
        assertFalse(commandLine.hasSwitch(option1));

        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.hasSwitch(option1));

        commandLine.addSwitch(option2, false);
        assertFalse(commandLine.hasSwitch(option2));
    }

    @Test
    public void testSetDefaultSwitch() {
        assertNull(commandLine.getDefaultSwitch(option1));

        commandLine.setDefaultSwitch(option1, true);
        assertTrue(commandLine.getDefaultSwitch(option1));

        commandLine.setDefaultSwitch(option2, false);
        assertFalse(commandLine.getDefaultSwitch(option2));
    }

    @Test
    public void testAddProperty() {
        assertNull(commandLine.getProperty("property"));

        commandLine.addProperty("property", "value1");
        assertEquals("value1", commandLine.getProperty("property"));

        commandLine.addProperty("property", "value2");
        assertEquals("value2", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-"));
        assertTrue(commandLine.looksLikeOption("--"));
        assertFalse(commandLine.looksLikeOption("argument"));
    }
}

