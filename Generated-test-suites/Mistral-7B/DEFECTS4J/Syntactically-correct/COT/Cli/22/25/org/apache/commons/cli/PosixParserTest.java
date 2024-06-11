package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

 private static final String TEST_ARGUMENTS = "--test -a -b arg1 -c arg2 -d arg3 -e arg4 -f -g arg5 --";

 @Test
 public void testFlatten() {
  Options options = new Options();

  options.addOption("a", true, "option A");
  options.addOption("b", true, "option B");
  options.addOption("c", true, "option C");
  options.addOption("d", true, "option D");
  options.addOption("e", true, "option E");
  options.addOption("f", false, "option F");
  options.addOption("g", true, "option G");

  Parser parser = new PosixParser();
  String[] args = parser.parse(options, TEST_ARGUMENTS.split(" "));

  List<String> expected = Arrays.asList("--test", "-a", "true", "-b", "arg1", "-c", "arg2", "-d", "arg3", "-e", "arg4", "-f", "--", "-g", "arg5");
  Assert.assertArrayEquals(expected.toArray(new String[0]), args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOption() {
  Options options = new Options();

  Parser parser = new PosixParser();
  String[] args = parser.flatten(options, new String[]{"--invalid"}, false);
  Assert.fail();
 }

 @Test
 public void testStopAtNonOption() {
  Options options = new Options();

  options.addOption("a", true, "option A");
  options.addOption("b", true, "option B");

  Parser parser = new PosixParser();
  String[] args = parser.flatten(options, new String[]{"--test", "-a", "-b", "nonOption"}, true);

  List<String> expected = Arrays.asList("--test", "-a", "-b", "nonOption");
  Assert.assertArrayEquals(expected.toArray(new String[0]), args);
 }

 @Test
 public void testBurstTokenSingleCharacterOption() {
  Options options = new Options();

  options.addOption("a", true, "option A");
  options.addOption("b", true, "option B");

  Parser parser = new PosixParser();
  String[] args = parser.flatten(options, new String[]{"--test", "-a", "-abc"}, false);

  List<String> expected = Arrays.asList("--test", "-a", "--", "-b", "c");
  Assert.assertArrayEquals(expected.toArray(new String[0]), args);
 }

 @Test
 public void testBurstTokenLongOption() {
  Options options = new Options();

  options.addOption("abcd", true, "longOptionA");

  Parser parser = new PosixParser();
  String[] args = parser.flatten(options, new String[]{"--test", "--abcd", "arg1"}, false);

  List<String> expected = Arrays.asList("--test", "--abcd", "arg1");
  Assert.assertArrayEquals(expected.toArray(new String[0]), args);
 }

 @Test
 public void testBurstTokenLongOptionWithArg() {
  Options options = new Options();

  options.addOption("abcd", true, "longOptionA");
  options.addOption("efg", true, "longOptionB");

  Parser parser = new PosixParser();
  String[] args = parser.flatten(options, new String[]{"--test", "--abcd=arg1", "--efg=arg2"}, false);

  List<String> expected = Arrays.asList("--test", "--abcd", "arg1", "--", "--efg", "arg2");
  Assert.assertArrayEquals(expected.toArray(new String[0]), args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBurstTokenInvalidOption() {
  Options options = new Options();

  Parser parser = new PosixParser();
  String[] args = parser.flatten(options, new String[]{"--test", "-i"}, false);
  Assert.fail();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBurstTokenNoOption() {
  Options options = new Options();

  Parser parser = new PosixParser();
  String[] args = parser.flatten(options, new String[]{"--test", "nonOption"}, false);
  Assert.fail();
 }

}