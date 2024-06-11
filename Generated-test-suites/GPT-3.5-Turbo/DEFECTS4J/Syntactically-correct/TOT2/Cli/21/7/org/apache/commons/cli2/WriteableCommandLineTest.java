import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test");
        cmd.addOption(option);
        assertTrue(cmd.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test");
        cmd.addOption(option);
        cmd.addValue(option, "value");
        assertEquals("value", cmd.getValue(option));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test");
        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");
        List values = cmd.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test");
        cmd.setDefaultValues(option, List.of("default1", "default2"));
        List values = cmd.getValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains("default1"));
        assertTrue(values.contains("default2"));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test");
        cmd.addSwitch(option, true);
        assertTrue(cmd.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadyAdded() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test");
        cmd.addSwitch(option, true);
        cmd.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test");
        cmd.setDefaultSwitch(option, true);
        assertTrue(cmd.getSwitch(option));
    }

    @Test
    public void testAddPropertyToOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test");
        cmd.addProperty(option, "key", "value");
        assertEquals("value", cmd.getProperty(option, "key"));
    }

    @Test
    public void testAddPropertyToDefault() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        cmd.addProperty("key", "value");
        assertEquals("value", cmd.getProperty("key"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        assertTrue(cmd.looksLikeOption("-test"));
        assertFalse(cmd.looksLikeOption("test"));
    }

    // Additional test methods can be added for the remaining functionalities

}

class WriteableCommandLineImpl implements WriteableCommandLine {

    // Implement all the methods of the WriteableCommandLine interface

}