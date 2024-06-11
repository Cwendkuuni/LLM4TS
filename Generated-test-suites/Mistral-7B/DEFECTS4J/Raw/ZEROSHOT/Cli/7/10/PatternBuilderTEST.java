package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class PatternBuilderTest {

 @Test
 public void testPatternBuilder() {
 PatternBuilder patternBuilder = new PatternBuilder();

 // Test empty pattern
 patternBuilder.reset();
 Option option = patternBuilder.create();
 assertNotNull(option);

 // Test simple option with no arguments
 patternBuilder.withPattern("a:");
 option = patternBuilder.create();
 assertNotNull(option);

 // Test option with required argument
 patternBuilder.withPattern("a:s");
 option = patternBuilder.create();
 assertNotNull(option.getArgument());

 // Test option with optional argument
 patternBuilder.withPattern("a:s");
 patternBuilder.reset();
 patternBuilder.withPattern("a:s{1,}");
 option = patternBuilder.create();
 assertNotNull(option.getArgument());

 // Test option with validator
 patternBuilder.withPattern("b:@java.lang.Integer");
 option = patternBuilder.create();
 assertNotNull(option.getValidator());

 // Test option with multiple validators
 patternBuilder.withPattern("c:#{yyyy-MM-dd HH:mm:ss},%d");
 option = patternBuilder.create();
 assertNotNull(option.getValidator());

 // Test option with file validator
 patternBuilder.withPattern("d:<.");
 option = patternBuilder.create();
 assertNotNull(option.getValidator());

 // Test option with url validator
 patternBuilder.withPattern("e:/");
 option = patternBuilder.create();
 assertNotNull(option.getValidator());
 }
}