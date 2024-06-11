package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testSimpleOption() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder
 .withLongOpt("test")
 .withDescription("Test option")
 .isRequired()
 .create();

 CommandLine args = new DefaultParser().parse(new String[]{"-test"});
 assertEquals("Test option", args.getOptionValue("test"));
 assertNull(args.getOptionValue("unknown"));

 Option testOption = args.getOption("test");
 assertTrue(testOption.isSet());
 assertFalse(testOption.hasArg());
 assertFalse(testOption.isUnbounded());
 }

 @Test
 public void testOptionalOption() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder
 .withLongOpt("test")
 .withDescription("Test option")
 .hasArg()
 .isRequired(false)
 .create();

 CommandLine args = new DefaultParser().parse(new String[]{"-test", "arg"});
 assertEquals("arg", args.getOptionValue("test"));
 assertNull(args.getOptionValue("unknown"));

 Option testOption = args.getOption("test");
 assertTrue(testOption.isSet());
 assertTrue(testOption.hasArg());
 assertFalse(testOption.isRequired());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.create();
 }

 @Test
 public void testOptionWithValueSeparator() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder
 .withLongOpt("test")
 .withDescription("Test option")
 .isRequired()
 .hasArgs()
 .withValueSeparator(':')
 .create();

 CommandLine args = new DefaultParser().parse(new String[]{"-test", "arg1:arg2"});
 assertEquals("arg1:arg2", args.getOptionValue("test"));

 Option testOption = args.getOption("test");
 assertTrue(testOption.isSet());
 assertTrue(testOption.hasArg());
 assertTrue(testOption.isUnbounded());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidValueSeparator() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder
 .withLongOpt("test")
 .withDescription("Test option")
 .isRequired()
 .hasArgs()
 .withValueSeparator(' ')
 .create();
 }
}