package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        // Typical use case
        OptionBuilder.withLongOpt("test");
        
        assertEquals("test", OptionBuilder.create().getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        // Typical use case
        OptionBuilder.hasArg();
        
        assertEquals(1, OptionBuilder.create().getArgs());
        
        // Edge case
        OptionBuilder.hasArg(false);
        
        assertEquals(-1, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testWithArgName() {
        // Typical use case
        OptionBuilder.withArgName("name");
        
        assertEquals("name", OptionBuilder.create().getArgName());
    }
    
    @Test
    public void testIsRequired() {
        // Typical use case
        OptionBuilder.isRequired();
        
        assertTrue(OptionBuilder.create().isRequired());
        
        // Edge case
        OptionBuilder.isRequired(false);
        
        assertFalse(OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testWithValueSeparator() {
        // Typical use case
        OptionBuilder.withValueSeparator(':');
        
        assertEquals(':', OptionBuilder.create().getValueSeparator());
    }
    
    @Test
    public void testHasOptionalArg() {
        // Typical use case
        OptionBuilder.hasOptionalArg();
        
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }

    // Add more test cases for other methods

}