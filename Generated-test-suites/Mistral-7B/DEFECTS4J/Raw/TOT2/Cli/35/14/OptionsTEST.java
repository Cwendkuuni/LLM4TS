package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "Alias option.");
 options.addOption(option);
 assertEquals("Alias option.", options.getOption("a").getDescription());
 }

 @Test
 public void testAddOptionWithArgument() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "Alias option with argument.");
 options.addOption(option);
 assertEquals("Alias option with argument.", options.getOption("a").getDescription());
 assertEquals(1, options.getOption("a").getArgPosition());
 }

 @Test
 public void testAddOptionWithLongName() {
 Options options = new Options();
 Option option = new Option("alias", "longAlias", false, "Long alias option.");
 options.addOption(option);
 assertEquals("Long alias option.", options.getOption("--longAlias").getDescription());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group");
 Option option = new Option("g", "groupOption", false, "Group option.", group);
 options.addOptionGroup(group);
 assertEquals("Group option.", options.getOption("g").getDescription());
 assertEquals("Group", options.getOptionGroup("group").getName());
 }

 @Test
 public void testAddRequiredOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "Alias option.");
 options.addOption(option);
 options.addOption(option);
 options.addOption(Option.builder("b").required(true).build());

 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a", "--b"};
 try {
 parser.parse(options, args);
 fail("Expected ParseException.");
 } catch (ParseException e) {
 // expected
 }
 assertNull(parser.getParsedOptions().getOption("a"));
 assertSame(options.getOption("b"), parser.getParsedOptions().getOption("b"));
 }

 @Test
 public void testGetOptions() {
 Options options = new Options();
 Option option1 = new Option("a", "alias1", false, "Alias option 1.");
 Option option2 = new Option("b", "alias2", false, "Alias option 2.");
 options.addOption(option1);
 options.addOption(option2);
 List<Option> optionsList = options.getOptions();
 assertEquals(2, optionsList.size());
 assertEquals(option1, optionsList.get(0));
 assertEquals(option2, optionsList.get(1));
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = new Options();
 Option option1 = new Option("a", "alias1", false, "Alias option 1.");
 Option option2 = new Option("b", "alias2", false, "Alias option 2.");
 Option option3 = new Option("c", "alias3", false, "Alias option 3.", null, true, "group");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 options.addOptionGroup(new OptionGroup("group", "Group"));
 List<Option> requiredOptionsList = options.getRequiredOptions();
 assertEquals(1, requiredOptionsList.size());
 assertFalse(requiredOptionsList.get(0).isRequired());
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "Alias option.");
 options.addOption(option);
 Option result = options.getOption("a");
 assertSame(option, result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetOptionNull() {
 Options options = new Options();
 options.getOption(null);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("option1", "l", false, "Option 1.");
 Option option2 = new Option("option2", "l", false, "Option 2.");
 options.addOption(option1);
 options.addOption(option2);
 List<String> result = options.getMatchingOptions("l");
 assertEquals(2, result.size());
 assertEquals("option1", result.get(0));
 assertEquals("option2", result.get(1));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "Alias option.");
 options.addOption(option);
 assertTrue(options.hasOption("a"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "Alias option.");
 options.addOption(option);
 assertTrue(options.hasLongOption("alias"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "Alias option.");
 options.addOption(option);
 assertTrue(options.hasShortOption("a"));
 }

 @Test
 public void testHasNoOption() {
 Options options = new Options();
 assertFalse(options.hasOption("nonexistent"));
 }

 @Test
 public void testToString() {
 Options options = new Options();
 Option option1 = new Option("a", "alias1", false, "Alias option 1.");
 Option option2 = new Option("b", "alias2", false, "Alias option 2.");
 options.addOption(option1);
 options.addOption(option2);
 assertEquals("[ Options: [ short {a=Alias option 1.} ], [ long {} ] ]", options.toString());
 }

}