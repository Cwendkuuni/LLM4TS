import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

public class OptionImplTest {

  private OptionImpl option;

  @Before
  public void setUp() {
    option = new OptionImpl(1, true) {
      public boolean canProcess(final WriteableCommandLine commandLine,
                              final ListIterator arguments) {
        return true;
      }

      public String getPreferredName() {
        return "test";
      }

      public String getDescription() {
        return "test option";
      }

      public Set<String> getPrefixes() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("-");
        return prefixes;
      }

      public Set<String> getTriggers() {
        Set<String> triggers = new HashSet<>();
        triggers.add("test");
        return triggers;
      }
    };
  }

  @Test
  public void testCanProcess() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    ListIterator<String> arguments = Arrays.asList("-t", "value").listIterator();
    boolean actual = option.canProcess(commandLine, arguments);
    
    Assert.assertTrue(actual);
  }

  @Test
  public void testToString() {
    String expected = "test";
    String actual = option.toString();
    
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetId() {
    int expected = 1;
    int actual = option.getId();

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testEquals() {
    OptionImpl otherOption = new OptionImpl(1, true) {
      public String getPreferredName() {
        return "test";
      }

      public String getDescription() {
        return "test option";
      }

      public Set<String> getPrefixes() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("-");
        return prefixes;
      }

      public Set<String> getTriggers() {
        Set<String> triggers = new HashSet<>();
        triggers.add("test");
        return triggers;
      }
    };

    boolean actual = option.equals(otherOption);

    Assert.assertTrue(actual);
  }

  @Test
  public void testHashCode() {
    int expected = option.getPreferredName().hashCode();
    expected = (expected * 37) + option.getDescription().hashCode();
    expected = (expected * 37) + option.getPrefixes().hashCode();
    expected = (expected * 37) + option.getTriggers().hashCode();

    int actual = option.hashCode();

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFindOption() {
    String trigger = "test";
    Option actual = option.findOption(trigger);

    Assert.assertEquals(option, actual);
  }

  @Test
  public void testIsRequired() {
    boolean expected = true;
    boolean actual = option.isRequired();

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testDefaults() {
    WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    option.defaults(commandLine);
    // check if any exceptions are thrown
  }
}