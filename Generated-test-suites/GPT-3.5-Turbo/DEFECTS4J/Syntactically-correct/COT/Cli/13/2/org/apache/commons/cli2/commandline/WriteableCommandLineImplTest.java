import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        commandLine.addOption(option);
        // Verify that the option is added to the command line
        assert(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Argument();
        Object value = "testValue";
        commandLine.addValue(option, value);
        // Verify that the value is added to the option in the command line
        assert(commandLine.getValues(option, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        boolean value = true;
        commandLine.addSwitch(option, value);
        // Verify that the switch is added to the option in the command line
        assert(commandLine.getSwitch(option, null) == value);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        commandLine.addOption(option);
        // Verify that the command line has the option
        assert(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option().withPreferredName("test");
        commandLine.addOption(option);
        // Verify that the command line returns the correct option for the trigger
        assert(commandLine.getOption("test") == option);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Argument();
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        // Verify that the command line returns the correct values for the option
        assert(commandLine.getValues(option, null).equals(values));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        boolean value = true;
        commandLine.addSwitch(option, value);
        // Verify that the command line returns the correct switch value for the option
        assert(commandLine.getSwitch(option, null) == value);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        // Verify that the property is added to the command line
        assert(commandLine.getProperty(property, null).equals(value));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        String trigger = "-test";
        // Verify that the command line recognizes the trigger as an option
        assert(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        commandLine.getNormalised().add("arg1");
        commandLine.getNormalised().add("arg2");
        String expected = "arg1 arg2";
        // Verify that the command line's string representation is correct
        assert(commandLine.toString().equals(expected));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        // Verify that the command line's default values are set correctly for the option
        assert(commandLine.getValues(option, null).equals(defaults));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        // Verify that the command line's default switch value is set correctly for the option
        assert(commandLine.getSwitch(option, null) == defaultSwitch);
    }

}