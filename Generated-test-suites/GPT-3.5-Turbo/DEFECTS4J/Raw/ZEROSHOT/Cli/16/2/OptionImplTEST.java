import static org.junit.Assert.*;
import org.junit.Test;

public class OptionImplTest {
  
  @Test
  public void testCanProcess() {
    OptionImpl option = new OptionImpl(1, true) {
      public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
        if (arguments.hasNext()) {
          return true;
        } else {
          return false;
        }
      }
    };
    
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator arguments = new ArrayList().iterator();
    assertFalse(option.canProcess(commandLine, arguments));
    
    arguments.add("test");
    assertTrue(option.canProcess(commandLine, arguments));
  }
  
  @Test
  public void testToString() {
    OptionImpl option = new OptionImpl(1, true) {
      public String getDescription() {
        return "test description";
      }
      
      public void appendUsage(StringBuffer buffer, DisplaySetting setting, String childPrefix) {
        buffer.append("test usage");
      }
    };
    
    assertEquals("test description", option.toString());
  }
  
  @Test
  public void testEquals() {
    OptionImpl option1 = new OptionImpl(1, true) {
      public String getPreferredName() {
        return "test1";
      }
      
      public String getDescription() {
        return "test description1";
      }
      
      public List getPrefixes() {
        List prefixes = new ArrayList();
        prefixes.add("prefix1");
        return prefixes;
      }
      
      public Set getTriggers() {
        Set triggers = new HashSet();
        triggers.add("trigger1");
        return triggers;
      }
    };
    
    OptionImpl option2 = new OptionImpl(2, false) {
      public String getPreferredName() {
        return "test2";
      }
      
      public String getDescription() {
        return "test description2";
      }
      
      public List getPrefixes() {
        List prefixes = new ArrayList();
        prefixes.add("prefix2");
        return prefixes;
      }
      
      public Set getTriggers() {
        Set triggers = new HashSet();
        triggers.add("trigger2");
        return triggers;
      }
    };
    
    assertTrue(option1.equals(option1));
    assertFalse(option1.equals(option2));
  }
  
  @Test
  public void testHashCode() {
    OptionImpl option = new OptionImpl(1, true) {
      public String getPreferredName() {
        return "test";
      }
      
      public String getDescription() {
        return "test description";
      }
      
      public List getPrefixes() {
        List prefixes = new ArrayList();
        prefixes.add("prefix");
        return prefixes;
      }
      
      public Set getTriggers() {
        Set triggers = new HashSet();
        triggers.add("trigger");
        return triggers;
      }
    };
    
    assertEquals(41529304, option.hashCode());
  }
  
  @Test
  public void testFindOption() {
    OptionImpl option = new OptionImpl(1, true) {
      public Set getTriggers() {
        Set triggers = new HashSet();
        triggers.add("trigger1");
        triggers.add("trigger2");
        return triggers;
      }
    };
    
    assertEquals(option, option.findOption("trigger1"));
    assertNull(option.findOption("trigger3"));
  }
  
  @Test
  public void testIsRequired() {
    OptionImpl option1 = new OptionImpl(1, true) {
      public Set getTriggers() {
        return new HashSet();
      }
    };
    
    OptionImpl option2 = new OptionImpl(2, false) {
      public Set getTriggers() {
        return new HashSet();
      }
    };
    
    assertTrue(option1.isRequired());
    assertFalse(option2.isRequired());
  }
  
  @Test
  public void testDefaults() {
    OptionImpl option = new OptionImpl(1, true) {
      public void defaults(WriteableCommandLine commandLine) {
        commandLine.setProperty("test", "value");
      }
      
      public Set getTriggers() {
        return new HashSet();
      }
    };
    
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    option.defaults(commandLine);
    assertEquals("value", commandLine.getProperty("test"));
  }
  
  @Test
  public void testCheckPrefixes() {
    OptionImpl option = new OptionImpl(1, true) {
      public Set getTriggers() {
        return new HashSet();
      }
    };
    
    Set prefixes = new HashSet();
    prefixes.add("prefix");
    prefixes.add("pre");
    
    option.checkPrefixes(prefixes);
    
    prefixes.remove("pre");
    
    try {
      option.checkPrefixes(prefixes);
      fail("Should have thrown an IllegalArgumentException");
    } catch (Exception e) {
      assertEquals(IllegalArgumentException.class, e.getClass());
    }
  }
}