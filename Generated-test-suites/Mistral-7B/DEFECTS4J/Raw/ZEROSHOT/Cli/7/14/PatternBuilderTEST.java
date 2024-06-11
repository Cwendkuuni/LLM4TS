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
 * Tests for the PatternBuilder class
 */
public class PatternBuilderTest {

    @Test
    public void testPatternBuilderCreation() {
        PatternBuilder builder = new PatternBuilder();
        assertNotNull(builder);

        PatternBuilder builderWithGBOBAB = new PatternBuilder(
            new GroupBuilder(),
            new DefaultOptionBuilder(),
            new ArgumentBuilder());
        assertNotNull(builderWithGBOBAB);
    }

    @Test
    public void testReset() {
        PatternBuilder builder = new PatternBuilder();
        Option option1 = builder.withPattern("a:");
        Option option2 = builder.withPattern("b:");
        assertEquals(2, builder.create().getOptions().size());

        builder.reset();
        assertNotNull(builder.create());
        assertEquals(0, builder.create().getOptions().size());
    }

    @Test
    public void testCreateOption() {
        PatternBuilder builder = new PatternBuilder();

        // Test empty pattern
        builder.withPattern("");
        assertNotNull(builder.create());
        assertEquals(0, builder.create().getOptions().size());

        // Test single character pattern
        builder.withPattern("a:");
        Option option1 = builder.create();
        Argument argument1 = option1.getArgs()[0];
        assertEquals(1, option1.getOptions().size());
        assertNotNull(argument1);
        assertEquals("", argument1.getName());
        assertEquals(Validator.class, argument1.getValidator().getClass());

        // Test multiple character pattern with validator
        builder.withPattern("@c:");
        Option option2 = builder.create();
        Argument argument2 = option2.getArgs()[0];
        assertEquals(1, option2.getOptions().size());
        assertNotNull(argument2);
        assertEquals("c", argument2.getName());
        assertInstanceOf(ClassValidator.class, argument2.getValidator());

        // Test multiple character pattern without validator
        builder.reset();
        builder.withPattern(":d:");
        Option option3 = builder.create();
        Argument argument3 = option3.getArgs()[0];
        assertEquals(1, option3.getOptions().size());
        assertNotNull(argument3);
        assertEquals("", argument3.getName());
        assertNull(argument3.getValidator());

        // Test required option
        builder.reset();
        builder.withPattern("!a:");
        Option option4 = builder.create();
        Argument argument4 = option4.getArgs()[0];
        assertEquals(1, option4.getOptions().size());
        assertNotNull(argument4);
        assertEquals("", argument4.getName());
        assertTrue(option4.isRequired());

        // Test multiple characters with validator and required option
        builder.reset();
        builder.withPattern("!*@c:");
        Option option5 = builder.create();
        Argument argument5 = option5.getArgs()[0];
        assertEquals(1, option5.getOptions().size());
        assertNotNull(argument5);
        assertEquals("", argument5.getName());
        assertTrue(option5.isRequired());
        assertInstanceOf(ClassValidator.class, argument5.getValidator());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder builder = new PatternBuilder();

        // Test empty pattern
        builder.withPattern("");
        assertNotNull(builder.create());
        assertEquals(0, builder.create().getOptions().size());

        // Test single character pattern
        builder.withPattern("a:");
        Option option1 = builder.create();
        Argument argument1 = option1.getArgs()[0];
        assertEquals(1, option1.getOptions().size());
        assertNotNull(argument1);
        assertEquals("", argument1.getName());
        assertEquals(Validator.class, argument1.getValidator().getClass());

        // Test multiple character pattern with validator
        builder.withPattern("@c:");
        Option option2 = builder.create();
        Argument argument2 = option2.getArgs()[0];
        assertEquals(1, option2.getOptions().size());
        assertNotNull(argument2);
        assertEquals("c", argument2.getName());
        assertInstanceOf(ClassValidator.class, argument2.getValidator());

        // Test multiple character pattern without validator
        builder.reset();
        builder.withPattern(":d:");
        Option option3 = builder.create();
        Argument argument3 = option3.getArgs()[0];
        assertEquals(1, option3.getOptions().size());
        assertNotNull(argument3);
        assertEquals("", argument3.getName());
        assertNull(argument3.getValidator());

        // Test required option
        builder.reset();
        builder.withPattern("!a:");
        Option option4 = builder.create();
        Argument argument4 = option4.getArgs()[0];
        assertEquals(1, option4.getOptions().size());
        assertNotNull(argument4);
        assertEquals("", argument4.getName());
        assertTrue(option4.isRequired());

        // Test multiple characters with validator and required option
        builder.reset();
        builder.withPattern("!*@c:");
        Option option5 = builder.create();
        Argument argument5 = option5.getArgs()[0];
        assertEquals(1, option5.getOptions().size());
        assertNotNull(argument5);
        assertEquals("", argument5.getName());
        assertTrue(option5.isRequired());
        assertInstanceOf(ClassValidator.class, argument5.getValidator());

        // Test various patterns
        builder.reset();
        builder.withPattern("+a:@java.lang.String:");
        Option option6 = builder.create();
        Argument argument6 = option6.getArgs()[0];
        assertEquals(1, option6.getOptions().size());
        assertNotNull(argument6);
        assertEquals("", argument6.getName());
        assertInstanceOf(ClassValidator.class, argument6.getValidator());
        assertInstanceOf(String.class, argument6.getValidator().getType());

        builder.reset();
        builder.withPattern("#d:<test.txt>");
        Option option7 = builder.create();
        Argument argument7 = option7.getArgs()[0];
        assertEquals(1, option7.getOptions().size());
        assertNotNull(argument7);
        assertEquals("", argument7.getName());
        assertInstanceOf(FileValidator.class, argument7.getValidator());
        assertTrue(argument7.getValidator().isFile());
        assertTrue(argument7.getValidator().isExisting());

        builder.reset();
        builder.withPattern("/u:");
        Option option8 = builder.create();
        Argument argument8 = option8.getArgs()[0];
        assertEquals(1, option8.getOptions().size());
        assertNotNull(argument8);
        assertEquals("", argument8.getName());
        assertInstanceOf(UrlValidator.class, argument8.getValidator());

        builder.reset();
        builder.withPattern("%i:");
        Option option9 = builder.create();
        Argument argument9 = option9.getArgs()[0];
        assertEquals(1, option9.getOptions().size());
        assertNotNull(argument9);
        assertEquals("", argument9.getName());
        assertInstanceOf(NumberValidator.class, argument9.getValidator());

        builder.reset();
        builder.withPattern("*/log.txt");
        Option option10 = builder.create();
        Argument argument10 = option10.getArgs()[0];
        assertEquals(1, option10.getOptions().size());
        assertNotNull(argument10);
        assertEquals("", argument10.getName());
        assertInstanceOf(FileValidator.class, argument10.getValidator());
        assertFalse(argument10.getValidator().isFile());
        assertFalse(argument10.getValidator().isExisting());

        builder.reset();
        builder.withPattern(":email:");
        Option option11 = builder.create();
        Argument argument11 = option11.getArgs()[0];
        assertEquals(1, option11.getOptions().size());
        assertNotNull(argument11);
        assertEquals("email", argument11.getName());
        assertNull(argument11.getValidator());

        builder.reset();
        builder.withPattern("!@email:");
        Option option12 = builder.create();
        Argument argument12 = option12.getArgs()[0];
        assertEquals(1, option12.getOptions().size());
        assertNotNull(argument12);
        assertEquals("email", argument12.getName());
        assertTrue(option12.isRequired());
        assertInstanceOf(ClassValidator.class, argument12.getValidator());
        assertInstanceOf(String.class, argument12.getValidator().getType());
    }
}