import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        Option option = new Option("o", "output", true, "Output file");
        Argument arg = new Argument(0, "inputFile", true, "Input file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(arg, "inputFile.txt"));

        // When adding an option
        commandLine.addOption(option);

        // Then the option is added to the list of options
        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));

        // When adding a value to an option
        commandLine.addValue(option, "output.txt");

        // Then the value is added to the list of values for the option
        List values = commandLine.getValues(option, null);
        Assert.assertEquals("output.txt", values.get(0));
        Assert.assertEquals(1, values.size());

        // When adding a switch to an option
        commandLine.addSwitch(option, true);

        // Then the switch is true for the option
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        // When setting default values for an option
        List defaultValues = Arrays.asList("defaultOutput.txt");
        commandLine.setDefaultValues(option, defaultValues);

        // Then the default values are set for the option
        values = commandLine.getValues(option, null);
        Assert.assertEquals(defaultValues, values);

        // When setting a default switch for an option
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        // Then the default switch is true for the option
        switchValue = commandLine.getSwitch(option, false);
        Assert.assertEquals(defaultSwitch, switchValue);

        // When getting properties
        commandLine.addProperty("property1", "value1");
        Properties properties = commandLine.getProperties();
        Assert.assertEquals("value1", properties.getProperty("property1"));

        // When getting the properties as a set
        Map<String, String> propertyMap = new HashMap<>();
        propertyMap.put("property1", "value1");
        Set<String> propertiesSet = commandLine.getProperties();
        Assert.assertEquals(propertyMap, propertiesSet);

        // When checking if the command line looks like an option
        Assert.assertTrue(commandLine.looksLikeOption("o"));

        // When getting the command line as a string
        String commandLineString = commandLine.toString();
        Assert.assertEquals("inputFile.txt output.txt", commandLineString);
    }
}