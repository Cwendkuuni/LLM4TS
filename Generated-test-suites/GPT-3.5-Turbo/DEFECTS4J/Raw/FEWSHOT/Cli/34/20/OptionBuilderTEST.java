package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;


public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.withLongOpt("foo");
        assertEquals("foo", OptionBuilder.create().getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.reset();
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasArgHasArgTrue() {
        OptionBuilder.reset();
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasArgHasArgFalse() {
        OptionBuilder.reset();
        OptionBuilder.hasArg(false);
        assertEquals(-1, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.reset();
        OptionBuilder.withArgName("arg");
        assertEquals("arg", OptionBuilder.create().getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.reset();
        OptionBuilder.isRequired();
        assertEquals(true, OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testWithRequired() {
        OptionBuilder.reset();
        OptionBuilder.isRequired(true);
        assertEquals(true, OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.reset();
        OptionBuilder.hasArgs();
        assertEquals(Integer.MAX_VALUE, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasArgsInt() {
        OptionBuilder.reset();
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.reset();
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.create().getArgs());
        assertEquals(true, OptionBuilder.create().hasOptionalArgs());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.reset();
        OptionBuilder.hasOptionalArgs();
        assertEquals(Integer.MAX_VALUE, OptionBuilder.create().getArgs());
        assertEquals(true, OptionBuilder.create().hasOptionalArgs());
    }
    
    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.reset();
        OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.create().getArgs());
        assertEquals(true, OptionBuilder.create().hasOptionalArgs());
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.reset();
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.create().getType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.reset();
        OptionBuilder.withDescription("test");
        assertEquals("test", OptionBuilder.create().getDescription());
    }
    
    @Test
    public void testCreateChar() {
        OptionBuilder.reset();
        Option option = OptionBuilder.create('f');
        assertNotNull(option);
        assertEquals("f", option.getOpt());
    }
    
    @Test
    public void testCreate() {
        OptionBuilder.reset();
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertNull(option.getOpt());
    }
    
    @Test
    public void testCreateString() {
        OptionBuilder.reset();
        Option option = OptionBuilder.create("f");
        assertNotNull(option);
        assertEquals("f", option.getOpt());
    }
    
}