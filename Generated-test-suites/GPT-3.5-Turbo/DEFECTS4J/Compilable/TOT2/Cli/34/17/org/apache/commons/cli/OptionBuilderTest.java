package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.create().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.create().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("arg");
        assertEquals("arg", OptionBuilder.create().getArgName());
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
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.create().isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgsWithNum() {
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
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.create().getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.create().getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.create().getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithChar() {
        OptionBuilder.withLongOpt("char").create('c');
        assertEquals("char", OptionBuilder.create().getLongOpt());
    }

    @Test
    public void testCreateWithString() {
        OptionBuilder.withLongOpt("string").create("test");
        assertEquals("string", OptionBuilder.create().getLongOpt());
    }
}