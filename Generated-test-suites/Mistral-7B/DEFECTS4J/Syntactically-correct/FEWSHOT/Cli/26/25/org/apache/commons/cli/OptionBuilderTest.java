package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testDefaultValues() {
        Option option = OptionBuilder.create();
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("longOption")
                .create();
        assertEquals("longOption", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertEquals(1, option.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithArgName() {
        Option option = OptionBuilder.hasArg().withArgName("argName")
                .create();
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':').create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorEmpty() {
        Option option = OptionBuilder.withValueSeparator().create();
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testHasArgs() {
        Option option = OptionBuilder.hasArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        Option option = OptionBuilder.hasArgs(2).create();
        assertEquals(2, option.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        assertTrue(option.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        assertTrue(option.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        Option option = OptionBuilder.hasOptionalArgs(3).create();
        assertTrue(option.isOptionalArg());
        assertEquals(3, option.getNumberOfArgs());
    }

    @Test
    public void testWithType() {
        Option option = OptionBuilder.withType(String.class).create();
        assertSame(String.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("description")
                .create();
        assertEquals("description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullLongOpt() {
        OptionBuilder.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        assertTrue(option.isOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
    }

    @Test
    public void testCreateWithOptionalArgAndOneArg() {
        Option option = OptionBuilder.hasOptionalArg().hasArg().create();
        assertTrue(option.isOptionalArg());
        assertEquals(1, option.getNumberOfArgs());
    }

    @Test
    public void testCreateWithOptionalArgAndTwoArgs() {
        Option option = OptionBuilder.hasOptionalArg().hasArgs(2).create();
        assertTrue(option.isOptionalArg());
        assertEquals(2, option.getNumberOfArgs());
    }

}