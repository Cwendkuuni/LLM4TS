package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

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
    public void testHasArgWithBoolean() {
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
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.create().getValueSeparator());
    }
    
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.create().getValueSeparator());
    }
    
    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.create().isRequired());
        
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.create().hasOptionalArg());
        assertEquals(1, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertTrue(OptionBuilder.create().hasOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(3);
        assertTrue(OptionBuilder.create().hasOptionalArg());
        assertEquals(3, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, OptionBuilder.create().getType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        assertEquals("test", OptionBuilder.create().getDescription());
    }
    
    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('c');
        assertEquals("c", option.getOpt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCharThrowsIllegalArgumentException() {
        OptionBuilder.create('?');
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOptThrowsIllegalArgumentException() {
        OptionBuilder.create();
    }
    
    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getOpt());
    }
}