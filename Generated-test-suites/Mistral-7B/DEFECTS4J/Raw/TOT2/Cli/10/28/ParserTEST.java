package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

 private CommandLineParser parser;
 private Options options;

 @Before
 public void setup() {
 options = new Options();
 options.addRequiredOption("i", "input", true, "Input file path");
 parser = new DefaultParser();
 }

 /**
 * Test parsing simple command line arguments
 */
 @Test
 public void testSimpleParsing() throws ParseException {
 String[] args = new String[]{"-i", "test.txt"};
 CommandLine cmd = parser.parse(options, args, false);

 Assert.assertEquals("test.txt", cmd.getOptionValue("input"));
 }

 /**
 * Test parsing command line arguments with optional arguments
 */
 @Test
 public void testOptionalParsing() throws ParseException {
 String[] args = new String[]{"-i", "test.txt", "--help"};
 CommandLine cmd = parser.parse(options, args, false);

 Assert.assertEquals("test.txt", cmd.getOptionValue("input"));
 Assert.assertTrue(cmd.hasOption("help"));
 }

 /**
 * Test parsing command line arguments with multiple occurrences of the same option
 */
 @Test
 public void testMultipleOccurrences() throws ParseException {
 String[] args = new String[]{"-i", "test.txt", "-i", "test2.txt"};
 CommandLine cmd = parser.parse(options, args, false);

 List<String> inputFiles = cmd.getOptionValues("input");
 Assert.assertEquals(2, inputFiles.size());
 Assert.assertEquals("test.txt", inputFiles.get(0));
 Assert.assertEquals("test2.txt", inputFiles.get(1));
 }

 /**
 * Test parsing command line arguments with required options missing
 */
 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOptions() throws ParseException {
 String[] args = new String[]{"-i", "test.txt"};
 CommandLine cmd = parser.parse(options, args, false);
 }

 /**
 * Test parsing command line arguments with non-existent options
 */
 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOptions() throws ParseException {
 String[] args = new String[]{"-x", "test.txt"};
 CommandLine cmd = parser.parse(options, args, false);
 }
}