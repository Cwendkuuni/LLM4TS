package org.apache.commons.cli2.option; // make sure the test file is in the same package as the class under test

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.ReverseStringComparator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class GroupImplTest {

    private List<Option> options;
    private Group group;
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        options = new ArrayList<>();
        options.add(new OptionBuilder("option1")
                .longOpt("longOption1")
                .description("Description for option1")
                .hasArg()
                .withArgName("ARG_NAME1")
                .create());

        options.add(new OptionBuilder("option2")
                .longOpt("longOption2")
                .description("Description for option2")
                .create());

        group = new GroupImpl(options, "My Group", "Description for My Group", 1, 3);
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testCanProcess_ValidOption() {
        group.canProcess(commandLine, "longOption1");
        Assert.assertTrue(true);
    }

    @Test
    public void testCanProcess_InvalidOption() {
        Assert.assertFalse(group.canProcess(commandLine, "invalidOption"));
    }

    @Test
    public void testCanProcess_Bursting() {
        commandLine.parse("--longOption1 ARG_NAME1 ARG_NAME1");
        Assert.assertTrue(group.canProcess(commandLine, "longOption1"));
        Assert.assertTrue(group.canProcess(commandLine, "longOption1"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_TooManyOptions() {
        commandLine.parse("--longOption1 ARG_NAME1 ARG_NAME1 ARG_NAME1");
        group.canProcess(commandLine, "longOption1");
        group.canProcess(commandLine, "longOption1");
        group.canProcess(commandLine, "longOption1");
    }

    @Test
    public void testCanProcess_Argument() {
        options.set(0, new ArgumentBuilder()
                .longOpt("arg1")
                .description("Description for arg1")
                .create());

        group = new GroupImpl(options, "My Group", "Description for My Group", 1, 3);
        commandLine.parse("--arg1 ARG_VALUE");
        Assert.assertTrue(group.canProcess(commandLine, "arg1"));
    }

    @Test
    public void testProcess_ValidArguments() {
        commandLine.parse("--longOption1 ARG_NAME1 VALUE_NAME1 --longOption2 VALUE_NAME2");
        group.process(commandLine, commandLine.iterator());
        Assert.assertTrue(true);
    }

    @Test(expected = OptionException.class)
    public void testProcess_InvalidArguments() {
        commandLine.parse("--longOption1 ARG_NAME1 VALUE_NAME1 VALUE_NAME2");
        group.process(commandLine, commandLine.iterator());
    }

    @Test
    public void testValidate_ValidOptions() {
        commandLine.parse("--longOption1 ARG_NAME1 VALUE_NAME1 --longOption2 VALUE_NAME2");
        group.validate(commandLine);
        Assert.assertTrue(true);
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidOptions() {
        commandLine.parse("--longOption1");
        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage_Defaults() {
        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);
        Assert.assertEquals(
                "[My Group (options):]\n" + // the name is optional
                        "  --option1 <ARG_NAME1> ARG_VALUE1 (Description for option1)\n" + // the description is present
                        "  --option2 (Description for option2)\n",
                buffer.toString());
    }

    @Test
    public void testAppendUsage_HelpSettings() {
        final StringBuffer buffer = new StringBuffer();
        final Set helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        group.appendUsage(buffer, helpSettings, null);
        Assert.assertEquals(
                "[My Group]\n" + // the name is mandatory
                        "Options:\n" +
                        "  --option1 <ARG_NAME1> ARG_VALUE1 (Description for option1)\n" + // the description is present
                        "  --option2 (Description for option2)\n",
                buffer.toString());
    }

    @Test
    public void testHelpLines_Defaults() {
        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        Assert.assertEquals(2, helpLines.size());

        final String helpLine1 = helpLines.get(0).getShortOption();
        Assert.assertEquals("option1", helpLine1);

        final String helpLine2 = helpLines.get(1).getShortOption();
        Assert.assertEquals("option2", helpLine2);
    }

    @Test
    public void testHelpLines_HelpSettings() {
        final List<HelpLine> helpLines = group.helpLines(0, new HashSet<>(Arrays.asList(DisplaySetting.DISPLAY_GROUP_NAME)), null);
        Assert.assertEquals(1, helpLines.size());

        final String helpLine = helpLines.get(0).getLongOption();
        Assert.assertEquals("My Group", helpLine);
    }
}