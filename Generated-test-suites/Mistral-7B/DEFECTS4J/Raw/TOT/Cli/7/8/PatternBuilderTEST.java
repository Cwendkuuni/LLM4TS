package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreateOptionWithValidPattern() {
        patternBuilder.withPattern("@<file>");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertEquals("file", option.getLongOpt());
        assertEquals('f', option.getOpt());
        assertNull(option.getValue());
        assertFalse(option.isRequired());
        assertNotNull(option.getValidator());
        assertInstanceof(FileValidator.class, option.getValidator());
        assertFalse(((FileValidator) option.getValidator()).isFile());
    }

    @Test
    public void testCreateOptionWithRequiredArgument() {
        patternBuilder.withPattern("%d<number>");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertEquals("number", option.getLongOpt());
        assertEquals('d', option.getOpt());
        assertInstanceof(Argument.class, option.getValue());
        assertTrue(option.isRequired());
        assertNotNull(option.getValidator());
        assertInstanceof(NumberValidator.class, option.getValidator());
    }

    @Test
    public void testCreateOptionWithOptionalArgument() {
        patternBuilder.withPattern("%s");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertEquals("", option.getLongOpt());
        assertEquals('', option.getOpt());
        assertInstanceof(Argument.class, option.getValue());
        assertFalse(option.isRequired());
        assertNotNull(option.getValidator());
        assertNull(option.getValidator());
    }

    @Test
    public void testCreateOptionWithInvalidPattern() {
        patternBuilder.withPattern("@<file>+");
        assertThrows(IllegalArgumentException.class, () -> patternBuilder.create());
    }

    @Test
    public void testCreateOptionWithMissingPattern() {
        patternBuilder.reset();
        assertThrows(IllegalArgumentException.class, () -> patternBuilder.create());
    }
}