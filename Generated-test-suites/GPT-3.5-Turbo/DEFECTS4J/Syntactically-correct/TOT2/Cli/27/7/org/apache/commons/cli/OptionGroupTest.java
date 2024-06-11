import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;

public class OptionGroupTest {

  private OptionGroup optionGroup;

  @Before
  public void setUp() {
    optionGroup = new OptionGroup();
  }

  @Test
  public void testAddOption() {
    Option option = new Option("a", "Add an option");
    OptionGroup result = optionGroup.addOption(option);
    assertTrue(result.getOptions().contains(option));
  }

  @Test
  public void testGetNames() {
    Option option1 = new Option("a", "Option 1");
    Option option2 = new Option("b", "Option 2");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    Collection result = optionGroup.getNames();
    assertEquals(2, result.size());
    assertTrue(result.contains("a"));
    assertTrue(result.contains("b"));
  }

  @Test
  public void testGetOptions() {
    Option option1 = new Option("x", "Option X");
    Option option2 = new Option("y", "Option Y");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    Collection result = optionGroup.getOptions();
    assertEquals(2, result.size());
    assertTrue(result.contains(option1));
    assertTrue(result.contains(option2));
  }

  @Test
  public void testSetSelected() {
    Option option1 = new Option("x", "Option X");
    Option option2 = new Option("y", "Option Y");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);
    assertEquals("x", optionGroup.getSelected());

    try {
      optionGroup.setSelected(option2);
    } catch (AlreadySelectedException e) {
      fail("Exception should not be thrown");
    }

    try {
      optionGroup.setSelected(option1);
      fail("Exception not thrown");
    } catch (AlreadySelectedException e) {
      // This is expected
    }
  }

  @Test
  public void testIsRequired() {
    optionGroup.setRequired(true);
    assertTrue(optionGroup.isRequired());
  }

  @Test
  public void testToString() {
    Option option1 = new Option("x", "Option X");
    Option option2 = new Option("y", "Option Y");
    option1.setLongOpt("x-long");
    option2.setLongOpt("y-long");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    String expected = "[-x Option X, --y-long Option Y]";
    assertEquals(expected, optionGroup.toString());
  }
}