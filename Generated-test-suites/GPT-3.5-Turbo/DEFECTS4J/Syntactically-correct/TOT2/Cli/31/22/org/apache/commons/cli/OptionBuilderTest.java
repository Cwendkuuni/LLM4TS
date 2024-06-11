import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder option = OptionBuilder.withLongOpt("testLongOpt");
        assertNotNull(option);
        assertEquals("testLongOpt", OptionBuilder.longopt);
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder option = OptionBuilder.hasArg();
        assertNotNull(option);
        assertEquals(1, OptionBuilder.numberOfArgs);
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder option = OptionBuilder.isRequired();
        assertNotNull(option);
        assertTrue(OptionBuilder.required);
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder option = OptionBuilder.withArgName("testArgName");
        assertNotNull(option);
        assertEquals("testArgName", OptionBuilder.argName);
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder option = OptionBuilder.withDescription("testDescription");
        assertNotNull(option);
        assertEquals("testDescription", OptionBuilder.description);
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('o');
        assertNotNull(option);
        assertEquals("o", option.getOpt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
    
    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("testOpt");
        assertNotNull(option);
        assertEquals("testOpt", option.getOpt());
    }
}