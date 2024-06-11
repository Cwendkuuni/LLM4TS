
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.create().getLongOpt());

        OptionBuilder.reset();
        OptionBuilder.withLongOpt("another-test");
        assertEquals("another-test", OptionBuilder.create().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.reset();
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.create().getArgs());

        OptionBuilder.reset();
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.create().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.reset();
        OptionBuilder.withArgName("name");
        assertEquals("name", OptionBuilder.create().getArgName());

        OptionBuilder.reset();
        OptionBuilder.withArgName("another-name");
        assertEquals("another-name", OptionBuilder.create().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.reset();
        OptionBuilder.isRequired();
        assertEquals(true, OptionBuilder.create().isRequired());

        OptionBuilder.reset();
        OptionBuilder.isRequired(false);
        assertEquals(false, OptionBuilder.create().isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.reset();
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.create().getValueSeparator());

        OptionBuilder.reset();
        OptionBuilder.withValueSeparator('=');
        assertEquals('=', OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.reset();
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());

        OptionBuilder.reset();
        OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.reset();
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.create().getArgs());
        assertEquals(true, OptionBuilder.create().hasOptionalArg());

        OptionBuilder.reset();
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.create().getArgs());
        assertEquals(true, OptionBuilder.create().hasOptionalArg());

        OptionBuilder.reset();
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.create().getArgs());
        assertEquals(true, OptionBuilder.create().hasOptionalArg());
    }

    @Test
    public void testWithOptionalArgs() {
        OptionBuilder.reset();
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
        assertEquals(true, OptionBuilder.create().hasOptionalArg());

        OptionBuilder.reset();
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
        assertEquals(true, OptionBuilder.create().hasOptionalArg());

        OptionBuilder.reset();
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
        assertEquals(true, OptionBuilder.create().hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.reset();
        OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.create().getArgs());
        assertEquals(true, OptionBuilder.create().hasOptionalArg());

        OptionBuilder.reset();
        OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.create().getArgs());
        assertEquals(true, OptionBuilder.create().hasOptionalArg());

        OptionBuilder.reset();
        OptionBuilder.hasOptionalArgs(4);
        assertEquals(4, OptionBuilder.create().getArgs());
        assertEquals(true, OptionBuilder.create().hasOptionalArg());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.reset();
        OptionBuilder.withDescription("test description");
        assertEquals("test description", OptionBuilder.create().getDescription());

        OptionBuilder.reset();
        OptionBuilder.withDescription("another description");
        assertEquals("another description", OptionBuilder.create().getDescription());
    }

    @Test
    public void testCreateChar() {
        OptionBuilder.reset();
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertEquals(false, option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(false, option.hasOptionalArg());
        assertEquals((char) 0, option.getValueSeparator());
    }

    @Test
    public void testCreate() {
        OptionBuilder.reset();
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        assertNull(option.getOpt());
        assertEquals("test", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertEquals(false, option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(false, option.hasOptionalArg());
        assertEquals((char) 0, option.getValueSeparator());
    }

    @Test
    public void testCreateString() {
        OptionBuilder.reset();
        Option option = OptionBuilder.create("t");
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertEquals(false, option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(false, option.hasOptionalArg());
        assertEquals((char) 0, option.getValueSeparator());
    }
}
