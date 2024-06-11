package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class GroupImplTest {

    @Test
    public void testGroupImplConstruction() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "a"));
        options.add(new Option("b", "option B", false, "b"));

        GroupImpl group = new GroupImpl(options, "MyGroup", "A description", 1, 2);

        Assert.assertEquals("MyGroup", group.getPreferredName());
        Assert.assertEquals("A description", group.getDescription());
        Assert.assertEquals(Collections.unmodifiableList(options), group.getOptions());
        Assert.assertEquals(1, group.getMinimum());
        Assert.assertEquals(2, group.getMaximum());
    }

    @Test
    public void testCanProcess_TwoOptionsWithSameTrigger_ShouldReturnFalse() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "a"));
        options.add(new Option("a", "option A", true, "a"));

        GroupImpl group = new GroupImpl(options, null, null, 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        String arg = "a";

        Assert.assertFalse(group.canProcess(commandLine, arg));
    }

    @Test
    public void testCanProcess_OptionWithNoTrigger_ShouldReturnFalse() {
        List<Option> options = new ArrayList<>();
        options.add(new Argument("arg"));

        GroupImpl group = new GroupImpl(options, null, null, 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        String arg = "arg";

        Assert.assertFalse(group.canProcess(commandLine, arg));
    }

    @Test
    public void testCanProcess_OptionWithTrigger_ShouldReturnTrue() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "a"));

        GroupImpl group = new GroupImpl(options, null, null, 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        String arg = "a";

        Assert.assertTrue(group.canProcess(commandLine, arg));
    }

    @Test(expected = OptionException.class)
    public void testProcess_InvalidOption_ShouldThrowOptionException() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "a"));

        GroupImpl group = new GroupImpl(options, null, null, 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parse("--invalid-option");

        ListIterator<String> arguments = commandLine.getParsed();

        group.process(commandLine, arguments);
    }

    @Test
    public void testProcess_TwoOptionsWithSameTrigger_ShouldProcessBoth() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "a"));
        options.add(new Option("a", "option A", true, "a"));

        GroupImpl group = new GroupImpl(options, null, null, 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parse("--a --a");

        ListIterator<String> arguments = commandLine.getParsed();

        group.process(commandLine, arguments);
    }

    @Test
    public void testProcess_AnonymousArgument_ShouldProcess() {
        List<Option> options = new ArrayList<>();
        options.add(new Argument("arg"));

        GroupImpl group = new GroupImpl(options, null, null, 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parse("--arg");

        ListIterator<String> arguments = commandLine.getParsed();

        group.process(commandLine, arguments);
    }

    @Test
    public void testValidate_MinimumOptionsNotPresent_ShouldThrowOptionException() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "a"));

        GroupImpl group = new GroupImpl(options, null, null, 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parse("");

        Assert.assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testValidate_MaximumOptionsPresent_ShouldThrowOptionException() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "a"));

        GroupImpl group = new GroupImpl(options, null, null, 1, 2);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parse("--a --a --a");

        Assert.assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testAppendUsage() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "a"));
        options.add(new Option("b", "option B", false, "b"));

        GroupImpl group = new GroupImpl(options, "MyGroup", "A description", 1, 2);

        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new LinkedHashSet<>();

        group.appendUsage(buffer, helpSettings, null);

        String expectedUsage =
                "MyGroup:\n" +
                        "A description\n" +
                        "  --a <arg>\n" +
                        "  --b\n";

        Assert.assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "a"));
        options.add(new Option("b", "option B", false, "b"));

        GroupImpl group = new GroupImpl(options, "MyGroup", "A description", 1, 2);

        List<HelpLine> helpLines = group.helpLines(0, null, null);

        String expectedUsage =
                "MyGroup:\n" +
                        "A description\n" +
                        "  --a <arg>\n" +
                        "  --b\n";

        for (HelpLine helpLine : helpLines) {
            Assert.assertEquals(expectedUsage, helpLine.getLine());
        }
    }
}