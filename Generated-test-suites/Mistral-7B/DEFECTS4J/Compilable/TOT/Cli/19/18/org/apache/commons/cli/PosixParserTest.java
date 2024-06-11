package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 private static final String[] ARGUMENTS_EMPTY = new String[0];
 private static final String[] ARGUMENTS_ONE = {"arg1"};
 private static final String[] ARGUMENTS_TWO = {"arg1", "--optionA", "arg3"};
 private static final String[] ARGUMENTS_THREE = {"arg1", "--optionA", "arg3", "--optionB=valueB"};
 private static final String[] ARGUMENTS_INVALID = {"arg1", "--invalidOption", "arg3"};

 @Test
 public void testEmpty() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = parser.flatten(options, ARGUMENTS_EMPTY, false);
 assertEquals(new String[0], args);
 }

 @Test
 public void testOne() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("optionA", true, "Description for option A");
 String[] args = parser.flatten(options, ARGUMENTS_ONE, false);
 assertEquals("arg1", args[0]);
 }

 @Test
 public void testTwo() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("optionA", true, "Description for option A");
 options.addOption("optionB", true, "Description for option B");
 String[] args = parser.flatten(options, ARGUMENTS_TWO, false);
 assertEquals("arg1", args[0]);
 assertEquals("--optionA", args[1]);
 assertEquals("arg3", args[2]);
 }

 @Test(expected = ParseException.class)
 public void testInvalid() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("optionA", true, "Description for option A");
 String[] args = parser.flatten(options, ARGUMENTS_INVALID, false);
 fail("Should throw ParseException");
 }

 @Test
 public void testTwoOptionsWithValue() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("optionA", true, "Description for option A");
 options.addOption("optionB", true, "Description for option B");
 String[] args = parser.flatten(options, ARGUMENTS_THREE, false);
 assertEquals("arg1", args[0]);
 assertEquals("--optionA", args[1]);
 assertEquals("arg3", args[2]);
 assertEquals("--optionB=valueB", args[3]);
 }
}