import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        Option option = new Option("o");

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption("o"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        Option option = new Option("o");

        commandLine.addValue(option, "value");

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        Option option = new Option("o");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        Option option = new Option("o");
        List defaultValues = new ArrayList();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());

        List actualDefaultValues = commandLine.getDefaultValues(option);
        assertEquals(2, actualDefaultValues.size());
        assertEquals("default1", actualDefaultValues.get(0));
        assertEquals("default2", actualDefaultValues.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        Option option = new Option("o");

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        Option option = new Option("o");

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getDefaultSwitchValue(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        Option option = new Option("o");

        commandLine.addProperty(option, "property", "value");

        assertEquals("value", commandLine.getPropertyValue(option, "property"));
    }

    @Test
    public void testAddDefaultProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getDefaultPropertyValue("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o");

        commandLine.setCurrentOption(option);

        assertSame(option, commandLine.getCurrentOption());
    }

}