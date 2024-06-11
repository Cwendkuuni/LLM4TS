import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

  private OptionGroup optionGroup;

  @Before
  public void setUp() {
    optionGroup = new OptionGroup();
  }

  @After
  public void tearDown() {
    optionGroup = null;
  }

  // Valid test case for addOption()
  @Test
  public void testAddOptionValid() {
    Option option = new Option("a", "add", false, "Add option");
    OptionGroup result = optionGroup.addOption(option);

    assertNotNull(result);
    assertTrue(optionGroup.getOptions().contains(option));
  }

  // Invalid test case for addOption()
  @Test
  public void testAddOptionInvalid() {
    Option option = null;
    OptionGroup result = optionGroup.addOption(option);

    assertNull(result);
  }

  // Valid test case for getNames() with multiple options
  @Test
  public void testGetNamesValid() {
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    Collection<String> result = optionGroup.getNames();

    assertNotNull(result);
    assertTrue(result.contains("a"));
    assertTrue(result.contains("r"));
  }

  // Valid test case for getNames() with no options
  @Test
  public void testGetNamesEmpty() {
    Collection<String> result = optionGroup.getNames();

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  // Valid test case for getOptions() with multiple options
  @Test
  public void testGetOptionsValid() {
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    Collection<Option> result = optionGroup.getOptions();

    assertNotNull(result);
    assertTrue(result.contains(option1));
    assertTrue(result.contains(option2));
  }

  // Valid test case for getOptions() with no options
  @Test
  public void testGetOptionsEmpty() {
    Collection<Option> result = optionGroup.getOptions();

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  // Valid test case for setSelected() when option is null
  @Test
  public void testSetSelectedNull() {
    Option option1 = new Option("a", "add", false, "Add option");
    optionGroup.addOption(option1);

    optionGroup.setSelected(null);
    String result = optionGroup.getSelected();

    assertNull(result);
  }

  // Valid test case for setSelected() when option is selected for the first time
  @Test
  public void testSetSelectedFirstTime() {
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);
    String result = optionGroup.getSelected();

    assertEquals("a", result);
  }

  // Invalid test case for setSelected() when option is already selected
  @Test(expected = AlreadySelectedException.class)
  public void testSetSelectedAlreadySelected() {
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);
    optionGroup.setSelected(option2);
  }

  // Valid test case for getSelected() when option is null
  @Test
  public void testGetSelectedNull() {
    String result = optionGroup.getSelected();

    assertNull(result);
  }

  // Valid test case for getSelected() when option is selected
  @Test
  public void testGetSelectedValid() {
    Option option1 = new Option("a", "add", false, "Add option");
    optionGroup.addOption(option1);

    optionGroup.setSelected(option1);
    String result = optionGroup.getSelected();

    assertEquals("a", result);
  }

  // Valid test case for setRequired() when required is true
  @Test
  public void testSetRequiredTrue() {
    optionGroup.setRequired(true);

    assertTrue(optionGroup.isRequired());
  }

  // Valid test case for setRequired() when required is false
  @Test
  public void testSetRequiredFalse() {
    optionGroup.setRequired(false);

    assertFalse(optionGroup.isRequired());
  }

  // Valid test case for isRequired() when required is true
  @Test
  public void testIsRequiredTrue() {
    optionGroup.setRequired(true);

    assertTrue(optionGroup.isRequired());
  }

  // Valid test case for isRequired() when required is false
  @Test
  public void testIsRequiredFalse() {
    optionGroup.setRequired(false);

    assertFalse(optionGroup.isRequired());
  }

  // Valid test case for toString() with multiple options
  @Test
  public void testToStringValid() {
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    String result = optionGroup.toString();

    assertEquals("[ -a Add option, -r Remove option ]", result);
  }

  // Valid test case for toString() with no options
  @Test
  public void testToStringEmpty() {
    String result = optionGroup.toString();

    assertEquals("[]", result);
  }

}