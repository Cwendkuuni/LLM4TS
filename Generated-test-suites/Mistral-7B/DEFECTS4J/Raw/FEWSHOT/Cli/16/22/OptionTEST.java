package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new TestOption();
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = new ArrayList<>(Arrays.asList("--test", "arg1", "arg2").listIterator());

        option.process(commandLine, args);

        assertEquals("--test", commandLine.getOptions().get(0).getPreferredName());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
    }

    @Test(expected = OptionException.class)
    public void testProcessInvalid() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = new ArrayList<>(Arrays.asList("--test", "arg1", "--test").listIterator());

        option.process(commandLine, args);
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        option.defaults(commandLine);

        assertNull(commandLine.getOptionValues("test"));
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = new ArrayList<>(Arrays.asList("--test", "arg1").listIterator());

        assertTrue(option.canProcess(commandLine, args));
    }

    @Test
    public void testCanProcessFalse() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = new ArrayList<>(Arrays.asList("--arg", "test").listIterator());

        assertFalse(option.canProcess(commandLine, args));
    }

    @Test
    public void testCanProcessNull() {
        WriteableCommandLine commandLine = null;
        ListIterator<String> args = new ArrayList<>(Arrays.asList("--test").listIterator()).listIterator();

        assertNull(option.canProcess(commandLine, args));
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();

        assertEquals(Arrays.asList("--test", "-t"), triggers);
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();

        assertEquals(Arrays.asList("--", "-"), prefixes);
    }

    @Test(expected = OptionException.class)
    public void testValidateNullCommandLine() {
        WriteableCommandLine commandLine = null;

        option.validate(commandLine);
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.putStringValue("test", "value");

        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalid() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.putStringValue("test", "invalid");

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = option.helpLines(0, new HashSet<DisplaySetting>(), null);

        assertEquals(1, helpLines.size());
        assertEquals("TEST\n", helpLines.get(0).getShortOption());
        assertEquals("test\n", helpLines.get(0).getLongOption());
        assertEquals("Test option\n", helpLines.get(0).getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, new HashSet<DisplaySetting>(), null);

        assertEquals("TEST (=VALUE)\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("TEST", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test option", option.getDescription());
    }

    @Test
    public void testGetId() {
        assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption() {
        Option testOption = new TestOption();
        Option anotherOption = new AnotherOption();

        List<Option> options = Arrays.asList(testOption, anotherOption).subList(0, 1);

        assertEquals(testOption, testOption.findOption("--test"));
    }

    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testHasParent() {
        Option groupOption = new GroupOption();
        option.setParent(groupOption);

        assertSame(groupOption, option.getParent());
    }
}

class TestOption implements Option {
    @Override
    public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
        // Do nothing
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
        // Do nothing
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, String argument) {
        return false;
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
        return false;
    }

    @Override
    public Set getTriggers() {
        return new HashSet(Arrays.asList("--test", "-t"));
    }

    @Override
    public Set getPrefixes() {
        return new HashSet(Arrays.asList("--", "-"));
    }

    @Override
    public void validate(WriteableCommandLine commandLine) throws OptionException {
        // Do nothing
    }

    @Override
    public List helpLines(int depth, Set helpSettings, Comparator comp) {
        return null;
    }

    @Override
    public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
        // Do nothing
    }

    @Override
    public String getPreferredName() {
        return "TEST";
    }

    @Override
    public String getDescription() {
        return "Test option";
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public Option findOption(String trigger) {
        return null;
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public Option getParent() {
        return null;
    }

    @Override
    public void setParent(Option parent) {
        // Do nothing
    }
}

class AnotherOption implements Option {
    // Implement the methods as needed
}

class GroupOption implements OptionGroup {
    // Implement the methods as needed
}