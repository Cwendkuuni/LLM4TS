package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
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
    public void testWriteableCommandLineImpl() {
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("o", "option2", true, "Option 2");
        Option option3 = new Argument("a", "argument", true, "Argument");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.singletonList(option2));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
        commandLine.addValue(option2, "value2");
        commandLine.addSwitch(option1, true);

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(2, commandLine.getOptionTriggers().size());
        Assert.assertEquals(3, commandLine.getNormalised().size());

        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertTrue(commandLine.hasOption(option2));
        Assert.assertFalse(commandLine.hasOption(new Option("nonexistent", "nonexistent", false, "Nonexistent option")));

        Option optionFromMap = commandLine.getOption("o");
        Assert.assertEquals(option1, optionFromMap);

        List values = commandLine.getValues(option2, Collections.emptyList());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value2", values.get(0));

        Boolean switchValue = commandLine.getSwitch(option1, false);
        Assert.assertTrue(switchValue);

        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        Properties properties = commandLine.getProperties();
        Set propertyNames = properties.stringPropertyNames();
        Assert.assertEquals(2, propertyNames.size());
        Assert.assertEquals("property1", propertyNames.iterator().next());
        Assert.assertEquals("property2", propertyNames.iterator().next());
        Assert.assertEquals("value1", properties.getProperty("property1"));
        Assert.assertEquals("value2", properties.getProperty("property2"));

        Assert.assertTrue(commandLine.looksLikeOption("o--option1"));
        Assert.assertFalse(commandLine.looksLikeOption("--nonexistent"));

        Assert.assertEquals("option1 true option2 value2 argument", commandLine.toString());
    }
}