package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GroupImplTest {
    private static final String GROUP_NAME = "myGroup";
    private static final String GROUP_DESCRIPTION = "Description of the group";
    private static final int GROUP_MINIMUM = 1;
    private static final int GROUP_MAXIMUM = 3;
    private static final List GROUP_OPTIONS = new ArrayList() {{
        add(new Option("o1", "Option 1", true, "Optional option 1"));
        add(new Option("o2", "Option 2", false, "Required option 2"));
        add(new Option("o3", "Option 3", false, "Optional option 3"));
        add(new Argument("arg1", "Argument 1", true, "Argument 1"));
    }};

    private GroupImpl group;

    @Before
    public void setUp() {
        group = new GroupImpl(GROUP_OPTIONS, GROUP_NAME, GROUP_DESCRIPTION, GROUP_MINIMUM, GROUP_MAXIMUM);
    }

    @Test
    public void testCanProcess_ValidOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        Assert.assertTrue(group.canProcess(commandLine, "o1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanProcess_InvalidOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        Assert.assertFalse(group.canProcess(commandLine, "invalidOption"));
    }

    @Test
    public void testCanProcess_BurstingOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parse("--o2 --o2 --o2");

        Assert.assertTrue(group.canProcess(commandLine, "o2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanProcess_InvalidBurstingOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parse("--o3 --o3 --o3");

        Assert.assertFalse(group.canProcess(commandLine, "o3"));
    }

    @Test
    public void testCanProcess_Argument() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parse("--arg1 argValue");

        Assert.assertTrue(group.canProcess(commandLine, "arg1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanProcess_InvalidArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        Assert.assertFalse(group.canProcess(commandLine, "invalidArg"));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = group.getPrefixes();

        List prefixesList = new LinkedList(prefixes);

        Assert.assertFalse(prefixesList.isEmpty());
        Assert.assertEquals("", prefixesList.get(0).toString());
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();

        List triggersList = new ArrayList(triggers);

        Assert.assertFalse(triggersList.isEmpty());
        Assert.assertEquals("o1", triggersList.get(0).toString());
        Assert.assertEquals("o2", triggersList.get(1).toString());
        Assert.assertEquals("o3", triggersList.get(2).toString());
    }

    @Test
    public void testProcess_ValidOptions() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parse("--o1 --o2");

        group.process(commandLine, commandLine.iterator());

        Assert.assertEquals(2, commandLine.getOptions().size());
        Assert.assertEquals("o1", commandLine.getOption(0).getLongOpt());
        Assert.assertEquals("o2", commandLine.getOption(1).getLongOpt());
    }

    @Test(expected = OptionException.class)
    public void testProcess_InvalidOption_RequiredOptionMissing() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parse("--o1");

        group.process(commandLine, commandLine.iterator());

        Assert.assertFalse(commandLine.hasOption("o2"));
    }

    @Test(expected = OptionException.class)
    public void testProcess_InvalidOption_MaximumReached() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        for (int i = 0; i < GROUP_MAXIMUM + 1; i++) {
            commandLine.parse("--o1");
        }

        group.process(commandLine, commandLine.iterator());

        Assert.assertFalse(commandLine.hasOption("o1"));
    }

    @Test(expected = OptionException.class)
    public void testProcess_InvalidOption_Bursting() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parse("--o2 --o2 --o2");

        group.process(commandLine, commandLine.iterator());

        Assert.assertFalse(commandLine.hasOption("o3"));
    }

    @Test
    public void testProcess_Argument() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parse("--arg1 argValue");

        group.process(commandLine, commandLine.iterator());

        Assert.assertEquals("argValue", commandLine.getOption("arg1").getValue());
    }

    @Test
    public void testValidate_ValidOptions() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parse("--o1 --o2 --o1");

        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            Assert.fail("Validation should have succeeded");
        }
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidOptions_MinimumNotReached() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parse("--o1");

        try {
            group.validate(commandLine);
            Assert.fail("Validation should have failed");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidOptions_MaximumReached() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        for (int i = 0; i < GROUP_MAXIMUM + 1; i++) {
            commandLine.parse("--o1");
        }

        try {
            group.validate(commandLine);
            Assert.fail("Validation should have failed");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidOptions_WrongOrder() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parse("--o2 --o1");

        try {
            group.validate(commandLine);
            Assert.fail("Validation should have failed");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testValidate_Argument() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.parse("--arg1 argValue");

        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            Assert.fail("Validation should have succeeded");
        }
    }

    @Test(expected = OptionException.class)
    public void testValidate_Argument_Missing() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        try {
            group.validate(commandLine);
            Assert.fail("Validation should have failed");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, Collections.<DisplaySetting>emptySet(), null);

        String expectedUsage = String.format(
            "%s%n%s:%s%n%s%n%s%n%s%n",
            ResourceConstants.USAGE_GROUP_INTRO,
            GROUP_NAME,
            ResourceConstants.USAGE_OPTIONS_INTRO,
            ResourceConstants.USAGE_GROUP,
            ResourceConstants.USAGE_OPTIONS,
            ResourceConstants.USAGE_ARGUMENT);

        Assert.assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<String> helpLines = group.helpLines(0, Collections.<DisplaySetting>emptySet(), null);

        String expectedHelpLines = String.join(System.lineSeparator(),
            ResourceConstants.HELP_GROUP_HEAD,
            GROUP_NAME,
            ResourceConstants.HELP_DESCRIPTION,
            ResourceConstants.HELP_OPTIONS_HEADER,
            "[o1]",
            ResourceConstants.USAGE_OPTIONS_SHORT,
            "[o1]",
            "Optional option 1",
            ResourceConstants.USAGE_OPTIONS_LONG,
            "[o1]",
            "Optional option 1",
            ResourceConstants.HELP_OPTIONS_FOOTER,
            "[arg1]",
            ResourceConstants.USAGE_ARGUMENT_SHORT,
            "[arg1]",
            "Argument 1",
            ResourceConstants.HELP_ARGUMENT_FOOTER,
            ResourceConstants.HELP_GROUP_FOOT);

        Assert.assertEquals(expectedHelpLines, buildStringFromList(helpLines));
    }

    private String buildStringFromList(List<String> lines) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String line : lines) {
            stringBuilder.append(line).append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }
}