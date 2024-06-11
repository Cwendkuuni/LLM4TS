package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class OptionBuilderTest {
    
    @Before
    public void setUp() {
        OptionBuilder.reset();
    }
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testOpt");
        assertEquals("testOpt", OptionBuilder.create().getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasArgWithValue() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasArgWithoutValue() {
        OptionBuilder.hasArg(false);
        assertEquals(0, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArg");
        assertEquals("testArg", OptionBuilder.create().getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testWithSeparator() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.create().getValueSeparator());
    }
    
    @Test
    public void testWithDefaultSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.create().getValueSeparator());
    }
    
    @Test
    public void testIsRequiredWithValue() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testIsRequiredWithoutValue() {
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasArgsWithValue() {
        OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.create().getArgs());
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgsWithValue() {
        OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.create().getArgs());
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }
    
    @Test
    public void testWithType() {
        Object type = new Object();
        OptionBuilder.withType(type);
        assertEquals(type, OptionBuilder.create().getType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        assertEquals("testDescription", OptionBuilder.create().getDescription());
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertNotNull(option);
        assertEquals("t", option.getOpt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
    
    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("testOpt");
        assertNotNull(option);
        assertEquals("testOpt", option.getOpt());
    }
}