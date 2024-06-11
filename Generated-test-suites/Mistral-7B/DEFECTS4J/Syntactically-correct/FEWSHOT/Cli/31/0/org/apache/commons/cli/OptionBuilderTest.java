package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderReset() {
        OptionBuilder.reset();

        assertNull(OptionBuilder.description);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("testOption");

        assertEquals("testOption", builder.longopt);
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder builder = OptionBuilder.hasArg();

        assertEquals(1, builder.numberOfArgs);
        assertFalse(builder.optionalArg);
    }

    @Test
    public void testOptionBuilderHasArgWithOptionalArg() {
        OptionBuilder builder = OptionBuilder.hasArg(true);

        assertEquals(1, builder.numberOfArgs);
        assertTrue(builder.optionalArg);
    }

    @Test(expected = IllegalStateException.class)
    public void testOptionBuilderHasArgAfterHasArgs() {
        OptionBuilder builder = OptionBuilder.hasArgs();
        OptionBuilder.hasArg();
    }

    @Test
    public void testOptionBuilderWithArgName() {
        OptionBuilder builder = OptionBuilder.withArgName("testArg");

        assertEquals("testArg", builder.argName);
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder builder = OptionBuilder.isRequired();

        assertTrue(builder.required);
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder builder = OptionBuilder.hasArgs();

        assertEquals(Option.UNLIMITED_VALUES, builder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasArgsWithNum() {
        OptionBuilder builder = OptionBuilder.hasArgs(5);

        assertEquals(5, builder.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasOptionalArgs() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs();

        assertEquals(Option.UNLIMITED_VALUES, builder.numberOfArgs);
        assertTrue(builder.optionalArg);
    }

    @Test
    public void testOptionBuilderHasOptionalArgsWithNum() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs(3);

        assertEquals(3, builder.numberOfArgs);
        assertTrue(builder.optionalArg);
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder builder = OptionBuilder.withType(String.class);

        assertEquals(String.class, builder.type);
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder builder = OptionBuilder.withDescription("test description");

        assertEquals("test description", builder.description);
    }

    @Test
    public void testCreateOption() {
        Option option = OptionBuilder.create('t');

        assertNotNull(option);
        assertEquals("", option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testCreateOptionWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("testOption").create('t');

        assertEquals("testOption", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testCreateOptionWithArgName() {
        Option option = OptionBuilder.withArgName("testArg").create('t');

        assertEquals("", option.getLongOpt());
        assertEquals("testArg", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertEquals("testArg", option.getArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithoutLongOpt() {
        OptionBuilder.create();
    }
}