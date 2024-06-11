import org.junit.Test;
import org.junit.Assert;

public class OptionsTest {

  @Test
  public void testAddOptionGroup() {
    Options options = new Options();
    OptionGroup group = new OptionGroup();
    Option option1 = new Option("a", "option1", false, "description");
    Option option2 = new Option("b", "option2", true, "description");
    group.addOption(option1);
    group.addOption(option2);
    options.addOptionGroup(group);

    Assert.assertTrue(options.hasOption("a"));
    Assert.assertFalse(options.hasOption("b"));

    Assert.assertEquals(group, options.getOptionGroup(option1));
  }

  @Test
  public void testAddOption() {
    Options options = new Options();
    Option option = new Option("a", "option", false, "description");
    options.addOption(option);

    Assert.assertTrue(options.hasOption("a"));
    Assert.assertFalse(options.hasOption("b"));

    Assert.assertEquals(option, options.getOption("a"));
  }

  @Test
  public void testGetOptions() {
    Options options = new Options();
    Option option1 = new Option("a", "option1", false, "description1");
    Option option2 = new Option("b", "option2", true, "description2");
    options.addOption(option1);
    options.addOption(option2);

    Assert.assertEquals(2, options.getOptions().size());

    Assert.assertTrue(options.getOptions().contains(option1));
    Assert.assertTrue(options.getOptions().contains(option2));
  }

  @Test
  public void testGetRequiredOptions() {
    Options options = new Options();
    Option option1 = new Option("a", "option1", false, "description1");
    option1.setRequired(true);
    Option option2 = new Option("b", "option2", true, "description2");
    options.addOption(option1);
    options.addOption(option2);

    Assert.assertEquals(1, options.getRequiredOptions().size());

    Assert.assertTrue(options.getRequiredOptions().contains(option1));
  }

  @Test
  public void testGetOption() {
    Options options = new Options();
    Option option1 = new Option("a", "option1", false, "description1");
    Option option2 = new Option("b", "option2", true, "description2");
    options.addOption(option1);
    options.addOption(option2);

    Assert.assertEquals(option1, options.getOption("a"));
    Assert.assertEquals(option2, options.getOption("b"));
  }

  @Test
  public void testGetMatchingOptions() {
    Options options = new Options();
    Option option1 = new Option("a", "option1", false, "description1");
    Option option2 = new Option("b", "option2", true, "description2");
    options.addOption(option1);
    options.addOption(option2);

    Assert.assertEquals(2, options.getMatchingOptions("o").size());

    Assert.assertTrue(options.getMatchingOptions("o").contains("option1"));
    Assert.assertTrue(options.getMatchingOptions("o").contains("option2"));
  }

  @Test
  public void testHasOption() {
    Options options = new Options();
    Option option1 = new Option("a", "option1", false, "description1");
    Option option2 = new Option("b", "option2", true, "description2");
    options.addOption(option1);
    options.addOption(option2);

    Assert.assertTrue(options.hasOption("a"));
    Assert.assertTrue(options.hasOption("b"));
    Assert.assertFalse(options.hasOption("c"));
  }

  @Test
  public void testHasLongOption() {
    Options options = new Options();
    Option option1 = new Option("a", "option1", false, "description1");
    Option option2 = new Option("b", "option2", true, "description2");
    options.addOption(option1);
    options.addOption(option2);

    Assert.assertTrue(options.hasLongOption("option1"));
    Assert.assertTrue(options.hasLongOption("option2"));
    Assert.assertFalse(options.hasLongOption("option3"));
  }

  @Test
  public void testHasShortOption() {
    Options options = new Options();
    Option option1 = new Option("a", "option1", false, "description1");
    Option option2 = new Option("b", "option2", true, "description2");
    options.addOption(option1);
    options.addOption(option2);

    Assert.assertTrue(options.hasShortOption("a"));
    Assert.assertTrue(options.hasShortOption("b"));
    Assert.assertFalse(options.hasShortOption("c"));
  }
  
  @Test
  public void testToString() {
    Options options = new Options();
    Option option1 = new Option("a", "option1", false, "description1");
    Option option2 = new Option("b", "option2", true, "description2");
    options.addOption(option1);
    options.addOption(option2);

    String expected = "[ Options: [ short {a=Option [ opt: a, longOpt: option1, hasArg: false, description: description1 ]} ] [ long {option1=Option [ opt: a, longOpt: option1, hasArg: false, description: description1 ], option2=Option [ opt: b, longOpt: option2, hasArg: true, description: description2 ]} ]";
    Assert.assertEquals(expected, options.toString());
  }

}