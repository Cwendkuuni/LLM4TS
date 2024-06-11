import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @Test
 public void testAddOptionShort() {
 Option option = new Option("a", "alias", false, "Test option with short alias");
 options.addOption(option);
 Assert.assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionLong() {
 Option option = new Option("a", "alias-long", false, "Test option with long alias");
 options.addOption(option);
 Assert.assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionShortWithValue() {
 Option option = new Option("a", "alias", true, "Test option with short alias and value");
 options.addOption(option);
 Assert.assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionLongWithValue() {
 Option option = new Option("a", "alias-long", true, "Test option with long alias and value");
 options.addOption(option);
 Assert.assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Test option group");
 Option option = new Option("g", "group-option", false, "Test option in group");
 group.addOption(option);
 options.addOptionGroup(group);
 Assert.assertEquals(1, options.getOptions().size());
 Assert.assertEquals(1, options.getOptionGroups().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateShortOption() {
 Option option1 = new Option("a", "alias", false, "Test option with short alias");
 options.addOption(option1);
 Option option2 = new Option("a", "alias", false, "Test option with short alias");
 options.addOption(option2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateLongOption() {
 Option option1 = new Option("--long-alias", "alias", false, "Test option with long alias");
 options.addOption(option1);
 Option option2 = new Option("--long-alias", "alias", false, "Test option with long alias");
 options.addOption(option2);
 }

 @Test
 public void testGetOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Test option group");
 Option option = new Option("g", "group-option", false, "Test option in group");
 group.addOption(option);
 options.addOptionGroup(group);
 Option groupOption = options.getOption("g");
 Assert.assertNotNull(groupOption);
 Assert.assertEquals(group, options.getOptionGroup(groupOption));
 }

 @Test
 public void testHasShortOption() {
 Option option = new Option("a", "alias", false, "Test option with short alias");
 options.addOption(option);
 Assert.assertTrue(options.hasShortOption("a"));
 }

 @Test
 public void testHasLongOption() {
 Option option = new Option("--alias-long", "alias", false, "Test option with long alias");
 options.addOption(option);
 Assert.assertTrue(options.hasLongOption("--alias-long"));
 }

 @Test
 public void testHasShortOptionWithValue() {
 Option option = new Option("a", "alias", true, "Test option with short alias and value");
 options.addOption(option);
 Assert.assertTrue(options.hasOption("a"));
 }

 @Test
 public void testHasLongOptionWithValue() {
 Option option = new Option("--alias-long", "alias", true, "Test option with long alias and value");
 options.addOption(option);
 Assert.assertTrue(options.hasOption("--alias-long"));
 }

 @Test
 public void testGetOption() {
 Option option = new Option("a", "alias", false, "Test option with short alias");
 options.addOption(option);
 Assert.assertEquals(option, options.getOption("a"));
 }

 @Test
 public void testGetMatchingOptions() {
 Option option1 = new Option("--long-option-1", "option1", false, "Test option with long name starting with option1");
 Option option2 = new Option("--long-option-2", "option2", false, "Test option with long name starting with option2");
 Option option3 = new Option("--long-option", "option", false, "Test option with long name option");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 List<String> optionsList = options.getMatchingOptions("option");
 Assert.assertEquals(2, optionsList.size());
 }
}