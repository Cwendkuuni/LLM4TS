package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private String[] args = {"-a", "1", "--b", "2", "--c=3", "--d", "--e=4", "--f", "-g"};

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A with no argument");
 options.addOption("b", true, "Option B with no argument");
 options.addOption("c", true, "Option C with argument");
 options.addOption("d", false, "Option D with no argument");
 options.addOption("e", true, "Option E with argument");
 options.addOption("f", false, "Option F with no argument");
 options.addOption("g", false, "Option G with no argument");

 DefaultParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, args, false);

 // check parsed options
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 assertTrue(cmd.hasOption("c"));
 assertTrue(cmd.getOptionValue("c").equals("1"));
 assertTrue(cmd.hasOption("d"));
 assertFalse(cmd.hasOption("e"));
 assertNull(cmd.getOptionValue("e"));
 assertFalse(cmd.hasOption("f"));
 assertFalse(cmd.hasOption("g"));

 // check parsed arguments
 List<String> argsList = cmd.getArgs();
 assertEquals(3, argsList.size());
 assertEquals("2", argsList.get(0));
 assertEquals("3", argsList.get(1));
 assertEquals("4", argsList.get(2));
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("a", true, "Required Option A with no argument");

 DefaultParser parser = new DefaultParser();
 String[] args = {"--b"};
 parser.parse(options, args, false);
 fail("Expected ParseException when required option is missing");
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A with no argument");
 options.addOption("b", true, "Option B with no argument");
 options.addOption("c", true, "Option C with argument");
 options.addOption("d", false, "Option D with no argument");
 options.addOption("e", true, "Option E with argument");
 options.addOption("f", false, "Option F with no argument");
 options.addOption("g", false, "Option G with no argument");
 options.addOption("h", true, "Invalid Option H with no argument"); // should not be present in Options

 DefaultParser parser = new DefaultParser();
 String[] args = {"-h"};
 try {
 parser.parse(options, args, false);
 fail("Expected ParseException when invalid option is encountered");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testMissingValueForOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A with no argument");
 options.addOption("b", true, "Option B with no argument");
 options.addOption("c", true, "Option C with required argument");

 DefaultParser parser = new DefaultParser();
 String[] args = {"-a", "--c"};
 try {
 parser.parse(options, args, false);
 fail("Expected ParseException when required argument for option C is missing");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A with no argument");
 options.addOption("b", true, "Option B with no argument");
 options.addOption("c", true, "Option C with argument");
 options.addOption("d", false, "Option D with no argument");
 options.addOption("e", true, "Option E with argument");
 options.addOption("f", false, "Option F with no argument");
 options.addOption("g", false, "Option G with no argument");

 Properties prop = new Properties();
 prop.setProperty("a", "1");
 prop.setProperty("c", "2");
 prop.setProperty("e", "4");

 CommandLine cmd = new DefaultParser().parse(options, args, prop, false);

 // check parsed options
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 assertTrue(cmd.hasOption("c"));
 assertTrue(cmd.getOptionValue("c").equals("2"));
 assertTrue(cmd.hasOption("d"));
 assertFalse(cmd.hasOption("e"));
 assertNull(cmd.getOptionValue("e"));
 assertFalse(cmd.hasOption("f"));
 assertFalse(cmd.hasOption("g"));

 // check parsed arguments
 List<String> argsList = cmd.getArgs();
 assertEquals(3, argsList.size());
 assertEquals("2", argsList.get(0));
 assertEquals("4", argsList.get(1));
 assertEquals("", argsList.get(2));
 }
}