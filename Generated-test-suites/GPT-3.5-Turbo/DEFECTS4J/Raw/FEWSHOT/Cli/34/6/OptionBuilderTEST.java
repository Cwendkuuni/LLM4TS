import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.reset();
        assertEquals("foo", OptionBuilder.withLongOpt("foo").create().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.reset();
        assertEquals(1, OptionBuilder.hasArg().create().getArgs());
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder.reset();
        assertEquals(1, OptionBuilder.hasArg(true).create().getArgs());
        assertEquals(Option.UNINITIALIZED, OptionBuilder.hasArg(false).create().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.reset();
        assertEquals("foo", OptionBuilder.withArgName("foo").create().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.reset();
        assertEquals(true, OptionBuilder.isRequired().create().isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.reset();
        assertEquals(':', OptionBuilder.withValueSeparator(':').create().getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.reset();
        assertEquals('=', OptionBuilder.withValueSeparator().create().getValueSeparator());
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder.reset();
        assertEquals(true, OptionBuilder.isRequired(true).create().isRequired());
        assertEquals(false, OptionBuilder.isRequired(false).create().isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.reset();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.hasArgs().create().getArgs());
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder.reset();
        assertEquals(5, OptionBuilder.hasArgs(5).create().getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.reset();
        assertEquals(1, OptionBuilder.hasOptionalArg().create().getArgs());
        assertEquals(true, OptionBuilder.hasOptionalArg().create().hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.reset();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.hasOptionalArgs().create().getArgs());
        assertEquals(true, OptionBuilder.hasOptionalArgs().create().hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.reset();
        assertEquals(5, OptionBuilder.hasOptionalArgs(5).create().getArgs());
        assertEquals(true, OptionBuilder.hasOptionalArgs(5).create().hasOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.reset();
        assertEquals(Integer.class, OptionBuilder.withType(Integer.class).create().getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.reset();
        assertEquals("foo", OptionBuilder.withDescription("foo").create().getDescription());
    }

    @Test
    public void testCreateChar() {
        OptionBuilder.reset();
        assertEquals("f", OptionBuilder.create('f').getOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder.reset();
        assertNull(OptionBuilder.create().getLongOpt());
    }

    @Test
    public void testCreateString() {
        OptionBuilder.reset();
        assertEquals("foo", OptionBuilder.create("foo").getOpt());
    }
}