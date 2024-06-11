import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("b", "b", false, "B");
        commandLine.addOption(option);
        List options = commandLine.getOptions();
        Option actualOption = (Option) options.get(0);
        Assert.assertEquals("b", actualOption.getLongOpt());
        Assert.assertEquals("B", actualOption.getOpt());
        Assert.assertFalse(actualOption.isValueSeparator());
        Assert.assertEquals(false, actualOption.isRequired());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals("value", values.get(0));
        Assert.assertEquals(1, values.size());
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a");
        commandLine.addOption(option);
        boolean hasOption = commandLine.hasOption(option);
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a");
        commandLine.addOption(option);
        Option actualOption = commandLine.getOption("a");
        Assert.assertEquals(option, actualOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addProperty("property", "value");
        String propertyValue = commandLine.getProperty("property", null);
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        Set properties = commandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.containsAll(Arrays.asList("property1", "property2")));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a", "a"));
        commandLine.addOption(new Option("b", "b"));
        String commandLineString = commandLine.toString();
        Assert.assertEquals("a a b", commandLineString);
    }

    @Test
    public void testGetListNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a", "", true));
        commandLine.addOption(new Option("b", "", true, "B"));
        commandLine.addOption(new Option("c", "c", true, "C"));
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(3, normalised.size());
        Assert.assertEquals("B", normalised.get(1));
        Assert.assertEquals("C", normalised.get(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetListOptionsModify() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        List options = commandLine.getOptions();
        options.add(new Option("a", "a"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapNameToOptionModify() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Map nameToOption = commandLine.getNameToOption();
        nameToOption.put("a", new Option("a", "a"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapValuesModify() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Map values = commandLine.getValues();
        List valueList = new ArrayList();
        values.put(new Option("a", "a"), valueList);
        valueList.add("value");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapSwitchesModify() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Map switches = commandLine.getSwitches();
        switches.put(new Option("a", "a"), Boolean.TRUE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapDefaultValuesModify() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Map defaultValues = commandLine.getDefaultValues();
        defaultValues.put(new Option("a", "a"), new ArrayList<>());
        List defaultValuesList = (List) defaultValues.get(new Option("a", "a"));
        defaultValuesList.add("defaultValue");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapDefaultSwitchesModify() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Map defaultSwitches = commandLine.getDefaultSwitches();
        defaultSwitches.put(new Option("a", "a"), Boolean.TRUE);
    }
}