package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();
        assertEquals(null, OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("--foo");
        assertEquals("--foo", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArg_false() {
        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        assertEquals("argName", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparator_empty() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequired_false() {
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgs_num() {
        OptionBuilder.hasArgs(5);
        assertEquals(5, OptionBuilder.numberOfArgs);
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
    public void testHasOptionalArgs_num() {
        OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("A test Option");
        assertEquals("A test Option", OptionBuilder.description);
    }

    @Test
    public void testCreate_char() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("--foo");
        OptionBuilder.hasArg();
        OptionBuilder.isRequired();
        OptionBuilder.withValueSeparator(':');

        Option option = OptionBuilder.create('F');
        assertEquals("--foo", option.getLongOpt());
        assertEquals(Option.REQUIRED_ARG, option.getArgType());
        assertEquals(1, option.getArgs().length);
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreate_null() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("--foo");
        Option option = OptionBuilder.create(null);
        assertEquals("--foo", option.getLongOpt());
        assertNull(option.getArgType());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
    }

    @Test
    public void testCreate_empty() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("--foo");
        Option option = OptionBuilder.create("");
        assertEquals("--foo", option.getLongOpt());
        assertNull(option.getArgType());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
    }

    @Test
    public void testCreate_empty_char() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("--foo");
        Option option = OptionBuilder.create(' ');
        assertEquals("--foo", option.getLongOpt());
        assertNull(option.getArgType());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
    }

    @Test
    public void testCreate_hasOptionalArgs() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("--foo");
        OptionBuilder.hasOptionalArgs(2);

        Option option = OptionBuilder.create('F');
        assertEquals("--foo", option.getLongOpt());
        assertEquals(Option.OPTIONAL_ARG, option.getArgType());
        assertEquals(2, option.getArgs().length);
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreate_hasOptionalArg() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("--foo");
        OptionBuilder.hasOptionalArg();

        Option option = OptionBuilder.create('F');
        assertEquals("--foo", option.getLongOpt());
        assertEquals(Option.OPTIONAL_ARG, option.getArgType());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreate_hasArgs() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("--foo");
        OptionBuilder.hasArgs();

        Option option = OptionBuilder.create('F');
        assertEquals("--foo", option.getLongOpt());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgType());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreate_hasArgs_num() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("--foo");
        OptionBuilder.hasArgs(5);

        Option option = OptionBuilder.create('F');
        assertEquals("--foo", option.getLongOpt());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgType());
        assertEquals(5, option.getArgs().length);
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreate_withType() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("--foo");
        OptionBuilder.withType(Integer.class);

        Option option = OptionBuilder.create('F');
        assertEquals("--foo", option.getLongOpt());
        assertEquals(Option.VALUE_OPTION, option.getArgType());
        assertNull(option.getValueSeparator());
        assertEquals(Integer.class, option.getValue());
    }

    @Test
    public void testCreate_withDescription() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("--foo");
        OptionBuilder.withDescription("A test Option");

        Option option = OptionBuilder.create('F');
        assertEquals("--foo", option.getLongOpt());
        assertEquals("A test Option", option.getDescription());
        assertNull(option.getValueSeparator());
    }
}