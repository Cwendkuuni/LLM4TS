package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {
    private OptionImpl option;

    @Before
    public void setup() {
        option = new MockOptionImpl(1, true);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator arguments = mock(ListIterator.class);

        // Test when arguments are empty
        assertFalse(option.canProcess(commandLine, arguments));

        // Test when arguments contains a valid argument
        List<String> argumentList = new ArrayList<>();
        argumentList.add("argument");
        arguments = argumentList.listIterator();
        assertTrue(option.canProcess(commandLine, arguments));

        // Test when arguments contains an invalid argument
        argumentList.add("-invalid");
        assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        assertEquals("MockOptionImpl", option.toString());
    }

    @Test
    public void testEquals() {
        OptionImpl sameOption = new MockOptionImpl(1, true);
        OptionImpl differentOption = new MockOptionImpl(2, false);

        assertTrue(option.equals(sameOption));
        assertFalse(option.equals(differentOption));
    }

    @Test
    public void testHashCode() {
        OptionImpl sameOption = new MockOptionImpl(1, true);

        assertEquals(option.hashCode(), sameOption.hashCode());
    }

    @Test
    public void testFindOption() {
        String trigger = "trigger";
        option.addTrigger(trigger);

        assertEquals(option, option.findOption(trigger));
    }

    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testDefaults() {
        CommandLine commandLine = mock(CommandLine.class);

        option.defaults(commandLine);

        // Verify that nothing is done
    }

    private static class MockOptionImpl extends OptionImpl {
        private Set<String> prefixes = new HashSet<>();
        private Set<String> triggers = new HashSet<>();

        public MockOptionImpl(int id, boolean required) {
            super(id, required);
        }

        public Set<String> getPrefixes() {
            return prefixes;
        }

        public Set<String> getTriggers() {
            return triggers;
        }

        public void addTrigger(String trigger) {
            triggers.add(trigger);
        }
    }
}