package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
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
    public void testPatternBuilder_Constructor() {
        PatternBuilder patternBuilder = new PatternBuilder();
        assertNotNull(patternBuilder);

        PatternBuilder patternBuilder1 = new PatternBuilder(
            new GroupBuilder(),
            new DefaultOptionBuilder(),
            new ArgumentBuilder());
        assertNotNull(patternBuilder1);
    }

    @Test
    public void testCreateOption_RequiredAndNotTypeSpecified_ShouldCreateOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("n");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testCreateOption_NotRequiredAndNotTypeSpecified_ShouldNotCreateOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("s");
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testCreateOption_RequiredAndWithTypeSpecified_ShouldCreateOptionWithValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("@");
        Option option = patternBuilder.create();
        assertNotNull(option);
        Validator validator = ((Option) option).getValidator();
        assertNotNull(validator);
        assertInstanceOf(ClassValidator.class, validator);
    }

    @Test
    public void testCreateOption_NotRequiredAndWithTypeSpecified_ShouldCreateOptionWithoutValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("%");
        Option option = patternBuilder.create();
        assertNotNull(option);
        Validator validator = ((Option) option).getValidator();
        assertNull(validator);
    }

    @Test(expected = AssertionError.class)
    public void testCreateOption_MultipleTypesSpecified_ShouldThrowException() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("%@");
        patternBuilder.create();
    }

    @Test
    public void testCreateOption_SameTypeSpecifiedMultipleTimes_ShouldCreateOneOptionWithMultiplesValidators() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("%%");
        Option option = patternBuilder.create();
        assertNotNull(option);
        Validator[] validators = ((Option) option).getValidators();
        assertNotNull(validators);
        assertInstanceOf(NumberValidator.class, validators[0]);
        assertInstanceOf(NumberValidator.class, validators[1]);
    }

    @Test
    public void testCreateMultipleOptions() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("n:s");
        Option option1 = patternBuilder.create();
        Option option2 = patternBuilder.create();
        assertNotNull(option1);
        assertNotNull(option2);
    }

    @Test
    public void testCreateMultipleOptionsWithSameShortName_ShouldThrowException() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("n:s n:s");
        assertThrows(IllegalArgumentException.class, () -> patternBuilder.create());
    }

    @Test
    public void testCreateMultipleOptionsWithSameLongName_ShouldCreateDifferentOptions() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("n:s n:l s:s");
        Option option1 = patternBuilder.create();
        Option option2 = patternBuilder.create();
        assertNotNull(option1);
        assertNotNull(option2);
        assertNotSame(option1, option2);
    }

    @Test
    public void testCreateOption_WithArgument_ShouldCreateOptionWithArgument() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("n:s");
        Option option = patternBuilder.create();
        Argument argument = ((Option) option).getArgument();
        assertNotNull(argument);
    }

    @Test
    public void testCreateOption_WithoutArgument_ShouldCreateOptionWithoutArgument() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("n");
        Option option = patternBuilder.create();
        Argument argument = ((Option) option).getArgument();
        assertNull(argument);
    }
}