package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptName");
 Option option = optionBuilder.create('L');
 assertEquals("longOptName", option.getLongOpt());
 assertEquals("", option.getOpt());
 assertEquals("", option.getDescription());
 assertEquals(false, option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgCount());
 assertNull(option.getType());
 assertFalse(option.isOptional());
 assertNull(option.getValueSeparator());
 assertNull(option.getArgName());
 }

 @Test
 public void testOptionBuilderWithLongOptAndDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptName")
 .withDescription("This is a test option");
 Option option = optionBuilder.create('L');
 assertEquals("longOptName", option.getLongOpt());
 assertEquals("", option.getOpt());
 assertEquals("This is a test option", option.getDescription());
 assertEquals(false, option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgCount());
 assertNull(option.getType());
 assertFalse(option.isOptional());
 assertNull(option.getValueSeparator());
 assertNull(option.getArgName());
 }

 @Test
 public void testOptionBuilderWithLongOptRequired() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptName")
 .isRequired();
 Option option = optionBuilder.create('L');
 assertEquals("longOptName", option.getLongOpt());
 assertEquals("", option.getOpt());
 assertEquals("", option.getDescription());
 assertTrue(option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgCount());
 assertNull(option.getType());
 assertFalse(option.isOptional());
 assertNull(option.getValueSeparator());
 assertNull(option.getArgName());
 }

 @Test
 public void testOptionBuilderWithLongOptArgument() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptName")
 .hasArg();
 Option option = optionBuilder.create('L');
 assertEquals("longOptName", option.getLongOpt());
 assertEquals("", option.getOpt());
 assertEquals("", option.getDescription());
 assertEquals(false, option.isRequired());
 assertEquals(1, option.getArgCount());
 assertNull(option.getType());
 assertFalse(option.isOptional());
 assertNull(option.getValueSeparator());
 assertNull(option.getArgName());
 }

 @Test
 public void testOptionBuilderWithLongOptOptionalArgument() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptName")
 .hasOptionalArg();
 Option option = optionBuilder.create('L');
 assertEquals("longOptName", option.getLongOpt());
 assertEquals("", option.getOpt());
 assertEquals("", option.getDescription());
 assertEquals(false, option.isRequired());
 assertEquals(1, option.getArgCount());
 assertTrue(option.isOptional());
 assertNull(option.getValueSeparator());
 assertNull(option.getArgName());
 }

 @Test
 public void testOptionBuilderWithLongOptValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptName")
 .withValueSeparator(';');
 Option option = optionBuilder.create('L');
 assertEquals("longOptName", option.getLongOpt());
 assertEquals("", option.getOpt());
 assertEquals("", option.getDescription());
 assertEquals(false, option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgCount());
 assertNull(option.getType());
 assertFalse(option.isOptional());
 assertEquals(';', option.getValueSeparator());
 assertNull(option.getArgName());
 }

 @Test
 public void testOptionBuilderWithLongOptType() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptName")
 .withType(String.class);
 Option option = optionBuilder.create('L');
 assertEquals("longOptName", option.getLongOpt());
 assertEquals("", option.getOpt());
 assertEquals("", option.getDescription());
 assertEquals(false, option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgCount());
 assertNotNull(option.getType());
 assertFalse(option.isOptional());
 assertNull(option.getValueSeparator());
 assertNull(option.getArgName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderWithUninitializedLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.create();
 Option option = optionBuilder.create('L');
 fail("OptionBuilder must be configured with a long opt before creating an Option");
 }

 @Test
 public void testOptionBuilderWithShortOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptName")
 .create('l');
 Option option = optionBuilder.create('l');
 assertEquals(option.getLongOpt(), optionBuilder.getLongOpt());
 assertEquals("l", option.getOpt());
 assertEquals("", option.getDescription());
 assertEquals(false, option.isRequired());
 assertEquals(Option.UNINITIALIZED, option.getArgCount());
 assertNull(option.getType());
 assertFalse(option.isOptional());
 assertNull(option.getValueSeparator());
 assertNull(option.getArgName());
 }
}