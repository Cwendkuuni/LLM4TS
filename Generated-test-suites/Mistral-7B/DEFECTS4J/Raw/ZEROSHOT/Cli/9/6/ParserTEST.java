package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 // Set up the Options
 Options options = new Options();
 options.addOption("o", "option", true, "Option description");
 options.addOptionGroup(new OptionGroup("g", "Group", "Options in group"));
 options.addOption("g1", "group1", true, "Group option 1");
 options.addOption("g2", "group2", true, "Group option 2");
 options.addOption("r", "required", ValueConstants.NO_VALUE, "Required option");

 // Create a Parser instance
 Parser parser = new DefaultParser();

 // Test parsing with no arguments
 String[] args1 = {};
 CommandLine cmd1 = parser.parse(options, args1, false);
 assertNull(cmd1.getOptionValue("o"));
 assertNull(cmd1.getOptionValue("g1"));
 assertNull(cmd1.getOptionValue("g2"));
 assertNull(cmd1.getOptionValue("r"));

 // Test parsing with optional arguments
 String[] args2 = {"-o", "value1", "--group1", "value2"};
 CommandLine cmd2 = parser.parse(options, args2, false);
 assertEquals("value1", cmd2.getOptionValue("o"));
 assertEquals("value2", cmd2.getOptionValue("g1"));
 assertNull(cmd2.getOptionValue("g2"));
 assertNull(cmd2.getOptionValue("r"));

 // Test parsing with required arguments
 String[] args3 = {"-r"};
 try {
 parser.parse(options, args3, false);
 fail("MissingRequiredOptionException not thrown");
 } catch (MissingOptionException e) {
 String expected = "Missing required option: r";
 assertTrue(e.getMessage().equals(expected));
 }

 // Test parsing with group options
 String[] args4 = {"-g", "value1"};
 CommandLine cmd4 = parser.parse(options, args4, false);
 assertEquals("value1", cmd4.getOptionValue("g1"));
 assertEquals("value1", cmd4.getOptionValue("g2"));
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 // Set up the Options
 Options options = new Options();
 options.addOption("o", "option", true, "Option description");

 // Create a Parser instance
 Parser parser = new DefaultParser();

 // Test parsing with unrecognized option
 String[] args = {"-x", "value"};
 parser.parse(options, args, false);
 }
}