package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for OptionBuilder class.
 */
public class OptionBuilderTest {

 @Test
 public void testBasicOption() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withLongOpt("myOption")
 .isRequired()
 .create();
 CommandLineParser parser = new DefaultParser();

 String[] args = {"--myOption"};
 try {
 CommandLine cmd = parser.parse(args);
 assertEquals("myOption", cmd.getOptionValue("myOption"));
 assertTrue(cmd.hasOption("myOption"));
 } catch (ParseException e) {
 // won't reach here
 }
 }

 @Test
 public void testOptionWithArg() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withLongOpt("myOption")
 .hasArg()
 .create();
 CommandLineParser parser = new DefaultParser();

 String[] args = {"--myOption", "arg"};
 try {
 CommandLine cmd = parser.parse(args);
 assertEquals("arg", cmd.getOptionValue("myOption"));
 } catch (ParseException e) {
 // won't reach here
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingLongOpt() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.create();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingDescription() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withLongOpt("myOption")
 .create();
 }

 @Test
 public void testOptionWithValueSeparator() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withLongOpt("myOption")
 .hasArg()
 .withValueSeparator('=')
 .create();
 CommandLineParser parser = new DefaultParser();

 String[] args = {"--myOption", "key=value"};
 try {
 CommandLine cmd = parser.parse(args);
 assertEquals("value", cmd.getOptionValue("myOption"));
 } catch (ParseException e) {
 // won't reach here
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidValueSeparator() {
 OptionBuilder builder = new OptionBuilder();
 Option option = builder.withLongOpt("myOption")
 .hasArg()
 .withValueSeparator(';') // invalid value separator
 .create();
 }
}