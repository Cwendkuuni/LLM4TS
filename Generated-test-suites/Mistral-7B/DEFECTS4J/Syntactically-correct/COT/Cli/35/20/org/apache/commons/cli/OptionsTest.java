import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options options;

 @Before
 public void setup() {
 options = new Options();
 }

 @Test
 public void testAddOption() {
 options.addOption("a", "optionA", false, "Option A");
 Option optionA = options.getOption("a");
 Assert.assertNotNull(optionA);
 Assert.assertEquals("Option A", optionA.getLongOpt());
 Assert.assertFalse(optionA.hasArg());
 }

 @Test
 public void testAddOptionWithArgument() {
 options.addOption("a", "optionA", true, "Option A with argument");
 Option optionA = options.getOption("a");
 Assert.assertNotNull(optionA);
 Assert.assertEquals("Option A", optionA.getLongOpt());
 Assert.assertTrue(optionA.hasArg());
 }

 @Test
 public void testAddOptionWithShortAndLong() {
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("b", "optionB", false, "Option B");
 Option optionA = options.getOption("a");
 Option optionB = options.getOption("b");
 Assert.assertNotNull(optionA);
 Assert.assertEquals("Option A", optionA.getLongOpt());
 Assert.assertFalse(optionA.hasArg());
 Assert.assertNotNull(optionB);
 Assert.assertEquals("Option B", optionB.getLongOpt());
 Assert.assertFalse(optionB.hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group Name", "Group description");
 options.addOptionGroup(group);
 Option optionInGroup = new Option("g", "optionG", false, "Option in group");
 group.addOption(optionInGroup);
 Option optionInGroupFromOptions = options.getOption("g");
 Assert.assertNotNull(optionInGroupFromOptions);
 Assert.assertEquals("group Name", optionInGroupFromOptions.getLongOpt());
 Assert.assertFalse(optionInGroupFromOptions.hasArg());
 }

 @Test
 public void testGetMatchingOptions() {
 options.addOption("o1", "option1");
 options.addOption("o2", "option2");
 List<String> matchingOptions = options.getMatchingOptions("o");
 Assert.assertEquals(2, matchingOptions.size());
 Assert.assertTrue(matchingOptions.contains("o1"));
 Assert.assertTrue(matchingOptions.contains("o2"));
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateOption() {
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("a", "optionA2", false, "Option A2");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullOption() {
 options.addOption(null, null, false, "Option N");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullDescriptionOption() {
 options.addOption("a", "optionA", false, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullLongOptOption() {
 options.addOption(null, "optionA", false, "Option A");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullShortOptOption() {
 options.addOption("a", null, false, "Option A");
 }
}