package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasArg().create();
 assertEquals(Option.UNINITIALIZED, option.getArgs());

 optionBuilder.reset();
 optionBuilder.hasArg();
 Option option1 = optionBuilder.create();
 assertEquals(1, option1.getArgs().getValue());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasArgs(3).create();
 assertEquals(3, option.getArgs().getValue());
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasOptionalArg().create();
 assertTrue(option.isOptional());
 }

 @Test
 public void testOptionBuilderHasOptionalArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasOptionalArgs(2).create();
 assertEquals(2, option.getArgs().getMaxValue());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.isRequired().create();
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionBuilderWithArgName() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withArgName("argName").create();
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withDescription("testOptionDescription").create();
 assertEquals("testOptionDescription", option.getDescription());
 }

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withLongOpt("testLongOpt").create();
 assertEquals("testLongOpt", option.getLongOpt());
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withType(String.class).create();
 assertEquals(String.class, option.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderCreateWithoutLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.create();
 fail("IllegalArgumentException expected");
 }
}