package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class OptionsTest {

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("testGroup", "Test Group");
 options.addOptionGroup(optionGroup);
 assertEquals(1, options.getOptionGroups().size());
 }

 @Test
 public void testGetOptionGroups() {
 Options options = new Options();
 OptionGroup optionGroup1 = new OptionGroup("group1", "Group 1");
 OptionGroup optionGroup2 = new OptionGroup("group2", "Group 2");
 options.addOptionGroup(optionGroup1);
 options.addOptionGroup(optionGroup2);
 List<OptionGroup> optionGroups = options.getOptionGroups();
 assertEquals(2, optionGroups.size());
 assertEquals(optionGroup1, optionGroups.get(0));
 assertEquals(optionGroup2, optionGroups.get(1));
 }

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option option = new Option("opt", "opt desc", true);
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithArg() {
 Options options = new Options();
 Option option = new Option("opt", "opt desc", true, "arg");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertEquals(option, options.getOptions().get(0));
 assertEquals("arg", option.getArgName());
 }

 @Test
 public void testAddOptionWithShortArg() {
 Options options = new Options();
 Option option = new Option("o", "opt desc", true, "arg");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertEquals(option, options.getOptions().get(0));
 assertEquals("arg", option.getArgName());
 }

 @Test
 public void testAddOptionWithNoArg() {
 Options options = new Options();
 Option option = new Option("o", "opt desc");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertNull(option.getArgName());
 }

 @Test
 public void testAddOptionWithShortAndLongNames() {
 Options options = new Options();
 Option option = new Option("o", "longOpt", false, "arg");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertEquals(option, options.getOptions().get(0));
 assertEquals("longOpt", option.getLongOpt());
 assertEquals("o", option.getKey());
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option option = new Option("o", "opt desc");
 options.addOption(option);
 assertEquals(option, options.getOption("o"));
 assertEquals(option, options.getOption("opt desc"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("opt1", "Opt 1");
 Option option2 = new Option("opt2", "Opt 2");
 Option option3 = new Option("opta", "Opt A");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 List<String> optionsWithName = options.getMatchingOptions("opt");
 assertEquals(2, optionsWithName.size());
 assertEquals(option1.getKey(), optionsWithName.get(0));
 assertEquals(option2.getKey(), optionsWithName.get(1));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("o", "opt desc");
 options.addOption(option);
 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("opt desc"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option option = new Option("o", "longOpt", false, "arg");
 options.addOption(option);
 assertTrue(options.hasLongOption("longOpt"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option option = new Option("o", "opt desc");
 options.addOption(option);
 assertTrue(options.hasShortOption("o"));
 }

 @Test
 public void testGetOptionGroup() {
 Options options = new Options();OptionGroup optionGroup = new OptionGroup("testGroup", "Test Group");
 Option option = new Option("o", "opt desc", true, "arg");
 option.setOptionGroup(optionGroup);
 options.addOption(option);
 OptionGroup returnedGroup = options.getOptionGroup(option);
 assertEquals(optionGroup, returnedGroup);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Options options = new Options();
 Option option = new Option("o", "opt desc");
 options.addOption(option);
 options.addOption(option);
 }

 @Test
 public void testToString() {
 Options options = new Options();
 Option option1 = new Option("o1", "opt1 desc");
 Option option2 = new Option("o2", "opt2 desc", true);
 Option option3 = new Option("o3", "opt3 desc", true, "arg3");
 Option option4 = new Option("o4", "opt4 desc", false);
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 options.addOption(option4);
 String stringRepresentation = options.toString();
 String expectedStringRepresentation = "[ Options: [ short {o1=option1 desc, o2=, o3=arg3, o4=} ] [ long {o1=opt1 desc, o2=, o3=opt3 desc, o4=opt4 desc} ]]";
 assertEquals(expectedStringRepresentation, stringRepresentation);
 }
}