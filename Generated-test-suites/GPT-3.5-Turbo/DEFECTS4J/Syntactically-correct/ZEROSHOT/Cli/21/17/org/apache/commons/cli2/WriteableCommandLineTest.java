import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

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
        List<Option> options = writeableCommandLine.getOptions();
        Option addedOption = options.get(0);
        assertEquals("test", addedOption.getName());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value");
        List<Object> values = writeableCommandLine.getValues(option);
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value");
        writeableCommandLine.addValue(option, "default");
        List<Object> undefaultedValues = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(1, undefaultedValues.size());
        assertEquals("value", undefaultedValues.get(0));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultValues(option, List.of("default1", "default2"));
        List<Object> defaultValues = writeableCommandLine.getDefaultValues(option);
        assertEquals(2, defaultValues.size());
        assertEquals("default1", defaultValues.get(0));
        assertEquals("default2", defaultValues.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option);
        assertTrue(switchValue);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultSwitch(option, true);
        Boolean defaultSwitch = writeableCommandLine.getDefaultSwitch(option);
        assertTrue(defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("test");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", "value");
        String propertyValue = writeableCommandLine.getProperty(option, "property");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testAddDefaultProperty() {
        writeableCommandLine.addDefaultProperty("property", "value");
        String defaultPropertyValue = writeableCommandLine.getDefaultProperty("property");
        assertEquals("value", defaultPropertyValue);
    }

    @Test
    public void testLooksLikeOption() {
        boolean looksLikeOption = writeableCommandLine.looksLikeOption("-test");
        assertTrue(looksLikeOption);
    }

    @Test
    public void testGetCurrentOption() {
        Option option = new Option("test");
        writeableCommandLine.setCurrentOption(option);
        Option currentOption = writeableCommandLine.getCurrentOption();
        assertEquals("test", currentOption.getName());
    }

    @Test
    public void testSetCurrentOption() {
        Option option = new Option("test");
        writeableCommandLine.setCurrentOption(option);
        Option currentOption = writeableCommandLine.getCurrentOption();
        assertEquals("test", currentOption.getName());
    }
}