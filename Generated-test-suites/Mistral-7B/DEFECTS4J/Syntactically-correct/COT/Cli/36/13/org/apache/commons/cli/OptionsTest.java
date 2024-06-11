import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createTestOptions() {
 Options options = new Options();

 // Test addOption with short option only
 options.addOption("a", "option-a", false, "Description for option A");

 // Test addOption with short option and argument
 options.addOption("b", "option-b", true, "Description for option B");

 // Test addOption with long option only
 options.addOption("c", "option-c", false, "Description for option C");

 // Test addOption with long option and argument
 options.addOption("d", "option-d", true, "Description for option D");

 // Test addOptionGroup
 OptionGroup group = new OptionGroup("group", "Options group");
 group.setRequired(false);
 Option option1 = new Option("g1", "group-option-1", false, "Description for group option 1");
 Option option2 = new Option("g2", "group-option-2", false, "Description for group option 2");
 group.addOption(option1);
 group.addOption(option2);
 options.addOptionGroup(group);

 return options;
 }

 @Test
 public void testGetOptions() {
 Options options = createTestOptions();
 List<Option> optionsList = options.helpOptions();
 assertEquals(4, optionsList.size());

 Option optionA = optionsList.get(0);
 assertEquals("a", optionA.getKey());
 assertFalse(optionA.hasArg());
 assertEquals("option-a", optionA.getLongOpt());
 assertEquals(false, optionA.isRequired());
 assertEquals("Description for option A", optionA.getDescription());

 Option optionB = optionsList.get(1);
 assertEquals("b", optionB.getKey());
 assertTrue(optionB.hasArg());
 assertEquals("option-b", optionB.getLongOpt());
 assertEquals(false, optionB.isRequired());
 assertEquals("Description for option B", optionB.getDescription());

 Option optionC = optionsList.get(2);
 assertEquals("c", optionC.getKey());
 assertFalse(optionC.hasArg());
 assertEquals("option-c", optionC.getLongOpt());
 assertEquals(false, optionC.isRequired());
 assertEquals("Description for option C", optionC.getDescription());

 Option optionD = optionsList.get(3);
 assertEquals("d", optionD.getKey());
 assertTrue(optionD.hasArg());
 assertEquals("option-d", optionD.getLongOpt());
 assertEquals(false, optionD.isRequired());
 assertEquals("Description for option D", optionD.getDescription());
 }

 @Test
 public void testHasOption() {
 Options options = createTestOptions();
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("b"));
 assertTrue(options.hasOption("c"));
 assertTrue(options.hasOption("d"));

 assertTrue(options.hasLongOption("option-a"));
 assertTrue(options.hasLongOption("option-b"));
 assertTrue(options.hasLongOption("option-c"));
 assertTrue(options.hasLongOption("option-d"));

 assertTrue(options.hasShortOption("a"));
 assertTrue(options.hasShortOption("b"));
 assertTrue(options.hasShortOption("c"));
 assertTrue(options.hasShortOption("d"));

 // Test invalid option
 assertFalse(options.hasOption("x"));
 assertFalse(options.hasLongOption("x"));
 assertFalse(options.hasShortOption("x"));
 }

 @Test
 public void testGetOption() {
 Options options = createTestOptions();
 Option optionA = options.getOption("a");
 Option optionB = options.getOption("b");
 Option optionC = options.getOption("c");
 Option optionD = options.getOption("d");
 Option optionG1 = options.getOption("g1");
 Option optionG2 = options.getOption("g2");
 Option optionGroup = options.getOptionGroup("group");

 assertEquals(optionA, options.getOption("a"));
 assertEquals(optionB, options.getOption("b"));
 assertEquals(optionC, options.getOption("c"));
 assertEquals(optionD, options.getOption("d"));
 assertEquals(optionG1, options.getOption("g1"));
 assertEquals(optionG2, options.getOption("g2"));
 assertEquals(optionGroup, options.getOptionGroup("group"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createTestOptions();
 List<String> optionsList = options.getMatchingOptions("op");
 assertEquals(2, optionsList.size());
 assertEquals("option-c", optionsList.get(0));
 assertEquals("option-d", optionsList.get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateShortOption() {
 Options options = new Options();
 options.addOption("a", "option-a", false, "Description for option A");
 options.addOption("a", "option-a", false, "Description for option A");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateLongOption() {
 Options options = new Options();
 options.addOption("c", "option-c", false, "Description for option C");
 options.addOption("c", "option-c", false, "Description for option C");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithInvalidDescription() {
 Options options = new Options();
 options.addOption("e", "option-e", false, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithInvalidLongOpt() {
 Options options = new Options();
 options.addOption("f", null, false, "Description for option F");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGroupWithNullName() {
 Options options = new Options();
 OptionGroup group = new OptionGroup(null, "Options group");
 options.addOptionGroup(group);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGroupWithEmptyName() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("", "Options group");
 options.addOptionGroup(group);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGroupWithInvalidDescription() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", null);
 options.addOptionGroup(group);
 }

 @Test
 public void testAddRequiredOptionToGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Options group");
 Option option = new Option("r", "required-option", true, "Description for required option");
 group.addOption(option);
 options.addOptionGroup(group);

 assertTrue(options.hasOption("r"));
 assertTrue(options.getOption("r").isRequired());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddRequiredOptionDirectly() {
 Options options = new Options();
 Option option = new Option("r", "required-option", true, "Description for required option");
 options.addOption(option);
 }
}