package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class PatternBuilderTest {

 private PatternBuilder builder;

 @Test
 public void testEmptyPattern() {
 builder = new PatternBuilder();
 builder.reset();
 Option option = builder.create();
 assertEquals(Option.Builder.class, option.getClass());
 assertFalse(option.isRequired());
 assertNull(option.getLongOpt());
 assertNull(option.getValue());
 }

 @Test
 public void testSimpleOption() {
 builder = new PatternBuilder();
 builder.withPattern("a:");
 Option option = builder.create();
 assertEquals("a", option.getShortName());
 assertEquals(null, option.getLongOpt());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionalOptionWithValue() {
 builder = new PatternBuilder();
 builder.withPattern("a:s");
 Option option = builder.create();
 assertEquals("a", option.getShortName());
 assertEquals("s", option.getLongOpt());
 assertFalse(option.isRequired());
 }

 @Test
 public void testRequiredOption() {
 builder = new PatternBuilder();
 builder.withPattern("a:s");
 builder.create(); // create an optional option
 builder.reset();
 builder.withPattern("a:s");
 builder.withPattern("!a:s");
 Option option = builder.create();
 assertEquals("a", option.getShortName());
 assertEquals("a", option.getLongOpt());
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionalOptionWithValidator() {
 builder = new PatternBuilder();
 builder.withPattern("%d:");
 Option option = builder.create();
 assertEquals(NumberValidator.class, option.getValidator().getClass());
 }

 @Test
 public void testRequiredOptionWithValidator() {
 builder = new PatternBuilder();
 builder.withPattern("%d:");
 builder.create(); // create an optional option
 builder.reset();
 builder.withPattern("%d:");
 builder.withPattern("!%d:");
 Option option = builder.create();
 assertEquals(NumberValidator.class, option.getValidator().getClass());
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionalOptionWithMultipleValidators() {
 builder = new PatternBuilder();
 builder.withPattern("d:s@java.lang.String");
 Option option = builder.create();
 assertEquals(NumberValidator.class, option.getValidator().getClass());
 assertEquals(String.class, option.getValidator().getArgumentType());
 }

 @Test
 public void testRequiredOptionWithMultipleValidators() {
 builder = new PatternBuilder();
 builder.withPattern("d:s@java.lang.String");
 builder.create(); // create an optional option
 builder.reset();
 builder.withPattern("d:s@java.lang.String");
 builder.withPattern("!d:s@java.lang.String");
 Option option = builder.create();
 assertEquals(NumberValidator.class, option.getValidator().getClass());
 assertEquals(String.class, option.getValidator().getArgumentType());
 assertTrue(option.isRequired());
 }

 @Test
 public void testGroupOption() {
 builder = new PatternBuilder();
 Option group = builder.create();
 builder.reset();
 builder.withPattern("g:");
 Option subOption = builder.create();
 group.addOption(subOption);
 assertEquals(Option.Builder.class, group.getClass());
 assertEquals("g", group.getShortName());
 assertNull(group.getLongOpt());
 assertTrue(group.isAllowedGroup());
 assertEquals(1, group.getOptions().size());
 assertEquals("g", group.getOptions().get(0).getShortName());
 }

 @Test
 public void testNestedGroupOption() {
 builder = new PatternBuilder();
 Option group = builder.create();
 builder.reset();
 builder.withPattern("g:");
 Option subGroup = builder.create();
 group.addOption(subGroup);
 Option subOption = builder.create();
 subGroup.addOption(subOption);
 assertEquals(Option.Builder.class, group.getClass());
 assertEquals("g", group.getShortName());
 assertNull(group.getLongOpt());
 assertTrue(group.isAllowedGroup());
 assertEquals(1, group.getOptions().size());
 assertEquals("g", group.getOptions().get(0).getShortName());
 assertEquals(Option.Builder.class, subGroup.getClass());
 assertEquals("g", subGroup.getShortName());
 assertNull(subGroup.getLongOpt());
 assertTrue(subGroup.isAllowedGroup());
 assertEquals(1, subGroup.getOptions().size());
 assertEquals("g", subGroup.getOptions().get(0).getShortName());
 }

 @Test
 public void testOptionWithTwoShortNames() {
 builder = new PatternBuilder();
 Option option = builder.create();
 builder.withPattern("aa:s");
 option = builder.create();
 assertEquals("aa", option.getShortName());
 assertSame(option, builder.getLastOptionCreated());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOption() {
 builder = new PatternBuilder();
 builder.withPattern("!");
 }
}