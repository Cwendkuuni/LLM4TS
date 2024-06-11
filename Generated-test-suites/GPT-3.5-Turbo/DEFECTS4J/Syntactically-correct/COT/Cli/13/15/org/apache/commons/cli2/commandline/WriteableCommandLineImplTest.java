import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option rootOption = new Option("root", "Root Option");
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test", "Test Option");
        commandLine.addOption(option);
        
        assertEquals(true, commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument("arg", "Argument");
        commandLine.addOption(argument);
        Object value = "value";
        commandLine.addValue(argument, value);

        List<Object> values = commandLine.getValues(argument, null);
        assertEquals(Arrays.asList(value), values);
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("switch", "Switch Option");
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(true, switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);

        String retrievedValue = commandLine.getProperty(property, null);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testLooksLikeOption() {
        assertEquals(true, commandLine.looksLikeOption("-option"));
        assertEquals(false, commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        List<String> args = Arrays.asList(arg1, arg2);
        commandLine = new WriteableCommandLineImpl(null, args);

        String expected = arg1 + " " + arg2;
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testSetDefaultValues() {
        Argument argument = new Argument("arg", "Argument");
        Option option = argument.getMap().values().iterator().next();
        List<Object> defaults = Arrays.asList("default1", "default2");

        commandLine.setDefaultValues(option, defaults);

        List<Object> retrievedDefaults = commandLine.getValues(option, null);
        assertEquals(defaults, retrievedDefaults);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("switch", "Switch Option");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(defaultSwitch, switchValue);
    }
}

