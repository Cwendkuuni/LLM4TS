import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setUp() {
        writeableCommandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value");
        assertEquals("value", writeableCommandLine.getValue(option));
    }

    @Test
    public void testGetArgumentValues() {
        Option option = new Option("test");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List<String> argumentValues = writeableCommandLine.getArgumentValues(option);
        assertEquals(defaultValues, argumentValues);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, writeableCommandLine.getArgumentValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultSwitch(option, false);
        assertFalse(writeableCommandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        writeableCommandLine.addProperty("key", "value");
        assertEquals("value", writeableCommandLine.getProperty("key"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("-test"));
        assertFalse(writeableCommandLine.looksLikeOption("value"));
    }
}