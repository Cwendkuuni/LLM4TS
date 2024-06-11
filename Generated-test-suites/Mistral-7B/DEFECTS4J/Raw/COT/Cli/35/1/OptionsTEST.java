import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options createOptionsUnderTest() {
 return new Options();
 }

 @Test
 public void testAddOptionShortNameOnly() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "Description for option A");

 Option optionA = options.getOption("a");
 Assert.assertNotNull(optionA);
 Assert.assertEquals("Description for option A", optionA.getDescription());
 }

 @Test
 public void testAddOptionShortNameOnlyWithArgument() {
 Options options = createOptionsUnderTest();
 options.addOption("a", true, "Description for option A");

 Option optionA = options.getOption("a");
 Assert.assertNotNull(optionA);
 Assert.assertEquals("Description for option A", optionA.getDescription());
 Assert.assertTrue(optionA.hasArg());
 }

 @Test
 public void testAddOptionShortNameAndLongName() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "description for short option A", "l", "description for long option A");

 Option optionA = options.getOption("a");
 Assert.assertNotNull(optionA);
 Assert.assertEquals("description for short option A", optionA.getOpt());
 Assert.assertEquals("description for long option A", optionA.getLongOpt());
 }

 @Test
 public void testAddOptionWithArgumentAndLongName() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "description for short option A", true, "description for long option A");

 Option optionA = options.getOption("a");
 Assert.assertNotNull(optionA);
 Assert.assertEquals("description for short option A", optionA.getOpt());
 Assert.assertEquals("description for long option A", optionA.getLongOpt());
 Assert.assertTrue(optionA.hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptionsUnderTest();
 OptionGroup optionGroup = new OptionGroup("groupName", "Description for option group");
 options.addOptionGroup(optionGroup);

 Option optionInGroup = new Option("optionInGroup", true, optionGroup, "Description for option in group");
 optionGroup.addOption(optionInGroup);

 Option optionInGroupAdded = options.getOption("optionInGroup");
 Assert.assertNotNull(optionInGroupAdded);
 Assert.assertEquals(optionGroup, optionInGroupAdded.getParent());
 Assert.assertTrue(optionInGroupAdded.isRequired());
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateOptionShortName() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "Description for option A");
 options.addOption("a", "Another description for option A");
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateOptionLongName() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "Description for option A", true, "optionALongName");
 options.addOption("a", "Another description for option A", true, "optionALongName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullDescription() {
 Options options = createOptionsUnderTest();
 options.addOption("a", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullOptionName() {
 Options options = createOptionsUnderTest();
 options.addOption(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullParentGroup() {
 Options options = createOptionsUnderTest();
 Option option = new Option("a", null);
 options.addOption(option);
 }

 @Test
 public void testGetOptions() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "Description for option A");
 options.addOption("b", "Description for option B");

 List<Option> optionsList = options.getOptions();
 Assert.assertNotNull(optionsList);
 Assert.assertEquals(2, optionsList.size());
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = createOptionsUnderTest();
 options.addOption("a", true, "Description for option A");
 options.addOption("b", false, "Description for option B");

 List<Option> requiredOptions = options.getRequiredOptions();
 Assert.assertNotNull(requiredOptions);
 Assert.assertEquals(1, requiredOptions.size());
 Assert.assertTrue(requiredOptions.get(0).isRequired());
 }

 @Test
 public void testGetOption() {
 Options options = createOptionsUnderTest();options.addOption("a", "Description for option A");

 Option option = options.getOption("a");
 Assert.assertNotNull(option);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptionsUnderTest();
 options.addOption("a");
 options.addOption("ab");
 options.addOption("abc");

 List<String> matchingOptions = options.getMatchingOptions("a");
 Assert.assertNotNull(matchingOptions);
 Assert.assertEquals(2, matchingOptions.size());
 Assert.assertTrue(matchingOptions.contains("a"));
 Assert.assertTrue(matchingOptions.contains("ab"));
 }

 @Test
 public void testHasOption() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "Description for option A");

 Assert.assertTrue(options.hasOption("a"));
 }

 @Test
 public void testHasLongOption() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "Description for option A", "l", "Description for long option A");

 Assert.assertTrue(options.hasLongOption("l--optionA"));
 }

 @Test
 public void testHasShortOption() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "Description for option A");

 Assert.assertTrue(options.hasShortOption("a"));
 }

 @Test
 public void testGetOptionGroup() {
 Options options = createOptionsUnderTest();
 OptionGroup optionGroup = new OptionGroup("groupName", "Description for option group");
 options.addOptionGroup(optionGroup);
 options.addOption("optionInGroup", true, optionGroup, "Description for option in group");

 OptionGroup retrievedOptionGroup = options.getOptionGroup("groupName");
 Assert.assertEquals(optionGroup, retrievedOptionGroup);
 }

 @Test
 public void testToString() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "Description for option A");
 options.addOption("b", "Description for option B");

 String toString = options.toString();
 Assert.assertNotNull(toString);
 }
}