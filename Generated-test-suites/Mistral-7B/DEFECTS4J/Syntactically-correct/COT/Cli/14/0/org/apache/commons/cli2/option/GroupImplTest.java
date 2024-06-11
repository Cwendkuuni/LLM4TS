package org.apache.commons.cli2.option;

import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

import static org.apache.commons.cli2.option.GroupImpl.ReverseStringComparator.getInstance;

public class GroupImplTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testGroupImplConstructor() {
        List options = new ArrayList();

        Option option1 = new Option("opt1", "Option 1");
        options.add(option1);

        Option option2 = new Option("opt2", "Option 2");
        options.add(option2);

        Group group = new GroupImpl(options, "MyGroup", "Description of MyGroup", 1, 2);

        Assert.assertEquals("MyGroup", group.getPreferredName());
        Assert.assertEquals("Description of MyGroup", group.getDescription());
        Assert.assertEquals(2, group.getOptions().size());
        Assert.assertEquals(1, group.getMinimum());
        Assert.assertEquals(2, group.getMaximum());
        Assert.assertFalse(group.isRequired());
    }

    @Test
    public void testCanProcess() {
        List options = new ArrayList();

        Option opt1 = new Option("opt1", "Option 1");
        options.add(opt1);

        Group group = new GroupImpl(options, "MyGroup", "Description of MyGroup", 1, 2);

        String[] arguments = {"opt1", "arg1"};

        Assert.assertTrue(group.canProcess(commandLine, arguments[0]));
        Assert.assertTrue(opt1.canProcess(commandLine, arguments[0]));
        Assert.assertFalse(group.canProcess(commandLine, "invalidOption"));
        Assert.assertFalse(opt1.canProcess(commandLine, "invalidOption"));
    }

    @Test(expected = OptionException.class)
    public void testValidate() throws OptionException {
        List options = new ArrayList();

        Option opt1 = new Option("opt1", "Option 1");
        options.add(opt1);

        Group group = new GroupImpl(options, "MyGroup", "Description of MyGroup", 2, 2);

        String[] arguments = {"opt1", "arg1", "arg2"};

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewArguments() throws OptionException {
        List options = new ArrayList();

        Option opt1 = new Option("opt1", "Option 1");
        options.add(opt1);

        Group group = new GroupImpl(options, "MyGroup", "Description of MyGroup", 2, 2);

        String[] arguments = {"arg1"};

        group.validate(commandLine);
    }

    @Test
    public void testProcess() throws OptionException {
        List options = new ArrayList();

        Option opt1 = new Option("opt1", "Option 1");
        options.add(opt1);

        Group group = new GroupImpl(options, "MyGroup", "Description of MyGroup", 1, 2);

        String[] arguments = {"opt1", "arg1"};

        group.process(commandLine, commandLine.listIterator());

        Assert.assertEquals("opt1=arg1", commandLine.getArgs().get(0));
        Assert.assertEquals(opt1, commandLine.getOption("opt1"));
    }

    @Test
    public void testProcessWithMultipleArguments() throws OptionException {
        List options = new ArrayList();

        Option opt1 = new Option("opt1", "Option 1");
        options.add(opt1);

        Option opt2 = new Option("opt2", "Option 2");
        options.add(opt2);

        Group group = new GroupImpl(options, "MyGroup", "Description of MyGroup", 1, 2);

        String[] arguments = {"opt1", "arg1", "arg2"};

        group.process(commandLine, commandLine.listIterator());

        Assert.assertEquals("opt1=arg1, opt2=arg2", commandLine.getArgs().get(0));
        Assert.assertEquals(opt1, commandLine.getOption("opt1"));
        Assert.assertEquals(opt2, commandLine.getOption("opt2"));
    }

    @Test
    public void testAppendUsage() {
        List options = new ArrayList();

        Option opt1 = new Option("opt1", "Option 1");
        options.add(opt1);

        Group group = new GroupImpl(options, "MyGroup", "Description of MyGroup", 1, 2);

        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();

        group.appendUsage(buffer, helpSettings, null);

        String expectedUsage = "[MyGroup]";
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            expectedUsage = "[MyGroup]\n";
        }

        Assert.assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List options = new ArrayList();

        Option opt1 = new Option("opt1", "Option 1");
        options.add(opt1);

        Group group = new GroupImpl(options, "MyGroup", "Description of MyGroup", 1, 2);

        List helpLines = group.helpLines(0, Collections.emptySet(), null);

        String expectedUsage = "MyGroup:\n" +
                "  Option 1 - Option 1\n";

        for (Object o : helpLines) {
            Assert.assertEquals(expectedUsage, ((HelpLine) o).getHelp());
        }
    }
}