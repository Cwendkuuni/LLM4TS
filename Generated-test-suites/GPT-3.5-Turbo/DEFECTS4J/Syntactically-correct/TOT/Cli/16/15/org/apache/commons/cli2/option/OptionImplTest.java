import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.ListIterator;

import static org.junit.Assert.*;

public class OptionImplTest {
    
    private OptionImpl option;
    private Set<String> prefixes;
    private Set<String> triggers;
    
    @Before
    public void setUp() {
        prefixes = new HashSet<>();
        prefixes.add("-");
        prefixes.add("--");
        
        triggers = new HashSet<>();
        triggers.add("a");
        triggers.add("add");
        
        option = new OptionImpl(1, true) {
            @Override
            public ListIterator getTriggers() {
                return new LinkedList<>(triggers).listIterator();
            }
            
            @Override
            public String getPreferredName() {
                return "add";
            }
            
            @Override
            public String getDescription() {
                return "Add option";
            }
            
            @Override
            public Set getPrefixes() {
                return prefixes;
            }
        };
    }
    
    @Test
    public void testCanProcess_ValidArgument_ReturnsTrue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = new LinkedList<>(Collections.singletonList("add")).listIterator();
        
        boolean result = option.canProcess(commandLine, arguments);
        
        assertTrue(result);
    }
    
    @Test
    public void testCanProcess_InvalidArgument_ReturnsFalse() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = new LinkedList<>(Collections.singletonList("sub")).listIterator();
        
        boolean result = option.canProcess(commandLine, arguments);
        
        assertFalse(result);
    }
    
    @Test
    public void testToString_ReturnsCorrectString() {
        String expected = "add";
        
        String result = option.toString();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        boolean result = option.equals(option);
        
        assertTrue(result);
    }
    
    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        boolean result = option.equals(null);
        
        assertFalse(result);
    }
    
    @Test
    public void testEquals_ObjectOfDifferentClass_ReturnsFalse() {
        boolean result = option.equals("add");
        
        assertFalse(result);
    }
    
    @Test
    public void testEquals_ObjectWithDifferentId_ReturnsFalse() {
        OptionImpl other = new OptionImpl(2, true) {
            @Override
            public ListIterator getTriggers() {
                return new LinkedList<>(triggers).listIterator();
            }
            
            @Override
            public String getPreferredName() {
                return "add";
            }
            
            @Override
            public String getDescription() {
                return "Add option";
            }
            
            @Override
            public Set getPrefixes() {
                return prefixes;
            }
        };
        
        boolean result = option.equals(other);
        
        assertFalse(result);
    }
    
    @Test
    public void testEquals_ObjectWithDifferentPreferredName_ReturnsFalse() {
        OptionImpl other = new OptionImpl(1, true) {
            @Override
            public ListIterator getTriggers() {
                return new LinkedList<>(triggers).listIterator();
            }
            
            @Override
            public String getPreferredName() {
                return "subtract";
            }
            
            @Override
            public String getDescription() {
                return "Subtract option";
            }
            
            @Override
            public Set getPrefixes() {
                return prefixes;
            }
        };
        
        boolean result = option.equals(other);
        
        assertFalse(result);
    }
    
    @Test
    public void testEquals_ObjectWithDifferentDescription_ReturnsFalse() {
        OptionImpl other = new OptionImpl(1, true) {
            @Override
            public ListIterator getTriggers() {
                return new LinkedList<>(triggers).listIterator();
            }
            
            @Override
            public String getPreferredName() {
                return "add";
            }
            
            @Override
            public String getDescription() {
                return "Subtract option";
            }
            
            @Override
            public Set getPrefixes() {
                return prefixes;
            }
        };
        
        boolean result = option.equals(other);
        
        assertFalse(result);
    }
    
    @Test
    public void testEquals_ObjectWithDifferentPrefixes_ReturnsFalse() {
        Set<String> otherPrefixes = new HashSet<>();
        otherPrefixes.add("-");
        otherPrefixes.add("---");
        
        OptionImpl other = new OptionImpl(1, true) {
            @Override
            public ListIterator getTriggers() {
                return new LinkedList<>(triggers).listIterator();
            }
            
            @Override
            public String getPreferredName() {
                return "add";
            }
            
            @Override
            public String getDescription() {
                return "Add option";
            }
            
            @Override
            public Set getPrefixes() {
                return otherPrefixes;
            }
        };
        
        boolean result = option.equals(other);
        
        assertFalse(result);
    }
    
    @Test
    public void testEquals_ObjectWithDifferentTriggers_ReturnsFalse() {
        Set<String> otherTriggers = new HashSet<>();
        otherTriggers.add("a");
        otherTriggers.add("subtract");
        
        OptionImpl other = new OptionImpl(1, true) {
            @Override
            public ListIterator getTriggers() {
                return new LinkedList<>(otherTriggers).listIterator();
            }
            
            @Override
            public String getPreferredName() {
                return "add";
            }
            
            @Override
            public String getDescription() {
                return "Add option";
            }
            
            @Override
            public Set getPrefixes() {
                return prefixes;
            }
        };
        
        boolean result = option.equals(other);
        
        assertFalse(result);
    }
    
    @Test
    public void testHashCode_ReturnsCorrectHashCode() {
        int expected = 31 * 37 * 37 * 37 * 37;
        expected += "add".hashCode();
        expected = (expected * 37) + "Add option".hashCode();
        expected = (expected * 37) + prefixes.hashCode();
        expected = (expected * 37) + triggers.hashCode();
        
        int result = option.hashCode();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFindOption_TriggerExists_ReturnsThisOption() {
        String trigger = "add";
        
        Option result = option.findOption(trigger);
        
        assertEquals(option, result);
    }
    
    @Test
    public void testFindOption_TriggerDoesNotExist_ReturnsNull() {
        String trigger = "subtract";
        
        Option result = option.findOption(trigger);
        
        assertNull(result);
    }
    
    @Test
    public void testIsRequired_ReturnsTrue() {
        boolean result = option.isRequired();
        
        assertTrue(result);
    }
    
    @Test
    public void testDefaults_DoesNothing() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        option.defaults(commandLine);
        
        // Assert no exception is thrown and no changes are made to commandLine
    }
    
    @Test
    public void testCheckPrefixes_NoPrefixes_DoesNothing() {
        Set<String> emptyPrefixes = new HashSet<>();
        
        option.checkPrefixes(emptyPrefixes);
        
        // Assert no exception is thrown
    }
    
    @Test
    public void testCheckPrefixes_PrefixesExist_NoExceptionThrown() {
        option.checkPrefixes(prefixes);
        
        // Assert no exception is thrown
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes_WrongPrefix_ExceptionThrown() {
        prefixes.clear();
        prefixes.add("--");
        
        option.checkPrefixes(prefixes);
    }

}