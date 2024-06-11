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

 private PatternBuilder pb;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 pb = new PatternBuilder();
 }

 @Test
 public void testCreateOption() {
 Option option = pb.create();
 assertNotNull(option);
 }

 @Test
 public void testWithPattern() {
 // Test creating an option with no arguments
 pb.withPattern("t:");
 Option option1 = pb.create();
 assertEquals("t", option1.getOpt());
 assertNull(option1.getArgument());

 // Test creating an option with an optional argument
 pb.withPattern("t:s");
 Option option2 = pb.create();
 assertEquals("t", option2.getOpt());
 assertNotNull(option2.getArgument());

 // Test creating an option with a required argument
 pb.withPattern("t:s");
 pb.create(); // create an option without an argument to clear the builder
 pb.withPattern("t:s");
 Option option3 = pb.create();
 assertEquals("t", option3.getOpt());
 assertNotNull(option3.getArgument());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidPattern() {
 // Test invalid pattern, missing opt character
 pb.withPattern(":s");
 }

 @Test
 public void testValidators() {
 // Testing ClassValidator
 pb.withPattern("C:<className>");
 Option option1 = pb.create();
 assertNotNull(option1.getArgument());
 assertNotNull(option1.getValidator());
 assertTrue(option1.getValidator() instanceof ClassValidator);

 // Testing NumberValidator
 pb.withPattern("N:<number>");
 Option option2 = pb.create();
 assertNotNull(option2.getArgument());
 assertNotNull(option2.getValidator());
 assertTrue(option2.getValidator() instanceof NumberValidator);

 // Testing DateValidator
 pb.withPattern("D:<date>");
 Option option3 = pb.create();
 assertNotNull(option3.getArgument());
 assertNotNull(option3.getValidator());
 assertTrue(option3.getValidator() instanceof DateValidator);

 // Testing FileValidator for existing file
 pb.withPattern("F:<file>");
 Option option4 = pb.create();
 assertNotNull(option4.getArgument());
 assertNotNull(option4.getValidator());
 assertTrue(option4.getValidator() instanceof FileValidator);
 ((FileValidator)option4.getValidator()).assertFileExists();

 // Testing FileValidator for non-existing file
 pb.withPattern("F:<non-existent-file>");
 Option option5 = pb.create();
 assertNotNull(option5.getArgument());
 assertNotNull(option5.getValidator());
 assertTrue(option5.getValidator() instanceof FileValidator);
 ((FileValidator)option5.getValidator()).assertFileDoesNotExist();

 // Testing UrlValidator
 pb.withPattern("U:<url>");
 Option option6 = pb.create();
 assertNotNull(option6.getArgument());
 assertNotNull(option6.getValidator());
 assertTrue(option6.getValidator() instanceof UrlValidator);
 }
}