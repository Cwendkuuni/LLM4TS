package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.ListIterator;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class OptionImplTest {
    
    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, true) {
            
            @Override
            protected boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            protected void appendUsage(StringBuffer buffer, DisplaySetting setting, ListIterator triggerIterator) {
                // not implemented
            }

            @Override
            protected void process(WriteableCommandLine commandLine, ListIterator arguments) {
                // not implemented
            }

        };
        
        WriteableCommandLine commandLine = null;
        ListIterator<String> arguments = null;
        
        assertTrue(option.canProcess(commandLine, arguments));
    }
    
    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, true) {
            
            @Override
            protected boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            protected void appendUsage(StringBuffer buffer, DisplaySetting setting, ListIterator triggerIterator) {
                // not implemented
            }

            @Override
            protected void process(WriteableCommandLine commandLine, ListIterator arguments) {
                // not implemented
            }

        };
        
        String expected = "";
        assertEquals(expected, option.toString());
    }
    
    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, true) {
            
            @Override
            protected boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            protected void appendUsage(StringBuffer buffer, DisplaySetting setting, ListIterator triggerIterator) {
                // not implemented
            }

            @Override
            protected void process(WriteableCommandLine commandLine, ListIterator arguments) {
                // not implemented
            }

        };
        
        OptionImpl option2 = new OptionImpl(2, false) {
            
            @Override
            protected boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            protected void appendUsage(StringBuffer buffer, DisplaySetting setting, ListIterator triggerIterator) {
                // not implemented
            }

            @Override
            protected void process(WriteableCommandLine commandLine, ListIterator arguments) {
                // not implemented
            }

        };
        
        assertTrue(option1.equals(option1));
        assertFalse(option1.equals(null));
        assertFalse(option1.equals("test"));
        
        assertFalse(option1.equals(option2));
    }
    
    @Test
    public void testHashCode() {
        OptionImpl option1 = new OptionImpl(1, true) {
            
            @Override
            protected boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            protected void appendUsage(StringBuffer buffer, DisplaySetting setting, ListIterator triggerIterator) {
                // not implemented
            }

            @Override
            protected void process(WriteableCommandLine commandLine, ListIterator arguments) {
                // not implemented
            }

        };
        
        OptionImpl option2 = new OptionImpl(2, false) {
            
            @Override
            protected boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            protected void appendUsage(StringBuffer buffer, DisplaySetting setting, ListIterator triggerIterator) {
                // not implemented
            }

            @Override
            protected void process(WriteableCommandLine commandLine, ListIterator arguments) {
                // not implemented
            }

        };
        
        int expected1 = 1;
        int expected2 = 2;
        
        assertEquals(expected1, option1.hashCode());
        assertEquals(expected2, option2.hashCode());
    }
    
    @Test
    public void testFindOption() {
        OptionImpl option1 = new OptionImpl(1, true) {
            
            @Override
            protected boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            protected void appendUsage(StringBuffer buffer, DisplaySetting setting, ListIterator triggerIterator) {
                // not implemented
            }

            @Override
            protected void process(WriteableCommandLine commandLine, ListIterator arguments) {
                // not implemented
            }

        };
        
        OptionImpl option2 = new OptionImpl(2, false) {
            
            @Override
            protected boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            protected void appendUsage(StringBuffer buffer, DisplaySetting setting, ListIterator triggerIterator) {
                // not implemented
            }

            @Override
            protected void process(WriteableCommandLine commandLine, ListIterator arguments) {
                // not implemented
            }

        };
        
        String trigger1 = "trigger1";
        String trigger2 = "trigger2";
        
        option1.addTrigger(trigger1);
        option2.addTrigger(trigger2);
        
        Option expectedOption1 = option1;
        Option expectedOption2 = option2;
        
        Option actualOption1 = option1.findOption(trigger1);
        Option actualOption2 = option2.findOption(trigger2);
        Option actualOption3 = option2.findOption(trigger1);
        Option actualOption4 = option2.findOption(trigger2+"extra");
        
        assertEquals(expectedOption1, actualOption1);
        assertEquals(expectedOption2, actualOption2);
        assertNull(actualOption3);
        assertNull(actualOption4);
    }
    
    @Test
    public void testIsRequired() {
        OptionImpl option1 = new OptionImpl(1, true) {
            
            @Override
            protected boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            protected void appendUsage(StringBuffer buffer, DisplaySetting setting, ListIterator triggerIterator) {
                // not implemented
            }

            @Override
            protected void process(WriteableCommandLine commandLine, ListIterator arguments) {
                // not implemented
            }

        };
        
        OptionImpl option2 = new OptionImpl(2, false) {
            
            @Override
            protected boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            protected void appendUsage(StringBuffer buffer, DisplaySetting setting, ListIterator triggerIterator) {
                // not implemented
            }

            @Override
            protected void process(WriteableCommandLine commandLine, ListIterator arguments) {
                // not implemented
            }

        };
        
        assertTrue(option1.isRequired());
        assertFalse(option2.isRequired());
    }
    
    @Test
    public void testDefaults() {
        OptionImpl option = new OptionImpl(1, true) {
            
            @Override
            protected boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            protected void appendUsage(StringBuffer buffer, DisplaySetting setting, ListIterator triggerIterator) {
                // not implemented
            }

            @Override
            protected void process(WriteableCommandLine commandLine, ListIterator arguments) {
                // not implemented
            }

        };
        
        WriteableCommandLine commandLine = null;
        
        option.defaults(commandLine);
    }
    
    @Test
    public void testCheckPrefixes() {
        OptionImpl option = new OptionImpl(1, true) {
            
            @Override
            protected boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            protected void appendUsage(StringBuffer buffer, DisplaySetting setting, ListIterator triggerIterator) {
                // not implemented
            }

            @Override
            protected void process(WriteableCommandLine commandLine, ListIterator arguments) {
                // not implemented
            }

        };
        
        Set<String> prefixes = new HashSet<String>();
        prefixes.add("-");
        
        option.checkPrefixes(prefixes);
    }
    
    private boolean equals(Object left,
            Object right) {
        if ((left == null) && (right == null)) {
            return true;
        } else if ((left == null) || (right == null)) {
            return false;
        } else {
            return left.equals(right);
        }
    }
    
}