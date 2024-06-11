import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
    
    @Test
    public void testWithLongOpt() {
        Option opt = OptionBuilder.withLongOpt("test").create();
        assertNotNull(opt.getLongOpt());
        assertEquals("test", opt.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option opt = OptionBuilder.hasArg().create('t');
        assertTrue(opt.hasArg());
    }

    @Test
    public void testHasArgWithTrue() {
        Option opt = OptionBuilder.hasArg(true).create('t');
        assertTrue(opt.hasArg());
    }

    @Test
    public void testHasArgWithFalse() {
        Option opt = OptionBuilder.hasArg(false).create('t');
        assertFalse(opt.hasArg());
    }

    @Test
    public void testWithArgName() {
        Option opt = OptionBuilder.withArgName("name").create('t');
        assertEquals("name", opt.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option opt = OptionBuilder.isRequired().create('t');
        assertTrue(opt.isRequired());
    }

    @Test
    public void testWithDescription() {
        Option opt = OptionBuilder.withDescription("description").create('t');
        assertEquals("description", opt.getDescription());
    }
    
    @Test
    public void testCreateWithChar() {
        Option opt = OptionBuilder.create('t');
        assertEquals('t', opt.getOpt());
    }
    
    @Test
    public void testCreateWithNullChar() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            OptionBuilder.create((char) 0);
        });
        assertNotNull(thrown.getMessage());
    }
    
    @Test
    public void testCreate() {
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            OptionBuilder.create();
        });
        assertNotNull(thrown.getMessage());
    }
    
    @Test
    public void testCreateWithString() {
        Option opt = OptionBuilder.create("test");
        assertEquals("test", opt.getOpt());
    }
    
    @Test
    public void testWithOptionalArg() {
        Option opt = OptionBuilder.hasOptionalArg().create('t');
        assertTrue(opt.hasOptionalArg());
    }
    
    @Test
    public void testWithOptionalArgs() {
        Option opt = OptionBuilder.hasOptionalArgs().create('t');
        assertTrue(opt.hasOptionalArgs());
    }
    
    @Test
    public void testWithOptionalArgsNumber() {
        Option opt = OptionBuilder.hasOptionalArgs(3).create('t');
        assertEquals(3, opt.getArgs());
    }
    
    @Test
    public void testWithType() {
        Option opt = OptionBuilder.withType(Integer.class).create('t');
        assertEquals(Integer.class, opt.getType());
    }
    
}