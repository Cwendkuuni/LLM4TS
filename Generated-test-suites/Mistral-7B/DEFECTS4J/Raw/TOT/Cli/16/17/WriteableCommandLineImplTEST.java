package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() throws ParseException {
        Option option = OptionBuilder.withLongOpt("output")
                .hasArg(true)
                .isRequired(false)
                .withDescription("Output file name")
                .create(false);

        CommandLine cmd = new WriteableCommandLineImpl(option, new ArrayList<>());

        // add option and its value
        cmd.addOption(option);
        cmd.addValue(option, "output.txt");

        // test getValue method
        List values = cmd.getValues(option, null);
        Assert.assertEquals("output.txt", values.get(0));

        // test getSwitch method
        Boolean switchValue = cmd.getSwitch(option, false);
        Assert.assertFalse(switchValue);

        // test addSwitch method
        cmd.addSwitch(option, true);
        switchValue = cmd.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        // test hasOption method
        Assert.assertTrue(cmd.hasOption(option));

        // test getOption method
        Option optionFromCmd = cmd.getOption(option.getLongOpt());
        Assert.assertEquals(option, optionFromCmd);

        // testtoString method
        Assert.assertEquals("output.txt", cmd.toString());

        // test getOptions method
        List options = cmd.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));

        // test getOptionTriggers method
        Set optionTriggers = cmd.getOptionTriggers();
        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertEquals(option.getLongOpt(), optionTriggers.iterator().next());

        // test looksLikeOption method
        Assert.assertTrue(cmd.looksLikeOption("--output"));
        Assert.assertFalse(cmd.looksLikeOption("--nonexistent"));

        // test getProperty method
        Option propertyOption = OptionBuilder.withLongOpt("property")
                .hasArg(false)
                .withDescription("Property name")
                .create(false);
        cmd = new WriteableCommandLineImpl(propertyOption, new ArrayList<>());
        cmd.addOption(propertyOption);
        cmd.addProperty(propertyOption, "property.name", "property.value");
        String propertyValue = cmd.getProperty(propertyOption, "defaultValue");
        Assert.assertEquals("property.value", propertyValue);
    }
}