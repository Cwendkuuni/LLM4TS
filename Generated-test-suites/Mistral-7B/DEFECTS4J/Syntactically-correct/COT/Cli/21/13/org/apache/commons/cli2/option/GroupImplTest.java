package org.apache.commons.cli2.option;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static org.apache.commons.cli2.CliBuilder.newGroup;
import static org.apache.commons.cli2.CliBuilder.newOption;
import static org.apache.commons.cli2.CliBuilder.newRequiredOption;
import static org.apache.commons.cli2.CliBuilder.newValueOption;

public class GroupImplTest {

    @Test
    public void testConstructor() {
        final List options = Arrays.asList(
                newOption("o1", "Option 1", true),
                newOption("o2", "Option 2")
        );

        final Group group = newGroup("g")
                .required(false)
                .withOptions(options)
                .build();

        Assert.assertEquals("g", group.getName());
        Assert.assertEquals("Option 1 and Option 2", group.getDescription());
        Assert.assertEquals(2, group.getOptions().size());
        Assert.assertFalse(group.isRequired());
    }

    @Test
    public void testCanProcess() {
        final List options = Arrays.asList(
                newOption("o1", "Option 1", true),
                newOption("o2", "Option 2")
        );

        final Group group = newGroup("g")
                .required(false)
                .withOptions(options)
                .build();

        final WriteableCommandLine commandLine = new WriteableCommandLine();

        // Test valid option
        Assert.assertTrue(group.canProcess(commandLine, "o1"));

        // Test non-existent option
        Assert.assertFalse(group.canProcess(commandLine, "nonExistent"));

        // Test invalid option
        commandLine.addOption("o1");
        Assert.assertFalse(group.canProcess(commandLine, "o1"));

        // Test bursting
        commandLine.addOption("o1");
        commandLine.addOption("o2");
        Assert.assertTrue(group.canProcess(commandLine, "o2"));

        // Test anonymous argument
        final List anonymous = Arrays.asList(newArgument("a"));
        final Group groupWithAnonymous = newGroup("g")
                .required(false)
                .withOptions(options)
                .withAnonymous(anonymous)
                .build();

        Assert.assertTrue(groupWithAnonymous.canProcess(commandLine, "a"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMinimum() {
        final List options = Arrays.asList(newOption("o1", "Option 1"));

        newGroup("g")
                .required(false)
                .withOptions(options)
                .withMinimum(-1)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaximum() {
        final List options = Arrays.asList(newOption("o1", "Option 1"));

        newGroup("g")
                .required(false)
                .withOptions(options)
                .withMaximum(-1)
                .build();
    }

    @Test
    public void testProcess() {
        final List options = Arrays.asList(
                newOption("o1", "Option 1", true),
                newOption("o2", "Option 2")
        );

        final Group group = newGroup("g")
                .required(false)
                .withOptions(options)
                .build();

        final WriteableCommandLine commandLine = new WriteableCommandLine();

        final List args = Arrays.asList("g", "o1", "arg1", "o2", "arg2");
        group.process(commandLine, args.listIterator());

        Assert.assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getValuesForOption("o1"));
        Assert.assertEquals(Arrays.asList("arg2"), commandLine.getValuesForOption("o2"));

        // Test with anonymous argument
        final List anonymous = Arrays.asList(newArgument("a"));
        final Group groupWithAnonymous = newGroup("g")
                .required(false)
                .withOptions(options)
                .withAnonymous(anonymous)
                .build();

        final List argsWithAnonymous = Arrays.asList("g", "a", "arg");
        groupWithAnonymous.process(commandLine, argsWithAnonymous.listIterator());

        Assert.assertEquals(Arrays.asList("arg"), commandLine.getValuesForOption("a"));
    }

    @Test
    public void testValidate() {
        final List options = Arrays.asList(
                newOption("o1", "Option 1", true),
                newOption("o2", "Option 2")
        );

        final Group group = newGroup("g")
                .required(false)
                .withOptions(options)
                .build();

        final WriteableCommandLine commandLine = new WriteableCommandLine();

        final List args = Arrays.asList("g", "o1");
        group.validate(commandLine);

        Assert.assertEquals(1, commandLine.getValuesForOption("o1").size());

        // Test minimum
        final Group groupWithMinimum = newGroup("g")
                .required(false)
                .withOptions(options)
                .withMinimum(2)
                .build();

        final List argsWithMinimum = Arrays.asList("g", "o1");
        try {
            groupWithMinimum.validate(commandLine);
            Assert.fail();
        } catch (OptionException e) {
            Assert.assertEquals(ResourceConstants.MISSING_OPTION, e.getMessage());
        }

        // Test maximum
        final Group groupWithMaximum = newGroup("g")
                .required(false)
                .withOptions(options)
                .withMaximum(1)
                .build();

        final List argsWithMaximum = Arrays.asList("g", "o1", "o1");
        try {
            groupWithMaximum.validate(commandLine);
            Assert.fail();
        } catch (OptionException e) {
            Assert.assertEquals(ResourceConstants.TOO_MANY_OPTIONS, e.getMessage());
        }
    }

    @Test
    public void testAppendUsage() {
        final List options = Arrays.asList(
                newOption("o1", "Option 1", true),
                newOption("o2", "Option 2")
        );

        final Group group = newGroup("g")
                .required(false)
                .withOptions(options)
                .build();

        final StringBuilder buffer = new StringBuilder();

        group.appendUsage(buffer, Collections.emptySet(), null);

        Assert.assertEquals("g [o1] [o2]\n", buffer.toString());

        final Set helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        group.appendUsage(buffer, helpSettings, null);

        Assert.assertEquals("g (Option 1\n" +
                "  [o1]: ...\n" +
                "Option 2\n" +
                "  [o2]: ...\n)", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List options = Arrays.asList(
                newOption("o1", "Option 1", true),
                newOption("o2", "Option 2")
        );

        final Group group = newGroup("g")
                .required(false)
                .withOptions(options)
                .build();

        final List helpLines = group.helpLines(0, Collections.emptySet(), null);

        Assert.assertEquals(1, helpLines.size());
        Assert.assertEquals("g [o1] [o2]", helpLines.get(0).getShortDescription());

        final Set helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        final List helpLinesExpanded = group.helpLines(0, helpSettings, null);

        Assert.assertEquals(3, helpLinesExpanded.size());
        Assert.assertEquals("g (Option 1\n" +
                "  [o1]: ...\n" +
                "Option 2\n" +
                "  [o2]: ...\n)", helpLinesExpanded.get(0).getLongDescription());

        final List helpLinesOptions = group.helpLines(1, Collections.emptySet(), null);

        Assert.assertEquals(1, helpLinesOptions.size());
        Assert.assertEquals("Option 1\n" +
                "  [o1]: ...\n", helpLinesOptions.get(0).getLongDescription());

        final List helpLinesOptionsExpanded = group.helpLines(1, helpSettings, null);

        Assert.assertEquals(2, helpLinesOptionsExpanded.size());
        Assert.assertEquals("Option 1\n" +
                "  [o1]: ...\n" +
                "    [BOOLEAN, ...]: ...\n", helpLinesOptionsExpanded.get(0).getLongDescription());

        final List helpLinesSecondOption = group.helpLines(2, Collections.emptySet(), null);

        Assert.assertEquals(1, helpLinesSecondOption.size());
        Assert.assertEquals("Option 2\n" +
                "  [o2]: ...\n", helpLinesSecondOption.get(0).getLongDescription());
    }

    @Test
    public void testGetOptions() {
        final List options = Arrays.asList(
                newOption("o1", "Option 1", true),
                newOption("o2", "Option 2")
        );

        final Group group = newGroup("g")
                .required(false)
                .withOptions(options)
                .build();

        Assert.assertEquals(options, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        final List anonymous = Arrays.asList(newArgument("a"));

        final Group group = newGroup("g")
                .required(false)
                .withOptions(Collections.emptyList())
                .withAnonymous(anonymous)
                .build();

        Assert.assertEquals(anonymous, group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        final List options = Arrays.asList(
                newOption("o1", "Option 1", true),
                newOption("o2", "Option 2")
        );

        final Group group = newGroup("g")
                .required(false)
                .withOptions(options)
                .build();

        final Option option1 = group.findOption("o1");
        Assert.assertNotNull(option1);
        Assert.assertEquals("Option 1", option1.getLongOpt());

        final Option option2 = group.findOption("o2");
        Assert.assertNotNull(option2);
        Assert.assertEquals("Option 2", option2.getLongOpt());

        final Option nonExistent = group.findOption("nonExistent");
        Assert.assertNull(nonExistent);
    }

    @Test
    public void testGetMinimum() {
        final Group group = newGroup("g")
                .required(false)
                .withMinimum(1)
                .build();

        Assert.assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        final Group group = newGroup("g")
                .required(false)
                .withMaximum(1)
                .build();

        Assert.assertEquals(1, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        final List options = Arrays.asList(
                newOption("o1", "Option 1", true),
                newOption("o2", "Option 2")
        );

        final Group group = newGroup("g")
                .required(true)
                .withOptions(options)
                .build();

        Assert.assertTrue(group.isRequired());
        Assert.assertTrue(newGroup("g")
                .required(false)
                .withOptions(options)
                .build().isRequired());

        final List optionsWithMinimum = Arrays.asList(
                newOption("o1", "Option 1", true),
                newOption("o2", "Option 2", 1)
        );

        Assert.assertFalse(newGroup("g")
                .required(false)
                .withOptions(optionsWithMinimum)
                .build().isRequired());

        final List optionsWithMinimumAndMaximum = Arrays.asList(
                newOption("o1", "Option 1", true),
                newOption("o2", "Option 2", 1, 2)
        );

        Assert.assertFalse(newGroup("g")
                .required(false)
                .withOptions(optionsWithMinimumAndMaximum)
                .build().isRequired());
    }

    @Test
    public void testDefaults() {
        final List options = Arrays.asList(
                newOption("o1", "Option 1", true),
                newOption("o2", "Option 2")
        );

        final Group group = newGroup("g")
                .required(false)
                .withOptions(options)
                .build();

        group.defaults(new WriteableCommandLine());

        Assert.assertFalse(new WriteableCommandLine().hasOption("o1"));
        Assert.assertFalse(new WriteableCommandLine().hasOption("o2"));

        for (final Option option : options) {
            option.defaults(new WriteableCommandLine());
        }

        group.defaults(new WriteableCommandLine());

        Assert.assertFalse(new WriteableCommandLine().hasOption("o1"));
        Assert.assertFalse(new WriteableCommandLine().hasOption("o2"));

        final Option optionWithValue = newOption("o3", "Option 3", new ValueOption("val"));

        final List optionsWithValue = Arrays.asList(
                newOption("o1", "Option 1", true),
                optionWithValue
        );

        final Group groupWithValue = newGroup("g")
                .required(false)
                .withOptions(optionsWithValue)
                .build();

        groupWithValue.defaults(new WriteableCommandLine());

        Assert.assertFalse(new WriteableCommandLine().hasOption("o1"));
        Assert.assertFalse(new WriteableCommandLine().hasOption("o3"));
        Assert.assertNull(new WriteableCommandLine().getValueForOption("o3"));
    }
}

class ReverseStringComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o1.compareTo(new StringBuilder(o2).reverse().toString());
    }
}