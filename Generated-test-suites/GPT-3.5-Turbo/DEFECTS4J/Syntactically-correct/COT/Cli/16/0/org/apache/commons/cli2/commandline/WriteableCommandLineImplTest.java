import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("r", "root");
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("t", "test");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("t", "test");
        Object value = "value";
        commandLine.addValue(option, value);
        Assert.assertTrue(commandLine.getValues(option, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch");
        boolean value = true;
        commandLine.addSwitch(option, value);
        Assert.assertEquals(value, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("t", "test");
        Assert.assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("t", "test");
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("t"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("t", "test");
        Object value = "value";
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.addValue(option, value);
        Assert.assertEquals(value, commandLine.getValues(option, defaultValues).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("t", "test");
        Object value = "value";
        commandLine.addValue(option, value);
        Assert.assertEquals(value, commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("s", "switch");
        boolean defaultValue = true;
        Assert.assertEquals(defaultValue, commandLine.getSwitch(option, defaultValue));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        Assert.assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property");
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        Assert.assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property");
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        Assert.assertEquals(property, commandLine.getProperties(option).iterator().next());
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-t";
        Assert.assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        List<String> arguments = new ArrayList<>();
        arguments.add(arg1);
        arguments.add(arg2);
        commandLine = new WriteableCommandLineImpl(null, arguments);
        Assert.assertEquals(arg1 + " " + arg2, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("t", "test");
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("t", "test");
        commandLine.addOption(option);
        Assert.assertEquals(option.getPreferredName(), commandLine.getOptionTriggers().iterator().next());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("t", "test");
        List<Object> defaults = new ArrayList<>();
        defaults.add("default");
        commandLine.setDefaultValues(option, defaults);
        List<Object> values = commandLine.getValues(option, null);
        for (Object defaultValue : defaults) {
            Assert.assertTrue(values.contains(defaultValue));
        }
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("t", "test");
        boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Assert.assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        String arg = "arg";
        List<String> arguments = new ArrayList<>();
        arguments.add(arg);
        commandLine = new WriteableCommandLineImpl(null, arguments);
        Assert.assertEquals(arg, commandLine.getNormalised().get(0));
    }

}