package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertEquals;
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
        PatternBuilder builder = new PatternBuilder();
        Option option = builder.create();
        assertNotNull(option);
    }

    @Test
    public void testCreateMultipleOptions() {
        PatternBuilder builder = new PatternBuilder();
        Option option1 = builder.create();
        Option option2 = builder.create();
        assertEquals(2, builder.options.size());
    }

    @Test
    public void testWithPatternSingleOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("p:");
        Option option = builder.create();
        assertEquals("p", option.getShortName());
        assertEquals(null, option.getArgument());
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPatternOptionalWithArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("p:s");
        Option option = builder.create();
        assertEquals("p", option.getShortName());
        assertNotNull(option.getArgument());
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithPatternRequiredWithArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("p:s!");
        Option option = builder.create();
        assertEquals("p", option.getShortName());
        assertNotNull(option.getArgument());
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithPatternOptionalWithNoArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("p:");
        Option option = builder.create();
        assertEquals("p", option.getShortName());
        assertNull(option.getArgument());
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPatternRequiredWithNoArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("p:!");
        Option option = builder.create();
        assertEquals("p", option.getShortName());
        assertNull(option.getArgument());
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithPatternOptionalWithArgumentAndValidators() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("p:%d!");
        Option option = builder.create();
        assertEquals("p", option.getShortName());
        assertNotNull(option.getArgument());
        assertTrue(option.isRequired());
        assertEquals(NumberValidator.class, option.getValidator().getClass());
    }

    @Test
    public void testWithPatternRequiredWithArgumentAndValidators() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("p:%s!");
        Option option = builder.create();
        assertEquals("p", option.getShortName());
        assertNotNull(option.getArgument());
        assertTrue(option.isRequired());
        assertNull(option.getValidator());
    }

    @Test
    public void testWithPatternWithLongOptionName() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("longOptionName::");
        Option option = builder.create();
        assertEquals("longOptionName", option.getLongName());
        assertNull(option.getArgument());
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPatternWithShortAndLongOptionName() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("s,longOptionName::");
        Option option = builder.create();
        assertEquals("s", option.getShortName());
        assertEquals("longOptionName", option.getLongName());
        assertNull(option.getArgument());
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPatternWithArgumentAndValidatorsAndShortAndLongOptionName() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("p:%s:@java.lang.String!");
        Option option = builder.create();
        assertEquals("p", option.getShortName());
        assertNotNull(option.getArgument());
        assertTrue(option.isRequired());
        assertEquals(ClassValidator.class, option.getValidator().getClass());
        assertEquals(String.class, ((ClassValidator) option.getValidator()).getInstanceType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithPatternInvalidPattern() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("p:!");
        builder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithPatternInvalidPattern2() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("p:%:");
        builder.create();
    }

    @Test
    public void testWithPatternWithMultipleShortOptionNames() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("p,q:s");
        Option option1 = builder.create();
        Option option2 = builder.create();
        assertEquals("p", option1.getShortName());
        assertEquals("q", option2.getShortName());
        assertNotNull(option1.getArgument());
        assertNotNull(option2.getArgument());
        assertFalse(option1.isRequired());
        assertFalse(option2.isRequired());
    }
}