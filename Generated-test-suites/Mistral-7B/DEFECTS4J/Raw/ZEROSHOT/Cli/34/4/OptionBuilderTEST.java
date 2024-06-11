package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithDefaultSettings() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.create('D');
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertFalse(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertDefaultValue(Option.NO_ARGUMENTS, option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withLongOpt("testLongOpt").create('D');
 assertEquals("testLongOpt", option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertFalse(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertDefaultValue(Option.NO_ARGUMENTS, option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withDescription("Test Option").create('D');
 assertEquals("Test Option", option.getDescription());
 assertNull(option.getLongOpt());
 assertNull(option.getArgName());
 assertFalse(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertDefaultValue(Option.NO_ARGUMENTS, option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithArgName() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withArgName("testArgName").create('D');
 assertEquals("testArgName", option.getArgName());
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertFalse(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertDefaultValue(Option.NO_ARGUMENTS, option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.isRequired().create('D');
 assertTrue(option.isRequired());
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertDefaultValue(Option.NO_ARGUMENTS, option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.hasArg().create('D');
 assertEquals(1, option.getArgs());
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertFalse(option.isRequired());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertDefaultValue(Option.NO_ARGUMENTS, option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.hasArgs().create('D');
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertFalse(option.isRequired());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertDefaultValue(Option.NO_ARGUMENTS, option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.hasOptionalArg().create('D');
 assertTrue(option.hasOptionalArg());
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 assertNull(option.getType());
 assertFalse(option.isRequired());
 assertDefaultValue(Option.NO_ARGUMENTS, option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withValueSeparator(':').create('D');
 assertEquals(':', option.getValueSeparator());
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertFalse(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 }

 @Test
 public void testOptionBuilderWithType() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withType(Integer.TYPE).create('D');
 assertEquals(Integer.TYPE, option.getType());
 assertNull(option.getLongOpt());
 assertNull(option.getDescription());
 assertNull(option.getArgName());
 assertFalse(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 assertFalse(option.hasOptionalArg());
 assertDefaultValue(Option.NO_ARGUMENTS, option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithDescriptionAndValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withDescription("Test Option")
 .withValueSeparator(':')
 .create('D');
 assertEquals("Test Option", option.getDescription());
 assertEquals(':', option.getValueSeparator());
 assertNull(option.getLongOpt());
 assertNull(option.getArgName());
 assertFalse(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 }

 @Test
 public void testOptionBuilderWithDescriptionArgNameAndValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withDescription("Test Option")
 .withArgName("testArg")
 .withValueSeparator(':')
 .create('D');
 assertEquals("Test Option", option.getDescription());
 assertEquals("testArg", option.getArgName());
 assertEquals(':', option.getValueSeparator());
 assertNull(option.getLongOpt());
 assertFalse(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 }

 @Test
 public void testOptionBuilderWithDescriptionArgNameTypeAndValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withDescription("Test Option")
 .withArgName("testArg")
 .withType(Integer.TYPE)
 .withValueSeparator(':')
 .create('D');
 assertEquals("Test Option", option.getDescription());
 assertEquals("testArg", option.getArgName());
 assertEquals(Integer.TYPE, option.getType());
 assertEquals(':', option.getValueSeparator());
 assertNull(option.getLongOpt());
 assertFalse(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 assertFalse(option.hasOptionalArg());
 }

 @Test
 public void testOptionBuilderWithDescriptionArgNameTypeAndArgs() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withDescription("Test Option")
 .withArgName("testArg")
 .withType(Integer.TYPE)
 .withArgs(2)
 .create('D');
 assertEquals("Test Option", option.getDescription());
 assertEquals("testArg", option.getArgName());
 assertEquals(Integer.TYPE, option.getType());
 assertEquals(2, option.getArgs());
 assertNull(option.getLongOpt());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertDefaultValue(Option.NO_ARGUMENTS, option.getValueSeparator());
 }

 @Test
 public void testOptionBuilderWithDescriptionArgNameTypeAndArgsAndValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withDescription("Test Option")
 .withArgName("testArg")
 .withType(Integer.TYPE)
 .withArgs(2)
 .withValueSeparator(':')
 .create('D');
 assertEquals("Test Option", option.getDescription());
 assertEquals("testArg", option.getArgName());
 assertEquals(Integer.TYPE, option.getType());
 assertEquals(2, option.getArgs());
 assertEquals(':', option.getValueSeparator());
 assertNull(option.getLongOpt());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 }

 @Test
 public void testOptionBuilderWithDescriptionLongOptArgNameTypeAndArgsAndValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withDescription("Test Option for long opt")
 .withLongOpt("testLongOpt")
 .withArgName("testArg")
 .withType(Integer.TYPE)
 .withArgs(2)
 .withValueSeparator(':')
 .create('D');
 assertEquals("Test Option for long opt", option.getDescription());
 assertEquals("testLongOpt", option.getLongOpt());
 assertEquals("testArg", option.getArgName());
 assertEquals(Integer.TYPE, option.getType());
 assertEquals(2, option.getArgs());
 assertEquals(':', option.getValueSeparator());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 }

 @Test
 public void testOptionBuilderWithDescriptionLongOptArgNameTypeAndArgsAndValueSeparatorAndOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.getInstance();
 Option option = optionBuilder.withDescription("Test Option for long opt")
 .withLongOpt("testLongOpt")
 .withArgName("testArg")
 .withType(Integer.TYPE)
 .withArgs(2)
 .withValueSeparator(':')
 .hasOptionalArg()
 .create('D');
 assertEquals("Test Option for long opt", option.getDescription());
 assertEquals("testLongOpt", option.getLongOpt());
 assertEquals("testArg", option.getArgName());
 assertEquals(Integer.TYPE, option.getType());
 assertEquals(2, option.getArgs());
 assertEquals(':', option.getValueSeparator());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 }

 private static void assertDefaultValue(int expectedValue, char valueSeparator) {
 assertEquals(expectedValue, Option.NO_ARGUMENTS);
 assertEquals(valueSeparator, Option.NO_ARGUMENTS_VALUE_SEPARATOR);
 }
}