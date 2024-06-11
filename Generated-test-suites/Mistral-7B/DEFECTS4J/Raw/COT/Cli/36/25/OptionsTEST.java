import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @Test
 public void testAddOption() {
 options.addOption("a", "optionA", false, "Option A");
 Option optionA = options.getOption("a");
 Assert.assertNotNull(optionA);
 }

 @Test
 public void testAddOptionWithArg() {
 options.addOption("b", "optionB", true, "Option B with argument");
 Option optionB = options.getOption("b");
 Assert.assertNotNull(optionB);
 Assert.assertTrue(optionB.hasArg());
 }

 @Test
 public void testAddLongOption() {
 options.addOption("c", "optionC", false, "Option C");
 Option optionC = options.getOption("c");
 Assert.assertNotNull(optionC);
 }

 @Test
 public void testAddLongOptionWithArg() {
 options.addOption("d", "optionD", true, "Option D with argument");
 Option optionD = options.getOption("d");
 Assert.assertNotNull(optionD);
 Assert.assertTrue(optionD.hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Group Options");
 Option optionE = new Option("e", "optionE", false, "Option E");
 group.addOption(optionE);
 options.addOptionGroup(group);
 Option optionGroupE = options.getOptionGroup(optionE);
 Assert.assertNotNull(optionGroupE);
 Assert.assertEquals("group", optionGroupE.getKey());
 }

 @Test
 public void testGetRequiredOptions() {
 options.addOption("r", "requiredOption", true, "Required Option");
 List<Option> requiredOptions = options.getRequiredOptions();
 Assert.assertFalse(requiredOptions.isEmpty());
 }

 @Test
 public void testGetMatchingOptions() {
 options.addOption("o", "optionOne", false, "Option One");
 options.addOption("op", "optionTwo", false, "Option Two");
 List<String> matchingOptions = options.getMatchingOptions("op");
 Assert.assertFalse(matchingOptions.isEmpty());
 Assert.assertEquals(1, matchingOptions.size());
 Assert.assertEquals("optionTwo", matchingOptions.get(0));
 }

 @Test
 public void testHasOption() {
 options.addOption("h", "help", false, "Print this help message.");
 Assert.assertTrue(options.hasOption("h"));
 }

 @Test
 public void testHasLongOption() {
 options.addOption("l", "longOption", false, "Long Option.");
 Assert.assertTrue(options.hasLongOption("l"));
 }

 @Test
 public void testHasShortOption() {
 options.addOption("h", "help", false, "Print this help message.");
 Assert.assertTrue(options.hasShortOption("h"));
 }

 @Test(expected = ParseException.class)
 public void testParseException() {
 String[] args = {"--nonOption"};
 try {
 options.parse(args);
 } catch (ParseException e) {
 // expected
 }
 }

}