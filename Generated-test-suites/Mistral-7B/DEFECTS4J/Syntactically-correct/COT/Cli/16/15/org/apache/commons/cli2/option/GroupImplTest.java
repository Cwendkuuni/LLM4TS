package org.apache.commons.cli2.option;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static org.apache.commons.cli2.Group.DEFAULT_MINIMUM;
import static org.apache.commons.cli2.Group.DEFAULT_MAXIMUM;
import static org.apache.commons.cli2.Group.DEFAULT_IS_REQUIRED;

public class GroupImplTest {
    // Test data
    private GroupImpl group;
    private List<Option> options;

    @Before
    public void setUp() {
        options = Arrays.asList(
                new Option("o1", "Option 1", true),
                new Option("o2", "Option 2", false),
                new Option("o3", "Option 3", false),
                new Option("o4", "Option 4", true),
                new Option("o5", "Option 5", false)
        );

        group = new GroupImpl(options, "MyGroup", "My Group description", DEFAULT_MINIMUM, DEFAULT_MAXIMUM);
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("MyGroup", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals("My Group description", group.getDescription());
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        // Test canProcess with valid options
        Assert.assertTrue(group.canProcess(commandLine, "o1"));
        Assert.assertTrue(group.canProcess(commandLine, "o2"));
        Assert.assertTrue(group.canProcess(commandLine, "o3"));
        Assert.assertTrue(group.canProcess(commandLine, "o4"));
        Assert.assertTrue(group.canProcess(commandLine, "o5"));

        // Test canProcess with invalid options
        Assert.assertFalse(group.canProcess(commandLine, "invalidOption"));

        // Test canProcess with missing options
        Assert.assertFalse(group.canProcess(commandLine, null));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithTooFewOptions() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("arg1", "arg2");
        commandLine.parseArgumentList(arguments);

        group.process(commandLine, arguments.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithTooManyOptions() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("arg1", "arg2", "arg3", "arg4", "arg5", "arg6");
        commandLine.parseArgumentList(arguments);

        group.process(commandLine, arguments.listIterator());
    }

    @Test
    public void testProcessWithValidOptions() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("--o1", "--o2", "--o3");
        commandLine.parseArgumentList(arguments);

        group.process(commandLine, arguments.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidOptions() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("--invalidOption", "--o1");
        commandLine.parseArgumentList(arguments);

        group.process(commandLine, arguments.listIterator());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewOptions() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("arg1", "arg2");
        commandLine.parseArgumentList(arguments);

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyOptions() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("arg1", "arg2", "arg3", "arg4", "arg5");
        commandLine.parseArgumentList(arguments);

        group.validate(commandLine);
    }

    @Test
    public void testValidateWithValidOptions() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("--o1", "--o2", "--o3", "--o4", "--o5");
        commandLine.parseArgumentList(arguments);

        group.validate(commandLine);
    }
}