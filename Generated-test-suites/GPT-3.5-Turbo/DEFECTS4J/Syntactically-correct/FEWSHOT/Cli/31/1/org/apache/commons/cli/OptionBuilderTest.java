
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        Option option = OptionBuilder.create("t");
        assertEquals("testLongOpt", option.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create("t");
        assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testHasArg_WithFalse() {
        OptionBuilder.hasArg(false);
        Option option = OptionBuilder.create("t");
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArgName");
        Option option = OptionBuilder.create("t");
        assertEquals("testArgName", option.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create("t");
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testWithSeparator() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create("t");
        assertEquals(':', option.getValueSeparator());
    }
    
    @Test
    public void testWithSeparator_Default() {
        OptionBuilder.withValueSeparator();
        Option option = OptionBuilder.create("t");
        assertEquals('=', option.getValueSeparator());
    }
    
    @Test
    public void testIsRequired_WithFalse() {
        OptionBuilder.isRequired(false);
        Option option = OptionBuilder.create("t");
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create("t");
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }
    
    @Test
    public void testHasArgs_WithNum() {
        OptionBuilder.hasArgs(3);
        Option option = OptionBuilder.create("t");
        assertEquals(3, option.getArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create("t");
        assertTrue(option.hasOptionalArg());
        assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create("t");
        assertTrue(option.hasOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }
    
    @Test
    public void testHasOptionalArgs_WithNumArgs() {
        OptionBuilder.hasOptionalArgs(2);
        Option option = OptionBuilder.create("t");
        assertTrue(option.hasOptionalArg());
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testWithType() {
        Object type = new Object();
        OptionBuilder.withType(type);
        Option option = OptionBuilder.create("t");
        assertEquals(type, option.getType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        Option option = OptionBuilder.create("t");
        assertEquals("testDescription", option.getDescription());
    }
    
    @Test
    public void testCreate_WithCharOpt() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }
    
    @Test
    public void testCreate_WithNullOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        Option option = OptionBuilder.create();
        assertEquals("testLongOpt", option.getLongOpt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreate_WithoutLongOpt() {
        OptionBuilder.create();
    }

}
