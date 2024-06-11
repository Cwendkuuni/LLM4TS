package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testCreateOptionWithShortOpt() {
        Option option = OptionBuilder.create('a');
        assertEquals("a", option.getOpt());
    }
    
    @Test
    public void testCreateOptionWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("long").create();
        assertEquals("long", option.getLongOpt());
    }

    @Test
    public void testCreateOptionWithDescription() {
        Option option = OptionBuilder.withDescription("description").create();
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testCreateOptionWithArgName() {
        Option option = OptionBuilder.withArgName("argName").create();
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testCreateOptionIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreateOptionWithHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertEquals(1, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testCreateOptionWithHasArgs() {
        Option option = OptionBuilder.hasArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testCreateOptionWithHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        assertEquals(1, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testCreateOptionWithHasOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testCreateOptionWithHasArgsNumArgs() {
        Option option = OptionBuilder.hasArgs(2).create();
        assertEquals(2, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testCreateOptionWithRequiredAndNumArgs() {
        Option option = OptionBuilder.isRequired(true).hasArgs(2).create();
        assertTrue(option.isRequired());
        assertEquals(2, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testCreateOptionWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':').create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreateOptionWithDefaultSeparator() {
        Option option = OptionBuilder.withValueSeparator().create();
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testCreateOptionWithNumberType() {
        Option option = OptionBuilder.withType(Number.class).create();
        assertEquals(Number.class, option.getType());
    }
}
