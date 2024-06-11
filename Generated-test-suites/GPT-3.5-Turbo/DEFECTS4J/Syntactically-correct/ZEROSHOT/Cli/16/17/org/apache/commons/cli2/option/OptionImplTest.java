import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.cli2.*;
import org.apache.commons.cli2.option.*;
import java.util.*;

public class OptionImplTest {
  
  @Test
  public void testCanProcess() {
    // create OptionImpl object
    OptionImpl option = new OptionImpl(1, true) {
      public boolean canProcess(final WriteableCommandLine commandLine,
                                final ListIterator arguments) {
        return true;
      }
    };

    // create WriteableCommandLine object
    WriteableCommandLine commandLine = new WriteableCommandLine() {
      public Option getOption(int id) {
        return null;
      }
      public Option[] getOptions() {
        return null;
      }
      public boolean hasOption(int id) {
        return false;
      }
      public boolean hasOption(String trigger) {
        return false;
      }
      public String[] getOptionValues(int id) {
        return null;
      }
      public String getOptionValue(int id) {
        return null;
      }
      public String getOptionValue(String trigger) {
        return null;
      }
      public List getOptionValuesList(int id) {
        return null;
      }
      public List getOptionValuesList(String trigger) {
        return null;
      }
      public void addOption(int id) {}
      public void addOption(Option option) {}
      public void addOption(int id, String argument) {}
      public void addOption(String trigger, String argument) {}
      public void addValue(int id, String value) {}
      public void addValue(String trigger, String value) {}
    };

    // create ListIterator object
    ListIterator arguments = new ListIterator() {
      public void add(Object arg0) {}
      public boolean hasNext() {
        return true;
      }
      public boolean hasPrevious() {
        return true;
      }
      public Object next() {
        return "";
      }
      public int nextIndex() {
        return 0;
      }
      public Object previous() {
        return "";
      }
      public int previousIndex() {
        return 0;
      }
      public void remove() {}
      public void set(Object arg0) {}
    };

    // test canProcess method
    assertTrue(option.canProcess(commandLine, arguments));
  }
  
  @Test
  public void testToString() {
    // create OptionImpl object
    OptionImpl option = new OptionImpl(1, true) {
      public void appendUsage(StringBuffer buffer, DisplaySetting settings,
                              List prefixes) {}
    };
    
    // test toString method
    assertNull(option.toString());
  }
  
  @Test
  public void testEquals() {
    // create OptionImpl objects
    OptionImpl option1 = new OptionImpl(1, true) {
      public void appendUsage(StringBuffer buffer, DisplaySetting settings,
                              List prefixes) {}
    };
    OptionImpl option2 = new OptionImpl(1, true) {
      public void appendUsage(StringBuffer buffer, DisplaySetting settings,
                              List prefixes) {}
    };
    
    // test equals method
    assertTrue(option1.equals(option2));
  }
  
  @Test
  public void testHashCode() {
    // create OptionImpl object
    OptionImpl option = new OptionImpl(1, true) {
      public void appendUsage(StringBuffer buffer, DisplaySetting settings,
                              List prefixes) {}
    };
    
    // test hashCode method
    assertEquals(option.hashCode(), option.getId());
  }
  
  @Test
  public void testFindOption() {
    // create OptionImpl object
    OptionImpl option = new OptionImpl(1, true) {
      public void appendUsage(StringBuffer buffer, DisplaySetting settings,
                              List prefixes) {}
    };
    
    // test findOption method
    assertNull(option.findOption(""));
    assertNull(option.findOption("trigger"));
  }
  
  @Test
  public void testIsRequired() {
    // create OptionImpl object
    OptionImpl option = new OptionImpl(1, true) {
      public void appendUsage(StringBuffer buffer, DisplaySetting settings,
                              List prefixes) {}
    };
    
    // test isRequired method
    assertTrue(option.isRequired());
  }
  
  @Test
  public void testDefaults() {
    // create OptionImpl object
    OptionImpl option = new OptionImpl(1, true) {
      public void appendUsage(StringBuffer buffer, DisplaySetting settings,
                              List prefixes) {}
      public void defaults(final WriteableCommandLine commandLine) {}
    };
    
    // create WriteableCommandLine object
    WriteableCommandLine commandLine = new WriteableCommandLine() {
      public Option getOption(int id) {
        return null;
      }
      public Option[] getOptions() {
        return null;
      }
      public boolean hasOption(int id) {
        return false;
      }
      public boolean hasOption(String trigger) {
        return false;
      }
      public String[] getOptionValues(int id) {
        return null;
      }
      public String getOptionValue(int id) {
        return null;
      }
      public String getOptionValue(String trigger) {
        return null;
      }
      public List getOptionValuesList(int id) {
        return null;
      }
      public List getOptionValuesList(String trigger) {
        return null;
      }
      public void addOption(int id) {}
      public void addOption(Option option) {}
      public void addOption(int id, String argument) {}
      public void addOption(String trigger, String argument) {}
      public void addValue(int id, String value) {}
      public void addValue(String trigger, String value) {}
    };
    
    // test defaults method
    option.defaults(commandLine);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCheckPrefixes() {
    // create OptionImpl object
    OptionImpl option = new OptionImpl(1, true) {
      public void appendUsage(StringBuffer buffer, DisplaySetting settings,
                              List prefixes) {}
    };
    
    // create HashSet object
    Set prefixes = new HashSet();
    prefixes.add("-");
    
    // test checkPrefixes method
    option.checkPrefixes(prefixes);
  }
}