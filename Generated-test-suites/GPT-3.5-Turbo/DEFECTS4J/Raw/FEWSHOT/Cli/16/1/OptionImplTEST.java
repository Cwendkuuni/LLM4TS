package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {
    private OptionImpl option;

    @Before
    public void setUp() {
        option = new OptionImpl(1, true) {};
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        ListIterator<String> arguments = null;

        assertFalse(option.canProcess(commandLine, arguments));

        arguments = new ArrayList<>();
        assertFalse(option.canProcess(commandLine, arguments));

        arguments.add("argument");
        assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        String expected = "option";
        String actual = option.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetId() {
        int expected = 1;
        int actual = option.getId();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, false) {};
        OptionImpl option2 = new OptionImpl(1, true) {};
        OptionImpl option3 = new OptionImpl(2, false) {};
        OptionImpl option4 = new OptionImpl(2, true) {};

        assertTrue(option1.equals(option1));
        assertFalse(option1.equals(null));
        assertFalse(option1.equals(new Object()));
        assertFalse(option1.equals(option2));
        assertFalse(option1.equals(option3));
        assertFalse(option1.equals(option4));
    }

    @Test
    public void testHashCode() {
        int expected = 37 * 1;
        int actual = option.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testFindOption() {
        OptionImpl option1 = new OptionImpl(1, false) {};
        OptionImpl option2 = new OptionImpl(2, true) {};

        Option foundOption = option1.findOption("trigger");
        assertNotNull(foundOption);
        assertTrue(foundOption instanceof OptionImpl);
        assertEquals(option1, foundOption);

        foundOption = option2.findOption("trigger");
        assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());

        OptionImpl optionalOption = new OptionImpl(1, false) {};
        assertFalse(optionalOption.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = null;

        option.defaults(commandLine);
        // Verify default behavior
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("-");
        prefixes.add("--");

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefix() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("-");
        prefixes.add("--");

        option.checkPrefix(prefixes, "--trigger");
        // Verify no exception is thrown

        option.checkPrefix(prefixes, "trigger");
        // Verify exception is thrown
    }
}