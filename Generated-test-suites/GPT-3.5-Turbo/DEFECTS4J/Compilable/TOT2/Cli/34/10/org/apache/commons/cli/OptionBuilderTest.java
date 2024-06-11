package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("testLongOpt").create();
        assertEquals("testLongOpt", option.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertTrue(option.hasArgs());
    }
    
    @Test
    public void testHasArgWithBoolean() {
        Option option = OptionBuilder.hasArg(true).create();
        assertTrue(option.hasArgs());
        
        Option option2 = OptionBuilder.hasArg(false).create();
        assertFalse(option2.hasArgs());
    }
    
    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("argName").create();
        assertEquals("argName", option.getArgName());
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
    public void testIsRequiredWithBoolean() {
        Option option = OptionBuilder.isRequired(true).create();
        assertTrue(option.isRequired());
        
        Option option2 = OptionBuilder.isRequired(false).create();
        assertFalse(option2.isRequired());
    }
    
    @Test
    public void testHasArgs() {
        Option option = OptionBuilder.hasArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }
    
    @Test
    public void testHasArgsWithInt() {
        Option option = OptionBuilder.hasArgs(2).create();
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }
    
    @Test
    public void testHasOptionalArgsWithInt() {
        Option option = OptionBuilder.hasOptionalArgs(3).create();
        assertEquals(3, option.getArgs());
    }
    
    @Test
    public void testWithType() {
        Option option = OptionBuilder.withType(Integer.class).create();
        assertEquals(Integer.class, option.getType());
    }
    
    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("testDescription").create();
        assertEquals("testDescription", option.getDescription());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithCharException() {
        OptionBuilder.create('t');
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOptException() {
        OptionBuilder.create();
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.withLongOpt("testLongOpt").create('t');
        assertEquals('t', option.getOpt());
    }
    
}