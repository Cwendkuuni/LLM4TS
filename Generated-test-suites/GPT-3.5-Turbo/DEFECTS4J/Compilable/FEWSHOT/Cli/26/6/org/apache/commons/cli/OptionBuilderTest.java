package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
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
    public void testHasArgWithArg() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.create().getArgs());
        
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.create().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.create().getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertEquals(true, OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.create().getDescription());
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
    public void testIsRequiredBoolean() {
        OptionBuilder.isRequired(true);
        assertEquals(true, OptionBuilder.create().isRequired());
        
        OptionBuilder.isRequired(false);
        assertEquals(false, OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasArgsInt() {
        OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.create().getArgs());
        assertEquals(true, OptionBuilder.create().hasOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
        assertEquals(true, OptionBuilder.create().hasOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.create().getArgs());
        assertEquals(true, OptionBuilder.create().hasOptionalArg());
    }
    
    @Test
    public void testWithType() {
        Object type = new Object();
        OptionBuilder.withType(type);
        assertEquals(type, OptionBuilder.create().getType());
    }
    
    @Test
    public void testCreateChar() {
        assertEquals("a", OptionBuilder.create('a').getOpt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCharInvalid() {
        OptionBuilder.create('-');
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
    
    @Test
    public void testCreateString() {
        assertEquals("test", OptionBuilder.create("test").getOpt());
    }
}