package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Test;

public class GroupImplTest {
    @Test
    public void testGroupImplWithOptions() {
        final String groupName = "my_group";
        final String groupDescription = "Description of my_group";

        final List<Option> options = Arrays.asList(
                OptionBuilder.newOption("option1", "Option 1")
                        .longOpt("long-option1")
                        .build(),
                OptionBuilder.newOption("option2", "Option 2")
                        .longOpt("long-option2")
                        .build(),
                OptionBuilder.newOption("option3", "Option 3")
                        .longOpt("long-option3")
                        .build()
        );

        final GroupImpl group = new GroupImpl(options, groupName, groupDescription, 1, 3);

        assertEquals(groupName, group.getPreferredName());
        assertEquals(groupDescription, group.getDescription());
        assertEquals(options, group.getOptions());
        assertEquals(1, group.getMinimum());
        assertEquals(3, group.getMaximum());
        assertFalse(group.isRequired());

        assertTrue(group.canProcess(createCommandLine(), "option1"));
        assertTrue(group.canProcess(createCommandLine(), "option2"));
        assertTrue(group.canProcess(createCommandLine(), "option3"));
        assertFalse(group.canProcess(createCommandLine(), "unknown"));

        final Set<String> prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());

        group.process(createCommandLine(), createListIterator());
        assertEquals(3, group.getOptions().size());

        group.validate(createCommandLine());

        group.appendUsage(createStringBuffer(), Collections.emptySet(), null);
    }

    @Test
    public void testGroupImplWithArguments() {
        final String groupName = "my_group";
        final String groupDescription = "Description of my_group";

        final List<Option> options = Arrays.asList();

        final List<Argument> arguments = Arrays.asList(
                OptionBuilder.newArgument("arg1", "Argument 1")
                        .build(),
                OptionBuilder.newArgument("arg2", "Argument 2")
                        .build()
        );

        final GroupImpl group = new GroupImpl(options, groupName, groupDescription, 0, 0, arguments);

        assertEquals(groupName, group.getPreferredName());
        assertEquals(groupDescription, group.getDescription());
        assertEquals(Collections.emptyList(), group.getOptions());
        assertEquals(0, group.getMinimum());
        assertEquals(0, group.getMaximum());
        assertFalse(group.isRequired());
        assertSame(arguments, group.getAnonymous());

        assertTrue(group.canProcess(createCommandLine(), "arg1"));
        assertTrue(group.canProcess(createCommandLine(), "arg2"));
        assertFalse(group.canProcess(createCommandLine(), "unknown"));

        final Set<String> prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());

        group.process(createCommandLine(), createListIterator());
        assertEquals(arguments.size(), group.getAnonymous().size());

        group.validate(createCommandLine());

        group.appendUsage(createStringBuffer(), Collections.emptySet(), null);
    }

    @Test(expected = OptionException.class)
    public void testGroupImplWithInvalidOptions() {
        final String groupName = "my_group";
        final String groupDescription = "Description of my_group";

        final List<Option> options = Arrays.asList(
                OptionBuilder.newOption("option1", "Option 1")
                        .longOpt("long-option1")
                        .build(),
                OptionBuilder.newOption("option2", "Option 2")
                        .longOpt("long-option2")
                        .build(),
                OptionBuilder.newOption("option3", "Option 3")
                        .longOpt("long-option3")
                        .build(),
                OptionBuilder.newOption("option4", "Option 4")
                        .longOpt("long-option4")
                        .build()
        );

        final GroupImpl group = new GroupImpl(options, groupName, groupDescription, 1, 3);

        assertTrue(group.canProcess(createCommandLine(), "option1"));
        assertTrue(group.canProcess(createCommandLine(), "option2"));
        assertTrue(group.canProcess(createCommandLine(), "option3"));

        // should be an error since the max is 3
        group.process(createCommandLine(), createListIterator());
        group.process(createCommandLine(), createListIterator());
        group.process(createCommandLine(), createListIterator());
        group.process(createCommandLine(), createListIterator());
    }

    @Test
    public void testGroupImplValidation() {
        final String groupName = "my_group";
        final String groupDescription = "Description of my_group";

        final List<Option> options = Arrays.asList(
                OptionBuilder.newOption("option1", "Option 1")
                        .longOpt("long-option1")
                        .build(),
                OptionBuilder.newOption("option2", "Option 2")
                        .longOpt("long-option2")
                        .isRequired()
                        .build()
        );

        final GroupImpl group = new GroupImpl(options, groupName, groupDescription, 1, 3);

        assertThrows(OptionException.class, () -> group.validate(createCommandLine()));
    }

    @Test
    public void testGroupImplValidationWithRequiredOptionMissing() {
        final String groupName = "my_group";
        final String groupDescription = "Description of my_group";

        final List<Option> options = Arrays.asList(
                OptionBuilder.newOption("option1", "Option 1")
                        .longOpt("long-option1")
                        .isRequired()
                        .build()
        );

        final GroupImpl group = new GroupImpl(options, groupName, groupDescription, 1, 3);

        assertThrows(OptionException.class, () -> group.validate(createCommandLine()));
    }

    @Test
    public void testGroupImplValidationWithExtraOptions() {
        final String groupName = "my_group";
        final String groupDescription = "Description of my_group";

        final List<Option> options = Arrays.asList(
                OptionBuilder.newOption("option1", "Option 1")
                        .longOpt("long-option1")
                        .build(),
                OptionBuilder.newOption("option2", "Option 2")
                        .longOpt("long-option2")
                        .build()
        );

        final WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.parse(options);
        commandLine.addOption("unknown");

        final GroupImpl group = new GroupImpl(options, groupName, groupDescription, 1, 3);

        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testGroupImplValidationWithMissingOptions() {
        final String groupName = "my_group";
        final String groupDescription = "Description of my_group";

        final List<Option> options = Arrays.asList(
                OptionBuilder.newOption("option1", "Option 1")
                        .longOpt("long-option1")
                        .build(),
                OptionBuilder.newOption("option2", "Option 2")
                        .longOpt("long-option2")
                        .isRequired()
                        .build()
        );

        final WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.parse(options);
        commandLine.removeOption("option2");

        final GroupImpl group = new GroupImpl(options, groupName, groupDescription, 1, 3);

        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testGroupImplHelp() {
        final String groupName = "my_group";
        final String groupDescription = "Description of my_group";

        final List<Option> options = Arrays.asList(
                OptionBuilder.newOption("option1", "Option 1")
                        .longOpt("long-option1")
                        .build(),
                OptionBuilder.newOption("option2", "Option 2")
                        .longOpt("long-option2")
                        .build()
        );

        final GroupImpl group = new GroupImpl(options, groupName, groupDescription, 1, 3);

        final List<HelpLine> helpLines = group.helpLines(1, Collections.emptySet(), null);

        assertEquals(helpLines.size(), 4);
        assertEquals(groupName, helpLines.get(0).getOptionSpec().getLongOpt());
        assertEquals("Description of my_group", helpLines.get(0).getLongDescription());
        assertEquals("", helpLines.get(0).getShortDescription());
        assertFalse(helpLines.get(0).hasOptionName());
        assertFalse(helpLines.get(0).hasOptionDescription());

        assertEquals("Option 1", helpLines.get(1).getOptionSpec().getLongOpt());
        assertEquals("", helpLines.get(1).getLongDescription());
        assertEquals("Option 1", helpLines.get(1).getShortDescription());
        assertFalse(helpLines.get(1).hasOptionName());
        assertFalse(helpLines.get(1).hasOptionDescription());

        assertEquals("Option 2", helpLines.get(2).getOptionSpec().getLongOpt());
        assertEquals("", helpLines.get(2).getLongDescription());
        assertEquals("Option 2", helpLines.get(2).getShortDescription());
        assertFalse(helpLines.get(2).hasOptionName());
        assertFalse(helpLines.get(2).hasOptionDescription());

        assertEquals("", helpLines.get(3).getOptionSpec().getLongOpt());
        assertEquals("", helpLines.get(3).getLongDescription());
        assertEquals("", helpLines.get(3).getShortDescription());
        assertFalse(helpLines.get(3).hasOptionName());
        assertFalse(helpLines.get(3).hasOptionDescription());
    }

    @Test
    public void testGroupImplHelpWithArguments() {
        final String groupName = "my_group";
        final String groupDescription = "Description of my_group";

        final List<Option> options = Collections.emptyList();

        final List<Argument> arguments = Arrays.asList(
                OptionBuilder.newArgument("arg1", "Argument 1")
                        .build(),
                OptionBuilder.newArgument("arg2", "Argument 2")
                        .build()
        );

        final GroupImpl group = new GroupImpl(options, groupName, groupDescription, 0, 0, arguments);

        final List<HelpLine> helpLines = group.helpLines(1, Collections.emptySet(), null);

        assertEquals(helpLines.size(), 4);
        assertEquals(groupName, helpLines.get(0).getOptionSpec().getLongOpt());
        assertEquals("Description of my_group", helpLines.get(0).getLongDescription());
        assertEquals("", helpLines.get(0).getShortDescription());
        assertFalse(helpLines.get(0).hasOptionName());
        assertFalse(helpLines.get(0).hasOptionDescription());

        assertEquals("Argument 1", helpLines.get(1).getOptionSpec().getLongOpt());
        assertEquals("Argument 1", helpLines.get(1).getShortDescription());
        assertEquals("", helpLines.get(1).getLongDescription());
        assertFalse(helpLines.get(1).hasOptionName());
        assertFalse(helpLines.get(1).hasOptionDescription());

        assertEquals("Argument 2", helpLines.get(2).getOptionSpec().getLongOpt());
        assertEquals("Argument 2", helpLines.get(2).getShortDescription());
        assertEquals("", helpLines.get(2).getLongDescription());
        assertFalse(helpLines.get(2).hasOptionName());
        assertFalse(helpLines.get(2).hasOptionDescription());

        assertEquals("", helpLines.get(3).getOptionSpec().getLongOpt());
        assertEquals("", helpLines.get(3).getLongDescription());
        assertEquals("", helpLines.get(3).getShortDescription());
        assertFalse(helpLines.get(3).hasOptionName());
        assertFalse(helpLines.get(3).hasOptionDescription());
    }

    private WriteableCommandLine createCommandLine() {
        return new DefaultCommandLine();
    }

    private ListIterator<String> createListIterator() {
        return new ArrayList<String>().listIterator();
    }

    private StringBuffer createStringBuffer() {
        return new StringBuffer();
    }
}