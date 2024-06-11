import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArg_WithBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("name");
        assertEquals("name", OptionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertEquals(true, OptionBuilder.getRequired());
    }

    @Test
    public void testWithSameValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.getValuesep());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.getValuesep());
    }

    @Test
    public void testIsRequired_WithBoolean() {
        OptionBuilder.isRequired(true);
        assertEquals(true, OptionBuilder.getRequired());
        OptionBuilder.isRequired(false);
        assertEquals(false, OptionBuilder.getRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgs_WithNum() {
        OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(true, OptionBuilder.getOptionalArg());
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(true, OptionBuilder.getOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgs_WithNumArgs() {
        OptionBuilder.hasOptionalArgs(3);
        assertEquals(true, OptionBuilder.getOptionalArg());
        assertEquals(3, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test description");
        assertEquals("test description", OptionBuilder.getDescription());
    }

    @Test
    public void testCreate_WithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_EmptyLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreate_WithLongOpt() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        assertNull(option.getOpt());
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testCreate_WithLongOptAndChar() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create("t");
        assertEquals("t", option.getOpt());
        assertEquals("test", option.getLongOpt());
    }
}