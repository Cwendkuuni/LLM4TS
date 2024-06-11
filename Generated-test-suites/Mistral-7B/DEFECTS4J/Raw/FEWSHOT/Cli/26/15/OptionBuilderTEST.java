package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
        assertFalse(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("newLongopt");
        assertEquals("newLongopt", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("newArgName");
        assertEquals("newArgName", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparatorEmpty() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder.hasArgs(5);
        assertEquals(5, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(new Integer(10));
        assertEquals(new Integer(10), OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("newDescription");
        assertEquals("newDescription", OptionBuilder.description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoLongOpt() {
        OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEmptyLongOpt() {
        OptionBuilder.withLongOpt("");
        OptionBuilder.create();
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("newLongopt");
        OptionBuilder.description = "newDescription";
        OptionBuilder.argName = "newArgName";
        OptionBuilder.required = true;
        OptionBuilder.numberOfArgs = 1;
        OptionBuilder.optionalArg = true;
        OptionBuilder.valuesep = ':';
        OptionBuilder.type = new Integer(10);

        Option option = OptionBuilder.create('X');

        assertEquals("newLongopt", option.getLongOpt());
        assertEquals("newDescription", option.getDescription());
        assertEquals("newArgName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Option.VALUE_OPTIONAL, option.getOpt());
        assertEquals(1, option.getArgs().length);
        assertEquals(10, option.getType().getClass().getMethod("intValue").invoke(option.getType()));
        assertEquals(':', option.getValueSeparator());
        assertTrue(option.hasOptionalArg());
    }
}