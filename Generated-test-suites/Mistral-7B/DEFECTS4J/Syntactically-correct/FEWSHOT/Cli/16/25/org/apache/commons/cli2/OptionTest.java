package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.util.HelpFormatter;
import org.apache.commons.cli2.util.HelpFormatter.DefaultHelpFormatter;
import org.apache.commons.cli2.util.HelpFormatter.DefaultParser;
import org.apache.commons.cli2.util.HelpFormatter.DefaultUsageIntegrator;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testProcess() {
        WriteableCommandLine cmd = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("--option", "value").listIterator();
        Option option = new MockOption();
        option.process(cmd, args);
        assertTrue(cmd.hasOption("option"));
        assertEquals("value", cmd.getOptionValue("option"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidArgument() {
        WriteableCommandLine cmd = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("--option", "value", "--invalid").listIterator();
        Option option = new MockOption();
        option.process(cmd, args);
        fail("Should throw OptionException for invalid argument");
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine cmd = new WriteableCommandLine();
        Option option = new MockOption();
        option.defaults(cmd);
        assertTrue(cmd.hasOption("option"));
        assertNull(cmd.getOptionValue("option"));
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine cmd = new WriteableCommandLine();
        Option option = new MockOption();
        ListIterator<String> arguments = Arrays.asList("--option", "value").listIterator();
        assertTrue(option.canProcess(cmd, arguments));
        ListIterator<String> arguments2 = Arrays.asList("--invalid", "option").listIterator();
        assertFalse(option.canProcess(cmd, arguments2));
    }

    @Test
    public void testGetTriggers() {
        Option option = new MockOption();
        Set triggers = option.getTriggers();
        assertTrue(triggers.contains("--option"));
        assertTrue(triggers.contains("-o"));
        assertFalse(triggers.contains("--other"));
    }

    @Test
    public void testGetPrefixes() {
        Option option = new MockOption();
        Set prefixes = option.getPrefixes();
        assertTrue(prefixes.contains("--"));
        assertTrue(prefixes.contains("-"));
        assertFalse(prefixes.contains("--other"));
    }

    @Test
    public void testValidate() {
        WriteableCommandLine cmd = new WriteableCommandLine();
        Option option = new MockOption();
        option.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidCommandLine() {
        WriteableCommandLine cmd = new WriteableCommandLine();
        cmd.addOption("--invalid", true);
        Option option = new MockOption();
        option.validate(cmd);
        fail("Should throw OptionException for invalid CommandLine");
    }

    @Test
    public void testHelpLines() {
        Option option = new MockOption();
        Set helpSettings = new HashSet();
        Comparator comp = null;
        List help = option.helpLines(1, helpSettings, comp);
        assertEquals(1, help.size());
        HelpLineImpl helpLine = (HelpLineImpl) help.get(0);
        assertEquals("--option", helpLine.getOptionSpec().getLongOpt());
        assertEquals("option", helpLine.getOptionSpec().getOpt());
        assertEquals("Option description", helpLine.getDescription());
    }

    @Test
    public void testAppendUsage() {
        Option option = new MockOption();
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comp = null;
        option.appendUsage(buffer, helpSettings, comp);
        assertEquals("--option, -o <value> OPTION description\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Option option = new MockOption();
        assertEquals("option", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = new MockOption();
        assertEquals("Option description", option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new MockOption();
        assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption() {
        Option parentOption = new MockOption();
        Option childOption = new MockOption();
        parentOption.setParent(childOption);
        Option option = new MockOption();
        Option foundOption = option.findOption("parent");
        assertNull(foundOption);
        foundOption = option.findOption("parentOption");
        assertSame(parentOption, foundOption);
        foundOption = option.findOption("child");
        assertSame(childOption, foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option = new MockOption();
        assertFalse(option.isRequired());
    }

    @Test
    public void testFindParent() {
        Option childOption = new MockOption();
        Option parentOption = new MockOption();
        childOption.setParent(parentOption);
        Option option = new MockOption();
        Option foundOption = option.findParent();
        assertNull(foundOption);
        foundOption = option.findParent("parentOption");
        assertSame(parentOption, foundOption);
    }

    @Test
    public void testToString() {
        Option option = new MockOption();
        assertEquals("MockOption[option, Option description, 1]", option.toString());
    }

    private static class MockOption implements Option {

        private Option parent;

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args)
            throws OptionException {
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
            Set<String> set = new HashSet<>();
            set.add("--option");
            set.add("-o");
            return set;
        }

        @Override
        public Set<String> getPrefixes() {
            Set<String> set = new HashSet<>();
            set.add("--");
            set.add("-");
            return set;
        }

        @Override
        public void validate(WriteableCommandLine commandLine)
            throws OptionException {
        }

        @Override
        public List<HelpLineImpl> helpLines(int depth, Set helpSettings, Comparator comp) {
            return null;
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
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
            return parent;
        }

        @Override
        public void setParent(Option parent) {
            this.parent = parent;
        }

        @Override
        public String toString() {
            return null;
        }
    }

    private static class HelpLineImpl implements HelpLine {

        private String optionSpec;

        public HelpLineImpl(String optionSpec, String description) {
            this.optionSpec = optionSpec;
        }

        @Override
        public String getOptionSpec() {
            return optionSpec;
        }

        @Override
        public String getUsage() {
            return null;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public String getValuesDescription() {
            return null;
        }

        @Override
        public String getLongOpt() {
            return optionSpec.split(" ")[0];
        }

        @Override
        public String getOpt() {
            return optionSpec.split(" ")[0].charAt(1) == '-' ? optionSpec.split(" ")[0].substring(2) : optionSpec.split(" ")[0];
        }
    }

    private static class WriteableCommandLine implements CommandLine {

        private List<String> arguments = new ArrayList<>();

        @Override
        public boolean hasOption(String name) {
            return false;
        }

        @Override
        public String getOptionValue(String name) {
            return null;
        }

        @Override
        public List<String> getArgList() {
            return arguments;
        }

        @Override
        public String[] getArgs() {
            return arguments.toArray(new String[0]);
        }

        @Override
        public void addOption(Option option) {
        }

        @Override
        public void addOptions(List<Option> options) {
        }

        @Override
        public void addArg(String arg) {
            arguments.add(arg);
        }

        @Override
        public void addArgs(List<String> args) {
            arguments.addAll(args);
        }

        @Override
        public void clearArgs() {
            arguments.clear();
        }

        @Override
        public void clearOptions() {
        }

        @Override
        public void clear() {
            arguments.clear();
        }
    }

    @Test
    public void testHelpFormatter() {
        Option option = new MockOption();
        Set<String> helpSettings = new HashSet<>();
        Comparator<Option> comp = null;
        HelpFormatter formatter = new DefaultHelpFormatter();
        formatter.setHelpFormatter(new DefaultHelpFormatter.DefaultHelpFormatter(new DefaultParser(), new DefaultUsageIntegrator()));
        formatter.printHelp("MockApp", option.helpLines(0, helpSettings, comp), true);
    }
}