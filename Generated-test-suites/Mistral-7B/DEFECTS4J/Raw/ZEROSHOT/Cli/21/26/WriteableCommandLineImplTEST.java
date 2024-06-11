import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * WriteableCommandLineImplTest class file to test all methods in the WriteableCommandLineImpl class.
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        Option rootOption = new Option("root", "root option");
        List arguments = new ArrayList();
        arguments.add("argument1");
        arguments.add("argument2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // When

        // test addOption method
        Option option1 = new Option("option1", "Option 1", true);
        commandLine.addOption(option1);
        Assert.assertTrue(commandLine.hasOption(option1));

        Option option2 = new Option("option2", "Option 2", false);
        commandLine.addOption(option2);
        Assert.assertTrue(commandLine.hasOption(option2));

        // test getOption method
        Assert.assertEquals(option1, commandLine.getOption("option1"));
        Assert.assertEquals(option2, commandLine.getOption("option2"));

        // test getValues method
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        List<Object> values = commandLine.getValues(option1, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        // test getUndefaultedValues method
        List<Object> undefaultedValues = commandLine.getUndefaultedValues(option1);
        Assert.assertEquals(0, undefaultedValues.size());

        // test getSwitch method
        commandLine.addSwitch(option2, true);
        Boolean switchValue = commandLine.getSwitch(option2, false);
        Assert.assertTrue(switchValue);

        // test getProperty method
        Option propertyOption = new Option("property", "Property Option");
        commandLine.addProperty(propertyOption, "propertyName", "propertyValue");
        String propertyValue = commandLine.getProperty(propertyOption, "defaultValue");
        Assert.assertEquals("propertyValue", propertyValue);

        // test getProperties method
        commandLine.addProperty(propertyOption, "propertyName1", "propertyValue1");
        commandLine.addProperty(propertyOption, "propertyName2", "propertyValue2");
        Set<String> properties = commandLine.getProperties(propertyOption);
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("propertyName1"));
        Assert.assertTrue(properties.contains("propertyName2"));

        // test looksLikeOption method
        Assert.assertTrue(commandLine.looksLikeOption("option1"));
        Assert.assertFalse(commandLine.looksLikeOption("argument1"));

        // test toString method
        Assert.assertEquals("argument1 argument2 option1 true option2 false", commandLine.toString());

        // test getOptions method
        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));

        // test getOptionTriggers method
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("option1"));
        Assert.assertTrue(optionTriggers.contains("option2"));

        // test setDefaultValues method
        commandLine.setDefaultValues(option1, new ArrayList<>());
        List<Object> defaultValues = commandLine.getValues(option1, null);
        Assert.assertEquals(0, defaultValues.size());

        // test setDefaultSwitch method
        commandLine.setDefaultSwitch(option2, false);
        Boolean defaultSwitchValue = commandLine.getSwitch(option2, null);
        Assert.assertFalse(defaultSwitchValue);

        // test getNormalised method
        List<String> normalised = commandLine.getNormalised();
        Assert.assertEquals(3, normalised.size());
        Assert.assertEquals("argument1", normalised.get(0));
        Assert.assertEquals("argument2", normalised.get(1));
        Assert.assertEquals("option1", normalised.get(2));
    }
}