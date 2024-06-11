import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        
        assertEquals("test", OptionBuilder.create().getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        
        assertTrue(OptionBuilder.create().hasArg());
    }
    
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        
        assertTrue(OptionBuilder.create().hasArg());
        
        OptionBuilder.hasArg(false);
        
        assertFalse(OptionBuilder.create().hasArg());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        
        assertEquals("test", OptionBuilder.create().getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        
        assertTrue(OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        
        assertEquals(':', OptionBuilder.create().getValueSeparator());
        
        OptionBuilder.withValueSeparator();
        
        assertEquals('=', OptionBuilder.create().getValueSeparator());
    }
    
    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        
        assertTrue(OptionBuilder.create().isRequired());
        
        OptionBuilder.isRequired(false);
        
        assertFalse(OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder.hasArgs(5);
        
        assertEquals(5, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        
        assertTrue(OptionBuilder.create().hasArg());
        assertTrue(OptionBuilder.create().hasOptionalArg());
        assertFalse(OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
        assertTrue(OptionBuilder.create().hasOptionalArg());
        assertFalse(OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder.hasOptionalArgs(3);
        
        assertEquals(3, OptionBuilder.create().getArgs());
        assertTrue(OptionBuilder.create().hasOptionalArg());
        assertFalse(OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        
        assertEquals(Integer.class, OptionBuilder.create().getType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        
        assertEquals("test", OptionBuilder.create().getDescription());
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals('\0', option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }
    
    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("test").isRequired().hasArgs();
        
        Option option = OptionBuilder.create();
        
        assertNull(option.getOpt());
        assertEquals("test", option.getLongOpt());
        assertNull(option.getDescription());
        assertTrue(option.hasArg());
        assertTrue(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals('\0', option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }
    
    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("test");
        
        assertEquals("test", option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals('\0', option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }
    
}