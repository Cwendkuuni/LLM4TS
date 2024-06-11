package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
 Option option = optionBuilder.create();
 assertEquals("longOpt", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create();
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 }

 @Test
 public void testOptionBuilderHasArgWithName() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
 Option option = optionBuilder.create();
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = optionBuilder.create();
 assertEquals(true, option.isRequired());
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
 Option option = optionBuilder.create();
 assertEquals(',', option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = optionBuilder.create();
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = optionBuilder.create();
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 assertEquals(true, option.isOptional());
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
 Option option = optionBuilder.create();
 assertNotNull(option.getType());
 assertEquals(String.class, option.getType());
 }

 @Test(expected = IllegalStateException.class)
 public void testOptionBuilderWithoutLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.create();
 }
}