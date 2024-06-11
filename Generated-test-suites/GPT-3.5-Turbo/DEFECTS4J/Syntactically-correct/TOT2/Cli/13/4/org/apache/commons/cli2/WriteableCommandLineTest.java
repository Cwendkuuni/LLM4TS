import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setup() {
        writeableCommandLine= new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("--test");
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("--test");
        Object value = "value";
        writeableCommandLine.addValue(option, value);
        assertTrue(writeableCommandLine.getValues(option).contains(value));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("--test");
        List defaultValues = List.of("default1", "default2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, writeableCommandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("--test");
        boolean value = true;
        writeableCommandLine.addSwitch(option, value);
        assertEquals(value, writeableCommandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("--test");
        Boolean defaultValue = true;
        writeableCommandLine.setDefaultSwitch(option, defaultValue);
        assertEquals(defaultValue, writeableCommandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "propertyValue";
        writeableCommandLine.addProperty(property, value);
        assertEquals(value, writeableCommandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "--test";
        assertTrue(writeableCommandLine.looksLikeOption(argument));
    }

    // Add additional test methods for edge cases and error scenarios

}