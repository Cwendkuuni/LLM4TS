package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Test;

public class PatternBuilderTest {

 private PatternBuilder patternBuilder;

 @Test
 public void testPatternBuilderInstance() {
 patternBuilder = new PatternBuilder();
 assertEquals(3, patternBuilder.getBuilders().size());
 }

 @Test
 public void testCreateOptionWithNoArguments() {
 patternBuilder.reset();
 patternBuilder.withPattern("ab");
 Option option = patternBuilder.create();
 assertNull(option.getArgument());
 }

 @Test
 public void testCreateOptionWithSingleArgument() {
 patternBuilder.reset();
 patternBuilder.withPattern("a:s");
 Option option = patternBuilder.create();
 Argument argument = option.getArgument();
 assertEquals("s", argument.getName());
 assertNull(argument.getValidator());
 }

 @Test
 public void testCreateOptionWithValidator() {
 patternBuilder.reset();
 patternBuilder.withPattern("a:@F");
 Option option = patternBuilder.create();
 Argument argument = option.getArgument();
 assertNull(argument);
 assertEquals(FileValidator.class, option.getValidator().getClass());
 }

 @Test
 public void testCreateOptionWithMultipleArguments() {
 patternBuilder.reset();
 patternBuilder.withPattern("a:*:s:i");
 Option option = patternBuilder.create();
 Argument argument1 = option.getArgument(0);
 assertEquals("s", argument1.getName());
 Argument argument2 = option.getArgument(1);
 assertEquals("i", argument2.getName());
 assertNull(option.getValidator());
 }

 @Test
 public void testCreateOptionWithMinAndMaxArguments() {
 patternBuilder.reset();
 patternBuilder.withPattern("a:s{1,3}");
 Option option = patternBuilder.create();
 Argument argument = option.getArgument();
 assertEquals(new String[]{"s", "s", "s"}, argument.getOptions());
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateOptionWithNoPattern() {
 patternBuilder.reset();
 patternBuilder.create();
 }

 @Test
 public void testCreateOptionWithInvalidPattern() {
 patternBuilder.reset();
 patternBuilder.withPattern("!");
 }
}