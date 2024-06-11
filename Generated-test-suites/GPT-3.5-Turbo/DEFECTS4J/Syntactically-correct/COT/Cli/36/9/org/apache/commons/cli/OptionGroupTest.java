import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {
  private OptionGroup optionGroup;

  @Before
  public void setUp() {
    optionGroup = new OptionGroup();
  }

  @Test
  public void testAddOption() {
    Option option = new Option("test", "test option");
    OptionGroup result = optionGroup.addOption(option);
    assertTrue(result.getOptions().contains(option));
  }

  @Test
  public void testGetNames() {
    Option option1 = new Option("test1", "test option 1");
    Option option2 = new Option("test2", "test option 2");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    Collection<String> names = optionGroup.getNames();
    assertTrue(names.contains("test1"));
    assertTrue(names.contains("test2"));
    assertEquals(2, names.size());
  }

  @Test
  public void testGetOptions() {
    Option option1 = new Option("test1", "test option 1");
    Option option2 = new Option("test2", "test option 2");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    Collection<Option> options = optionGroup.getOptions();
    assertTrue(options.contains(option1));
    assertTrue(options.contains(option2));
    assertEquals(2, options.size());
  }

  @Test
  public void testSetSelected() {
    Option option1 = new Option("test1", "test option 1");
    Option option2 = new Option("test2", "test option 2");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);
    assertEquals("test1", optionGroup.getSelected());

    optionGroup.setSelected(option2);
    assertEquals("test2", optionGroup.getSelected());
  }

  @Test(expected = AlreadySelectedException.class)
  public void testSetSelected_Exception() {
    Option option1 = new Option("test1", "test option 1");
    Option option2 = new Option("test2", "test option 2");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);
    optionGroup.setSelected(option2);
  }

  @Test
  public void testGetSelected() {
    assertNull(optionGroup.getSelected());

    Option option = new Option("test", "test option");
    optionGroup.addOption(option);
    optionGroup.setSelected(option);

    assertEquals("test", optionGroup.getSelected());
  }

  @Test
  public void testSetRequired() {
    optionGroup.setRequired(true);
    assertTrue(optionGroup.isRequired());

    optionGroup.setRequired(false);
    assertFalse(optionGroup.isRequired());
  }

  @Test
  public void testIsRequired() {
    assertFalse(optionGroup.isRequired());

    optionGroup.setRequired(true);
    assertTrue(optionGroup.isRequired());
  }

  @Test
  public void testToString() {
    Option option1 = new Option("t1", "test option 1");
    Option option2 = new Option("t2", "test option 2");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    String expected = "[--t1 test option 1, --t2 test option 2]";
    assertEquals(expected, optionGroup.toString());
  }
}