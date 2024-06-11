import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options createOptions() {
 return new Options();
 }

 @Test
 public void testAddOption() {
 Options options = createOptions();
 options.addOption("o", "option", false, "Test option");
 Option option = options.getOption("o");
 Assert.assertNotNull(option);
 Assert.assertEquals("option", option.getLongOpt());
 Assert.assertEquals(false, option.isRequired());
 }

 @Test
 public void testAddOptionWithArgument() {
 Options options = createOptions();
 options.addOption("o", "option", true, "Test option with argument");
 Option option = options.getOption("o");
 Assert.assertNotNull(option);
 Assert.assertEquals("option", option.getLongOpt());
 Assert.assertEquals(true, option.isRequired());
 }

 @Test
 public void testAddOptionWithShortAndLong() {
 Options options = createOptions();
 options.addOption("o", null, false, "Test option", "opt");
 options.addOption("t", "testOption", false, "Test test option");
 Option option = options.getOption("o");
 Assert.assertNotNull(option);
 Assert.assertEquals("opt", option.getOpt());
 Assert.assertEquals("option", option.getLongOpt());
 Assert.assertEquals(false, option.isRequired());

 Option option2 = options.getOption("t");
 Assert.assertNotNull(option2);
 Assert.assertEquals("testOption", option2.getLongOpt());
 Assert.assertEquals(false, option2.isRequired());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptions();
 OptionGroup group = new OptionGroup("MyGroup", "My group", true);
 group.setRequired(true);
 options.addOptionGroup(group);
 Option option = new Option("o", "option", false, "Test option");
 group.addOption(option);
 Option optGroupOption = options.getOption("o");
 OptionGroup groupFromOptions = options.getOptionGroup(option);
 Assert.assertEquals(group, groupFromOptions);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithSameKey() {
 Options options = createOptions();
 options.addOption("o", "option", false, "Test option");
 options.addOption("o", "option2", false, "Test option2");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionGroupWithSameKey() {
 Options options = createOptions();
 OptionGroup group = new OptionGroup("MyGroup", "My group", true);
 options.addOptionGroup(group);
 options.addOptionGroup(group);
 }

 @Test
 public void testGetOptions() {
 Options options = createOptions();
 options.addOption("o", "option", false, "Test option");
 List<Option> optionsList = options.getOptions();
 Assert.assertFalse(optionsList.isEmpty());
 Option option = optionsList.get(0);
 Assert.assertEquals("option", option.getLongOpt());
 }

 @Test(expected = ParseException.class)
 public void testGetOptionWithNonExistingOption() {
 Options options = createOptions();
 options.addOption("o", "option", false, "Test option");
 Assert.assertThrows(ParseException.class, () -> options.getOption("n"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptions();
 options.addOption("opt", "option1");
 options.addOption("optA", "option2A");
 options.addOption("optB", "option2B");
 List<String> optionsList = options.getMatchingOptions("opt");
 Assert.assertEquals(2, optionsList.size());
 Assert.assertEquals("option1", optionsList.get(0));
 Assert.assertEquals("option2B", optionsList.get(1));
 }

 @Test
 public void testHasOption() {
 Options options = createOptions();
 options.addOption("o", "option", false, "Test option");
 Assert.assertTrue(options.hasOption("o"));
 Assert.assertTrue(options.hasOption("option"));
 Assert.assertFalse(options.hasOption("n"));
 }

 @Test
 public void testHasLongOption() {
 Options options = createOptions();
 options.addOption("o", "option", false, "Test option");
 Assert.assertTrue(options.hasLongOption("o"));
 Assert.assertTrue(options.hasLongOption("option"));
 Assert.assertFalse(options.hasLongOption("n"));
 }

 @Test
 public void testHasShortOption() {
 Options options = createOptions();
 options.addOption("o", "option", false, "Test option");
 Assert.assertTrue(options.hasShortOption("o"));
 Assert.assertTrue(options.hasShortOption("option"));
 Assert.assertFalse(options.hasShortOption("n"));
 }

 @Test
 public void testGetOptionGroup() {
 Options options = createOptions();
 OptionGroup group = new OptionGroup("MyGroup", "My group", true);
 options.addOptionGroup(group);
 Option option = new Option("o", "option", false, "Test option");
 group.addOption(option);
 Option optGroupOption = options.getOption("o");
 OptionGroup groupFromOptions = options.getOptionGroup(optGroupOption);
 Assert.assertEquals(group, groupFromOptions);
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = createOptions();
 options.addOptionGroup(new OptionGroup("requiredGroup", "Required group", true));
 List<Object> requiredOptions = options.getRequiredOptions();
 Assert.assertFalse(requiredOptions.isEmpty());
 }

 @Test
 public void testToString() {
 Options options = createOptions();
 options.addOption("o", "option", false, "Test option");
 String toString = options.toString();
 Assert.assertTrue(toString.contains("short"));
 Assert.assertTrue(toString.contains("long"));
 }
}