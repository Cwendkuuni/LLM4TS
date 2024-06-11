import org.junit.Before;
import org.junit.Test;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import static org.junit.Assert.*;

public class OptionTest {
  
  private Option option;
  
  @Before
  public void setUp() {
    // Initialize option object for testing
    option = new Option() {
      @Override
      public void process(final WriteableCommandLine commandLine, final ListIterator args) throws OptionException {
      }
      
      @Override
      public void defaults(final WriteableCommandLine commandLine) {
      }
      
      @Override
      public boolean canProcess(final WriteableCommandLine commandLine, final String argument) {
        return false;
      }
      
      @Override
      public boolean canProcess(final WriteableCommandLine commandLine, final ListIterator arguments) {
        return false;
      }
      
      @Override
      public Set getTriggers() {
        return null;
      }
      
      @Override
      public Set getPrefixes() {
        return null;
      }
      
      @Override
      public void validate(final WriteableCommandLine commandLine) throws OptionException {
      }
      
      @Override
      public List helpLines(final int depth, final Set helpSettings, final Comparator comp) {
        return null;
      }
      
      @Override
      public void appendUsage(final StringBuffer buffer, final Set helpSettings, final Comparator comp) {
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
      public int getId() {
        return 0;
      }
      
      @Override
      public Option findOption(final String trigger) {
        return null;
      }
      
      @Override
      public boolean isRequired() {
        return false;
      }
      
      @Override
      public Option getParent() {
        return null;
      }
      
      @Override
      public void setParent(final Option parent) {
      }
    };
  }
  
  @Test
  public void testProcess() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator args = null;
    try {
      option.process(commandLine, args);
    } catch (OptionException e) {
      fail("OptionException thrown");
    }
  }
  
  @Test
  public void testDefaults() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    option.defaults(commandLine);
  }
  
  @Test
  public void testCanProcessString() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    String argument = "";
    boolean result = option.canProcess(commandLine, argument);
    assertEquals(false, result);
  }
  
  @Test
  public void testCanProcessListIterator() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator arguments = null;
    boolean result = option.canProcess(commandLine, arguments);
    assertEquals(false, result);
  }
  
  @Test
  public void testGetTriggers() {
    Set triggers = option.getTriggers();
    assertEquals(null, triggers);
  }
  
  @Test
  public void testGetPrefixes() {
    Set prefixes = option.getPrefixes();
    assertEquals(null, prefixes);
  }
  
  @Test
  public void testValidate() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    try {
      option.validate(commandLine);
    } catch (OptionException e) {
      fail("OptionException thrown");
    }
  }
  
  @Test
  public void testHelpLines() {
    int depth = 0;
    Set helpSettings = null;
    Comparator comp = null;
    List helpLines = option.helpLines(depth, helpSettings, comp);
    assertEquals(null, helpLines);
  }
  
  @Test
  public void testAppendUsage() {
    StringBuffer buffer = new StringBuffer();
    Set helpSettings = null;
    Comparator comp = null;
    option.appendUsage(buffer, helpSettings, comp);
  }
  
  @Test
  public void testGetPreferredName() {
    String preferredName = option.getPreferredName();
    assertEquals(null, preferredName);
  }
  
  @Test
  public void testGetDescription() {
    String description = option.getDescription();
    assertEquals(null, description);
  }
  
  @Test
  public void testGetId() {
    int id = option.getId();
    assertEquals(0, id);
  }
  
  @Test
  public void testFindOption() {
    String trigger = "";
    Option foundOption = option.findOption(trigger);
    assertEquals(null, foundOption);
  }
  
  @Test
  public void testIsRequired() {
    boolean isRequired = option.isRequired();
    assertEquals(false, isRequired);
  }
  
  @Test
  public void testGetParent() {
    Option parent = option.getParent();
    assertEquals(null, parent);
  }
  
  @Test
  public void testSetParent() {
    Option parent = null;
    option.setParent(parent);
  }
  
}