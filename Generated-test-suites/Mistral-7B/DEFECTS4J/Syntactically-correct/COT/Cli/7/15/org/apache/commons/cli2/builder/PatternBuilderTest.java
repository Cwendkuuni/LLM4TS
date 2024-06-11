package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.junit.Test;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @Test
    public void testPatternBuilderCreation() {
        PatternBuilder patternBuilder = new PatternBuilder();
        assertNotNull(patternBuilder);
    }

    @Test
    public void testCreateOptionWithSingleCharacterType() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("t:");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testCreateOptionWithRequiredFlag() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("n:!");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreateOptionWithMultipleCharacterType() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("p:%@");
        Option option = patternBuilder.create();
        assertNotNull(option.getArgName());
        assertTrue(option.getValueValidator() instanceof ClassValidator);
    }

    @Test(expected = AssertionError.class)
    public void testCreateOptionWithInvalidCharacter() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("p:x:");
        patternBuilder.create();
    }

    @Test
    public void testCreateMultipleOptionsWithSameShortName() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("t:,t:");
        Option option1 = patternBuilder.create();
        Option option2 = patternBuilder.create();
        assertNotNull(option1);
        assertNotNull(option2);
        assertTrue(option1.getShortName().equals(option2.getShortName()));
    }

    @Test
    public void testCreateOptionWithArgName() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:*argName");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.getArgName().equals("argName"));
    }

    @Test
    public void testCreateOptionWithValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("%:");
        Option option = patternBuilder.create();
        assertNotNull(option.getValueValidator());
        assertTrue(option.getValueValidator() instanceof NumberValidator);
    }

    @Test
    public void testCreateOptionWithFileValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("<:");
        Option option = patternBuilder.create();
        assertNotNull(option.getValueValidator());
        assertTrue(option.getValueValidator() instanceof FileValidator);
    }

    @Test
    public void testCreateOptionWithDateValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("#:");
        Option option = patternBuilder.create();
        assertNotNull(option.getValueValidator());
        assertTrue(option.getValueValidator() instanceof DateValidator);
    }

    @Test
    public void testCreateOptionWithUrlValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("/:");
        Option option = patternBuilder.create();
        assertNotNull(option.getValueValidator());
        assertTrue(option.getValueValidator() instanceof UrlValidator);
    }
}