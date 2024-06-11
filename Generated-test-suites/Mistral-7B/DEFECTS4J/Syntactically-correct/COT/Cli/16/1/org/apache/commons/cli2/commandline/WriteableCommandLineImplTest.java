import org.apache.commons.cli2.CommandLine;
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
        Option option = Option.builder("o").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("s").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = Option.builder("s").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = Option.builder("o").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = Option.builder("o").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Option returnedOption = writeableCommandLine.getOption("o");
        Assert.assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("o").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("o").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        Option option = Option.builder("s").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = Option.builder("p").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", "value");
        String propertyValue = writeableCommandLine.getProperty(option, "property");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        Option option = Option.builder("p").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Properties properties = new Properties();
        properties.put("property1", "value1");
        properties.put("property2", "value2");
        writeableCommandLine.addProperty(option, properties);
        Set propertiesSet = writeableCommandLine.getProperties(option);
        Assert.assertEquals(2, propertiesSet.size());
    }

    @Test
    public void testToString() {
        Option option1 = Option.builder("o1").required(false).build();
        Option option2 = Option.builder("o2").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        writeableCommandLine.addValue(option1, "value1");
        writeableCommandLine.addValue(option2, "value2");
        String commandLineString = writeableCommandLine.toString();
        String expectedCommandLineString = "o1 value1 o2 value2";
        Assert.assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = Option.builder("o1").required(false).build();
        Option option2 = Option.builder("o2").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List optionsList = writeableCommandLine.getOptions();
        List expectedOptionsList = new ArrayList();
        expectedOptionsList.add(option1);
        expectedOptionsList.add(option2);
        Assert.assertEquals(expectedOptionsList, optionsList);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = Option.builder("o1").required(false).build();
        Option option2 = Option.builder("o2").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggersSet = writeableCommandLine.getOptionTriggers();
        Set expectedOptionTriggersSet = new HashSet<>();
        expectedOptionTriggersSet.add("o1");
        expectedOptionTriggersSet.add("o2");
        Assert.assertEquals(expectedOptionTriggersSet, optionTriggersSet);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("o").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("s").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.setDefaultSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option option1 = Option.builder("o1").required(false).build();
        Option option2 = Option.builder("o2").required(false).build();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        writeableCommandLine.addValue(option1, "value1");
        writeableCommandLine.addValue(option2, "value2");
        List normalisedList = writeableCommandLine.getNormalised();
        List expectedNormalisedList = new ArrayList();
        expectedNormalisedList.add("value1");
        expectedNormalisedList.add("value2");
        Assert.assertEquals(expectedNormalisedList, normalisedList);
    }
}