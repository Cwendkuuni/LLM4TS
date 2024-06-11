package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testSimpleParse() throws ParseException {
 Options options = new Options();
 options.addOption("a", "arg1", true, "Argument 1");
 options.addOption("b", "arg2", true, "Argument 2");
 Parser parser = new DefaultParser();
 String[] args = {"--arg1", "1", "--arg2", "2"};
 CommandLine cmd = parser.parse(options, args, null);
 assertEquals("1", cmd.getOptionValue("arg1"));
 assertEquals("2", cmd.getOptionValue("arg2"));
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", "arg1", true, "Argument 1");
 Parser parser = new DefaultParser();
 String[] args = {"--unrecognizedOption", "1"};
 parser.parse(options, args, null);
 fail("Unrecognized option: unrecognizedOption");
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 Parser parser = new DefaultParser();
 String[] args = {};
 try {
 parser.parse(options, args, null);
 fail("Missing required option: a");
 } catch (MissingOptionException e) {
 // expected
 }
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 Parser parser = new DefaultParser();
 String[] args = {"--a"};
 try {
 parser.parse(options, args, null);
 fail("Missing argument for option: b");
 } catch (MissingArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 Parser parser = new DefaultParser();
 Properties properties = new Properties();
 properties.setProperty("a", "valueA");
 CommandLine cmd = parser.parse(options, null, properties, false);
 assertEquals("valueA", cmd.getOptionValue("a"));
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 Parser parser = new DefaultParser();
 String[] args = {"--arg1", "--", "arg3", "--arg2", "arg2"};
 CommandLine cmd = parser.parse(options, args, null, true);
 assertEquals("arg3", cmd.getArg(0));
 assertEquals("arg2", cmd.getArg(1));
 }
}