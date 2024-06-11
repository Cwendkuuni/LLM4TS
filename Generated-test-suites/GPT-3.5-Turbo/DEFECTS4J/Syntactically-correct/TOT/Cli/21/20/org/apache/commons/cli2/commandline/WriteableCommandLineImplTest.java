import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option.Builder("root").create();
        commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
    }

    @Test
    public void testAddOption() {
        Option option = new Option.Builder("test").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument.Builder("arg").create();
        String value = "value";
        commandLine.addValue(option, value);

        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option.Builder("switch").create();
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        String retrievedValue = commandLine.getProperty(property);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option.Builder("test").create();
        List defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);
        List retrievedDefaults = commandLine.getValues(option, null);
        assertEquals(defaults, retrievedDefaults);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option.Builder("test").create();
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testLooksLikeOption() {
        String validOption = "-option";
        assertTrue(commandLine.looksLikeOption(validOption));
        
        String invalidOption = "invalid";
        assertFalse(commandLine.looksLikeOption(invalidOption));
    }

}