import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
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
  public void testAddOptionGroup() {
    OptionGroup group = new OptionGroup();
    options.addOptionGroup(group);
    assertTrue(options.getOptionGroups().contains(group));
  }

  @Test
  public void testAddOption() {
    Option option = new Option("o", "option", true, "description");
    options.addOption(option);
    assertTrue(options.getOptions().contains(option));
    assertTrue(options.hasOption("o"));
    assertTrue(options.hasLongOption("option"));
    assertTrue(options.hasShortOption("o"));
  }

  @Test
  public void testGetOption() {
    Option option = new Option("o", "option", true, "description");
    options.addOption(option);
    assertEquals(option, options.getOption("o"));
  }

  @Test
  public void testGetMatchingOptions() {
    Option option1 = new Option("o", "option1", true, "description1");
    Option option2 = new Option("a", "option2", true, "description2");
    Option option3 = new Option("b", "option3", true, "description3");
    options.addOption(option1);
    options.addOption(option2);
    options.addOption(option3);
    assertEquals(2, options.getMatchingOptions("o").size());
    assertEquals(1, options.getMatchingOptions("a").size());
    assertEquals(1, options.getMatchingOptions("b").size());
  }

  @Test
  public void testHasOption() {
    Option option = new Option("o", "option", true, "description");
    options.addOption(option);
    assertTrue(options.hasOption("o"));
    assertTrue(options.hasLongOption("option"));
    assertTrue(options.hasShortOption("o"));
  }

  @Test
  public void testGetOptionGroup() {
    Option option = new Option("o", "option", true, "description");
    OptionGroup group = new OptionGroup();
    group.addOption(option);
    options.addOptionGroup(group);
    assertEquals(group, options.getOptionGroup(option));
  }

  @Test
  public void testToString() {
    Option option = new Option("o", "option", true, "description");
    options.addOption(option);
    assertEquals("[ Options: [ short {o=Option - o}, long {option=Option - o} ]", options.toString());
  }
}