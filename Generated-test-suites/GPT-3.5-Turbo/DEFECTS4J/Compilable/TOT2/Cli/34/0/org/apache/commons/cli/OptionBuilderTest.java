package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("testLongOpt").create();
        assertNotNull(option.getLongOpt());
        assertEquals("testLongOpt", option.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testHasArgs() {
        Option option = OptionBuilder.hasArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }
    
    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        char separator = ':';
        Option option = OptionBuilder.withValueSeparator(separator).create();
        assertEquals(separator, option.getValueSeparator());
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create('t');
        assertNotNull(option);
        assertEquals('t', option.getOpt());
    }
    // Add more test cases for other methods in OptionBuilder class
    
}