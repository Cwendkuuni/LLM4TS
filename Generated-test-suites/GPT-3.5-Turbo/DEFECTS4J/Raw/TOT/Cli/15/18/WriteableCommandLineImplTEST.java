import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;
    private Option option;
    private Argument argument;

    @Before
    public void setup() {
        option = new OptionBuilder().withPreferredName("option").create();
        argument = new ArgumentBuilder().withName("argument").create();
        List<Argument> arguments = new ArrayList<>();
        arguments.add(argument);
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option newOption = new OptionBuilder().withPreferredName("newOption").create();
        commandLine.addOption(newOption);

        Assert.assertTrue(commandLine.hasOption(newOption));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(argument, "value");

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value");

        Assert.assertEquals(expectedValues, commandLine.getUndefaultedValues(argument));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);

        boolean expectedValue = true;

        Assert.assertEquals(expectedValue, commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Assert.assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Assert.assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue");

        commandLine.setDefaultValues(argument, defaultValues);
        commandLine.addValue(argument, "value");

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value");

        Assert.assertEquals(expectedValues, commandLine.getValues(argument, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(argument, "value");

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value");

        Assert.assertEquals(expectedValues, commandLine.getUndefaultedValues(argument));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, true);

        boolean expectedValue = true;

        Assert.assertEquals(expectedValue, commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";

        commandLine.addProperty(option, property, value);

        Assert.assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";

        commandLine.addProperty(option, property, value);

        Assert.assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetProperties() {
        String property = "property";
        String value = "value";

        commandLine.addProperty(option, property, value);

        Set<String> expectedProperties = Set.of(property);

        Assert.assertEquals(expectedProperties, commandLine.getProperties(option));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(commandLine.looksLikeOption("-option"));
    }

    @Test
    public void testToString() {
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);
        
        Assert.assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option);

        Assert.assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Set<String> expectedTriggers = Set.of("option");

        Assert.assertEquals(expectedTriggers, commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue");

        commandLine.setDefaultValues(option, defaultValues);

        Assert.assertEquals(defaultValues, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);

        boolean expectedValue = true;

        Assert.assertEquals(expectedValue, commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        List<String> expectedNormalised = new ArrayList<>();
        expectedNormalised.add("argument");

        Assert.assertEquals(expectedNormalised, commandLine.getNormalised());
    }
}