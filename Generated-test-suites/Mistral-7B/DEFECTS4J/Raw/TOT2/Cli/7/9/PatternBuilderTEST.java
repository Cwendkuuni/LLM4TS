package org.apache.commons.cli2.builder;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Option;
import java.util.HashSet;
import java.util.Set;

public class PatternBuilderTest {
    PatternBuilder patternBuilder;

    @Test
    public void testCreate() {
        patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testWithPattern() {
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("+-h:/");
        option = patternBuilder.create();
        assertEquals("h", option.getLongOpt());
        assertEquals(true, option.getFlagged());
        assertEquals("/", option.getOpt());
        assertEquals(Optional.of(""), option.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidPattern() {
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("%:");
        patternBuilder.create();
    }

    @Test(expected = NullPointerException.class)
    public void testCreateOptionWithNullParameters() {
        patternBuilder = new PatternBuilder();
        patternBuilder.createOption(' ', false, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithInvalidType() {
        patternBuilder = new PatternBuilder();
        patternBuilder.createOption('x', false, 'x');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithInvalidFlag() {
        patternBuilder = new PatternBuilder();
        patternBuilder.createOption('!', false, '+');
    }

    @Test
    public void testCreateOptionWithMinimumMaximum() {
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("+:");
        patternBuilder.createOption('*', false, '*');

        Option option1 = patternBuilder.create();
        Option option2 = patternBuilder.create();

        assertEquals(1, patternBuilder.getOptions().size());
        assertNotNull(option1);
        assertNotNull(option2);
        assertEquals(2, patternBuilder.getOptions().size());
    }

    @Test
    public void testCreateWithNoOption() {
        patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidatorWithNullCharacter() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.validator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidatorWithInvalidType() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.validator('Z');
    }
}