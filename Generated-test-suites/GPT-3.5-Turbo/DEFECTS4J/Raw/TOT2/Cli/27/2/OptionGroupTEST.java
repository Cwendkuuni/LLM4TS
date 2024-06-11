import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {

  @Test
  public void testAddOption() {
    OptionGroup optionGroup = new OptionGroup();
    Option option = new Option("t", "test", false, "Test option");
    OptionGroup updatedGroup = optionGroup.addOption(option);
    assertTrue(updatedGroup.getOptions().contains(option));
  }

  @Test
  public void testGetNames() {
    OptionGroup optionGroup = new OptionGroup();
    Option option = new Option("t", "test", false, "Test option");
    optionGroup.addOption(option);
    assertEquals(1, optionGroup.getNames().size());
    assertTrue(optionGroup.getNames().contains("t"));
  }

  @Test
  public void testSetSelected() throws AlreadySelectedException {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("t", "test1", false, "Test option 1");
    Option option2 = new Option("r", "test2", false, "Test option 2");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    optionGroup.setSelected(option1);
    assertEquals("t", optionGroup.getSelected());
    assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option2));
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
    Option option1 = new Option("t", "test1", false, "Test option 1");
    Option option2 = new Option("r", "test2", false, "Test option 2");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    String expected = "[--test1 Test option 1, --test2 Test option 2]";
    assertEquals(expected, optionGroup.toString());
  }

}