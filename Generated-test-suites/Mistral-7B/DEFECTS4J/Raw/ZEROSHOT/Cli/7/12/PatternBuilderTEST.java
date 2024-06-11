package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testCreateEmptyPattern() {
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testCreateSingleOptionPattern() {
        patternBuilder.withPattern("a:");
        Option option = patternBuilder.create();
        assertEquals("a", option.getLongOpt());
        assertEquals(Option.UNBOUNDED_VALUE, option.getNumberOfArgs());
        assertFalse(option.isRequired());
        assertNull(option.getArgument());
    }

    @Test
    public void testCreateSingleOptionPatternWithArgument() {
        patternBuilder.withPattern("a:s");
        Option option = patternBuilder.create();
        assertEquals("a", option.getLongOpt());
        assertEquals(1, option.getNumberOfArgs());
        assertFalse(option.isRequired());
    }

    @Test
    public void testCreateSingleOptionPatternWithRequiredArgument() {
        patternBuilder.withPattern("a:s");
        patternBuilder.reset();
        patternBuilder.withPattern("a:s");
        Option option = patternBuilder.create();
        assertEquals("a", option.getLongOpt());
        assertEquals(1, option.getNumberOfArgs());
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreateMultipleOptionsPattern() {
        patternBuilder.withPattern("a:,b:i");
        Option optionA = patternBuilder.create();
        assertEquals("a", optionA.getLongOpt());
        assertEquals(Option.UNBOUNDED_VALUE, optionA.getNumberOfArgs());
        assertFalse(optionA.isRequired());
        assertNull(optionA.getArgument());

        Option optionB = patternBuilder.create();
        assertEquals("b", optionB.getLongOpt());
        assertEquals(1, optionB.getNumberOfArgs());
        assertFalse(optionB.isRequired());
    }

    @Test
    public void testCreateMultipleOptionsPatternWithGroup() {
        patternBuilder.withPattern("g {a:, b:i}");
        GroupBuilder groupBuilder = new GroupBuilder();
        patternBuilder.gbuilder = groupBuilder;
        Option optionGroup = patternBuilder.create();
        assertEquals("g", optionGroup.getLongOpt());
        assertEquals(0, optionGroup.getNumberOfArgs());
        assertFalse(optionGroup.isRequired());

        Option optionA = ((Option)optionGroup.getChildren().get(0));
        assertEquals("a", optionA.getLongOpt());
        assertEquals(Option.UNBOUNDED_VALUE, optionA.getNumberOfArgs());
        assertFalse(optionA.isRequired());
        assertNull(optionA.getArgument());

        Option optionB = ((Option)optionGroup.getChildren().get(1));
        assertEquals("b", optionB.getLongOpt());
        assertEquals(1, optionB.getNumberOfArgs());
        assertFalse(optionB.isRequired());
    }

    @Test
    public void testCreateMultipleOptionsPatternWithValidator() {
        patternBuilder.withPattern("i:/");
        Option option = patternBuilder.create();
        assertEquals("i", option.getLongOpt());
        assertEquals(1, option.getNumberOfArgs());
        assertFalse(option.isRequired());
        assertTrue(option.hasValidators());
        Set validators = option.getValidators();
        assertEquals(1, validators.size());
        Validator validator = (Validator)validators.iterator().next();
        assertTrue(validator instanceof UrlValidator);
    }

    @Test
    public void testCreateMultipleOptionsPatternWithMultipleValidators() {
        patternBuilder.withPattern("s:@java.lang.String,i:#2018-01-01");
        Option option = patternBuilder.create();
        assertEquals("s", option.getLongOpt());
        assertEquals(1, option.getNumberOfArgs());
        assertFalse(option.isRequired());
        assertTrue(option.hasValidators());
        Set validators = option.getValidators();
        assertEquals(2, validators.size());
        Validator stringValidator = (Validator)validators.iterator().next();
        assertTrue(stringValidator instanceof ClassValidator);
        assertEquals(String.class, ((ClassValidator)stringValidator).getClazz());

        Validator dateValidator = (Validator)validators.iterator().next();
        assertTrue(dateValidator instanceof DateValidator);
        assertEquals("2018-01-01", ((DateValidator)dateValidator).getFormatString());
    }

    @Test
    public void testCreateMultipleOptionsPatternWithClassValidator() {
        patternBuilder.withPattern("c:@java.lang.MyClass");
        Option option = patternBuilder.create();
        assertEquals("c", option.getLongOpt());
        assertEquals(1, option.getNumberOfArgs());
        assertFalse(option.isRequired());
        assertTrue(option.hasValidators());
        Set validators = option.getValidators();
        assertEquals(1, validators.size());
        Validator validator = (Validator)validators.iterator().next();
        assertTrue(validator instanceof ClassValidator);
        assertEquals(MyClass.class, ((ClassValidator)validator).getClazz());
    }

    @Test
    public void testCreateMultipleOptionsPatternWithNumberValidator() {
        patternBuilder.withPattern("n:%d");
        Option option = patternBuilder.create();
        assertEquals("n", option.getLongOpt());
        assertEquals(1, option.getNumberOfArgs());
        assertFalse(option.isRequired());
        assertTrue(option.hasValidators());
        Set validators = option.getValidators();
        assertEquals(1, validators.size());
        Validator validator = (Validator)validators.iterator().next();
        assertTrue(validator instanceof NumberValidator);
    }

    @Test
    public void testCreateMultipleOptionsPatternWithFileValidator() {
        patternBuilder.withPattern("f:<");
        Option option = patternBuilder.create();
        assertEquals("f", option.getLongOpt());
        assertEquals(1, option.getNumberOfArgs());
        assertFalse(option.isRequired());
        assertTrue(option.hasValidators());
        Set validators = option.getValidators();
        assertEquals(1, validators.size());
        Validator validator = (Validator)validators.iterator().next();
        assertTrue(validator instanceof FileValidator);
        assertTrue(((FileValidator)validator).isExisting());
        assertTrue(((FileValidator)validator).isFile());
    }

    @Test
    public void testCreateMultipleOptionsPatternWithFileValidatorAndArgument() {
        patternBuilder.withPattern("d:>");
        patternBuilder.reset();
        patternBuilder.withPattern("d:>arg");
        Option option = patternBuilder.create();
        assertEquals("d", option.getLongOpt());
        assertEquals(1, option.getNumberOfArgs());
        assertTrue(option.isRequired());
        assertTrue(option.hasValidators());
        Set validators = option.getValidators();
        assertEquals(1, validators.size());
        Validator validator = (Validator)validators.iterator().next();
        assertTrue(validator instanceof FileValidator);
        assertFalse(((FileValidator)validator).isExisting());
        assertFalse(((FileValidator)validator).isFile());
        Argument argument = option.getArgument();
        assertSame("arg", argument.getName());
    }

    @Test
    public void testCreateMultipleOptionsPatternWithStar() {
        patternBuilder.withPattern("a:*");
        Option optionA = patternBuilder.create();
        assertEquals("a", optionA.getLongOpt());
        assertEquals(Option.UNBOUNDED_VALUE, optionA.getNumberOfArgs());
        assertFalse(optionA.isRequired());
        assertNull(optionA.getArgument());

        patternBuilder.reset();
        patternBuilder.withPattern("a:");
        Option optionB = patternBuilder.create();
        assertEquals("a", optionB.getLongOpt());
        assertEquals(1, optionB.getNumberOfArgs());
        assertFalse(optionB.isRequired());

        Option optionC = patternBuilder.create();
        assertEquals("a", optionC.getLongOpt());
        assertEquals(1, optionC.getNumberOfArgs());
        assertFalse(optionC.isRequired());
        assertSame(optionB, optionC.getPreviousOption());
    }

    @Test
    public void testCreateMultipleOptionsPatternWithSlash() {
        patternBuilder.withPattern("a:/");
        Option optionA = patternBuilder.create();
        assertEquals("a", optionA.getLongOpt());
        assertEquals(1, optionA.getNumberOfArgs());
        assertFalse(optionA.isRequired());
        assertTrue(optionA.hasValidators());
        Set validators = optionA.getValidators();
        assertEquals(1, validators.size());
        Validator validator = (Validator)validators.iterator().next();
        assertTrue(validator instanceof UrlValidator);

        patternBuilder.reset();
        Option optionB = patternBuilder.create();
        assertEquals("a", optionB.getLongOpt());
        assertEquals(1, optionB.getNumberOfArgs());
        assertFalse(optionB.isRequired());
        assertNull(optionB.getArgument());

        Option optionC = patternBuilder.create();
        assertEquals("a", optionC.getLongOpt());
        assertEquals(1, optionC.getNumberOfArgs());
        assertFalse(optionC.isRequired());
        assertSame(optionB, optionC.getPreviousOption());

        Option optionD = patternBuilder.create();
        assertEquals("a", optionD.getLongOpt());
        assertEquals(1, optionD.getNumberOfArgs());
        assertFalse(optionD.isRequired());
        assertSame(optionC, optionD.getPreviousOption());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateInvalidPattern() {
        patternBuilder.withPattern("@a:");
        Option option = patternBuilder.create();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateInvalidPatternWithArgument() {
        patternBuilder.withPattern("a:s@");
        Option option = patternBuilder.create();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateInvalidPatternWithGroup() {
        patternBuilder.withPattern("g {a:, b:}");
        Option option = patternBuilder.create();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateInvalidPatternWithStar() {
        patternBuilder.withPattern("a:s*");
        Option option = patternBuilder.create();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateInvalidPatternWithSlash() {
        patternBuilder.withPattern("a:/");
        Option option = patternBuilder.create();
        // this should not happen, but if it does, the test passes
    }

    private static class MyClass {
        // a simple class to validate with ClassValidator
    }
}