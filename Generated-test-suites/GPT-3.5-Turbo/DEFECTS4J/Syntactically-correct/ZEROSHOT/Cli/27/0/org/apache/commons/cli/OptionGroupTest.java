import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {
  
  private Option option1;
  private Option option2;
  private Option option3;
  private OptionGroup group;

  @Before
  public void setUp() {
    option1 = new Option("a", "option1", false, "Option 1");
    option2 = new Option("b", "option2", false, "Option 2");
    option3 = new Option("c", "option3", false, "Option 3");
    group = new OptionGroup();
  }

  @Test
  public void testAddOption() {
    group.addOption(option1);
    assertTrue(group.getOptions().contains(option1));
  }

  @Test
  public void testGetNames() {
    group.addOption(option1);
    group.addOption(option2);
    group.addOption(option3);
    assertTrue(group.getNames().contains("a"));
    assertTrue(group.getNames().contains("b"));
    assertTrue(group.getNames().contains("c"));
  }

  @Test
  public void testGetOptions() {
    group.addOption(option1);
    group.addOption(option2);
    group.addOption(option3);
    assertTrue(group.getOptions().contains(option1));
    assertTrue(group.getOptions().contains(option2));
    assertTrue(group.getOptions().contains(option3));
  }

  @Test
  public void testSetSelected() {
    group.addOption(option1);
    group.addOption(option2);
    group.addOption(option3);
    try {
      group.setSelected(option1);
      assertEquals("a", group.getSelected());
      group.setSelected(null);
      assertNull(group.getSelected());
    } catch (AlreadySelectedException e) {
      fail("Expected no AlreadySelectedException");
    }
  }
  
  @Test
  public void testSetSelectedAlreadySelected() {
    group.addOption(option1);
    group.addOption(option2);
    group.addOption(option3);
    try {
      group.setSelected(option1);
      group.setSelected(option2);
      fail("Expected AlreadySelectedException");
    } catch (AlreadySelectedException e) {
      // Expected exception
    }
  }

  @Test
  public void testGetSelected() {
    assertNull(group.getSelected());
    group.addOption(option1);
    group.setSelected(option1);
    assertEquals("a", group.getSelected());
  }

  @Test
  public void testSetRequired() {
    assertFalse(group.isRequired());
    group.setRequired(true);
    assertTrue(group.isRequired());
  }
  
  @Test
  public void testToString() {
    group.addOption(option1);
    group.addOption(option2);
    group.addOption(option3);
    assertEquals("[-a Option 1, -b Option 2, -c Option 3]", group.toString());
  }
}