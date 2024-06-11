import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        // Test with a valid long option
        OptionBuilder.withLongOpt("testOption");
        Option option = OptionBuilder.create();
        assertEquals("testOption", option.getLongOpt());
        
        // Test with null long option
        OptionBuilder.withLongOpt(null);
        try {
            option = OptionBuilder.create();
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Exception thrown as expected
        }
    }
    
    @Test
    public void testHasArg() {
        // Test with hasArg true
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        
        // Test with hasArg false
        OptionBuilder.hasArg(false);
        option = OptionBuilder.create();
        assertFalse(option.hasArg());
    }
    
    @Test
    public void testWithArgName() {
        // Test with a valid argument name
        OptionBuilder.withArgName("testArg");
        Option option = OptionBuilder.create();
        assertEquals("testArg", option.getArgName());
        
        // Test with null argument name
        OptionBuilder.withArgName(null);
        option = OptionBuilder.create();
        assertNull(option.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        // Test with isRequired true
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
        
        // Test with isRequired false
        OptionBuilder.isRequired(false);
        option = OptionBuilder.create();
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testWithValueSeparator() {
        // Test with value separator
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create();
        assertEquals(':', option.getValueSeparator());
        
        // Test without value separator
        OptionBuilder.withValueSeparator();
        option = OptionBuilder.create();
        assertEquals('=', option.getValueSeparator());
    }
    
    @Test
    public void testHasArgs() {
        // Test with hasArgs
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }
    
    @Test
    public void testHasOptionalArgs() {
        // Test with hasOptionalArgs
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testWithArgs() {
        // Test with valid number of args
        OptionBuilder.hasArgs(2);
        Option option = OptionBuilder.create();
        assertEquals(2, option.getArgs());
        
        // Test with invalid number of args
        OptionBuilder.hasArgs(-1);
        option = OptionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }
    
    @Test
    public void testWithOptionalArg() {
        // Test with optional arg
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertEquals(1, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testWithType() {
        // Test with valid type
        OptionBuilder.withType(Integer.class);
        Option option = OptionBuilder.create();
        assertEquals(Integer.class, option.getType());
        
        // Test with null type
        OptionBuilder.withType(null);
        option = OptionBuilder.create();
        assertNull(option.getType());
    }
    
    @Test
    public void testWithDescription() {
        // Test with valid description
        OptionBuilder.withDescription("Test option");
        Option option = OptionBuilder.create();
        assertEquals("Test option", option.getDescription());
        
        // Test with null description
        OptionBuilder.withDescription(null);
        option = OptionBuilder.create();
        assertNull(option.getDescription());
    }
    
    @Test
    public void testCreate() {
        // Test create with long opt invariant
        OptionBuilder.withLongOpt("testOption");
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals("testOption", option.getLongOpt());
        
        // Test create without long opt
        OptionBuilder.reset();
        try {
            option = OptionBuilder.create();
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Exception thrown as expected
        }
    }
}