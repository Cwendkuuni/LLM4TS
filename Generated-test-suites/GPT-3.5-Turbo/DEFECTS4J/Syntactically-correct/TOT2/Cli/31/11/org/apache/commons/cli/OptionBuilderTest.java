import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option opt = OptionBuilder.withLongOpt("test").create();
        assertEquals("test", opt.getLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option opt = OptionBuilder.hasArg().create();
        assertEquals(1, opt.getArgs());
    }
    
    @Test
    public void testHasArgWithBoolean() {
        Option opt = OptionBuilder.hasArg(true).create();
        assertEquals(1, opt.getArgs());
    }
    
    @Test
    public void testWithArgName() {
        Option opt = OptionBuilder.withArgName("name").create();
        assertEquals("name", opt.getArgName());
    }
    
    @Test
    public void testIsRequired() {
        Option opt = OptionBuilder.isRequired().create();
        assertTrue(opt.isRequired());
    }
    
    @Test
    public void testWithValueSeparator() {
        Option opt = OptionBuilder.withValueSeparator(':').create();
        assertEquals(':', opt.getValueSeparator());
    }
    
    @Test
    public void testWithOptionalArg() {
        Option opt = OptionBuilder.hasOptionalArg().create();
        assertTrue(opt.hasOptionalArg());
    }
    
    @Test
    public void testWithArgs() {
        Option opt = OptionBuilder.hasArgs().create();
        assertEquals(Integer.MAX_VALUE, opt.getArgs());
    }
    
    @Test
    public void testWithOptionalArgs() {
        Option opt = OptionBuilder.hasOptionalArgs().create();
        assertEquals(Integer.MAX_VALUE, opt.getArgs());
        assertTrue(opt.hasOptionalArg());
    }
    
    @Test
    public void testWithOptionalArgsNum() {
        Option opt = OptionBuilder.hasOptionalArgs(2).create();
        assertEquals(2, opt.getArgs());
        assertTrue(opt.hasOptionalArg());
    }
    
    @Test
    public void testWithType() {
        Object type = new Object();
        Option opt = OptionBuilder.withType(type).create();
        assertEquals(type, opt.getType());
    }
    
    @Test
    public void testWithDescription() {
        Option opt = OptionBuilder.withDescription("description").create();
        assertEquals("description", opt.getDescription());
    }

    @Test
    public void testCreateChar() {
        Option opt = OptionBuilder.create('c');
        assertEquals("c", opt.getOpt());
    }
    
    @Test
    public void testCreateString() {
        Option opt = OptionBuilder.withLongOpt("test").create();
        assertEquals("test", opt.getLongOpt());
    }

}