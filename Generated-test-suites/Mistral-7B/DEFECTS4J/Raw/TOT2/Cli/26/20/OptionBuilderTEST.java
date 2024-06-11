package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

 // Test for withLongOpt method
 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withLongOpt("option1").create();
 Assert.assertEquals("option1", option.getLongOpt());
 }

 // Test for hasArg method
 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasArg().create();
 Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
 }

 // Test for hasArg with argument
 @Test
 public void testHasArgWithArgument() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasArg().withArgName("arg1").create();
 Assert.assertEquals(1, option.getArgs());
 Assert.assertEquals("arg1", option.getArgName());
 }

 // Test for isRequired method
 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.isRequired().create();
 Assert.assertTrue(option.isRequired());
 }

 // Test for withValueSeparator method
 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withValueSeparator(':').create();
 Assert.assertEquals(':', option.getValueSeparator());
 }

 // Test for hasArgs method
 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasArgs().create();
 Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
 }

 // Test for hasArgs with specific number of arguments
 @Test
 public void testHasArgsWithSpecificNumber() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasArgs(2).create();
 Assert.assertEquals(2, option.getArgs());
 }

 // Test for hasOptionalArg method
 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasOptionalArg().create();
 Assert.assertTrue(option.isOptional());
 }

 // Test for hasOptionalArgs method
 @Test
 public void testHasOptionalArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.hasOptionalArgs(3).create();
 Assert.assertEquals(3, option.getArgs());
 Assert.assertTrue(option.isOptional());
 }

 // Test for withType method
 @Test
 public void testWithType() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withType(String.class).create();
 Assert.assertEquals(String.class, option.getType());
 }

 // Test for withDescription method
 @Test
 public void testWithDescription() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withDescription("Test Option").create();
 Assert.assertEquals("Test Option", option.getDescription());
 }

 // Test for create method without any settings
 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithoutSettings() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.create();
 }

 // Test for create method with null long opt
 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithNullLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.withLongOpt(null).create();
 }
}