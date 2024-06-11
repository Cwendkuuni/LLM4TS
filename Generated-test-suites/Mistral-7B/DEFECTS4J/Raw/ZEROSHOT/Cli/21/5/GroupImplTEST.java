package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class GroupImplTest {
    private GroupImpl group;
    private Option option1;
    private Option option2;
    private List<Option> options;

    @Before
    public void setUp() {
        List<Option> optionList = new ArrayList<>();
        option1 = new OptionBuilder()
                .withLongOpt(" option1")
                .withDescription(" option1 description")
                .create();
        option2 = new OptionBuilder()
                .withLongOpt(" option2")
                .withDescription(" option2 description")
                .create();
        optionList.add(option1);
        optionList.add(option2);
        options = Collections.unmodifiableList(optionList);
        group = new GroupImpl(options, null, null, 1, Integer.MAX_VALUE, false);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseArgumentList(new String[]{"--option1"});
        assertTrue(group.canProcess(commandLine, "--option1"));
        commandLine.clear();
        commandLine.parseArgumentList(new String[]{"--option2"});
        assertTrue(group.canProcess(commandLine, "--option2"));
        commandLine.parseArgumentList(new String[]{"--option1", "--option2"});
        assertTrue(group.canProcess(commandLine, "--option1"));
        assertTrue(group.canProcess(commandLine, "--option2"));
        commandLine.parseArgumentList(new String[]{"--option1", "--option2", "--option1"});
        assertTrue(group.canProcess(commandLine, "--option1"));
        assertTrue(group.canProcess(commandLine, "--option2"));
        commandLine.parseArgumentList(new String[]{"--option2", "--option1"});
        assertTrue(group.canProcess(commandLine, "--option2"));
        assertTrue(group.canProcess(commandLine, "--option1"));
        commandLine.parseArgumentList(new String[]{"--option1", "--option2", "arg"});
        assertTrue(group.canProcess(commandLine, "--option1"));
        assertTrue(group.canProcess(commandLine, "--option2"));
        commandLine.parseArgumentList(new String[]{"arg"});
        assertFalse(group.canProcess(commandLine, "--option1"));
        assertFalse(group.canProcess(commandLine, "--option2"));
        commandLine.parseArgumentList(new String[]{"--invalidOption"});
        assertFalse(group.canProcess(commandLine, "--invalidOption"));
        commandLine.parseArgumentList(new String[]{"--option1", "--option1"});
        assertFalse(group.canProcess(commandLine, "--option1"));
        commandLine.parseArgumentList(new String[]{"--option1"});
        assertFalse(group.canProcess(commandLine, "--option2"));
        commandLine.parseArgumentList(new String[]{"--option2"});
        assertFalse(group.canProcess(commandLine, "--option1"));
        commandLine.parseArgumentList(new String[]{"--option1", "arg"});
        assertFalse(group.canProcess(commandLine, "--option1"));
        assertFalse(group.canProcess(commandLine, "--option2"));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = new HashSet<>();
        triggers.addAll(group.getTriggers());
        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
    }

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("--option1", "arg1", "--option2", "arg2").stream().map(String::new).collect(Collectors.toList());
        commandLine.parseArgumentList(arguments.toArray(String[]
                .class));
        List<String> processedArguments = new ArrayList<>();
        ListIterator<String> argumentsIterator = commandLine.getArgs().listIterator();
        group.process(commandLine, argumentsIterator);
        String currentArg = argumentsIterator.next();
        processedArguments.add(currentArg);
        String nextArg = argumentsIterator.next();
        while (nextArg != null) {
            processedArguments.add(nextArg);
            currentArg = nextArg;
            nextArg = argumentsIterator.next();
        }
        assertEquals(2, processedArguments.size());
        assertEquals("option1", processedArguments.get(0));
        assertEquals("arg1", processedArguments.get(1));
        commandLine.clear();
        arguments = Arrays.asList("--option2", "arg1", "--option1", "arg2").stream().map(String::new).collect(Collectors.toList());
        commandLine.parseArgumentList(arguments.toArray(String[]
                .class));
        processedArguments.clear();
        group.process(commandLine, argumentsIterator);
        currentArg = argumentsIterator.next();
        processedArguments.add(currentArg);
        nextArg = argumentsIterator.next();
        while (nextArg != null) {
            processedArguments.add(nextArg);
            currentArg = nextArg;
            nextArg = argumentsIterator.next();
        }
        assertEquals(2, processedArguments.size());
        assertEquals("option2", processedArguments.get(0));
        assertEquals("arg1", processedArguments.get(1));
        commandLine.clear();
        arguments = Arrays.asList("--option2", "--option1", "arg1", "arg2").stream().map(String::new).collect(Collectors.toList());
        commandLine.parseArgumentList(arguments.toArray(String[]
                .class));
        processedArguments.clear();
        group.process(commandLine, argumentsIterator);
        currentArg = argumentsIterator.next();
        processedArguments.add(currentArg);
        nextArg = argumentsIterator.next();
        while (nextArg != null) {
            processedArguments.add(nextArg);
            currentArg = nextArg;
            nextArg = argumentsIterator.next();
        }
        assertEquals(3, processedArguments.size());
        assertEquals("option2", processedArguments.get(0));
        assertEquals("option1", processedArguments.get(1));
        assertEquals("arg1", processedArguments.get(2));
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseArgumentList(new String[]{"--option1", "--option2"});
        group.validate(commandLine);
        commandLine = new WriteableCommandLine();
        commandLine.parseArgumentList(new String[]{"--option1", "--option2", "--option1"});
        group.validate(commandLine);
        commandLine = new WriteableCommandLine();
        commandLine.parseArgumentList(new String[]{"--option1"});
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Missing option: option2", e.getMessage());
        }
        commandLine.parseArgumentList(new String[]{"--option1", "--option2", "--option1", "--option3"});
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Too many options", e.getMessage());
        }
    }

    @Test
    public void testGetPreferredName() {
        String preferredName = group.getPreferredName();
        assertNull(preferredName);
    }

    @Test
    public void testGetDescription() {
        String description = group.getDescription();
        assertNull(description);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, null, null);
        String usage = buffer.toString();
        String expectedUsage = "| Group:\n" +
                "  option1 description\n" +
                "  option2 description\n";
        assertEquals(expectedUsage, usage);
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0, new HashSet<>(), null);
        assertEquals(2, helpLines.size());
        HelpLine helpLine1 = helpLines.get(0);
        HelpLine helpLine2 = helpLines.get(1);
        assertEquals("option1 description\n", helpLine1.getLongOpt());
        assertEquals("option1\n", helpLine1.getShortOpt());
        assertEquals("", helpLine1.getValuesDescription());
        assertEquals("option2 description\n", helpLine2.getLongOpt());
        assertEquals("option2\n", helpLine2.getShortOpt());
        assertEquals("", helpLine2.getValuesDescription());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = group.getAnonymous();
        assertTrue(anonymous.isEmpty());
    }

    @Test
    public void testFindOption() {
        Option foundOption = group.findOption("option1");
        assertEquals(option1, foundOption);
        foundOption = group.findOption("option3");
        assertNull(foundOption);
    }

    @Test
    public void testGetMinimum() {
        int minimum = group.getMinimum();
        assertEquals(1, minimum);
    }

    @Test
    public void testGetMaximum() {
        int maximum = group.getMaximum();
        assertEquals(Integer.MAX_VALUE, maximum);
    }

    @Test
    public void testIsRequired() {
        boolean required = group.isRequired();
        assertFalse(required);
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        group.defaults(commandLine);
        assertFalse(commandLine.hasOption("option1"));
        assertFalse(commandLine.hasOption("option2"));
    }
}

class ReverseStringComparator {
    // empty
}