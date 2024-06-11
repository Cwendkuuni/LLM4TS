package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testBasicUsage() {
 Options options = new Options();
 options.addOption("i", "inputFile", true, "path to input file");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-i", "/path/to/input/file.txt"});
 assertEquals("/path/to/input/file.txt", cmd.getOptionValue("inputFile"));
 } catch (ParseException e) {
 fail("ParseException should not be thrown");
 }
 }

 @Test
 public void testMissingOption() {
 Options options = new Options();
 options.addOption("i", "inputFile", true, "path to input file");

 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(options.toArray(), new String[]{"-o", "/path/to/output/file.txt"});
 fail("MissingOptionException should be thrown");
 } catch (MissingOptionException e) {
 // expected
 }
 }

 @Test
 public void testExtraOption() {
 Options options = new Options();
 options.addOption("i", "inputFile", true, "path to input file");

 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(options.toArray(), new String[]{"-i", "/path/to/input/file.txt", "-x", "extra"});
 fail("IllegalArgumentException should be thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testShortOptionWithArg() {
 Options options = new Options();
 options.addOption("i", "inputFile", true, "path to input file");
 options.addOption("o", "outputFile", true, "path to output file");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-i", "/path/to/input/file.txt", "-o", "/path/to/output/file.txt"});
 assertEquals("/path/to/input/file.txt", cmd.getOptionValue("inputFile"));
 assertEquals("/path/to/output/file.txt", cmd.getOptionValue("outputFile"));
 } catch (ParseException e) {
 fail("ParseException should not be thrown");
 }
 }

 @Test
 public void testLongOptionWithArg() {
 Options options = new Options();
 options.addOption("i", "inputFile", true, "path to input file");
 options.addOption("o", "outputFile", true, "path to output file");
 options.addOption("h", "help", false, "display help message and exit");

 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"--help"});
 assertTrue(cmd.hasOption("help"));
 } catch (ParseException e) {
 fail("ParseException should not be thrown");
 }
 }

 @Test
 public void testOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group");
 group.setRequired(true);
 options.addOptionGroup(group);
 group.addOption("g", "groupOption", false, "group option");

 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(options.toArray(), new String[]{"-g"});
 fail("MissingOptionException should be thrown for required option group");
 } catch (MissingOptionException e) {
 // expected
 }
 }
}