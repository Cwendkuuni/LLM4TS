package org.apache.commons.cli;

import static org.junit.Assert.*;
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
        assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testHasArg_boolean() {
        OptionBuilder.hasArg(true);
        Option option1 = OptionBuilder.create();
        assertEquals(1, option1.getArgs());
        
        OptionBuilder.hasArg(false);
        Option option2 = OptionBuilder.create();
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
    }
    
    @Test
    public void testWithRequired_boolean() {
        OptionBuilder.isRequired(true);
        Option option1 = OptionBuilder.create();
        assertTrue(option1.isRequired());
        
        OptionBuilder.isRequired(false);
        Option option2 = OptionBuilder.create();
        assertFalse(option2.isRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }
    
    @Test
    public void testHasArgs_int() {
        OptionBuilder.hasArgs(2);
        Option option = OptionBuilder.create();
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgs_int() {
        OptionBuilder.hasOptionalArgs(2);
        Option option = OptionBuilder.create();
        assertEquals(2, option.getArgs());
        assertTrue(option.hasOptionalArg());
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
    public void testCreate_char() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }
    
    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getLongOpt());
    }
    
    @Test
    public void testCreate_String() {
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getOpt());
    }
}