import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmdLine = new DefaultWriteableCommandLine();
        Option option = new DefaultOption("testOption");
        cmdLine.addOption(option);
        assertTrue(cmdLine.hasOption("testOption"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmdLine = new DefaultWriteableCommandLine();
        Option option = new DefaultOption("testOption");
        cmdLine.addOption(option);
        cmdLine.addValue(option, "testValue");
        assertEquals("testValue", cmdLine.getValue(option));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine cmdLine = new DefaultWriteableCommandLine();
        Option option = new DefaultOption("testOption");
        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");
        assertEquals(2, cmdLine.getUndefaultedValues(option).size());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmdLine = new DefaultWriteableCommandLine();
        Option option = new DefaultOption("testOption");
        cmdLine.setDefaultValues(option, List.of("default1", "default2"));
        assertEquals(List.of("default1", "default2"), cmdLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmdLine = new DefaultWriteableCommandLine();
        Option option = new DefaultOption("testOption");
        cmdLine.addSwitch(option, true);
        assertTrue(cmdLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchError() {
        WriteableCommandLine cmdLine = new DefaultWriteableCommandLine();
        Option option = new DefaultOption("testOption");
        cmdLine.addSwitch(option, true);
        cmdLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmdLine = new DefaultWriteableCommandLine();
        Option option = new DefaultOption("testOption");
        cmdLine.setDefaultSwitch(option, true);
        assertTrue(cmdLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmdLine = new DefaultWriteableCommandLine();
        Option option = new DefaultOption("testOption");
        cmdLine.addProperty(option, "prop1", "value1");
        assertEquals("value1", cmdLine.getProperty(option, "prop1"));
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine cmdLine = new DefaultWriteableCommandLine();
        cmdLine.addProperty("prop1", "value1");
        assertEquals("value1", cmdLine.getDefaultProperty("prop1"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmdLine = new DefaultWriteableCommandLine();
        assertTrue(cmdLine.looksLikeOption("-test"));
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine cmdLine = new DefaultWriteableCommandLine();
        Option option = new DefaultOption("testOption");
        cmdLine.setCurrentOption(option);
        assertEquals(option, cmdLine.getCurrentOption());
    }
}