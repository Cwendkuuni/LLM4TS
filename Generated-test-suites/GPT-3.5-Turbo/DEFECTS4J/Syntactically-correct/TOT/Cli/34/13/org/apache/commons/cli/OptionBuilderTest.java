import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    // Test case for withLongOpt method
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.getLongOpt());
    }
    
    // Test case for hasArg method
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }
    
    // Test case for hasArg(boolean hasArg) method
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        
        OptionBuilder.hasArg(false);
        assertEquals(-1, OptionBuilder.getNumberOfArgs());
    }
    
    // Test case for withArgName method
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        assertEquals("test", OptionBuilder.getArgName());
    }
    
    // Test case for isRequired method
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.isRequired());
    }
    
    // Test case for withValueSeparator(char sep) method
    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.getValueSeparator());
    }
    
    // Test case for withValueSeparator() method
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.getValueSeparator());
    }
    
    // Test case for isRequired(boolean newRequired) method
    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.isRequired());
        
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.isRequired());
    }
    
    // Test case for hasArgs() method
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }
    
    // Test case for hasArgs(int num) method
    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.getNumberOfArgs());
    }
    
    // Test case for hasOptionalArg() method
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.hasOptionalArg());
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }
    
    // Test case for hasOptionalArgs() method
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertTrue(OptionBuilder.hasOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }
    
    // Test case for hasOptionalArgs(int numArgs) method
    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(3);
        assertTrue(OptionBuilder.hasOptionalArg());
        assertEquals(3, OptionBuilder.getNumberOfArgs());
    }
    
    // Test case for withType(Class newType) method
    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.getType());
    }
    
    // Test case for withDescription(String newDescription) method
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        assertEquals("test", OptionBuilder.getDescription());
    }
    
    // Test case for create(char opt) method
    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }
    
    // Test case for create() method
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
    
    // Test case for create(String opt) method
    @Test
    public void testCreateWithOpt() {
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getOpt());
    }
}