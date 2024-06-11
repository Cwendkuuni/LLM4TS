import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OptionsTest {
  
  private Options options;
  private Option option;
  private OptionGroup optionGroup;
  
  @Before
  public void setUp() {
    options = new Options();
    option = new Option("o", "option", true, "Option description");
    optionGroup = new OptionGroup();
  }
  
  @Test
  public void testAddOptionGroup() {
    optionGroup.addOption(option);
    options.addOptionGroup(optionGroup);
    assertEquals(1, options.getOptionGroups().size());
  }
  
  @Test
  public void testAddOption_shortName() {
    options.addOption("o", true, "Option description");
    assertTrue(options.hasOption("o"));
    assertFalse(options.hasLongOption("option"));
  }
  
  @Test
  public void testAddOption_shortAndLongName() {
    options.addOption("o", "option", true, "Option description");
    assertTrue(options.hasOption("o"));
  }
  
  @Test
  public void testAddOption_longName() {
    options.addOption(option);
    assertFalse(options.hasShortOption("o"));
    assertTrue(options.hasLongOption("option"));
  }
  
  @Test
  public void testAddOption_groupWithRequiredOption() {
    option.setRequired(true);
    OptionGroup group = new OptionGroup();
    group.addOption(option);
    options.addOptionGroup(group);
    assertEquals(1, options.getRequiredOptions().size());
  }
  
  @Test
  public void testGetOption_validOption() {
    options.addOption(option);
    assertEquals(option, options.getOption("o"));
    assertEquals(option, options.getOption("option"));
  }
  
  @Test
  public void testGetOption_invalidOption() {
    options.addOption(option);
    assertNull(options.getOption("invalid"));
  }
  
  @Test
  public void testGetMatchingOptions() {
    options.addOption("op", "option", true, "Option description");
    options.addOption("opt", "option2", true, "Option description");
    options.addOption("opt2", "option3", true, "Option description");
    assertEquals(2, options.getMatchingOptions("opt").size());
  }
  
  @Test
  public void testHasOption_validOption() {
    options.addOption(option);
    assertTrue(options.hasOption("o"));
    assertTrue(options.hasOption("option"));
  }
  
  @Test
  public void testHasOption_invalidOption() {
    options.addOption(option);
    assertFalse(options.hasOption("invalid"));
  }
  
  @Test
  public void testHasLongOption_validOption() {
    options.addOption(option);
    assertFalse(options.hasLongOption("o"));
    assertTrue(options.hasLongOption("option"));
  }
  
  @Test
  public void testHasLongOption_invalidOption() {
    options.addOption(option);
    assertFalse(options.hasLongOption("invalid"));
  }
  
  @Test
  public void testHasShortOption_validOption() {
    options.addOption(option);
    assertTrue(options.hasShortOption("o"));
    assertFalse(options.hasShortOption("option"));
  }
  
  @Test
  public void testHasShortOption_invalidOption() {
    options.addOption(option);
    assertFalse(options.hasShortOption("invalid"));
  }
  
  @Test
  public void testGetOptionGroup_validOption() {
    optionGroup.addOption(option);
    options.addOptionGroup(optionGroup);
    assertEquals(optionGroup, options.getOptionGroup(option));
  }
  
  @Test
  public void testGetOptionGroup_invalidOption() {
    optionGroup.addOption(option);
    options.addOptionGroup(optionGroup);
    assertNull(options.getOptionGroup(new Option("x", "invalid", true, "Invalid option")));
  }
  
  @Test
  public void testToString() {
    options.addOption(option);
    assertEquals("[ Options: [ short {o: option: true [Option description]} ] [ long {}]", options.toString());
  }
}