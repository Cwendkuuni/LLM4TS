import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        Option option = Option.create();
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = Option.create();
        assertTrue(option.hasArg());
    }
    
    // other test methods for the remaining public methods
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("arg");
        Option option = Option.create();
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = Option.create();
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test description");
        Option option = Option.create();
        assertEquals("test description", option.getDescription());
    }
    
    // additional test methods for edge cases and exception handling scenarios
    
    @Test(expected=IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        Option.create();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCreateWithInvalidCharOpt() {
        Option.create('*');
    }
}