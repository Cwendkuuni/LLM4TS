package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.util.TestUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionImplTest {

    private OptionImpl option;
    private WriteableCommandLine commandLine;
    private ResourceHelper resourceHelper;

    @Before
    public void setUp() {
        commandLine = new TestUtil.DummyWriteableCommandLine();
        resourceHelper = ResourceHelper.getResourceHelper();
    }

    @Test
    public void testOptionImplConstructor() {
        option = new OptionImpl(1, false);
        assertEquals(1, option.getId());
        assertFalse(option.isRequired());
    }

    @Test
    public void testCanProcess() {
        option = new OptionImpl(1, false);
        assertFalse(option.canProcess(commandLine, null));

        option = new OptionImpl(1, false);
        commandLine.addArgument("arg1");
        assertFalse(option.canProcess(commandLine, null));

        option = new OptionImpl(1, false);
        commandLine.addArgument("--option1");
        assertTrue(option.canProcess(commandLine, null));

        option = new OptionImpl(1, true);
        commandLine.addArgument("--option1");
        assertTrue(option.canProcess(commandLine, null));

        option = new OptionImpl(1, true);
        commandLine.addArgument("arg1");
        assertFalse(option.canProcess(commandLine, null));
    }

    @Test
    public void testToString() {
        option = new OptionImpl(1, false);
        String expected = String.format("%1$s%n%2$s%n", "OPTION:", "OPTION 1");
        assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals() {
        option = new OptionImpl(1, false);
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(2, false);

        assertTrue(option.equals(option1));
        assertFalse(option.equals(option2));
        assertFalse(option.equals(null));
    }

    @Test
    public void testHashCode() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(1, false);
        OptionImpl option3 = new OptionImpl(2, false);

        assertEquals(option1.hashCode(), option1.hashCode());
        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindOptionWithNoMatchingTrigger() {
        option = new OptionImpl(1, false);
        option.checkPrefixes(new HashSet<>(Arrays.asList("prefix2")));
    }

    @Test
    public void testFindOptionWithMatchingTrigger() {
        option = new OptionImpl(1, false);
        option.getTriggers();
        Set triggers = new HashSet<>(Arrays.asList("prefix1", "option1"));
        option.checkPrefixes(triggers);
        assertEquals(option, option.findOption("option1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsRequiredWithNullCommandLine() {
        option = new OptionImpl(1, true);
        option.isRequired();
    }

    @Test
    public void testIsRequiredWithEmptyArgumentsList() {
        option = new OptionImpl(1, true);
        assertFalse(option.isRequired(commandLine, null));
    }

    @Test
    public void testIsRequiredWithMatchingArgument() {
        option = new OptionImpl(1, true);
        commandLine.addArgument("arg1");
        assertTrue(option.isRequired(commandLine, null));
    }

    @Test
    public void testIsRequiredWithMismatchingArgument() {
        option = new OptionImpl(1, true);
        commandLine.addArgument("arg2");
        assertFalse(option.isRequired(commandLine, null));
    }

    @Test
    public void testCheckPrefixesWithEmptyPrefixList() {
        option = new OptionImpl(1, false);
        option.checkPrefixes(new HashSet<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithNonMatchingPrefix() {
        option = new OptionImpl(1, false);
        Set prefixes = new HashSet<>(Arrays.asList("prefix2"));
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesWithMatchingPrefixes() {
        option = new OptionImpl(1, false);
        Set prefixes = new HashSet<>(Arrays.asList("prefix1", "prefix2"));
        option.checkPrefixes(prefixes);
    }
}