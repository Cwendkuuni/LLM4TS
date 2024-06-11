package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class GroupImplTest {

    private final List<Option> options;
    private final GroupImpl group;

    public GroupImplTest(List<Option> options) {
        this.options = options;
        this.group = new GroupImpl(options, null, null, 0, 1);
    }

    @Parameterized.Parameters(name = "Group with options {0}")
    public static List<Object> data() {
        List<Option> optionsList = new ArrayList<>();

        optionsList.add(new Option("a", "option A", true, "a help message"));
        optionsList.add(new Option("b", "option B", false, "a help message"));
        optionsList.add(new Option("c", "option C", false, "a help message"));
        optionsList.add(new Option("d", "option D", false, "a help message"));
        optionsList.add(new Option("e", "option E", false, "a help message"));
        optionsList.add(new Argument("e", "anonymous argument E"));
        optionsList.add(new Argument("f", "anonymous argument F"));

        return Arrays.asList(new Object[]{optionsList});
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine("testCommand");
        commandLine.parseArgument(new String[]{"testCommand", "--a", "--b", "value"});

        // test with no arguments
        assertFalse(group.canProcess(commandLine, null));

        // test with first option
        assertSame(group.findOption("--a"), group.options.get(0));
        assertTrue(group.canProcess(commandLine, "--a"));

        // test with second option
        assertSame(group.findOption("--b"), group.options.get(1));
        assertTrue(group.canProcess(commandLine, "--b"));

        // test with valid anonymous argument
        assertSame(group.findOption("--e"), group.options.get(options.size() - 2));
        assertTrue(group.canProcess(commandLine, "--e"));
        assertTrue(group.canProcess(commandLine, "value--e"));

        // test with invalid anonymous argument
        assertFalse(group.canProcess(commandLine, "--f"));

        // test with invalid option
        assertFalse(group.canProcess(commandLine, "--x"));

        // test with option trigger
        String optionTrigger = ((Option) group.options.get(0)).getLongOpt();
        assertTrue(group.canProcess(commandLine, optionTrigger));

        // test with option alias
        String optionAlias = ((Option) group.options.get(0)).getShortOpt();
        assertTrue(group.canProcess(commandLine, optionAlias));

        // test with option value
        String optionValue = "value--a";
        assertTrue(group.canProcess(commandLine, optionValue));

        // test with bursting
        String optionBursting = "--a";
        Option optionToProcess = group.findOption("--c");
        assertTrue(group.canProcess(commandLine, optionBursting + optionToProcess.getLongOpt()));

        // test with anonymous argument
        String anonymousArgument = "--e";
        assertTrue(group.canProcess(commandLine, anonymousArgument));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = group.getPrefixes();
        assertEquals(Collections.emptySet(), prefixes);
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = new HashSet<>();

        for (Option option : group.getOptions()) {
            triggers.addAll(option.getTriggers());
        }

        assertEquals(new HashSet<>(Arrays.asList("a", "b", "c", "d", "e")), triggers);
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine("testCommand");
        commandLine.parseArgument(new String[]{"testCommand", "--a", "--b", "value"});

        // test with no arguments
        assertNull(group.process(commandLine, null));

        // test with first option
        group.process(commandLine, commandLine.iterator());
        assertEquals("--a", commandLine.getOptionName());
        assertTrue(group.options.get(0).isValid());

        // test with second option
        group.process(commandLine, commandLine.iterator());
        assertEquals("--b", commandLine.getOptionName());
        assertTrue(group.options.get(1).isValid());

        // test with valid anonymous argument
        group.process(commandLine, commandLine.iterator());
        assertEquals("--e", commandLine.getOptionName());
        assertTrue(group.options.get(options.size() - 2).isValid());

        // test with invalid anonymous argument
        assertFalse(group.process(commandLine, "--f") instanceof Option);

        // test with invalid option
        assertFalse(group.process(commandLine, "--x") instanceof Option);

        // test with option trigger
        String optionTrigger = ((Option) group.options.get(0)).getLongOpt();
        group.process(commandLine, commandLine.iterator());
        assertEquals(optionTrigger, commandLine.getOptionName());
        assertTrue(group.options.get(0).isValid());

        // test with option alias
        String optionAlias = ((Option) group.options.get(0)).getShortOpt();
        group.process(commandLine, commandLine.iterator());
        assertEquals(optionAlias, commandLine.getOptionName());
        assertTrue(group.options.get(0).isValid());

        // test with option value
        String optionValue = "value--a";
        group.process(commandLine, commandLine.iterator());
        assertEquals(optionValue, commandLine.getOptionName());
        assertTrue(group.options.get(0).isValid());

        // test with bursting
        String optionBursting = "--a";
        Option optionToProcess = group.findOption("--c");
        group.process(commandLine, commandLine.iterator());
        assertEquals(optionBursting + optionToProcess.getLongOpt(), commandLine.getOptionName());
        assertTrue(group.options.get(2).isValid());

        // test with anonymous argument
        String anonymousArgument = "--e";
        group.process(commandLine, commandLine.iterator());
        assertEquals(anonymousArgument, commandLine.getOptionName());
        assertTrue(group.options.get(options.size() - 2).isValid());
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLine("testCommand");

        // test with valid command line
        group.validate(commandLine);

        // test with missing options
        commandLine.clear();
        commandLine.parseArgument(new String[]{"testCommand"});
        try {
            group.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }

        // test with extra options
        commandLine.clear();
        commandLine.parseArgument(new String[]{"testCommand", "--a", "--a"});
        try {
            group.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testGetPreferredName() {
        assertEquals(null, group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals(null, group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);
        String expectedUsage = "| " +
                "group(" +
                "  option:--a<boolean> " +
                "  option:--b<boolean> " +
                "  option:--c<boolean> " +
                "  option:--d<boolean> " +
                "  option:--e<anonymous argument> " +
                "  option:--f<anonymous argument> " +
                ")\n";
        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0,
                Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null);
        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertEquals("group", helpLine.getHeader());
        List<HelpLine> helpLinesOptions = ((HelpLineImpl) helpLine).getHelpLines();
        assertEquals(6, helpLinesOptions.size());

        for (HelpLine helpLineOption : helpLinesOptions) {
            String expectedOptionName = "";
            String expectedOptionDescription = "";

            switch (helpLineOption.getHeader().charAt(0)) {
                case 'o':
                    expectedOptionName = "--a";
                    expectedOptionDescription = "option A";
                    break;
                case 'o':
                    expectedOptionName = "--b";
                    expectedOptionDescription = "option B";
                    break;
                case 'o':
                    expectedOptionName = "--c";
                    expectedOptionDescription = "option C";
                    break;
                case 'o':
                    expectedOptionName = "--d";
                    expectedOptionDescription = "option D";
                    break;
                case 'a':
                    expectedOptionName = "--e";
                    expectedOptionDescription = "anonymous argument E";
                    break;
                case 'a':
                    expectedOptionName = "--f";
                    expectedOptionDescription = "anonymous argument F";
                    break;
            }

            assertEquals(expectedOptionName, helpLineOption.getHeader());
            assertEquals(expectedOptionDescription, helpLineOption.getDescription());
        }

        List<HelpLine> helpLinesAnonymous = group.helpLines(0,
                Collections.singleton(DisplaySetting.DISPLAY_GROUP_ARGUMENT), null);
        assertEquals(2, helpLinesAnonymous.size());

        for (HelpLine helpLineAnonymous : helpLinesAnonymous) {
            String expectedAnonymousName = "";

            switch (helpLineAnonymous.getHeader().charAt(0)) {
                case 'a':
                    expectedAnonymousName = "--e";
                    break;
                case 'a':
                    expectedAnonymousName = "--f";
                    break;
            }

            assertEquals(expectedAnonymousName, helpLineAnonymous.getHeader());
        }
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();
        assertEquals(5, options.size());
        for (Option option : options) {
            assertEquals(group.findOption(option.getLongOpt()), option);
        }
    }

    @Test
    public void testGetAnonymous() {
        List<Argument> anonymous = group.getAnonymous();
        assertEquals(2, anonymous.size());
        for (Argument argument : anonymous) {
            assertEquals(group.findOption(argument.getName()), argument.getOption());
        }
    }

    @Test
    public void testFindOption() {
        Option optionA = group.findOption("--a");
        assertEquals(group.options.get(0), optionA);

        Option optionB = group.findOption("--b");
        assertEquals(group.options.get(1), optionB);

        Option optionC = group.findOption("--c");
        assertEquals(group.options.get(2), optionC);

        Option optionD = group.findOption("--d");
        assertEquals(group.options.get(3), optionD);

        Option optionE = group.findOption("--e");
        assertEquals(group.options.size() - 2, group.options.indexOf(optionE));
        assertEquals(group.options.get(options.size() - 2), optionE);

        Option optionF = group.findOption("--f");
        assertEquals(group.options.size() - 1, group.options.indexOf(optionF));
        assertEquals(group.options.get(options.size() - 1), optionF);

        Option optionX = group.findOption("--x");
        assertNull(optionX);

        Option optionXLong = group.findOption("--nonexistentOption");
        assertNull(optionXLong);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(0, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine("testCommand");

        group.defaults(commandLine);

        for (Option option : group.options) {
            assertTrue(option.isSetDefaultValue());
        }

        for (Argument argument : group.anonymous) {
            assertTrue(argument.isSetDefaultValue());
        }
    }
}

class HelperMethods {

    static final Comparator<String> ReverseStringComparator = new Comparator<String>() {
        @Override
        public int compare(final String o1, final String o2) {
            return o1.compareTo(o2) * -1;
        }
    };

    static Map<String, Option> getOptionMap(List<Option> options) {
        Map<String, Option> optionMap = new HashMap<>();

        for (Option option : options) {
            optionMap.putAll(option.getTriggers(), option);
        }

        return optionMap;
    }
}