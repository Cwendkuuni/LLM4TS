import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        // Test with a valid long option
        assertNotNull(OptionBuilder.create());
        
        // Test with a null long option
        assertNull(OptionBuilder.create(null));
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // Test with an option that requires an argument
        assertNotNull(OptionBuilder.create());
        
        OptionBuilder.hasArg(false);
        // Test with an option that does not require an argument
        assertNotNull(OptionBuilder.create());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        // Test with a valid argument name
        assertNotNull(OptionBuilder.create());
        
        // Test with a null argument name
        assertNull(OptionBuilder.create(null));
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // Test with a required option
        assertNotNull(OptionBuilder.create());
        
        OptionBuilder.isRequired(false);
        // Test with an optional option
        assertNotNull(OptionBuilder.create());
    }
    
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        // Test with a valid value separator
        assertNotNull(OptionBuilder.create());
        
        OptionBuilder.withValueSeparator();
        // Test with the default value separator
        assertNotNull(OptionBuilder.create());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // Test with an option that can have unlimited argument values
        assertNotNull(OptionBuilder.create());
        
        OptionBuilder.hasArgs(2);
        // Test with an option that can have a specific number of argument values
        assertNotNull(OptionBuilder.create());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // Test with an option that can have an optional argument
        assertNotNull(OptionBuilder.create());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        // Test with a valid description
        assertNotNull(OptionBuilder.create());
        
        // Test with a null description
        assertNull(OptionBuilder.create(null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullLongOpt() {
        OptionBuilder.create();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidOpt() {
        OptionBuilder.create("test");
    }

}