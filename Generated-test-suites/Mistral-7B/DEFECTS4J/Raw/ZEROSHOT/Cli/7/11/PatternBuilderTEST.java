package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @Test
    public void testPatternBuilder() {
        PatternBuilder builder = new PatternBuilder();

        // Test empty pattern
        builder.reset();
        Option option = builder.create();
        assertNull(option);

        // Test simple pattern
        builder.withPattern("a:");
        option = builder.create();
        assertNotNull(option);
        assertEquals("a", option.getLongOpt());
        assertNull(option.getArgument());

        // Test pattern with optional argument
        builder.reset();
        builder.withPattern("a:s");
        option = builder.create();
        assertNotNull(option);
        assertEquals("a", option.getLongOpt());
        assertInstanceOf(Argument.class, option.getArgument());

        // Test pattern with required argument
        builder.reset();
        builder.withPattern("a:s");
        builder.create();
        option = builder.create();
        assertNotNull(option);
        assertEquals("a", option.getLongOpt());
        assertInstanceOf(Argument.class, option.getArgument());
        assertNotNull(option.getArgument().getValue());

        // Test pattern with validator
        builder.reset();
        builder.withPattern("d:");
        option = builder.create();
        assertNotNull(option);
        assertEquals("d", option.getLongOpt());
        assertNull(option.getArgument());
        assertTrue(option.getValidator() instanceof DateValidator);

        // Test pattern with multiple validators
        builder.reset();
        builder.withPattern("n:i,f:");
        option = builder.create();
        assertNotNull(option);
        assertEquals("n", option.getLongOpt());
        assertInstanceOf(Argument.class, option.getArgument());
        assertTrue(option.getArgument().getValidator() instanceof NumberValidator);
        assertEquals("f", option.getNextOption().getLongOpt());
        assertNull(option.getNextOption().getArgument());
        assertNull(option.getNextOption().getNextOption());

        // Test pattern with multiple options
        builder.reset();
        builder.withPattern("g:a:,h:i:");
        Option option1 = builder.create();
        Option option2 = builder.create();
        assertEquals("g", option1.getLongOpt());
        assertEquals("a", option1.getArgument().getName());
        assertEquals("h", option2.getLongOpt());
        assertEquals("i", option2.getArgument().getName());

        // Test pattern with group
        builder.reset();
        builder.withPattern("g: {a:,b:}");
        Option groupOption = builder.create();
        assertEquals("g", groupOption.getLongOpt());
        assertEquals(2, groupOption.getOptions().size());
        Option optionA = groupOption.getOptions().get(0);
        assertEquals("a", optionA.getLongOpt());
        assertNull(optionA.getArgument());
        Option optionB = groupOption.getOptions().get(1);
        assertEquals("b", optionB.getLongOpt());
        assertNull(optionB.getArgument());
    }
}