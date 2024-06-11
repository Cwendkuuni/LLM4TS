package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 private String[] testArgs = new String[]{"--arg1", "-a", "--arg2=value", "-b", "--arg3", "-c", "--arg4=value2"};

 @Test
 public void testFlatten() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] parsedArgs = parser.flatten(options, testArgs, false);

 assertEquals(testArgs.length, parsedArgs.length);
 for (int i = 0; i < testArgs.length; i++) {
 assertEquals(testArgs[i], parsedArgs[i]);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithInvalidOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] testArgs = new String[]{"--invalidOption"};

 try {
 parser.flatten(options, testArgs, false);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithInvalidLongOptionValue() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 Option option = new Option("arg1", "arg1", true, "value");
 options.addOption(option);
 String[] testArgs = new String[]{"--arg1=invalidValue"};

 try {
 parser.flatten(options, testArgs, false);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithMultipleInvalidOptions() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 Option option1 = new Option("arg1", "arg1");
 Option option2 = new Option("arg2", "arg2");
 options.addOption(option1);
 options.addOption(option2);
 String[] testArgs = new String[]{"--arg1", "--arg3"};

 try {
 parser.flatten(options, testArgs, false);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithUnrecognizedOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] testArgs = new String[]{"--unrecognizedOption"};

 try {
 parser.flatten(options, testArgs, false);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 Option option = new Option("arg1", "arg1", true, "value");
 options.addOption(option);
 String[] testArgs = new String[]{"--arg1", "arg2", "--arg2", "arg3"};

 String[] parsedArgs = parser.flatten(options, testArgs, true);
 String[] expectedArgs = new String[]{"--arg1", "arg2"};

 assertArrayEquals(expectedArgs, parsedArgs);
 }

 @Test
 public void testFlattenWithBurstedOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 Option option = new Option("arg", "arg");
 option.setArgName("argName");
 options.addOption(option);
 String[] testArgs = new String[]{"-arg--argName", "argValue"};

 String[] parsedArgs = parser.flatten(options, testArgs, false);
 String[] expectedArgs = new String[]{"-arg", "argName", "argValue"};

 assertArrayEquals(expectedArgs, parsedArgs);
 }
}