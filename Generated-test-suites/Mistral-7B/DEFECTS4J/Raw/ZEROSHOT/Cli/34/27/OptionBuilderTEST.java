package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
 Option option = optionBuilder.create('o');
 assertEquals("optionLong", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create('a');
 assertEquals(Option.UNINITIALIZED, option.getArgs().length);
 optionBuilder.isRequired();
 option = optionBuilder.create('a');
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionBuilderArgName() {
 OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
 Option option = optionBuilder.create('n');
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = optionBuilder.create('r');
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = optionBuilder.create('h');
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = optionBuilder.create('v');
 assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(Integer.TYPE);
 Option option = optionBuilder.create('i');
 assertEquals(Integer.TYPE, option.getType());
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("This is a test option");
 Option option = optionBuilder.create('t');
 assertEquals("This is a test option", option.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderMissingLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create('x');
 }
}