import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options options;

 @Test
 public void testOptionsInstance() {
 options = new Options();
 Assert.assertNotNull(options);
 }

 @Test
 public void testAddOptionShort() {
 options.addOption("a", "optionA", false, "Short option A");
 Assert.assertEquals("optionA", options.getOption("a").getLongOpt());
 Assert.assertFalse(options.getOption("a").getHasArg());
 }

 @Test
 public void testAddOptionShortWithArg() {
 options.addOption("b", "optionB", true, "Short option B with argument");
 Assert.assertEquals("optionB", options.getOption("b").getLongOpt());
 Assert.assertTrue(options.getOption("b").getHasArg());
 }

 @Test
 public void testAddOptionLong() {
 options.addOption("c", "optionC", false, "Long option C");
 Assert.assertEquals("optionC", options.getOption("c").getKey());
 Assert.assertNull(options.getOption("c").getLongOpt());
 }

 @Test
 public void testAddOptionLongWithArg() {
 options.addOption("d", "optionD", true, "Long option D with argument");
 Assert.assertEquals("optionD", options.getOption("d").getKey());
 Assert.assertEquals("optionD", options.getOption("d").getLongOpt());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("GroupA", "Group A", true);
 group.addOption(new Option("e", "optionE", true, "Option E in GroupA"));
 options.addOptionGroup(group);
 Assert.assertEquals("GroupA", options.getOptionGroup(options.getOption("e")).getName());
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateOption() {
 options.addOption("f", "optionF", false, "Option F");
 options.addOption("f", "optionF", false, "Option F (duplicate)");
 }

 @Test
 public void testHasOption() {
 options.addOption("g", "optionG", false, "Option G");
 Assert.assertTrue(options.hasOption("g"));
 Assert.assertTrue(options.hasOption("g", false));
 Assert.assertTrue(options.hasOption("g", true));
 Assert.assertTrue(options.hasLongOption("optionG"));
 Assert.assertTrue(options.hasShortOption("g"));
 }

 @Test
 public void testHasNoOption() {
 options.addOption("h", "optionH", false, "Option H");
 Assert.assertFalse(options.hasOption("i"));
 }

 @Test
 public void testGetMatchingOptions() {
 options.addOption("j", "optionJ", false, "Option J");
 options.addOption("k", "optionK", false, "Option K");
 options.addOption("l", "optionL", false, "Option L");
 List<String> matching = options.getMatchingOptions("j");
 Assert.assertEquals(1, matching.size());
 Assert.assertEquals("optionJ", matching.get(0));
 }

 @Test
 public void testParseOptions() {
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a", "argA", "-b", "argB", "--optionC"};
 try {
 CommandLine cmd = parser.parse(options.parseOptions(args), true);
 Assert.assertNotNull(cmd);
 Assert.assertEquals("argA", cmd.getOptionValue("a"));
 Assert.assertEquals("argB", cmd.getOptionValue("b"));
 Assert.assertNull(cmd.getOptionValue("c"));
 } catch (ParseException e) {
 Assert.fail("Unexpected parse exception: " + e.getMessage());
 }
 }

}