package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 private static final String[] ARGS_WITH_OPTIONS = {"-a", "--option1", "1", "2", "--option2", "3"};
 private static final String[] ARGS_WITH_ONLY_OPTION = {"--option"};
 private static final String[] ARGS_WITH_ONLY_ARG = {"arg1", "arg2"};
 private static final String[] ARGS_WITH_MISSING_OPTION = {"arg1", "arg2", "--missingOption"};
 private static final String[] ARGS_WITH_MISSING_ARG = {"-a", "--option"};

 @Test
 public void testParsingWithOptionsAndArgs() throws ParseException {
 Options options = new Options();
 options.addOption("a", "optionA", true, "an option A");
 options.addOption("o", "optionB", false, "an option B");
 options.addOption("i", "optionC", false, "an option C");

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS_WITH_OPTIONS, false);

 assertEquals(2, cmd.getArgs().length);
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("o"));
 assertFalse(cmd.hasOption("i"));
 assertEquals("1", cmd.getOptionValue("optionA"));
 assertEquals("2", cmd.getOptionValue("option1"));
 assertNull(cmd.getOptionValue("optionC"));
 }

 @Test(expected = ParseException.class)
 public void testParsingWithMissingOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", "optionA", true, "an option A");

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS_WITH_MISSING_OPTION, false);
 fail("A missing option should have caused a ParseException");
 }

 @Test(expected = ParseException.class)
 public void testParsingWithMissingArg() throws ParseException {
 Options options = new Options();
 options.addOption("a", "optionA", true, "an option A");

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS_WITH_MISSING_ARG, false);
 fail("A missing argument should have caused a ParseException");
 }

 @Test
 public void testParsingWithOnlyOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", "optionA", true, "an option A");
 options.addOption("o", "optionB", false, "an option B");

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS_WITH_ONLY_OPTION, false);
 assertTrue(cmd.getArgs().length == 0);
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("o"));
 assertFalse(cmd.hasOption("i"));
 }

 @Test
 public void testParsingWithOnlyArgs() throws ParseException {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options, ARGS_WITH_ONLY_ARG, false);
 assertEquals(2, cmd.getArgs().length);
 assertFalse(cmd.hasOption("a"));
 assertFalse(cmd.hasOption("o"));
 assertFalse(cmd.hasOption("i"));
 }
}