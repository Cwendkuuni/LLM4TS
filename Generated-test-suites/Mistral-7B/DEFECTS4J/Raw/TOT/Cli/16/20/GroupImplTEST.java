package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.options.GroupBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test class for GroupImpl.
 */
public class GroupImplTest {

    @Test
    public void testGroup() {
        // build command line options
        final Group group = new GroupBuilder()
                .setName("testGroup")
                .addOption(OptionBuilder.withName("optionA")
                        .setDescription("Option A"))
                .addOption(OptionBuilder.withName("optionB")
                        .setDescription("Option B"))
                .addOption(OptionBuilder.withName("optionC")
                        .setDescription("Option C")
                        .setRequired(true)
                        .create())
                .create();

        // parse command line arguments
        final CommandLine cmd = new CommandLine();
        group.parse(cmd, new String[]{"-t", "--testGroup", "--optionA", "--optionB", "--optionC"});

        // check options
        Assert.assertEquals("testGroup", cmd.getName());
        Assert.assertTrue(cmd.hasOption("optionA"));
        Assert.assertTrue(cmd.hasOption("optionB"));
        Assert.assertTrue(cmd.hasOption("optionC"));
        Assert.assertFalse(cmd.hasOption("unknownOption"));

        // print help
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test Group", "", true);
        formatter.printHelp("Test Group", group, true);

        // test parse with invalid arguments
        try {
            group.parse(cmd, new String[]{"--testGroup", "--optionA", "--optionA"});
            Assert.fail();
        } catch (final OptionException e) {
            // expected
        }

        try {
            group.parse(cmd, new String[]{"--testGroup", "--optionA", "--optionC"});
            Assert.fail();
        } catch (final OptionException e) {
            // expected
        }
    }

    @Test
    public void testGroupWithAnonymousArgs() {
        // build command line options
        final Group group = new GroupBuilder()
                .setName("testGroup")
                .addOption(OptionBuilder.withName("optionA")
                        .setDescription("Option A"))
                .addOption(OptionBuilder.withName("optionB")
                        .setDescription("Option B")
                        .create())
                .addOption(OptionBuilder.withName("anonymousArg")
                        .create())
                .create();

        // parse command line arguments
        final CommandLine cmd = new CommandLine();
        group.parse(cmd, new String[]{"-t", "--testGroup", "--optionA", "--optionB", "arg1", "arg2"});

        // check options
        Assert.assertEquals("testGroup", cmd.getName());
        Assert.assertTrue(cmd.hasOption("optionA"));
        Assert.assertTrue(cmd.hasOption("optionB"));
        Assert.assertTrue(cmd.hasOption("anonymousArg"));
        Assert.assertEquals(2, cmd.getValuesForOption("anonymousArg").size());
        Assert.assertEquals("arg1", cmd.getValuesForOption("anonymousArg").get(0));
        Assert.assertEquals("arg2", cmd.getValuesForOption("anonymousArg").get(1));

        // print help
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test Group", "", true);
        formatter.printHelp("Test Group", group, true);

        // test parse with invalid arguments
        try {
            group.parse(cmd, new String[]{"--testGroup", "--optionA", "--optionB"});
            Assert.fail();
        } catch (final OptionException e) {
            // expected
        }

        try {
            group.parse(cmd, new String[]{"--testGroup", "--optionA", "arg1"});
            Assert.fail();
        } catch (final OptionException e) {
            // expected
        }
    }
}