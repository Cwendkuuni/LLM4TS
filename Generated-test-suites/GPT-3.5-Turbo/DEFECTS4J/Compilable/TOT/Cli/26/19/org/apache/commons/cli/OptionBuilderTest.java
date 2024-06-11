package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("testLongOpt").create();
        assertEquals("testLongOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        Option option = OptionBuilder.hasArg(true).create();
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        option = OptionBuilder.hasArg(false).create();
        assertFalse(option.hasArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("testArgName").create();
        assertEquals("testArgName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':').create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorDefault() {
        Option option = OptionBuilder.withValueSeparator().create();
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        Option option = OptionBuilder.isRequired(true).create();
        assertTrue(option.isRequired());
        option = OptionBuilder.isRequired(false).create();
        assertFalse(option.isRequired());
    }

    @Test
    public void testHasArgs() {
        Option option = OptionBuilder.hasArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        Option option = OptionBuilder.hasArgs(5).create();
        assertEquals(5, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        assertTrue(option.hasOptionalArg());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        Option option = OptionBuilder.hasOptionalArgs(3).create();
        assertEquals(3, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        Option option = OptionBuilder.withType(Object.class).create();
        assertEquals(Object.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("testDescription").create();
        assertEquals("testDescription", option.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        assertNull(option.getOpt());
        assertNotNull(option.getLongOpt());
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("testOption");
        assertEquals("testOption", option.getOpt());
    }

}