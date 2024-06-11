import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

  @Test
  public void testProcess() {
    WriteableCommandLine commandLine = new CommandLineImpl();
    Option option = new OptionImpl();

    // Test case 1: Empty argument list
    ListIterator<String> args1 = new ArrayList<String>().listIterator();
    try {
      option.process(commandLine, args1);
      fail("Expected OptionException");
    } catch (OptionException e) {
      assertEquals("Empty argument list", e.getMessage());
    }

    // Test case 2: Non-empty argument list
    ListIterator<String> args2 = new ArrayList<String>(Arrays.asList("-a")).listIterator();
    try {
      option.process(commandLine, args2);
      assertEquals(true, commandLine.getOption("a"));
    } catch (OptionException e) {
      fail("Unexpected OptionException");
    }
  }

  @Test
  public void testDefaults() {
    WriteableCommandLine commandLine = new CommandLineImpl();
    Option option = new OptionImpl();

    // Test case 1: Option has no defaults
    option.defaults(commandLine);
    assertEquals(0, commandLine.getOptions().size());

    // Test case 2: Option has defaults
    option.setDefault("default");
    option.defaults(commandLine);
    assertEquals(1, commandLine.getOptions().size());
    assertEquals("default", commandLine.getOption("a"));
  }

  @Test
  public void testCanProcess() {
    WriteableCommandLine commandLine = new CommandLineImpl();
    Option option = new OptionImpl();

    // Test case 1: Option can process the argument
    assertEquals(true, option.canProcess(commandLine, "-a"));

    // Test case 2: Option cannot process the argument
    assertEquals(false, option.canProcess(commandLine, "abc"));
  }

  @Test
  public void testGetTriggers() {
    Option option = new OptionImpl();

    // Test case 1: Empty triggers set
    Set<String> triggers1 = option.getTriggers();
    assertNotNull(triggers1);
    assertEquals(0, triggers1.size());

    // Test case 2: Non-empty triggers set
    option.addTrigger("-a");
    Set<String> triggers2 = option.getTriggers();
    assertNotNull(triggers2);
    assertEquals(1, triggers2.size());
    assertTrue(triggers2.contains("-a"));
  }

  @Test
  public void testGetPrefixes() {
    Option option = new OptionImpl();

    // Test case 1: Empty prefixes set
    Set<String> prefixes1 = option.getPrefixes();
    assertNotNull(prefixes1);
    assertEquals(0, prefixes1.size());

    // Test case 2: Non-empty prefixes set
    option.addPrefix("-a");
    Set<String> prefixes2 = option.getPrefixes();
    assertNotNull(prefixes2);
    assertEquals(1, prefixes2.size());
    assertTrue(prefixes2.contains("-a"));
  }

  @Test
  public void testValidate() {
    WriteableCommandLine commandLine = new CommandLineImpl();
    Option option = new OptionImpl();

    // Test case 1: Option is valid
    try {
      option.validate(commandLine);
    } catch (OptionException e) {
      fail("Unexpected OptionException");
    }

    // Test case 2: Option is not valid
    option.setRequired(true);
    try {
      option.validate(commandLine);
      fail("Expected OptionException");
    } catch (OptionException e) {
      assertEquals("Option is required", e.getMessage());
    }
  }

  @Test
  public void testHelpLines() {
    Option option = new OptionImpl();

    // Test case: Empty help lines
    List<HelpLineImpl> helpLines = option.helpLines(0, null, null);
    assertNotNull(helpLines);
    assertEquals(0, helpLines.size());
  }

  @Test
  public void testAppendUsage() {
    StringBuffer buffer = new StringBuffer();
    Option option = new OptionImpl();

    // Test case: Empty usage
    option.appendUsage(buffer, null, null);
    assertEquals("", buffer.toString());
  }

  @Test
  public void testGetPreferredName() {
    Option option = new OptionImpl();

    // Test case: Empty preferred name
    assertEquals("", option.getPreferredName());

    // Test case: Non-empty preferred name
    option.setPreferredName("a");
    assertEquals("a", option.getPreferredName());
  }

  @Test
  public void testGetDescription() {
    Option option = new OptionImpl();

    // Test case: Empty description
    assertEquals("", option.getDescription());

    // Test case: Non-empty description
    option.setDescription("description");
    assertEquals("description", option.getDescription());
  }

  @Test
  public void testGetId() {
    Option option = new OptionImpl();

    // Test case: Default id
    assertEquals(0, option.getId());

    // Test case: Custom id
    option.setId(1);
    assertEquals(1, option.getId());
  }

  @Test
  public void testFindOption() {
    Option option = new OptionImpl();

    // Test case: Option not found
    Option foundOption1 = option.findOption("a");
    assertNull(foundOption1);

    // Test case: Option found
    Option foundOption2 = option.findOption("b");
    assertNotNull(foundOption2);
  }

  @Test
  public void testIsRequired() {
    Option option = new OptionImpl();

    // Test case 1: Option is not required
    assertEquals(false, option.isRequired());

    // Test case 2: Option is required
    option.setRequired(true);
    assertEquals(true, option.isRequired());
  }
}