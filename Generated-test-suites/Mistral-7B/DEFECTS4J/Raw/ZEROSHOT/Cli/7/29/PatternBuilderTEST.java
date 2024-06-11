package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;

/**
 * Tests for PatternBuilder class
 */
public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    /**
     * Test creating a pattern builder instance
     */
    @Test
    public void testPatternBuilderInstance() {
        PatternBuilder patternBuilder = new PatternBuilder();
        assertNotNull(patternBuilder);
    }

    /**
     * Test creating an Option instance with no pattern set
     */
    @Test
    public void testCreateOptionNoPattern() {
        Option option = patternBuilder.create();
        assertNull(option);
    }

    /**
     * Test creating an Option instance with a pattern set
     */
    @Test
    public void testCreateOptionWithPattern() {
        patternBuilder.withPattern("a:");
        Option option = patternBuilder.create();
        assertEquals("a", option.getLongOpt());
        assertEquals('a', option.getOpt());
        assertFalse(option.isRequired());
        assertNull(option.getArgument());

        patternBuilder.reset();
        patternBuilder.withPattern("a:s");
        option = patternBuilder.create();
        assertEquals("a", option.getLongOpt());
        assertEquals('a', option.getOpt());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
    }

    /**
     * Test creating options with different patterns
     */
    @Test
    public void testCreateOptionsWithDifferentPatterns() {
        patternBuilder.withPattern("a:s");
        Option option1 = patternBuilder.create();

        patternBuilder.reset();
        patternBuilder.withPattern("b:i");
        Option option2 = patternBuilder.create();

        patternBuilder.reset();
        patternBuilder.withPattern("c:@java.lang.String");
        Option option3 = patternBuilder.create();

        patternBuilder.reset();
        patternBuilder.withPattern("d:/#{yyyy-MM-dd}");
        Option option4 = patternBuilder.create();

        Set<Option> options = new HashSet<>(Arrays.asList(option1, option2, option3, option4));
        Option groupOption = patternBuilder.create();
        assertEquals(4, groupOption.getOptions().size());
        assertEquals(options, groupOption.getOptions());
    }

    /**
     * Test setting a required option
     */
    @Test
    public void testCreateRequiredOption() {
        patternBuilder.withPattern("a:s");
        patternBuilder.reset();
        patternBuilder.withPattern("b:s!");
        Option option = patternBuilder.create();
        assertTrue(option.isRequired());
    }

    /**
     * Test setting a non-required option
     */
    @Test
    public void testCreateNonRequiredOption() {
        patternBuilder.withPattern("a:s");
        patternBuilder.reset();
        patternBuilder.withPattern("b:s");
        Option option = patternBuilder.create();
        assertFalse(option.isRequired());
    }

    /**
     * Test setting an option with a validator
     */
    @Test
    public void testCreateOptionWithValidator() {
        patternBuilder.withPattern("a:%");
        patternBuilder.reset();
        patternBuilder.withPattern("b:#");
        Option option = patternBuilder.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof DateValidator);
    }

    /**
     * Test setting an option with a class validator
     */
    @Test
    public void testCreateOptionWithClassValidator() {
        patternBuilder.withPattern("a:@java.lang.String");
        patternBuilder.reset();
        patternBuilder.withPattern("b:@java.util.Date");
        Option option = patternBuilder.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof ClassValidator);
    }

    /**
     * Test setting an option with a file validator
     */
    @Test
    public void testCreateOptionWithFileValidator() {
        patternBuilder.withPattern("a:<");
        patternBuilder.reset();
        patternBuilder.withPattern("b:<");
        Option option = patternBuilder.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof FileValidator);
    }

    /**
     * Test setting an option with a url validator
     */
    @Test
    public void testCreateOptionWithUrlValidator() {
        patternBuilder.withPattern("a:/");
        patternBuilder.reset();
        patternBuilder.withPattern("b:/");
        Option option = patternBuilder.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof UrlValidator);
    }

    /**
     * Test setting an option with a number validator
     */
    @Test
    public void testCreateOptionWithNumberValidator() {
        patternBuilder.withPattern("a:%");
        patternBuilder.reset();
        patternBuilder.withPattern("b:%i");
        Option option = patternBuilder.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof NumberValidator);
    }

    /**
     * Test creating multiple options with the same name
     */
    @Test(expected = IllegalStateException.class)
    public void testCreateMultipleOptionsWithSameName() {
        patternBuilder.withPattern("a:s");
        patternBuilder.reset();
        patternBuilder.withPattern("a:s");
        patternBuilder.create();
    }

    /**
     * Test creating multiple options with the same long opt name
     */
    @Test(expected = IllegalStateException.class)
    public void testCreateMultipleOptionsWithSameLongOptName() {
        patternBuilder.withPattern("a:s");
        patternBuilder.reset();
        patternBuilder.withPattern("--long-opt:s");
        patternBuilder.create();
    }

    /**
     * Test creating multiple options with the same short opt name
     */
    @Test(expected = IllegalStateException.class)
    public void testCreateMultipleOptionsWithSameShortOptName() {
        patternBuilder.withPattern("a:s");
        patternBuilder.reset();
        patternBuilder.withPattern("-s:s");
        patternBuilder.create();
    }
}