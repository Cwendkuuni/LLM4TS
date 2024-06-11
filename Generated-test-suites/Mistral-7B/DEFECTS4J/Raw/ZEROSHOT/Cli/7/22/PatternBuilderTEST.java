package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    /**
     * Test creating a single option
     */
    @Test
    public void testCreateSingleOption() {
        patternBuilder.withPattern("a:");
        Option option = patternBuilder.create();

        assertEquals("a", option.getLongOpt());
        assertEquals('a', option.getOpt());
        assertNull(option.getArgument());
    }

    /**
     * Test creating a required option
     */
    @Test
    public void testCreateRequiredOption() {
        patternBuilder.withPattern("a:!");
        Option option = patternBuilder.create();

        assertEquals("a", option.getLongOpt());
        assertEquals('a', option.getOpt());
        assertNull(option.getArgument());
        assertTrue(option.isRequired());
    }

    /**
     * Test creating an option with an argument
     */
    @Test
    public void testCreateOptionWithArgument() {
        patternBuilder.withPattern("a:s");
        Option option = patternBuilder.create();

        assertEquals("a", option.getLongOpt());
        assertEquals('a', option.getOpt());
        assertNotNull(option.getArgument());
    }

    /**
     * Test creating an option with a class validator
     */
    @Test
    public void testCreateOptionWithClassValidator() {
        patternBuilder.withPattern("a:@FooBar");
        Option option = patternBuilder.create();

        assertEquals("a", option.getLongOpt());
        assertEquals('a', option.getOpt());
        assertNull(option.getArgument());
        assertEquals("FooBar.class", option.getValidator().getClass().getName());
    }

    /**
     * Test creating an option with a number validator
     */
    @Test
    public void testCreateOptionWithNumberValidator() {
        patternBuilder.withPattern("a:%d");
        Option option = patternBuilder.create();

        assertEquals("a", option.getLongOpt());
        assertEquals('a', option.getOpt());
        assertNull(option.getArgument());
        assertEquals(NumberValidator.class, option.getValidator().getClass());
    }

    /**
     * Test creating an option with a date validator
     */
    @Test
    public void testCreateOptionWithDateValidator() {
        patternBuilder.withPattern("a:%<d");
        Option option = patternBuilder.create();

        assertEquals("a", option.getLongOpt());
        assertEquals('a', option.getOpt());
        assertNull(option.getArgument());
        assertEquals(DateValidator.class, option.getValidator().getClass());
    }

    /**
     * Test creating an option with a file validator
     */
    @Test
    public void testCreateOptionWithFileValidator() {
        patternBuilder.withPattern("a:<s");
        Option option = patternBuilder.create();

        assertEquals("a", option.getLongOpt());
        assertEquals('a', option.getOpt());
        assertNull(option.getArgument());
        assertEquals(FileValidator.class, option.getValidator().getClass());
    }

    /**
     * Test creating multiple options
     */
    @Test
    public void testCreateMultipleOptions() {
        patternBuilder.withPattern("a:s b:s c:d");
        Option optionA = patternBuilder.create();
        Option optionB = patternBuilder.create();
        Option optionC = patternBuilder.create();

        assertEquals("a", optionA.getLongOpt());
        assertEquals('a', optionA.getOpt());
        assertNotNull(optionA.getArgument());

        assertEquals("b", optionB.getLongOpt());
        assertEquals('b', optionB.getOpt());
        assertNotNull(optionB.getArgument());

        assertEquals("c", optionC.getLongOpt());
        assertEquals('c', optionC.getOpt());
        assertNull(optionC.getArgument());
    }

    /**
     * Test creating multiple options with the same short name
     */
    @Test(expected = IllegalStateException.class)
    public void testCreateMultipleOptionsWithSameShortName() {
        patternBuilder.withPattern("a:s a:s");
        patternBuilder.create();
    }

    /**
     * Test creating an option with a wildcard argument
     */
    @Test
    public void testCreateOptionWithWildcardArgument() {
        patternBuilder.withPattern("a:s*");
        Option option = patternBuilder.create();

        assertEquals("a", option.getLongOpt());
        assertEquals('a', option.getOpt());
        assertNotNull(option.getArgument());
    }

    /**
     * Test creating an option with a wildcard argument and a validator
     */
    @Test
    public void testCreateOptionWithWildcardArgumentAndValidator() {
        patternBuilder.withPattern("a:s*/%s");
        Option option = patternBuilder.create();

        assertEquals("a", option.getLongOpt());
        assertEquals('a', option.getOpt());
        assertEquals(2, option.getArguments().size());
        assertEquals("file", option.getArguments().get(0).getName());
        assertEquals("value", option.getArguments().get(1).getName());
    }

    /**
     * Test creating an option with a multiple arguments
     */
    @Test
    public void testCreateOptionWithMultipleArguments() {
        patternBuilder.withPattern("a:s,s");
        Option option = patternBuilder.create();

        assertEquals("a", option.getLongOpt());
        assertEquals('a', option.getOpt());
        assertEquals(2, option.getArguments().size());
        assertEquals("file1", option.getArguments().get(0).getName());
        assertEquals("file2", option.getArguments().get(1).getName());
    }

    /**
     * Test creating an option with a required argument
     */
    @Test(expected = IllegalStateException.class)
    public void testCreateOptionWithRequiredArgument() {
        patternBuilder.withPattern("a:s:");
        patternBuilder.create();
    }

    /**
     * Test creating an option with a minimum argument greater than the maximum number of arguments
     */
    @Test(expected = IllegalStateException.class)
    public void testCreateOptionWithMinimumGreaterThanMaximum() {
        patternBuilder.withPattern("a:s{2,1}");
        patternBuilder.create();
    }

    /**
     * Test creating an option with a date validator and a null argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithDateValidatorAndNullArgument() {
        patternBuilder.withPattern("a:%<d");
        patternBuilder.create();
        patternBuilder.reset();

        patternBuilder.create();
    }

    /**
     * Test creating an option with a file validator and a null argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithFileValidatorAndNullArgument() {
        patternBuilder.withPattern("a:<s");
        patternBuilder.create();
        patternBuilder.reset();

        patternBuilder.create();
    }

    /**
     * Test creating an option with a url validator and a null argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithUrlValidatorAndNullArgument() {
        patternBuilder.withPattern("a:/s");
        patternBuilder.create();
        patternBuilder.reset();

        patternBuilder.create();
    }

    /**
     * Test creating an option with a number validator and a null argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithNumberValidatorAndNullArgument() {
        patternBuilder.withPattern("a:%d");
        patternBuilder.create();
        patternBuilder.reset();

        patternBuilder.create();
    }

    /**
     * Test creating an option with a class validator and a null argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithClassValidatorAndNullArgument() {
        patternBuilder.withPattern("a:@FooBar");
        patternBuilder.create();
        patternBuilder.reset();

        patternBuilder.create();
    }

    /**
     * Test creating an option with a number validator and a non-numeric argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithNumberValidatorAndNonNumericArgument() {
        patternBuilder.withPattern("a:%d");
        patternBuilder.create();
        patternBuilder.reset();

        patternBuilder.withArguments(new Argument[]{ new Argument("arg") });
        patternBuilder.create();
    }

    /**
     * Test creating an option with a class validator and a non-existent class
     */
    @Test(expected = ClassNotFoundException.class)
    public void testCreateOptionWithClassValidatorAndNonExistentClass() {
        patternBuilder.withPattern("a:@NonExistentClass");
        patternBuilder.create();
    }

    /**
     * Test creating an option with a class validator and a null class
     */
    @Test(expected = NullPointerException.class)
    public void testCreateOptionWithClassValidatorAndNullClass() {
        patternBuilder.withPattern("a:@null");
        patternBuilder.create();
    }

    /**
     * Test creating an option with a date validator and a null argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithDateValidatorAndNullArgumentDate() {
        patternBuilder.withPattern("a:%<d");
        patternBuilder.create();
        patternBuilder.reset();

        patternBuilder.create();
    }

    /**
     * Test creating an option with a file validator and a null argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithFileValidatorAndNullArgumentFile() {
        patternBuilder.withPattern("a:<s");
        patternBuilder.create();
        patternBuilder.reset();

        patternBuilder.create();
    }

    /**
     * Test creating an option with a url validator and a null argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithUrlValidatorAndNullArgumentUrl() {
        patternBuilder.withPattern("a:/s");
        patternBuilder.create();
        patternBuilder.reset();

        patternBuilder.create();
    }

    /**
     * Test creating an option with a number validator and a negative number
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithNumberValidatorAndNegativeNumber() {
        patternBuilder.withPattern("a:%d");
        patternBuilder.create();
        patternBuilder.reset();

        patternBuilder.withArguments(new Argument[]{ new Argument("-1") });
        patternBuilder.create();
    }

    /**
     * Test creating an option with a url validator and an invalid url
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithUrlValidatorAndInvalidUrl() {
        patternBuilder.withPattern("a:/s");
        patternBuilder.create();
        patternBuilder.reset();

        patternBuilder.withArguments(new Argument[]{ new Argument("invalid url") });
        patternBuilder.create();
    }

    /**
     * Test creating an option with a date validator and a null date
     */
    @Test(expected = NullPointerException.class)
    public void testCreateOptionWithDateValidatorAndNullDate() {
        patternBuilder.withPattern("a:%<d");
        patternBuilder.create();
        patternBuilder.reset();

        patternBuilder.create();
    }

    /**
     * Test creating an option with a date validator and a past date
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithDateValidatorAndPastDate() {
        patternBuilder.withPattern("a:%<d");
        patternBuilder.create();
        patternBuilder.reset();

        patternBuilder.withArguments(new Argument[]{ new Argument("2022-01-31") });
        patternBuilder.create();
    }

    /**
     * Test creating an option with a date validator and a present date
     */
    @Test
    public void testCreateOptionWithDateValidatorAndPresentDate() {
        patternBuilder.withPattern("a:%<d");
        patternBuilder.create();
        patternBuilder.reset();

        patternBuilder.withArguments(new Argument[]{ new Argument(LocalDate.now().toString()) });
        Option option = patternBuilder.create();

        assertEquals(LocalDate.now(), option.getValues().get(0));
    }

    /**
     * Test creating an option with a date validator and a future date
     */
    @Test
    public void testCreateOptionWithDateValidatorAndFutureDate() {
        patternBuilder.withPattern("a:%<d");
        patternBuilder.create();
        patternBuilder.reset();

        patternBuilder.withArguments(new Argument[]{ new Argument("2022-02-01") });
        Option option = patternBuilder.create();

        assertEquals(LocalDate.of(2022, 2, 1), option.getValues().get(0));
    }

    /**
     * Test creating an option with a file validator and an invalid file
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithFileValidatorAndInvalidFile() {
        patternBuilder.withPattern("a:<s");
        patternBuilder.create();
        patternBuilder.reset();

        patternBuilder.withArguments(new Argument[]{ new Argument("invalid file") });
        patternBuilder.create();
    }

    /**
     * Test creating an option with a file validator and an existing file
     */
    @Test
    public void testCreateOptionWithFileValidatorAndExistingFile() {
        patternBuilder.withPattern("a:<s");
        patternBuilder.create();
        patternBuilder.reset();

        String filePath = "target/test-classes/PatternBuilderTestFile.txt";
        patternBuilder.withArguments(new Argument[]{ new Argument(filePath) });
        Option option = patternBuilder.create();

        assertEquals(filePath, option.getValues().get(0));
    }

    /**
     * Test creating an option with a class validator and a valid class
     */
    @Test
    public void testCreateOptionWithClassValidatorAndValidClass() {
        patternBuilder.withPattern("a:@org.apache.commons.cli2.builder.PatternBuilderTest");
        patternBuilder.create();
        patternBuilder.reset();

        Option option = patternBuilder.create();

        assertEquals(PatternBuilderTest.class, option.getValues().get(0).getClass());
    }

    /**
     * Test creating an option with a class validator and a non-existent class
     */
    @Test(expected = ClassNotFoundException.class)
    public void testCreateOptionWithClassValidatorAndNonExistentClass2() {
        patternBuilder.withPattern("a:@NonExistentClass2");
        patternBuilder.create();
    }
}