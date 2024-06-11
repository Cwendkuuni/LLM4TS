import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

  OptionGroup optionGroup;

  @Before
  public void setUp() {
    optionGroup = new OptionGroup();
  }

  // Test addOption method
  @Test
  public void testAddOption() {
    Option option1 = new Option("a", "apple", false, "Test Option 1");
    Option option2 = new Option("b", "banana", true, "Test Option 2");

    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    assertTrue(optionGroup.getOptions().contains(option1));
    assertTrue(optionGroup.getOptions().contains(option2));
  }

  // Test getNames method
  @Test
  public void testGetNames() {
    Option option1 = new Option("a", "apple", false, "Test Option 1");
    Option option2 = new Option("b", "banana", true, "Test Option 2");

    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    assertEquals(2, optionGroup.getNames().size());
    assertTrue(optionGroup.getNames().contains("a"));
    assertTrue(optionGroup.getNames().contains("b"));
  }

  // Test setSelected method with valid option
  @Test
  public void testSetSelectedValidOption() throws AlreadySelectedException {
    Option option1 = new Option("a", "apple", false, "Test Option 1");
    Option option2 = new Option("b", "banana", true, "Test Option 2");

    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);

    assertEquals("a", optionGroup.getSelected());
  }

  // Test setSelected method with already selected option
  @Test(expected = AlreadySelectedException.class)
  public void testSetSelectedAlreadySelectedOption() throws AlreadySelectedException {
    Option option1 = new Option("a", "apple", false, "Test Option 1");
    Option option2 = new Option("b", "banana", true, "Test Option 2");

    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);
    optionGroup.setSelected(option2);
  }

  // Test setSelected method with null option
  @Test
  public void testSetSelectedNullOption() throws AlreadySelectedException {
    Option option1 = new Option("a", "apple", false, "Test Option 1");

    optionGroup.addOption(option1);

    optionGroup.setSelected(null);

    assertNull(optionGroup.getSelected());
  }

  // Test isRequired method
  @Test
  public void testIsRequired() {
    optionGroup.setRequired(true);
    assertTrue(optionGroup.isRequired());

    optionGroup.setRequired(false);
    assertFalse(optionGroup.isRequired());
  }

  // Test toString method
  @Test
  public void testToString() {
    Option option1 = new Option("a", "apple", false, "Test Option 1");
    Option option2 = new Option("b", "banana", true, "Test Option 2");

    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    String expected = "[ -a Test Option 1, -b Test Option 2 ]";
    assertEquals(expected, optionGroup.toString());
  }
}