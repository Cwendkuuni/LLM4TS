import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longopt");
        assertEquals("longopt", OptionBuilder.getLongopt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        
        OptionBuilder.hasArg(false);
        assertEquals(-1, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.isRequired());
    }
    
    @Test
    public void testWithOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.hasOptionalArg());
    }
    
    @Test
    public void testWithOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.getType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.getDescription());
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('c');
        assertNotNull(option);
        assertEquals("c", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertEquals(false, option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(false, option.hasOptionalArg());
        assertEquals((char) 0, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }
    
    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("longopt");
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals(null, option.getOpt());
        assertEquals("longopt", option.getLongOpt());
        assertEquals(false, option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(false, option.hasOptionalArg());
        assertEquals((char) 0, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }
    
    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("opt");
        assertNotNull(option);
        assertEquals("opt", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertEquals(false, option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(false, option.hasOptionalArg());
        assertEquals((char) 0, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }
    
}