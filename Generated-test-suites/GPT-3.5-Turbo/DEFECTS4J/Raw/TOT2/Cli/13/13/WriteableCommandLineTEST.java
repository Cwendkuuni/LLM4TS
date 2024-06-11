import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.cli2.*;

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
        List values = writeableCommandLine.getValues(option);
        assertEquals("value", values.get(0));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        List defaultValues = new ArrayList();
        defaultValues.add("default");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getValues(option);
        assertEquals("default", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option));
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
    }
}