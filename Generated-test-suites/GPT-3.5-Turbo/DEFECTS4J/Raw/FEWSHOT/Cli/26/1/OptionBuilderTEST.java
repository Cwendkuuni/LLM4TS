import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("myOption");
        // Verify the long option is set
        assertEquals("myOption", OptionBuilder.getLongopt());
    }
    
    @Test
    public void testHasArg() {
        // Test when hasArg is true
        OptionBuilder.hasArg();
        // Verify the number of arguments is set to 1
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        
        // Test when hasArg is false
        OptionBuilder.hasArg(false);
        // Verify the number of arguments is set to Option.UNINITIALIZED
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("myArg");
        // Verify the argument name is set
        assertEquals("myArg", OptionBuilder.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // Verify the option is required
        assertTrue(OptionBuilder.isRequired());
    }
    
    @Test
    public void testWithOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // Verify the number of arguments is set to 1
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        // Verify the option is optional
        assertTrue(OptionBuilder.isOptionalArg());
    }
    
    @Test
    public void testWithOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // Verify the number of arguments is set to Option.UNLIMITED_VALUES
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        // Verify the option is optional
        assertTrue(OptionBuilder.isOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs(3);
        // Verify the number of arguments is set to 3
        assertEquals(3, OptionBuilder.getNumberOfArgs());
        // Verify the option is optional
        assertTrue(OptionBuilder.isOptionalArg());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("myDescription");
        // Verify the description is set
        assertEquals("myDescription", OptionBuilder.getDescription());
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('o');
        // Verify the option is created with the correct short option
        assertEquals("o", option.getOpt());
        // Verify the option is not required
        assertFalse(option.isRequired());
        // Verify the option has 0 arguments
        assertEquals(0, option.getArgs());
    }
    
    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("myOption");
        // Verify the option is created with the correct long option
        assertEquals("myOption", option.getLongOpt());
        // Verify the option is not required
        assertFalse(option.isRequired());
        // Verify the option has 0 arguments
        assertEquals(0, option.getArgs());
    }
}