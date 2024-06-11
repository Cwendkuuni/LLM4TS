import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setUp() {
        writeableCommandLine = new WriteableCommandLineImplementation();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        assertEquals(1, writeableCommandLine.getOptions().size());
        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        assertEquals(1, writeableCommandLine.getValues(option).size());
        assertTrue(writeableCommandLine.getValues(option).contains("value1"));

        writeableCommandLine.addValue(option, "value2");
        assertEquals(2, writeableCommandLine.getValues(option).size());
        assertTrue(writeableCommandLine.getValues(option).contains("value1"));
        assertTrue(writeableCommandLine.getValues(option).contains("value2"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List<String> undefaultedValues = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(2, undefaultedValues.size());
        assertTrue(undefaultedValues.contains("value1"));
        assertTrue(undefaultedValues.contains("value2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        writeableCommandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, writeableCommandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option));

        writeableCommandLine.addSwitch(option, false);
        assertFalse(writeableCommandLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchDuplicate() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);

        writeableCommandLine.setDefaultSwitch(option, true);
        assertEquals(true, writeableCommandLine.getDefaultSwitch(option));

        writeableCommandLine.setDefaultSwitch(option, false);
        assertEquals(false, writeableCommandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property1", "value1");
        assertEquals("value1", writeableCommandLine.getProperty(option, "property1"));

        writeableCommandLine.addProperty(option, "property2", "value2");
        assertEquals("value2", writeableCommandLine.getProperty(option, "property2"));
    }

    @Test
    public void testAddPropertyDefault() {
        writeableCommandLine.addProperty("defaultProperty1", "defaultValue1");
        assertEquals("defaultValue1", writeableCommandLine.getProperty("defaultProperty1"));

        writeableCommandLine.addProperty("defaultProperty2", "defaultValue2");
        assertEquals("defaultValue2", writeableCommandLine.getProperty("defaultProperty2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("-option"));
        assertTrue(writeableCommandLine.looksLikeOption("--option"));
        assertFalse(writeableCommandLine.looksLikeOption("value"));
    }
}