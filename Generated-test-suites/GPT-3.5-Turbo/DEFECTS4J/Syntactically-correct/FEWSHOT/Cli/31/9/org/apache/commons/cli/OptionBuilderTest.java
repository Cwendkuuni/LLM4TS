package org.apache.commons.cli;

import static org.junit.Assert.*;

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
        assertEquals(true, option.hasArg());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create();
        assertEquals(true, option.hasArg());
        assertEquals(1, option.getArgs());

        OptionBuilder.hasArg(false);
        option = OptionBuilder.create();
        assertEquals(false, option.hasArg());
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
    public void testWithOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertEquals(true, option.hasArg());
        assertEquals(true, option.hasOptionalArg());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertEquals(true, option.hasArg());
        assertEquals(true, option.hasOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(3);
        Option option = OptionBuilder.create();
        assertEquals(true, option.hasArg());
        assertEquals(true, option.hasOptionalArg());
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        Option option = OptionBuilder.create();
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testCreateString() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create("t");
        assertEquals("test", option.getLongOpt());
        assertEquals("t", option.getOpt());
    }

}