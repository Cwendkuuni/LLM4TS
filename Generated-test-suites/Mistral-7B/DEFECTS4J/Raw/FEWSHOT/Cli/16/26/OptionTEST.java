package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.util.HelpLine;
import org.apache.commons.cli2.util.HelpSetting;
import org.apache.commons.cli2.util.HelpSetting.DisplaySetting;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> args = Arrays.asList("--option", "value").listIterator();
        Option option = new DummyOption();

        option.process(commandLine, args);

        assertEquals("value", commandLine.getValue("option"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> args = Arrays.asList("--invalid").listIterator();
        Option option = new DummyOption();

        option.process(commandLine, args);
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new DummyOption();
        option.defaults(commandLine);
        assertEquals("default", commandLine.getValue("option"));
    }

    @Test
    public void testCanProcessWithValidArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> args = Arrays.asList("--option").listIterator();
        Option option = new DummyOption();

        assertTrue(option.canProcess(commandLine, args));
    }

    @Test
    public void testCanProcessWithInvalidArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> args = Arrays.asList("--invalid").listIterator();
        Option option = new DummyOption();

        assertFalse(option.canProcess(commandLine, args));
    }

    @Test
    public void testCanProcessWithValidArguments() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> args = Arrays.asList("--option1", "--option2").listIterator();
        Option option = new DummyOption();

        assertTrue(option.canProcess(commandLine, args));
    }

    @Test
    public void testGetTriggers() {
        Option option = new DummyOption();
        Set<String> triggers = option.getTriggers();
        assertTrue(triggers.contains("--option"));
        assertEquals(1, triggers.size());
    }

    @Test
    public void testGetPrefixes() {
        Option option = new DummyOption();
        Set<String> prefixes = option.getPrefixes();
        assertTrue(prefixes.contains("--"));
        assertEquals(1, prefixes.size());
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new DummyOption();
        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidCommandLine() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new DummyOption();
        commandLine.setValue("invalid", "value");
        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Option option = new DummyOption();
        int depth = 1;
        Set<HelpSetting> helpSettings = new HashSet<>();
        HelpFormatter formatter = new HelpFormatter();

        List<HelpLine> helpLines = option.helpLines(depth, helpSettings, null);
        HelpLine helpLine = helpLines.get(0);

        assertEquals("--option", helpLine.getLongOpt());
        assertEquals("Option description", helpLine.getShortOpt());
        assertEquals("Option description", helpLine.getDescription());
        assertEquals("value", helpLine.getValues()[0]);

        formatter.printHelp("test", "", helpLines.toArray(new HelpLine[helpLines.size()]), true);
    }

    @Test
    public void testAppendUsage() {
        Option option = new DummyOption();
        StringBuffer buffer = new StringBuffer();
        Set<HelpSetting> helpSettings = new HashSet<>();
        HelpFormatter formatter = new HelpFormatter();

        option.appendUsage(buffer, helpSettings, null);

        assertEquals("--option <value>   Option description\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Option option = new DummyOption();
        assertEquals("option", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = new DummyOption();
        assertEquals("Option description", option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new DummyOption();
        assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption() {
        Option parent = new DummyGroup();
        Option option1 = new DummyOption();
        Option option2 = new DummyOption();
        parent.addOption(option1);
        parent.addOption(option2);

        Option foundOption = parent.findOption("option1");
        assertSame(option1, foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option = new DummyOption();
        assertFalse(option.isRequired());
    }

    @Test
    public void testFindOptionWithParent() {
        Option parent = new DummyGroup();
        Option option1 = new DummyOption();
        Option option2 = new DummyOption();
        parent.addOption(option1);
        parent.addOption(option2);

        Option foundOption = option1.findOption("option2");
        assertNull(foundOption);
    }

    @Test
    public void testSetParent() {
        Option option = new DummyOption();
        Option parent = new DummyGroup();

        option.setParent(parent);
        assertSame(parent, option.getParent());
    }

    static class DummyOption implements Option {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
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
            return new HashSet<>(Collections.singletonList("--option"));
        }

        @Override
        public Set<String> getPrefixes() {
            return new HashSet<>(Collections.singletonList("--"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
        }

        @Override
        public List helpLines(int depth, Set<HelpSetting> helpSettings, Comparator<Option> comp) {
            return new PriorityQueue<>();
        }

        @Override
        public void appendUsage(StringBuilder buffer, Set<HelpSetting> helpSettings, Comparator<Option> comp) {
        }

        @Override
        public String getPreferredName() {
            return "option";
        }

        @Override
        public String getDescription() {
            return "Option description";
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

    static class DummyGroup implements Option {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
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
            return new HashSet<>();
        }

        @Override
        public Set<String> getPrefixes() {
            return new HashSet<>(Arrays.asList("--", "-"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
        }

        @Override
        public List helpLines(int depth, Set<HelpSetting> helpSettings, Comparator<Option> comp) {
            return new PriorityQueue<>();
        }

        @Override
        public void appendUsage(StringBuilder buffer, Set<HelpSetting> helpSettings, Comparator<Option> comp) {
        }

        @Override
        public String getPreferredName() {
            return "group";
        }

        @Override
        public String getDescription() {
            return "Group description";
        }

        @Override
        public int getId() {
            return 0;
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

        @Override
        public void addOption(Option option) {
        }
    }
}