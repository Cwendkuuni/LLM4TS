
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longOpt");
        assertEquals("longOpt", OptionBuilder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        OptionBuilder.hasArg(false);
        assertEquals(-1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testWithSeparator() {
        OptionBuilder.withValueSeparator('=');
        assertEquals('=', OptionBuilder.getValuesep());
    }

    @Test
    public void testWithSeparatorDefault() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.getValuesep());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.isRequired());

        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Integer.MAX_VALUE, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder.hasArgs(5);
        assertEquals(5, OptionBuilder.getNumberOfArgs());
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
        assertEquals(Integer.MAX_VALUE, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder.hasOptionalArgs(5);
        assertEquals(5, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('c');
        assertEquals("c", option.getOpt());
        assertNull(option.getLongOpt());
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        assertNull(option.getOpt());
        assertNotNull(option.getLongOpt());
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("opt");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
    }

}
