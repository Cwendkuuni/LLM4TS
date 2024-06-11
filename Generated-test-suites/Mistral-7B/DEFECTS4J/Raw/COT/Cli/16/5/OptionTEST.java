package org.apache.commons.cli2.test;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionTest {

    private Option option;
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        option = new TestOption();
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testProcess() {
        option.process(commandLine, Arrays.asList("test").listIterator());
        assertEquals("test", commandLine.getOptionValues(option.getId()).get(0));
    }

    @Test
    public void testDefaults() {
        option.defaults(commandLine);
        assertNull(commandLine.getOptionValues(option.getId()));
    }

    @Test
    public void testCanProcessWithSingleArgument() {
        assertTrue(option.canProcess(commandLine, Arrays.asList("test").listIterator()));
    }

    @Test
    public void testCanProcessWithMultipleArguments() {
        List<String> arguments = Arrays.asList("test1", "test2").asList();
        ListIterator<String> argumentsIterator = arguments.listIterator();

        assertTrue(option.canProcess(commandLine, argumentsIterator));
        assertEquals("test1", commandLine.getOptionValues(option.getId()).get(0));
        assertFalse(argumentsIterator.hasNext());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidArgument() {
        List<String> arguments = Arrays.asList("invalid").asList();
        ListIterator<String> argumentsIterator = arguments.listIterator();

        option.process(commandLine, argumentsIterator);
        fail("Should have thrown OptionException");
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();
        assertEquals(1, triggers.size());
        assertEquals("test", triggers.iterator().next());
    }

    @Test
    public void testGetPrefixes() {
        Set<Character> prefixes = option.getPrefixes();
        assertEquals(1, prefixes.size());
        assertEquals('-', prefixes.iterator().next());
    }

    @Test
    public void testValidate() {
        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidCommandLine() {
        commandLine.setOptionValue("invalidOption", "invalidValue");
        option.validate(commandLine);
        fail("Should have thrown OptionException");
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = option.helpLines(0, new HashSet<>(), null);
        assertEquals(1, helpLines.size());

        HelpLine helpLine = helpLines.get(0);
        assertEquals("test\n", helpLine.getShortOptionString());
        assertEquals("Test option", helpLine.getLongOptionString());
        assertEquals("Test description\n", helpLine.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, new HashSet<>(), null);
        assertEquals("test\n  Test description\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("test", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test description\n", option.getDescription());
    }

    @Test
    public void testgetId() {
        assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption() {
        Option anotherOption = new TestOption();
        option.addSubOption(anotherOption);

        Option foundOption = option.findOption("test");
        assertEquals(option, foundOption);

        Option foundAnotherOption = option.findOption("test2");
        assertEquals(anotherOption, foundAnotherOption);

        Option notFoundOption = option.findOption("notFound");
        assertNull(notFoundOption);
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }

    @Test
    public void testHasParent() {
        assertNull(option.getParent());
    }

    @Test
    public void testSetParent() {
        Option parentOption = new TestOption();
        option.setParent(parentOption);
        assertEquals(parentOption, option.getParent());
    }
}

class TestOption implements Option {
    @Override
    public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
        // Empty implementation
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
        // Empty implementation
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, String argument) {
        return true;
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
        return true;
    }

    @Override
    public Set getTriggers() {
        return new HashSet() {{
            add("test");
        }};
    }

    @Override
    public Set getPrefixes() {
        return new HashSet() {{
            add('-');
        }};
    }

    @Override
    public void validate(WriteableCommandLine commandLine) throws OptionException {
        // Empty implementation
    }

    @Override
    public List helpLines(int depth, Set helpSettings, Comparator comp) {
        return new ArrayList<>();
    }

    @Override
    public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
        // Empty implementation
    }

    @Override
    public String getPreferredName() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "Test description\n";
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public Option findOption(String trigger) {
        return this;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public Option getParent() {
        return null;
    }

    @Override
    public void setParent(Option parent) {
        // Empty implementation
    }

    @Override
    public void addSubOption(Option option) {
        // Empty implementation
    }
}