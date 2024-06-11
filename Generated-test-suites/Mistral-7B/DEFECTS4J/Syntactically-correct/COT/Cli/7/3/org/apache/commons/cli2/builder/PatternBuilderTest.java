package org.apache.commons.cli2.builder;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
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
    public void testCreate() {
        PatternBuilder pb = new PatternBuilder();
        Option option = pb.create();
        assertNotNull(option);
    }

    @Test
    public void testCreateOption() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:");
        Option option = pb.create();
        assertEquals("a", option.getLongOpt());
        assertEquals(0, option.getArgs().length);
        assertFalse(option.isRequired());
        
        pb.reset();
        pb.withPattern("a:");
        pb.withPattern("b:");
        option = pb.create();
        assertEquals("a or b", option.getLongOpt());
        assertEquals(new String[]{"b"}, option.getArgs());
        assertFalse(option.isRequired());
        
        pb.reset();
        pb.withPattern("a:");
        pb.withPattern("b:");
        pb.withPattern("c:!");
        option = pb.create();
        assertEquals("a or b", option.getLongOpt());
        assertFalse(option.isRequired());
        assertArrayEquals(new String[]{"c"}, option.getArgs());
        assertTrue(option.isRequired());
        
        pb.reset();
        pb.withPattern("a:/");
        pb.withPattern("b:<");
        option = pb.create();
        assertEquals("a or b", option.getLongOpt());
        assertNotNull(option.getValidator());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("!");
    }

    @Test
    public void testCreateOptionWithValidators() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("i:@java.lang.Integer");
        Option option = pb.create();
        assertEquals("i", option.getLongOpt());
        assertNotNull(option.getValidator());
        assertInstanceOf(NumberValidator.class, option.getValidator());
    }

    @Test
    public void testCreateOptionWithDateValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("d:#");
        Option option = pb.create();
        assertEquals("d", option.getLongOpt());
        assertNotNull(option.getValidator());
        assertInstanceOf(DateValidator.class, option.getValidator());
    }

    @Test
    public void testCreateOptionWithFileValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("f:<");
        Option option = pb.create();
        assertEquals("f", option.getLongOpt());
        assertNotNull(option.getValidator());
        assertInstanceOf(FileValidator.class, option.getValidator());
        assertTrue(((FileValidator) option.getValidator()).isExisting());
    }

    @Test
    public void testCreateOptionWithUrlValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("u:/");
        Option option = pb.create();
        assertEquals("u", option.getLongOpt());
        assertNotNull(option.getValidator());
        assertInstanceOf(UrlValidator.class, option.getValidator());
    }
}