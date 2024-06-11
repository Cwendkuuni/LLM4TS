package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 @Test
 public void testBasicUsage() {
 Options options = new Options();
 options.addOption("a", "anOption", true, "An option with a boolean value");
 options.addOption("b", "anOptionWithValue", false, "An option without a value");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-a", "true", "-b"});

 Assert.assertTrue(cmd.hasOption("a"));
 Assert.assertTrue(cmd.getOptionValue("a").equals("true"));
 Assert.assertFalse(cmd.hasOption("b"));
 } catch (ParseException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testLongOptionUsage() {
 Options options = new Options();
 options.addOption("a", "anOption", true, "An option with a boolean value");
 options.addOption("l-anOptionWithValue", "anOptionWithValue", false, "An option with a long name");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"--anOptionWithValue"});

 Assert.assertFalse(cmd.hasOption("a"));
 Assert.assertNull(cmd.getOptionValue("a"));
 Assert.assertTrue(cmd.hasOption("l-anOptionWithValue"));
 } catch (ParseException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testMissingOptionUsage() {
 Options options = new Options();
 options.addOption("a", "anOption", true, "An option with a boolean value");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"--missingOption"});
 Assert.fail();
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testMissingValueOptionUsage() {
 Options options = new Options();
 options.addOption("a", "anOption", true, "An option with a boolean value");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-a"});
 Assert.fail();
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testOptionInOptionGroupUsage() {
 Options options = new Options();
 Options group = new Options();
 group.addOption("g", "groupOption", false, "A group option");
 group.addOption("g1", "groupOption1", true, "A group option with a value");

 options.addOptionGroup(group);
 options.addOption("a", "anOption", true, "An option with a boolean value");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"--groupOption", "-g"});

 Assert.assertTrue(cmd.hasOption("g"));
 Assert.assertTrue(cmd.hasOption("g1"));
 Assert.assertFalse(cmd.hasOption("a"));
 } catch (ParseException e) {
 e.printStackTrace();
 }
 }
}