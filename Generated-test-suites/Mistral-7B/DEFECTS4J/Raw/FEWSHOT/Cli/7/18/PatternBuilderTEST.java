package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
    public void testPatternBuilder() {
        final PatternBuilder builder = new PatternBuilder();
        assertNotNull(builder);
    }

    @Test
    public void testCreateOption() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:");
        final Option option = builder.create();
        assertTrue(option.isSettable());
        assertTrue(option.isRequired());
        assertEquals("a", option.getOpt());
        assertNull(option.getArgument());

        builder.reset();
        builder.withPattern("b:@com.example.MyClass");
        option = builder.create();
        assertTrue(option.isSettable());
        assertTrue(option.isRequired());
        assertEquals("b", option.getOpt());
        assertNotNull(option.getValue());

        builder.reset();
        builder.withPattern("c:%d");
        option = builder.create();
        assertTrue(option.isSettable());
        assertFalse(option.isRequired());
        assertEquals("c", option.getOpt());
        assertNotNull(option.getValue());
        assertTrue(NumberValidator.isValid(option.getValue(), NumberValidator.TYPE_INT));

        builder.reset();
        builder.withPattern("d:<file>");
        option = builder.create();
        assertTrue(option.isSettable());
        assertTrue(option.isRequired());
        assertEquals("d", option.getOpt());
        assertNotNull(option.getValue());
        assertFalse(option.getValue().isEmpty());

        builder.reset();
        builder.withPattern("e:*/");
        option = builder.create();
        assertTrue(option.isSettable());
        assertFalse(option.isRequired());
        assertEquals("e", option.getOpt());
        assertNull(option.getValue());

        builder.reset();
        builder.withPattern("f/:");
        option = builder.create();
        assertTrue(option.isSettable());
        assertTrue(option.isRequired());
        assertEquals("f", option.getOpt());
        assertNull(option.getValue());
    }

    @Test
    public void testCreateMultipleOptions() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a: a: b:");
        final Option option1 = builder.create();
        final Option option2 = builder.create();
        assertTrue(option1.isSettable());
        assertTrue(option1.isRequired());
        assertEquals("a", option1.getOpt());
        assertNull(option1.getValue());

        assertTrue(option2.isSettable());
        assertTrue(option2.isRequired());
        assertEquals("b", option2.getOpt());
        assertNull(option2.getValue());

        builder.reset();
        builder.withPattern("a: @com.example.MyClass a: %d");
        final Option option3 = builder.create();
        final Option option4 = builder.create();
        assertTrue(option3.isSettable());
        assertFalse(option3.isRequired());
        assertEquals("a", option3.getOpt());
        assertNotNull(option3.getValue());
        assertTrue(ClassValidator.isValid(option3.getValue(), String.class));

        assertTrue(option4.isSettable());
        assertFalse(option4.isRequired());
        assertEquals("a", option4.getOpt());
        assertNotNull(option4.getValue());
        assertTrue(NumberValidator.isValid(option4.getValue(), NumberValidator.TYPE_INT));
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidPattern() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:!:");
    }

    @Test
    public void testValidateWithValidators() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:@com.example.MyClass");
        final Option option = builder.create();
        final ClassValidator validator = (ClassValidator) option.getValidator();
        validator.validate(new Class[] { MyClass.class });
    }

    private static class MyClass {}
}