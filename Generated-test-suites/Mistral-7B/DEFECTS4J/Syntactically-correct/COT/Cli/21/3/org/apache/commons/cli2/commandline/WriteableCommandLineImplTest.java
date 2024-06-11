import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {
    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test option");
        cmdLine.addOption(option);

        List options = cmdLine.getOptions();
        Assert.assertEquals(1, options.size());
        Option optionFromCmdLine = (Option) options.get(0);
        Assert.assertEquals(option.getPreferredName(), optionFromCmdLine.getPreferredName());
        Assert.assertEquals(option.isRequired(), optionFromCmdLine.isRequired());
        Assert.assertEquals(option.getLongOpt(), optionFromCmdLine.getLongOpt());
        Assert.assertEquals(option.getDescription(), optionFromCmdLine.getDescription());

        Option optionTrigger = cmdLine.getOption("t");
        Assert.assertEquals(option, optionTrigger);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("i", "input", 1, "Input file");
        cmdLine.addOption(option);

        cmdLine.addValue(option, "test.txt");
        List values = cmdLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test.txt", values.get(0));

        cmdLine.addValue(option, "another.txt");
        values = cmdLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test.txt", values.get(0));
        Assert.assertEquals("another.txt", values.get(1));

        List defaultValues = cmdLine.getDefaultValues(option);
        Assert.assertNull(defaultValues);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("v", "verbose", false, "Verbose mode");
        cmdLine.addOption(option);

        cmdLine.addSwitch(option, true);
        Boolean switchValue = cmdLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        cmdLine.addSwitch(option, false);
        switchValue = cmdLine.getSwitch(option, true);
        Assert.assertFalse(switchValue);

        Map defaultSwitches = cmdLine.getDefaultSwitches();
        Assert.assertNull(defaultSwitches);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("v", "verbose", false, "Verbose mode");
        cmdLine.addOption(option);

        cmdLine.addSwitch(option, true);
        cmdLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test option");
        cmdLine.addOption(option);

        boolean hasOption = cmdLine.hasOption(option);
        Assert.assertTrue(hasOption);

        Option otherOption = new Option("other", "other", false, "Other option");
        hasOption = cmdLine.hasOption(otherOption);
        Assert.assertFalse(hasOption);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("p", "property", 1, "Property option");
        cmdLine.addOption(option);

        cmdLine.addProperty(option, "property.name", "property.value");
        String propertyValue = cmdLine.getProperty(option, "property.name", null);
        Assert.assertEquals("property.value", propertyValue);

        Assert.assertNull(cmdLine.getProperty(option, "other.name", null));
        Map properties = cmdLine.getProperties(option);
        Assert.assertEquals("property.name", properties.keySet().iterator().next());
        Assert.assertEquals("property.value", properties.get("property.name"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("p", "property", 1, "Property option");
        cmdLine.addOption(option);

        cmdLine.addProperty(option, "property.name1", "property.value1");
        cmdLine.addProperty(option, "property.name2", "property.value2");

        Map properties = cmdLine.getProperties(option);
        Set propertyNames = properties.keySet();
        List propertyValues = new ArrayList(properties.values());

        Assert.assertEquals(2, propertyNames.size());
        Assert.assertEquals("property.name1", propertyNames.iterator().next());
        Assert.assertEquals("property.name2", propertyNames.iterator().next());

        Assert.assertEquals(2, propertyValues.size());
        Assert.assertEquals("property.value1", propertyValues.get(0));
        Assert.assertEquals("property.value2", propertyValues.get(1));

        Assert.assertEquals(Collections.unmodifiableMap(cmdLine.getProperties()), properties);
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option1 = new Option("i", "input1", 1, "Input file 1");
        Option option2 = new Option("i", "input2", 1, "Input file 2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        cmdLine.addValue(option1, "input1.txt");
        cmdLine.addValue(option2, "input2.txt");

        String cmdLineString = cmdLine.toString();
        String expectedCmdLineString = "input1.txt input2.txt";
        Assert.assertEquals(expectedCmdLineString, cmdLineString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option1 = new Option("i", "input1", 1, "Input file 1");
        Option option2 = new Option("i", "input2", 1, "Input file 2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        List options = cmdLine.getOptions();
        Assert.assertEquals(2, options.size());
        Option option1FromList = (Option) options.get(0);
        Assert.assertEquals(option1, option1FromList);
        Option option2FromList = (Option) options.get(1);
        Assert.assertEquals(option2, option2FromList);
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option1 = new Option("i", "input1", 1, "Input file 1");
        Option option2 = new Option("i", "input2", 1, "Input file 2");
        Option option3 = new Option("o", "output", 1, "Output file");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        cmdLine.addOption(option3);

        Set optionTriggers = cmdLine.getOptionTriggers();
        Assert.assertEquals(3, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("i"));
        Assert.assertTrue(optionTriggers.contains("o"));
    }
}