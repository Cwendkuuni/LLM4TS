package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    public void testOptionImplWithNoArguments() {
        OptionImpl option = new OptionImpl(1, false);

        assertTrue(option.canProcess(null, null));
        assertTrue(option.equals(option));
        assertEquals(1, option.hashCode());
        assertNull(option.findOption("trigger"));
        assertFalse(option.isRequired());
    }

    @Test
    public void testOptionImplWithArguments() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "This is an option";
            }

            @Override
            public List getPrefixes() {
                return Arrays.asList("--option", "-o");
            }

            @Override
            public List getTriggers() {
                return Arrays.asList("option", "o");
            }
        };

        WriteableCommandLine commandLine = new WriteableCommandLine();
        Set prefixes = new HashSet(Arrays.asList("--option", "-o"));

        assertTrue(option.canProcess(commandLine, null));
        assertTrue(option.equals(option));
        assertEquals(1, option.hashCode());
        assertEquals(option, option.findOption("option"));
        assertEquals(option, option.findOption("o"));
        assertFalse(option.isRequired());

        prefixes.add("--another-option");
        assertFalse(option.canProcess(commandLine, null));
        assertTrue(option.checkPrefixes(prefixes));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionImplMissingPrefix() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "This is an option";
            }

            @Override
            public List getPrefixes() {
                return Arrays.asList("--other-option");
            }

            @Override
            public List getTriggers() {
                return Arrays.asList("option");
            }
        };

        Set prefixes = new HashSet(Arrays.asList("--option", "-o"));

        assertFalse(option.canProcess(null, null));
        assertFalse(option.checkPrefixes(prefixes));
    }

    @Test
    public void testOptionImplMultipleTriggers() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "This is an option";
            }

            @Override
            public List getPrefixes() {
                return Arrays.asList("--option", "-o");
            }

            @Override
            public List getTriggers() {
                return Arrays.asList("option", "o", "opt");
            }
        };

        WriteableCommandLine commandLine = new WriteableCommandLine();
        Set prefixes = new HashSet(Arrays.asList("--option", "-o"));

        assertTrue(option.canProcess(commandLine, null));
        assertTrue(option.equals(option));
        assertEquals(1, option.hashCode());
        assertEquals(option, option.findOption("option"));
        assertEquals(option, option.findOption("o"));
        assertEquals(option, option.findOption("opt"));
        assertTrue(option.checkPrefixes(prefixes));
    }
}