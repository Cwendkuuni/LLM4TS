import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        
        assertEquals("testLongOpt", OptionBuilder.getOption().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        
        assertEquals(1, OptionBuilder.getOption().getArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        
        assertEquals(1, OptionBuilder.getOption().getArgs());
        
        optionBuilder = OptionBuilder.hasArg(false);
        
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getOption().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("testArgName");
        
        assertEquals("testArgName", OptionBuilder.getOption().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        
        assertTrue(OptionBuilder.getOption().isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        
        assertEquals(':', OptionBuilder.getOption().getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        
        assertEquals('=', OptionBuilder.getOption().getValueSeparator());
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        
        assertTrue(OptionBuilder.getOption().isRequired());
        
        optionBuilder = OptionBuilder.isRequired(false);
        
        assertFalse(OptionBuilder.getOption().isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getOption().getArgs());
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        
        assertEquals(3, OptionBuilder.getOption().getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        
        assertEquals(1, OptionBuilder.getOption().getArgs());
        assertTrue(OptionBuilder.getOption().hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getOption().getArgs());
        assertTrue(OptionBuilder.getOption().hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        
        assertEquals(2, OptionBuilder.getOption().getArgs());
        assertTrue(OptionBuilder.getOption().hasOptionalArg());
    }

    @Test
    public void testWithType() {
        Object type = new Object();
        OptionBuilder optionBuilder = OptionBuilder.withType(type);
        
        assertEquals(type, OptionBuilder.getOption().getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("testDescription");
        
        assertEquals("testDescription", OptionBuilder.getOption().getDescription());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('a');
        
        assertEquals('a', option.getOpt());
        assertNull(option.getLongOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        Option option = OptionBuilder.create();
        
        assertNull(option.getOpt());
        assertEquals("testLongOpt", option.getLongOpt());
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("testOpt");
        
        assertEquals("testOpt", option.getOpt());
        assertNull(option.getLongOpt());
    }
}