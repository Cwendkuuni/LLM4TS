import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List valueList = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, valueList.size());
        Assert.assertEquals("value1", valueList.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySetSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Option optionFromWriteableCommandLine = writeableCommandLine.getOption("o");
        Assert.assertNotNull(optionFromWriteableCommandLine);
        Assert.assertEquals(option, optionFromWriteableCommandLine);
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List valueList = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, valueList.size());
        Assert.assertEquals("value1", valueList.get(0));
        Assert.assertEquals("value2", valueList.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List valueList = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, valueList.size());
        Assert.assertEquals("value1", valueList.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Assert.assertFalse(writeableCommandLine.getSwitch(option, null));
        writeableCommandLine.addSwitch(option, true);
        Assert.assertTrue(writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = Option.builder("p").longOpt("property").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", "value");
        String propertyValue = writeableCommandLine.getProperty(option, "property");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        Option option = Option.builder("p").longOpt("property").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Map<String, String> propertyMap = new HashMap<>();
        propertyMap.put("property1", "value1");
        writeableCommandLine.addProperty(option, propertyMap);
        Map<String, String> propertyMapFromWriteableCommandLine = writeableCommandLine.getProperties(option);
        Assert.assertEquals(1, propertyMapFromWriteableCommandLine.size());
        Assert.assertEquals("value1", propertyMapFromWriteableCommandLine.get("property1"));
    }

    @Test
    public void testToString() {
        Option option = Option.builder("a").longOpt("argument").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "arg1");
        String writeableCommandLineString = writeableCommandLine.toString();
        Assert.assertEquals("arg1", writeableCommandLineString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List optionsList = writeableCommandLine.getOptions();
        Assert.assertEquals(2, optionsList.size());
        Option optionFromOptionsList = (Option) optionsList.get(0);
        Assert.assertEquals(option1, optionFromOptionsList);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggersSet = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggersSet.size());
        Assert.assertTrue(optionTriggersSet.contains("o1"));
        Assert.assertTrue(optionTriggersSet.contains("o2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        List defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultSwitch(option, Boolean.TRUE);
        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option option = Option.builder("a").longOpt("argument").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "arg1");
        List normalisedList = writeableCommandLine.getNormalised();
        Assert.assertEquals(1, normalisedList.size());
        Assert.assertEquals("arg1", normalisedList.get(0));
    }
}