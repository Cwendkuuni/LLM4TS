package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class OptionBuilderTest {

 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
 Option option = optionBuilder.create('T');
 assertEquals("testLongOpt", option.getLongOpt());
 }

 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg();
 Option option = optionBuilder.create('A');
 assertEquals(1, option.getArgs().length);
 }

 @Test
 public void testHasArgWithName() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
 Option option = optionBuilder.create('A');
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = OptionBuilder.isRequired();
 Option option = optionBuilder.create('R');
 assertTrue(option.isRequired());
 }

 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
 Option option = optionBuilder.create('V');
 assertEquals(',', option.getValueSeparator());
 }

 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasArgs();
 Option option = optionBuilder.create('S');
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
 }

 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
 Option option = optionBuilder.create('O');
 assertTrue(option.isOptionalArg());
 }

 @Test
 public void testHasOptionalArgs() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
 Option option = optionBuilder.create('P');
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
 }

 @Test
 public void testHasOptionalArgsWithNumber() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
 Option option = optionBuilder.create('P');
 assertEquals(2, option.getArgs().length);
 }

 @Test
 public void testWithType() {
 OptionBuilder optionBuilder = OptionBuilder.withType("java.lang.Integer.TYPE");
 Option option = optionBuilder.create('I');
 assertEquals("java.lang.Integer.TYPE", option.getType().toString());
 }

 @Test
 public void testWithDescription() {
 OptionBuilder optionBuilder = OptionBuilder.withDescription("Test Option");
 Option option = optionBuilder.create('T');
 assertEquals("Test Option", option.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithoutLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.create('X');
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHasArgWithNegativeNumber() {
 OptionBuilder optionBuilder = OptionBuilder.hasArg(-1);
 Option option = optionBuilder.create('X');
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHasOptionalArgsWithNegativeNumber() {
 OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(-1);
 Option option = optionBuilder.create('X');
 }
}