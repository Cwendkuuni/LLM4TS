import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options createOptions() {
 return new Options()
 .addOption("a", "alias", true, "An alias option")
 .addOption("b", "boolean", false, "An boolean option")
 .addOptionGroup(new OptionGroup("group1", "Group 1")
 .addOption("c", "group-option", false, "A group option")
 .addOption("d", "group-option", true, "Another group option")
 )
 .addOptionGroup(new OptionGroup("group2", "Group 2")
 .addOption("e", "group-option", false, "A group option from group 2")
 );
 }

 @Test
 public void testAddOption() {
 Options options = createOptions();
 Option optionA = options.getOption("a");
 Assert.assertNotNull(optionA);
 }

 @Test
 public void testAddOptionWithArgument() throws ParseException {
 Options options = createOptions();
 Option optionB = options.getOption("b");
 Assert.assertNotNull(optionB);

 String[] args = new String[] {"--boolean"};
 options.parse(args);
 Assert.assertTrue(options.isSet("boolean"));
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptions();
 OptionGroup optionGroup1 = options.getOptionGroup("group1");
 Assert.assertNotNull(optionGroup1);
 Option optionC = optionGroup1.getOption("c");
 Assert.assertNotNull(optionC);
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingOption() throws ParseException {
 Options options = createOptions();
 String[] args = new String[] {"--missing"};
 options.parse(args);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 Options options = createOptions();
 String[] args = new String[] {"--invalid"};
 options.parse(args);
 }

 @Test(expected = ParseException.class)
 public void testParseWithOptionArgumentMismatch() throws ParseException {
 Options options = createOptions();
 String[] args = new String[] {"--alias", "invalid"};
 options.parse(args);
 }

 @Test
 public void testParseWithMultipleArguments() throws ParseException {
 Options options = createOptions();
 String[] args = new String[] {"--alias", "value1", "value2"};
 options.parse(args);
 Assert.assertEquals("value1", options.getOptionValue("alias"));
 Assert.assertEquals("value2", options.getOptionValue("alias"));
 }

 @Test
 public void testParseWithGroupOption() throws ParseException {
 Options options = createOptions();
 String[] args = new String[] {"--group1", "--group-option"};
 options.parse(args);
 Option optionC = options.getOption("c");
 Assert.assertTrue(optionC.isSet());
 }

 @Test
 public void testParseWithMultipleGroupOptions() throws ParseException {
 Options options = createOptions();
 String[] args = new String[] {"--group1", "--group-option", "--group2", "--group-option"};
 options.parse(args);
 Option optionC = options.getOption("c");
 Assert.assertTrue(optionC.isSet());
 Option optionE = options.getOption("e");
 Assert.assertTrue(optionE.isSet());
 }
}