import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options createOptionsUnderTest() {
 return new Options();
 }

 @Test
 public void testAddOption() {
 Options options = createOptionsUnderTest();

 // Test adding a single character option with no argument
 options.addOption('a', "optionA");

 // Test adding a single character option with an argument
 options.addOption('b', "optionB", true, "argumentB");

 // Test adding an option with a short and long name
 options.addOption("longOptionC", "optionC", false, "descriptionC");
 options.addOption('c', "optionC", false, "descriptionC");

 Assert.assertEquals(3, options.getOptions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionNullDescription() {
 Options options = createOptionsUnderTest();
 options.addOption('d', null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionNullOptionGroup() {
 Options options = createOptionsUnderTest();
 Option option = new Option("optionE", true, null, "descriptionE");
 options.addOptionGroup(null);
 options.addOption(option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionGroupNullOptions() {
 Options options = createOptionsUnderTest();
 OptionGroup optionGroup = new OptionGroup("groupF", "groupDescriptionF");
 options.addOptionGroup(optionGroup);
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptionsUnderTest();
 OptionGroup optionGroup = new OptionGroup("groupG", "groupDescriptionG");
 Option optionA = new Option("optionGA", true, null, "descriptionGA");
 Option optionB = new Option("optionGB", true, null, "descriptionGB");
 optionGroup.addOption(optionA);
 optionGroup.addOption(optionB);
 options.addOptionGroup(optionGroup);

 Assert.assertEquals(3, options.getOptions().size());
 }

 @Test
 public void testAddOptionRequiresArgument() {
 Options options = createOptionsUnderTest();
 Option optionA = new Option("optionHA", true, null, "descriptionHA");
 options.addOption(optionA);

 try {
 options.parse("-hA");
 Assert.fail();
 } catch (ParseException e) {
 //expected
 }
 }

 @Test
 public void testAddOptionDoesNotRequireArgument() {
 Options options = createOptionsUnderTest();
 Option optionA = new Option("optionIA", false, null, "descriptionIA");
 options.addOption(optionA);
 options.parse("--optionI");
 }

 @Test
 public void testGetOption() {
 Options options = createOptionsUnderTest();
 Option optionA = new Option("optionJ", false, null, "descriptionJ");
 Option optionK = new Option("optionK", false, null, "descriptionK");
 options.addOption(optionA);
 options.addOption(optionK);

 Option optionRetrieved = options.getOption("optionJ");
 Assert.assertEquals(optionA, optionRetrieved);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionInvalidOption() {
 Options options = createOptionsUnderTest();
 Option optionA = new Option("optionL", false, null, "descriptionL");
 options.addOption(optionA);

 try {
 options.getOption("invalidOption");
 Assert.fail();
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptionsUnderTest();
 Option optionA = new Option("optionM", false, null, "descriptionM");
 Option optionN = new Option("optionN", false, null, "descriptionN");
 Option optionO = new Option("optionO", false, null, "descriptionO");
 options.addOption(optionA);
 options.addOption(optionN);
 options.addOption(optionO);

 List<String> matchingOptions = options.getMatchingOptions("optio");
 Assert.assertEquals(2, matchingOptions.size());
 Assert.assertEquals("optionM", matchingOptions.get(0));
 Assert.assertEquals("optionN", matchingOptions.get(1));
 }

 @Test
 public void testHasOption() {
 Options options = createOptionsUnderTest();Option optionA = new Option("optionP", false, null, "descriptionP");
 Option optionQ = new Option("optionQ", false, null, "descriptionQ");
 options.addOption(optionA);
 options.addOption(optionQ);

 Assert.assertTrue(options.hasOption("optionP"));
 Assert.assertTrue(options.hasOption("optionQ"));
 Assert.assertFalse(options.hasOption("invalidOption"));
 }

 @Test
 public void testHasLongOption() {
 Options options = createOptionsUnderTest();
 Option optionA = new Option("optionR", false, null, "descriptionR");
 Option optionS = new Option("optionS", false, null, "descriptionS");
 options.addOption(optionA);
 options.addOption(optionS);

 Assert.assertTrue(options.hasLongOption("optionR"));
 Assert.assertTrue(options.hasLongOption("optionS"));
 Assert.assertFalse(options.hasLongOption("invalidOption"));
 }

 @Test
 public void testHasShortOption() {
 Options options = createOptionsUnderTest();
 Option optionA = new Option("optionT", false, null, "descriptionT");
 Option optionU = new Option("optionU", false, null, "descriptionU");
 options.addOption(optionA);
 options.addOption(optionU);

 Assert.assertTrue(options.hasShortOption('t'));
 Assert.assertTrue(options.hasShortOption('u'));
 Assert.assertFalse(options.hasShortOption('v'));
 }

 @Test
 public void testGetOptionGroup() {
 Options options = createOptionsUnderTest();
 OptionGroup optionGroup = new OptionGroup("groupH", "groupDescriptionH");
 Option optionA = new Option("optionHA", true, null, "descriptionHA");
 Option optionB = new Option("optionHB", true, null, "descriptionHB");
 optionGroup.addOption(optionA);
 optionGroup.addOption(optionB);
 options.addOptionGroup(optionGroup);

 OptionGroup retrievedOptionGroup = options.getOptionGroup(optionA);
 Assert.assertEquals(optionGroup, retrievedOptionGroup);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionGroupNullOption() {
 Options options = createOptionsUnderTest();
 OptionGroup optionGroup = new OptionGroup("groupI", "groupDescriptionI");
 options.addOptionGroup(optionGroup);

 try {
 options.getOptionGroup(null);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 //expected
 }
 }
}