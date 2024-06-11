package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, Object argument) {
                return false;
            }
        };

        WriteableCommandLine commandLine = null;
        assertFalse(option.canProcess(commandLine, null));

        commandLine = new WriteableCommandLine() {
            @Override
            public void addOption(Option option, Object value) {
                
            }
        };
        assertFalse(option.canProcess(commandLine, null));

        commandLine = new WriteableCommandLine() {
            @Override
            public void addOption(Option option, Object value) {
                
            }
        };
        assertTrue(option.canProcess(commandLine, "argument"));

        commandLine = new WriteableCommandLine() {
            @Override
            public void addOption(Option option, Object value) {
                
            }
        };
        assertFalse(option.canProcess(commandLine, ""));

        commandLine = new WriteableCommandLine() {
            @Override
            public void addOption(Option option, Object value) {
                
            }
        };
        assertTrue(option.canProcess(commandLine, "valid"));

    }

    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {
                buffer.append("Usage: ").append(getPreferredName());
            }
        };

        assertEquals("Usage: null", option.toString());

        option = new OptionImpl(2, false) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {
                buffer.append("Usage: ").append(getPreferredName());
            }
        };

        assertEquals("Usage: null", option.toString());
    }

    @Test
    public void testEquals() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            protected void checkPrefixes(Set prefixes) {
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {

            }
        };
        OptionImpl sameOption = option;
        assertTrue(option.equals(sameOption));

        Object differentType = new Object();
        assertFalse(option.equals(differentType));

        differentType = null;
        assertFalse(option.equals(differentType));

        OptionImpl differentOption = new OptionImpl(1, true) {
            @Override
            protected void checkPrefixes(Set prefixes) {
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {

            }
        };
        assertFalse(option.equals(differentOption));

        differentOption = new OptionImpl(2, true) {
            @Override
            protected void checkPrefixes(Set prefixes) {
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {

            }
        };
        assertFalse(option.equals(differentOption));

        differentOption = new OptionImpl(1, false) {
            @Override
            protected void checkPrefixes(Set prefixes) {
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {

            }
        };
        assertFalse(option.equals(differentOption));

        differentOption = new OptionImpl(1, true) {
            @Override
            protected void checkPrefixes(Set prefixes) {
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {

            }
        };
        assertTrue(option.equals(differentOption));
    }

    @Test
    public void testHashCode() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            protected void checkPrefixes(Set prefixes) {
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {

            }
        };

        assertEquals(36, option.hashCode());

        option = new OptionImpl(2, false) {
            @Override
            protected void checkPrefixes(Set prefixes) {
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {

            }
        };
        assertEquals(36, option.hashCode());
    }

    @Test
    public void testFindOption() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            protected void checkPrefixes(Set prefixes) {
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {

            }
        };

        assertNull(option.findOption(null));

        option = new OptionImpl(2, false) {
            @Override
            protected void checkPrefixes(Set prefixes) {
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {

            }
        };
        assertNull(option.findOption(null));

        Set<String> triggers = new HashSet<>();
        triggers.add("trigger1");
        option = new OptionImpl(2, false) {
            @Override
            protected void checkPrefixes(Set prefixes) {
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {

            }

            @Override
            public Set<String> getTriggers() {
                return triggers;
            }
        };
        assertNull(option.findOption(null));
    }

    @Test
    public void testIsRequired() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            protected void checkPrefixes(Set prefixes) {
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {

            }
        };

        assertTrue(option.isRequired());

        option = new OptionImpl(2, false) {
            @Override
            protected void checkPrefixes(Set prefixes) {
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {

            }
        };
        assertFalse(option.isRequired());
    }

    @Test
    public void testDefaults() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            protected void checkPrefixes(Set prefixes) {
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {

            }
        };

        WriteableCommandLine commandLine = null;
        option.defaults(commandLine);

        commandLine = new WriteableCommandLine() {
            @Override
            public void addOption(Option option, Object value) {

            }
        };
        option.defaults(commandLine);

    }

    @Test
    public void testCheckPrefixes() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {

            }

            @Override
            protected void checkPrefix(Set prefixes, String trigger) {
                if (trigger.startsWith("pre")) {
                    return;    
                }
                throw new IllegalArgumentException("Trigger needs prefix");
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<String>() {{
                    add("pre_trigger1");
                    add("pre_trigger2");
                    add("trigger1");
                    add("trigger2");
                }};
            }
        };
        Set<String> prefixes = new HashSet<String>() {{
            add("pre");
        }};
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesIllegalArgumentException() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set excludedTriggers) {

            }

            @Override
            protected void checkPrefix(Set prefixes, String trigger) {
                if (trigger.startsWith("pre")) {
                    return;    
                }
                throw new IllegalArgumentException("Trigger needs prefix");
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<String>() {{
                    add("trigger1");
                    add("trigger2");
                }};
            }
        };
        Set<String> prefixes = new HashSet<String>() {{
            add("-");
        }};
        option.checkPrefixes(prefixes);
    }
}