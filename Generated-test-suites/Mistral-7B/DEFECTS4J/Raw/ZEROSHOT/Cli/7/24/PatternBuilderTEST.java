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
import org.apache.commons.cli2.validator.Validator;
import org.junit.Test;

/**
 * Tests for the PatternBuilder class
 */
public class PatternBuilderTest {

    @Test
    public void testPatternBuilder() {
        // Test empty pattern
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNull(option);

        // Test simple pattern
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:");
        Option option1 = patternBuilder.create();
        assertEquals("a", option1.getLongOpt());
        assertEquals(null, option1.getArgument());
        assertNull(option1.getShortName());
        assertNull(option1.isRequired());

        // Test pattern with required argument
        patternBuilder.reset();
        patternBuilder.withPattern("a:s");
        Option option2 = patternBuilder.create();
        assertEquals("a", option2.getLongOpt());
        Argument argument = option2.getArgument();
        assertEquals("s", argument.getName());
        assertNull(option2.getShortName());
        assertTrue(option2.isRequired());

        // Test pattern with optional argument
        patternBuilder.reset();
        patternBuilder.withPattern("a:s");
        option2 = patternBuilder.create();
        assertEquals("a", option2.getLongOpt());
        argument = option2.getArgument();
        assertEquals("s", argument.getName());
        assertNull(option2.getShortName());
        assertFalse(option2.isRequired());

        // Test pattern with multiple optional arguments
        patternBuilder.reset();
        patternBuilder.withPattern("a:s,b:s,c:s");
        Option option3 = patternBuilder.create();
        assertEquals("a", option3.getLongOpt());
        Argument argument1 = option3.getArgument();
        assertEquals("s", argument1.getName());
        assertNull(option3.getShortName());
        assertFalse(option3.isRequired());

        Option option4 = patternBuilder.create();
        assertEquals("b", option4.getLongOpt());
        Argument argument2 = option4.getArgument();
        assertEquals("s", argument2.getName());
        assertNull(option4.getShortName());
        assertFalse(option4.isRequired());

        Option option5 = patternBuilder.create();
        assertEquals("c", option5.getLongOpt());
        Argument argument3 = option5.getArgument();
        assertEquals("s", argument3.getName());
        assertNull(option5.getShortName());
        assertFalse(option5.isRequired());

        // Test pattern with validator
        patternBuilder.reset();
        patternBuilder.withPattern("a:@java.lang.String");
        Option option6 = patternBuilder.create();
        assertEquals("a", option6.getLongOpt());
        assertNull(option6.getArgument());
        assertNull(option6.getShortName());
        assertNull(option6.isRequired());
        assertEquals(ClassValidator.class, option6.getValidator().getClass());

        // Test pattern with multiple validators
        patternBuilder.reset();
        patternBuilder.withPattern("a:@java.lang.String,b:#yyyy-MM-dd");
        Option option7 = patternBuilder.create();
        assertEquals("a", option7.getLongOpt());
        assertNull(option7.getArgument());
        assertNull(option7.getShortName());
        assertNull(option7.isRequired());
        assertEquals(ClassValidator.class, option7.getValidator().getClass());
        assertEquals(DateValidator.class, option7.getValidator(1).getClass());

        // Test pattern with different validators for each argument
        patternBuilder.reset();
        patternBuilder.withPattern("a:@java.lang.String,b:*:@org.apache.commons.io.FileUtils.FileType");
        Option option8 = patternBuilder.create();
        assertEquals("a", option8.getLongOpt());
        Argument argument4 = option8.getArgument();
        assertEquals("String", argument4.getClass().getName());
        assertNull(option8.getShortName());
        assertNull(option8.isRequired());
        assertEquals(ClassValidator.class, option8.getValidator().getClass());
        assertEquals(1, option8.getValidators().size());
        assertEquals(FileValidator.class, option8.getValidator(0).getClass());

        Argument argument5 = option8.getArgument(1);
        assertEquals("FileType", argument5.getClass().getName());
        assertNull(option8.getShortName());
        assertNull(option8.isRequired());
        assertEquals(1, option8.getValidators().size());
        assertEquals(FileValidator.class, option8.getValidator(0).getClass());
    }
}