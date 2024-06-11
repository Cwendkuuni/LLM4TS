package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.util.ArgList;
import org.junit.Test;

public class ParserTest {

 DefaultParser parser = new DefaultParser();

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", 1, "Option C with an argument");
 options.addOption("d", 2, "Option D with an argument");
 options.addOption("e", int.class, "Option E with an integer argument");
 options.addOption("f", null, "Option F with no argument or value");
 options.addOption("g", 3, "Option G with an optional argument");

 CommandLine cmd = parser.parse(options, new String[]{"-a", "-b", "-c", "foo", "-d", "bar", "--e", "123", "--f", "--g", "qux"});

 assertEquals(5, cmd.getArgs().size());
 assertEquals("foo", cmd.getArgs().get(0));
 assertEquals("bar", cmd.getArgs().get(1));
 assertEquals("123", cmd.getOptionValues("e")[0]);
 assertNull(cmd.getOptionValues("h")[0]);
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 assertTrue(cmd.hasOption("c"));
 assertSame(options.findOption("c"), cmd.getOption("c"));
 assertSame(options.findOption("e"), cmd.getOption("e"));
 assertFalse(cmd.hasOption("h"));
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", 1, "Option C with an argument");
 options.addRequiredOption("d", "Option D is required");

 String[] args = new String[]{"-a", "-b", "-c", "--e", "123"};
 try {
 parser.parse(options, args);
 fail();
 } catch (ParseException e) {
 assertEquals("Option D is required", e.getMessage());
 }
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", 1, "Option C with an argument");

 String[] args = new String[]{"-a", "-b", "--c"};
 try {
 parser.parse(options, args);
 fail();
 } catch (ParseException e) {
 assertEquals("Missing argument for option: c", e.getMessage());
 }
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", 1, "Option C with an argument");

 String[] args = new String[]{"-x", "-a", "-b", "--c", "foo"};
 try {
 parser.parse(options, args);
 fail();
 } catch (ParseException e) {
 assertEquals("Unrecognized option: x", e.getMessage());
 }
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", 1, "Option C with an argument");
 options.addOption("d", 2, "Option D with an argument");
 options.addOption("e", int.class, "Option E with an integer argument");
 options.addOption("f", null, "Option F with no argument or value");
 options.addOption("g", 3, "Option G with an optional argument");

 Properties props = new Properties();
 props.setProperty("a", "1");
 props.setProperty("d", "qux");

 CommandLine cmd = parser.parse(options, new String[0], props);

 assertEquals(2, cmd.getArgs().size());
 assertEquals("qux", cmd.getOptionValues("d")[0]);
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("d"));
 assertFalse(cmd.hasOption("h"));
 }
}