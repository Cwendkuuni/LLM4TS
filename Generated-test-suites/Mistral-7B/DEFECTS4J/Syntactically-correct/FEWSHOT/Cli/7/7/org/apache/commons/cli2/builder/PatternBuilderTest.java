package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder builder = new PatternBuilder();
        Option option = builder.create();
        assertNotNull(option);
    }

    @Test
    public void testCreateMultiple() {
        PatternBuilder builder = new PatternBuilder();
        Option option1 = builder.create();
        Option option2 = builder.create();
        assertEquals(2, builder.options.size());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder builder = new PatternBuilder();

        builder.withPattern("a:");
        Option option = builder.create();
        Argument argument = (Argument) option.getArgument();
        assertNotNull(argument);
        assertEquals("", argument.getName());
        assertEquals(0, argument.getMinimum());
        assertEquals(1, argument.getMaximum());

        builder.reset();

        builder.withPattern("a:b:c:");
        option = builder.create();
        argument = (Argument) option.getArgument();
        assertNotNull(argument);
        assertEquals("b", argument.getName());
        assertEquals(0, argument.getMinimum());
        assertEquals(1, argument.getMaximum());

        builder.reset();

        builder.withPattern("a:b:c:d*");
        option = builder.create();
        argument = (Argument) option.getArgument();
        assertNotNull(argument);
        assertEquals("d", argument.getName());
        assertEquals(0, argument.getMinimum());
        assertEquals(-1, argument.getMaximum());

        builder.reset();

        builder.withPattern("a:b:c:d/");
        option = builder.create();
        assertNull(option.getArgument());
    }

    @Test
    public void testWithPatternWithValidator() {
        PatternBuilder builder = new PatternBuilder();

        builder.withPattern("a:@java.lang.String");
        Option option = builder.create();
        assertEquals(ClassValidator.class, option.getValidator().getClass());

        builder.reset();

        builder.withPattern("a:%");
        option = builder.create();
        assertEquals(NumberValidator.class, option.getValidator().getClass());

        builder.reset();

        builder.withPattern("a:#");
        option = builder.create();
        assertEquals(DateValidator.class, option.getValidator().getClass());

        builder.reset();

        builder.withPattern("a:<");
        option = builder.create();
        assertEquals(FileValidator.class, option.getValidator().getClass());

        builder.reset();

        builder.withPattern("a:>");
        option = builder.create();
        assertEquals(FileValidator.class, option.getValidator().getClass());

        builder.reset();

        builder.withPattern("a:*/");
        option = builder.create();
        assertEquals(UrlValidator.class, option.getValidator().getClass());
    }

    @Test
    public void testWithPatternWithShortName() {
        PatternBuilder builder = new PatternBuilder();

        builder.withPattern("a:b:c:");
        Option option = builder.create();
        assertEquals('b', option.getShortName().charAt(0));

        builder.reset();

        builder.withPattern("a:--option:");
        option = builder.create();
        assertEquals("option", option.getLongName());

        builder.reset();

        builder.withPattern("a:-o:");
        option = builder.create();
        assertEquals("o", option.getShortName());
    }

    @Test
    public void testWithPatternWithRequired() {
        PatternBuilder builder = new PatternBuilder();

        builder.withPattern("a:");
        Option option = builder.create();
        assertTrue(option.isRequired());

        builder.reset();

        builder.withPattern("a:b");
        option = builder.create();
        assertFalse(option.isRequired());
    }

    @Test(expected = IllegalStateException.class)
    public void testWithPatternInvalid() {
        PatternBuilder builder = new PatternBuilder();

        builder.withPattern("a:a:");
    }
}