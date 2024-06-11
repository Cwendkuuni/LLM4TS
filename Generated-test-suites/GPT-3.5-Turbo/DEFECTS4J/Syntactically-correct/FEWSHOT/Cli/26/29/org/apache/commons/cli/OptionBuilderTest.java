package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longopt");
        Option option = OptionBuilder.create();
        assertEquals("longopt", option.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testHasArgBoolean() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        
        OptionBuilder.hasArg(false);
        option = OptionBuilder.create();
        assertFalse(option.hasArg());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        Option option = OptionBuilder.create();
        assertEquals("argName", option.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testWithRequiredBoolean() {
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
        
        OptionBuilder.isRequired(false);
        option = OptionBuilder.create();
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }
    
    @Test
    public void testHasArgsInt() {
        OptionBuilder.hasArgs(4);
        Option option = OptionBuilder.create();
        assertEquals(4, option.getArgs());
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
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(3);
        Option option = OptionBuilder.create();
        assertEquals(3, option.getArgs());
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
        OptionBuilder.withDescription("description");
        Option option = OptionBuilder.create();
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('o');
        assertEquals("o", option.getOpt());
    }
    
    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("longopt");
        Option option = OptionBuilder.create();
        assertEquals("longopt", option.getLongOpt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateException() {
        OptionBuilder.create();
    }
    
    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("option");
        assertEquals("option", option.getOpt());
    }

}