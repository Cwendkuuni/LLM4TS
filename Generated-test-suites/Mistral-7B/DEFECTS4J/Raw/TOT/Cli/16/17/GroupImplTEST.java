package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Testing GroupImpl class
 */
public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // create input
        List<Option> options = new ArrayList<>();
        Option option1 = OptionBuilder.withLongOpt("longOpt1")
                .withDescription("Description for longOpt1")
                .isRequired()
                .create(false);
        Option option2 = OptionBuilder.withLongOpt("longOpt2")
                .withDescription("Description for longOpt2")
                .withArgs(1)
                .create(null);
        Option option3 = OptionBuilder.withLongOpt("longOpt3")
                .withDescription("Description for longOpt3")
                .create(null);
        Option arg1 = OptionBuilder.withLongOpt("arg1")
                .withDescription("Description for arg1")
                .create(null);
        Option arg2 = OptionBuilder.withLongOpt("arg2")
                .withDescription("Description for arg2")
                .create(null);

        Group group = new GroupImpl(options, "group", "Group description", 1, 2);
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);
        group.addOption(arg1);
        group.addOption(arg2);

        // test parse
        CommandLine commandLine = null;
        try {
            commandLine = new CommandLineParser().parse(group, new String[]{"--longOpt1", "arg1", "--longOpt2=value"});
        } catch (ParseException e) {
            // ignore
        }
        Assert.assertNotNull(commandLine);
        Assert.assertEquals("arg1", commandLine.getOptionValue("arg1"));
        Assert.assertEquals("value", commandLine.getOptionValue("longOpt2"));
        Assert.assertFalse(commandLine.hasOption("longOpt3"));
        Assert.assertEquals(1, commandLine.getOptions().size());

        // test parse with incorrect number of options
        try {
            new CommandLineParser().parse(group, new String[]{"--longOpt1", "arg1", "--longOpt1", "--longOpt2=value"});
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        // test parse with incorrect option
        try {
            new CommandLineParser().parse(group, new String[]{"--wrongOption"});
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        // test help
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("test", group, true, true, true, false);
    }
}