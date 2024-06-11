import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");
        
        assertEquals("longopt", OptionBuilder.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        
        optionBuilder = OptionBuilder.hasArg(false);
        
        assertEquals(-1, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        
        assertEquals("argName", OptionBuilder.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        
        assertTrue(OptionBuilder.isRequired());
    }
    
    @Test
    public void testWithValueSeparatorCharacter() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        
        assertEquals(':', OptionBuilder.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        
        assertEquals('=', OptionBuilder.getValueSeparator());
    }
    
    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        
        assertTrue(OptionBuilder.isRequired());
        
        optionBuilder = OptionBuilder.isRequired(false);
        
        assertFalse(OptionBuilder.isRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        
        assertEquals(-2, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        
        assertEquals(3, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        
        assertTrue(OptionBuilder.hasOptionalArg());
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        
        assertTrue(OptionBuilder.hasOptionalArg());
        assertEquals(-2, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        
        assertTrue(OptionBuilder.hasOptionalArg());
        assertEquals(3, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        
        assertEquals(String.class, OptionBuilder.getType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        
        assertEquals("description", OptionBuilder.getDescription());
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('c');
        
        assertEquals("c", option.getOpt());
    }
    
    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("longopt");
        Option option = OptionBuilder.create();
        
        assertEquals("longopt", option.getLongOpt());
    }
    
    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("opt");
        
        assertEquals("opt", option.getOpt());
    }
}