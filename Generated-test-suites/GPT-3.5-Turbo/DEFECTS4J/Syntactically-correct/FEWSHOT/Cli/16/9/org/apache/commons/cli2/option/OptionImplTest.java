package org.apache.commons.cli2.option;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

public class OptionImplTest {
    private OptionImpl option;

    @Before
    public void setUp() {
        option = new OptionImpl(1, false) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, int displaySetting, Set< ? super Option> handled) {
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Set<String> getPrefixes() {
                return null;
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>();
            }
        };
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        assertFalse(option.canProcess(commandLine, null));
    }

    @Test
    public void testToString() {
        assertNotNull(option.toString());
    }

    @Test
    public void testEquals() {
        Object obj = new Object();
        assertFalse(option.equals(obj));

        OptionImpl sameOption = new OptionImpl(1, false) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, int displaySetting, Set< ? super Option> handled) {
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Set<String> getPrefixes() {
                return null;
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>();
            }
        };
        assertTrue(option.equals(sameOption));

        sameOption = new OptionImpl(2, false) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, int displaySetting, Set< ? super Option> handled) {
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Set<String> getPrefixes() {
                return null;
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>();
            }
        };
        assertFalse(option.equals(sameOption));
    }

    @Test
    public void testHashCode() {
        assertEquals(option.getId(), option.hashCode());
    }

    @Test
    public void testFindOption() {
        assertNull(option.findOption("trigger"));
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = null;
        option.defaults(commandLine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        Set<String> prefixes = null;
        option.checkPrefixes(prefixes);
    }
}