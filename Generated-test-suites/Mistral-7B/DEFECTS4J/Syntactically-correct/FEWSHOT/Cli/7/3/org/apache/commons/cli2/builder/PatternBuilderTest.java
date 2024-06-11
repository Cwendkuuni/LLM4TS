package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;

import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreateOptionWithNoPattern() {
        PatternBuilder builder = new PatternBuilder();
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(option.isUnbound());
    }

    @Test
    public void testCreateOptionWithValidPattern() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:");
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(option.isBound());
        assertEquals("a", option.getShortName());
    }

    @Test
    public void testCreateOptionWithRequiredArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s");
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(option.isBound());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.isRequired());
    }

    @Test
    public void testCreateOptionWithOptionalArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s");
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(option.isBound());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertFalse(argument.isRequired());
    }

    @Test
    public void testCreateOptionWithValidators() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("i:@java.lang.Integer");
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(option.isBound());
        Argument argument = option.getArgument();
        assertNull(argument);
        assertEquals(NumberValidator.class, option.getValidator().getClass());

        builder.reset();
        builder.withPattern("d:#");
        option = builder.create();
        assertNotNull(option);
        assertTrue(option.isBound());
        argument = option.getArgument();
        assertNull(argument);
        assertEquals(DateValidator.class, option.getValidator().getClass());

        builder.reset();
        builder.withPattern("f:<");
        option = builder.create();
        assertNotNull(option);
        assertTrue(option.isBound());
        argument = option.getArgument();
        assertNull(argument);
        assertEquals(FileValidator.class, option.getValidator().getClass());
    }

    @Test
    public void testCreateMultipleOptions() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s,b:s,c:s");
        Option optionA = builder.create();
        Option optionB = builder.create();
        Option optionC = builder.create();
        assertNotNull(optionA);
        assertNotNull(optionB);
        assertNotNull(optionC);
        assertEquals("a", optionA.getShortName());
        assertEquals("b", optionB.getShortName());
        assertEquals("c", optionC.getShortName());
    }

    @Test
    public void testCreateOptionWithInvalidPattern() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s%");
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(option.isUnbound());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithEmptyPattern() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("");
        Option option = builder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithInvalidCharacters() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:!@%#$");
    }

    @Test
    public void testCreateOptionWithLongPattern() {
        PatternBuilder builder = new PatternBuilder();
        String longPattern = "a:s,b:s,c:s,d:#,e:@java.lang.Integer,f:<,g:@org.apache.commons.cli2.builder.PatternBuilderTest$TestClass,h:/";
        builder.withPattern(longPattern);
        Option optionA = builder.create();
        Option optionB = builder.create();
        Option optionC = builder.create();
        Option optionD = builder.create();
        Option optionE = builder.create();
        Option optionF = builder.create();
        Option optionG = builder.create();
        Option optionH = builder.create();
        assertNotNull(optionA);
        assertNotNull(optionB);
        assertNotNull(optionC);
        assertNotNull(optionD);
        assertNotNull(optionE);
        assertNotNull(optionF);
        assertNotNull(optionG);
        assertNotNull(optionH);
        assertEquals("a", optionA.getShortName());
        assertEquals("b", optionB.getShortName());
        assertEquals("c", optionC.getShortName());
        assertEquals("d", optionD.getShortName());
        assertEquals("e", optionE.getShortName());
        assertEquals("f", optionF.getShortName());
        assertEquals("g", optionG.getShortName());
        assertEquals("h", optionH.getShortName());
    }

    @Test
    public void testCreateOptionWithClassValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("c:@java.lang.String");
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(option.isBound());
        Argument argument = option.getArgument();
        assertNull(argument);
        assertEquals(ClassValidator.class, option.getValidator().getClass());
        assertTrue(option.getValidator().isValid("Hello"));
        assertTrue(option.getValidator().isValid("World"));
        assertFalse(option.getValidator().isValid(123));
    }

    @Test
    public void testCreateOptionWithNumberValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("i:%");
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(option.isBound());
        Argument argument = option.getArgument();
        assertNull(argument);
        assertEquals(NumberValidator.class, option.getValidator().getClass());
        assertTrue(option.getValidator().isValid(1));
        assertTrue(option.getValidator().isValid(10));
        assertTrue(option.getValidator().isValid(1.2));
        assertTrue(option.getValidator().isValid(10.5));
        assertFalse(option.getValidator().isValid("Hello"));
        assertFalse(option.getValidator().isValid(null));
        assertFalse(option.getValidator().isValid(new Object()));
    }

    @Test
    public void testCreateOptionWithDateValidator() {
        PatternBuilder builder = new PatternBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        builder.withPattern("d:#");
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(option.isBound());
        Argument argument = option.getArgument();
        assertNull(argument);
        assertEquals(DateValidator.class, option.getValidator().getClass());
        assertTrue(option.getValidator().isValid(LocalDate.of(2022, 1, 1)));
        assertTrue(option.getValidator().isValid(LocalDate.of(2022, 12, 31)));
        assertFalse(option.getValidator().isValid(LocalDate.of(1999, 12, 31)));
        assertFalse(option.getValidator().isValid(LocalDate.of(2022, 13, 31)));
    }

    @Test
    public void testCreateOptionWithFileValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("f:<");
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(option.isBound());
        Argument argument = option.getArgument();
        assertNull(argument);
        assertEquals(FileValidator.class, option.getValidator().getClass());
        assertTrue(option.getValidator().isValid("path/to/existing/file.txt"));
        assertFalse(option.getValidator().isValid("path/to/non-existing/file.txt"));
        assertFalse(option.getValidator().isValid("path/to/directory"));
    }

    @Test
    public void testCreateOptionWithUrlValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("h:/");
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(option.isBound());
        Argument argument = option.getArgument();
        assertNull(argument);
        assertEquals(UrlValidator.class, option.getValidator().getClass());
        assertTrue(option.getValidator().isValid("http://example.com"));
        assertTrue(option.getValidator().isValid("https://example.com"));
        assertTrue(option.getValidator().isValid("https://user:password@example.com:8080/path?query=value#fragment"));
        assertFalse(option.getValidator().isValid("file:/path/to/existing/file.txt"));
        assertFalse(option.getValidator().isValid("path/to/non-existing"));
    }

}