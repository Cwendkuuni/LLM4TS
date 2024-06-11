package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "Option with alias a");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertEquals(option, options.getOption("a"));
 }

 @Test
 public void testAddRequiredOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "Option with alias a is required");
 options.addOption(option);
 List<Option> helpOptions = options.helpOptions();
 assertEquals(1, helpOptions.size());
 assertTrue(helpOptions.get(0).isRequired());
 }

 @Test(expected = ParseException.class)
 public void testAddConflictingOption() {
 Options options = new Options();
 Option optShort = new Option("a", "alias", false, "Option with short alias a");
 Option optLong = new Option("a", "another-alias", false, "Option with long alias a");
 options.addOption(optShort);
 options.addOption(optLong);
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group label");
 Option option = new Option("a", "option-a", false, "Option in group");
 group.addOption(option);
 options.addOptionGroup(group);
 assertEquals(1, options.getOptions().size());
 assertEquals(option, options.getOption("a"));
 }

 @Test
 public void testAddRequiredOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group label");
 group.setRequired(true);
 Option option = new Option("a", "option-a", false, "Option in group");
 group.addOption(option);
 options.addOptionGroup(group);
 List<Object> requiredOpts = options.getRequiredOptions();
 assertEquals(1, requiredOpts.size());
 assertTrue(requiredOpts.get(0) instanceof OptionGroup);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("l", "long-option1", false, "Long option 1");
 Option option2 = new Option("l", "long-option2", false, "Long option 2");
 options.addOption(option1);
 options.addOption(option2);
 List<String> matchingOptions = options.getMatchingOptions("l");
 assertEquals(2, matchingOptions.size());
 assertTrue(matchingOptions.contains("long-option1"));
 assertTrue(matchingOptions.contains("long-option2"));
 }
}