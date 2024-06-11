import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option rootOption;
    private List<String> arguments;

    @Before
    public void setUp() {
        rootOption = new Option("r", "root", "Root option", false, false, 0, null);
        arguments = new ArrayList<>(Arrays.asList("arg1", "arg2", "arg3"));
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", "Test option", false, false, 0, null);
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument("a", "arg", "Test argument", false, false, false, 0, null);
        Object value = "value";
        commandLine.addValue(argument, value);
        assertTrue(commandLine.getValues(argument, Collections.emptyList()).contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch", "Test switch", false, false, 0, null);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption(option));
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(rootOption));
    }

    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("nonexistent"));
        assertEquals(rootOption, commandLine.getOption("root"));
    }

    @Test
    public void testGetValues() {
        Argument argument = new Argument("a", "arg", "Test argument", false, false, false, 0, null);
        List<Object> defaultValues = Collections.singletonList("default");
        List<Object> values = Arrays.asList("value1", "value2");
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        assertEquals(values, commandLine.getValues(argument, defaultValues));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("s", "switch", "Test switch", false, false, 0, null);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property, null));
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("nonoption"));
        assertTrue(commandLine.looksLikeOption("-r"));
    }

    @Test
    public void testToString() {
        assertEquals("arg1 arg2 arg3", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertTrue(commandLine.getOptions().contains(rootOption));
    }

    @Test
    public void testGetOptionTriggers() {
        assertTrue(commandLine.getOptionTriggers().contains(rootOption.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        Argument argument = new Argument("a", "arg", "Test argument", false, false, false, 0, null);
        List<Object> defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(argument, defaults);
        assertEquals(defaults, commandLine.getValues(argument, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("s", "switch", "Test switch", false, false, 0, null);
        commandLine.setDefaultSwitch(option, true);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(arguments, commandLine.getNormalised());
    }
}