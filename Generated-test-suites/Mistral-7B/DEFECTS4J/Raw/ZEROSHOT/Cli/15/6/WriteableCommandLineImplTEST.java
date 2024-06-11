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

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("opt", "option", true, "Option description");
        Argument arg = new Argument("arg", "arg description");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.singletonList("test"));

        commandLine.addArgument(arg);
        commandLine.addOption(option);

        commandLine.addValue(option, "value");
        commandLine.addSwitch(option, true);

        Option propertyOption = new Option("prop", "property", true, "Property option");
        commandLine.addProperty(propertyOption, "propertyName", "propertyValue");

        Assert.assertTrue(commandLine.hasOption(option));
        Assert.assertEquals(option, commandLine.getOption("opt"));
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(Collections.singletonList("test"), commandLine.getNormalised());
        Assert.assertEquals(1, commandLine.getOptionTriggers().size());
        Assert.assertEquals("test opt", commandLine.toString());

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        List propertyValues = commandLine.getProperties(propertyOption);
        Assert.assertEquals(1, propertyValues.size());
        Assert.assertEquals("propertyValue", propertyValues.iterator().next());

        Set properties = commandLine.getProperties();
        Assert.assertEquals(1, properties.size());
        Assert.assertTrue(properties.contains("propertyName"));

        Assert.assertEquals(arg, commandLine.getArgument(0));
        Assert.assertEquals("arg", commandLine.getArgument(0).getName());

        Argument argument = commandLine.getArgument(1);
        Assert.assertNull(argument);
    }
}