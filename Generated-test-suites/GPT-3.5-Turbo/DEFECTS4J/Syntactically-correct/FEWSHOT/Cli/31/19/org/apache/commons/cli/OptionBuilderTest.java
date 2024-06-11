package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionBuilderTest {

    private OptionBuilder optionBuilder;
    
    @Before
    public void setup() {
        optionBuilder = new OptionBuilder();
    }
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder result = optionBuilder.withLongOpt("test");
        assertEquals(optionBuilder, result);
        assertEquals("test", optionBuilder.getLongopt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder result = optionBuilder.hasArg();
        assertEquals(optionBuilder, result);
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArg_WithParameter() {
        OptionBuilder result = optionBuilder.hasArg(true);
        assertEquals(optionBuilder, result);
        assertEquals(1, optionBuilder.getNumberOfArgs());
        
        result = optionBuilder.hasArg(false);
        assertEquals(optionBuilder, result);
        assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder result = optionBuilder.withArgName("name");
        assertEquals(optionBuilder, result);
        assertEquals("name", optionBuilder.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder result = optionBuilder.isRequired();
        assertEquals(optionBuilder, result);
        assertTrue(optionBuilder.isRequired());
    }
    
    @Test
    public void testWithValueSeparator_Char() {
        OptionBuilder result = optionBuilder.withValueSeparator(':');
        assertEquals(optionBuilder, result);
        assertEquals(':', optionBuilder.getValuesep());
    }
    
    @Test
    public void testWithValueSeparator_Default() {
        OptionBuilder result = optionBuilder.withValueSeparator();
        assertEquals(optionBuilder, result);
        assertEquals('=', optionBuilder.getValuesep());
    }
    
    @Test
    public void testIsRequired_WithParameter() {
        OptionBuilder result = optionBuilder.isRequired(true);
        assertEquals(optionBuilder, result);
        assertTrue(optionBuilder.isRequired());
        
        result = optionBuilder.isRequired(false);
        assertEquals(optionBuilder, result);
        assertFalse(optionBuilder.isRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder result = optionBuilder.hasArgs();
        assertEquals(optionBuilder, result);
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgs_WithParameter() {
        OptionBuilder result = optionBuilder.hasArgs(2);
        assertEquals(optionBuilder, result);
        assertEquals(2, optionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder result = optionBuilder.hasOptionalArg();
        assertEquals(optionBuilder, result);
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder result = optionBuilder.hasOptionalArgs();
        assertEquals(optionBuilder, result);
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArgs_WithParameter() {
        OptionBuilder result = optionBuilder.hasOptionalArgs(2);
        assertEquals(optionBuilder, result);
        assertTrue(optionBuilder.isOptionalArg());
        assertEquals(2, optionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testWithType() {
        OptionBuilder result = optionBuilder.withType(Integer.class);
        assertEquals(optionBuilder, result);
        assertEquals(Integer.class, optionBuilder.getType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder result = optionBuilder.withDescription("description");
        assertEquals(optionBuilder, result);
        assertEquals("description", optionBuilder.getDescription());
    }
    
    @Test
    public void testCreate_Char() throws IllegalArgumentException {
        OptionBuilder result = optionBuilder.create('c');
        assertEquals(optionBuilder, result);
        
        Option option = optionBuilder.create();
        assertEquals("c", option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgs());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }
    
    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder result = optionBuilder.withLongOpt("test").create();
        assertEquals(optionBuilder, result);
        
        Option option = optionBuilder.create();
        assertNull(option.getOpt());
        assertEquals("test", option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgs());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }
    
    @Test
    public void testCreate_WithStringParameter() throws IllegalArgumentException {
        OptionBuilder result = optionBuilder.withLongOpt("test").create("t1");
        assertEquals(optionBuilder, result);
        
        Option option = optionBuilder.create();
        assertEquals("t1", option.getOpt());
        assertEquals("test", option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgs());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }

}