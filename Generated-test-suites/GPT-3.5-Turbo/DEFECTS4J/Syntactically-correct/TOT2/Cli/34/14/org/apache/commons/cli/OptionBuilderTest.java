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
        Option opt = OptionBuilder.withLongOpt("test").hasArg().create();
        assertTrue(opt.hasArg());
        assertEquals(1, opt.getArgs());
    }

    @Test
    public void testHasArgWithArgNumber() {
        Option opt = OptionBuilder.withLongOpt("test").hasArg(2).create();
        assertTrue(opt.hasArg());
        assertEquals(2, opt.getArgs());
    }

    @Test
    public void testWithArgName() {
        Option opt = OptionBuilder.withLongOpt("test").withArgName("name").create();
        assertEquals("name", opt.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option opt = OptionBuilder.withLongOpt("test").isRequired().create();
        assertTrue(opt.isRequired());
    }

    @Test
    public void testWithDescription() {
        Option opt = OptionBuilder.withLongOpt("test").withDescription("description").create();
        assertEquals("description", opt.getDescription());
    }

    @Test
    public void testWithValueSeparator() {
        Option opt = OptionBuilder.withLongOpt("test").withValueSeparator(':').create();
        assertEquals(':', opt.getValueSeparator());
    }

    @Test
    public void testCreate() {
        Option opt = OptionBuilder.withLongOpt("test").create();
        assertNotNull(opt);
        assertEquals("test", opt.getLongOpt());
    }

}