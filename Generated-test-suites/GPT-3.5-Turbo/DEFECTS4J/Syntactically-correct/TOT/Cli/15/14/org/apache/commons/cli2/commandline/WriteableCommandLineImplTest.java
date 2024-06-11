import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.Command;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.builder.CommandBuilder;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    private Option option;
    private CommandLine commandLine;

    @Before
    public void setUp() {
        option = new CommandBuilder().withName("input").withShortName("i").create();

        List args = Arrays.asList("arg1", "arg2");
        commandLine = new WriteableCommandLineImpl(option, args);
    }

    @Test
    public void testAddOption() {
        Option newOption = new CommandBuilder().withName("output").withShortName("o").create();
        commandLine.addOption(newOption);
        assertTrue(commandLine.hasOption(newOption));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "value1");
        assertTrue(commandLine.getValues(option, null).contains("value1"));
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getValues(option, null).size());
    }

    @Test
    public void testAddSwitch() {
        Option switchOption = new CommandBuilder().withName("switch").create();
        commandLine.addSwitch(switchOption, true);
        assertTrue(commandLine.getSwitch(switchOption, false));
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option));
        Option nonExistingOption = new CommandBuilder().withName("output").withShortName("o").create();
        assertFalse(commandLine.hasOption(nonExistingOption));
    }

    @Test
    public void testGetOption() {
        assertEquals(option, commandLine.getOption("input"));
        Option nonExistingOption = new CommandBuilder().withName("output").withShortName("o").create();
        assertNull(commandLine.getOption("output"));
    }

    @Test
    public void testGetValues() {
        Option argumentOption = new Argument();
        commandLine.addOption(argumentOption);
        commandLine.addValue(argumentOption, "value1");
        commandLine.addValue(argumentOption, "value2");

        List defaultValues = Arrays.asList("default1", "default2");
        List values = commandLine.getValues(argumentOption, defaultValues);

        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));

        List undefaultedValues = commandLine.getUndefaultedValues(argumentOption);
        assertEquals(2, undefaultedValues.size());
        assertTrue(undefaultedValues.contains("value1"));
        assertTrue(undefaultedValues.contains("value2"));
    }

    @Test
    public void testGetSwitch() {
        Option switchOption = new CommandBuilder().withName("switch").create();
        assertTrue(commandLine.getSwitch(switchOption, false));
        Option nonExistingOption = new CommandBuilder().withName("output").withShortName("o").create();
        assertNull(commandLine.getSwitch(nonExistingOption, null));
        commandLine.addSwitch(nonExistingOption, true);
        assertTrue(commandLine.getSwitch(nonExistingOption, false));
    }

    @Test
    public void testGetProperty() {
        String property = "key";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testSetDefaultValues() {
        Option defaultOption = new Argument();
        List defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(defaultOption, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(defaultOption, null));
        commandLine.setDefaultValues(defaultOption, null);
        assertNull(commandLine.getValues(defaultOption, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option switchOption = new CommandBuilder().withName("switch").create();
        commandLine.setDefaultSwitch(switchOption, true);
        assertTrue(commandLine.getSwitch(switchOption, false));
        commandLine.setDefaultSwitch(switchOption, null);
        assertNull(commandLine.getSwitch(switchOption, null));
    }

    @Test
    public void testGetNormalised() {
        List args = Arrays.asList("arg1", "arg2");
        assertEquals(args, commandLine.getNormalised());
    }
}