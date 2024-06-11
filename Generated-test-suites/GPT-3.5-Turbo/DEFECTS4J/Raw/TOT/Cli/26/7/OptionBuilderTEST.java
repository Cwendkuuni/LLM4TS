import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("input");
        
        Option option = builder.create();
        
        assertEquals("input", option.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder builder = OptionBuilder.hasArg();
        
        Option option = builder.create("i");
        
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder builder = OptionBuilder.withArgName("filename");
        
        Option option = builder.create("f");
        
        assertEquals("filename", option.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder builder = OptionBuilder.isRequired();
        
        Option option = builder.create("r");
        
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder builder = OptionBuilder.withDescription("This is a test option");
        
        Option option = builder.create("t");
        
        assertEquals("This is a test option", option.getDescription());
    }
    
    // Test edge cases and error scenarios
    
    @Test(expected=IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder builder = OptionBuilder.create();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCreateWithInvalidOptChar() {
        OptionBuilder builder = OptionBuilder.create("123");
    }
    
}