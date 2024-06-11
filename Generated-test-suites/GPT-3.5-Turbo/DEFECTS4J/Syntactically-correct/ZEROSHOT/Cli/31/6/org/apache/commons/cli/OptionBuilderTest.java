import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Before
    public void setUp() {
        OptionBuilder.reset();
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", OptionBuilder.create().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgWithValue() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.create().getArgs());
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.create().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArgName");
        assertEquals("testArgName", OptionBuilder.create().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.create().isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testIsRequiredWithValue() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.create().isRequired());
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.create().isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgsWithValue() {
        OptionBuilder.hasArgs(5);
        assertEquals(5, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.create().hasOptionalArg());
        assertEquals(1, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertTrue(OptionBuilder.create().hasOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArgsWithValue() {
        OptionBuilder.hasOptionalArgs(3);
        assertTrue(OptionBuilder.create().hasOptionalArg());
        assertEquals(3, OptionBuilder.create().getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.create().getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        assertEquals("testDescription", OptionBuilder.create().getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoLongOpt() {
        OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoLongOptAndReset() {
        OptionBuilder.withDescription("testDescription");
        assertNotEquals(null, OptionBuilder.create());
        OptionBuilder.reset();
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertEquals(null, option.getDescription());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertEquals(null, option.getDescription());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testCreateWithProperties() {
        OptionBuilder.withLongOpt("testLongOpt");
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("testArgName");
        OptionBuilder.isRequired();
        OptionBuilder.withValueSeparator(':');
        OptionBuilder.hasArgs();
        OptionBuilder.hasOptionalArg();
        OptionBuilder.withType(String.class);
        OptionBuilder.withDescription("testDescription");

        Option option = OptionBuilder.create();
        assertEquals("testLongOpt", option.getLongOpt());
        assertEquals(1, option.getArgs());
        assertEquals("testArgName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(':', option.getValueSeparator());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertTrue(option.hasOptionalArg());
        assertEquals(String.class, option.getType());
        assertEquals("testDescription", option.getDescription());
    }
}