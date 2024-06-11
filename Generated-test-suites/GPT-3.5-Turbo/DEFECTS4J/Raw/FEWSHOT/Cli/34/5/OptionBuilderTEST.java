package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArg_WithBoolean() {
        OptionBuilder.hasArg(false);
        Option option = OptionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("name");
        Option option = OptionBuilder.create();
        assertEquals("name", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testWithRequired() {
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create();
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgs_WithNumArgs() {
        OptionBuilder.hasArgs(3);
        Option option = OptionBuilder.create();
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertEquals(1, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs_WithNumArgs() {
        OptionBuilder.hasOptionalArgs(2);
        Option option = OptionBuilder.create();
        assertEquals(2, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        Option option = OptionBuilder.create();
        assertEquals(String.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        Option option = OptionBuilder.create();
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testCreate_Char() {
        Option option = OptionBuilder.create('c');
        assertEquals("c", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_NoLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreate_String() {
        Option option = OptionBuilder.create("opt");
        assertEquals("opt", option.getOpt());
    }
}