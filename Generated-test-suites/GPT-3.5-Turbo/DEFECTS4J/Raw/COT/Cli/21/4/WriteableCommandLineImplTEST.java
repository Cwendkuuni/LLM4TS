import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl writeableCommandLine;

    @Before
    public void setUp() {
        writeableCommandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        writeableCommandLine.addValue(option, "value");

        assertTrue(writeableCommandLine.getValues(option, null).contains("value"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        writeableCommandLine.addSwitch(option, true);

        assertTrue(writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);

        assertEquals(option, writeableCommandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test");
        writeableCommandLine.addValue(option, "value");

        assertTrue(writeableCommandLine.getValues(option, null).contains("value"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test");
        writeableCommandLine.addValue(option, "value");

        assertTrue(writeableCommandLine.getUndefaultedValues(option).contains("value"));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test");
        writeableCommandLine.addSwitch(option, true);

        assertTrue(writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("test");
        writeableCommandLine.addProperty(option, "property", "value");

        assertEquals("value", writeableCommandLine.getProperty(option, "property", null));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("test");
        writeableCommandLine.addProperty(option, "property", "value");

        assertEquals("value", writeableCommandLine.getProperty(option, "property", null));
    }

    @Test
    public void testGetPropertyOption() {
        assertEquals("value", writeableCommandLine.getProperty("property"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("test");
        writeableCommandLine.addProperty(option, "property", "value");

        assertTrue(writeableCommandLine.getProperties(option).contains("property"));
    }

    @Test
    public void testGetPropertiesDefault() {
        assertTrue(writeableCommandLine.getProperties().contains("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(writeableCommandLine.looksLikeOption("value"));
        assertTrue(writeableCommandLine.looksLikeOption("-option"));
    }

    @Test
    public void testToString() {
        Option option = new Option("test");
        writeableCommandLine.addValue(option, "value");

        assertEquals("value", writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.getOptionTriggers().contains("test"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        writeableCommandLine.setDefaultValues(option, null);

        assertNull(writeableCommandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        writeableCommandLine.setDefaultSwitch(option, null);

        assertNull(writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(new ArrayList(), writeableCommandLine.getNormalised());
    }
}