import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "output file");
        List arguments = new ArrayList();
        arguments.add("test.txt");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        // Test adding an option
        Option option1 = new Option("i", "input", true, "input file");
        writeableCommandLine.addOption(option1);

        // Test adding a value to an option
        writeableCommandLine.addValue(option, "test.txt");
        writeableCommandLine.addValue(option1, "input.txt");

        // Test adding a switch to an option
        writeableCommandLine.addSwitch(option, true);

        // Test getting an option
        Option getOption = writeableCommandLine.getOption("o");
        Assert.assertNotNull(getOption);

        // Test getting values for an option
        List optionValues = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, optionValues.size());
        Assert.assertEquals("test.txt", optionValues.get(0));

        // Test getting undefined values for an option
        List undefinedValues = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertTrue(undefinedValues.isEmpty());

        // Test getting a switch for an option
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        // Test getting properties for an option
        Option propertyOption = new Option("p", "property", true, "property value");
        writeableCommandLine.addProperty(propertyOption, "property.name", "property.value");

        Map optionProperties = writeableCommandLine.getProperties(propertyOption);
        Assert.assertEquals(1, optionProperties.size());
        Assert.assertEquals("property.value", optionProperties.get("property.name"));

        Properties allProperties = writeableCommandLine.getProperties();
        Assert.assertEquals(1, allProperties.size());
        Assert.assertEquals("property.value", allProperties.getProperty("property.name"));

        // Test looking like an option
        Assert.assertTrue(writeableCommandLine.looksLikeOption("o"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("arg"));

        // Test getting options
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Option optionFromList = (Option) options.get(0);
        Assert.assertEquals("o", optionFromList.getLongOpt());
        Option option1FromList = (Option) options.get(1);
        Assert.assertEquals("i", option1FromList.getLongOpt());

        // Test getting option triggers
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("o"));
        Assert.assertTrue(optionTriggers.contains("i"));

        // Test setting default values for an option
        writeableCommandLine.setDefaultValues(option, new ArrayList() {{
            add("test_default.txt");
        }});
        List defaultValues = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, defaultValues.size());
        Assert.assertEquals("test_default.txt", defaultValues.get(0));

        // Test setting a default switch for an option
        writeableCommandLine.setDefaultSwitch(option, false);
        Boolean defaultSwitch = writeableCommandLine.getSwitch(option, true);
        Assert.assertFalse(defaultSwitch);

        // Test getting normalised arguments
        List normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("test.txt", normalised.get(0));
        Assert.assertEquals("input.txt", normalised.get(1));

        // Test toString method
        String toString = writeableCommandLine.toString();
        Assert.assertEquals("test.txt input.txt", toString);
    }
}