package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {
  
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        // Check that the longopt value is set correctly
        assertEquals("test", OptionBuilder.create().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // Check that the numberOfArgs value is set correctly
        assertEquals(1, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasArg_WithArg() {
        OptionBuilder.hasArg(true);
        // Check that the numberOfArgs value is set correctly
        assertEquals(1, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasArg_WithoutArg() {
        OptionBuilder.hasArg(false);
        // Check that the numberOfArgs value is set correctly
        assertEquals(Option.UNINITIALIZED, OptionBuilder.create().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        // Check that the argName value is set correctly
        assertEquals("test", OptionBuilder.create().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // Check that the required value is set correctly
        assertTrue(OptionBuilder.create().isRequired());
    }

    @Test
    public void testWithValueSeparator_char() {
        OptionBuilder.withValueSeparator(':');
        // Check that the valueSeparator value is set correctly
        assertEquals(':', OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testWithValueSeparator_default() {
        OptionBuilder.withValueSeparator();
        // Check that the valueSeparator value is set correctly
        assertEquals('=', OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testIsRequired_withRequired() {
        OptionBuilder.isRequired(true);
        // Check that the required value is set correctly
        assertTrue(OptionBuilder.create().isRequired());
    }

    @Test
    public void testIsRequired_withoutRequired() {
        OptionBuilder.isRequired(false);
        // Check that the required value is set correctly
        assertFalse(OptionBuilder.create().isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // Check that the numberOfArgs value is set correctly
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgs_int() {
        OptionBuilder.hasArgs(3);
        // Check that the numberOfArgs value is set correctly
        assertEquals(3, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // Check that the numberOfArgs value is set correctly
        assertEquals(1, OptionBuilder.create().getArgs());
        // Check that the optionalArg value is set correctly
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // Check that the numberOfArgs value is set correctly
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
        // Check that the optionalArg value is set correctly
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgs_int() {
        OptionBuilder.hasOptionalArgs(3);
        // Check that the numberOfArgs value is set correctly
        assertEquals(3, OptionBuilder.create().getArgs());
        // Check that the optionalArg value is set correctly
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        // Check that the type value is set correctly
        assertEquals(Integer.class, OptionBuilder.create().getType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        // Check that the description value is set correctly
        assertEquals("test", OptionBuilder.create().getDescription());
    }

}