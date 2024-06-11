import org.apache.commons.cli2.CommandLine;
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
        // given
        Option rootOption = new Option("t", "test", true, "test description");
        List arguments = new ArrayList();
        arguments.add("testValue");
        arguments.add("--test");
        arguments.add("--no-log");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // when
        Option option1 = new Option("n", "noLog", false, "no log description");
        commandLine.addOption(option1);
        commandLine.addSwitch(option1, true);

        Option option2 = new Option("l", "log", false, "log description");
        commandLine.addOption(option2);
        commandLine.addSwitch(option2, false);

        Argument arg1 = new Argument("testValue");
        commandLine.addOption(arg1);

        String propertyKey = "testProperty";
        String propertyValue = "testPropertyValue";
        commandLine.addProperty(option1, propertyKey, propertyValue);

        // then
        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertTrue(commandLine.getSwitch(option1, false));
        List values = commandLine.getValues(option1, null);
        Assert.assertEquals(0, values.size());

        Assert.assertFalse(commandLine.hasOption(option2));
        Assert.assertFalse(commandLine.getSwitch(option2, false));
        values = commandLine.getValues(option2, null);
        Assert.assertEquals(0, values.size());

        Assert.assertFalse(commandLine.hasOption(arg1));
        List argValues = commandLine.getValues(arg1, null);
        Assert.assertEquals(1, argValues.size());
        Assert.assertEquals("testValue", argValues.get(0));

        Properties properties = commandLine.getProperties(option1);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals(propertyValue, properties.getProperty(propertyKey));

        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(3, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("t"));
        Assert.assertTrue(optionTriggers.contains("n"));
        Assert.assertTrue(optionTriggers.contains("l"));

        List options = commandLine.getOptions();
        Assert.assertEquals(3, options.size());
        Option option = (Option) options.get(0);
        Assert.assertEquals(rootOption, option);

        option = (Option) options.get(1);
        Assert.assertEquals(option1, option);

        option = (Option) options.get(2);
        Assert.assertEquals(option2, option);

        Map nameToOptionMap = commandLine.getNameToOption();
        Assert.assertEquals(3, nameToOptionMap.size());
        Assert.assertEquals(option1, nameToOptionMap.get("n"));
        Assert.assertEquals(option2, nameToOptionMap.get("l"));
        Assert.assertEquals(arg1, nameToOptionMap.get("testValue"));

        List normalised = commandLine.getNormalised();
        Assert.assertEquals(3, normalised.size());
        Assert.assertEquals("--test", normalised.get(0).toString());
        Assert.assertEquals("--no-log", normalised.get(1).toString());
        Assert.assertEquals("testValue", normalised.get(2).toString());
    }
}