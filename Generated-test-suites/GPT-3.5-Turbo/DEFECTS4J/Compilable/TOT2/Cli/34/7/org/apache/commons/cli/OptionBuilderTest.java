package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("testLongOpt").create();
        assertNotNull(option);
        assertEquals("testLongOpt", option.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertNotNull(option);
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testHasArgWithBoolean() {
        Option option = OptionBuilder.hasArg(true).create();
        assertNotNull(option);
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("testArgName").create();
        assertNotNull(option);
        assertEquals("testArgName", option.getArgName());
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
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        assertNotNull(option);
        assertEquals("t", option.getOpt());
    }
    
    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        assertNotNull(option);
    }
    
    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("test");
        assertNotNull(option);
        assertEquals("test", option.getOpt());
    }
}