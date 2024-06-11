package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
 Option option = optionBuilder.create('L');
 assertEquals("longOpt", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create('A');
 assertEquals(1, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = optionBuilder.create('O');
 assertTrue(option.isOptional());
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
 Option option = optionBuilder.create('V');
 assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = optionBuilder.create('R');
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = optionBuilder.create('H');
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderHasArgsWithLimit() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
 Option option = optionBuilder.create('G');
 assertEquals(5, option.getArgs().length);
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
 Option option = optionBuilder.create('I');
 assertEquals(Integer.class, option.getType());
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("Test Option");
 Option option = optionBuilder.create('T');
 assertEquals("Test Option", option.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderWithoutLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.create('U');
 fail("IllegalArgumentException expected");
 }
}