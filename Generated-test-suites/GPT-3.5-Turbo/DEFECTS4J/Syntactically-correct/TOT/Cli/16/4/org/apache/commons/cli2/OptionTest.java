import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

  private Option option;

  @Before
  public void setUp() {
    option = new OptionImpl();
  }

  @Test
  public void testProcess_typicalUseCase() throws OptionException {
    WriteableCommandLine commandLine = new CommandLineImpl();
    ListIterator args = List.of("--option", "value").listIterator();

    option.process(commandLine, args);

    assertTrue(commandLine.hasOption("option"));
    assertEquals("value", commandLine.getOptionValue("option"));
  }

  @Test(expected = OptionException.class)
  public void testProcess_errorScenario() throws OptionException {
    WriteableCommandLine commandLine = new CommandLineImpl();
    ListIterator args = List.of("--option").listIterator();

    option.process(commandLine, args);
  }

  @Test
  public void testDefaults_typicalUseCase() {
    WriteableCommandLine commandLine = new CommandLineImpl();
    commandLine.addOption("option");

    option.defaults(commandLine);

    assertEquals("defaultValue", commandLine.getOptionValue("option"));
  }

  @Test
  public void testCanProcess_typicalUseCase() {
    WriteableCommandLine commandLine = new CommandLineImpl();
    String argument = "--option";

    boolean result = option.canProcess(commandLine, argument);
    
    assertTrue(result);
  }

  @Test
  public void testCanProcess_edgeCase() {
    WriteableCommandLine commandLine = new CommandLineImpl();
    String argument = null;

    boolean result = option.canProcess(commandLine, argument);
    
    assertFalse(result);
  }

  @Test
  public void testGetTriggers_typicalUseCase() {
    Set triggers = option.getTriggers();

    assertTrue(triggers.contains("--option"));
  }

  @Test
  public void testGetPrefixes_typicalUseCase() {
    Set prefixes = option.getPrefixes();

    assertTrue(prefixes.contains("--"));
  }

  @Test
  public void testValidate_typicalUseCase() throws OptionException {
    WriteableCommandLine commandLine = new CommandLineImpl();
    commandLine.addOption("option");

    option.validate(commandLine);
  }

  @Test(expected = OptionException.class)
  public void testValidate_errorScenario() throws OptionException {
    WriteableCommandLine commandLine = new CommandLineImpl();

    option.validate(commandLine);
  }

  @Test
  public void testHelpLines_typicalUseCase() {
    int depth = 0;
    Set helpSettings = Set.of();
    Comparator comp = Comparator.naturalOrder();

    List helpLines = option.helpLines(depth, helpSettings, comp);

    assertNotNull(helpLines);
    assertEquals(1, helpLines.size());
  }

  @Test
  public void testAppendUsage_typicalUseCase() {
    StringBuffer buffer = new StringBuffer();

    option.appendUsage(buffer, Set.of(), Comparator.naturalOrder());

    assertEquals("--option", buffer.toString());
  }

  @Test
  public void testGetPreferredName_typicalUseCase() {
    String preferredName = option.getPreferredName();

    assertEquals("option", preferredName);
  }

  @Test
  public void testGetDescription_typicalUseCase() {
    String description = option.getDescription();

    assertEquals("This is an option", description);
  }

  @Test
  public void testGetId_typicalUseCase() {
    int id = option.getId();

    assertEquals(1, id);
  }

  @Test
  public void testFindOption_typicalUseCase() {
    Option foundOption = option.findOption("--option");

    assertNotNull(foundOption);
    assertEquals("option", foundOption.getPreferredName());
  }

  @Test
  public void testIsRequired_typicalUseCase() {
    boolean isRequired = option.isRequired();

    assertFalse(isRequired);
  }

  @Test
  public void testGetParent_typicalUseCase() {
    Option parent = option.getParent();

    assertNull(parent);
  }

  @Test
  public void testSetParent_typicalUseCase() {
    Option parent = new OptionImpl();

    option.setParent(parent);

    assertEquals(parent, option.getParent());
  }
}