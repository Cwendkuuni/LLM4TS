import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.withLongOpt("test").toString());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.hasArg().intValue());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.hasArg(true).intValue());
        OptionBuilder.hasArg(false);
        assertEquals(-1, OptionBuilder.hasArg(false).intValue());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        assertEquals("test", OptionBuilder.withArgName("test").toString());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertEquals(true, OptionBuilder.isRequired().booleanValue());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.withValueSeparator(':').charValue());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.withValueSeparator().charValue());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        assertEquals(true, OptionBuilder.isRequired(true).booleanValue());
        OptionBuilder.isRequired(false);
        assertEquals(false, OptionBuilder.isRequired(false).booleanValue());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(-2, OptionBuilder.hasArgs().intValue());
    }

    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.hasArgs(2).intValue());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.hasOptionalArg().intValue());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(-2, OptionBuilder.hasOptionalArgs().intValue());
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.hasOptionalArgs(2).intValue());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Class.class);
        assertEquals(Class.class, OptionBuilder.withType(Class.class).getClass());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        assertEquals("test", OptionBuilder.withDescription("test").toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithChar() {
        OptionBuilder.create('t');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithString() {
        assertEquals("t", OptionBuilder.create("t").toString());
    }
}