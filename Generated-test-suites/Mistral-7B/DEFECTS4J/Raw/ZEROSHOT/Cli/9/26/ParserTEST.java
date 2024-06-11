package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 Parser parser = new DefaultParser();

 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");
 options.addOption("h", "help", false, "print help");

 String[] args = {"-i", "input.txt", "-o", "output.txt"};
 CommandLine cmd = parser.parse(options, args, null);

 assertEquals("input.txt", cmd.getOptionValue("input"));
 assertEquals("output.txt", cmd.getOptionValue("output"));
 assertNull(cmd.getOptionValue("help"));
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 Parser parser = new DefaultParser();

 Options options = new Options();
 options.addOption("i", "input", true, "input file");

 String[] args = {"-o", "output.txt"};
 CommandLine cmd = parser.parse(options, args, null);

 fail("Expected ParseException not thrown");
 }

 @Test(expected = ParseException.class)
 public void testUnknownOption() throws ParseException {
 Parser parser = new DefaultParser();

 Options options = new Options();
 options.addOption("i", "input", true, "input file");

 String[] args = {"--unknown"};
 CommandLine cmd = parser.parse(options, args, null);

 fail("Expected ParseException not thrown");
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 Parser parser = new DefaultParser();

 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");
 options.addOption("h", "help", false, "print help");

 Properties properties = new Properties();
 properties.put("input", "input.txt");
 properties.put("output", "output.txt");

 String[] args = {"--input", "--output"};
 CommandLine cmd = parser.parse(options, args, properties, false);

 assertEquals("input.txt", cmd.getOptionValue("input"));
 assertEquals("output.txt", cmd.getOptionValue("output"));
 assertNull(cmd.getOptionValue("help"));
 }

 @Test
 public void testParseWithStopAtNonOption() throws ParseException {
 Parser parser = new DefaultParser();

 Options options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");
 options.addOption("h", "help", false, "print help");

 String[] args = {"--input", "input.txt", "--output", "output.txt", "--"};
 CommandLine cmd = parser.parse(options, args, null, true);

 assertEquals(3, cmd.getArgs().size());
 }

 @Test
 public void testMissingRequiredOption() throws ParseException {
 Parser parser = new DefaultParser();

 Options options = new Options();
 options.addOption(null);
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");

 String[] args = {};

 try {
 parser.parse(options, args, null);
 fail("Expected MissingOptionException not thrown");
 } catch (MissingOptionException e) {
 // expected
 }
 }
}