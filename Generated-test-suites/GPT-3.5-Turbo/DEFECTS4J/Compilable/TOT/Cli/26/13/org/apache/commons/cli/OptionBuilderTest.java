package org.apache.commons.cli;
import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        // Typical use case
        Option option = OptionBuilder.withLongOpt("opt").create();
        assertEquals("opt", option.getLongOpt());
        
        // Edge case: empty string
        Option option2 = OptionBuilder.withLongOpt("").create();
        assertEquals("", option2.getLongOpt());
        
        // Error scenario: null long option value
        try {
            Option option3 = OptionBuilder.withLongOpt(null).create();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testHasArg() {
        // Typical use case
        Option option = OptionBuilder.hasArg().create();
        assertEquals(1, option.getArgs());
        
        // Error scenario: hasArg(false)
        Option option2 = OptionBuilder.hasArg(false).create();
        assertEquals(Option.UNINITIALIZED, option2.getArgs());
    }
    
    @Test
    public void testWithArgName() {
        // Typical use case
        Option option = OptionBuilder.withArgName("name").create();
        assertEquals("name", option.getArgName());
        
        // Edge case: empty string
        Option option2 = OptionBuilder.withArgName("").create();
        assertEquals("", option2.getArgName());
        
        // Error scenario: null argument name
        try {
            Option option3 = OptionBuilder.withArgName(null).create();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testIsRequired() {
        // Typical use case
        Option option = OptionBuilder.isRequired().create();
        assertTrue(option.isRequired());
        
        // Error scenario: isRequired(false)
        Option option2 = OptionBuilder.isRequired(false).create();
        assertFalse(option2.isRequired());
    }
    
    @Test
    public void testWithValueSeparator() {
        // Typical use case
        Option option = OptionBuilder.withValueSeparator(':').create();
        assertEquals(':', option.getValueSeparator());
        
        // Error scenario: withValueSeparator()
        Option option2 = OptionBuilder.withValueSeparator().create();
        assertEquals('=', option2.getValueSeparator());
    }
    
    @Test
    public void testIsOptionalArg() {
        // Typical use case
        Option option = OptionBuilder.hasOptionalArg().create();
        assertEquals(1, option.getArgs());
        assertTrue(option.hasOptionalArg());
        
        // Typical use case: hasOptionalArgs()
        Option option2 = OptionBuilder.hasOptionalArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option2.getArgs());
        assertTrue(option2.hasOptionalArg());
        
        // Typical use case: hasOptionalArgs(2)
        Option option3 = OptionBuilder.hasOptionalArgs(2).create();
        assertEquals(2, option3.getArgs());
        assertTrue(option3.hasOptionalArg());
        
        // Error scenario: hasOptionalArg() called after hasArg()
        OptionBuilder.hasArg();
        try {
            Option option4 = OptionBuilder.hasOptionalArg().create();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testWithArgs() {
        // Typical use case
        Option option = OptionBuilder.hasArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        
        // Typical use case: hasArgs(2)
        Option option2 = OptionBuilder.hasArgs(2).create();
        assertEquals(2, option2.getArgs());
        
        // Error scenario: hasArgs() called after hasOptionalArg()
        OptionBuilder.hasOptionalArg();
        try {
            Option option3 = OptionBuilder.hasArgs().create();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testWithType() {
        // Typical use case
        Option option = OptionBuilder.withType(Integer.class).create();
        assertEquals(Integer.class, option.getType());
        
        // Edge case: null type
        Option option2 = OptionBuilder.withType(null).create();
        assertNull(option2.getType());
    }
    
    @Test
    public void testWithDescription() {
        // Typical use case
        Option option = OptionBuilder.withDescription("description").create();
        assertEquals("description", option.getDescription());
        
        // Edge case: empty description
        Option option2 = OptionBuilder.withDescription("").create();
        assertEquals("", option2.getDescription());
        
        // Error scenario: null description
        try {
            Option option3 = OptionBuilder.withDescription(null).create();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCreateChar() {
        // Typical use case
        Option option = OptionBuilder.create('a');
        assertEquals("a", option.getOpt());
        
        // Error scenario: invalid character
        try {
            Option option2 = OptionBuilder.create('-');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCreate() {
        // Typical use case
        Option option = OptionBuilder.withLongOpt("opt").create();
        assertNotNull(option);
        
        // Error scenario: longOpt not set
        try {
            Option option2 = OptionBuilder.create();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCreateString() {
        // Typical use case
        Option option = OptionBuilder.create("opt");
        assertEquals("opt", option.getOpt());
        
        // Edge case: empty string
        Option option2 = OptionBuilder.create("");
        assertEquals("", option2.getOpt());
        
        // Error scenario: null opt string
        try {
            Option option3 = OptionBuilder.create(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
}