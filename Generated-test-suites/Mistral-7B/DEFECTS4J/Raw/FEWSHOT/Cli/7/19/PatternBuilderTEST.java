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
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testPatternBuilder_Empty() {
        PatternBuilder builder = new PatternBuilder();
        Option option = builder.create();
        assertNull(option);
    }

    @Test
    public void testPatternBuilder_SingleCharacterOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("-a");
        Option option = builder.create();
        assertEquals("-a", option.getOpt());
        assertNull(option.getArgument());
    }

    @Test
    public void testPatternBuilder_SingleCharacterRequiredOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("-a!");
        Option option = builder.create();
        assertEquals("-a", option.getOpt());
        assertNull(option.getArgument());
        assertTrue(option.isRequired());
    }

    @Test
    public void testPatternBuilder_SingleCharacterOptionalWithArgumentOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("-a:");
        Option option = builder.create();
        assertEquals("-a", option.getOpt());
        assertNotNull(option.getArgument());
    }

    @Test
    public void testPatternBuilder_SingleCharacterOptionalWithArgumentOptionWithArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("-a:arg");
        Option option = builder.create();
        assertEquals("-a", option.getOpt());
        assertNotNull(option.getArgument());
        assertEquals("arg", option.getArgument().getName());
    }

    @Test
    public void testPatternBuilder_MultipleCharacterOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("--foo");
        Option option = builder.create();
        assertEquals("--foo", option.getOpt());
        assertNull(option.getArgument());
    }

    @Test
    public void testPatternBuilder_MultipleCharacterRequiredOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("--foo!");
        Option option = builder.create();
        assertEquals("--foo", option.getOpt());
        assertNull(option.getArgument());
        assertTrue(option.isRequired());
    }

    @Test
    public void testPatternBuilder_MultipleCharacterOptionalWithArgumentOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("--foo:");
        Option option = builder.create();
        assertEquals("--foo", option.getOpt());
        assertNotNull(option.getArgument());
    }

    @Test
    public void testPatternBuilder_MultipleCharacterOptionalWithArgumentOptionWithArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("--foo:arg");
        Option option = builder.create();
        assertEquals("--foo", option.getOpt());
        assertNotNull(option.getArgument());
        assertEquals("arg", option.getArgument().getName());
    }

    @Test
    public void testPatternBuilder_WithValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("%d");
        Option option = builder.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof NumberValidator);
    }

    @Test
    public void testPatternBuilder_WithClassValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("@java.lang.String");
        Option option = builder.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof ClassValidator);
    }

    @Test
    public void testPatternBuilder_WithDateValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("#yyyy-MM-dd");
        Option option = builder.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof DateValidator);
    }

    @Test
    public void testPatternBuilder_WithFileValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("<file>");
        Option option = builder.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof FileValidator);
    }

    @Test
    public void testPatternBuilder_WithFileValidatorExisting() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("<existingFile>");
        Option option = builder.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof FileValidator);
        assertTrue(((FileValidator)option.getValidator()).isExisting());
    }

    @Test
    public void testPatternBuilder_WithUrlValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("/url");
        Option option = builder.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof UrlValidator);
    }

    @Test
    public void testPatternBuilder_WithMultipleValidators() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern(":%s %s");
        Option option = builder.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof NumberValidator);
        assertTrue(((NumberValidator)option.getValidator()).getMinimum() == 1);
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof StringValidator);
    }
}