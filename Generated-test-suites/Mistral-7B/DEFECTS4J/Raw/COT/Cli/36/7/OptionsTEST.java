import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 private Options createTestOptions() {
 Options options = new Options();

 options.addOption("a", "test-option-a", true, "Test Option A");
 options.addOptionGroup(new OptionGroup("group1", "Test Group 1")
 .addOption("g1a", "test-option-g1a", true, "Test Group 1 Option A")
 .addOption("g1b", "test-option-g1b", false, "Test Group 1 Option B")
 .addOption("g1c", "test-option-g1c", false, "Test Group 1 Option C"));
 options.addOptionGroup(new OptionGroup("group2", "Test Group 2")
 .addOption("g2a", "test-option-g2a", false, "Test Group 2 Option A")
 .addOption("g2b", "test-option-g2b", false, "Test Group 2 Option B"));

 return options;
 }

 @Test
 public void testAddOption() {
 // Test adding an option with a short key
 Options options = createTestOptions();
 Option optionA = new Option("a", "test-option-a", true, "Test Option A");
 assertEquals(optionA, options.getOption("a"));
 assertTrue(options.hasOption("a"));

 // Test adding an option with a long key
 Option optionAL = new Option("test-long-option-a", "test-long-option-a", true, "Test Long Option A");
 options.addOption(optionAL);
 assertEquals(optionAL, options.getOption("test-long-option-a"));
 assertTrue(options.hasLongOption("test-long-option-a"));

 // Test adding an option to a group
 OptionGroup optionGroup = new OptionGroup("test-option-group", "Test Option Group")
 .addOption(new Option("tog-a", "test-option-group-a", true, "Test Option Group A"));
 options.addOptionGroup(optionGroup);
 Option optionTogA = new Option("tog-a", null, true, "Test Option Group A");
 assertEquals(optionTogA, options.getOption("tog-a"));
 assertTrue(options.hasOption("tog-a"));
 assertSame(optionGroup, options.getOptionGroup(optionTogA));
 }

 @Test
 public void testAddOptionGroup() {
 // Test adding an OptionGroup
 Options options = createTestOptions();
 OptionGroup optionGroup1 = new OptionGroup("group1", "Test Group 1")
 .addOption("g1a", "test-option-g1a", true, "Test Group 1 Option A")
 .addOption("g1b", "test-option-g1b", false, "Test Group 1 Option B")
 .addOption("g1c", "test-option-g1c", false, "Test Group 1 Option C");
 options.addOptionGroup(optionGroup1);

 Collection<OptionGroup> optionGroups = options.getOptionGroups();
 assertTrue(optionGroups.contains(optionGroup1));
 assertEquals(3, optionGroups.size());

 // Test adding an OptionGroup with the same name
 OptionGroup optionGroup2 = new OptionGroup("group1", "Test Group 1")
 .addOption("g1d", "test-option-g1d", true, "Test Group 1 Option D");
 options.addOptionGroup(optionGroup2);
 assertEquals(4, options.getOptions().size());
 assertEquals("group1", optionGroup2.getName());
 }

 @Test
 public void testGetOption() {
 Options options = createTestOptions();
 Option optionA = options.getOption("a");
 assertSame(optionA, options.getOption("test-option-a"));
 assertSame(optionA, options.getOption("--test-option-a"));
 assertNull(options.getOption("not-a-option"));
 assertNull(options.getOption("--not-a-option"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createTestOptions();
 List<String> matchingOptions = options.getMatchingOptions("g1");
 assertEquals(3, matchingOptions.size());
 assertEquals("g1a", matchingOptions.get(0));
 assertEquals("g1b", matchingOptions.get(1));
 assertEquals("g1c", matchingOptions.get(2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Options options = new Options();
 Option optionA = new Option("a", "test-option-a", true, "Test Option A");
 options.addOption(optionA);
 options.addOption(optionA);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateLongOption() {
 Options options = new Options();
 Option optionAL = new Option("test-long-option-a", "test-long-option-a", true, "Test Long Option A");
 options.addOption(optionAL);
 options.addOption(optionAL);
 }

 @Test
 public void testHasOption() {
 Options options = createTestOptions();
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("--test-option-a"));
 assertFalse(options.hasOption("not-a-option"));
 assertFalse(options.hasOption("--not-a-option"));
 }

 @Test
 public void testHasLongOption() {
 Options options = createTestOptions();
 assertTrue(options.hasLongOption("test-long-option-a"));
 assertFalse(options.hasLongOption("not-long-option"));
 }

 @Test
 public void testHasShortOption() {
 Options options = createTestOptions();
 assertTrue(options.hasShortOption("a"));
 assertFalse(options.hasShortOption("not-option"));
 }

 @Test
 public void testGetOptionGroup() {
 Options options = createTestOptions();
 OptionGroup optionGroup1 = new OptionGroup("group1", "Test Group 1")
 .addOption("g1a", "test-option-g1a", true, "Test Group 1 Option A")
 .addOption("g1b", "test-option-g1b", false, "Test Group 1 Option B")
 .addOption("g1c", "test-option-g1c", false, "Test Group 1 Option C");
 options.addOptionGroup(optionGroup1);
 Option optionG1A = new Option("g1a", null, true, "Test Group 1 Option A");
 assertEquals(optionGroup1, options.getOptionGroup(optionG1A));
 assertNull(options.getOptionGroup(new Option("not-option")));
 }

 @Test
 public void testToString() {
 Options options = createTestOptions();
 assertEquals("[ Options: [ short {a=}, test-long-option-a=}, long {} ]]\n", options.toString());
 }

}