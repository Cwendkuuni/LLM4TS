package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testPatternBuilder() {
        PatternBuilder builder = new PatternBuilder();
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
    }

    @Test
    public void testPatternBuilderWithPattern() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:@java.lang.String");
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        Argument argument = ((Option) option).getArgument();
        assertTrue(argument != null);
        assertTrue(argument instanceof Argument);
        Validator validator = argument.getValidator();
        assertTrue(validator instanceof ClassValidator);
        ClassValidator classValidator = (ClassValidator) validator;
        assertTrue(classValidator.getInstance().isInstance("testString"));

        builder.reset();
        builder.withPattern("b:+java.lang.Integer");
        option = builder.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        argument = ((Option) option).getArgument();
        assertTrue(argument != null);
        assertTrue(argument instanceof Argument);
        validator = argument.getValidator();
        assertTrue(validator instanceof NumberValidator);
        NumberValidator numberValidator = (NumberValidator) validator;
        assertTrue(numberValidator.getNumberInstance().isValid("123"));

        builder.reset();
        builder.withPattern("c:#yyyy-MM-dd'T'HH:mm:ss'Z'");
        option = builder.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        argument = ((Option) option).getArgument();
        assertTrue(argument != null);
        assertTrue(argument instanceof Argument);
        validator = argument.getValidator();
        assertTrue(validator instanceof DateValidator);
        DateValidator dateValidator = (DateValidator) validator;
        assertTrue(dateValidator.getDateInstance().isValid("2023-03-15T14:30:00Z"));

        builder.reset();
        builder.withPattern("d:<file>");
        option = builder.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        argument = ((Option) option).getArgument();
        assertTrue(argument != null);
        assertTrue(argument instanceof Argument);
        validator = argument.getValidator();
        assertTrue(validator instanceof FileValidator);
        FileValidator fileValidator = (FileValidator) validator;
        assertTrue(fileValidator.isValid("testFile.txt"));

        builder.reset();
        builder.withPattern("e:>file");
        option = builder.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        argument = ((Option) option).getArgument();
        assertTrue(argument != null);
        assertTrue(argument instanceof Argument);
        validator = argument.getValidator();
        assertTrue(validator instanceof FileValidator);
        FileValidator existingFileValidator = (FileValidator) validator;
        assertTrue(existingFileValidator.isValid("testFile.txt"));

        builder.reset();
        builder.withPattern("f:/*url");
        option = builder.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        argument = ((Option) option).getArgument();
        assertTrue(argument != null);
        assertTrue(argument instanceof Argument);
        validator = argument.getValidator();
        assertTrue(validator instanceof UrlValidator);
    }
}