package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.GroupBuilder;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setup() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        patternBuilder.withPattern("-a");
        Option option = patternBuilder.create();
        assertEquals("-a", option.getShortName());

        patternBuilder.reset();
        patternBuilder.withPattern("-a!");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        assertTrue(option.isRequired());

        patternBuilder.reset();
        patternBuilder.withPattern("-a@");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        Argument argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof ClassValidator);
        assertTrue(argument.getMinimum() == 1);

        patternBuilder.reset();
        patternBuilder.withPattern("-a:");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() == null);
        assertTrue(argument.getMaximum() == 1);

        patternBuilder.reset();
        patternBuilder.withPattern("-a%");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof NumberValidator);
        assertTrue(argument.getMaximum() == 1);
        assertTrue(argument.getMinimum() == 1);

        patternBuilder.reset();
        patternBuilder.withPattern("-a#");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof DateValidator);
        assertTrue(argument.getMaximum() == 1);
        
        patternBuilder.reset();
        patternBuilder.withPattern("-a<");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof FileValidator);
        assertTrue(argument.getMinimum() == 1);
        
        patternBuilder.reset();
        patternBuilder.withPattern("-a>");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof FileValidator);
        assertTrue(argument.getMaximum() == 1);

        patternBuilder.reset();
        patternBuilder.withPattern("-a/");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof UrlValidator);
        assertTrue(argument.getMaximum() == 1);
    }

    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("-a");
        Option option = patternBuilder.create();
        assertEquals("-a", option.getShortName());

        patternBuilder.reset();
        patternBuilder.withPattern("-ab");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());

        patternBuilder.reset();
        patternBuilder.withPattern("-a!");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        assertTrue(option.isRequired());

        patternBuilder.reset();
        patternBuilder.withPattern("-a@");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        Argument argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof ClassValidator);
        assertTrue(argument.getMinimum() == 1);

        patternBuilder.reset();
        patternBuilder.withPattern("-a:");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() == null);
        assertTrue(argument.getMaximum() == 1);

        patternBuilder.reset();
        patternBuilder.withPattern("-a%");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof NumberValidator);
        assertTrue(argument.getMaximum() == 1);
        assertTrue(argument.getMinimum() == 1);

        patternBuilder.reset();
        patternBuilder.withPattern("-a#");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof DateValidator);
        assertTrue(argument.getMaximum() == 1);

        patternBuilder.reset();
        patternBuilder.withPattern("-a<");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof FileValidator);
        assertTrue(argument.getMinimum() == 1);

        patternBuilder.reset();
        patternBuilder.withPattern("-a>");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof FileValidator);
        assertTrue(argument.getMaximum() == 1);

        patternBuilder.reset();
        patternBuilder.withPattern("-a/");
        option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof UrlValidator);
        assertTrue(argument.getMaximum() == 1);
    }

    @Test
    public void testReset() {
        patternBuilder.withPattern("-a");
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertTrue(option == null);
    }
}