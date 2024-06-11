package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ValidationResult;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

 private PatternBuilder builder;

 @Before
 public void setup() {
 builder = new PatternBuilder();
 }

 @Test
 public void testBasicOption() {
 // Given
 builder.withPattern("a:");
 Option option = builder.create();

 // When
 ValidationResult result = option.validate();

 // Then
 assertFalse(result.hasErrors());
 // add more assertions for typical and edge cases
 }

 @Test
 public void testOptionalOptionWithDefaultValue() {
 // Given
 builder.withPattern("a:i{1,3}");
 Option option = builder.create();

 // When
 ValidationResult result = option.validate();

 // Then
 assertFalse(result.hasErrors());
 // add more assertions for typical and edge cases
 }

 @Test
 public void testRequiredOption() {
 // Given
 builder.withPattern("a:i{1,3}");
 Option option = builder.create();
 option.setRequired(true);

 // When
 ValidationResult result = option.validate();

 // Then
 assertTrue(result.hasErrors());
 // add more assertions for typical and edge cases
 }

 @Test
 public void testOptionalOptionWithNoValue() {
 // Given
 builder.withPattern("a:");
 Option option = builder.create();
 option.setRequired(false);

 // When
 ValidationResult result = option.validate();

 // Then
 assertTrue(result.hasErrors());
 // add more assertions for typical and edge cases
 }

 @Test
 public void testOptionWithArgument() {
 // Given
 builder.withPattern("a:s");
 Option option = builder.create();
 Argument arg = option.getArgument();

 // When
 ValidationResult result = option.validate();
 String value = "test";
 arg.setValue(value);
 result = option.validate();

 // Then
 assertFalse(result.hasErrors());
 // add more assertions for typical and edge cases
 }

 @Test
 public void testComplexPattern() {
 // Given
 builder.withPattern("a:i{1,3}@com.example.MyClass");
 Option option = builder.create();

 // When
 ValidationResult result = option.validate();

 // Then
 assertFalse(result.hasErrors());
 // add more assertions for typical and edge cases
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPattern() {
 // Given
 builder.withPattern("a:x");
 Option option = builder.create();

 // When
 option.validate();

 // Then
 fail("Should have thrown an IllegalArgumentException");
 }
}