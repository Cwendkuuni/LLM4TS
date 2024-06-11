package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class GroupImplTest {

    @Test
    public void testGroupImplConstructor() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        options.add(new Option("opt2", "Option 2", false, "f"));
        Group group = new GroupImpl(options, "My Group", "Description for My Group");

        Assert.assertEquals("My Group", group.getPreferredName());
        Assert.assertEquals("Description for My Group", group.getDescription());
        Assert.assertNull(group.getMinimum());
        Assert.assertNull(group.getMaximum());
        Assert.assertNotNull(group.getOptions());
        Assert.assertNotNull(group.getTriggers());
        Assert.assertEquals(2, group.getTriggers().size());
        Assert.assertTrue(group.canProcess(new WriteableCommandLine(), "opt1"));
        Assert.assertTrue(group.canProcess(new WriteableCommandLine(), "opt2"));
        Assert.assertFalse(group.canProcess(new WriteableCommandLine(), "unknown"));
        Assert.assertSame(Collections.unmodifiableList(options), group.getOptions());
    }

    @Test
    public void testGroupImplCanProcessOption() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        Group group = new GroupImpl(options, "My Group", "Description for My Group");

        WriteableCommandLine commandLine = new WriteableCommandLine();

        Assert.assertTrue(group.canProcess(commandLine, "opt1"));
        Assert.assertFalse(group.canProcess(commandLine, "unknown"));
    }

    @Test(expected = OptionException.class)
    public void testGroupImplProcessInvalidNumberOfOptions() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        Group group = new GroupImpl(options, "My Group", "Description for My Group");

        WriteableCommandLine commandLine = new WriteableCommandLine();

        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        arguments.add("arg3");
        commandLine.parseArgumentList(arguments);

        group.process(commandLine, arguments.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testGroupImplProcessNonExistentOption() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        Group group = new GroupImpl(options, "My Group", "Description for My Group");

        WriteableCommandLine commandLine = new WriteableCommandLine();

        List arguments = new ArrayList();
        arguments.add("unknown");
        commandLine.parseArgumentList(arguments);

        group.process(commandLine, arguments.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testGroupImplProcessOptionAfterMaximum() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        options.add(new Option("opt2", "Option 2", true, "t"));
        Group group = new GroupImpl(options, "My Group", "Description for My Group");

        WriteableCommandLine commandLine = new WriteableCommandLine();

        List arguments = new ArrayList();
        arguments.add("opt1");
        arguments.add("opt1");
        arguments.add("opt1");
        commandLine.parseArgumentList(arguments);

        group.process(commandLine, arguments.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testGroupImplProcessOptionAfterMinimum() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        Group group = new GroupImpl(options, "My Group", "Description for My Group");

        WriteableCommandLine commandLine = new WriteableCommandLine();

        List arguments = new ArrayList();
        commandLine.parseArgumentList(arguments);

        group.process(commandLine, arguments.listIterator());
    }

    @Test
    public void testGroupImplProcessOption() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        Group group = new GroupImpl(options, "My Group", "Description for My Group");

        WriteableCommandLine commandLine = new WriteableCommandLine();

        List arguments = new ArrayList();
        arguments.add("opt1");
        commandLine.parseArgumentList(arguments);

        group.process(commandLine, arguments.listIterator());

        Assert.assertEquals(1, commandLine.getNumOptions());
        Assert.assertEquals("opt1", commandLine.getOptionValues(0)[0]);
    }

    @Test
    public void testGroupImplProcessAnonymousArgument() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", false, "f"));
        Group group = new GroupImpl(options, "My Group", "Description for My Group");

        WriteableCommandLine commandLine = new WriteableCommandLine();

        List arguments = new ArrayList();
        arguments.add("arg1");
        commandLine.parseArgumentList(arguments);

        group.process(commandLine, arguments.listIterator());

        Assert.assertEquals(1, commandLine.getNumArguments());
        Assert.assertEquals("arg1", commandLine.getArgumentValues(0)[0]);
    }

    @Test
    public void testGroupImplProcessAnonymousArguments() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", false, "f"));
        Group group = new GroupImpl(options, "My Group", "Description for My Group");

        WriteableCommandLine commandLine = new WriteableCommandLine();

        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine.parseArgumentList(arguments);

        group.process(commandLine, arguments.listIterator());

        Assert.assertEquals(2, commandLine.getNumArguments());
        Assert.assertEquals("arg1", commandLine.getArgumentValues(0)[0]);
        Assert.assertEquals("arg2", commandLine.getArgumentValues(1)[0]);
    }

    @Test
    public void testGroupImplValidate() throws OptionException {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        Group group = new GroupImpl(options, "My Group", "Description for My Group");

        WriteableCommandLine commandLine = new WriteableCommandLine();

        List arguments = new ArrayList();

        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }

        arguments.add("opt1");
        commandLine.parseArgumentList(arguments);
        group.validate(commandLine);

        // No exception should be thrown

        arguments.add("opt2");
        commandLine.parseArgumentList(arguments);

        try {
            group.validate(commandLine);
            Assert.fail("Expected OptionException: MissingOption");
        } catch (OptionException e) {
            // Expected exception
        }

        arguments.remove(arguments.size() - 1);
        commandLine.parseArgumentList(arguments);

        try {
            group.validate(commandLine);
            Assert.fail("Expected OptionException: TooFewOptions");
        } catch (OptionException e) {
            // Expected exception
        }
    }

    @Test
    public void testGroupImplAppendUsage() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        Group group = new GroupImpl(options, "My Group", "Description for My Group");

        List<String> expectedUsage = new ArrayList();
        expectedUsage.add("My Group:");
        expectedUsage.add("  Option 1");
        expectedUsage.add("      is an option");
        expectedUsage.add("      and its value is of type: java.lang.String");
        expectedUsage.add("");

        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, null, null);

        List usage = new ArrayList(Collections.list(buffer.toString().split("\\r\\n|\\n")));

        for (int i = 1; i < usage.size(); i++) {
            Assert.assertEquals((String) expectedUsage.get(i), (String) usage.get(i));
        }
    }

    @Test
    public void testGroupImplHelpLines() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        Group group = new GroupImpl(options, "My Group", "Description for My Group");

        List expectedHelpLines = new ArrayList();
        expectedHelpLines.add("My Group:");
        expectedHelpLines.add("  Option 1 -t Toggle Option 1");
        expectedHelpLines.add("");

        List helpLines = group.helpLines(0, null, null);

        for (int i = 0; i < helpLines.size(); i++) {
            Assert.assertEquals((String) expectedHelpLines.get(i), (String) helpLines.get(i));
        }

        expectedHelpLines.clear();
        expectedHelpLines.add("My Group:");
        expectedHelpLines.add("  Option 1 -t Toggle Option 1");
        expectedHelpLines.add("  Option 1 (value):");
        expectedHelpLines.add("      is an option");
        expectedHelpLines.add("      and its value is of type: java.lang.String");
        expectedHelpLines.add("");

        helpLines = group.helpLines(1, null, null);

        for (int i = 0; i < helpLines.size(); i++) {
            Assert.assertEquals((String) expectedHelpLines.get(i), (String) helpLines.get(i));
        }
    }
}