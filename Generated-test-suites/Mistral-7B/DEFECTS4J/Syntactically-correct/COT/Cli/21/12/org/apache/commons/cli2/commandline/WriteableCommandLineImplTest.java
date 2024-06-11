import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("o", "output", false, "Output flag");
        writeableCommandLine.addOption(option);

        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("o", "output", false, "Output flag");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "outputFile.txt");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("outputFile.txt", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("v", "verbose", true, "Verbose output");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("v", "verbose", true, "Verbose output");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("o", "output", false, "Output flag");
        writeableCommandLine.addOption(option);

        boolean result = writeableCommandLine.hasOption(option);
        Assert.assertTrue(result);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("o", "output", false, "Output flag");
        writeableCommandLine.addOption(option);

        Option result = writeableCommandLine.getOption("o");
        Assert.assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("o", "output", false, "Output flag");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "outputFile.txt");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("outputFile.txt", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("o", "output", false, "Output flag");
        writeableCommandLine.addOption(option);

        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("v", "verbose", true, "Verbose output");
        writeableCommandLine.addOption(option);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertFalse(switchValue);

        writeableCommandLine.addSwitch(option, true);

        switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSwitchAlreadySet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("v", "verbose", true, "Verbose output");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);

        writeableCommandLine.getSwitch(option, false);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("p", "property", true, "Property option");
        Option propertyOption = new PropertyOption("property", "Property value", false, "Property description");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(propertyOption);

        writeableCommandLine.addProperty(option, propertyOption, "testProperty");

        String result = writeableCommandLine.getProperty("property");
        Assert.assertEquals("testProperty", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPropertyUnknownOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("p", "property", true, "Property option");
        writeableCommandLine.addOption(option);

        writeableCommandLine.getProperty("unknownProperty");
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("p", "property", true, "Property option");
        Option propertyOption = new PropertyOption("property", "Property value", false, "Property description");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(propertyOption);

        writeableCommandLine.addProperty(option, propertyOption, "testProperty1");
        writeableCommandLine.addProperty(option, propertyOption, "testProperty2");

        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("property", "testProperty1");
        expectedProperties.put("property", "testProperty2");

        Map<String, String> resultProperties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(expectedProperties, resultProperties);
    }

    @Test
    public void testGetPropertiesUnknownOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Map<String, String> resultProperties = writeableCommandLine.getProperties(new Option("unknownOption", "unknownOption", false, "Unknown option description"));
        Assert.assertEquals(Collections.emptyMap(), resultProperties);
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        String[] arguments = new String[]{"arg1", "arg2"};

        writeableCommandLine.setNormalised(new ArrayList<>(List.of(arguments)));

        String result = writeableCommandLine.toString();
        String expected = String.join(" ", arguments);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option1 = new Option("o", "output", false, "Output flag");
        Option option2 = new Option("v", "verbose", true, "Verbose output");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List options = writeableCommandLine.getOptions();
        List expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);

        Assert.assertEquals(expectedOptions, options);
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option1 = new Option("o", "output", false, "Output flag");
        Option option2 = new Option("v", "verbose", true, "Verbose output");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Set expectedOptionTriggers = new HashSet<>();
        expectedOptionTriggers.add("o");
        expectedOptionTriggers.add("v");

        Assert.assertEquals(expectedOptionTriggers, optionTriggers);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("o", "output", false, "Output flag");
        writeableCommandLine.addOption(option);

        List defaults = new ArrayList<>();
        defaults.add("outputFile1.txt");
        defaults.add("outputFile2.txt");

        writeableCommandLine.setDefaultValues(option, defaults);

        List values = writeableCommandLine.getValues(option, null);
        List expectedValues = new ArrayList<>();
        expectedValues.add("outputFile1.txt");
        expectedValues.add("outputFile2.txt");

        Assert.assertEquals(expectedValues, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("v", "verbose", true, "Verbose output");
        writeableCommandLine.addOption(option);

        writeableCommandLine.setDefaultSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testSetDefaultSwitchNull() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("v", "verbose", true, "Verbose output");
        writeableCommandLine.addOption(option);

        writeableCommandLine.setDefaultSwitch(option, null);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        String[] arguments = new String[]{"arg1", "arg2"};

        writeableCommandLine.setNormalised(new ArrayList<>(List.of(arguments)));

        List normalised = writeableCommandLine.getNormalised();
        List expectedNormalised = new ArrayList<>(List.of(arguments));

        Assert.assertEquals(expectedNormalised, normalised);
    }
}