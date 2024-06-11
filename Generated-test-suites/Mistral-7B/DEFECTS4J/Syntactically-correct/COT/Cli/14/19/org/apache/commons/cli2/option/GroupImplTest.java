package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.help.HelpLine;
import org.apache.commons.cli2.option.GroupImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {

    @Test
    public void testGroupImplConstructionWithOptions() {
        String[] options = {"option1", "option2", "option3"};
        GroupImpl group = new GroupImpl(Arrays.asList(options), null, null, 1, 3);
        Assert.assertEquals(3, group.getOptions().size());
        Assert.assertEquals("option1", group.getOptions().get(0).getLongOpt());
        Assert.assertEquals("option2", group.getOptions().get(1).getLongOpt());
        Assert.assertEquals("option3", group.getOptions().get(2).getLongOpt());
    }

    @Test
    public void testGroupImplConstructionWithArgument() {
        String[] options = {"option1", "arg"};
        GroupImpl group = new GroupImpl(Arrays.asList(options), null, null, 1, 1);;
        Assert.assertEquals(2, group.getOptions().size());
        Assert.assertEquals("option1", group.getOptions().get(0).getLongOpt());
        Assert.assertNotNull(group.getAnonymous().get(0));
        Assert.assertEquals("arg", group.getAnonymous().get(0).getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGroupImplConstructionWithInvalidMinimum() {
        String[] options = {"option1"};
        new GroupImpl(Arrays.asList(options), null, null, -1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGroupImplConstructionWithInvalidMaximum() {
        String[] options = {"option1"};
        new GroupImpl(Arrays.asList(options), null, null, 1, -1);
    }

    @Test
    public void testGroupImplCanProcess() {
        String[] options = {"option1", "option2"};
        GroupImpl group = new GroupImpl(Arrays.asList(options), null, null, 1, 3);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        Assert.assertTrue(group.canProcess(commandLine, "option1"));
        Assert.assertTrue(group.canProcess(commandLine, "option2"));
        Assert.assertFalse(group.canProcess(commandLine, "nonexistentOption"));
    }

    @Test
    public void testGroupImplValidate() {
        String[] options = {"option1", "option2"};
        GroupImpl group = new GroupImpl(Arrays.asList(options), null, null, 1, 3);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.addOption("option1");
        commandLine.addOption("option2");

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testGroupImplValidateWithTooFewOptions() {
        String[] options = {"option1", "option2"};
        GroupImpl group = new GroupImpl(Arrays.asList(options), null, null, 1, 3);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.addOption("option1");

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testGroupImplValidateWithTooManyOptions() {
        String[] options = {"option1", "option2"};
        GroupImpl group = new GroupImpl(Arrays.asList(options), null, null, 1, 3);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        commandLine.addOption("option1");
        commandLine.addOption("option2");
        commandLine.addOption("nonexistentOption");

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testGroupImplProcessWithInvalidArgument() {
        String[] options = {"option1", "arg"};
        GroupImpl group = new GroupImpl(Arrays.asList(options), null, null, 1, 1);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator arguments = commandLine.getArguments();

        arguments.next();

        group.process(commandLine, arguments);
    }

    @Test
    public void testGroupImplProcessWithValidArguments() {
        String[] options = {"option1", "option2", "arg1", "arg2"};
        GroupImpl group = new GroupImpl(Arrays.asList(options), null, null, 1, 4);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator arguments = commandLine.getArguments();

        arguments.next(); // skip the command line
        arguments.next(); // skip the first option

        group.process(commandLine, arguments);

        Assert.assertEquals("arg1", commandLine.getOptionValues("arg1")[0]);
        Assert.assertEquals("arg2", commandLine.getOptionValues("arg2")[0]);
    }

    @Test
    public void testGroupImplAppendUsage() {
        String[] options = {"option1", "option2"};
        GroupImpl group = new GroupImpl(Arrays.asList(options), null, null, 1, 3);

        StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, null, null);

        Assert.assertEquals("group (" + ResourceConstants.UNDEFINED + ")\n" +
                "  option1\n" +
                "  option2\n", buffer.toString());
    }

    @Test
    public void testGroupImplHelpLines() {
        String[] options = {"option1", "option2"};
        GroupImpl group = new GroupImpl(Arrays.asList(options), null, null, 1, 3);

        List<HelpLine> helpLines = group.helpLines(1, null, null);

        Assert.assertEquals(2, helpLines.size());

        HelpLine helpLine1 = helpLines.get(0);
        Assert.assertEquals("group", helpLine1.getHeaders().get(0));
        Assert.assertEquals("", helpLine1.getHeaders().get(1));
        Assert.assertEquals("", helpLine1.getHeaders().get(2));
        Assert.assertEquals("", helpLine1.getDescription());
        Assert.assertEquals("option1\n", helpLine1.getOptionSpecs().get(0).getLongOpt());
        Assert.assertEquals("", helpLine1.getOptionSpecs().get(0).getDescription());
        Assert.assertEquals("option2\n", helpLine1.getOptionSpecs().get(1).getLongOpt());
        Assert.assertEquals("", helpLine1.getOptionSpecs().get(1).getDescription());

        HelpLine helpLine2 = helpLines.get(1);
        Assert.assertEquals("", helpLine2.getHeaders().get(0));
        Assert.assertEquals("", helpLine2.getHeaders().get(1));
        Assert.assertEquals("", helpLine2.getHeaders().get(2));
        Assert.assertEquals("", helpLine2.getDescription());
        Assert.assertEquals(null, helpLine2.getOptionSpecs());
    }
}