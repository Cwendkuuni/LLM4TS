package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {

    private OptionImpl option;

    @Before
    public void setup() {
        option = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        ListIterator arguments = null;

        assertFalse(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        String expectedOutput = "";
        assertEquals(expectedOutput, option.toString());
    }

    @Test
    public void testGetId() {
        int expectedId = 1;
        assertEquals(expectedId, option.getId());
    }

    @Test
    public void testEquals() {
        OptionImpl sameOption = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };

        OptionImpl differentOption = new OptionImpl(2, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };

        assertTrue(option.equals(sameOption));
        assertFalse(option.equals(differentOption));
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = 1;
        assertEquals(expectedHashCode, option.hashCode());
    }

    @Test
    public void testFindOption() {
        String trigger = "trigger";
        assertNull(option.findOption(trigger));
    }

    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = null;
        option.defaults(commandLine);
    }

    @Test
    public void testCheckPrefixes() {
        Set<String> prefixes = new HashSet<>(Arrays.asList("-"));
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes_Exception() {
        Set<String> prefixes = new HashSet<>(Arrays.asList("--"));
        option.getTriggers().add("trigger");
        option.checkPrefixes(prefixes);
    }
}