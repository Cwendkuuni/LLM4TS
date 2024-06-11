import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options createOptions() {
 return new Options().addOption("i", "inputFile", true, "input file").addOption("o", "outputFile", true, "output file");
 }

 @Test
 public void testAddOption() {
 Options options = createOptions();
 Assert.assertEquals(-1, options.getOptions().size());

 Option option = new Option("i", "inputFile", true, "input file");
 options.addOption(option);

 Assert.assertEquals(1, options.getOptions().size());
 Assert.assertEquals(option, options.getOptions().get(0));
 }

 @Test
 public void testAddOptionWithArg() {
 Options options = createOptions();
 Option option = new Option("i", "inputFile", true, "input file");
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();
 try {
 parser.parse(options, new String[]{"-i", "test.txt"});
 } catch (ParseException e) {
 Assert.fail("parse error: " + e.getMessage());
 }

 Assert.assertEquals("test.txt", parser.getOptions().getOptionValues("inputFile")[0]);
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group options");
 options.addOptionGroup(group);

 group.addOption("g", "groupOption", false, "group option");

 CommandLineParser parser = new DefaultParser();
 try {
 parser.parse(options, new String[]{"-g"});
 } catch (ParseException e) {
 Assert.fail("parse error: " + e.getMessage());
 }

 Assert.assertTrue(parser.getOptions().hasOption("groupOption"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Options options = new Options();
 Option option = new Option("i", "inputFile", true, "input file");
 options.addOption(option);
 options.addOption(option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateKeyInOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group options");
 Option option = new Option("i", "inputFile", true, "input file");
 group.addOption(option);
 options.addOptionGroup(group);
 options.addOption(option);
 }

 @Test
 public void testGetOptions() {
 Options options = createOptions();
 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(options, new String[]{"-i", "test.txt", "-o", "test.txt"});
 } catch (ParseException e) {
 Assert.fail("parse error: " + e.getMessage());
 }

 Assert.assertEquals(2, options.getOptions().size());
 }

 @Test(expected = NullPointerException.class)
 public void testGetOptionNull() {
 Options options = createOptions();
 Assert.assertNull(options.getOption(null));
 }

 @Test
 public void testGetOption() {
 Options options = createOptions();
 Option option = options.getOption("i");
 Assert.assertNotNull(option);
 Assert.assertEquals("inputFile", option.getLongOpt());
 Assert.assertTrue(option.hasArg());
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options().addOption("a", "apple").addOption("b", "banana")
 .addOption("c", "cherry").addOption("d", "date");

 List<String> matchingOptions = options.getMatchingOptions("c");
 Assert.assertEquals(1, matchingOptions.size());
 Assert.assertEquals("c", matchingOptions.get(0));

 matchingOptions = options.getMatchingOptions("appl");
 Assert.assertEquals(1, matchingOptions.size());
 Assert.assertEquals("apple", matchingOptions.get(0));
 }

 @Test
 public void testHasOption() {
 Options options = new Options().addOption("a", "apple").addOption("b", "banana")
 .addOption("c", "cherry").addOption("d", "date");

 Assert.assertTrue(options.hasOption("a"));
 Assert.assertTrue(options.hasOption("b"));
 Assert.assertTrue(options.hasOption("c"));
 Assert.assertTrue(options.hasOption("d"));
 Assert.assertFalse(options.hasOption("e"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options().addOption("a", "apple").addOption("b", "banana")
 .addOption("c", "cherry").addOption("d", "date");

 Assert.assertTrue(options.hasShortOption("a"));
 Assert.assertFalse(options.hasShortOption("e"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options().addOption("a", "apple").addOption("b", "banana")
 .addOption("c", "cherry").addOption("d", "date");

 Assert.assertTrue(options.hasLongOption("apple"));
 Assert.assertTrue(options.hasLongOption("banana"));
 Assert.assertFalse(options.hasLongOption("e"));
 }
}