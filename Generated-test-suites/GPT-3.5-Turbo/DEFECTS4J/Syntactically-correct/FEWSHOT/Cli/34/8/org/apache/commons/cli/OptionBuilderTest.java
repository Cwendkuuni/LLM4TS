
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgWithCustomValue() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        assertEquals("test", OptionBuilder.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertEquals(true, OptionBuilder.isRequired());
    }
    
    @Test
    public void testWithSeparator() {
        OptionBuilder.withValueSeparator(',');
        assertEquals(',', OptionBuilder.getValueSeparator());
    }
    
    @Test
    public void testWithDefaultSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.getValueSeparator());
    }
    
    @Test
    public void testIsRequiredWithCustomValue() {
        OptionBuilder.isRequired(true);
        assertEquals(true, OptionBuilder.isRequired());

        OptionBuilder.isRequired(false);
        assertEquals(false, OptionBuilder.isRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgsWithCustomValue() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(true, OptionBuilder.isOptionalArg());
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(true, OptionBuilder.isOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArgsWithCustomValue() {
        OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.getType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        assertEquals("test", OptionBuilder.getDescription());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCreateWithCharInvalid() {
        OptionBuilder.create('%');
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals(false, option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertNull(option.getArgName());
    }
    
    @Test
    public void testCreateWithoutChar() {
        OptionBuilder.withLongOpt("test")
                     .hasArg()
                     .withArgName("arg")
                     .isRequired()
                     .withValueSeparator(':')
                     .withType(Integer.class)
                     .withDescription("description");
        
        Option option = OptionBuilder.create();
        
        assertEquals(null, option.getOpt());
        assertEquals("test", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals(true, option.isRequired());
        assertEquals(false, option.hasOptionalArg());
        assertEquals(1, option.getArgs());
        assertEquals(Integer.class, option.getType());
        assertEquals(':', option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }
}
