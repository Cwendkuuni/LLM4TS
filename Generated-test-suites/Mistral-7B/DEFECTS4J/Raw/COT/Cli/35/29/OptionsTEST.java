import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 private Options createOptions() {
 return new Options();
 }

 @Test
 public void testAddOptionShort() {
 Options options = createOptions();
 options.addOption('a', "optionA");
 assertNotNull(options.getOption("a"));
 }

 @Test
 public void testAddOptionLong() {
 Options options = createOptions();
 options.addOption("optionB", "longOptionB");
 assertNotNull(options.getOption("optionB"));
 }

 @Test
 public void testAddOptionShortWithArg() {
 Options options = createOptions();
 options.addOption('a', "optionA", true);
 assertNotNull(options.getOption("a"));
 assertTrue(options.getOption("a").hasArg());
 }

 @Test
 public void testAddOptionLongWithArg() {
 Options options = createOptions();
 options.addOption("optionC", "longOptionC", true);
 assertNotNull(options.getOption("optionC"));
 assertTrue(options.getOption("optionC").hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptions();
 OptionGroup group = new OptionGroup("groupName", "groupDescription");
 options.addOptionGroup(group);
 assertNotNull(options.getOptionGroup("groupName"));
 }

 @Test
 public void testAddOptionWithDuplicateKey() {
 Options options = createOptions();
 options.addOption('a', "optionA");
 options.addOption('a', "optionA");
 assertFalse(options.getOptions().size() > 1);
 }

 @Test(expected = ParseException.class)
 public void testAddOptionWithNegativeShortOpt() {
 Options options = createOptions();
 options.addOption(-1, "optionD", false, "description");
 }

 @Test(expected = ParseException.class)
 public void testAddOptionWithNullDescription() {
 Options options = createOptions();
 options.addOption('e', null);
 }

 @Test(expected = ParseException.class)
 public void testAddOptionWithNullShortOpt() {
 Options options = createOptions();
 options.addOption(null, "optionF", false, "description");
 }

 @Test(expected = ParseException.class)
 public void testAddOptionWithNullLongOpt() {
 Options options = createOptions();
 options.addOption("optionG", null);
 }

 @Test(expected = ParseException.class)
 public void testAddOptionWithDuplicateLongOpt() {
 Options options = createOptions();
 options.addOption("optionH", "longOptionH");
 options.addOption("optionH", "longOptionH");
 }

 @Test(expected = ParseException.class)
 public void testAddOptionWithEmptyShortOpt() {
 Options options = createOptions();
 options.addOption('', "optionI", false, "description");
 }

 @Test(expected = ParseException.class)
 public void testAddOptionWithEmptyLongOpt() {
 Options options = createOptions();
 options.addOption("", "optionJ", false, "description");
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = createOptions();
 OptionGroup group = new OptionGroup("group", "group");
 Option opt = new Option("opt", "o", false, "option");
 group.addOption(opt);
 options.addOptionGroup(group);
 options.addOption("optionK", false, "optionK");
 options.addOption("optionL", false, "optionL");
 List<Option> requiredOptions = options.getRequiredOptions();
 assertTrue(requiredOptions.contains(opt));
 assertTrue(requiredOptions.size() == 1);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptions();
 options.addOption("optionM", false, "optionM");
 options.addOption("optionN", false, "optionN");
 options.addOption("optionO", false, "optionO");
 List<String> matchingOptions = options.getMatchingOptions("o");
 assertTrue(matchingOptions.size() == 2);
 assertTrue(matchingOptions.contains("optionO"));
 assertTrue(matchingOptions.contains("optionM"));
 }

 @Test
 public void testGetOption() {
 Options options = createOptions();
 Option opt = new Option("optP", "longOptP", false, "optionP");
 options.addOption(opt);
 Option result = options.getOption("longOptP");
 assertNotNull(result);
 assertSame(opt, result);
 }

 @Test
 public void testGetOptionUnknown() {
 Options options = createOptions();
 Option result = options.getOption("unknown");
 assertNull(result);
 }

 @Test
 public void testToString() {
 Options options = createOptions();
 Option opt = new Option("optionQ", "longOptQ", false, "optionQ");
 options.addOption(opt);
 String expected = "[ Options: [ short {} ] [ long {optionQ=longOptQ} ]]";
 String result = options.toString();
 assertEquals(expected, result);
 }
}