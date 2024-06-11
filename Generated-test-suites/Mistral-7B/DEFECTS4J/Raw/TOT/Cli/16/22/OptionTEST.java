package org.apache.commons.cli2.test;

import org.apache.commons.cli2.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.cli2.CliFactory.withDefaults;
import static org.apache.commons.cli2.TestUtils.commandLine;
import static org.apache.commons.cli2.TestUtils.writeableCommandLine;
import static org.junit.Assert.*;

/**
 * Test cases for {@link Option}.
 */
public class OptionTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = writeableCommandLine();
    }

    /**
     * Test for {@link Option#process(WriteableCommandLine, ListIterator)}.
     */
    @Test
    public void testProcess() {
        Option option = new MockOption();
        List<String> args = Arrays.asList("--test", "arg1", "--test2", "arg2").listIterator();

        option.process(commandLine, args);

        assertEquals("--test=arg1,--test=arg2", commandLine.getOptions().toString());
        assertEquals("arg1,arg2", commandLine.getArgs().get(0));
        assertEquals(1, commandLine.getArgs().size());
    }

    /**
     * Test for {@link Option#defaults(WriteableCommandLine)}.
     */
    @Test
    public void testDefaults() {
        Option option = new MockOption();
        option.defaults(commandLine);

        assertNull(commandLine.getOptions().get("test"));
    }

    /**
     * Test for {@link Option#canProcess(WriteableCommandLine, String)}.
     */
    @Test
    public void testCanProcessString() {
        Option option = new MockOption();

        assertTrue(option.canProcess(commandLine, "--test"));
        assertFalse(option.canProcess(commandLine, "--other"));
    }

    /**
     * Test for {@link Option#canProcess(WriteableCommandLine, ListIterator)}.
     */
    @Test
    public void testCanProcessListIterator() {
        Option option = new MockOption();
        List<String> args = Arrays.asList("--test", "arg1", "--test2", "arg2").listIterator();

        assertTrue(option.canProcess(commandLine, args));
        assertFalse(option.canProcess(commandLine, args)); // consumes the first argument

        List<String> remainingArgs = new ArrayList<>(Arrays.asList("--other", "arg3"));
        args = commandLine.getArguments().listIterator();
        for (String arg : remainingArgs) {
            args.next();
        }

        assertFalse(option.canProcess(commandLine, args));
    }

    /**
     * Test for {@link Option#getTriggers()}.
     */
    @Test
    public void testGetTriggers() {
        Option option = new MockOption();
        Set<String> triggers = option.getTriggers();

        assertTrue(triggers.contains("--test"));
        assertTrue(triggers.contains("-t"));
    }

    /**
     * Test for {@link Option#getPrefixes()}.
     */
    @Test
    public void testGetPrefixes() {
        Option option = new MockOption();
        Set<String> prefixes = option.getPrefixes();

        assertTrue(prefixes.contains("--"));
        assertTrue(prefixes.contains("-"));
    }

    /**
     * Test for {@link Option#validate(WriteableCommandLine)}.
     */
    @Test(expected = OptionException.class)
    public void testValidate() {
        Option option = new MockInvalidOption();
        option.validate(commandLine);
    }

    /**
     * Test for {@link Option#helpLines(int, Set, Comparator)}.
     */
    @Test
    public void testHelpLines() {
        Option option = new MockOption();
        List<HelpLine> helpLines = option.helpLines(0, new HashSet<>(), null);

        assertEquals(1, helpLines.size());
        assertEquals("--test", helpLines.get(0).getOptionSpec().getLongOpt());
        assertEquals("test option\n", helpLines.get(0).getShortOptionName());
        assertEquals("test option\n", helpLines.get(0).getFullDescription());
    }

    /**
     * Test for {@link Option#appendUsage(StringBuffer, Set, Comparator)}.
     */
    @Test
    public void testAppendUsage() {
        Option option = new MockOption();
        StringBuffer buffer = new StringBuffer();

        option.appendUsage(buffer, new HashSet<>(), null);

        assertEquals("--test:\n" +
                "  test option\n", buffer.toString());
    }

    /**
     * Test for {@link Option#getId()}.
     */
    @Test
    public void testGetId() {
        Option option = new MockOption();

        assertEquals(1, option.getId());
    }

    /**
     * Test for {@link Option#findOption(String)}.
     */
    @Test
    public void testFindOption() {
        Option option1 = new MockOption();
        Option option2 = new MockOption();
        Option option3 = new MockOption();

        Cli cli = withDefaults();
        cli.registerOption(option1);
        cli.registerOption(option2);
        cli.registerOption(option3);

        Option result = cli.findOption("--test");

        assertEquals(option1, result);
    }

    /**
     * Test for {@link Option#isRequired()}.
     */
    @Test
    public void testIsRequired() {
        Option option = new MockOptionalOption();

        assertFalse(option.isRequired());
    }

    /**
     * Test for {@link Option#getParent()}.
     */
    @Test
    public void testGetParent() {
        Option parentOption = new MockParentOption();
        Option childOption = new MockChildOption();
        parentOption.setParent(childOption);

        assertEquals(childOption, parentOption.getParent());
    }

}

class MockOption implements Option {

    @Override
    public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
        // no-op
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
        // no-op
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, String argument) {
        return true;
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
        return true;
    }

    @Override
    public Set<String> getTriggers() {
        return new HashSet<>(Arrays.asList("--test", "-t"));
    }

    @Override
    public Set<String> getPrefixes() {
        return new HashSet<>(Arrays.asList("--", "-"));
    }

    @Override
    public void validate(WriteableCommandLine commandLine) throws OptionException {
        // no-op
    }

    @Override
    public List helpLines(int depth, Set<DisplaySetting> helpSettings, Comparator<Option> comp) {
        return null;
    }

    @Override
    public void appendUsage(StringBuffer buffer, Set<DisplaySetting> helpSettings, Comparator<Option> comp) {
        // no-op
    }

    @Override
    public String getPreferredName() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "test option";
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
        return false;
    }

    @Override
    public Option getParent() {
        return null;
    }

    @Override
    public void setParent(Option parent) {
        // no-op
    }

}

class MockInvalidOption implements Option {

    @Override
    public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
        throw new OptionException("Invalid option", args.nextIndex());
    }

    // other methods are empty
}

class MockParentOption implements Option {

    private Option childOption;

    @Override
    public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
        // no-op
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
        // no-op
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, String argument) {
        return true;
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
        return true;
    }

    @Override
    public Set<String> getTriggers() {
        return new HashSet<>(Arrays.asList("--parent"));
    }

    @Override
    public Set<String> getPrefixes() {
        return new HashSet<>(Arrays.asList("--", "-"));
    }

    @Override
    public void validate(WriteableCommandLine commandLine) throws OptionException {
        // no-op
    }

    @Override
    public List helpLines(int depth, Set<DisplaySetting> helpSettings, Comparator<Option> comp) {
        return null;
    }

    @Override
    public void appendUsage(StringBuffer buffer, Set<DisplaySetting> helpSettings, Comparator<Option> comp) {
        // no-op
    }

    @Override
    public String getPreferredName() {
        return "parent";
    }

    @Override
    public String getDescription() {
        return "parent option";
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
        return false;
    }

    @Override
    public Option getParent() {
        return null;
    }

    @Override
    public void setParent(Option parent) {
        this.childOption = parent;
    }

}

class MockChildOption implements Option {

    @Override
    public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
        // no-op
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
        // no-op
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, String argument) {
        return true;
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
        return true;
    }

    @Override
    public Set<String> getTriggers() {
        return new HashSet<>(Arrays.asList("--child"));
    }

    @Override
    public Set<String> getPrefixes() {
        return new HashSet<>(Arrays.asList("--", "-"));
    }

    @Override
    public void validate(WriteableCommandLine commandLine) throws OptionException {
        // no-op
    }

    @Override
    public List helpLines(int depth, Set<DisplaySetting> helpSettings, Comparator<Option> comp) {
        return null;
    }

    @Override
    public void appendUsage(StringBuffer buffer, Set<DisplaySetting> helpSettings, Comparator<Option> comp) {
        // no-op
    }

    @Override
    public String getPreferredName() {
        return "child";
    }

    @Override
    public String getDescription() {
        return "child option";
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public Option findOption(String trigger) {
        return null;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public Option getParent() {
        return new MockParentOption();
    }

    @Override
    public void setParent(Option parent) {
        // no-op
    }

}