package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.List;

public class PosixParserTest {

 private PosixParser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addRequiredOption("i", "inputFile", true, "input file");
 options.addOption("o", "outputFile", true, "output file");
 options.addOption("h", "help", false, "print help");
 }

 @Test
 public void testEmptyArgs() throws ParseException {
 String[] args = {};
 String[] expectedArgs = {};
 String[] resultArgs = parser.parse(options, args);
 Assert.assertArrayEquals(expectedArgs, resultArgs);
 }

 @Test
 public void testValidArgs() throws ParseException {
 String[] args = {"-i", "input.txt", "-o", "output.txt"};
 String[] expectedArgs = {"input.txt", "output.txt"};
 String[] resultArgs = parser.parse(options, args);
 Assert.assertArrayEquals(expectedArgs, resultArgs);
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredArg() throws ParseException {
 String[] args = {"-i", "-o"};
 String[] expectedArgs = {"-i", "-o"};
 String[] resultArgs = parser.parse(options, args);
 Assert.assertArrayEquals(expectedArgs, resultArgs);
 FailureException failure = assertThrows(ParseException.class, () -> {
 parser.parse(options, args);
 });
 assertEquals("The following required options do not have values, please provide a value for: [inputFile]", failure.getMessage());
 }

 @Test
 public void testLongArg() throws ParseException {
 String[] args = {"--help"};
 boolean expectedHelp = true;
 parser = new PosixParser();
 parser.parse(options, args);
 assertTrue(parser.getOption("h").isSet());
 }

 @Test
 public void testInvalidArg() throws ParseException {
 String[] args = {"-x"};
 boolean expectedHelp = false;
 parser = new PosixParser();
 parser.parse(options, args);
 assertFalse(parser.getOption("x").isSet());
 }

 @Test
 public void testBursting() throws ParseException {
 String[] args = {"-i", "--output", "output.txt"};
 String[] expectedArgs = {"input.txt", "--output", "output.txt"};
 String[] resultArgs = parser.parse(options, args).toString().split("\\s+");
 Assert.assertArrayEquals(expectedArgs, resultArgs);
 }

 @Test
 public void testBurstingWithNonOption() throws ParseException {
 String[] args = {"-i", "--output", "output.txt", "nonOption"};
 String[] expectedArgs = {"input.txt", "--output", "output.txt", "nonOption"};
 String[] resultArgs = parser.parse(options, args).toString().split("\\s+");
 Assert.assertArrayEquals(expectedArgs, resultArgs);
 }

 @Test
 public void testBurstingWithStopAtNonOption() throws ParseException {
 options.addOption("s", "stopAtNonOption", false, "stop at non option");
 String[] args = {"-i", "--output", "output.txt", "--", "nonOption"};
 String[] expectedArgs = {"input.txt", "--output", "--"};
 String[] resultArgs = parser.parse(options, args).toString().split("\\s+");
 Assert.assertArrayEquals(expectedArgs, resultArgs);
 }
}