import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("name", "description");
        cmdLine.addOption(option);
        assertTrue(cmdLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("name", "description");
        cmdLine.addValue(option, "value");
        assertEquals("value", cmdLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("name", "description");
        cmdLine.addSwitch(option, true);
        assertTrue(cmdLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("name", "description");
        assertFalse(cmdLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("name", "description");
        cmdLine.addOption(option);
        assertEquals(option, cmdLine.getOption("name"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("name", "description");
        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");
        assertEquals("value1", cmdLine.getValues(option, null).get(0));
        assertEquals("value2", cmdLine.getValues(option, null).get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("name", "description");
        cmdLine.addSwitch(option, true);
        assertTrue(cmdLine.getSwitch(option, null));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        cmdLine.addProperty("property", "value");
        assertEquals("value", cmdLine.getProperty("property", null));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        assertTrue(cmdLine.looksLikeOption("-a"));
        assertFalse(cmdLine.looksLikeOption("arg"));
    }

}