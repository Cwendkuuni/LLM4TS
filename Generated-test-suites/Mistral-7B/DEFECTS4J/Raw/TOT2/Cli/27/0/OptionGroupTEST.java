import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {
  @Test
  public void testAddOption() {
    OptionGroup optionGroup = new OptionGroup();
    Option option = new Option("test", "test", false, "Test option");

    optionGroup.addOption(option);

    // Check if the option was added to the group
    Collection<Option> options = optionGroup.getOptions();
    assertTrue(options.contains(option));
  }

  @Test
  public void testAddOptionDuplicate() {
    OptionGroup optionGroup = new OptionGroup();
    Option option = new Option("test", "test", false, "Test option");

    optionGroup.addOption(option);
    optionGroup.addOption(option);

    // Check that the option was only added once
    Collection<Option> options = optionGroup.getOptions();
    assertEquals(1, options.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddNullOption() {
    OptionGroup optionGroup = new OptionGroup();
    Option option = null;

    optionGroup.addOption(option);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddOptionWithNullName() {
    OptionGroup optionGroup = new OptionGroup();
    Option option = new Option(null, "test", false, "Test option");

    optionGroup.addOption(option);
  }

  @Test(expected = AlreadySelectedException.class)
  public void testSetSelectedDuplicate() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("test1", "test1", false, "Test option 1");
    Option option2 = new Option("test2", "test2", false, "Test option 2");

    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);
    optionGroup.setSelected(option2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetNullSelectedOption() {
    OptionGroup optionGroup = new OptionGroup();
    Option option = new Option("test", "test", false, "Test option");

    optionGroup.setSelected(null);
  }

  @Test
  public void testSetSelectedNotInGroup() {
    OptionGroup optionGroup = new OptionGroup();
    Option option = new Option("test", "test", false, "Test option");
    Option optionInGroup = new Option("group", "group", false, "Group option");

    optionGroup.addOption(optionInGroup);

    try {
      optionGroup.setSelected(option);
    } catch (AlreadySelectedException e) {
      // This should not be thrown since the option is not in the group
      fail("Unexpected AlreadySelectedException");
    }
  }

  @Test
  public void testGetNames() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("test1", "test1", false, "Test option 1");
    Option option2 = new Option("test2", "test2", false, "Test option 2");

    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    Collection<String> names = optionGroup.getNames();
    assertEquals(2, names.size());
    assertTrue(names.contains("test1"));
    assertTrue(names.contains("test2"));
  }

  @Test
  public void testGetOptions() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("test1", "test1", false, "Test option 1");
    Option option2 = new Option("test2", "test2", false, "Test option 2");

    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    Collection<Option> options = optionGroup.getOptions();
    assertEquals(2, options.size());
    assertTrue(options.contains(option1));
    assertTrue(options.contains(option2));
  }

  @Test
  public void testSetSelected() throws AlreadySelectedException {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("test1", "test1", false, "Test option 1");
    Option option2 = new Option("test2", "test2", false, "Test option 2");

    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);

    assertEquals("test1", optionGroup.getSelected());
  }

  @Test(expected = AlreadySelectedException.class)
  public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("test1", "test1", false, "Test option 1");
    Option option2 = new Option("test2", "test2", false, "Test option 2");

    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);
    optionGroup.setSelected(option2);
  }

  @Test
  public void testIsRequired() {
    OptionGroup optionGroup = new OptionGroup();
    Option option = new Option("test", "test", false, "Test option");

    optionGroup.addOption(option);

    assertFalse(optionGroup.isRequired());
  }

  @Test
  public void testSetRequired() {
    OptionGroup optionGroup = new OptionGroup();

    optionGroup.setRequired(true);

    assertTrue(optionGroup.isRequired());
  }

  @Test
  public void testToString() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("test1", "test1", false, "Test option 1");
    Option option2 = new Option("test2", "test2", false, "Test option 2");

    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    String expected = "[-test1 Test option 1, -test2 Test option 2]";
    assertEquals(expected, optionGroup.toString());
  }
}