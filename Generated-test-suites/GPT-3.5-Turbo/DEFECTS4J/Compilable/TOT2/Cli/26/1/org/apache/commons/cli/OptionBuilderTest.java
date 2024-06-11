package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("test").create();
        assertEquals("test", option.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testHasArgWithArg() {
        Option option = OptionBuilder.hasArg(true).create();
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("name").create();
        assertEquals("name", option.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':').create();
        assertEquals(':', option.getValueSeparator());
    }
    
    @Test
    public void testIsOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("description").create();
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testCreateOption() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }
}