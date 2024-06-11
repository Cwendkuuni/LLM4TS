import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options options;

 @Test
 public void testAddOption() {
 options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");
 Option optionA = options.getOption("a");
 Assert.assertNotNull(optionA);
 Assert.assertEquals("optionA", optionA.getLongOpt());
 Assert.assertFalse(optionA.hasArg());
 }

 @Test
 public void testAddOptionWithArg() {
 options = new Options();
 options.addOption("b", "optionB", true, "Description for option B");
 Option optionB = options.getOption("b");
 Assert.assertNotNull(optionB);
 Assert.assertEquals("optionB", optionB.getLongOpt());
 Assert.assertTrue(optionB.hasArg());
 }

 @Test
 public void testAddOptionWithShortAndLong() {
 options = new Options();
 options.addOption('c', "optionC", false, "Description for option C");
 options.addOption("optionD", "optionD", false, "Description for option D");
 Option optionC = options.getOption("c");
 Option optionD = options.getOption("optionD");
 Assert.assertNotNull(optionC);
 Assert.assertEquals("optionC", optionC.getLongOpt());
 Assert.assertFalse(optionC.hasArg());
 Assert.assertNotNull(optionD);
 Assert.assertEquals("optionD", optionD.getLongOpt());
 Assert.assertFalse(optionD.hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 options = new Options();
 OptionGroup group = new OptionGroup("group", "Group name");
 options.addOptionGroup(group);
 group.setRequired(true);
 Option option1 = new Option("option1", "Option 1", false, "Description for option 1");
 Option option2 = new Option("option2", "Option 2", true, "Description for option 2");
 group.addOption(option1);
 group.addOption(option2);
 options.addOptionGroup(group);
 List<Option> optionsList = options.helpOptions();
 Assert.assertEquals(4, optionsList.size());
 Assert.assertTrue(optionsList.get(0).isRequired());
 Assert.assertTrue(optionsList.get(1).isRequired());
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateOption() {
 options = new Options();
 options.addOption("d", "optionD", false, "Description for option D");
 try {
 options.addOption("d", "optionD", false, "Description for option D");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testGetOption() {
 options = new Options();
 Option optionA = options.addOption("a", "optionA", false, "Description for option A");
 Option optionB = options.addOption("b", "optionB", false, "Description for option B");
 Option optionC = options.getOption("a");
 Assert.assertEquals(optionA, optionC);
 }

 @Test
 public void testGetMatchingOptions() {
 options = new Options();
 options.addOption("option1", "Option 1");
 options.addOption("option2", "Option 2");
 options.addOption("option3", "Option 3");
 List<String> matchingOptions = options.getMatchingOptions("opt");
 Assert.assertEquals(1, matchingOptions.size());
 Assert.assertEquals("option1", matchingOptions.get(0));
 }

 @Test
 public void testHasOption() {
 options = new Options();
 options.addOption("option1", "Option 1");
 Assert.assertTrue(options.hasOption("option1"));
 Assert.assertFalse(options.hasOption("optionX"));
 }

 @Test
 public void testHasLongOption() {
 options = new Options();
 options.addOption("option1", "Option 1");
 Assert.assertTrue(options.hasLongOption("option1"));
 Assert.assertFalse(options.hasLongOption("optionX"));
 }

 @Test
 public void testHasShortOption() {
 options = new Options();
 options.addOption('o', "option1", false, "Option 1");
 Assert.assertTrue(options.hasShortOption("o"));
 Assert.assertFalse(options.hasShortOption("x"));
 }

 @Test
 public void testGetOptionGroup() {
 options = new Options();
 OptionGroup group = new OptionGroup("group", "Group name");
 Option option1 = new Option("option1", "Option 1", false, "Description for option 1");
 option1.setLongOpt("long-option1");
 group.addOption(option1);
 options.addOptionGroup(group);
 OptionGroup optionGroup = options.getOptionGroup(option1);
 Assert.assertNotNull(optionGroup);
 Assert.assertEquals("group", optionGroup.getName());
 }
}