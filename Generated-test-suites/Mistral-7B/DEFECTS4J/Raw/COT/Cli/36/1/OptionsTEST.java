package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options createOptions() {
 return new Options()
 .addOption("a", "optionalArgument", true, "some description")
 .addOption("b", true, "another description")
 .addOption("c", "noArgument", false, "no argument description")
 .addOptionGroup(new OptionGroup("group1", "group description", true)
 .addOption("d", "groupOption", false, "group option description")
 .addOption("e", "groupRequiredOption", true, "group required option description"));
 }

 @Test
 public void testAddOption() {
 Options options = createOptions();
 Option optionA = options.getOption("a");
 Assert.assertNotNull(optionA);
 }

 @Test
 public void testAddOptionWithShortAndLongName() {
 Options options = createOptions();
 Option optionB = options.getOption("b");
 Assert.assertNotNull(optionB);
 Assert.assertEquals("another description", optionB.getDescription());
 Assert.assertEquals('b', optionB.getOpt());
 Assert.assertFalse(optionB.hasArg());
 }

 @Test
 public void testAddOptionWithShortNameAndArgument() {
 Options options = createOptions();
 Option optionA = options.getOption("a");
 Assert.assertEquals("some description", optionA.getDescription());
 Assert.assertEquals('a', optionA.getOpt());
 Assert.assertTrue(optionA.hasArg());
 }

 @Test
 public void testAddOptionWithShortNameAndNoArgument() {
 Options options = createOptions()
 .addOption("c", "noArgument", false, "no argument description");
 Option optionC = options.getOption("c");
 Assert.assertEquals("no argument description", optionC.getDescription());
 Assert.assertEquals('c', optionC.getOpt());
 Assert.assertFalse(optionC.hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptions();
 OptionGroup optionGroup = options.getOptionGroup("group1");
 Assert.assertNotNull(optionGroup);
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateOption() {
 Options options = new Options();
 Option optionA = new Option("a", true, "description");
 options.addOption(optionA);
 try {
 options.addOption(optionA);
 } catch (IllegalArgumentException e) {
 // expected, as an Option cannot be added twice
 }
}

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullDescription() {
 Options options = new Options();
 try {
 options.addOption("nullOption", null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetOptions() {
 Options options = createOptions();
 List<Option> optionsList = options.getOptions();
 Assert.assertEquals(4, optionsList.size());
 Assert.assertTrue(optionsList.contains(options.getOption("a")));
 Assert.assertTrue(optionsList.contains(options.getOption("b")));
 Assert.assertTrue(optionsList.contains(options.getOption("c")));
 Assert.assertTrue(optionsList.contains(options.getOptionGroup("group1")));
 }

 @Test
 public void testHasOption() {
 Options options = createOptions();
 Assert.assertTrue(options.hasOption("a"));
 Assert.assertTrue(options.hasOption("b"));
 Assert.assertTrue(options.hasOption("c"));
 Assert.assertTrue(options.hasLongOption("optionalArgument"));
 Assert.assertTrue(options.hasShortOption("a"));
 Assert.assertFalse(options.hasShortOption("z"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptions();
 List<String> optionsList = options.getMatchingOptions("opt");
 Assert.assertEquals(1, optionsList.size());
 Assert.assertEquals("optionalArgument", optionsList.get(0));
 }

 @Test
 public void testGetOption() {
 Options options = createOptions();
 Option optionA = options.getOption("a");
 Option optionB = options.getOption("b");
 Option optionC = options.getOption("c");
 Option optionD = options.getOptionGroup("group1").getOption("d");
 Option optionE = options.getOptionGroup("group1").getOption("e");
 Assert.assertEquals(optionA, options.getOption("a"));
 Assert.assertEquals(optionB, options.getOption("b"));
 Assert.assertEquals(optionC, options.getOption("c"));
 Assert.assertEquals(optionD, options.getOption("d"));
 Assert.assertEquals(optionE, options.getOption("e"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionWithNonExistentOption() {
 Options options = createOptions();
 try {
 options.getOption("z");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetRequiredOptions() {
 Options options = createOptions();
 options.getRequiredOptions();
 }

 @Test
 public void testToString() {
 Options options = createOptions();
 Assert.assertEquals("[ Options: [ short {a=OPTIONAL_ARGUMENT}, c=NO_ARGUMENT}, long {} ], optionGroups={group1}]", options.toString());
 }
}