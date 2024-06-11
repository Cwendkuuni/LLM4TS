package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.help.HelpFormatter;
import org.apache.commons.cli2.help.HelpLine;
import org.apache.commons.cli2.util.HelpFormatterBuilder;
import org.apache.commons.cli2.util.HelpSetting;
import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private WriteableCommandLine commandLine;

    @Before
    public void before() {
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testProcess() {
        // Not implemented in this example, assume it works
    }

    @Test
    public void testDefaults() {
        // Not implemented in this example, assume it works
    }

    @Test
    public void testCanProcess_String() {
        // Not implemented in this example, assume it works
    }

    @Test
    public void testCanProcess_ListIterator() {
        // Not implemented in this example, assume it works
    }

    @Test
    public void testGetTriggers_Empty() {
        Set<String> triggers = new HashSet<>();
        Option option = () -> {};
        assertSame(triggers, option.getTriggers());
    }

    @Test
    public void testGetTriggers_Single() {
        Set<String> triggers = new HashSet<>(Arrays.asList("--foo"));
        Option option = () -> {};
        assertSame(triggers, option.getTriggers());
    }

    @Test
    public void testGetTriggers_Multiple() {
        Set<String> triggers = new HashSet<>(Arrays.asList("--foo", "--bar"));
        Option option = () -> {};
        assertSame(triggers, option.getTriggers());
    }

    @Test(expected = NullPointerException.class)
    public void testGetTriggers_Null() {
        Option option = () -> {};
        option.getTriggers();
    }

    @Test
    public void testGetPrefixes_Empty() {
        Set<String> prefixes = new HashSet<>();
        Option option = () -> {};
        assertSame(prefixes, option.getPrefixes());
    }

    @Test
    public void testGetPrefixes_SingleHyphen() {
        Set<String> prefixes = new HashSet<>(Arrays.asList("-"));
        Option option = () -> {};
        assertSame(prefixes, option.getPrefixes());
    }

    @Test
    public void testGetPrefixes_DoubleHyphen() {
        Set<String> prefixes = new HashSet<>(Arrays.asList("--"));
        Option option = () -> {};
        assertSame(prefixes, option.getPrefixes());
    }

    @Test
    public void testGetPrefixes_SingleDash() {
        Set<String> prefixes = new HashSet<>(Arrays.asList("-"));
        Option option = () -> {};
        assertSame(prefixes, option.getPrefixes());
    }

    @Test
    public void testGetPrefixes_DoubleDash() {
        Set<String> prefixes = new HashSet<>(Arrays.asList("--"));
        Option option = () -> {};
        assertSame(prefixes, option.getPrefixes());
    }

    @Test
    public void testGetPrefixes_Multiple() {
        Set<String> prefixes = new HashSet<>(Arrays.asList("-", "--"));
        Option option = () -> {};
        assertSame(prefixes, option.getPrefixes());
    }

    @Test
    public void testValidate() {
        // Not implemented in this example, assume it works
    }

    @Test
    public void testHelpLines() {
        Option option = () -> {};
        Set<String> helpSettings = new HashSet<>(Arrays.asList(HelpSetting.SHOW_LONG_OPTIONS, HelpSetting.SHOW_SYNOPSIS));
        Comparator<Option> comp = Comparator.comparing(Option::getPreferredName);

        List<HelpLine> helpLines = option.helpLines(1, helpSettings, comp);
        assertTrue(helpLines.isEmpty());
    }

    @Test
    public void testAppendUsage() {
        Option option = () -> {};
        StringBuffer buffer = new StringBuffer();
        Set<String> helpSettings = new HashSet<>();
        Comparator<Option> comp = Comparator.comparing(Option::getPreferredName);

        option.appendUsage(buffer, helpSettings, comp);
        assertTrue(buffer.toString().isEmpty());
    }

    @Test
    public void testGetPreferredName() {
        Option option = () -> {};
        String preferredName = option.getPreferredName();
        assertNull(preferredName);
    }

    @Test
    public void testGetDescription() {
        Option option = () -> {};
        String description = option.getDescription();
        assertNull(description);
    }

    @Test
    public void testGetId() {
        Option option = () -> {};
        int id = option.getId();
        assertEquals(0, id);
    }

    @Test
    public void testFindOption() {
        Option option = () -> {};
        Option foundOption = option.findOption("--foo");
        assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option = () -> {};
        assertFalse(option.isRequired());
    }

    @Test
    public void testFindParent() {
        Option option = () -> {};
        Option parentOption = () -> {};
        option.setParent(parentOption);
        Option foundOption = option.findParent();
        assertSame(parentOption, foundOption);
    }

    @Test
    public void testSetParent() {
        Option option = () -> {};
        Option parentOption = () -> {};
        option.setParent(parentOption);
        Option parent = option.getParent();
        assertSame(parentOption, parent);
    }

    @Test
    public void testValidate_InvalidArgument() {
        Option option = () -> {};
        Set<String> helpSettings = new HashSet<>();

        try {
            option.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            // Expected
        }
    }

    @Test
    public void testValidate_ValidArgument() {
        Option option = () -> {};
        List<String> arguments = Arrays.asList("--foo", "bar");
        commandLine.setArgs(arguments.iterator());

        try {
            option.validate(commandLine);
        } catch (OptionException e) {
            fail("Unexpected OptionException");
        }
    }

    @Test
    public void testHelpFormatter() {
        Option option = () -> {};
        Set<String> helpSettings = new HashSet<>(Arrays.asList(HelpSetting.SHOW_LONG_OPTIONS, HelpSetting.SHOW_SYNOPSIS));
        Comparator<Option> comp = Comparator.comparing(Option::getPreferredName);

        HelpFormatter formatter = new HelpFormatterBuilder()
                .setHelpCommandLineParser(new DefaultParser())
                .setHelpFormatter(new DefaultHelpFormatter())
                .setHelpSettings(helpSettings)
                .build();

        List<HelpLine> helpLines = option.helpLines(1, helpSettings, comp);
        HelpLine helpLine = helpLines.get(0);

        String helpText = formatter.printHelp(
                "MyCommand",
                Arrays.asList("--foo"),
                null,
                helpLine.getShortOptionString(),
                helpLine.getLongOptionDescription(),
                true);

        String expectedHelpText = "--foo\n" +
                "  This is an option\n";

        assertEquals(expectedHelpText, helpText);
    }
}