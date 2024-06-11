import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {
    
    // Test for withLongOpt method
    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", OptionBuilder.create().getLongOpt());
    }
    
    // Test for hasArg method
    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.create().getArgs());
    }
    
    // Test for hasArg with false condition
    @Test
    public void testHasArgWithFalse() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALISED, OptionBuilder.create().getArgs());
    }
    
    // Test for withArgName method
    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("testArgName");
        assertEquals("testArgName", OptionBuilder.create().getArgName());
    }
    
    // Test for isRequired method
    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(OptionBuilder.create().isRequired());
    }
    
    // Test for withValueSeparator method
    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.create().getValueSeparator());
    }
    
    // Test for create method with char parameter
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals('t', option.getOpt());
    }
    
    // Test for create method without any parameter
    @Test
    public void testCreateWithoutParam() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        Option option = OptionBuilder.create();
        assertEquals("testLongOpt", option.getLongOpt());
    }
    
    // Test for create method with String parameter
    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("testOpt");
        assertEquals("testOpt", option.getOpt());
    }
}