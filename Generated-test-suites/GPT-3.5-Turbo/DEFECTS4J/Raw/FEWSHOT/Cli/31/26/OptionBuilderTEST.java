import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("opt");
        assertEquals("opt", OptionBuilder.create().getLongOpt());
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
        OptionBuilder.withArgName("name");
        assertEquals("name", OptionBuilder.create().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.create().isRequired());
    }

    @Test
    public void testWithDefaultValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.create().isRequired());
        OptionBuilder.isRequired(false);
        assertEquals(false, OptionBuilder.create().isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.create().getArgs());
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
        OptionBuilder.withDescription("desc");
        assertEquals("desc", OptionBuilder.create().getDescription());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('o');
        assertEquals("o", option.getOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("opt");
        Option option = OptionBuilder.create();
        assertEquals("opt", option.getLongOpt());
        OptionBuilder.reset();
        try {
            OptionBuilder.create();
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateString() {
        OptionBuilder.withLongOpt("opt");
        Option option = OptionBuilder.create("o");
        assertEquals("o", option.getOpt());
    }
}