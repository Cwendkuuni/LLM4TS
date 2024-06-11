import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

  @Test
  public void testAddOption() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "option 1");
    Option option2 = new Option("b", "option 2");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    assertEquals(2, optionGroup.getOptions().size());
  }

  @Test
  public void testGetNames() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "option 1");
    Option option2 = new Option("b", "option 2");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    assertEquals(2, optionGroup.getNames().size());
    assertTrue(optionGroup.getNames().contains("a"));
    assertTrue(optionGroup.getNames().contains("b"));
  }

  @Test
  public void testGetOptions() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "option 1");
    Option option2 = new Option("b", "option 2");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    assertEquals(2, optionGroup.getOptions().size());
    assertTrue(optionGroup.getOptions().contains(option1));
    assertTrue(optionGroup.getOptions().contains(option2));
  }

  @Test
  public void testSetSelected() throws AlreadySelectedException {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "option 1");
    Option option2 = new Option("b", "option 2");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);
    assertEquals("a", optionGroup.getSelected());
  }

  @Test(expected = AlreadySelectedException.class)
  public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "option 1");
    Option option2 = new Option("b", "option 2");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);
    optionGroup.setSelected(option2); // should throw AlreadySelectedException
  }

  @Test
  public void testGetSelected() throws AlreadySelectedException {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "option 1");
    Option option2 = new Option("b", "option 2");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);
    assertEquals("a", optionGroup.getSelected());
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
    Option option1 = new Option("a", "option 1");
    Option option2 = new Option("b", "option 2");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    String expected = "[--a option 1, --b option 2]";
    assertEquals(expected, optionGroup.toString());
  }
}