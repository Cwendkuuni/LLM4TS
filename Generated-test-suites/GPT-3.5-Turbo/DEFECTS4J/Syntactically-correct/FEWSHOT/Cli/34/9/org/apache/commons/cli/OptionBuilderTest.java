package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        
        OptionBuilder.hasArg(false);
        Option option2 = OptionBuilder.create();
        assertFalse(option2.hasArg());
        assertEquals(Option.UNINITIALIZED, option2.getArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
        
        OptionBuilder.isRequired(false);
        Option option2 = OptionBuilder.create();
        assertFalse(option2.isRequired());
    }
    
    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create('t');
        assertEquals(':', option.getValueSeparator());
    }
    
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        Option option = OptionBuilder.create('t');
        assertEquals('=', option.getValueSeparator());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }
    
    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(2);
        Option option = OptionBuilder.create();
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertTrue(option.getOptionalArg());
        assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertTrue(option.getOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }
    
    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(2);
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertTrue(option.getOptionalArg());
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        Option option = OptionBuilder.create();
        assertEquals(Integer.class, option.getType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getDescription());
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithCharInvalid() {
        OptionBuilder.create('?');
    }
    
    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("test")
                     .hasArg()
                     .withArgName("arg")
                     .isRequired()
                     .withValueSeparator(':')
                     .hasArgs()
                     .withType(String.class)
                     .withDescription("option test");
                     
        Option option = OptionBuilder.create();
        
        assertEquals("test", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("arg", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(':', option.getValueSeparator());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(String.class, option.getType());
        assertEquals("option test", option.getDescription());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
}