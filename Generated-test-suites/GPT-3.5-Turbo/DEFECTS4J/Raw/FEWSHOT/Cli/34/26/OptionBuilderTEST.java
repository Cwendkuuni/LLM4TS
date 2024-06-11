import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        assertEquals("abc", OptionBuilder.withLongOpt("abc").create().getLongOpt());
        assertNull(OptionBuilder.withLongOpt(null).create().getLongOpt());
    }

    @Test
    public void testHasArg() {
        assertEquals(1, OptionBuilder.hasArg().create().getArgs());
        assertEquals(Option.UNINITIALIZED, OptionBuilder.hasArg(false).create().getArgs());
    }

    @Test
    public void testWithArgName() {
        assertEquals("argName", OptionBuilder.withArgName("argName").create().getArgName());
        assertNull(OptionBuilder.withArgName(null).create().getArgName());
    }

    @Test
    public void testIsRequired() {
        assertEquals(true, OptionBuilder.isRequired().create().isRequired());
        assertEquals(false, OptionBuilder.isRequired(false).create().isRequired());
    }

    @Test
    public void testWithDescription() {
        assertEquals("desc", OptionBuilder.withDescription("desc").create().getDescription());
        assertNull(OptionBuilder.withDescription(null).create().getDescription());
    }

    @Test
    public void testWithArgs() {
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.hasArgs().create().getArgs());
        assertEquals(3, OptionBuilder.hasArgs(3).create().getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        assertEquals(true, OptionBuilder.hasOptionalArg().create().hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.hasOptionalArgs().create().getArgs());
        assertEquals(5, OptionBuilder.hasOptionalArgs(5).create().getArgs());
    }

    @Test
    public void testWithType() {
        assertEquals(String.class, OptionBuilder.withType(String.class).create().getType());
        assertNull(OptionBuilder.withType(null).create().getType());
    }

    @Test
    public void testWithValueSeparator() {
        assertEquals(':', OptionBuilder.withValueSeparator(':').create().getValueSeparator());
        assertEquals('=', OptionBuilder.withValueSeparator().create().getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChar() {
        OptionBuilder.create('a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate() {
        OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateString() {
        OptionBuilder.create("abc");
    }
}