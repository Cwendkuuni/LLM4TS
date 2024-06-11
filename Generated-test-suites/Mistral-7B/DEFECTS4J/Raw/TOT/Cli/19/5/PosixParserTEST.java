package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

/**
 * Testing class for PosixParser class.
 */
public class PosixParserTest {

 /**
  * Basic test case for PosixParser.flatten() method.
  */
 @Test
 public void testBasic() {
  // setup
  String[] args = {"--foo=bar", "--baz", "-c", "-d=value", "arg1", "-e", "-f arg2"};
  Options options = new Options();
  Option fooOption = new Option("f", "foo", true, "A test option");
  Option barOption = new Option("b", "baz", false, "Another test option");
  Option cOption = new Option("c", "consume", false, "An option to consume the rest of the arguments");
  Option dOption = new Option("d", "data", true, "An option with an argument");
  Option eOption = new Option("e", "echo", false, "An option to print its argument");
  Option fOption = new Option("f", "flag", false, "A flag option");

  options.addOption(fooOption);
  options.addOption(barOption);
  options.addOption(cOption);
  options.addOption(dOption);
  options.addOption(eOption);
  options.addOption(fOption);

  PosixParser parser = new PosixParser();
  try {
   String[] tokens = parser.flatten(options, args, false);
   // verify
   assertArrayEquals(new String[]{"--foo", "bar", "--baz", "-c", "-d", "value", "arg1", "-e", "arg2"}, tokens);
  } catch (ParseException e) {
   // should not reach here
   e.printStackTrace();
  }
 }

 /**
  * Testing flatten() method when stopAtNonOption is set to true.
  */
 @Test
 public void testStopAtNonOption() {
  // setup
  String[] args = {"--foo=bar", "--baz", "-c", "arg1", "arg2", "-e", "-f arg3"};
  Options options = new Options();
  Option fooOption = new Option("f", "foo", true, "A test option");
  Option barOption = new Option("b", "baz", false, "Another test option");
  Option cOption = new Option("c", "consume", false, "An option to consume the rest of the arguments");
  Option eOption = new Option("e", "echo", false, "An option to print its argument");

  options.addOption(fooOption);
  options.addOption(barOption);
  options.addOption(cOption);

  PosixParser parser = new PosixParser();
  try {
   String[] tokens = parser.flatten(options, args, true);
   // verify
   assertArrayEquals(new String[]{"--foo", "bar", "--baz", "arg1", "arg2", "-e", "-f", "arg3"}, tokens);
  } catch (ParseException e) {
   // should not reach here
   e.printStackTrace();
  }
 }

 /**
  * Testing flatten() method when encountering an unknown option.
  */
 @Test
 public void testUnknownOption() {
  // setup
  String[] args = {"--foo=bar", "-x", "--baz", "-c", "arg1", "arg2", "-e", "-f arg3"};
  Options options = new Options();
  Option fooOption = new Option("f", "foo", true, "A test option");
  Option barOption = new Option("b", "baz", false, "Another test option");
  Option cOption = new Option("c", "consume", false, "An option to consume the rest of the arguments");
  Option eOption = new Option("e", "echo", false, "An option to print its argument");

  options.addOption(fooOption);
  options.addOption(barOption);
  options.addOption(cOption);

  PosixParser parser = new PosixParser();
  try {
   String[] tokens = parser.flatten(options, args, false);
   // verify
   assertArrayEquals(new String[]{"--foo", "bar", "--baz", "-x", "arg1", "arg2", "-e", "-f", "arg3"}, tokens);
   assertNull(parser.currentOption);
  } catch (ParseException e) {
   // should not reach here
   e.printStackTrace();
  }
 }

 /**
  * Testing flatten() method when encountering an option with no arguments.
  */
 @Test
 public void testNoArgumentOption() {
  // setup
  String[] args = {"--foo", "--baz", "-c", "arg1", "arg2", "-e", "-f arg3"};
  Options options = new Options();
  Option fooOption = new Option("f", "foo", false, "A test option without an argument");
  Option barOption = new Option("b", "baz", false, "Another test option");
  Option cOption = new Option("c", "consume", false, "An option to consume the rest of the arguments");
  Option eOption = new Option("e", "echo", false, "An option to print its argument");

  options.addOption(fooOption);
  options.addOption(barOption);
  options.addOption(cOption);

  PosixParser parser = new PosixParser();
  try {
   String[] tokens = parser.flatten(options, args, false);
   // verify
   assertArrayEquals(new String[]{"--foo", "--baz", "-c", "arg1", "arg2", "-e", "-f", "arg3"}, tokens);
   assertNull(parser.currentOption);
  } catch (ParseException e) {
   // should not reach here
   e.printStackTrace();
  }
 }

 /**
  * Testing flatten() method when encountering an option with an argument.
  */
 @Test
 public void testArgumentOption() {
  // setup
  String[] args = {"--foo=bar", "--baz", "-c", "arg1", "arg2", "-e", "-f arg3"};
  Options options = new Options();
  Option fooOption = new Option("f", "foo", true, "A test option with an argument");
  Option barOption = new Option("b", "baz", false, "Another test option");
  Option cOption = new Option("c", "consume", false, "An option to consume the rest of the arguments");
  Option eOption = new Option("e", "echo", false, "An option to print its argument");

  options.addOption(fooOption);
  options.addOption(barOption);
  options.addOption(cOption);

  PosixParser parser = new PosixParser();
  try {
   String[] tokens = parser.flatten(options, args, false);
   // verify
   assertArrayEquals(new String[]{"--foo", "bar", "--baz", "-c", "arg1", "arg2", "-e", "-f", "arg3"}, tokens);
   assertSame(fooOption, parser.currentOption);
  } catch (ParseException e) {
   // should not reach here
   e.printStackTrace();
  }
 }

 /**
  * Testing flatten() method when encountering an option with multiple arguments.
  */
 @Test
 public void testMultipleArgumentsOption() {
  // setup
  String[] args = {"--foo=bar1,bar2", "--baz", "-c", "arg1", "arg2", "-e", "-f arg3,arg4"};
  Options options = new Options();
  Option fooOption = new Option("f", "foo", true, "A test option with multiple arguments");
  Option barOption = new Option("b", "baz", false, "Another test option");
  Option cOption = new Option("c", "consume", false, "An option to consume the rest of the arguments");
  Option eOption = new Option("e", "echo", false, "An option to print its argument");

  options.addOption(fooOption);
  options.addOption(barOption);
  options.addOption(cOption);

  PosixParser parser = new PosixParser();
  try {
   String[] tokens = parser.flatten(options, args, false);
   // verify
   assertArrayEquals(new String[]{"--foo", "bar1,bar2", "--baz", "-c", "arg1", "arg2", "-e", "-f", "arg3,arg4"}, tokens);
   assertSame(fooOption, parser.currentOption);
  } catch (ParseException e) {
   // should not reach here
   e.printStackTrace();
  }
 }

 /**
  * Testing flatten() method when encountering an option with no argument and consume rest.
  */
 @Test
 public void testConsumeRestOption() {
  // setup
  String[] args = {"--foo", "--consume-rest", "arg1", "arg2", "arg3"};
  Options options = new Options();
  Option fooOption = new Option("f", "foo", false, "A test option without an argument");
  Option consumeRestOption = new Option("C", "consume-rest", false, "An option to consume the rest of the arguments");

  options.addOption(fooOption);
  options.addOption(consumeRestOption);

  PosixParser parser = new PosixParser();
  try {
   String[] tokens = parser.flatten(options, args, false);
   // verify
   assertArrayEquals(new String[]{"--foo", "--consume-rest", "arg1", "arg2", "arg3"}, tokens);
   assertNull(parser.currentOption);
  } catch (ParseException e) {
   // should not reach here
   e.printStackTrace();
  }
 }

}