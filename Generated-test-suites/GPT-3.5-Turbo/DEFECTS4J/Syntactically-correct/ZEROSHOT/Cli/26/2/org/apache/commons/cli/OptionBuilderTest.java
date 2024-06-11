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
    public void testHasArgBoolean() {
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
    public void testWithSeparator() {
        OptionBuilder.withValueSeparator('=');
        assertEquals('=', OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testWithSeparatorDefault() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testIsRequiredBoolean() {
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
    public void testHasArgsInt() {
        OptionBuilder.hasArgs(5);
        assertEquals(5, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.create().getArgs());
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, OptionBuilder.create().getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        assertEquals("testDescription", OptionBuilder.create().getDescription());
    }
}