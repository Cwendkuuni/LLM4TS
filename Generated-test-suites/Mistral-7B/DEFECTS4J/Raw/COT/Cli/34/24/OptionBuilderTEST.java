package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for OptionBuilder class.
 */
public class OptionBuilderTest {

    

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option1");
        assertEquals("option1", optionBuilder.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithFalse() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        assertEquals("argName", optionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorEmpty() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', optionBuilder.getValueSeparator());
    }

    @Test
    public void testIsRequiredFalse() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        assertFalse(optionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        assertEquals(2, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(3, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithType() {
        String className = "java.lang.Integer";
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, optionBuilder.getType());
        assertEquals(className, optionBuilder.getType().getName());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Test description");
        assertEquals("Test description", optionBuilder.getDescription());
    }

    @Test
    public void testCreateWithChar() throws IllegalArgumentException {
        Option option = OptionBuilder.create('t');
        assertNotNull(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() throws IllegalArgumentException {
        OptionBuilder.create();
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOpt() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("123");
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testCreateWithEmptyDescription() {
        Option option = OptionBuilder.create("", 't');
        assertNotNull(option);
        assertEquals("", option.getLongOpt());
        assertNotNull(option.getDescription());
        assertEquals("", option.getDescription());
    }

    @Test
    public void testCreateWithLongOptAndDescription() {
        Option option = OptionBuilder.create("testOption", 't');
        assertNotNull(option);
        assertEquals("testOption", option.getLongOpt());
        assertNotNull(option.getDescription());
        assertFalse(option.getDescription().isEmpty());
    }

    
}