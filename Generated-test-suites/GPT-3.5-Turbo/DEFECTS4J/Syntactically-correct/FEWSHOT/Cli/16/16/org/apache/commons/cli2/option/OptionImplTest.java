package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {
    private OptionImpl option;

    @Before
    public void setUp() {
        option = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, int setting, Set helpSettings) {
            }
        };
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");

        boolean result = option.canProcess(commandLine, arguments.listIterator());
        assertFalse(result);

        arguments.clear();

        result = option.canProcess(commandLine, arguments.listIterator());
        assertFalse(result);
    }

    @Test
    public void testToString() {
        String output = option.toString();
        assertNotNull(output);
    }

    @Test
    public void testEquals() {
        OptionImpl option2 = null;

        boolean result = option.equals(option2);
        assertFalse(result);

        option2 = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, int setting, Set helpSettings) {
            }
        };

        result = option.equals(option2);
        assertFalse(result);

        option2 = option;

        result = option.equals(option2);
        assertTrue(result);
    }

    @Test
    public void testHashCode() {
        int hashCode = option.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testFindOption() {
        String trigger = "trigger";
        OptionImpl result = option.findOption(trigger);
        assertNull(result);
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = null;
        option.defaults(commandLine);
    }

    @Test
    public void testCheckPrefixes() {
        Set<String> prefixes = null;
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefix() {
        Set<String> prefixes = null;
        String trigger = null;

        ResourceHelper helper = ResourceHelper.getResourceHelper();
        try {
            option.checkPrefix(prefixes, trigger);
        } catch (IllegalArgumentException e) {
            assertEquals(helper.getMessage(ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX, trigger,
                prefixes.toString()), e.getMessage());
        }
    }
}