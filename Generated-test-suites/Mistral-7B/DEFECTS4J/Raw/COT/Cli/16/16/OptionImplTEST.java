package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.Default;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.apache.commons.cli2.builder.OptionBuilder.Type;
import org.apache.commons.cli2.parser.DefaultParser;
import org.apache.commons.cli2.test.TestUtils;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {

    private OptionImpl option;
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        option = new OptionBuilder("optionName")
                .withLongOpt("longOptionName")
                .build();
        commandLine = new DefaultCommandLine();
    }

    @Test
    public void testConstructor() {
        OptionImpl opt = new OptionImpl(1, true);
        assertNotNull(opt);
    }

    @Test
    public void testCanProcess_whenArgumentIsNotPresent_returnsFalse() {
        assertFalse(option.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_whenArgumentMatches_returnsTrue() {
        commandLine.addArgument("optionName", new String[]{"value"});
        assertTrue(option.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_whenArgumentDoesNotMatch_returnsFalse() {
        commandLine.addArgument("otherArgument", new String[]{"value"});
        assertFalse(option.canProcess(commandLine, null));
    }

    @Test
    public void testToString_whenCalled_returnsCorrectString() {
        assertEquals("optionName", option.toString());
    }

    @Test
    public void testEquals_whenCalledWithSameOption_returnsTrue() {
        OptionImpl option2 = new OptionBuilder("optionName")
                .withLongOpt("longOptionName")
                .build();
        assertTrue(option.equals(option2));
    }

    @Test
    public void testEquals_whenCalledWithDifferentOption_returnsFalse() {
        OptionImpl option2 = new OptionBuilder("otherOptionName")
                .withLongOpt("otherLongOptionName")
                .build();
        assertFalse(option.equals(option2));
    }

    @Test
    public void testHashCode_whenCalledWithSameOption_returnsSameHashCode() {
        OptionImpl option2 = new OptionBuilder("optionName")
                .withLongOpt("longOptionName")
                .build();
        assertEquals(option.hashCode(), option2.hashCode());
    }

    @Test
    public void testHashCode_whenCalledWithDifferentOption_returnsDifferentHashCode() {
        OptionImpl option2 = new OptionBuilder("otherOptionName")
                .withLongOpt("otherLongOptionName")
                .build();
        assertNotSame(option.hashCode(), option2.hashCode());
    }

    @Test
    public void testFindOption_whenCalledWithMatchingTrigger_returnsOptionInstance() {
        OptionImpl option2 = new OptionBuilder("optionName2")
                .withLongOpt("longOptionName2")
                .build();
        OptionImpl option3 = new OptionBuilder("optionName")
                .withLongOpt("longOptionName")
                .build();

        Set<Option> options = new HashSet<>();
        options.add(option2);
        options.add(option3);

        Option foundOption = option.findOption("optionName");
        assertSame(option3, foundOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindOption_whenCalledWithMismatchingTrigger_throwsException() {
        OptionImpl option2 = new OptionBuilder("optionName2")
                .withLongOpt("longOptionName2")
                .build();
        OptionImpl option3 = new OptionBuilder("optionName")
                .withLongOpt("longOptionName")
                .build();

        Set<Option> options = new HashSet<>();
        options.add(option2);
        options.add(option3);

        option.findOption("optionName3");
    }

    @Test
    public void testIsRequired_whenCalled_returnsCorrectValue() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testDefaults_whenCalled_doesNothing() {
        // Nothing to test since no implementation provided
    }

    @Test
    public void testCheckPrefixes_whenPrefixesIsEmpty_doesNothing() {
        Set<String> prefixes = new HashSet<>();
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes_whenPrefixDoesNotMatch_throwsException() {
        Set<String> prefixes = new HashSet<String>(Arrays.asList("prefix1"));
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixes_whenPrefixMatches_doesNothing() {
        Set<String> prefixes = new HashSet<String>(Arrays.asList("prefixOptionName"));
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCanProcess_withArguments_whenArgumentIsNotPresent_returnsFalse() {
        commandLine.addArgument("otherArgument", new String[]{"value"});
        assertFalse(option.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_withArguments_whenArgumentIsPresent_returnsTrue() {
        commandLine.addArgument("optionName", new String[]{"value"});
        assertTrue(option.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_withArguments_whenArgumentIsNotMatching_returnsFalse() {
        commandLine.addArgument("otherArgument", new String[]{"value"});
        assertFalse(option.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_withArguments_whenArgumentIsPresentAndMatchesLongOpt_returnsTrue() {
        commandLine.addOption("--longOptionName", null, true, null, "longValue");
        assertTrue(option.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_withArguments_whenArgumentIsPresentAndMatchesShortOpt_returnsTrue() {
        commandLine.addOption("-o", new String[]{"value"});
        assertTrue(option.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_withArguments_whenArgumentIsPresentAndMatchesTrigger_returnsTrue() {
        commandLine.addOption("optionName", new String[]{"value"});
        assertTrue(option.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_withArguments_whenArgumentIsPresentAndMatchesTriggerWithPrefix_returnsTrue() {
        commandLine.addOption("prefixOptionName", new String[]{"value"});
        assertTrue(option.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_withArguments_whenArgumentsAreEmpty_returnsFalse() {
        assertTrue(option.canProcess(commandLine, null));
        assertFalse(option.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_withArguments_whenArgumentsAreMultiple_returnsFalse() {
        commandLine.addArgument("otherArgument", new String[]{"value1"});
        commandLine.addArgument("otherArgument", new String[]{"value2"});
        assertFalse(option.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_withArguments_whenCommandLineHasNoArguments_returnsFalse() {
        assertFalse(option.canProcess(commandLine, null));
    }
}