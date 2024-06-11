import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testLongOption");
        assertEquals("testLongOption", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArgName");
        assertEquals("testArgName", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.required);
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        Object type = new Object();
        OptionBuilder.withType(type);
        assertEquals(type, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        assertEquals("testDescription", OptionBuilder.description);
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getValueSeparator());
        assertNull(option.getDescription());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("testLongOption");
        Option option = OptionBuilder.create();
        assertNull(option.getOpt());
        assertEquals("testLongOption", option.getLongOpt());
        assertNull(option.getValueSeparator());
        assertNull(option.getDescription());
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("t");
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getValueSeparator());
        assertNull(option.getDescription());
    }
}