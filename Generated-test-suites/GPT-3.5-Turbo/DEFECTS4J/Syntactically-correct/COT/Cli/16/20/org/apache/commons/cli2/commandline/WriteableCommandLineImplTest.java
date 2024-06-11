import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option rootOption = new Option("root", "Root option");
        commandLine = new WriteableCommandLineImpl(rootOption, null);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test", "Test option");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument("arg", "Argument option");
        commandLine.addValue(argument, "value");

        assertTrue(commandLine.hasOption(argument));
        assertEquals("value", commandLine.getValues(argument, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("switch", "Switch option");
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
        assertEquals(value, commandLine.getProperty(new PropertyOption(), property, null));
    }

    @Test
    public void testGetDefaultValues() {
        Option option = new Option("defaultValues", "Default values option");
        commandLine.setDefaultValues(option, Collections.singletonList("default"));

        assertEquals(Collections.singletonList("default"), commandLine.getValues(option, null));
    }

    @Test
    public void testGetDefaultSwitch() {
        Option option = new Option("defaultSwitch", "Default switch option");
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        commandLine = new WriteableCommandLineImpl(null, Arrays.asList(arg1, arg2));

        assertEquals("arg1 arg2", commandLine.toString());
    }
}