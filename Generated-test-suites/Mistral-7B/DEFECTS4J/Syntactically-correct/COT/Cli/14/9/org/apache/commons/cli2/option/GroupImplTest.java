package org.apache.commons.cli2.option; // Make sure to import any necessary classes

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class GroupImplTest {

    private GroupImpl group;
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "opt1", true));
        options.add(createOption("option2", "opt2", false, 1, 2));
        options.add(createOption("option3", "opt3", true));
        options.add(createArgument("arg1", "argdesc", true, "arg"));

        group = new GroupImpl(options, "groupname", "groupdesc", 1, 3);
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testCanProcess() {
        Assert.assertTrue(group.canProcess(commandLine, "opt1"));
        Assert.assertTrue(group.canProcess(commandLine, "opt2"));
        Assert.assertTrue(group.canProcess(commandLine, "opt3"));
        Assert.assertFalse(group.canProcess(commandLine, "arg1"));
        Assert.assertFalse(group.canProcess(commandLine, "unknown"));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = group.getPrefixes();
        Assert.assertEquals(Collections.emptyList(), prefixes);
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = group.getTriggers();
        Assert.assertEquals(Arrays.asList("option1", "option2", "option3", "arg1"), triggers);
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidArg() {
        group.process(commandLine, commandLine.listIterator());
        commandLine.addOption("--invalid");
        group.process(commandLine, commandLine.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithMoreOptionsThanMaximum() {
        commandLine.addOption("opt1");
        commandLine.addOption("opt1");
        group.process(commandLine, commandLine.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithLessOptionsThanMinimum() {
        commandLine.addOption("opt1");
        group.process(commandLine, commandLine.listIterator());
        group.process(commandLine, commandLine.listIterator());
        commandLine.addOption("opt3");
        group.process(commandLine, commandLine.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidOption() {
        commandLine.addOption("invalid");
        group.process(commandLine, commandLine.listIterator());
    }

    @Test
    public void testProcessWithValidOptions() {
        commandLine.addOption("opt1");
        group.process(commandLine, commandLine.listIterator());
        Assert.assertTrue(commandLine.hasOption("option1"));

        commandLine.addOption("opt2");
        commandLine.addOption("opt3");
        group.process(commandLine, commandLine.listIterator());
        Assert.assertTrue(commandLine.hasOption("option2"));
        Assert.assertTrue(commandLine.hasOption("option3"));
    }

    @Test
    public void testValidate() throws OptionException {
        commandLine.addOption("opt1");
        group.validate(commandLine);

        commandLine.clear();
        group.validate(commandLine);
        commandLine.addOption("opt1");
        group.validate(commandLine);
        commandLine.addOption("opt2");
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidOptionNumber() throws OptionException {
        commandLine.addOption("opt1");
        commandLine.addOption("opt1");
        group.validate(commandLine);
    }

    private Option createOption(String longOpt, String opt, boolean required, int minOccurs, int maxOccurs) {
        return new OptionBuilder()
                .longOpt(longOpt)
                .shortOpt(opt)
                .required(required)
                .minOccurs(minOccurs)
                .maxOccurs(maxOccurs)
                .withDescription("desc")
                .create();
    }

    private Argument createArgument(String longArg, String argDesc, boolean required, String arg) {
        return new ArgumentBuilder()
                .longArg(longArg)
                .argName(arg)
                .required(required)
                .description(argDesc)
                .create();
    }
}