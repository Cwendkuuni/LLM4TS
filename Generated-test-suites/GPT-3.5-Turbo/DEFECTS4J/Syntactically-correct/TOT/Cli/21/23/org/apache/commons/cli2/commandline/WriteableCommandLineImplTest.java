
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.SwitchBuilder;
import org.apache.commons.cli2.util.ReflectionComparator;

import java.util.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        // Creating test arguments
        Option rootOption = new DefaultOptionBuilder()
                .withLongName("rootOption")
                .withDescription("Root option")
                .create();

        Option argOption = new ArgumentBuilder()
                .withName("argOption")
                .create();

        Option switchOption = new SwitchBuilder()
                .withLongName("switchOption")
                .create();

        List arguments = new ArrayList<>();
        arguments.add("test1");
        arguments.add("argOptionValue");

        // Create new WriteableCommandLineImpl instance for testing
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new DefaultOptionBuilder().withDescription("Test option").create();

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option argument = new ArgumentBuilder().withName("argument").create();

        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");

        List expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");

        Assert.assertEquals(expectedValues, commandLine.getUndefaultedValues(argument));
    }

    @Test
    public void testAddSwitch() {
        Option option = new SwitchBuilder().withLongName("switch").create();

        commandLine.addSwitch(option, true);

        Assert.assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new DefaultOptionBuilder().withLongName("option").create();

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new DefaultOptionBuilder().withLongName("testOption").create();

        commandLine.addOption(option);

        Assert.assertEquals(option, commandLine.getOption("testOption"));
    }

    @Test
    public void testGetValues_withDefaultValues() {
        Option argument = new ArgumentBuilder().withName("argument").create();
        List defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        commandLine.setDefaultValues(argument, defaultValues);
        commandLine.addValue(argument, "value1");

        List expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("defaultValue1");
        expectedValues.add("defaultValue2");

        Assert.assertEquals(expectedValues, commandLine.getValues(argument, Collections.EMPTY_LIST));
    }

    @Test
    public void testGetValues_withoutDefaultValues() {
        Option argument = new ArgumentBuilder().withName("argument").create();

        commandLine.addValue(argument, "value1");

        List expectedValues = new ArrayList<>();
        expectedValues.add("value1");

        Assert.assertEquals(expectedValues, commandLine.getValues(argument, Collections.EMPTY_LIST));
    }

    @Test
    public void testGetValues_withEmptyValuesAndDefaultValues() {
        Option argument = new ArgumentBuilder().withName("argument").create();
        List defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");

        commandLine.setDefaultValues(argument, defaultValues);

        List expectedValues = new ArrayList<>();
        expectedValues.add("defaultValue1");

        Assert.assertEquals(expectedValues, commandLine.getValues(argument, Collections.EMPTY_LIST));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option argument = new ArgumentBuilder().withName("argument").create();

        commandLine.addValue(argument, "value1");

        List expectedValues = new ArrayList<>();
        expectedValues.add("value1");

        Assert.assertEquals(expectedValues, commandLine.getUndefaultedValues(argument));
    }

    @Test
    public void testGetSwitch() {
        Option option = new SwitchBuilder().withLongName("switch").create();

        commandLine.addSwitch(option, true);

        Assert.assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetSwitch_withDefaultValue() {
        Option option = new SwitchBuilder().withLongName("switch").create();
        boolean defaultValue = true;

        Assert.assertTrue(commandLine.getSwitch(option, defaultValue));
    }

    @Test
    public void testGetProperty() {
        String property = "propertyName";
        String value = "propertyValue";

        commandLine.addProperty(property, value);

        Assert.assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        Option option = new DefaultOptionBuilder().withDescription("Test option").create();
        String property = "propertyName";
        String value = "propertyValue";

        commandLine.addProperty(option, property, value);

        Assert.assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetProperties() {
        Option option = new DefaultOptionBuilder().withDescription("Test option").create();
        String property = "propertyName";
        String value = "propertyValue";

        commandLine.addProperty(option, property, value);

        Set<String> expectedProperties = new HashSet<>();
        expectedProperties.add(property);

        Assert.assertEquals(expectedProperties, commandLine.getProperties(option));
    }

    @Test
    public void testGetProperties_withoutProperties() {
        Option option = new DefaultOptionBuilder().withDescription("Test option").create();

        Set<String> expectedProperties = new HashSet<>();

        Assert.assertEquals(expectedProperties, commandLine.getProperties(option));
    }

    @Test
    public void testGetProperties_withoutOption() {
        Set<String> expectedProperties = new HashSet<>();

        Assert.assertEquals(expectedProperties, commandLine.getProperties());
    }

    @Test
    public void testLooksLikeOption_withPrefix() {
        String trigger = "--option";

        boolean result = commandLine.looksLikeOption(trigger);

        Assert.assertTrue(result);
    }

    @Test
    public void testLooksLikeOption_withoutPrefix() {
        String trigger = "option";

        boolean result = commandLine.looksLikeOption(trigger);

        Assert.assertFalse(result);
    }

    @Test
    public void testToString() {
        String expectedOutput = "test1 argOptionValue";

        Assert.assertEquals(expectedOutput, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new DefaultOptionBuilder().withDescription("Test option").create();
        commandLine.addOption(option);

        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option);

        Assert.assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new DefaultOptionBuilder().withLongName("testOption").create();
        commandLine.addOption(option);

        Set<String> expectedTriggers = new HashSet<>();
        expectedTriggers.add("testOption");

        Assert.assertEquals(expectedTriggers, commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues_withDefaults() {
        Option option = new DefaultOptionBuilder().withDescription("Test option").create();
        List<String> defaults = new ArrayList<>();
        defaults.add("defaultValue1");
        defaults.add("defaultValue2");

        commandLine.setDefaultValues(option, defaults);

        Assert.assertEquals(defaults, commandLine.getValues(option, Collections.EMPTY_LIST));
    }

    @Test
    public void testSetDefaultValues_withoutDefaults() {
        Option option = new DefaultOptionBuilder().withDescription("Test option").create();

        commandLine.setDefaultValues(option, null);

        Assert.assertEquals(Collections.EMPTY_LIST, commandLine.getValues(option, Collections.EMPTY_LIST));
    }

    @Test
    public void testSetDefaultSwitch_withDefaultSwitch() {
        Option option = new DefaultOptionBuilder().withDescription("Test option").create();

        commandLine.setDefaultSwitch(option, true);

        Assert.assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testSetDefaultSwitch_withoutDefaultSwitch() {
        Option option = new DefaultOptionBuilder().withDescription("Test option").create();

        commandLine.setDefaultSwitch(option, null);

        Assert.assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        List<String> expectedNormalised = new ArrayList<>();
        expectedNormalised.add("test1");
        expectedNormalised.add("argOptionValue");

        Assert.assertEquals(expectedNormalised, commandLine.getNormalised());
    }

    @Test
    public void testToString_withArgumentsContainingSpace() {
        Option rootOption = new DefaultOptionBuilder()
                .withLongName("rootOption")
                .withDescription("Root option")
                .create();

        Option argOption = new ArgumentBuilder()
                .withName("argOption")
                .create();

        Option switchOption = new SwitchBuilder()
                .withLongName("switchOption")
                .create();

        List arguments = new ArrayList<>();
        arguments.add("test 1");
        arguments.add("argOptionValue");

        commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        String expectedOutput = "\"test 1\" argOptionValue";

        Assert.assertEquals(expectedOutput, commandLine.toString());
    }
}

