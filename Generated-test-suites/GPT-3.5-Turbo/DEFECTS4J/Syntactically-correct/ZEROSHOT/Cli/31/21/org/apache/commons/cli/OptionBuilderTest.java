import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", OptionBuilder.getLongopt());
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
        OptionBuilder.withArgName("testArgName");
        assertEquals("testArgName", OptionBuilder.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertEquals(true, OptionBuilder.isRequired());
    }
    
    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.getValuesep());
    }
    
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.getValuesep());
    }
    
    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        assertEquals(true, OptionBuilder.isRequired());
        
        OptionBuilder.isRequired(false);
        assertEquals(false, OptionBuilder.isRequired());
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(-1, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.getNumberOfArgs());
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        assertEquals(true, OptionBuilder.isOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(-1, OptionBuilder.getNumberOfArgs());
        assertEquals(true, OptionBuilder.isOptionalArg());
    }
    
    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(5);
        assertEquals(5, OptionBuilder.getNumberOfArgs());
        assertEquals(true, OptionBuilder.isOptionalArg());
    }
    
    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, OptionBuilder.getType());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        assertEquals("testDescription", OptionBuilder.getDescription());
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
        assertNull(option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(-1, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }
    
    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("testLongOpt");
        Option option = OptionBuilder.create();
        assertNull(option.getOpt());
        assertNull(option.getDescription());
        assertEquals("testLongOpt", option.getLongOpt());
        assertTrue(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(-1, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }
    
    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("testOpt");
        assertEquals("testOpt", option.getOpt());
        assertNull(option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(-1, option.getArgs());
        assertNull(option.getType());
        assertEquals((char) 0, option.getValueSeparator());
        assertEquals("arg", option.getArgName());
    }
}