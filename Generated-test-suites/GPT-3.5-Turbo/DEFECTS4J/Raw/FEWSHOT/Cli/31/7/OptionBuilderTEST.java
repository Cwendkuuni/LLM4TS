package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longOpt1");
        assertEquals("longOpt1", OptionBuilder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgWithArg() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName1");
        assertEquals("argName1", OptionBuilder.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.isRequired());
    }
    
    @Test
    public void testWithRequired() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.isRequired());
        
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.isRequired());
    }
    
    @Test
    public void testWithValueseparator() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.getValuesep());
    }
    
    @Test
    public void testWithValueseparatorDefault() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.getValuesep());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.getNumberOfArgs());
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
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description1");
        assertEquals("description1", OptionBuilder.getDescription());
    }
    
    @Test
    public void testCreateWithCharOpt() {
        Option option = OptionBuilder.create('o');
        assertEquals("o", option.getOpt());
        assertNull(option.getLongOpt());
    }
    
    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        assertNull(option.getOpt());
        assertNotNull(option.getLongOpt());
    }
    
    @Test
    public void testCreateWithOpt() {
        Option option = OptionBuilder.create("o");
        assertEquals("o", option.getOpt());
        assertNull(option.getLongOpt());
    }
}