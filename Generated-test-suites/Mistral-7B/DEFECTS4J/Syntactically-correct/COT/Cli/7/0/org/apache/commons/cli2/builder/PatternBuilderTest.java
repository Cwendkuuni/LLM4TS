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

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @Test
    public void testPatternBuilderCreation() {
        PatternBuilder builder = new PatternBuilder();
        assertNotNull(builder);
    }

    @Test
    public void testPatternBuilderCreateOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:");
        Option option = builder.create();
        assertNotNull(option);
        assertNull(option.getValue());
    }

    @Test
    public void testPatternBuilderCreateRequiredOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:!");
        Option option = builder.create();
        assertNotNull(option);
        assertNull(option.getValue());
        assertNotNull(option.isRequired());
    }

    @Test
    public void testPatternBuilderCreateOptionalArg() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:");
        Option option = builder.create();
        Argument arg = option.getArgument();
        assertNotNull(arg);
        assertNull(arg.getValue());
    }

    @Test
    public void testPatternBuilderCreateArgWithValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:%");
        Option option = builder.create();
        Argument arg = option.getArgument();
        Validator validator = arg.getValidator();
        assertNotNull(validator);
        assertInstanceOf(NumberValidator.class, validator);
    }

    @Test
    public void testPatternBuilderCreateArgWithMaxLimit() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:+{2}");
        Option option = builder.create();
        Argument arg = option.getArgument();
        int max = arg.getMaximum();
        assertEquals(2, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatternBuilderInvalidPattern() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:z");
        builder.create();
    }

    @Test
    public void testPatternBuilderCreateArgWithMinLimitAndMaxLimit() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:{1,3}");
        Option option = builder.create();
        Argument arg = option.getArgument();
        int min = arg.getMinimum();
        int max = arg.getMaximum();
        assertEquals(1, min);
        assertEquals(3, max);
    }

    @Test
    public void testPatternBuilderCreateArgWithMinLimit() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:{1}");
        Option option = builder.create();
        Argument arg = option.getArgument();
        int min = arg.getMinimum();
        assertEquals(1, min);
    }

    @Test
    public void testPatternBuilderCreateArgWithoutMinLimit() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:");
        Option option = builder.create();
        Argument arg = option.getArgument();
        int min = arg.getMinimum();
        assertEquals(0, min);
    }

    @Test
    public void testPatternBuilderCreateArgWithClassValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:@java.lang.String");
        Option option = builder.create();
        Argument arg = option.getArgument();
        Validator validator = arg.getValidator();
        assertNotNull(validator);
        assertInstanceOf(ClassValidator.class, validator);
    }

    @Test
    public void testPatternBuilderCreateArgWithClassValidatorAndInstance() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:@java.lang.String{true}");
        Option option = builder.create();
        Argument arg = option.getArgument();
        Validator validator = arg.getValidator();
        assertNotNull(validator);
        assertInstanceOf(ClassValidator.class, validator);
        assertTrue(arg.getInstanceIsRequired());
    }

    @Test
    public void testPatternBuilderCreateArgWithDateValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:#{yyyy-MM-dd}");
        Option option = builder.create();
        Argument arg = option.getArgument();
        Validator validator = arg.getValidator();
        assertNotNull(validator);
        assertInstanceOf(DateValidator.class, validator);
    }

    @Test
    public void testPatternBuilderCreateArgWithFileValidatorExisting() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:<{file,exists}");
        Option option = builder.create();
        Argument arg = option.getArgument();
        Validator validator = arg.getValidator();
        assertNotNull(validator);
        assertInstanceOf(FileValidator.class, validator);
        assertTrue(arg.isFileRequired());
        assertTrue(arg.isExisting());
    }

    @Test
    public void testPatternBuilderCreateArgWithFileValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:<");
        Option option = builder.create();
        Argument arg = option.getArgument();
        Validator validator = arg.getValidator();
        assertNotNull(validator);
        assertInstanceOf(FileValidator.class, validator);
    }

    @Test
    public void testPatternBuilderCreateArgWithURLValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:/{url}");
        Option option = builder.create();
        Argument arg = option.getArgument();
        Validator validator = arg.getValidator();
        assertNotNull(validator);
        assertInstanceOf(UrlValidator.class, validator);
    }
}