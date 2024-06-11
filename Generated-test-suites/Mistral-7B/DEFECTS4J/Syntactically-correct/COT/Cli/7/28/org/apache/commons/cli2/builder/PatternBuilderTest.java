package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;

public class PatternBuilderTest {

 private PatternBuilder patternBuilder;

 @Test
 public void testPatternBuilderCreate() {
 patternBuilder = new PatternBuilder();
 Option option = patternBuilder.create();
 assertNotNull(option);
 }

 @Test
 public void testPatternBuilderWithPatternSingleOption() {
 patternBuilder = new PatternBuilder();
 patternBuilder.withPattern("-a");
 Option option = patternBuilder.create();
 assertNotNull(option);
 assertNull(option.getArgument());
 }

 @Test
 public void testPatternBuilderWithPatternSingleRequiredOption() {
 patternBuilder = new PatternBuilder();
 patternBuilder.withPattern("-a:");
 Option option = patternBuilder.create();
 assertNotNull(option);
 assertNotNull(option.getArgument());
 }

 @Test
 public void testPatternBuilderWithPatternSingleOptionalArgument() {
 patternBuilder = new PatternBuilder();
 patternBuilder.withPattern("-a*");
 Option option = patternBuilder.create();
 assertNotNull(option);
 assertNull(option.getArgument());
 }

 @Test
 public void testPatternBuilderWithPatternMultipleOptions() {
 patternBuilder = new PatternBuilder();
 patternBuilder.withPattern("--foo -b: --bar");
 Option option1 = patternBuilder.create();
 Option option2 = patternBuilder.create();
 assertNotNull(option1);
 assertNull(option1.getArgument());
 assertNotNull(option2);
 assertNull(option2.getArgument());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPatternBuilderWithPatternInvalidPattern() {
 patternBuilder = new PatternBuilder();
 patternBuilder.withPattern("!");
 patternBuilder.create();
 }

 @Test
 public void testPatternBuilderWithPatternClassOption() {
 patternBuilder = new PatternBuilder();
 patternBuilder.withPattern("--foo:@org.apache.commons.cli2.MyClass");
 Option option = patternBuilder.create();
 assertNotNull(option);
 assertNotNull(option.getArgument());
 Validator validator = option.getValidator();
 assertNotNull(validator);
 assertInstanceOf(ClassValidator.class, validator);
 }

 @Test
 public void testPatternBuilderWithPatternNumberOption() {
 patternBuilder = new PatternBuilder();
 patternBuilder.withPattern("--foo:%d");
 Option option = patternBuilder.create();
 assertNotNull(option);
 assertNull(option.getArgument());
 Validator validator = option.getValidator();
 assertNotNull(validator);
 assertInstanceOf(NumberValidator.class, validator);
 }

 @Test
 public void testPatternBuilderWithPatternDateOption() {
 patternBuilder = new PatternBuilder();
 patternBuilder.withPattern("--foo:/d");
 Option option = patternBuilder.create();
 assertNotNull(option);
 assertNull(option.getArgument());
 Validator validator = option.getValidator();
 assertNotNull(validator);
 assertInstanceOf(DateValidator.class, validator);
 }

 @Test
 public void testPatternBuilderWithPatternFileOption() {
 patternBuilder = new PatternBuilder();
 patternBuilder.withPattern("--foo:<file>");
 Option option = patternBuilder.create();
 assertNotNull(option);
 assertNull(option.getArgument());
 Validator validator = option.getValidator();
 assertNotNull(validator);
 assertInstanceOf(FileValidator.class, validator);
 }

 @Test
 public void testPatternBuilderWithPatternFileExistingOption() {
 patternBuilder = new PatternBuilder();
 patternBuilder.withPattern("--foo:<file, exists>");
 Option option = patternBuilder.create();
 assertNotNull(option);
 assertNull(option.getArgument());
 Validator validator = option.getValidator();
 assertNotNull(validator);
 assertInstanceOf(FileValidator.class, validator);
 // Add check for file validation
 }

 @Test
 public void testPatternBuilderWithPatternUrlOption() {
 patternBuilder = new PatternBuilder();
 patternBuilder.withPattern("--foo:/>url<");
 Option option = patternBuilder.create();
 assertNotNull(option);
 assertNull(option.getArgument());
 Validator validator = option.getValidator();
 assertNotNull(validator);
 assertInstanceOf(UrlValidator.class, validator);
 }

}