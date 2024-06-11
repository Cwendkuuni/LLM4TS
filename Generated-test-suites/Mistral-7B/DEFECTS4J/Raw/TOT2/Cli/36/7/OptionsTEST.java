import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OptionsTest {

 private Options options;

 @Before
 public void setup() {
 options = new Options();
 options.addOption("a", "alpha", false, "an option with short name a and no argument");
 options.addOption("b", "beta", true, "an option with short name b and an argument");
 options.addOption("c", null, true, "an option with long name c and an argument");
 options.addOption("d", "delta", false, "an option with long name delta and no argument");
 }

 @Test
 public void testBasicUsage() {
 String[] args = {"-a", "-b", "valueB"};
 try {
 options.parse(args);
 } catch (ParseException e) {
 Assert.fail("ParseException unexpected: " + e.getMessage());
 }

 Assert.assertTrue(options.isSetOption("a"));
 Assert.assertTrue(options.isSetOption("b"));
 Assert.assertEquals("valueB", options.getOptionValue("b"));
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() {
 String[] args = {"-x"};
 try {
 options.parse(args);
 } catch (ParseException e) {
 // expected
 }

 Assert.assertFalse(options.isSetOption("a"));
 Assert.assertFalse(options.isSetOption("b"));
 Assert.assertNull(options.getOptionValue("b"));
 }

 @Test
 public void testLongOption() {
 String[] args = {"--c", "valueC"};
 try {
 options.parse(args);
 } catch (ParseException e) {
 Assert.fail("ParseException unexpected: " + e.getMessage());
 }

 Assert.assertTrue(options.isSetOption("c"));
 Assert.assertEquals("valueC", options.getOptionValue("c"));
 }

 @Test
 public void testShortOptionWithArgument() {
 String[] args = {"-b", "valueB"};
 try {
 options.parse(args);
 } catch (ParseException e) {
 Assert.fail("ParseException unexpected: " + e.getMessage());
 }

 Assert.assertTrue(options.isSetOption("b"));
 Assert.assertEquals("valueB", options.getOptionValue("b"));
 }

 @Test(expected = ParseException.class)
 public void testMissingArgumentForShortOption() {
 String[] args = {"-b"};
 try {
 options.parse(args);
 } catch (ParseException e) {
 // expected
 }

 Assert.assertFalse(options.isSetOption("b"));
 Assert.assertNull(options.getOptionValue("b"));
 }

 @Test
 public void testOptionGroup() {
 OptionGroup optionGroup = new OptionGroup("mygroup", "My Option Group");
 options.addOptionGroup(optionGroup);
 optionGroup.addOption("e", "epsilon", true, "an option with short name e and an argument");
 optionGroup.addOption("f", "phi", false, "an option with short name f and no argument");

 String[] args = {"--mygroup", "-e", "valueE"};
 try {
 options.parse(args);
 } catch (ParseException e) {
 Assert.fail("ParseException unexpected: " + e.getMessage());
 }

 Assert.assertTrue(options.isSetOption("e"));
 Assert.assertEquals("valueE", options.getOptionValue("e"));
 Assert.assertFalse(options.isSetOption("f"));
 }

 @Test(expected = ParseException.class)
 public void testMissingOptionInOptionGroup() {
 OptionGroup optionGroup = new OptionGroup("mygroup", "My Option Group");
 options.addOptionGroup(optionGroup);
 optionGroup.addOption("e", "epsilon", true, "an option with short name e and an argument");

 String[] args = {"--mygroup", "-g"};
 try {
 options.parse(args);
 } catch (ParseException e) {
 // expected
 }

 Assert.assertFalse(options.isSetOption("e"));
 Assert.assertFalse(options.isSetOption("g"));
 Assert.assertNull(options.getOptionValue("g"));
 }
}