package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.create().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgWithParameter() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.create().getArgs());

        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.create().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("arg");
        assertEquals("arg", OptionBuilder.create().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.create().isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, OptionBuilder.create().getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.create().getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        assertNotNull(option.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
}