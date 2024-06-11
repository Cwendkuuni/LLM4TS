package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {

 private static final String[] ARGS = new String[]{"--foo", "-a", "-b", "-c", "--foo=bar", "--", "-d", "-e", "--foo=qux"};

 @Test
 public void testFlatten() {
  Options options = new Options();
  options.addOption("foo", true, "f", "Prints foo");
  options.addOption("a", false, null, "Option A");
  options.addOption("b", false, null, "Option B");
  options.addOption("c", true, null, "Option C with no short option");

  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(options, ARGS, false);

  assertEquals(11, result.length);
  assertEquals("--foo", result[0]);
  assertEquals("-a", result[1]);
  assertEquals("-b", result[2]);
  assertEquals("-c", result[3]);
  assertEquals("--foo", result[4]);
  assertEquals("bar", result[5]);
  assertEquals("--", result[6]);
  assertEquals("-d", result[7]);
  assertEquals("-e", result[8]);
  assertEquals("--foo", result[9]);
  assertEquals("qux", result[10]);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
  Options options = new Options();
  options.addOption("foo", true, "f", "Prints foo");
  options.addOption("a", false, null, "Option A");
  options.addOption("b", false, null, "Option B");
  options.addOption("c", true, null, "Option C with no short option");

  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(options, ARGS, true);

  assertEquals(12, result.length);
  assertEquals("--foo", result[0]);
  assertEquals("-a", result[1]);
  assertEquals("-b", result[2]);
  assertEquals("-c", result[3]);
  assertEquals("--foo", result[4]);
  assertEquals("bar", result[5]);
  assertEquals("--", result[6]);
  assertEquals("-d", result[7]);
  assertEquals("-e", result[8]);
  assertEquals("--foo", result[9]);
  assertEquals("qux", result[10]);
  assertEquals("--", result[11]);
 }

 @Test
 public void testFlattenWithNoOptions() {
  PosixParser parser = new PosixParser();
  String[] args = new String[]{"--", "arg1", "--arg2=value", "--arg3"};
  String[] result = parser.flatten(null, args, false);

  assertEquals(4, result.length);
  assertEquals("--arg2", result[1]);
  assertEquals("value", result[2]);
 }

 @Test
 public void testFlattenWithEmptyArgs() {
  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(null, new String[0], false);

  assertEquals(0, result.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithNullOptions() {
  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(null, ARGS, false);
 }

 @Test
 public void testFlattenWithStopAtNonOptionAndNoOptions() {
  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(null, ARGS, true);

  assertEquals(13, result.length);
  assertEquals("--", result[0]);
  assertEquals("--", result[1]);
  for (String arg : ARGS) {
   if (!arg.startsWith("--")) {
    assertEquals(arg, result[2 + Arrays.asList(ARGS).indexOf(arg)]);
   }
  }
 }

 @Test
 public void testFlattenWithLongOption() {
  Options options = new Options();
  options.addOption("long-option", false, null, "Long option");

  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(options, new String[]{"--long-option"});

  assertEquals(1, result.length);
  assertEquals("--long-option", result[0]);
 }

 @Test
 public void testFlattenWithLongOptionAndValue() {
  Options options = new Options();
  options.addOption("long-option", true, null, "Long option with value");

  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(options, new String[]{"--long-option=value"});

  assertEquals(1, result.length);
  assertEquals("--long-option=value", result[0]);
 }

 @Test
 public void testFlattenWithShortOption() {
  Options options = new Options();
  options.addOption("a", false, null, "Option A");

  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(options, new String[]{"-a"});

  assertEquals(1, result.length);
  assertEquals("-a", result[0]);
 }

 @Test
 public void testFlattenWithShortOptionAndValue() {
  Options options = new Options();
  options.addOption("a", true, null, "Option A with value");

  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(options, new String[]{"-a=value"});

  assertEquals(1, result.length);
  assertEquals("-a=value", result[0]);
 }

 @Test
 public void testFlattenWithShortOptionAndLongOption() {
  Options options = new Options();
  options.addOption("a", false, null, "Option A");
  options.addOption("long-option", false, null, "Long option");

  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(options, new String[]{"-a", "--long-option"});

  assertEquals(2, result.length);
  assertEquals("-a", result[0]);
  assertEquals("--long-option", result[1]);
 }

 @Test
 public void testFlattenWithLongOptionAndShortOption() {
  Options options = new Options();
  options.addOption("long-option", false, null, "Long option");
  options.addOption("a", false, null, "Option A");

  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(options, new String[]{"--long-option", "-a"});

  assertEquals(2, result.length);
  assertEquals("--long-option", result[0]);
  assertEquals("-a", result[1]);
 }

 @Test
 public void testFlattenWithShortOptionWithValueAndLongOption() {
  Options options = new Options();
  options.addOption("a", true, null, "Option A with value");
  options.addOption("long-option", false, null, "Long option");

  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(options, new String[]{"-a=value", "--long-option"});

  assertEquals(2, result.length);
  assertEquals("-a=value", result[0]);
  assertEquals("--long-option", result[1]);
 }

 @Test
 public void testFlattenWithShortOptionWithValueAndShortOption() {
  Options options = new Options();
  options.addOption("a", true, null, "Option A with value");
  options.addOption("b", false, null, "Option B");

  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(options, new String[]{"-a=value", "-b"});

  assertEquals(2, result.length);
  assertEquals("-a=value", result[0]);
  assertEquals("-b", result[1]);
 }

 @Test
 public void testFlattenWithLongOptionWithArg() {
  Options options = new Options();
  options.addOption("long-option", true, null, "Long option with argument");

  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(options, new String[]{"--long-option", "arg"});

  assertEquals(2, result.length);
  assertEquals("--long-option", result[0]);
  assertEquals("arg", result[1]);
 }

 @Test
 public void testFlattenWithLongOptionWithArgAndValue() {
  Options options = new Options();
  options.addOption("long-option", true, null, "Long option with argument and value");

  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(options, new String[]{"--long-option=arg", "value"});

  assertEquals(2, result.length);
  assertEquals("--long-option=arg", result[0]);
  assertEquals("value", result[1]);
 }

 @Test
 public void testFlattenWithLongOptionWithArgAndValueAndShortOption() {
  Options options = new Options();
  options.addOption("long-option", true, null, "Long option with argument and value");
  options.addOption("a", false, null, "Option A");

  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(options, new String[]{"--long-option=arg", "value", "-a"});

  assertEquals(3, result.length);
  assertEquals("--long-option=arg", result[0]);
  assertEquals("value", result[1]);
  assertEquals("-a", result[2]);
 }

 @Test
 public void testFlattenWithLongOptionWithArgAndValueAndShortOptionWithArg() {
  Options options = new Options();
  options.addOption("long-option", true, null, "Long option with argument and value");
  options.addOption("a", true, "a", "Option A with argument");

  PosixParser parser = new PosixParser();
  String[] result = parser.flatten(options, new String[]{"--long-option=arg", "value", "-a=arg2"});

  assertEquals(3, result.length);
  assertEquals("--long-option=arg", result[0]);
  assertEquals("value", result[1]);
  assertEquals("-a=arg2", result[2]);
 }
}