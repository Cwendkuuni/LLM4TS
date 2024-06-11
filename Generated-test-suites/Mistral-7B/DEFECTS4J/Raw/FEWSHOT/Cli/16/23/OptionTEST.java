package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.util.DefaultHelpFormatter;
import org.apache.commons.cli2.util.HelpFormatterException;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testCanProcess() {
        Option option = new MockOption();

        WriteableCommandLine commandLine = new CommandLine();
        List<String> args = Arrays.asList("arg1", "arg2", "arg3").listIterator();

        assertTrue(option.canProcess(commandLine, args));
        assertTrue(option.canProcess(commandLine, args));
        assertTrue(option.canProcess(commandLine, args));

        // Test with null
        assertNull(option.canProcess(commandLine, null));
    }

    @Test
    public void testProcess() {
        Option option = new MockOption();
        WriteableCommandLine commandLine = new CommandLine();
        List<String> args = Arrays.asList("arg1", "arg2", "arg3").listIterator();

        option.process(commandLine, args);

        assertEquals("arg1", commandLine.getOptionValues("arg1").get(0));
        assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testDefaults() {
        Option option = new MockOption();
        WriteableCommandLine commandLine = new CommandLine();

        option.defaults(commandLine);

        assertEquals("defaultValue1", commandLine.getOptionValues("arg1").get(0));
        assertEquals("defaultValue2", commandLine.getOptionValues("arg2").get(0));
    }

    @Test
    public void testGetTriggers() {
        Option option = new MockOption();

        Set<String> triggers = option.getTriggers();

        assertTrue(triggers.contains("--arg1"));
        assertTrue(triggers.contains("-a"));
        assertEquals(2, triggers.size());
    }

    @Test
    public void testGetPrefixes() {
        Option option = new MockOption();

        Set<String> prefixes = option.getPrefixes();

        assertTrue(prefixes.contains(""));
        assertTrue(prefixes.contains("--"));
        assertTrue(prefixes.contains("-"));
        assertEquals(3, prefixes.size());
    }

    @Test
    public void testValidate() throws OptionException {
        Option option = new MockOption();
        WriteableCommandLine commandLine = new CommandLine();

        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalid() throws OptionException {
        Option option = new MockInvalidOption();
        WriteableCommandLine commandLine = new CommandLine();

        option.validate(commandLine);
    }

    @Test
    public void testFindOption() {
        Option parentOption = new MockParentOption();
        Option childOption = new MockOption();
        parentOption.setParent(childOption);

        Option foundOption = parentOption.findOption("--child");

        assertSame(childOption, foundOption);
    }

    @Test
    public void testHelpLines() {
        Option option = new MockOption();

        List<HelpLine> helpLines = option.helpLines(1, new HashSet(), null);

        assertEquals(1, helpLines.size());
        assertEquals("--arg1 <arg1>...\n", helpLines.get(0).getShortDescription());
    }

    @Test
    public void testAppendUsage() {
        Option option = new MockOption();

        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, new HashSet(), null);

        assertEquals("--arg1 <arg1>...\n", buffer.toString());
    }

    @Test
    public void testHelpFormatter() throws HelpFormatterException {
        Option option = new MockOption();

        HelpFormatter formatter = new DefaultHelpFormatter();
        StringWriter writer = new StringWriter();

        formatter.printHelp(writer, "MyProgram", option.helpLines(1, new HashSet(), null).toArray(new HelpLine[0]));

        assertEquals("MyProgram:\n", writer.getBuffer().toString());
        assertEquals("--arg1 <arg1>...\n", writer.getStringBuffer().substring(writer.getBuffer().length(), writer.getStringBuffer().length()));
    }

    @Test
    public void testIsRequired() {
        Option option = new MockOption();

        assertFalse(option.isRequired());

        Option requiredOption = new MockRequiredOption();

        assertTrue(requiredOption.isRequired());
    }

    @Test
    public void testFindOptionNull() {
        Option option = new MockOption();

        assertNull(option.findOption(null));
    }

}

class MockOption implements Option {

    @Override
    public void process(WriteableCommandLine commandLine, ListIterator args)
        throws OptionException {
        commandLine.addOption("arg1", true, null, null, null);
        args.next();
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
        commandLine.setOptionValue("arg1", "defaultValue1");
        commandLine.setOptionValue("arg2", "defaultValue2");
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
        Set<String> triggers = new HashSet<>();
        triggers.add("--arg1");
        triggers.add("-a");
        return triggers;
    }

    @Override
    public Set<String> getPrefixes() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("");
        prefixes.add("--");
        prefixes.add("-");
        return prefixes;
    }

    @Override
    public void validate(WriteableCommandLine commandLine)
        throws OptionException {
    }

    @Override
    public List helpLines(int depth, Set<?> helpSettings, Comparator<?> comp) {
        List<HelpLine> helpLines = new ArrayList<>();
        helpLines.add(new HelpLine("--arg1 <arg1>...", "An optional argument.", true));
        return helpLines;
    }

    @Override
    public void appendUsage(StringBuffer buffer, Set<?> helpSettings, Comparator<?> comp) {
    }

    @Override
    public String getPreferredName() {
        return "--arg1";
    }

    @Override
    public String getDescription() {
        return "An optional argument.";
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
    }
}

class MockParentOption implements Option {

    private Option childOption;

    @Override
    public void process(WriteableCommandLine commandLine, ListIterator<String> args)
        throws OptionException {
        childOption.process(commandLine, args);
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
        childOption.defaults(commandLine);
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, String argument) {
        return childOption.canProcess(commandLine, argument);
    }

    @Override
    public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
        return childOption.canProcess(commandLine, arguments);
    }

    @Override
    public Set<String> getTriggers() {
        Set<String> triggers = new HashSet<>();
        triggers.addAll(childOption.getTriggers());
        return triggers;
    }

    @Override
    public Set<String> getPrefixes() {
        Set<String> prefixes = new HashSet<>();
        prefixes.addAll(childOption.getPrefixes());
        return prefixes;
    }

    @Override
    public void validate(WriteableCommandLine commandLine)
        throws OptionException {
        childOption.validate(commandLine);
    }

    @Override
    public List helpLines(int depth, Set<?> helpSettings, Comparator<?> comp) {
        return childOption.helpLines(depth, helpSettings, comp);
    }

    @Override
    public void appendUsage(StringBuffer buffer, Set<?> helpSettings, Comparator<?> comp) {
        buffer.append(childOption.getPreferredName());
        buffer.append(" ");
        childOption.appendUsage(buffer, helpSettings, comp);
    }

    @Override
    public String getPreferredName() {
        return "--group";
    }

    @Override
    public String getDescription() {
        return "A group of options.";
    }

    @Override
    public int getId() {
        return 10;
    }

    @Override
    public Option findOption(String trigger) {
        return childOption;
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

class MockRequiredOption implements Option {

    @Override
    public void process(WriteableCommandLine commandLine, ListIterator<String> args)
        throws OptionException {
        commandLine.addOption("arg1", true, null, null, null);
        args.next();
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
        commandLine.setOptionValue("arg1", "defaultValue1");
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
        Set<String> triggers = new HashSet<>();
        triggers.add("--arg1");
        triggers.add("-a");
        return triggers;
    }

    @Override
    public Set<String> getPrefixes() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("");
        prefixes.add("--");
        prefixes.add("-");
        return prefixes;
    }

    @Override
    public void validate(WriteableCommandLine commandLine)
        throws OptionException {
        if (!commandLine.hasOption("arg1")) {
            throw new OptionException("The option --arg1 is required.");
        }
    }

    @Override
    public List helpLines(int depth, Set<?> helpSettings, Comparator<?> comp) {
        List<HelpLine> helpLines = new ArrayList<>();
        helpLines.add(new HelpLine("--arg1 <arg1>...", "This is a required option.", true, "value", "value description"));
        return helpLines;
    }

    @Override
    public void appendUsage(StringBuffer buffer, Set<?> helpSettings, Comparator<?> comp) {
    }

    @Override
    public String getPreferredName() {
        return "--arg1";
    }

    @Override
    public String getDescription() {
        return "This is a required option.";
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
    }
}

class MockInvalidOption implements Option {

    @Override
    public void process(WriteableCommandLine commandLine, ListIterator<String> args)
        throws OptionException {
        throw new OptionException("Invalid option.");
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
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
        Set<String> triggers = new HashSet<>();
        triggers.add("--arg1");
        triggers.add("-a");
        return triggers;
    }

    @Override
    public Set<String> getPrefixes() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("");
        prefixes.add("--");
        prefixes.add("-");
        return prefixes;
    }

    @Override
    public void validate(WriteableCommandLine commandLine)
        throws OptionException {
        throw new OptionException("Invalid option.");
    }

    @Override
    public List helpLines(int depth, Set<?> helpSettings, Comparator<?> comp) {
        List<HelpLine> helpLines = new ArrayList<>();
        return helpLines;
    }

    @Override
    public void appendUsage(StringBuffer buffer, Set<?> helpSettings, Comparator<?> comp) {
    }

    @Override
    public String getPreferredName() {
        return "--arg1";
    }

    @Override
    public String getDescription() {
        return "";
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
    }
}