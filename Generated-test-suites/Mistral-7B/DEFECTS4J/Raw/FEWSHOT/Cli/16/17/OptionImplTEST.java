package org.apache.commons.cli2.option;

import static org.junit.Assert. assertFalse;
import static org.junit.Assert. assertNotNull;
import static org.junit.Assert. assertNull;
import static org.junit.Assert. assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testOptionImplCreation() {
        OptionImpl option = new OptionImpl(1, true);
        assertEquals(1, option.getId());
        assertTrue(option.isRequired());
    }

    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, false);

        WriteableCommandLine cmd = new WriteableCommandLine();
        List<String> args = Arrays.asList("--arg1", "arg2", "--arg3");
        ListIterator<String> arguments = args.listIterator();

        assertFalse(option.canProcess(cmd, arguments));

        String arg1 = (String) arguments.next();
        arguments.previous();

        assertTrue(option.canProcess(cmd, arg1));
    }

    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, false);
        String expectedUsage = "  [--arg1 ARG1]\n";
        String optionString = option.toString();
        assertEquals(expectedUsage, optionString);
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(1, false);

        assertEquals(option1, option2);
    }

    @Test
    public void testHashCode() {
        OptionImpl option = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(1, false);

        assertEquals(option.hashCode(), option2.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindOptionNullTrigger() {
        OptionImpl option = new OptionImpl(1, false);
        assertNull(option.findOption(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindOptionNoMatch() {
        OptionImpl option = new OptionImpl(1, false);
        option.setTriggers(Arrays.asList("trigger1", "trigger2"));

        assertNull(option.findOption("trigger3"));
    }

    @Test
    public void testFindOptionMatch() {
        OptionImpl option = new OptionImpl(1, false);
        option.setTriggers(Arrays.asList("trigger1", "trigger2"));

        assertEquals(option, option.findOption("trigger1"));
    }

    @Test
    public void testFindOptionCaseInsensitive() {
        OptionImpl option = new OptionImpl(1, false);
        option.setTriggers(Arrays.asList("trigger1", "trigger2"));

        assertEquals(option, option.findOption("TRIGGER1"));
    }

    @Test
    public void testIsRequired() {
        OptionImpl option1 = new OptionImpl(1, true);
        OptionImpl option2 = new OptionImpl(2, false);

        assertTrue(option1.isRequired());
        assertFalse(option2.isRequired());
    }

    @Test
    public void testCheckPrefixesEmptyPrefixes() {
        OptionImpl option = new OptionImpl(1, false);

        Set<String> prefixes = new HashSet<>();
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesNonEmptyPrefixesNoMatch() {
        OptionImpl option = new OptionImpl(1, false);
        Set<String> prefixes = new HashSet<>(Arrays.asList("prefix1", "prefix2"));

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesMatch() {
        OptionImpl option = new OptionImpl(1, false);
        Set<String> prefixes = new HashSet<>(Arrays.asList("prefix1", "prefix2"));

        option.setPreferredName("prefix1arg");
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithTriggers() {
        OptionImpl option = new OptionImpl(1, false);
        Set<String> prefixes = new HashSet<>(Arrays.asList("prefix1", "prefix2"));

        option.setTriggers(Arrays.asList("trigger1", "trigger2"));
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesWithTriggerMatch() {
        OptionImpl option = new OptionImpl(1, false);
        Set<String> prefixes = new HashSet<>(Arrays.asList("prefix1", "prefix2"));

        option.setPreferredName("prefix1arg");
        option.setTriggers(Arrays.asList("trigger1", "prefix1trigger"));

        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithTriggerMismatch() {
        OptionImpl option = new OptionImpl(1, false);
        Set<String> prefixes = new HashSet<>(Arrays.asList("prefix1", "prefix2"));

        option.setPreferredName("prefix1arg");
        option.setTriggers(Arrays.asList("trigger1", "prefix2trigger"));

        option.checkPrefixes(prefixes);
    }
}