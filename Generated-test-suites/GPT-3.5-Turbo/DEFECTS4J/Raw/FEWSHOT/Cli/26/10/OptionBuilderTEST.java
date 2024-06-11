package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longopt");
        assertEquals("longopt", OptionBuilder.create().getLongOpt());
        assertNull(OptionBuilder.create('-').getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertTrue(OptionBuilder.create().hasArg());
        OptionBuilder.hasArg(false);
        assertFalse(OptionBuilder.create().hasArg());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argname");
        assertEquals("argname", OptionBuilder.create().getArgName());
        assertNull(OptionBuilder.create('a').getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.create().isRequired());
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.create('-').isRequired());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.create().getDescription());
        assertNull(OptionBuilder.create('-').getDescription());
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create('o');
        assertEquals(option.getOpt(), 'o');

        option = OptionBuilder.create();
        assertNull(option.getOpt());

        option = OptionBuilder.create("option");
        assertEquals(option.getOpt(), "option");

        option = OptionBuilder.create(null);
        assertNull(option.getOpt());
    }

    @Test
    public void testIsOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.create().hasOptionalArg());
        assertFalse(OptionBuilder.create("test").hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertTrue(OptionBuilder.create().hasOptionalArgs());
        assertFalse(OptionBuilder.create('-').hasOptionalArgs());
    }

    @Test
    public void testWithOptionalArgs() {
        OptionBuilder.hasOptionalArgs(5);
        assertEquals(5, OptionBuilder.create().getArgs());
        OptionBuilder.hasOptionalArgs(0);
        assertEquals(0, OptionBuilder.create().getArgs());
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.create().isRequired());
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.create('-').isRequired());
    }

    @Test
    public void testWithValuesSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.create().getValueSeparator());
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder.hasArgs(5);
        assertEquals(5, OptionBuilder.create().getArgs());
        OptionBuilder.hasArgs(0);
        assertEquals(0, OptionBuilder.create().getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, OptionBuilder.create().getType());
        assertNull(OptionBuilder.create("test").getType());
    }
}