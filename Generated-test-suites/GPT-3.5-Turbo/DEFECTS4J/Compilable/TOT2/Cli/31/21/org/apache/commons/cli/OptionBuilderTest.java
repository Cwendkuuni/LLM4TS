package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("test").create();
        assertNotNull(option);
        assertEquals("test", option.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertNotNull(option);
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("argName").create();
        assertNotNull(option);
        assertEquals("argName", option.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assertNotNull(option);
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':').create();
        assertNotNull(option);
        assertEquals(':', option.getValueSeparator());
    }
    
    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("test description").create();
        assertNotNull(option);
        assertEquals("test description", option.getDescription());
    }
    
    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('c');
        assertNotNull(option);
        assertEquals('c', option.getOpt());
    }
    
    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("opt");
        assertNotNull(option);
        assertEquals("opt", option.getOpt());
    }
    
    // Add more test cases for additional methods if needed
    
}