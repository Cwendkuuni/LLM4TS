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
    public void testCreateOption() {
        final PatternBuilder builder = new PatternBuilder();
        builder.createOption('a', false, 'a');
        assertNotNull(builder.create());
    }

    @Test
    public void testCreateMultipleOptions() {
        final PatternBuilder builder = new PatternBuilder();
        builder.createOption('a', false, 'a');
        builder.createOption('b', false, 'b');
        final Option option = builder.create();
        assertEquals(2, option.getArgs().size());
    }

    @Test
    public void testCreateRequiredOption() {
        final PatternBuilder builder = new PatternBuilder();
        builder.createOption('a', true, 'a');
        builder.createOption('b', false, 'b');
        final Option option = builder.create();
        assertNotNull(option.getArgs().get(0));
    }

    @Test
    public void testCreateOptionalOption() {
        final PatternBuilder builder = new PatternBuilder();
        builder.createOption('a', false, 'a');
        builder.createOption('b', false, 'b');
        final Option option = builder.create();
        assertNull(option.getArgs().get(0));
    }

    @Test
    public void testCreateArgumentOption() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:");
        final Option option = builder.create();
        assertNotNull(option);
        assertNotNull(option.getArgument().getName());
        assertNull(option.getArgument().getValue());
    }

    @Test
    public void testCreateArgumentOptionWithValue() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s");
        final Option option = builder.create();
        assertNotNull(option);
        assertNotNull(option.getArgument().getValue());
        assertEquals("", option.getArgument().getValue());
    }

    @Test
    public void testCreateRequiredArgumentOption() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s");
        builder.create(); // create an empty option
        builder.reset();
        builder.withPattern("a:s");
        builder.create(); // create a required argument option
        final Option option = builder.create();
        assertNotNull(option.getArgument().getValue());
        assertEquals("", option.getArgument().getValue());
    }

    @Test
    public void testCreateClassOption() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("A:");
        final Option option = builder.create();
        assertNotNull(option.getValidator());
        assertInstanceOf(ClassValidator.class, option.getValidator());
    }

    @Test
    public void testCreateNumberOption() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("i:");
        final Option option = builder.create();
        assertNotNull(option.getValidator());
        assertInstanceOf(NumberValidator.class, option.getValidator());
    }

    @Test
    public void testCreateDateOption() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("d:");
        final Option option = builder.create();
        assertNotNull(option.getValidator());
        assertInstanceOf(DateValidator.class, option.getValidator());
    }

    @Test
    public void testCreateFileOption() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("f:");
        final Option option = builder.create();
        assertNotNull(option.getValidator());
        assertInstanceOf(FileValidator.class, option.getValidator());
    }

    @Test
    public void testCreateExistingFileOption() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("f:<");
        final Option option = builder.create();
        assertNotNull(option.getValidator());
        assertInstanceOf(FileValidator.class, option.getValidator());
        assertTrue(option.getValidator() instanceof FileValidator);
        assertTrue(((FileValidator)option.getValidator()).isExisting());
    }

    @Test
    public void testCreateUrlOption() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("u:");
        final Option option = builder.create();
        assertNotNull(option.getValidator());
        assertInstanceOf(UrlValidator.class, option.getValidator());
    }

    @Test
    public void testCreatePatternWithMultipleOptions() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s b:i c:@Foo");
        final Option option = builder.create();
        assertNotNull(option);
        assertEquals(3, option.getArgs().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreatePatternWithInvalidCharacter() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("x:");
        builder.create();
    }

    @Test
    public void testResetAfterCreation() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s");
        final Option option1 = builder.create();
        builder.reset();
        final Option option2 = builder.create();
        assertNotNull(option1);
        assertNotNull(option2);
        assertNotSame(option1, option2);
    }
}