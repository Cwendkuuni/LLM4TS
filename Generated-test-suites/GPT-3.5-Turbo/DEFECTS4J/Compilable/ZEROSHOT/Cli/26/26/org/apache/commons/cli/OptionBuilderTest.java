package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        assertNotNull(OptionBuilder.create());
        assertEquals("test", OptionBuilder.create().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertNotNull(OptionBuilder.create());
        assertEquals(1, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        assertNotNull(OptionBuilder.create());
        assertEquals(1, OptionBuilder.create().getArgs());
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.create().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        assertNotNull(OptionBuilder.create());
        assertEquals("test", OptionBuilder.create().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertNotNull(OptionBuilder.create());
        assertTrue(OptionBuilder.create().isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        assertNotNull(OptionBuilder.create());
        assertEquals(':', OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertNotNull(OptionBuilder.create());
        assertEquals('=', OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        assertNotNull(OptionBuilder.create());
        assertTrue(OptionBuilder.create().isRequired());
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.create().isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertNotNull(OptionBuilder.create());
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(3);
        assertNotNull(OptionBuilder.create());
        assertEquals(3, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertNotNull(OptionBuilder.create());
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertNotNull(OptionBuilder.create());
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(3);
        assertNotNull(OptionBuilder.create());
        assertEquals(3, OptionBuilder.create().getArgs());
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertNotNull(OptionBuilder.create());
        assertEquals(String.class, OptionBuilder.create().getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        assertNotNull(OptionBuilder.create());
        assertEquals("test", OptionBuilder.create().getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithChar() {
        OptionBuilder.create('t');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNoLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithCharOpt() {
        Option option = OptionBuilder.create("t");
        assertNotNull(option);
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreateWithOpt() {
        Option option = OptionBuilder.create("test");
        assertNotNull(option);
        assertEquals("test", option.getOpt());
    }
}