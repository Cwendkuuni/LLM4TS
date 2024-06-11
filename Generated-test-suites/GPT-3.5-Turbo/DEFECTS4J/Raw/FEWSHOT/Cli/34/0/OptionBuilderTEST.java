package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", OptionBuilder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArgName");
        assertEquals("testArgName", OptionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.getValuesep());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.getValuesep());
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.isRequired());
        
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        assertEquals("testDescription", OptionBuilder.getDescription());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreate() {
        try {
            OptionBuilder.create();
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("must specify longopt", e.getMessage());
        }
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("testOpt");
        assertEquals("testOpt", option.getLongOpt());
    }
}