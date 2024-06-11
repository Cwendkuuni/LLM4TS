package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 @Test
 public void testSimpleOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");

 String[] args = {"-a", "--b"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.parse(options, args, false);
 assertTrue(Arrays.asList(parsedArgs).contains("--a"));
 assertFalse(Arrays.asList(parsedArgs).contains("--b"));
 }

 @Test
 public void testLongOptions() throws ParseException {
 Options options = new Options();
 options.addOption("longOptionA", true, "Long Option A");
 options.addOption("longOptionB", false, "Long Option B");

 String[] args = {"--longOptionA", "--longOptionB"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.parse(options, args, false);
 assertTrue(Arrays.asList(parsedArgs).contains("--longOptionA"));
 assertFalse(Arrays.asList(parsedArgs).contains("--longOptionB"));
 }

 @Test
 public void testOptionsWithArguments() throws ParseException {
 Options options = new Options();
 options.addOption("optionA", true, "Option A with argument");
 options.addOption("optionB", 1, "Option B with one argument");
 options.addOption("optionC", "arg1,arg2", "Option C with multiple arguments");

 String[] args = {"-optionA", "valueA", "--optionB", "valueB", "--optionC", "arg1,arg2"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.parse(options, args, false);
 assertEquals(3, parsedArgs.length);
 assertEquals("valueA", parsedArgs[0]);
 assertEquals("valueB", parsedArgs[1]);
 assertEquals("arg1,arg2", parsedArgs[2]);
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 Options options = new Options();
 options.addOption("missingOption", true, "Missing Option");

 String[] args = {"--missingOptionValue"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.parse(options, args, false);
 fail("Expected ParseException, but got: " + Arrays.toString(parsedArgs));
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("invalidOption", true, "Invalid Option");

 String[] args = {"--invalidOptionValue", "--anotherOption"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.parse(options, args, false);
 fail("Expected ParseException, but got: " + Arrays.toString(parsedArgs));
 }

 @Test
 public void testLongOptionWithArgument() throws ParseException {
 Options options = new Options();
 options.addOption("longOptionA", true, "Long Option A with argument");
 options.addOption("longOptionB", "arg", "Long Option B with argument");

 String[] args = {"--longOptionA", "valueA", "--longOptionB", "valueB"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.parse(options, args, false);
 assertEquals(3, parsedArgs.length);
 assertEquals("valueA", parsedArgs[0]);
 assertEquals("valueB", parsedArgs[1]);
 }
}