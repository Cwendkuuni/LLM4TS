import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longOpt");
        assertNull(OptionBuilder.create().getLongOpt());
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
        OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.create().getArgName());
    }
  
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.create().isRequired());

        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.create().isRequired());
    }
  
    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.create().getValueSeparator());
    }
  
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.create().getValueSeparator());
    }
  
    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.create().isRequired());

        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.create().isRequired());
    }
  
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }
  
    @Test
    public void testHasArgsInt() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.create().getArgs());
    }
  
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.create().hasOptionalArg());

        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.create().getArgs());
    }
  
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }
  
    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(5);
        assertEquals(5, OptionBuilder.create().getArgs());
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }
  
    @Test
    public void testWithType() {
        OptionBuilder.withType(java.util.Date.class);
        assertEquals(java.util.Date.class, OptionBuilder.create().getType());
    }
  
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.create().getDescription());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCreateCharIllegalArgumentException() {
        OptionBuilder.create('x');
    }
  
    @Test(expected=IllegalArgumentException.class)
    public void testCreateNullIllegalArgumentException() {
        OptionBuilder.create();
    }
  
    @Test(expected=IllegalArgumentException.class)
    public void testCreateStringIllegalArgumentException() {
        OptionBuilder.create("opt");
    }
}