import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 // Basic test cases for addOption method
 @Test
 public void testAddOptionWithShortOpt() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 Assert.assertTrue(options.hasOption("a"));
 }

 @Test
 public void testAddOptionWithLongOpt() {
 Options options = new Options();
 options.addOption("optionB", "optionB", false, "Option B");
 Assert.assertTrue(options.hasLongOption("optionB"));
 }

 @Test
 public void testAddOptionWithShortAndLongOpt() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("optionB", "optionB", false, "Option B");
 Assert.assertTrue(options.hasOption("a"));
 Assert.assertTrue(options.hasOption("optionB"));
 }

 // Test for edge cases and exception handling
 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateShortOpt() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("a", "optionA", false, "Option A");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateLongOpt() {
 Options options = new Options();
 options.addOption("optionA", "optionA", false, "Option A");
 options.addOption("optionA", "optionA", false, "Option A");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullOption() {
 Options options = new Options();
 options.addOption((String) null, "optionA", false, "Option A");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullDescription() {
 Options options = new Options();
 options.addOption("optionA", (String) null);
 }

 // Test for addOptionGroup method
 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Group");
 options.addOptionGroup(group);
 Assert.assertNotNull(options.getOptionGroup(group));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionGroupWithNullGroup() {
 Options options = new Options();
 options.addOptionGroup((OptionGroup) null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionGroupWithDuplicateName() {
 OptionGroup group1 = new OptionGroup("group1", "Group 1");
 Options options = new Options();
 options.addOptionGroup(group1);
 options.addOptionGroup(group1);
 }

 // Test for getOptions method
 @Test
 public void testGetOptions() {
 Options options = new Options();
 Option option = new Option("optionC", "optionC", false, "Option C");
 options.addOption(option);
 List<Option> optionsList = options.getOptions();
 Assert.assertEquals(1, optionsList.size());
 Assert.assertEquals(option, optionsList.get(0));
 }
}