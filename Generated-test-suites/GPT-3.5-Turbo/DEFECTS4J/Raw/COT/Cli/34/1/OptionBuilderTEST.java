import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("longOpt")
                                    .create();
        assertEquals("longOpt", option.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg()
                                    .create();
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
    }
    
    //Add test cases for other methods...
    
    @Test
    public void testCreateWithOpt() {
        Option option = OptionBuilder.create("opt");
        assertEquals("opt", option.getOpt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        Option option = OptionBuilder.create(); // Should throw an exception
    }
}