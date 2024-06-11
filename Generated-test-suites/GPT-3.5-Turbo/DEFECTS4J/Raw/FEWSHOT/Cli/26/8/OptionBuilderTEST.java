package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longopt");
        assertEquals("longopt", OptionBuilder.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.isRequired());
    }
    
    @Test
    public void testWithOptionalArg() {
        OptionBuilder.hasArg().hasOptionalArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.hasOptionalArg());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(5);
        assertEquals(5, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.getDescription());
    }
    
    @Test
    public void testCreateWithChar() {
        Option opt = OptionBuilder.create('a');
        assertNotNull(opt);
        assertEquals("a", opt.getOpt());
        assertNull(opt.getLongOpt());
        assertNull(opt.getDescription());
    }
    
    @Test
    public void testCreateWithoutLongOpt() {
        try {
            OptionBuilder.create();
        } catch (IllegalArgumentException e) {
            assertEquals("must specify longopt", e.getMessage());
        }
    }
    
    @Test
    public void testCreateWithLongOpt() {
        OptionBuilder.withLongOpt("longopt");
        Option opt = OptionBuilder.create();
        assertNotNull(opt);
        assertNull(opt.getOpt());
        assertEquals("longopt", opt.getLongOpt());
        assertNull(opt.getDescription());
    }
    
    @Test
    public void testCreateWithStringOpt() {
        Option opt = OptionBuilder.create("opt");
        assertNotNull(opt);
        assertEquals("opt", opt.getOpt());
        assertNull(opt.getLongOpt());
        assertNull(opt.getDescription());
    }
}