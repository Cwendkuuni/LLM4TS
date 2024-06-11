package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

/**
 * Test class for Parser.
 */
public class ParserTest {

 private static final String[] ARGS1 = {"--input", "input.txt", "-o", "output.txt"};
 private static final String[] ARGS2 = {"--input=input.txt", "--output=output.txt"};
 private static final String[] ARGS3 = {"--input", "input.txt", "--output", "output.txt", "--flag"};

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "input file");
 options.addOption("output", true, "o", "output file");
 options.addOption(new Option("flag", true, null, "flag option"));

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS1, true);

 assertEquals("input.txt", cmd.getOptionValue("input"));
 assertEquals("output.txt", cmd.getOptionValue("output"));
 assertTrue(cmd.hasOption("flag"));

 cmd = parser.parse(options, ARGS2, true);
 assertEquals("input.txt", cmd.getOptionValue("input"));
 assertEquals("output.txt", cmd.getOptionValue("output"));
 assertTrue(cmd.hasOption("flag"));

 cmd = parser.parse(options, ARGS3, true);
 assertEquals("input.txt", cmd.getOptionValue("input"));
 assertEquals("output.txt", cmd.getArgs()[0]);
 assertTrue(cmd.hasOption("flag"));
 }

 @Test(expected = MissingArgumentException.class)
 public void testMissingArgument() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "input file");
 options.addOption("output", true, "o", "output file");
 options.addOption("flag", true, null, "flag option");

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS1, true);

 assertEquals("input.txt", cmd.getOptionValue("input"));
 assertEquals("", cmd.getOptionValue("output"));
 assertFalse(cmd.hasOption("flag"));
 assertFalse(cmd.getArgs().isEmpty());
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "input file");
 options.addOption("output", true, "o", "output file");

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[0], true);
 fail("Should throw MissingOptionException");
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "input file");
 options.addOption("output", true, "o", "output file");

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, new String[]{"--unknown"}, true);
 fail("Should throw UnrecognizedOptionException");
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Options options = new Options();
 options.addRequiredOption("input", true, "i", "input file");
 options.addOption("output", true, "o", "output file");
 options.addOption(new Option("flag", true, null, "flag option"));

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS2, new Properties(){{
 put("input", "input.txt");
 put("output", "output.txt");
 }});

 assertEquals("input.txt", cmd.getOptionValue("input"));
 assertEquals("output.txt", cmd.getOptionValue("output"));
 assertTrue(cmd.hasOption("flag"));
 }
}