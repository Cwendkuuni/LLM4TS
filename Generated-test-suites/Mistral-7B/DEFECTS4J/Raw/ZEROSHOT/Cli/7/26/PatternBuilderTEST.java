package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @org.junit.Test
    public void testPatternBuilder() {
        // Test empty pattern
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.getShortName() == null);
        assertTrue(option.getLongName() == null);
        assertTrue(option.getArguments() == null);

        // Test simple pattern
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("o:");
        option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.getShortName() == 'o');
        assertTrue(option.getLongName() == null);
        assertTrue(option.getArguments() == null);

        // Test pattern with argument
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("s:s");
        option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.getShortName() == 's');
        assertTrue(option.getLongName() == null);
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().getName() == "s");

        // Test pattern with validator
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("i:@java.lang.Integer");
        option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.getShortName() == 'i');
        assertTrue(option.getLongName() == null);
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof ClassValidator);

        // Test pattern with multiple validators
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("f:<./test.txt>");
        option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.getShortName() == 'f');
        assertTrue(option.getLongName() == null);
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof FileValidator);
        assertTrue(((FileValidator)option.getValidator()).isExisting());
        assertTrue(((FileValidator)option.getValidator()).isFile());

        // Test pattern with required argument
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:*");
        option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.getShortName() == null);
        assertTrue(option.getLongName() == null);
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().isRequired());

        // Test pattern with multiple options
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("go:s,i:@java.lang.Integer");
        Option option1 = patternBuilder.create();
        Option option2 = patternBuilder.create();
        assertNotNull(option1);
        assertNotNull(option2);
        assertTrue(option1.getShortName() == 'g');
        assertTrue(option1.getLongName() == null);
        assertNotNull(option1.getArgument());
        assertTrue(option1.getArgument().getName() == "s");
        assertTrue(option2.getShortName() == 'i');
        assertTrue(option2.getLongName() == null);
        assertNotNull(option2.getValidator());
        assertTrue(option2.getValidator() instanceof ClassValidator);
    }
}