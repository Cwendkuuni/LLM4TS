import static org.junit.Assert.*;
import org.junit.Test;

public class OptionGroupTest {

  @Test
  public void testAddOption() {
    Option option = new Option("a", "add", false, "add option");
    OptionGroup group = new OptionGroup();
    group.addOption(option);
    Collection<Option> options = group.getOptions();
    assertTrue(options.contains(option));
  }

  @Test
  public void testGetNames() {
    Option option1 = new Option("a", "add", false, "add option");
    Option option2 = new Option("d", "delete", false, "delete option");
    OptionGroup group = new OptionGroup();
    group.addOption(option1);
    group.addOption(option2);
    Collection<String> names = group.getNames();
    assertTrue(names.contains("a"));
    assertTrue(names.contains("d"));
  }

  @Test
  public void testGetOptions() {
    Option option1 = new Option("a", "add", false, "add option");
    Option option2 = new Option("d", "delete", false, "delete option");
    OptionGroup group = new OptionGroup();
    group.addOption(option1);
    group.addOption(option2);
    Collection<Option> options = group.getOptions();
    assertTrue(options.contains(option1));
    assertTrue(options.contains(option2));
  }

  @Test
  public void testSetSelected() throws AlreadySelectedException {
    Option option1 = new Option("a", "add", false, "add option");
    Option option2 = new Option("d", "delete", false, "delete option");
    OptionGroup group = new OptionGroup();
    group.addOption(option1);
    group.addOption(option2);
    group.setSelected(option1);
    assertEquals(option1.getOpt(), group.getSelected());
  }

  @Test(expected = AlreadySelectedException.class)
  public void testSetSelectedWithAlreadySelectedOption() throws AlreadySelectedException {
    Option option1 = new Option("a", "add", false, "add option");
    Option option2 = new Option("d", "delete", false, "delete option");
    OptionGroup group = new OptionGroup();
    group.addOption(option1);
    group.addOption(option2);
    group.setSelected(option1);
    group.setSelected(option2);
  }

  @Test
  public void testGetSelected() throws AlreadySelectedException {
    Option option1 = new Option("a", "add", false, "add option");
    Option option2 = new Option("d", "delete", false, "delete option");
    OptionGroup group = new OptionGroup();
    group.addOption(option1);
    group.addOption(option2);
    group.setSelected(option2);
    assertEquals(option2.getOpt(), group.getSelected());
  }

  @Test
  public void testSetRequired() {
    OptionGroup group = new OptionGroup();
    group.setRequired(true);
    assertTrue(group.isRequired());
  }

  @Test
  public void testToString() {
    Option option1 = new Option("a", "add", false, "add option");
    Option option2 = new Option("d", "delete", false, "delete option");
    OptionGroup group = new OptionGroup();
    group.addOption(option1);
    group.addOption(option2);
    String expected = "[-a add option, -d delete option]";
    assertEquals(expected, group.toString());
  }
}