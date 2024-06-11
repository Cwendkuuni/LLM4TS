package org.apache.commons.cli2.builder;

import static org.junit.Assert.*;

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

 @Before
 public void setUp() {
 patternBuilder = new PatternBuilder();
 }

 @Test
 public void testCreateDefaultOption() {
 Option option = patternBuilder.create();
 assertNotNull(option);
 }

 @Test
 public void testCreateOptionWithShortNameAndArgument() {
 patternBuilder.withPattern("-t:");
 Option option = patternBuilder.create();
 assertNotNull(option);
 assertNotNull(option.getLongOpt());
 assertNotNull(option.getArgument());
 }

 @Test
 public void testCreateOptionWithShortNameAndNoArgument() {
 patternBuilder.withPattern("-t");
 Option option = patternBuilder.create();
 assertNotNull(option);
 assertNull(option.getArgument());
 }

 @Test
 public void testCreateOptionWithLongNameAndArgument() {
 patternBuilder.withPattern("--test:");
 Option option = patternBuilder.create();
 assertNotNull(option);
 assertNotNull(option.getLongOpt());
 assertNotNull(option.getArgument());
 }

 @Test
 public void testCreateOptionWithLongNameAndNoArgument() {
 patternBuilder.withPattern("--test");
 Option option = patternBuilder.create();
 assertNotNull(option);
 assertNull(option.getArgument());
 }

 @Test
 public void testCreateOptionWithSingleCharShortNameAndArgument() {
 patternBuilder.withPattern("-t:s");
 Option option = patternBuilder.create();
 assertNotNull(option);
 assertNotNull(option.getArgument());
 }

 @Test
 public void testCreateOptionWithSingleCharShortNameAndNoArgument() {
 patternBuilder.withPattern("-t:");
 Option option1 = patternBuilder.create();
 assertNotNull(option1);
 assertNull(option1.getArgument());
 patternBuilder.reset();
 patternBuilder.withPattern("-t");
 Option option2 = patternBuilder.create();
 assertNotNull(option2);
 assertNull(option2.getArgument());
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateOptionWithMultipleCharShortNameAndArgument() {
 patternBuilder.withPattern("-ts:");
 Option option = patternBuilder.create();
 fail("Should throw an IllegalStateException");
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateOptionWithMultipleCharShortNameAndNoArgument() {
 patternBuilder.withPattern("-ts:");
 Option option = patternBuilder.create();
 fail("Should throw an IllegalStateException");
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateOptionWithShortNameAndLongName() {
 patternBuilder.withPattern("-t --test");
 Option option = patternBuilder.create();
 fail("Should throw an IllegalStateException");
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateOptionWithLongNameAndShortName() {
 patternBuilder.withPattern("--test -t");
 Option option = patternBuilder.create();
 fail("Should throw an IllegalStateException");
 }

 @Test
 public void testCreateOptionWithOptionalValue() {
 patternBuilder.withPattern("-t:s");
 Option option = patternBuilder.create();
 assertNotNull(option.getValueState());
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateOptionWithSameCharShortNameAndDifferentLongName() {
 patternBuilder.withPattern("-t --test2");
 Option option = patternBuilder.create();
 fail("Should throw an IllegalStateException");
 }

 @Test
 public void testCreateOptionWithClassValidator() {
 patternBuilder.withPattern("-c:Class");
 Option option = patternBuilder.create();
 assertNotNull(option.getArgument());
 assertNotNull(option.getValidator());
 assertTrue(option.getValidator() instanceof ClassValidator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateOptionWithInvalidClassValidator() {
 patternBuilder.withPattern("-c:Integer");
 Option option = patternBuilder.create();
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testCreateOptionWithNumberValidator() {
 patternBuilder.withPattern("-i:int");
 Option option = patternBuilder.create();
 assertNotNull(option.getArgument());
 assertNotNull(option.getValidator());
 assertTrue(option.getValidator() instanceof NumberValidator);
 }

 @Test
 public void testCreateOptionWithDateValidator() {
 patternBuilder.withPattern("-d:Date");
 Option option = patternBuilder.create();
 assertNotNull(option.getArgument());
 assertNotNull(option.getValidator());
 assertTrue(option.getValidator() instanceof DateValidator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateOptionWithInvalidDateValidator() {
 patternBuilder.withPattern("-d:String");
 Option option = patternBuilder.create();
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testCreateOptionWithFileValidatorExistingFile() {
 patternBuilder.withPattern("-f:File");
 Option option = patternBuilder.create();
 assertNotNull(option.getArgument());
 assertNotNull(option.getValidator());
 assertTrue(option.getValidator() instanceof FileValidator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateOptionWithFileValidatorNonExistingFile() {
 patternBuilder.withPattern("-f:File");
 Option option = patternBuilder.create();
 patternBuilder.reset();
 patternBuilder.withPattern("-f:nonExistingFile");
 Option option2 = patternBuilder.create();
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateOptionWithFileValidatorDirectory() {
 patternBuilder.withPattern("-f:File");
 Option option = patternBuilder.create();
 patternBuilder.reset();
 patternBuilder.withPattern("-f:./");
 Option option2 = patternBuilder.create();
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testCreateOptionWithUrlValidator() {
 patternBuilder.withPattern("-u:URL");
 Option option = patternBuilder.create();
 assertNotNull(option.getArgument());
 assertNotNull(option.getValidator());
 assertTrue(option.getValidator() instanceof UrlValidator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateOptionWithInvalidUrlValidator() {
 patternBuilder.withPattern("-u:Integer");
 Option option = patternBuilder.create();
 fail("Should throw an IllegalArgumentException");
 }
}