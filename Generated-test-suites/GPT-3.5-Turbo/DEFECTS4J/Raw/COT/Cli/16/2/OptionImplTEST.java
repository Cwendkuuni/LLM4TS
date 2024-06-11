import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class OptionImplTest {
  
  @Test
  public void testCanProcess() {
    OptionImpl option = new OptionImpl(1, true) {
      
      @Override
      public boolean canProcess(WriteableCommandLine commandLine, String argument) {
        return false;
      }
    };
    
    WriteableCommandLine commandLine = null;
    List<String> arguments = null;
    assertFalse(option.canProcess(commandLine, arguments.listIterator()));
    
    arguments = new ArrayList<String>();
    assertFalse(option.canProcess(commandLine, arguments.listIterator()));
    
    arguments.add("test");
    assertFalse(option.canProcess(commandLine, arguments.listIterator()));
    
    commandLine = new WriteableCommandLineImpl();
    assertFalse(option.canProcess(commandLine, arguments.listIterator()));
  }
  
  @Test
  public void testToString() {
    OptionImpl option = new OptionImpl(1, true) {
      
      @Override
      public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, List helpSettings) {
        buffer.append("test");
      }
    };
    
    assertEquals(option.toString(), "test");
  }
  
  @Test
  public void testEquals() {
    OptionImpl option1 = new OptionImpl(1, true) {
      
      @Override
      public Set getTriggers() {
        Set triggers = new HashSet();
        triggers.add("trigger1");
        return triggers;
      }
    };
    
    OptionImpl option2 = new OptionImpl(1, true) {
      
      @Override
      public Set getTriggers() {
        Set triggers = new HashSet();
        triggers.add("trigger2");
        return triggers;
      }
    };
    
    assertFalse(option1.equals(option2));
    
    OptionImpl option3 = new OptionImpl(1, true) {
      @Override
      public Set getTriggers() {
        Set triggers = new HashSet();
        triggers.add("trigger1");
        return triggers;
      }
    };
    
    assertTrue(option1.equals(option3));
    
    OptionImpl option4 = new OptionImpl(2, true) {
      @Override
      public Set getTriggers() {
        Set triggers = new HashSet();
        triggers.add("trigger1");
        return triggers;
      }
    };
    
    assertFalse(option1.equals(option4));
    
    OptionImpl option5 = new OptionImpl(1, true) {
      @Override
      public Set getTriggers() {
        Set triggers = new HashSet();
        return triggers;
      }
    };
    
    assertFalse(option1.equals(option5));
  }
  
  @Test
  public void testHashCode() {
    OptionImpl option = new OptionImpl(1, true) {
      @Override
      public Set getTriggers() {
        Set triggers = new HashSet();
        triggers.add("trigger1");
        return triggers;
      }
    };
    
    assertEquals(option.hashCode(), 113757);
  }
  
  @Test
  public void testFindOption() {
    OptionImpl option = new OptionImpl(1, true) {
      @Override
      public Set getTriggers() {
        Set triggers = new HashSet();
        triggers.add("trigger1");
        return triggers;
      }
    };
    
    assertNull(option.findOption("trigger2"));
    assertEquals(option.findOption("trigger1"), option);
  }
  
  @Test
  public void testIsRequired() {
    OptionImpl option1 = new OptionImpl(1, true) {
      
      @Override
      public Set getTriggers() {
        Set triggers = new HashSet();
        triggers.add("trigger1");
        return triggers;
      }
    };
    
    assertTrue(option1.isRequired());
    
    OptionImpl option2 = new OptionImpl(1, false) {
      
      @Override
      public Set getTriggers() {
        Set triggers = new HashSet();
        triggers.add("trigger1");
        return triggers;
      }
    };
    
    assertFalse(option2.isRequired());
  }
  
  @Test
  public void testDefaults() {
    OptionImpl option = new OptionImpl(1, true) {
      
      @Override
      public Set getTriggers() {
        Set triggers = new HashSet();
        triggers.add("trigger1");
        return triggers;
      }
    };
    
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    option.defaults(commandLine);
  }
  
  @Test
  public void testCheckPrefixes() {
    OptionImpl option = new OptionImpl(1, true) {
      
      @Override
      public Set getTriggers() {
        Set triggers = new HashSet();
        triggers.add("trigger1");
        return triggers;
      }
      
      @Override
      public String getPreferredName() {
        return "preferredName";
      }
    };
    
    Set<String> prefixes = new HashSet<>();
    prefixes.add("-");
    prefixes.add("--");
    
    option.checkPrefixes(prefixes);
  }
  
}