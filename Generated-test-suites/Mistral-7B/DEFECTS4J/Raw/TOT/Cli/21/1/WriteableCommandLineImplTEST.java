import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option optionWithValue;
    private Option optionWithSwitch;
    private Option optionWithProperty;

    @Before
    public void setup() {
        Option rootOption = new Option("test", "test", false, "Test option");

        optionWithValue = new Option("value", "v", true, "Set a value");
        optionWithValue.setParent(rootOption);

        optionWithSwitch = new Option("switch", "s", false, "Set a switch");
        optionWithSwitch.setParent(rootOption);

        optionWithProperty = new Option("property", "p", true, "Set a property");
        optionWithProperty.setParent(rootOption);
        optionWithProperty.setType(PropertyOption.TYPE_STRING);

        commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList(new String[]{"v", "s", "--property=test=value"}));
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(optionWithValue));
        assertTrue(commandLine.hasOption(optionWithSwitch));
        assertTrue(commandLine.hasOption(optionWithProperty));
        assertFalse(commandLine.hasOption(new Option("unknown", "u", false, "Unknown option")));
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption(optionWithValue.getLongOpt());
        assertTrue(option == optionWithValue);
    }

    @Test
    public void testGetValues() {
        List values = commandLine.getValues(optionWithValue, null);
        assertEquals(Arrays.asList("value"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        List values = commandLine.getUndefaultedValues(optionWithValue);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testGetSwitch() {
        Boolean switchValue = commandLine.getSwitch(optionWithSwitch, false);
        assertTrue(switchValue == Boolean.TRUE);
    }

    @Test
    public void testGetProperty() {
        String propertyValue = commandLine.getProperty(optionWithProperty, "defaultValue"); ;
        assertEquals("test=value", propertyValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPropertyWhenNoProperty() {
        String propertyValue = commandLine.getProperty(new Option("noProperty", "n"), null);
        fail("Should throw an exception");
    }

    @Test
    public void testGetProperties() {
        Set properties = commandLine.getProperties(optionWithProperty);
        assertEquals(Collections.singleton("property"), properties);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("v"));
        assertTrue(commandLine.looksLikeOption("--value"));
        assertFalse(commandLine.looksLikeOption("--unknown"));
    }

    @Test
    public void testToString() {
        assertEquals("--test test --value value --switch --property property=test=value", commandLine.toString());
    }
}