import static org.junit.Assert.*;
import org.junit.Test;

public class OptionsTest {

  @Test
  public void testAddOptionGroup() {
    OptionGroup group = new OptionGroup();
    group.addOption(new Option("g", "group", false, "Group option"));
    Options options = new Options();
    options.addOptionGroup(group);
    assertTrue(options.hasOption("g"));
  }

  @Test
  public void testAddOption() {
    Options options = new Options();
    options.addOption(new Option("a", false, "Option a"));
    options.addOption(new Option("b", "option-b", true, "Option b"));
    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("option-b"));
  }

  @Test
  public void testGetOptions() {
    Options options = new Options();
    options.addOption(new Option("a", false, "Option a"));
    options.addOption(new Option("b", "option-b", true, "Option b"));
    assertEquals(2, options.getOptions().size());
  }

  @Test
  public void testGetRequiredOptions() {
    Options options = new Options();
    Option option = new Option("a", false, "Option a");
    option.setRequired(true);
    options.addOption(option);
    assertEquals(1, options.getRequiredOptions().size());
  }

  @Test
  public void testGetOption() {
    Options options = new Options();
    options.addOption(new Option("a", false, "Option a"));
    options.addOption(new Option("b", "option-b", true, "Option b"));
    assertEquals("Option a", options.getOption("a").getDescription());
    assertEquals("Option b", options.getOption("option-b").getDescription());
  }

  @Test
  public void testGetMatchingOptions() {
    Options options = new Options();
    options.addOption(new Option("test", false, "Test option"));
    options.addOption(new Option("type", false, "Type option"));
    options.addOption(new Option("tag", false, "Tag option"));
    assertEquals(2, options.getMatchingOptions("t").size());
  }

  @Test
  public void testHasOption() {
    Options options = new Options();
    options.addOption(new Option("a", false, "Option a"));
    options.addOption(new Option("b", "option-b", true, "Option b"));
    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("option-b"));
  }

  @Test
  public void testHasLongOption() {
    Options options = new Options();
    options.addOption(new Option("a", false, "Option a"));
    options.addOption(new Option("b", "option-b", true, "Option b"));
    assertTrue(options.hasLongOption("option-b"));
  }

  @Test
  public void testHasShortOption() {
    Options options = new Options();
    options.addOption(new Option("a", false, "Option a"));
    options.addOption(new Option("b", "option-b", true, "Option b"));
    assertTrue(options.hasShortOption("a"));
  }

  @Test
  public void testGetOptionGroup() {
    OptionGroup group = new OptionGroup();
    group.addOption(new Option("g", "group", false, "Group option"));
    Options options = new Options();
    options.addOptionGroup(group);
    assertNotNull(options.getOptionGroup(options.getOption("g")));
  }

  @Test
  public void testToString() {
    Options options = new Options();
    options.addOption(new Option("a", false, "Option a"));
    options.addOption(new Option("b", "option-b", true, "Option b"));
    assertNotNull(options.toString());
  }

}