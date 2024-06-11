package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;

public class GroupImplTest {

    private GroupImpl group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "o1"));
        options.add(new Option("opt2", "Option 2", false, "o2"));
        options.add(new Option("opt3", "Option 3", false, "o3"));
        options.add(new Option("arg1", "Argument 1", 1, "a1"));
        group = new GroupImpl(options, null, "TestGroup", 1, Integer.MAX_VALUE);
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testCanProcess_WithValidOption() {
        // Given
        String option = "opt1";

        // When
        boolean result = group.canProcess(commandLine, option);

        // Then
        Assert.assertTrue("Option should be processable", result);
    }

    @Test
    public void testCanProcess_WithInvalidOption() {
        // Given
        String option = "invalidOption";

        // When
        boolean result = group.canProcess(commandLine, option);

        // Then
        Assert.assertFalse("Invalid option should not be processable", result);
    }

    @Test
    public void testCanProcess_WithBurstableOption() {
        // Given
        String option = "opt2";
        String previousOption = "opt1";

        commandLine.parseArgument(previousOption);

        // When
        boolean result = group.canProcess(commandLine, option);

        // Then
        Assert.assertTrue("Burstable option should be processable", result);
    }

    @Test(expected = OptionException.class)
    public void testValidate_WithMinimumNumberOfOptions() {
        // Given

        // When
        group.validate(commandLine);

        // Then
        // Throws OptionException since minimum number of options is not met
    }

    @Test(expected = OptionException.class)
    public void testValidate_WithMaximumNumberOfOptions() {
        List options = new ArrayList(group.getOptions());
        options.add(new Option("opt5", "Option 5", false, "o5"));

        group = new GroupImpl(options, null, "TestGroup", 1, 4);
        commandLine.parseArgument("opt1");
        commandLine.parseArgument("opt2");
        commandLine.parseArgument("opt3");
        commandLine.parseArgument("opt4");

        // When
        group.validate(commandLine);

        // Then
        // Throws OptionException since maximum number of options is exceeded
    }

    @Test
    public void testProcess() {
        // Given
        String[] arguments = {"--opt1", "--arg1", "value1"};
        commandLine.parse(arguments);

        // When
        group.process(commandLine, commandLine.listIterator());

        // Then
        Assert.assertEquals("Option 1 should be set to true", true, commandLine.hasOption("opt1"));
        Assert.assertEquals("Argument 1 should have value 'value1'", "value1", commandLine.getOptionValue("arg1"));
    }

    @Test(expected = OptionException.class)
    public void testProcess_WithInvalidOption() {
        // Given
        String[] arguments = {"--invalidOption"};
        commandLine.parse(arguments);

        // When
        group.process(commandLine, commandLine.listIterator());

        // Then
        // Throws OptionException since invalid option is encountered
    }

    @Test
    public void testProcess_WithAnonymousArgument() {
        // Given
        String[] arguments = {"--arg1"};
        commandLine.parse(arguments);

        // When
        group.process(commandLine, commandLine.listIterator());

        // Then
        Assert.assertEquals("Argument 1 should have a value", true, commandLine.getOptionValue("arg1", "defaultValue").length() > 0);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);

        String expected = "TestGroup:\n" +
                "  OPTIONS:\n" +
                "    opt1: Option 1 (o1)\n" +
                "    opt2: Option 2\n" +
                "    opt3: Option 3\n" +
                "    arg1: Argument 1\n";

        // Then
        Assert.assertEquals("Usage should match the expected value", expected, buffer.toString());
    }
}