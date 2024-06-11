package org.apache.commons.cli.test;

import org.apache.commons.cli.Parser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParser() throws ParseException {
 Parser parser = new DefaultParser();

 // test with no options or arguments
 Options options = new Options();
 String[] arguments = new String[0];
 CommandLine cmd = parser.parse(options, arguments);
 assertTrue(cmd.getArgs().isEmpty());
 assertTrue(cmd.getOptions().isEmpty());

 // test with options but no arguments
 options = new Options();
 Option option1 = new Option("o", "option1", true, "option value");
 Option option2 = new Option("o", "option2", false, "option description");
 options.addOption(option1);
 options.addOption(option2);
 arguments = new String[0];
 cmd = parser.parse(options, arguments);
 assertTrue(cmd.getArgs().isEmpty());
 assertTrue(cmd.getOptions().size() == 2);
 assertTrue(cmd.hasOption("option1"));
 assertFalse(cmd.hasOption("option2"));
 assertNull(cmd.getOptionValue("option1"));
 assertNull(cmd.getOptionValue("option2"));

 // test with arguments and options
 options = new Options();
 Option option1 = new Option("o", "option1", true, "option value");
 Option option2 = new Option("o", "option2", false, "option description");
 options.addOption(option1);
 options.addOption(option2);
 arguments = new String[]{"--option1", "value1", "--option2"};
 cmd = parser.parse(options, arguments);
 assertTrue(cmd.getArgs().size() == 0);
 assertTrue(cmd.getOptions().size() == 2);
 assertTrue(cmd.hasOption("option1"));
 assertTrue(cmd.hasOption("option2"));
 assertEquals("value1", cmd.getOptionValue("option1"));
 assertFalse(cmd.getOptionValue("option2") instanceof String[]);
 assertNull(cmd.getOptionValue("option2"));

 // test with properties
 Properties properties = new Properties();
 properties.put("option1", "value1");
 properties.put("option2", "value2");
 options = new Options();
 Option option1 = new Option("o", "option1", true, "option value");
 Option option2 = new Option("o", "option2", false, "option description");
 options.addOption(option1);
 options.addOption(option2);
 arguments = new String[0];
 cmd = parser.parse(options, arguments, properties);
 assertTrue(cmd.getArgs().isEmpty());
 assertTrue(cmd.getOptions().size() == 2);
 assertTrue(cmd.hasOption("option1"));
 assertTrue(cmd.hasOption("option2"));
 assertEquals("value1", cmd.getOptionValue("option1"));
 assertFalse(cmd.getOptionValue("option2") instanceof String[]);
 assertNull(cmd.getOptionValue("option2"));

 // test required options
 options = new Options();
 Option option1 = new Option("o", "option1", true, "option value");
 Option option2 = new Option("o", "option2", true, "option description");
 options.addOption(option1);
 options.addOption(option2);
 arguments = new String[]{"--option1"};
 try {
 cmd = parser.parse(options, arguments);
 fail("Missing required option: option2");
 } catch (MissingOptionException e) {
 // expected
 }

 // test stopAtNonOption
 options = new Options();
 Option option1 = new Option("o", "option1", true, "option value");
 Option option2 = new Option("o", "option2", true, "option description");
 options.addOption(option1);
 options.addOption(option2);
 arguments = new String[]{"--option1", "arg1", "--option2", "arg2"};
 try {
 cmd = parser.parse(options, arguments, false);
 fail("Arguments not handled as expected");
 } catch (ParseException e) {
 // expected
 }
 cmd = parser.parse(options, arguments, true);
 assertEquals(Arrays.asList("arg1", "arg2"), cmd.getArgs());
 assertTrue(cmd.getOptions().size() == 2);
 assertTrue(cmd.hasOption("option1"));
 assertTrue(cmd.hasOption("option2"));
 assertEquals("arg1", cmd.getOptionValue("option1"));
 assertFalse(cmd.getOptionValue("option2") instanceof String[]);
 assertNull(cmd.getOptionValue("option2"));
 }
}