import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class OptionsTest {

  @Test
  public void testAddOptionGroup() {
    OptionGroup group1 = new OptionGroup();
    Option option1 = new Option("a", "aa", false, "option 1");
    Option option2 = new Option("b", "bb", true, "option 2");
    group1.addOption(option1);
    group1.addOption(option2);
    Options options = new Options();
    options.addOptionGroup(group1);
    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("b"));
    assertEquals(group1, options.getOptionGroup(option1));
  }

  @Test
  public void testAddOption() {
    Options options = new Options();
    options.addOption("a", "option a");
    assertTrue(options.hasOption("a"));

    options.addOption("b", true, "option b");
    assertTrue(options.hasOption("b"));
    assertTrue(options.getOption("b").hasArg());

    options.addOption("c", "cc", true, "option c");
    assertTrue(options.hasOption("c"));
    assertTrue(options.getOption("c").hasArg());

    Option optionD = new Option("d", "dd", "option d");
    options.addOption(optionD);
    assertTrue(options.hasOption("d"));
    assertEquals(optionD, options.getOption("d"));
  }

  @Test
  public void testGetOptions() {
    Options options = new Options();
    options.addOption("a", "option a");
    options.addOption("b", "option b");
    options.addOption("c", "option c");

    List<Option> optionList = options.getOptions();
    assertEquals(3, optionList.size());
    assertTrue(optionList.contains(options.getOption("a")));
    assertTrue(optionList.contains(options.getOption("b")));
    assertTrue(optionList.contains(options.getOption("c")));
  }

  @Test
  public void testGetRequiredOptions() {
    Options options = new Options();
    options.addOption("a", true, "option a");
    options.addOption("b", "option b");

    List<Object> requiredOptions = options.getRequiredOptions();
    assertEquals(2, requiredOptions.size());
    assertTrue(requiredOptions.contains("a"));
    assertTrue(requiredOptions.contains("b"));
  }

  @Test
  public void testGetOption() {
    Options options = new Options();
    options.addOption("a", "option a");
    assertEquals(options.getOption("a"), options.getOption("-a"));

    options.addOption("b", true, "option b");
    assertEquals(options.getOption("b"), options.getOption("-b"));
  }

  @Test
  public void testGetMatchingOptions() {
    Options options = new Options();
    options.addOption("ab", "option ab");
    options.addOption("ac", "option ac");
    options.addOption("ad", true, "option ad");
    options.addOption("ae", true, "option ae");

    List<String> matchingOptions = options.getMatchingOptions("a");
    assertEquals(4, matchingOptions.size());
    assertTrue(matchingOptions.contains("ab"));
    assertTrue(matchingOptions.contains("ac"));
    assertTrue(matchingOptions.contains("ad"));
    assertTrue(matchingOptions.contains("ae"));
    assertFalse(matchingOptions.contains("b"));
  }

  @Test
  public void testHasOption() {
    Options options = new Options();
    options.addOption("a", "option a");
    options.addOption("b", true, "option b");

    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("-a"));
    assertTrue(options.hasOption("b"));
    assertTrue(options.hasOption("-b"));
    assertFalse(options.hasOption("c"));
  }

  @Test
  public void testHasLongOption() {
    Options options = new Options();
    options.addOption("a", "option a");
    options.addOption("b", true, "option b");

    assertTrue(options.hasLongOption("option a"));
    assertFalse(options.hasLongOption("option c"));
  }

  @Test
  public void testHasShortOption() {
    Options options = new Options();
    options.addOption("a", "option a");
    options.addOption("b", true, "option b");

    assertTrue(options.hasShortOption("a"));
    assertFalse(options.hasShortOption("c"));
  }

  @Test
  public void testGetOptionGroup() {
    OptionGroup group1 = new OptionGroup();
    Option option1 = new Option("a", "aa", false, "option 1");
    group1.addOption(option1);
    Options options = new Options();
    options.addOptionGroup(group1);

    assertEquals(group1, options.getOptionGroup(option1));
  }

  @Test
  public void testToString() {
    Options options = new Options();
    options.addOption("a", "option a");
    options.addOption("b", true, "option b");

    assertEquals("[ Options: [ short {a=Option{ opt='-a', longOpt='null', hasArg=false, description='option a', required=false }," +
        " b=Option{ opt='-b', longOpt='null', hasArg=true, description='option b', required=false }} ] [ long {}]", options.toString());
  }
}