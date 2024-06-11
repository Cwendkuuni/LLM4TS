package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {

    private WriteableCommandLine commandLine;
    private Group group;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("option1", "option1", true, "Description 1"));
        options.add(new Argument("arg1", "Argument 1"));

        group = new GroupImpl(options, "group1", "Group 1", 1, 1, false);
        commandLine = new WriteableCommandLine("commandLine");
    }

    @Test
    public void testCanProcess_ValidOption() {
        Assert.assertTrue(group.canProcess(commandLine, "option1"));
    }

    @Test
    public void testCanProcess_InvalidOption() {
        Assert.assertFalse(group.canProcess(commandLine, "invalidOption"));
    }

    @Test
    public void testCanProcess_AnonymousArgument() {
        Assert.assertTrue(group.canProcess(commandLine, "arg1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanProcess_InvalidArgument() {
        commandLine.parse("--invalidArgument");
        group.canProcess(commandLine, "invalidArgument");
    }

    @Test
    public void testProcess_ValidOption() {
        commandLine.parse("--option1 trueValue");
        group.process(commandLine, commandLine.getArguments().listIterator());
        Option option1 = group.findOption("option1");
        Assert.assertEquals("trueValue", option1.getValue());
    }

    @Test(expected = OptionException.class)
    public void testProcess_InvalidOption() {
        commandLine.parse("--invalidOption");
        group.process(commandLine, commandLine.getArguments().listIterator());
    }

    @Test
    public void testProcess_AnonymousArgument() {
        commandLine.parse("--arg1 value");
        group.process(commandLine, commandLine.getArguments().listIterator());
        Argument arg1 = group.getAnonymous().get(0);
        Assert.assertEquals("value", arg1.getValue());
    }

    @Test
    public void testValidate_ValidOptions() {
        commandLine.parse("--option1 trueValue --option1 falseValue");
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidOptions() {
        commandLine.parse("--option1 trueValue");
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_MissingOptions() {
        commandLine.parse("--");
        group.validate(commandLine);
    }

    @Test
    public void testGetPreferredName() {
        Assert.assertEquals("group1", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals("Group 1", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Map<DisplaySetting, Boolean> helpSettings = new HashMap<>();
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_NAME, true);
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_EXPANDED, true);

        group.appendUsage(buffer, helpSettings, null);

        String expectedUsage = "-group1 (--option1 <value>)\n";
        Assert.assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        int depth = 1;
        Map<DisplaySetting, Boolean> helpSettings = new HashMap<>();
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_NAME, true);
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_EXPANDED, true);

        List<HelpLine> helpLines = group.helpLines(depth, helpSettings, null);

        HelpLine helpLine = helpLines.get(0);
        Assert.assertEquals("group1", helpLine.getName());
        Assert.assertEquals("Group 1", helpLine.getShortDescription());
        List<HelpLine> optionsHelpLines = helpLines.get(1).getHelpLines(depth + 1);
        Assert.assertEquals(1, optionsHelpLines.size());
        HelpLine optionHelpLine = optionsHelpLines.get(0);
        Assert.assertEquals("option1", optionHelpLine.getName());
        Assert.assertEquals("option1", optionHelpLine.getShortDescription());
        Assert.assertEquals("Description 1", optionHelpLine.getLongDescription());
    }
}