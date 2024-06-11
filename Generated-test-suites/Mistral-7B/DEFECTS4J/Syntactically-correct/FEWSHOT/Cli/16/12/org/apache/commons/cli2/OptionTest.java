package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.util.HelpFormatter;
import org.apache.commons.cli2.util.HelpFormatter.DefaultHelpFormatter;
import org.apache.commons.cli2.util.HelpFormatter.DefaultHelpFormatter.DisplaySetting;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("foo", "bar", "--option", "value").listIterator();
        Option option = new Option() {
            @Override
            public void process(WriteableCommandLine cmd, ListIterator<String> args) throws OptionException {
                cmd.setValue("value");
            }

            @Override
            public void defaults(WriteableCommandLine cmd) {
                cmd.setValue("default");
            }

            @Override
            public boolean canProcess(WriteableCommandLine cmd, String arg) {
                return arg.equals("option");
            }

            @Override
            public boolean canProcess(WriteableCommandLine cmd, ListIterator<String> args) {
                return args.next().equals("option");
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option"));
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--"));
            }

            @Override
            public void validate(WriteableCommandLine cmd) throws OptionException {
                // no validation
            }

            @Override
            public List helpLines(int depth, Set helpSettings, Comparator comp) {
                return null;
            }

            @Override
            public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
                // no usage
            }

            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "this is a test option";
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
            public Option setParent(Option parent) {
                this.parent = parent;
                return this;
            }

            @Override
            public Option getParent() {
                return parent;
            }

            private Option parent;
        };

        option.process(commandLine, args);
        assertTrue(commandLine.getValue().equals("value"));

        option.defaults(commandLine);
        assertNull(commandLine.getValue());

        args = Arrays.asList("--option").listIterator();
        assertFalse(option.canProcess(commandLine, args));
        assertFalse(option.canProcess(commandLine, args.next()));
        assertTrue(option.canProcess(commandLine, "--option"));
        args.next(); // consume --
        assertTrue(option.canProcess(commandLine, "value"));

        Set<String> triggers = option.getTriggers();
        Set<String> prefixes = option.getPrefixes();
        assertSame(triggers.getClass(), HashSet.class);
        assertTrue(triggers.contains("option"));
        assertSame(prefixes.getClass(), HashSet.class);
        assertTrue(prefixes.contains("--"));

        option.validate(commandLine); // no exception

        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new LinkedHashSet<>();
        Comparator comp = null;
        option.appendUsage(buffer, helpSettings, comp); // no usage

        assertSame(option.getPreferredName(), "option");
        assertEquals(option.getDescription(), "this is a test option");
        assertEquals(option.getId(), 1);

        Option child = new Option() {
            @Override
            public void process(WriteableCommandLine cmd, ListIterator<String> args) throws OptionException {
                cmd.setValue("child");
            }

            @Override
            public void defaults(WriteableCommandLine cmd) {
                cmd.setValue("defaultChild");
            }

            @Override
            public boolean canProcess(WriteableCommandLine cmd, String arg) {
                return arg.equals("childOption");
            }

            @Override
            public boolean canProcess(WriteableCommandLine cmd, ListIterator<String> args) {
                return args.next().equals("childOption");
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("childOption"));
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--child"));
            }

            @Override
            public void validate(WriteableCommandLine cmd) throws OptionException {
                // no validation
            }

            @Override
            public List helpLines(int depth, Set helpSettings, Comparator comp) {
                return null;
            }

            @Override
            public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
                // no usage
            }

            @Override
            public String getPreferredName() {
                return "child";
            }

            @Override
            public String getDescription() {
                return "this is a test child option";
            }

            @Override
            public int getId() {
                return 2;
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
            public Option setParent(Option parent) {
                this.parent = parent;
                return this;
            }

            @Override
            public Option getParent() {
                return parent;
            }

            private Option parent;
        };

        option.setParent(child);
        assertSame(option.getParent(), child);

        commandLine = new WriteableCommandLine();
        args = Arrays.asList("--option", "value", "--child", "childValue").listIterator();
        option.process(commandLine, args);
        assertSame(commandLine.getValue(), "value");
        assertSame(commandLine.getOptionValue("child"), "childValue");

        option.defaults(commandLine);
        assertNull(commandLine.getValue());
        assertNull(commandLine.getOptionValue("child"));

        Set<Option> options = new LinkedHashSet<>();
        options.add(option);
        options.add(child);

        HelpFormatter formatter = new DefaultHelpFormatter();
        formatter.printHelp("Test", Arrays.asList("test", "test", "test"), options, true);
        List<HelpLineImpl> helpLines = formatter.getHelpLines();
        int index = 0;
        assertTrue(helpLines.get(index++).getHeaders().stream().anyMatch(s -> s.equals("OPTIONS")));
        assertTrue(helpLines.get(index++).getHeaders().stream().anyMatch(s -> s.equals("OPTION")));
        assertTrue(helpLines.get(index++).getHeaders().stream().anyMatch(s -> s.equals("ARGUMENTS")));
        assertTrue(helpLines.get(index++).getHeaders().stream().anyMatch(s -> s.equals("CHILD OPTION")));
        assertTrue(helpLines.get(index++).getHeaders().stream().anyMatch(s -> s.equals("--option")));
        assertTrue(helpLines.get(index++).getHeaders().stream().anyMatch(s -> s.equals("--child")));
        assertTrue(helpLines.get(index++).getHeaders().stream().anyMatch(s -> s.equals("value")));
        assertTrue(helpLines.get(index++).getHeaders().stream().anyMatch(s -> s.equals("childValue")));
    }
}