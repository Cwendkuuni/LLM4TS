import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("test").create();
        assertNotNull(option);
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertNotNull(option);
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        Option option = OptionBuilder.hasArg(true).create();
        assertNotNull(option);
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("name").create();
        assertNotNull(option);
        assertEquals("name", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assertNotNull(option);
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':').create();
        assertNotNull(option);
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        assertNotNull(option);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testCreateWithOptChar() {
        Option option = OptionBuilder.create('o');
        assertNotNull(option);
        assertEquals("o", option.getOpt());
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testCreateWithOptString() {
        Option option = OptionBuilder.create("opt");
        assertNotNull(option);
        assertEquals("opt", option.getOpt());
    }

}