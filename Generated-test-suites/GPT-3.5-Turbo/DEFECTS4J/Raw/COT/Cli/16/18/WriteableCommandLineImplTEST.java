
import org.junit.Test;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.CommandLineImpl;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.group.CommandLineImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Test adding an option to the WriteableCommandLineImpl
        Option option = new Option("opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        commandLine.addOption(option);

        assert(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        // Test adding a value to the WriteableCommandLineImpl
        Option option = new Option("opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        String value = "test";
        commandLine.addValue(option, value);

        assert(commandLine.getValues(option, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        // Test adding a switch to the WriteableCommandLineImpl
        Option option = new Option("opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        boolean value = true;
        commandLine.addSwitch(option, value);

        assert(commandLine.getSwitch(option, false) == value);
    }

    @Test
    public void testHasOption() {
        // Test checking existence of an option in the WriteableCommandLineImpl
        Option option = new Option("opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        commandLine.addOption(option);

        assert(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        // Test getting an option from the WriteableCommandLineImpl by trigger
        Option option = new Option("opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        commandLine.addOption(option);

        assert(commandLine.getOption("opt") == option);
    }

    @Test
    public void testGetValues() {
        // Test getting values for an option from the WriteableCommandLineImpl
        Option option = new Option("opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(value1);
        expectedValues.add(value2);

        assert(commandLine.getValues(option, null).equals(expectedValues));
    }

    @Test
    public void testGetUndefaultedValues() {
        // Test getting undefaulted values for an option from the WriteableCommandLineImpl
        Option option = new Option("opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(value1);
        expectedValues.add(value2);

        assert(commandLine.getUndefaultedValues(option).equals(expectedValues));
    }

    @Test
    public void testGetSwitch() {
        // Test getting a switch for an option from the WriteableCommandLineImpl
        Option option = new Option("opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        boolean value = true;
        commandLine.addSwitch(option, value);

        assert(commandLine.getSwitch(option, false) == value);
    }

    @Test
    public void testGetProperty() {
        // Test getting a property from the WriteableCommandLineImpl by name
        Option option = new Option("opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        String property = "prop";
        String value = "value";
        commandLine.addProperty(option, property, value);

        assert(commandLine.getProperty(option, property, null).equals(value));
    }

    @Test
    public void testAddProperty() {
        // Test adding a property to the WriteableCommandLineImpl
        Option option = new Option("opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        String property = "prop";
        String value = "value";
        commandLine.addProperty(option, property, value);

        Set<String> properties = commandLine.getProperties(option);
        assert(properties.contains(property));
        assert(commandLine.getProperty(option, property, null).equals(value));
    }

    @Test
    public void testLooksLikeOption() {
        // Test checking if a trigger looks like an option in the WriteableCommandLineImpl
        Option option = new Option("opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);

        assert(commandLine.looksLikeOption("-opt"));
        assert(!commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        // Test converting the WriteableCommandLineImpl to a string
        Option option = new Option("opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        String value = "value";
        commandLine.addValue(option, value);

        assert(commandLine.toString().equals("value"));
    }

    @Test
    public void testSetDefaultValues() {
        // Test setting default values for an option in the WriteableCommandLineImpl
        Option option = new Option("opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        String defaultValue = "default";
        List<String> defaults = new ArrayList<>();
        defaults.add(defaultValue);
        commandLine.setDefaultValues(option, defaults);

        assert(commandLine.getValues(option, null).equals(defaults));
    }

    @Test
    public void testSetDefaultSwitch() {
        // Test setting a default switch for an option in the WriteableCommandLineImpl
        Option option = new Option("opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        assert(commandLine.getSwitch(option, false) == defaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        // Test getting the normalised arguments from the WriteableCommandLineImpl
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        Option option = new Option("opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        assert(commandLine.getNormalised().equals(arguments));
    }
}
