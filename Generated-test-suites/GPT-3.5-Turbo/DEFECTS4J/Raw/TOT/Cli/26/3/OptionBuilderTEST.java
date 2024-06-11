import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("option");
        Option option = OptionBuilder.create();
        assertEquals("option", option.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testHasArgWithArgumentTrue() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testHasArgWithArgumentFalse() {
        OptionBuilder.hasArg(false);
        Option option = OptionBuilder.create();
        assertFalse(option.hasArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }
    
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("name");
        Option option = OptionBuilder.create();
        assertEquals("name", option.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        Option option = OptionBuilder.create();
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testWithOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testWithOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testWithOptionalArgsNumber() {
        OptionBuilder.hasOptionalArgs(3);
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertEquals(3, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }
    
    @Test
    public void testWithDefaultArgName() {
        Option option = OptionBuilder.create('-');
        assertEquals("arg", option.getArgName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
    
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('c');
        assertEquals("c", option.getOpt());
    }
    
    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("opt");
        assertEquals("opt", option.getOpt());
    }
    
}