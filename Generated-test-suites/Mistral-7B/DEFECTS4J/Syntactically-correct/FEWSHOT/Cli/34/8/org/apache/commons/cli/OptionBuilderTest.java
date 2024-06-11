package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals('\0', OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("foo");
        assertEquals("foo", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithArgName() {
        OptionBuilder.hasArg().withArgName("bar");
        assertEquals("bar", OptionBuilder.argName);
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
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        assertEquals("description", OptionBuilder.description);
    }

    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("foo").hasArg().create('f');

        Option option = OptionBuilder.create('f');
        assertNotNull(option);
        assertEquals("foo", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals("", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getType());
        assertEquals('', option.getValueSeparator());
        assertFalse(option.isOptional());
    }

    @Test
    public void testCreateWithDescription() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("foo").hasArg().withDescription("description").create('f');

        Option option = OptionBuilder.create('f');
        assertNotNull(option);
        assertEquals("foo", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals("", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getType());
        assertEquals('', option.getValueSeparator());
        assertFalse(option.isOptional());
    }

    @Test
    public void testCreateWithArgName() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("foo").hasArg().withArgName("argName").create('f');

        Option option = OptionBuilder.create('f');
        assertNotNull(option);
        assertEquals("foo", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getType());
        assertEquals('', option.getValueSeparator());
        assertFalse(option.isOptional());
    }

    @Test
    public void testCreateWithType() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("foo").hasArg().withType(String.class).create('f');

        Option option = OptionBuilder.create('f');
        assertNotNull(option);
        assertEquals("foo", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals("", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertSame(String.class, option.getType());
        assertEquals('', option.getValueSeparator());
        assertFalse(option.isOptional());
    }

    @Test
    public void testCreateWithDescriptionAndType() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("foo").hasArg().withDescription("description").withType(String.class).create('f');

        Option option = OptionBuilder.create('f');
        assertNotNull(option);
        assertEquals("foo", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals("", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertSame(String.class, option.getType());
        assertEquals('', option.getValueSeparator());
        assertFalse(option.isOptional());
    }

    @Test
    public void testCreateWithValueSeparator() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("foo").hasArg().withValueSeparator(':').create('f');

        Option option = OptionBuilder.create('f');
        assertNotNull(option);
        assertEquals("foo", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals("", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getType());
        assertEquals(':', option.getValueSeparator());
        assertFalse(option.isOptional());
    }

    @Test
    public void testCreateWithDescriptionAndValueSeparator() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("foo").hasArg().withDescription("description").withValueSeparator(':').create('f');

        Option option = OptionBuilder.create('f');
        assertNotNull(option);
        assertEquals("foo", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals("", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getType());
        assertEquals(':', option.getValueSeparator());
        assertFalse(option.isOptional());
    }

    @Test
    public void testCreateWithArgNameAndValueSeparator() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("foo").hasArg().withArgName("argName").withValueSeparator(':').create('f');

        Option option = OptionBuilder.create('f');
        assertNotNull(option);
        assertEquals("foo", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getType());
        assertEquals(':', option.getValueSeparator());
        assertFalse(option.isOptional());
    }

    @Test
    public void testCreateWithDescriptionArgNameAndValueSeparator() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("foo").hasArg().withDescription("description").withArgName("argName").withValueSeparator(':').create('f');

        Option option = OptionBuilder.create('f');
        assertNotNull(option);
        assertEquals("foo", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getType());
        assertEquals(':', option.getValueSeparator());
        assertFalse(option.isOptional());
    }

    @Test
    public void testCreateWithOptionalArg() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("foo").hasOptionalArg().create('f');

        Option option = OptionBuilder.create('f');
        assertNotNull(option);
        assertEquals("foo", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals("", option.getArgName());
        assertFalse(option.isRequired());
        assertArrayEquals(new String[0], option.getArgs());
        assertNull(option.getType());
        assertEquals('', option.getValueSeparator());
        assertTrue(option.isOptional());
    }

    @Test
    public void testCreateWithOptionalArgs() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("foo").hasOptionalArgs().create('f');

        Option option = OptionBuilder.create('f');
        assertNotNull(option);
        assertEquals("foo", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals("", option.getArgName());
        assertFalse(option.isRequired());
        assertArrayEquals(new String[Option.UNLIMITED_VALUES], option.getArgs());
        assertNull(option.getType());
        assertEquals('', option.getValueSeparator());
        assertTrue(option.isOptional());
    }

    @Test
    public void testCreateWithOptionalArgsAndValueSeparator() throws IllegalArgumentException {
        OptionBuilder.withLongOpt("foo").hasOptionalArgs().withValueSeparator(':').create('f');

        Option option = OptionBuilder.create('f');
        assertNotNull(option);
        assertEquals("foo", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals("", option.getArgName());
        assertFalse(option.isRequired());
        assertArrayEquals(new String[Option.UNLIMITED_VALUES], option.getArgs());
        assertNull(option.getType());
        assertEquals(':', option.getValueSeparator());
        assertTrue(option.isOptional());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() throws IllegalArgumentException {
        OptionBuilder.create();
    }
}