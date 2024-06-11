package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline. Arguments;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.commandline.OptionBuilder;
import org.apache.commons.cli2.commandline.OptionFactory;
import org.apache.commons.cli2.commandline.ParserResult;
import org.apache.commons.cli2.commandline.ValueDescription;
import org.apache.commons.cli2.helpers.GroupBuilder;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {
    private static final String GROUP_NAME = "group";
    private static final String GROUP_DESCRIPTION = "This is a test group.";

    private List<Option> options;
    private Group group;
    private Parser parser;
    private HelpFormatter helpFormatter;

    @Before
    public void setUp() {
        // Create some options for the group
        options = new ArrayList<>();
        options.add(OptionBuilder
                .withName("option1")
                .withDescription("This is an option in the group")
                .createOption());
        options.add(OptionBuilder
                .withName("option2")
                .withDescription("Another option in the group")
                .createOption());
        options.add(OptionBuilder
                .withName("option3")
                .withDescription("Yet another option in the group")
                .createOption());

        // Create the group with the options
        group = new GroupImpl(options, GROUP_NAME, GROUP_DESCRIPTION, 1, Integer.MAX_VALUE);

        // Create a parser to parse command line arguments with the group
        parser = new DefaultParser();
        parser.parseArguments(args -> group.parse(args), Arrays.asList(group));

        // Create a help formatter to print help output
        helpFormatter = new HelpFormatter();
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new DefaultCommandLine();

        // Test when the command line does not match any option
        assertFalse(group.canProcess(commandLine, "nonexistent"));

        // Test when the command line matches the first option in the group
        commandLine.parseArgument(0, "option1");
        assertTrue(group.canProcess(commandLine, "option1"));

        // Test when the command line matches the second option in the group
        commandLine.parseArgument(0, "option2");
        assertTrue(group.canProcess(commandLine, "option2"));

        // Test when the command line matches an option that is not in the group
        assertFalse(group.canProcess(commandLine, "nonexistent"));

        // Test when the command line matches an anonymous argument in the group
        commandLine.parseArgument(0, "arg1");
        assertTrue(group.canProcess(commandLine, "arg1"));
    }

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new DefaultCommandLine();

        // Test when the command line does not match any option
        assertNull(group.process(commandLine, commandLine.iterator()));

        // Test when the command line matches the first option in the group
        commandLine.parseArgument(0, "option1");
        assertEquals("option1", group.process(commandLine, commandLine.iterator()).getName());

        // Test when the command line matches the second option in the group
        commandLine.parseArgument(0, "option2");
        assertEquals("option2", group.process(commandLine, commandLine.iterator()).getName());

        // Test when the command line matches an option that is not in the group
        assertNull(group.process(commandLine, commandLine.iterator()));

        // Test when the command line matches an anonymous argument in the group
        commandLine.parseArgument(0, "arg1");
        assertEquals("arg1", group.process(commandLine, commandLine.iterator()).getName());
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new DefaultCommandLine();

        // Test when the command line has the required number of options
        for (Option option : options) {
            commandLine.parseArgument(0, option.getLongOpt());
        }
        group.validate(commandLine);

        // Test when the command line has fewer options than required
        for (int i = options.size(); i > 0; i--) {
            commandLine.parseArgument(0, options.get(i - 1).getLongOpt());
        }
        assertFalse(group.validate(commandLine).isSuccess());
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = new HashSet<>(options.stream().map(Option::getLongOpt).collect(Collectors.toList()));
        Set<String> groupTriggers = group.getTriggers();
        assertEquals(triggers, groupTriggers);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals(GROUP_NAME, group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals(GROUP_DESCRIPTION, group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer usage = new StringBuffer();
        group.appendUsage(usage, Collections.emptySet(), null);
        assertTrue(usage.toString().contains(GROUP_NAME));
        assertTrue(usage.toString().contains(GROUP_DESCRIPTION));
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(1, Collections.emptySet(), null);
        assertEquals(options.size() + 1, helpLines.size());
        for (HelpLine helpLine : helpLines) {
            if (helpLine.getOption() == null) {
                assertTrue(helpLine.getHeaders().get(0).equals(GROUP_NAME));
                assertTrue(helpLine.getHeaders().get(1).equals(GROUP_DESCRIPTION));
            } else {
                Option option = helpLine.getOption();
                assertEquals(option.getLongOpt(), helpLine.getOptionSpec().getLongOpt());
                assertEquals(option.getDescription(), helpLine.getOptionSpec().getDescription());
            }
        }
    }

    @Test
    public void testGetOptions() {
        List<Option> groupOptions = group.getOptions();
        assertEquals(options.size(), groupOptions.size());
        for (Option groupOption : groupOptions) {
            assertTrue(options.contains(groupOption));
        }
    }

    @Test
    public void testGetAnonymous() {
        List<Option> groupAnonymous = group.getAnonymous();
        assertTrue(groupAnonymous.isEmpty());
    }

    @Test
    public void testFindOption() {
        Option option1 = group.findOption("option1");
        assertNotNull(option1);
        assertEquals("option1", option1.getLongOpt());

        Option option2 = group.findOption("option2");
        assertNotNull(option2);
        assertEquals("option2", option2.getLongOpt());

        Option nonexistent = group.findOption("nonexistent");
        assertNull(nonexistent);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        group.defaults(commandLine);
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertTrue(commandLine.hasOption("option3"));
    }
}