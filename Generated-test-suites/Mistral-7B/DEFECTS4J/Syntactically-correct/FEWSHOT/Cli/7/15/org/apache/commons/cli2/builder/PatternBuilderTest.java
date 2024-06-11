package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

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
        builder.createOption('%', false, ' ');
        assertNotNull(builder.obuilder.getOption());
    }

    @Test
    public void testCreateOptionWithValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.createOption('@', false, ' ');
        assertNotNull(builder.obuilder.getOption().getValidator());
        assertEquals(ClassValidator.class, builder.obuilder.getOption().getValidator().getClass());
    }

    @Test
    public void testCreateOptionWithArgument() {
        PatternBuilder builder = new PatternBuilder();
        Argument arg = new Argument("arg");
        builder.createOption(' ', true, 'a', arg);
        assertNotNull(builder.obuilder.getOption().getArgument());
        assertEquals("arg", builder.obuilder.getOption().getArgument().getName());
    }

    @Test
    public void testCreateOptionWithOptionalArgument() {
        PatternBuilder builder = new PatternBuilder();
        Argument arg = new Argument("arg");
        builder.createOption(' ', false, 'a', arg);
        assertNotNull(builder.obuilder.getOption().getArgument());
        assertEquals("arg", builder.obuilder.getOption().getArgument().getName());
    }

    @Test
    public void testCreateMultipleOptions() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("e:@f:i:o:d:");
        Option optionE = builder.create();
        Option optionF = builder.create();
        Option optionI = builder.create();
        Option optionO = builder.create();
        Option optionD = builder.create();
        assertNotNull(optionE);
        assertNotNull(optionF);
        assertNotNull(optionI);
        assertNotNull(optionO);
        assertNotNull(optionD);
    }

    @Test
    public void testCreateWithAllTypes() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("!@#%^&*+<>/ :e:f:i:o:d:");
        Option option = builder.create();
        assertTrue(option.isRequired());
        assertNotNull(option.getValidator());
        assertEquals(ClassValidator.class, option.getValidator().getClass());
        assertEquals("%", option.getValidator().getOptionString());
        Argument arg = option.getArgument();
        assertNotNull(arg);
        assertEquals("e", arg.getName());
    }

    @Test
    public void testCreateWithEmptyPattern() {
        PatternBuilder builder = new PatternBuilder();
        Option option = builder.create();
        assertNull(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidPattern() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("!@#%^&*+<>/ :e:f:i:o:d:z");
    }
}