package org.apache.commons.cli2.option; // make sure to import all necessary packages

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * Test for GroupImpl class
 */
public class GroupImplTest {
    

    @Test
    public void testGroupImplConstructor() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("opt1", "option 1", true, "o1");
        Option option2 = new Option("opt2", "option 2", false, "o2");
        options.add(option1);
        options.add(option2);

        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        Assert.assertEquals("group", group.getPreferredName());
        Assert.assertEquals("group description", group.getDescription());
        Assert.assertEquals(1, group.getMinimum());
        Assert.assertEquals(2, group.getMaximum());
        Assert.assertNotNull(group.getTriggers());
        Assert.assertNotNull(group.getPrefixes());
    }

    @Test
    public void testCanProcess() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("opt1", "option 1", true, "o1");
        Option option2 = new Option("opt2", "option 2", false, "o2");
        options.add(option1);
        options.add(option2);

        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        // test valid option
        Assert.assertTrue(group.canProcess(commandLine, "o1"));

        // test invalid option
        Assert.assertFalse(group.canProcess(commandLine, "invalid"));

        // test option with bursting
        commandLine.parse("--opt1 --opt1");
        Assert.assertTrue(group.canProcess(commandLine, "o1"));

        // test option with no bursting
        commandLine.clear();
        Assert.assertFalse(group.canProcess(commandLine, "o1"));

        // test anonymous argument
        Argument arg = new Argument("arg1", 1, "a1");
        group.addAnonymousOption(arg);

        commandLine.parse("--arg1 1");
        Assert.assertTrue(group.canProcess(commandLine, "arg1"));
    }

    @Test(expected = OptionException.class)
    public void testProcessInvalidOption() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("opt1", "option 1", true, "o1");
        Option option2 = new Option("opt2", "option 2", false, "o2");
        options.add(option1);
        options.add(option2);

        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = commandLine.getArguments();

        // add invalid option
        arguments.add("invalid");

        group.process(commandLine, arguments.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessTooManyOptions() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("opt1", "option 1", true, "o1");
        Option option2 = new Option("opt2", "option 2", false, "o2");
        options.add(option1);
        options.add(option2);

        Group group = new GroupImpl(options, "group", "group description", 1, 3);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = commandLine.getArguments();

        // add valid options
        arguments.add("o1");
        arguments.add("o1");
        arguments.add("o2");
        group.process(commandLine, arguments.listIterator());

        // add one more option
        arguments.add("o3");

        group.process(commandLine, arguments.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testValidateTooFewOptions() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("opt1", "option 1", true, "o1");
        Option option2 = new Option("opt2", "option 2", false, "o2");
        options.add(option1);
        options.add(option2);

        Group group = new GroupImpl(options, "group", "group description", 2, 2);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = commandLine.getArguments();

        // add valid options
        arguments.add("o1");
        arguments.add("o2");

        group.validate(commandLine);

        // remove one option
        arguments.remove(1);

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateTooManyOptions() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("opt1", "option 1", true, "o1");
        Option option2 = new Option("opt2", "option 2", false, "o2");
        options.add(option1);
        options.add(option2);

        Group group = new GroupImpl(options, "group", "group description", 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = commandLine.getArguments();

        // add valid option
        arguments.add("o1");

        group.validate(commandLine);

        // add one more option
        arguments.add("o2");

        group.validate(commandLine);
    }

    @Test
    public void testHelp() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("opt1", "option 1", true, "o1");
        Option option2 = new Option("opt2", "option 2", false, "o2");
        options.add(option1);
        options.add(option2);

        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        // append usage to buffer
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        group.appendUsage(buffer, helpSettings, null);

        String expectedUsage = "group (group description)\n" +
                "[]\n" +
                "opt1  (option 1) [o1]\n" +
                "opt2  (option 2)\n";

        String actualUsage = buffer.toString();
        Assert.assertEquals(expectedUsage, actualUsage);

        // test helpLines
        List<HelpLine> helpLines = group.helpLines(0, helpSettings, null);
        String expectedHelpLinesText = expectedUsage.replaceAll("\\[|\\]", "");
        String actualHelpLinesText = "";

        for (HelpLine helpLine : helpLines) {
            actualHelpLinesText += helpLine.getHelp();
        }

        Assert.assertEquals(expectedHelpLinesText, actualHelpLinesText);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("opt1", "option 1", true, "o1");
        Option option2 = new Option("opt2", "option 2", false, "o2");
        options.add(option1);
        options.add(option2);

        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        List<Option> actualOptions = group.getOptions();
        List<Option> expectedOptions = options;

        for (int i = 0; i < actualOptions.size(); i++) {
            Assert.assertEquals(expectedOptions.get(i), actualOptions.get(i));
        }
    }

    @Test
    public void testGetAnonymous() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("opt1", "option 1", true, "o1");
        options.add(option1);

        Argument arg1 = new Argument("arg1", 1, "a1");
        Group group = new GroupImpl(options, null, null, 0, 0);
        group.addAnonymousOption(arg1);

        List<Option> actualAnonymous = group.getAnonymous();
        Argument expectedArg1 = arg1;

        Assert.assertEquals(1, actualAnonymous.size());
        Assert.assertEquals(expectedArg1, actualAnonymous.get(0));
    }

    @Test
    public void testFindOption() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("opt1", "option 1", true, "o1");
        Option option2 = new Option("opt2", "option 2", false, "o2");
        Option option3 = new Option("opt3", "option 3", false, "o3");
        options.add(option1);
        options.add(option2);
        options.add(option3);

        Group group = new GroupImpl(options, "group", "group description", 1, 3);

        Option actualOption = group.findOption("o1");
        Option expectedOption = option1;

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testIsRequired() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("opt1", "option 1", true, "o1");
        Option option2 = new Option("opt2", "option 2", false, "o2");
        options.add(option1);
        options.add(option2);

        Group group = new GroupImpl(options, "group", "group description", 2, 3);

        Assert.assertFalse(group.isRequired());

        options = new ArrayList<>();
        option1 = new Option("opt1", "option 1", true, "o1");
        options.add(option1);

        group = new GroupImpl(options, "group", "group description", 1, 1);

        Assert.assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("opt1", "option 1", true, "o1");
        Option option2 = new Option("opt2", "option 2", false, "o2");
        options.add(option1);
        options.add(option2);

        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        group.defaults(commandLine);

        Assert.assertFalse(commandLine.hasOption("opt1"));
        Assert.assertFalse(commandLine.hasOption("opt2"));

        // test default value for option with value
        option1 = new Option("opt1", "option 1", true, "o1", "defaultValue");
        group = new GroupImpl(options, "group", "group description", 1, 2);

        commandLine.clear();
        group.defaults(commandLine);

        Assert.assertTrue(commandLine.hasOption("opt1", "defaultValue"));
    }

    
}