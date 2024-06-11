import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import org.apache.commons.cli.Option;

public class OptionGroupTest {
  
  private OptionGroup optionGroup;
  
  @Before
  public void setUp() {
    optionGroup = new OptionGroup();
  }
  
  @Test
  public void testAddOption() {
    Option option = new Option("a", "option A");
    OptionGroup result = optionGroup.addOption(option);
    assertNotNull(result);
    assertTrue(optionGroup.getOptions().contains(option));
  }
  
  @Test
  public void testGetNames() {
    Option optionA = new Option("a", "option A");
    Option optionB = new Option("b", "option B");
    optionGroup.addOption(optionA);
    optionGroup.addOption(optionB);
    Collection<String> names = optionGroup.getNames();
    assertNotNull(names);
    assertTrue(names.contains("a"));
    assertTrue(names.contains("b"));
  }
  
  @Test
  public void testGetOptions() {
    Option optionA = new Option("a", "option A");
    Option optionB = new Option("b", "option B");
    optionGroup.addOption(optionA);
    optionGroup.addOption(optionB);
    Collection<Option> options = optionGroup.getOptions();
    assertNotNull(options);
    assertTrue(options.contains(optionA));
    assertTrue(options.contains(optionB));
  }
  
  @Test
  public void testSetSelected() {
    Option optionA = new Option("a", "option A");
    Option optionB = new Option("b", "option B");
    optionGroup.addOption(optionA);
    optionGroup.addOption(optionB);
    
    try {
      optionGroup.setSelected(optionA);
      assertEquals("a", optionGroup.getSelected());
    } catch (AlreadySelectedException e) {
      fail();
    }
    
    try {
      optionGroup.setSelected(optionB);
      assertEquals("b", optionGroup.getSelected());
    } catch (AlreadySelectedException e) {
      fail();
    }
    
    try {
      optionGroup.setSelected(optionA);
      fail();
    } catch (AlreadySelectedException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void testGetSelected() {
    Option optionA = new Option("a", "option A");
    optionGroup.addOption(optionA);
    
    try {
      optionGroup.setSelected(optionA);
      assertEquals("a", optionGroup.getSelected());
    } catch (AlreadySelectedException e) {
      fail();
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
    Option optionA = new Option("a", "option A");
    Option optionB = new Option("b", "option B");
    optionGroup.addOption(optionA);
    optionGroup.addOption(optionB);
    assertEquals("[ -a option A, -b option B]", optionGroup.toString());
  }
}