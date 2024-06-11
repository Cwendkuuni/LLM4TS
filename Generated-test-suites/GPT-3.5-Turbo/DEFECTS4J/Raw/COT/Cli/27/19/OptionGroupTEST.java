import static org.junit.Assert.*;
import org.junit.Test;

public class OptionGroupTest {

  OptionGroup optionGroup;

  @Before
  public void setUp() {
    optionGroup = new OptionGroup();
  }

  @Test
  public void testAddOption() {
    Option option = new Option("a", "add", false, "Add option");
    optionGroup.addOption(option);
    
    Collection<Option> options = optionGroup.getOptions();
    assertTrue(options.contains(option));
  }

  @Test
  public void testGetNames() {
    Option option1 = new Option("a", "add", false, "Add option");
    optionGroup.addOption(option1);

    Option option2 = new Option("r", "remove", false, "Remove option");
    optionGroup.addOption(option2);
    
    Collection<String> names = optionGroup.getNames();
    assertTrue(names.contains("a"));
    assertTrue(names.contains("r"));
  }

  @Test
  public void testGetOptions() {
    Option option1 = new Option("a", "add", false, "Add option");
    optionGroup.addOption(option1);

    Option option2 = new Option("r", "remove", false, "Remove option");
    optionGroup.addOption(option2);
    
    Collection<Option> options = optionGroup.getOptions();
    assertTrue(options.contains(option1));
    assertTrue(options.contains(option2));
  }

  @Test
  public void testSetSelected() {
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");

    try {
      optionGroup.setSelected(option1);
      assertEquals(option1.getOpt(), optionGroup.getSelected());
    
      optionGroup.setSelected(option2);
      assertEquals(option2.getOpt(), optionGroup.getSelected());
    } catch (AlreadySelectedException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test(expected=AlreadySelectedException.class)
  public void testSetSelectedThrowsException() throws AlreadySelectedException {
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");

    optionGroup.setSelected(option1);
    optionGroup.setSelected(option2);
  }

  @Test
  public void testGetSelected() {
    Option option = new Option("a", "add", false, "Add option");

    try {
      optionGroup.setSelected(option);
      assertEquals(option.getOpt(), optionGroup.getSelected());

      optionGroup.setSelected(null);
      assertNull(optionGroup.getSelected());
    } catch (AlreadySelectedException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testSetRequired() {
    optionGroup.setRequired(true);
    assertTrue(optionGroup.isRequired());

    optionGroup.setRequired(false);
    assertFalse(optionGroup.isRequired());
  }

  @Test
  public void testToString() {
    Option option1 = new Option("a", "add", false, "Add option");
    optionGroup.addOption(option1);

    Option option2 = new Option("r", "remove", false, "Remove option");
    optionGroup.addOption(option2);

    String expected = "[ -a Add option, -r Remove option ]";
    String result = optionGroup.toString();

    assertEquals(expected, result);
  }
}