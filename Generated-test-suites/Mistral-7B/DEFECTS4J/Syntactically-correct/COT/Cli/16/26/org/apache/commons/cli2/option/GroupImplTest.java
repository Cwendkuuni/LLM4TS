package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.argument.ArgType;
import org.apache.commons.cli2.commandline.ArgParser;
import org.apache.commons.cli2.commandline.ParseContext;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.option.OptionBuilder;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GroupImplTest {

    private GroupImpl group;
    private Parser parser;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withName("option1")
                .withDescription("Option 1")
                .withArgType(ArgType.STRING)
                .create());
        options.add(OptionBuilder.withName("option2")
                .withDescription("Option 2")
                .withArgType(ArgType.INTEGER)
                .create());
        options.add(OptionBuilder.withName("group")
                .withDescription("Group")
                .withMinimum(1)
                .withMaximum(2)
                .isRequired()
                .createGroup(options));

        group = (GroupImpl) options.get(2);
        parser = new ArgParser();
    }

    @Test
    public void testCanProcess_ValidOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");
        boolean result = group.canProcess(commandLine, "group");
        Assert.assertTrue("Can process the valid option", result);
    }

    @Test
    public void testCanProcess_InvalidOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");
        boolean result = group.canProcess(commandLine, "invalid");
        Assert.assertFalse("Can't process the invalid option", result);
    }

    @Test
    public void testCanProcess_AnonymousArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");
        commandLine.addOption("option1", "value1");
        boolean result = group.canProcess(commandLine, null);
        Assert.assertTrue("Can process the anonymous argument", result);
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_InvalidAnonymousArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");
        commandLine.addOption("option1", "value1");
        group.canProcess(commandLine, "invalidAnonymous");
    }

    @Test(expected = OptionException.class)
    public void testProcess_InsufficientOptions() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");
        commandLine.addOption("option1", "value1");
        group.process(commandLine, commandLine.getArgumentIterator());
    }

    @Test
    public void testProcess_ValidOptions() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");
        commandLine.addOption("group", "value1");
        commandLine.addOption("option1", "value1");
        commandLine.addOption("option2", "1");

        group.process(commandLine, commandLine.getArgumentIterator());
        Assert.assertEquals("Expected value1 for option1", "value1", commandLine.getOptionValue("option1"));
        Assert.assertEquals("Expected value1 for option2", "1", commandLine.getOptionValue("option2").toString());
    }

    @Test(expected = OptionException.class)
    public void testProcess_InvalidOption() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");
        commandLine.addOption("option1", "value1");
        commandLine.addOption("group", "value1");
        commandLine.addOption("option2", "1");

        group.process(commandLine, commandLine.getArgumentIterator());
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");
        commandLine.addOption("option1", "value1");
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_InsufficientOptions() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_ExcessiveOptions() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");
        commandLine.addOption("option1", "value1");
        commandLine.addOption("group", "value1");
        commandLine.addOption("option1", "value2");
        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.<DisplaySetting>emptySet(), null);

        String expected = "GROUP\n" +
                "  Group\n" +
                "Options:\n" +
                "------\n" +
                "option1\n" +
                "  Option 1\n" +
                "  with an argument (value required)\n" +
                "option2\n" +
                "  Option 2\n" +
                "  with an argument (value required)\n";

        Assert.assertEquals("Expected usage string", expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0, Collections.<DisplaySetting>emptySet(), null);

        String expected = "GROUP\n" +
                "  Group\n" +
                "Options:\n" +
                "------\n" +
                "option1\n" +
                "  Option 1\n" +
                "  with an argument (value required)\n" +
                "option2\n" +
                "  Option 2\n" +
                "  with an argument (value required)\n";

        for (HelpLine helpLine : helpLines) {
            Assert.assertEquals("Expected help line string", expected, helpLine.getHelp());
        }
    }
}