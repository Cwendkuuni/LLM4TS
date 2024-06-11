package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        builder.createOption('%', false, '%');
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(option.getValidator() instanceof NumberValidator);

        builder.reset();
        builder.createOption('@', false, '@');
        option = builder.create();
        assertNotNull(option);
        assertTrue(option.getValidator() instanceof ClassValidator);

        builder.reset();
        builder.createOption(':', false, ':');
        option = builder.create();
        assertNotNull(option);
        assertTrue(option.getValidator() == null);

        builder.reset();
        builder.createOption('+', false, '+');
        option = builder.create();
        assertNotNull(option);
        assertTrue(option.getValidator() instanceof ClassValidator);
        option.setValidator(new ClassValidator() {
            @Override
            public void validate(Object value) throws ValidationException {
                // custom validation
            }
        });

        builder.reset();
        builder.createOption('@', true, '@');
        option = builder.create();
        assertNotNull(option);
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreatePatternOption() {
        PatternBuilder builder = new PatternBuilder();

        // simple option
        builder.withPattern("@");
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(option.getValidator() instanceof ClassValidator);

        // option with required flag
        builder.reset();
        builder.withPattern("!@");
        option = builder.create();
        assertNotNull(option);
        assertTrue(option.isRequired());

        // option with argument
        builder.reset();
        builder.withPattern(":%s");
        option = builder.create();
        assertNotNull(option);
        assertTrue(option.getArgument().isRequired());

        // option with validator
        builder.reset();
        builder.withPattern("%<");
        option = builder.create();
        assertNotNull(option);
        assertTrue(option.getValidator() instanceof FileValidator);

        // option with multiple validators
        builder.reset();
        builder.withPattern("%{@, java.lang.Number}");
        option = builder.create();
        assertNotNull(option);
        assertTrue(option.getValidator() instanceof NumberValidator);
        assertTrue(option.getValidator() instanceof ClassValidator);

        // option with a group
        builder.reset();
        builder.withPattern("(<group>:%s)(:<option>:%s)");
        option = builder.create();
        assertNotNull(option);
        assertTrue(option.getGroup() != null);
        assertTrue(option.getArgument().getName().equals("option"));

        // option with multiple arguments
        builder.reset();
        builder.withPattern("(<group>:%s%s):%s");
        option = builder.create();
        assertNotNull(option);
        assertTrue(option.getArguments().size() == 2);
    }
}