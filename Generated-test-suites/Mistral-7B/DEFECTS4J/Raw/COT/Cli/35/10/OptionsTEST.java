import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createOptionsUnderTest() {
 Options options = new Options();
 return options;
 }

 @Test
 public void testAddOptionShort() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "optionA", false, "Description for option A");
 assertEquals("Description for option A", options.getOption("a").getDescription());
 }

 @Test
 public void testAddOptionShortWithArg() {
 Options options = createOptionsUnderTest();
 options.addOption("b", "optionB", true, "Description for option B");
 assertEquals("Description for option B", options.getOption("b").getDescription());
 }

 @Test
 public void testAddOptionLong() {
 Options options = createOptionsUnderTest();
 options.addOption("c", "optionC", false, "Description for option C", "c");
 assertEquals("Description for option C", options.getOption("c").getDescription());
 }

 @Test
 public void testAddOptionLongWithArg() {
 Options options = createOptionsUnderTest();
 options.addOption("d", null, true, "Description for option D", "d");
 assertEquals("Description for option D", options.getOption("d").getDescription());
 }

 @Test
 public void testAddOptionShortAndLong() {
 Options options = createOptionsUnderTest();
 options.addOption("e", "optionE", false, "Description for option E", "e");
 options.addOption("f", null, false, "Description for option F", "f");
 assertEquals("Description for option F", options.getOption("f").getDescription());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptionsUnderTest();
 OptionGroup group = new OptionGroup("group", "Group description");
 options.addOptionGroup(group);
 assertTrue(options.hasOptionGroup("group"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "optionA", false, "Description for option A");
 try {
 options.addOption("a", "optionA", false, "Description for option A");
 } catch (IllegalArgumentException e) {
 assertEquals("Option 'a' already exists", e.getMessage());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionToGroup() {
 Options options = createOptionsUnderTest();
 OptionGroup group = new OptionGroup("group", "Group description");
 Option option = new Option("option", "option", false, "Description for option");
 group.addOption(option);
 try {
 options.addOption(option);
 } catch (IllegalStateException e) {
 assertEquals("Option 'option' is already in a group", e.getMessage());
 }
 }

 @Test
 public void testHasOption() {
 Options options = createOptionsUnderTest();
 options.addOption("a", "optionA", false, "Description for option A");
 assertTrue(options.hasOption("a"));
 }

 @Test
 public void testHasLongOption() {
 Options options = createOptionsUnderTest();
 options.addOption("c", "optionC", false, "Description for option C", "c");
 assertTrue(options.hasLongOption("c"));
 }

 @Test
 public void testHasShortOption() {
 Options options = createOptionsUnderTest();
 options.addOption("e", "optionE", false, "Description for option E", "e");
 assertTrue(options.hasShortOption("e"));
 }

 @Test
 public void testHasOptionGroup() {
 Options options = createOptionsUnderTest();
 OptionGroup group = new OptionGroup("group", "Group description");
 options.addOptionGroup(group);
 assertTrue(options.hasOptionGroup("group"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptionsUnderTest();
 options.addOption("option1", null, false, "Option1");
 options.addOption("option2", null, false, "Option2");
 options.addOption("option3", null, false, "Option3");
 List<String> matchingOptions = options.getMatchingOptions("opt");
 assertEquals(2, matchingOptions.size());
 assertEquals("option2", matchingOptions.get(0));
 assertEquals("option3", matchingOptions.get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionWithInvalidName() {
 Options options = createOptionsUnderTest();
 try {
 options.getOption("invalidOption");
 } catch (IllegalArgumentException e) {
 assertEquals("No such option: invalidOption", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionGroupWithInvalidName() {
 Options options = createOptionsUnderTest();
 OptionGroup group = new OptionGroup("group", "Group description");
 options.addOptionGroup(group);
 try {
 options.getOptionGroup("invalidGroup");
 } catch (IllegalArgumentException e) {
 assertEquals("No such option group: invalidGroup", e.getMessage());
 }
 }

}