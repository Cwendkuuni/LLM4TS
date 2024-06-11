package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.GroupBuilder;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

    private PatternBuilder builder;
    private GroupBuilder gbuilder;
    private DefaultOptionBuilder obuilder;
    private ArgumentBuilder abuilder;

    @Before
    public void setup() {
        gbuilder = new GroupBuilder();
        obuilder = new DefaultOptionBuilder();
        abuilder = new ArgumentBuilder();
        builder = new PatternBuilder(gbuilder, obuilder, abuilder);
    }

    @Test
    public void testCreate() {
        Option option = builder.create();
        assertNotNull(option);
        assertEquals(0, option.getOptions().size());
        assertNull(option.getArgument());
        assertEquals("", option.getDescription());
        assertNull(option.getFormat());
        assertNull(option.getLongName());
        assertNull(option.getProperty());
        assertNull(option.getShortName());
        assertEquals(1, option.getMaximum());
        assertEquals(1, option.getMinimum());
        assertEquals(0, option.getDisplaySeparator());

        obuilder.withShortName("a").withLongName("arg").withDescription("argument").withArgument(abuilder.create()).withRequired(true);
        option = builder.create();

        assertNotNull(option);
        assertNotNull(option.getOptions());
        assertEquals(1, option.getOptions().size());
        assertNull(option.getArgument());
        assertEquals("", option.getDescription());
        assertNull(option.getFormat());
        assertEquals("arg", option.getLongName());
        assertNull(option.getProperty());
        assertEquals("a", option.getShortName());
        assertEquals(1, option.getMaximum());
        assertEquals(1, option.getMinimum());
        assertEquals(0, option.getDisplaySeparator());
    }

    @Test
    public void testReset() {
        PatternBuilder resetBuilder = builder.reset();
        assertEquals(builder, resetBuilder);
    }

    @Test
    public void testWithPattern() {
        builder.withPattern("!@:%%#<>*/");

        Option option = builder.create();
        assertNotNull(option);
        assertNotNull(option.getOptions());
        assertEquals(9, option.getOptions().size());
        assertEquals(":", option.getOptions().get(0).getShortName());
        assertEquals("@", option.getOptions().get(1).getShortName());
        assertEquals("%", option.getOptions().get(2).getShortName());
        assertEquals("#", option.getOptions().get(3).getShortName());
        assertEquals("<", option.getOptions().get(4).getShortName());
        assertEquals(">", option.getOptions().get(5).getShortName());
        assertEquals("*", option.getOptions().get(6).getShortName());
        assertEquals("/", option.getOptions().get(7).getShortName());
        assertEquals("!", option.getOptions().get(8).getShortName());
        assertNull(option.getArgument());
        assertEquals("", option.getDescription());
        assertNull(option.getFormat());
        assertNull(option.getLongName());
        assertNull(option.getProperty());
        assertNull(option.getShortName());
        assertEquals(1, option.getMaximum());
        assertEquals(1, option.getMinimum());
        assertEquals(0, option.getDisplaySeparator());
    }

    @Test
    public void testValidator() {
        Validator validator = PatternBuilder.validator('@');
        assertNotNull(validator);
        assertEquals(ClassValidator.class, validator.getClass());

        validator = PatternBuilder.validator('+');
        assertNotNull(validator);
        assertEquals(ClassValidator.class, validator.getClass());

        validator = PatternBuilder.validator(':');
        assertNull(validator);

        validator = PatternBuilder.validator('%');
        assertNotNull(validator);
        assertEquals(NumberValidator.class, validator.getClass());

        validator = PatternBuilder.validator('#');
        assertNotNull(validator);
        assertEquals(DateValidator.class, validator.getClass());

        validator = PatternBuilder.validator('<');
        assertNotNull(validator);
        assertEquals(FileValidator.class, validator.getClass());

        validator = PatternBuilder.validator('>');
        assertNotNull(validator);
        assertEquals(FileValidator.class, validator.getClass());

        validator = PatternBuilder.validator('*');
        assertNotNull(validator);
        assertEquals(FileValidator.class, validator.getClass());

        validator = PatternBuilder.validator('/');
        assertNotNull(validator);
        assertEquals(UrlValidator.class, validator.getClass());

        validator = PatternBuilder.validator('?');
        assertNull(validator);
    }

}