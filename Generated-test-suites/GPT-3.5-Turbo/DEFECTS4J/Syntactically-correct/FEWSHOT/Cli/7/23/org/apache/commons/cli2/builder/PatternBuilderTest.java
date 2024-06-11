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

public class PatternBuilderTest {
    @Test
    public void testCreate() {
        // create a new PatternBuilder
        PatternBuilder patternBuilder = new PatternBuilder();

        // create an Option instance
        Option option = patternBuilder.create();

        // check that the Option instance is not null
        assertNotNull(option);
    }

    @Test
    public void testReset() {
        // create a new PatternBuilder
        PatternBuilder patternBuilder = new PatternBuilder();

        // reset the PatternBuilder
        patternBuilder.reset();

        // create an Option instance
        Option option = patternBuilder.create();

        // check that the Option instance is not null
        assertNotNull(option);
    }

    @Test
    public void testWithPattern() {
        // create a new PatternBuilder
        PatternBuilder patternBuilder = new PatternBuilder();

        // build an Option using a pattern string
        patternBuilder.withPattern("!@:foo");

        // create an Option instance
        Option option = patternBuilder.create();

        // check that the Option instance is not null
        assertNotNull(option);
    }

    @Test
    public void testValidator() {
        // create a new PatternBuilder
        PatternBuilder patternBuilder = new PatternBuilder();

        // create a Validator instance for '@'
        Validator validator1 = patternBuilder.validator('@');

        // check that the Validator instance is not null
        assertNotNull(validator1);

        // create a Validator instance for '+'
        Validator validator2 = patternBuilder.validator('+');

        // check that the Validator instance is not null
        assertNotNull(validator2);

        // create a Validator instance for ':'
        Validator validator3 = patternBuilder.validator(':');

        // check that the Validator instance is null
        assertNull(validator3);

        // create a Validator instance for '%'
        Validator validator4 = patternBuilder.validator('%');

        // check that the Validator instance is not null
        assertNotNull(validator4);

        // create a Validator instance for '#'
        Validator validator5 = patternBuilder.validator('#');

        // check that the Validator instance is not null
        assertNotNull(validator5);

        // create a Validator instance for '<'
        Validator validator6 = patternBuilder.validator('<');

        // check that the Validator instance is not null
        assertNotNull(validator6);

        // create a Validator instance for '>'
        Validator validator7 = patternBuilder.validator('>');

        // check that the Validator instance is not null
        assertNotNull(validator7);

        // create a Validator instance for '*'
        Validator validator8 = patternBuilder.validator('*');

        // check that the Validator instance is not null
        assertNotNull(validator8);

        // create a Validator instance for '/'
        Validator validator9 = patternBuilder.validator('/');

        // check that the Validator instance is not null
        assertNotNull(validator9);
    }
}