import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

  private Options options;

  @Before
  public void setUp() {
    options = new Options();
  }

  @Test
  public void testAddOption() {
    Option option = new Option("a", "option-a", true, "Option A");
    options.addOption(option);
    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("option-a"));
  }

  @Test
  public void testAddOptionGroup() {
    Option option1 = new Option("a", "option-a", true, "Option A");
    Option option2 = new Option("b", "option-b", true, "Option B");
    OptionGroup group = new OptionGroup();
    group.addOption(option1);
    group.addOption(option2);
    options.addOptionGroup(group);
    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("option-a"));
    assertTrue(options.hasOption("b"));
    assertTrue(options.hasOption("option-b"));
  }

  @Test
  public void testGetOption() {
    Option option1 = new Option("a", "option-a", true, "Option A");
    options.addOption(option1);
    Option returnedOption = options.getOption("a");
    assertEquals(option1, returnedOption);
  }

  @Test
  public void testHasOption() {
    Option option1 = new Option("a", "option-a", true, "Option A");
    options.addOption(option1);
    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("option-a"));
  }

  @Test
  public void testHasLongOption() {
    Option option1 = new Option("a", "option-a", true, "Option A");
    options.addOption(option1);
    assertTrue(options.hasLongOption("option-a"));
  }

  @Test
  public void testHasShortOption() {
    Option option1 = new Option("a", "option-a", true, "Option A");
    options.addOption(option1);
    assertTrue(options.hasShortOption("a"));
  }

  @Test
  public void testGetMatchingOptions() {
    Option option1 = new Option("a", "option-a", true, "Option A");
    Option option2 = new Option("abc", "option-abc", true, "Option ABC");
    options.addOption(option1);
    options.addOption(option2);
    List<String> returnedOptions = options.getMatchingOptions("a");
    assertEquals(2, returnedOptions.size());
    assertTrue(returnedOptions.contains("a"));
    assertTrue(returnedOptions.contains("option-a"));
  }

}