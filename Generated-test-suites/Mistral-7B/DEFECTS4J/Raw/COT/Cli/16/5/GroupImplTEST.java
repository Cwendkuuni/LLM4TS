package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.ReverseStringComparator;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GroupImplTest {
    private GroupImpl group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        final List options = new ArrayList();
        options.add(new Option("a", "optionA", true, "This is option A"));
        options.add(new Option("b", "optionB", false, "This is option B"));
        options.add(new Option("c", "optionC", false, "This is option C"));
        options.add(new Option("d", "optionD", false, "This is option D"));
        options.add(new Option("e", "optionE", false, "This is option E"));
        options.add(new Argument("f", "argumentF", true, "This is argument F"));

        group = new GroupImpl(options, "groupName", "Description for group", 1, 2);
        commandLine = new SimpleCommandLine();
    }

    @Test
    public void testCanProcess_ValidOption() {
        assertTrue(group.canProcess(commandLine, "a"));
    }

    @Test
    public void testCanProcess_BurstingOption() {
        commandLine.parse(new String[]{"--a", "--b", "--c"});
        commandLine.parse(new String[]{"--a", "--b", "--c", "--d"});
        assertTrue(group.canProcess(commandLine, "d"));
    }

    @Test
    public void testCanProcess_UnrecognizedOption() {
        assertFalse(group.canProcess(commandLine, "x"));
    }

    @Test
    public void testCanProcess_Argument() {
        assertTrue(group.canProcess(commandLine, "f"));
    }

    @Test(expected = OptionException.class)
    public void testValidate_TooFewOptions() {
        commandLine.parse(new String[]{"--a"});
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_TooManyOptions() {
        commandLine.parse(new String[]{"--a", "--a", "--b"});
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidOption() {
        commandLine.parse(new String[]{"--x"});
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_MissingArgument() {
        commandLine.parse(new String[]{"--a", "--f"});
        group.validate(commandLine);
    }

    @Test
    public void testProcess_ValidOption() {
        commandLine.parse(new String[]{"--a"});
        group.process(commandLine, commandLine.getArgs().listIterator());

        assertEquals(1, commandLine.getOptionValues("a").length);
        assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testProcess_BurstingOption() {
        commandLine.parse(new String[]{"--a", "--b", "--c"});
        group.process(commandLine, commandLine.getArgs().listIterator());

        assertEquals(3, commandLine.getOptionValues("a").length);
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.hasOption("c"));
    }

    @Test
    public void testProcess_UnrecognizedOption() {
        commandLine.parse(new String[]{"--x"});
        group.process(commandLine, commandLine.getArgs().listIterator());

        assertFalse(commandLine.hasOption("x"));
    }

    @Test
    public void testProcess_Argument() {
        commandLine.parse(new String[]{"--f", "test"});
        group.process(commandLine, commandLine.getArgs().listIterator());

        assertEquals("test", commandLine.getArgumentValues().get(0));
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder buffer = new StringBuilder();

        group.appendUsage(buffer, Collections.emptySet(), null);

        assertEquals("groupName:\n" +
                "Description for group\n\n" +
                "options:\n" +
                "  -a OPTIONAL: This is option A\n" +
                "  -b FALSE: This is option B\n" +
                "  -c FALSE: This is option C\n" +
                "  -d OPTIONAL: This is option D\n" +
                "  -e FALSE: This is option E\n" +
                "  --f ARGUMENT: This is argument F\n",
                buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);

        assertEquals(6, helpLines.size());

        final HelpLine helpLine = helpLines.get(0);
        assertEquals("groupName", helpLine.getName());
        assertEquals("Description for group", helpLine.getShortDescription());
        assertEquals("options:", helpLine.getLongDescription());

        final HelpLine optionHelpLine = helpLines.get(1);
        assertEquals("-a", optionHelpLine.getName());
        assertEquals("OPTIONAL", optionHelpLine.getOpt());
        assertEquals("This is option A", optionHelpLine.getLongDescription());

        final HelpLine argumentHelpLine = helpLines.get(5);
        assertEquals("--f", argumentHelpLine.getName());
        assertEquals("ARGUMENT", argumentHelpLine.getOpt());
        assertEquals("This is argument F", argumentHelpLine.getLongDescription());
    }

    @Test
    public void testGetOptions() {
        assertEquals(5, group.getOptions().size());
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(1, group.getAnonymous().size());
    }

    @Test
    public void testFindOption() {
        final Option optionA = group.findOption("a");
        assertEquals("a", optionA.getLongOpt());
        assertEquals("--a", optionA.getOpt());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(2, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);

        assertNull(commandLine.getOptionValues("a"));
        assertNull(commandLine.getOptionValues("b"));
        assertNull(commandLine.getOptionValues("c"));
        assertNull(commandLine.getOptionValues("d"));
        assertNull(commandLine.getOptionValues("e"));
        assertEquals(null, commandLine.getArgumentValues().get(0));
    }

    private static class SimpleCommandLine implements WriteableCommandLine {
        private final List<String> args = new LinkedList<>();

        @Override
        public void parse(final String[] args) {
            this.args.clear();
            for (final String arg : args) {
                this.args.add(arg);
            }
        }

        @Override
        public boolean hasOption(final String key) {
            for (final String arg : args) {
                if (arg.startsWith("--")) {
                    final String option = arg.substring(2);
                    if (option.equals(key)) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public String[] getArgs() {
            return args.toArray(new String[0]);
        }

        @Override
        public List<String> getArgList() {
            return args;
        }

        @Override
        public ListIterator<String> getArgsIterator() {
            return args.listIterator();
        }

        @Override
        public String getOptionValue(final String key) {
            for (final String arg : args) {
                if (arg.startsWith("--")) {
                    final String option = arg.substring(2);
                    final int indexOfValue = option.indexOf(':');
                    if (option.equals(key)) {
                        if (indexOfValue >= 0) {
                            return option.substring(indexOfValue + 1);
                        }
                        return "";
                    }
                }
            }
            return null;
        }

        @Override
        public Map<String, String> getOptionMap() {
            final Map<String, String> optionMap = new TreeMap<>(ReverseStringComparator.getInstance());
            for (final String arg : args) {
                if (arg.startsWith("--")) {
                    final String option = arg.substring(2);
                    final int indexOfValue = option.indexOf(':');
                    if (indexOfValue >= 0) {
                        optionMap.put(option.substring(0, indexOfValue), option.substring(indexOfValue + 1));
                    }
                }
            }
            return optionMap;
        }

        @Override
        public void setOptionValue(final String key, final String value) {
            for (int i = 0; i < args.size(); i++) {
                final String arg = args.get(i);
                if (arg.startsWith("--")) {
                    final String option = arg.substring(2);
                    final int indexOfValue = option.indexOf(':');
                    if (option.equals(key)) {
                        if (indexOfValue >= 0) {
                            args.set(i, option + ':' + value);
                            return;
                        }
                    }
                }
            }
            args.add(key + ':' + value);
        }

        @Override
        public void clear() {
            args.clear();
        }

        @Override
        public void addOption(final Option option) {
            args.add(option.getLongOpt());
        }

        @Override
        public void addArgument(final Argument argument) {
            args.add(argument.getName());
        }

        @Override
        public void addOptions(final Collection<? extends Option> options) {
            for (final Option option : options) {
                args.add(option.getLongOpt());
            }
        }

        @Override
        public void addArguments(final Collection<? extends Argument> arguments) {
            for (final Argument argument : arguments) {
                args.add(argument.getName());
            }
        }

        @Override
        public void addArgumentValue(final String value) {
            args.add(value);
        }

        @Override
        public void addOptionValue(final String value) {
            args.add("--" + value);
        }

        @Override
        public void addParameters(final String[] parameters) {
            args.addAll(List.of(parameters));
        }

        @Override
        public void addPositionalArgument(final String name, final String description) {
            args.add(name + ':');
            args.add(description);
        }

        @Override
        public void addPositionalArgument(final String name) {
            args.add(name);
        }

        @Override
        public void addPositionalArguments(final String[] names) {
            for (final String name : names) {
                args.add(name);
            }
        }

        @Override
        public void addPositionalArguments(final String[] names, final String description) {
            for (final String name : names) {
                args.add(name + ':');
                args.add(description);
            }
        }

        @Override
        public void addOption(final Option option, final DisplaySetting setting) {
            args.add(option.getLongOpt());
            if (setting != DisplaySetting.NONE) {
                args.add(setting.toString());
            }
        }

        @Override
        public void addArgument(final Argument argument, final DisplaySetting setting) {
            args.add(argument.getName());
            if (setting != DisplaySetting.NONE) {
                args.add(setting.toString());
            }
        }

        @Override
        public void addAllOptions(final Collection<? extends Option> options) {
            for (final Option option : options) {
                args.add(option.getLongOpt());
                if (option.isRequired()) {
                    args.add(DisplaySetting.REQUIRED.toString());
                } else if (option.isHidden()) {
                    args.add(DisplaySetting.HIDDEN.toString());
                }
            }
        }

        @Override
        public void addAllArguments(final Collection<? extends Argument> arguments) {
            for (final Argument argument : arguments) {
                args.add(argument.getName());
                if (argument.isRequired()) {
                    args.add(DisplaySetting.REQUIRED.toString());
                } else if (argument.isHidden()) {
                    args.add(DisplaySetting.HIDDEN.toString());
                }
            }
        }
    }
}