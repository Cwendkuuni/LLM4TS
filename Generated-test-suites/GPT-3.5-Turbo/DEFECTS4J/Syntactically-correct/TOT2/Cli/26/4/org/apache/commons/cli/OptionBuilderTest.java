import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.create().getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.create().getArgs());
        
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArg");
        assertEquals("testArg", OptionBuilder.create().getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.create().isRequired());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test description");
        assertEquals("test description", OptionBuilder.create().getDescription());
    }
    
    @Test
    public void testCreateWithOpt() {
        Option option = OptionBuilder.create('t');
        assertEquals('t', option.getOpt());
    }
    
    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        assertNotNull(option);
    }
    
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator('=');
        assertEquals('=', OptionBuilder.create().getValueSeparator());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.create().getType());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }
}