package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class OptionsTest {

 private Options createOptions() {
 Options options = new Options();
 options.addOption("o", "opt", false, "Option description");
 options.addOption("l", "longopt", true, "Long option description");
 options.addOptionGroup(new OptionGroup("g", "group", true, "Option group description"));
 options.addOption("a", "arg", true, "Option with argument description");
 options.addOption("r", "required", true, "Required option");
 return options;
 }

 @Test
 public void testAddOptionGroup() {
 Options options = createOptions();
 OptionGroup group = new OptionGroup("g", "group", true, "Option group description");
 List<Option> optionsInGroup = new ArrayList<>();
 optionsInGroup.add(new Option("i", "input", true, "Input file"));
 group.setOptions(optionsInGroup);
 options.addOptionGroup(group);

 assertEquals(1, options.getOptionGroups().size());
 assertSame(group, options.getOptionGroups().iterator().next());
 }

 @Test
 public void testGetOptionGroups() {
 Options options = createOptions();
 OptionGroup group1 = new OptionGroup("g1", "group1", true, "Group 1");
 OptionGroup group2 = new OptionGroup("g2", "group2", true, "Group 2");
 options.addOptionGroup(group1);
 options.addOptionGroup(group2);

 List<OptionGroup> optionGroups = options.getOptionGroups();
 assertTrue(optionGroups.contains(group1));
 assertTrue(optionGroups.contains(group2));
 }

 @Test
 public void testGetOptionWithShortName() {
 Options options = createOptions();
 Option option1 = new Option("o", "opt", false, "Option description");
 Option option2 = new Option("l", "longopt", true, "Long option description");
 options.addOption(option1);
 options.addOption(option2);

 Option option = options.getOption("o");
 assertEquals(option1, option);
 }

 @Test
 public void testGetOptionWithLongName() {
 Options options = createOptions();
 Option option1 = new Option("o", "opt", false, "Option description");
 Option option2 = new Option("longopt", null, true, "Long option description");
 options.addOption(option1);
 options.addOption(option2);

 Option option = options.getOption("longopt");
 assertEquals(option2, option);
 }

 @Test
 public void testGetOptionWithNonExistentName() {
 Options options = createOptions();

 assertNull(options.getOption("nonexistent"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = createOptions();
 options.addOption("opt1", null, false, "Option 1");
 options.addOption("opt2", null, false, "Option 2");
 options.addOption("opt3", null, false, "Option 3");

 List<String> matchingOptions = options.getMatchingOptions("opt");
 assertEquals(2, matchingOptions.size());
 assertTrue(matchingOptions.containsAll(Arrays.asList("opt1", "opt3")));
 }

 @Test
 public void testHasOption() {
 Options options = createOptions();
 Option option1 = new Option("o", "opt", false, "Option description");
 Option option2 = new Option("l", "longopt", true, "Long option description");
 options.addOption(option1);
 options.addOption(option2);

 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("l"));
 }

 @Test
 public void testHasLongOption() {
 Options options = createOptions();
 Option option1 = new Option("o", "opt", false, "Option description");
 Option option2 = new Option("longopt", null, true, "Long option description");
 options.addOption(option1);
 options.addOption(option2);

 assertTrue(options.hasLongOption("longopt"));
 assertFalse(options.hasLongOption("nonexistent"));
 }

 @Test
 public void testHasShortOption() {
 Options options = createOptions();
 Option option1 = new Option("o", "opt", false, "Option description");
 Option option2 = new Option("l", "longopt", true, "Long option description");
 options.addOption(option1);
 options.addOption(option2);

 assertTrue(options.hasShortOption("o"));
 assertFalse(options.hasShortOption("nonexistent"));
 }

 @Test
 public void testGetOptionGroup() {
 Options options = createOptions();
 OptionGroup group = new OptionGroup("g", "group", true, "Option group description");
 options.addOptionGroup(group);
 Option option = new Option("i", "input", true, "Input file");
 group.addOption(option);

 OptionGroup groupRetrieved = options.getOptionGroup(option);
 assertEquals(group, groupRetrieved);
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = createOptions();
 options.addOption("o", "opt", false, "Option description");
 options.addOption("l", "longopt", true, "Long option description");
 options.addOption("r", "required", true, "Required option");

 List<Option> requiredOptions = options.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 assertEquals("required", requiredOptions.get(0).getKey());
 }

 @Test
 public void testToString() {
 Options options = createOptions();
 String expected = "[ Options: [ short {o=OPTION, opt=Option description}, l=LONGOPT, longopt=Long option description}, long {--longopt=Long option description}]";
 assertEquals(expected, options.toString());
 }
}