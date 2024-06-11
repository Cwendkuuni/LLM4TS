import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.apache.commons.cli2.CliFactory.withDefaults;
import static org.apache.commons.cli2.Option.builder;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        Option option1 = builder().longOpt("option1").hasArg().build();
        Option option2 = builder().longOpt("option2").build();
        Option option3 = builder().longOpt("option3").hasArg(1).build();
        Option option4 = builder().longOpt("option4").build();
        Option option5 = builder().longOpt("option5").build();
        Option rootOption = builder().longOpt("root").required(true).build();

        List<String> args = Arrays.asList("--option1", "arg1", "--option2", "--option3", "arg3", "--option4", "--root");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, args);

        // When
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        writeableCommandLine.addOption(option3);
        writeableCommandLine.addOption(option4);
        writeableCommandLine.addOption(option5);

        // Then
        CommandLine commandLine = writeableCommandLine.parse();

        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertEquals("arg1", commandLine.getValue(option1));

        Assert.assertTrue(commandLine.hasOption(option2));
        Assert.assertFalse(commandLine.hasOptionValue(option2));

        Assert.assertTrue(commandLine.hasOption(option3));
        Assert.assertEquals("arg3", commandLine.getValue(option3));

        Assert.assertFalse(commandLine.hasOption(option4));

        Assert.assertFalse(commandLine.hasOption(option5));

        Assert.assertTrue(commandLine.hasOption(rootOption));

        // Get Options
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(5, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
        Assert.assertTrue(options.contains(option3));
        Assert.assertTrue(options.contains(option4));
        Assert.assertTrue(options.contains(option5));

        // Get Option Triggers
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(6, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("option1"));
        Assert.assertTrue(optionTriggers.contains("option2"));
        Assert.assertTrue(optionTriggers.contains("option3"));
        Assert.assertTrue(optionTriggers.contains("option4"));
        Assert.assertTrue(optionTriggers.contains("option5"));
        Assert.assertTrue(optionTriggers.contains("root"));

        // Get Normalised
        List normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(7, normalised.size());
        Assert.assertEquals("--option1", normalised.get(0));
        Assert.assertEquals("arg1", normalised.get(1));
        Assert.assertEquals("--option2", normalised.get(2));
        Assert.assertEquals("--option3", normalised.get(3));
        Assert.assertEquals("arg3", normalised.get(4));
        Assert.assertEquals("--option4", normalised.get(5));
        Assert.assertEquals("--root", normalised.get(6));

        // Get Values
        List values = writeableCommandLine.getValues(option1, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("arg1", values.get(0));

        // Get Undefaulted Values
        List unfaultedValues = writeableCommandLine.getUndefaultedValues(option1);
        Assert.assertEquals(1, unfaultedValues.size());
        Assert.assertEquals("arg1", unfaultedValues.get(0));

        // Get Switch
        Boolean switchValue = writeableCommandLine.getSwitch(option3, null);
        Assert.assertNull(switchValue);

        // Get Property
        String propertyValue = writeableCommandLine.getProperty("property1", "defaultValue");
        Assert.assertEquals("defaultValue", propertyValue);

        // Set Property
        writeableCommandLine.addProperty(option1, "property1", "value1");
        propertyValue = writeableCommandLine.getProperty("property1", null);
        Assert.assertEquals("value1", propertyValue);

        // Set Default Values
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        writeableCommandLine.setDefaultValues(option1, defaultValues);
        values = writeableCommandLine.getValues(option1, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("arg1", values.get(0));
        Assert.assertEquals("defaultValue1", values.get(1));

        // Set Default Switch
        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(option3, defaultSwitch);
        switchValue = writeableCommandLine.getSwitch(option3, null);
        Assert.assertEquals(defaultSwitch, switchValue);

        // Print Command Line
        System.out.println(writeableCommandLine);
    }
}