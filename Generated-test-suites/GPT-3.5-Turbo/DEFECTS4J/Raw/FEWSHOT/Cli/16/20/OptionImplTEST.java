package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {
    
    private OptionImpl option;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
              return "name";
            }

            @Override
            public String getDescription() {
              return "description";
            }

            @Override
            public Set<String> getPrefixes() {
              Set<String> prefixes = new HashSet<>();
              prefixes.add("-");
              prefixes.add("--");
              return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
              Set<String> triggers = new HashSet<>();
              triggers.add("trigger1");
              triggers.add("trigger2");
              return triggers;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
              return true;
            }
        };
        commandLine = mock(WriteableCommandLine.class);
    }

    @Test
    public void testCanProcess() {
        assertTrue(option.canProcess(commandLine, "argument"));
    }

    @Test
    public void testToString() {
        assertEquals("name (trigger1, trigger2)", option.toString());
    }

    @Test
    public void testGetId() {
        assertEquals(1, option.getId());
    }

    @Test
    public void testEquals() {
        OptionImpl option2 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
              return "name";
            }

            @Override
            public String getDescription() {
              return "description";
            }

            @Override
            public Set<String> getPrefixes() {
              Set<String> prefixes = new HashSet<>();
              prefixes.add("-");
              prefixes.add("--");
              return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
              Set<String> triggers = new HashSet<>();
              triggers.add("trigger1");
              triggers.add("trigger2");
              return triggers;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
              return true;
            }
        };
        assertTrue(option.equals(option2));
    }

    @Test
    public void testHashCode() {
        assertEquals(option.hashCode(), option.hashCode());
    }

    @Test
    public void testFindOption() {
        assertEquals(option, option.findOption("trigger1"));
        assertNull(option.findOption("trigger3"));
    }

    @Test
    public void testIsRequired() {
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testDefaults() {
        // nothing to test here
    }

    @Test
    public void testCheckPrefixes() {
        ResourceHelper resourceHelperMock = mock(ResourceHelper.class);
        when(resourceHelperMock.getMessage("OPTION_TRIGGER_NEEDS_PREFIX", "trigger", "-, --")).thenReturn("Error message");
        ResourceHelper.setResourceHelper(resourceHelperMock);

        Set<String> prefixes = new HashSet<>();
        prefixes.add("-");
        prefixes.add("--");
        option.checkPrefixes(prefixes);
    }
}