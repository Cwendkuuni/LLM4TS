import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "This is a test option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "This is a test option");
        Object value = "testValue";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValue(option));
    }

    @Test
    public void testGetArgumentValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "This is a test option");
        Object value = "testValue";
        commandLine.addValue(option, value);
        List values = commandLine.getArgumentValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "This is a test option");
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getArgumentValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testSwitch", "This is a test switch", true);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testSwitch", "This is a test switch", true);
        commandLine.setDefaultSwitch(option, true);
        assertTrue((boolean) commandLine.getValue(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(commandLine.looksLikeOption("-option"));
    }

}