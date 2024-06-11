import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options createOptions() {
 return new Options();
 }

 @Test
 public void testAddOption() {
 Options options = createOptions();
 options.addOption("a", "optionA", false, "Description for option A");
 Assert.assertEquals("optionA", options.getOption("a").getKey());
 Assert.assertEquals(false, options.getOption("a").isRequired());
 }

 @Test
 public void testAddOptionWithArg() {
 Options options = createOptions();
 options.addOption("b", "optionB", true, "Description for option B");
 Assert.assertEquals("optionB", options.getOption("b").getKey());
 Assert.assertEquals(true, options.getOption("b").isRequired());
 }

 @Test
 public void testAddOptionWithShortAndLong() {
 Options options = createOptions();
 options.addOption("c", "optionC", false, "Description for option C", "valueC");
 Assert.assertEquals("c", options.getOption("c").getKey());
 Assert.assertEquals("optionC", options.getOption("--optionC").getKey());
 Assert.assertEquals("valueC", options.getOption("c").getArgName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateKey() {
 Options options = createOptions();
 options.addOption("d", "optionD", false, "Description for option D");
 options.addOption("d", "optionD", false, "Description for option D");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionGroup() {
 Options options = createOptions();
 OptionGroup group = new OptionGroup("groupName", "group description");
 options.addOptionGroup(group);
 options.addOptionGroup(group);
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptions();
 OptionGroup group = new OptionGroup("groupName", "group description");
 options.addOptionGroup(group);
 Assert.assertNotNull(options.getOptionGroups());
 Assert.assertEquals(1, options.getOptionGroups().size());
 }

 @Test
 public void testParseException() {
 Options options = createOptions();
 try {
 options.parse("-d");
 } catch (ParseException e) {
 Assert.assertEquals("Invalid option: -d", e.getMessage());
 }
 }

 @Test
 public void testParseMissingRequiredOption() {
 Options options = createOptions();
 options.addOption("e", "optionE", true, "Option E requires an argument");
 try {
 options.parse("-f");
 } catch (ParseException e) {
 Assert.assertEquals("Missing required option '-e'", e.getMessage());
 }
 }

 @Test
 public void testParseWithValidOptions() {
 Options options = createOptions();
 options.addOption("f", "optionF", false, "Option F");
 options.addOption("g", "optionG", true, "Option G requires an argument");
 String[] args = new String[]{"-f", "--optionG", "valueG"};
 try {
 options.parse(args);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 Assert.assertArrayEquals(new String[]{"-f", "--optionG=valueG"}, args);
 }

 @Test
 public void testParseWithInvalidOption() {
 Options options = createOptions();
 options.addOption("h", "optionH", false, "Option H");
 String[] args = new String[]{"-i", "--optionH"};
 try {
 options.parse(args);
 } catch (ParseException e) {
 Assert.assertEquals("Invalid option: -i", e.getMessage());
 }
 }

 @Test
 public void testParseWithLongOptionWithArg() {
 Options options = createOptions();
 options.addOption("i", null, true, "Option I requires an argument");
 String[] args = new String[]{"--optionI", "valueI"};
 try {
 options.parse(args);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 Assert.assertEquals("--optionI=valueI", args[0]);
 }

 @Test
 public void testParseWithMixedShortAndLongOptions() {
 Options options = createOptions();
 options.addOption("j", "optionJ", false, "Option J");
 options.addOption("k", null, true, "Option K requires an argument");
 String[] args = new String[]{"-j", "--optionK", "valueK"};
 try {
 options.parse(args);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 Assert.assertArrayEquals(new String[]{"-j", "--optionK=valueK"}, args);
 }

 @Test
 public void testParseWithOptionGroup() {
 Options options = createOptions();
 OptionGroup group = new OptionGroup("groupName", "group description");
 options.addOption("l", null, false, "Option L is part of the group");
 options.addOption("m", null, false, "Option M is part of the group");
 group.addOption("n", null, false, "Option N is part of the group");
 options.addOptionGroup(group);
 String[] args = new String[]{"--groupName"};
 try {
 options.parse(args);
 } catch (ParseException e) {
 Assert.assertTrue(e.getMessage().startsWith("group 'groupName'"));
 }
 }

 @Test
 public void testToString() {
 Options options = createOptions();
 options.addOption("o", "optionO", false, "Description for option O");
 options.addOptionGroup(new OptionGroup("groupName", "group description"));
 Assert.assertEquals("Options: [ short {o=optionO} ] [ long {} ]", options.toString());
 }

}