package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "An alias option");
 options.addOption(option);

 assertEquals("An alias option", options.getOption("a").getLongOpt());
 assertEquals("An alias option", options.getOption("a").getDescription());
 assertTrue(options.getOption("a").isRequired());
 assertTrue(options.getOption("a").isVisible());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "A group of options");
 Option option1 = new Option("g1", "group1", false, "Group option 1");
 Option option2 = new Option("g2", "group2", false, "Group option 2");
 group.addOption(option1);
 group.addOption(option2);
 options.addOptionGroup(group);

 assertEquals(2, options.getOptions().size());
 assertEquals("Group option 1", options.getOption("g1").getDescription());
 assertEquals("Group option 2", options.getOption("g2").getDescription());
 assertFalse(options.getOption("g1").isRequired());
 assertFalse(options.getOption("g2").isRequired());
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "An alias option");
 options.addOption(option);

 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("a", true));
 assertTrue(options.hasLongOption("alias"));
 assertFalse(options.hasOption("b"));
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionShortOpt() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "An alias option");
 options.addOption(option);

 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parse(), new String[]{"-a"});
 fail("Expected ParseException");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionShortOptValue() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "An alias option");
 options.addOption(option);

 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parse(), new String[]{"-a", "value"});
 fail("Expected ParseException");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionLongOpt() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "An alias option");
 options.addOption(option);

 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parse(), new String[]{"--alias"});
 fail("Expected ParseException");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionLongOptValue() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "An alias option");
 options.addOption(option);

 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parse(), new String[]{"--alias", "value"});
 fail("Expected ParseException");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionMissingRequiredOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "An alias option");
 options.addOption(option);
 options.addOptionGroup(new OptionGroup("group", "A group of options"));

 try {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parse(), new String[0]);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // expected
 }
 }
}