import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.*;
import org.apache.commons.cli2.commandline.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new DefaultOptionBuilder()
            .withLongName("option")
            .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new DefaultOptionBuilder()
            .withLongName("option")
            .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        Option option = new DefaultOptionBuilder()
            .withLongName("option")
            .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new DefaultOptionBuilder()
            .withLongName("option")
            .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new DefaultOptionBuilder()
            .withLongName("option")
            .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertNull(commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        Option option = new DefaultOptionBuilder()
            .withLongName("option")
            .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertNull(commandLine.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new DefaultOptionBuilder()
            .withLongName("option")
            .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertEquals(Collections.EMPTY_LIST, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = new DefaultOptionBuilder()
            .withLongName("option")
            .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertNull(commandLine.getProperty("property"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals("default", commandLine.getProperty("property", "default"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals(Collections.EMPTY_SET, commandLine.getProperties());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertFalse(commandLine.looksLikeOption("trigger"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals("", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals(Collections.EMPTY_LIST, commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals(Collections.EMPTY_SET, commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new DefaultOptionBuilder()
            .withLongName("option")
            .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultValues(option, Collections.singletonList("default"));
        assertEquals(Collections.singletonList("default"), commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new DefaultOptionBuilder()
            .withLongName("option")
            .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals(Collections.EMPTY_LIST, commandLine.getNormalised());
    }
}