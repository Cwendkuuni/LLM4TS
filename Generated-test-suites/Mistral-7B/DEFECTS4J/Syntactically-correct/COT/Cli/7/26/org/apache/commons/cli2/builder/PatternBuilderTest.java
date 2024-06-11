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

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

 private PatternBuilder pb;

 @Before
 public void setUp() {
 pb = new PatternBuilder();
 }

 /**
 * Test create method
 */
 @Test
 public void testCreateMethod() {
 Option option = pb.create();
 assertNotNull(option);
 }

 /**
 * Test create with empty pattern
 */
 @Test
 public void testCreateWithEmptyPattern() {
 pb.reset();
 Option option = pb.create();
 assertNull(option);
 }

 /**
 * Test create with simple pattern
 */
 @Test
 public void testCreateWithSimplePattern() {
 pb.withPattern("a:");
 Option option = pb.create();
 assertEquals("a", option.getLongOpt());
 assertNull(option.getArgument());
 }

 /**
 * Test create with required argument
 */
 @Test
 public void testCreateWithRequiredArgument() {
 pb.withPattern("a:");
 pb.create();
 reset(pb);
 pb.withPattern("a:b");
 Option option = pb.create();
 assertEquals("a", option.getLongOpt());
 assertNotNull(option.getArgument());
 }

 /**
 * Test create with optional argument
 */
 @Test
 public void testCreateWithOptionalArgument() {
 pb.withPattern("a:");
 Option option1 = pb.create();
 assertNull(option1.getArgument());

 pb.reset();
 pb.withPattern("a:b");
 Option option2 = pb.create();
 assertEquals("b", option2.getArgument().getName());
 assertNull(option2.getArgument().getValue());
 }

 /**
 * Test create with multiple options
 */
 @Test
 public void testCreateWithMultipleOptions() {
 pb.withPattern("a:,b:");
 Option optionA = pb.create();
 assertEquals("a", optionA.getLongOpt());
 assertNull(optionA.getArgument());

 Option optionB = pb.create();
 assertEquals("b", optionB.getLongOpt());
 assertNull(optionB.getArgument());
 }

 /**
 * Test create with validators
 */
 @Test
 public void testCreateWithValidators() {
 pb.withPattern("i:@com.example.MyClass");
 Option option = pb.create();
 assertNotNull(option.getValidator());
 assertTrue(option.getValidator() instanceof ClassValidator);
 }

 /**
 * Test create with date validator
 */
 @Test
 public void testCreateWithDateValidator() {
 pb.withPattern("d:/");
 Option option = pb.create();
 assertFalse(option.getValidator() instanceof DateValidator);

 pb.reset();
 pb.withPattern("d:/dd/MM/yyyy");
 Option optionDate = pb.create();
 assertTrue(optionDate.getValidator() instanceof DateValidator);
 }

 /**
 * Test create with file validator
 */
 @Test
 public void testCreateWithFileValidator() {
 pb.withPattern("f:<");
 Option option = pb.create();
 assertFalse(option.getValidator() instanceof FileValidator);

 pb.reset();
 pb.withPattern("f:>");
 Option optionFile = pb.create();
 assertTrue(optionFile.getValidator() instanceof FileValidator);
 }

 /**
 * Test create with number validator
 */
 @Test
 public void testCreateWithNumberValidator() {
 pb.withPattern("n:%d");
 Option option = pb.create();
 assertFalse(option.getValidator() instanceof NumberValidator);

 pb.reset();
 pb.withPattern("n:%d");
 Option optionNumber = pb.create();
 assertTrue(optionNumber.getValidator() instanceof NumberValidator);
 }

 /**
 * Test create with url validator
 */
 @Test
 public void testCreateWithUrlValidator() {
 pb.withPattern("u:/");
 Option option = pb.create();
 assertFalse(option.getValidator() instanceof UrlValidator);

 pb.reset();
 pb.withPattern("u:/");
 Option optionUrl = pb.create();
 assertTrue(optionUrl.getValidator() instanceof UrlValidator);
 }

 /**
 * Test create with invalid pattern
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithInvalidPattern() {
 pb.withPattern("x:");
 Option option = pb.create();
 fail("Should not reach here");
 }

 /**
 * Test reset method
 */
 @Test
 public void testResetMethod() {
 pb.withPattern("a:b");
 Option option = pb.create();
 assertEquals("a", option.getLongOpt());
 assertNotNull(option.getArgument());

 pb.reset();
 Option option2 = pb.create();
 assertNull(option2.getArgument());
 }
}