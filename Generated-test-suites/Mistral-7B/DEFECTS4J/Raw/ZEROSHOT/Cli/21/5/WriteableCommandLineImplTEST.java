import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        // Test adding an option
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());

        // Test adding a value to an option
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        // Test adding a switch to an option
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        // Test getting an option
        Option getOption = commandLine.getOption("o");
        Assert.assertEquals(option, getOption);

        // Test getting values for an option
        List optionValues = commandLine.getValues(option, null);
        Assert.assertEquals(1, optionValues.size());
        Assert.assertEquals("value1", optionValues.get(0));

        // Test getting a switch value for an option
        Boolean optionSwitch = commandLine.getSwitch(option, false);
        Assert.assertTrue(optionSwitch);

        // Test getting undefined values for an option
        List undefinedValues = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, undefinedValues.size());

        // Test getting properties for an option
        commandLine.addProperty(option, "property1", "value1");
        Map properties = commandLine.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value1", properties.get("property1"));

        // Test getting all properties
        Map allProperties = commandLine.getProperties();
        Assert.assertTrue(allProperties.isEmpty());

        // Test looking like an option
        String trigger = "o--option";
        Assert.assertTrue(commandLine.looksLikeOption(trigger));

        // Test getting the toString representation
        String toString = commandLine.toString();
        Assert.assertEquals("value1 option", toString);

        // Test getting the options list
        List optionsList = commandLine.getOptions();
        Assert.assertEquals(1, optionsList.size());
        Option optionList = (Option) optionsList.get(0);
        Assert.assertEquals("o", optionList.getLongOpt());
        Assert.assertEquals("option", optionList.getOptString());
        Assert.assertTrue(optionList.isRequired());
        Assert.assertEquals(true, optionList.isValueSeparatorAllowed());
        Assert.assertEquals("an option", optionList.getDesc());
        Assert.assertEquals(Collections.EMPTY_LIST, optionList.getValues());
        Assert.assertEquals(0, optionList.getMaxArgValues());
        Assert.assertEquals(false, optionList.isIndefiniteValue());
        Assert.assertEquals(false, optionList.isOptionalArg());
        Assert.assertEquals(Collections.EMPTY_SET, optionList.getOptions());
        Assert.assertEquals(Collections.EMPTY_SET, optionList.getOptionTriggers());
        Assert.assertEquals(null, optionList.getDefaultValue());
        Assert.assertEquals(false, optionList.getDefaultValueSet());
        Assert.assertEquals(null, optionList.getDefaultSwitch());
        Assert.assertEquals(null, optionList.getDefaultSwitches());

        // Test getting the normalised list
        List normalisedList = commandLine.getNormalised();
        Assert.assertEquals(1, normalisedList.size());
        Assert.assertEquals("value1 option", normalisedList.get(0).toString());

        // Test setting default values for an option
        List defaultValues = new ArrayList();
        defaultValues.add("default1");
        commandLine.setDefaultValues(option, defaultValues);
        List defaultValuesList = commandLine.getValues(option, null);
        Assert.assertEquals(1, defaultValuesList.size());
        Assert.assertEquals("default1", defaultValuesList.get(0));

        // Test setting a default switch for an option
        commandLine.setDefaultSwitch(option, Boolean.FALSE);
        Boolean defaultSwitch = commandLine.getSwitch(option, Boolean.TRUE);
        Assert.assertFalse(defaultSwitch);
    }
}