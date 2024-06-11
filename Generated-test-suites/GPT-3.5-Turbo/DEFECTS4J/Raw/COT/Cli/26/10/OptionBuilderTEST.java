import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("newLongopt");
        assertEquals("newLongopt", OptionBuilder.getLongopt());
        OptionBuilder.reset();
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        OptionBuilder.reset();
    }
    
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
        OptionBuilder.reset();
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("name");
        assertEquals("name", OptionBuilder.getArgName());
        OptionBuilder.reset();
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.isRequired());
        OptionBuilder.reset();
    }
    
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.getValuesep());
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.getValuesep());
        OptionBuilder.reset();
    }
    
    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.isRequired());
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.isRequired());
        OptionBuilder.reset();
    }
    
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        OptionBuilder.reset();
    }
    
    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.getNumberOfArgs());
        OptionBuilder.reset();
    }
    
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
        OptionBuilder.reset();
    }
    
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
        OptionBuilder.reset();
    }
    
    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
        OptionBuilder.reset();
    }
    
    @Test
    public void testWithType() {
        Object obj = new Object();
        OptionBuilder.withType(obj);
        assertEquals(obj, OptionBuilder.getType());
        OptionBuilder.reset();
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("newDescription");
        assertEquals("newDescription", OptionBuilder.getDescription());
        OptionBuilder.reset();
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('D');
        assertEquals("D", option.getOpt());
        assertNull(option.getLongOpt());
        OptionBuilder.reset();
    }
    
    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("newLongopt");
        Option option = OptionBuilder.create();
        assertNull(option.getOpt());
        assertEquals("newLongopt", option.getLongOpt());
        OptionBuilder.reset();
    }
    
    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("D");
        assertEquals("D", option.getOpt());
        assertNull(option.getLongOpt());
        OptionBuilder.reset();
    }
}