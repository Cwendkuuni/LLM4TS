package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionShort() {
        Options options = new Options();
        options.addOption('f');
        assertEquals(1, options.getOptions().size());
        Option option = options.getOptions().get(0);
        assertEquals('f', option.getKey());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getArgName());
        assertNull(option.getValue());
    }

    @Test
    public void testAddOptionShortWithArg() {
        Options options = new Options();
        options.addOption('f', true);
        assertEquals(1, options.getOptions().size());
        Option option = options.getOptions().get(0);
        assertEquals('f', option.getKey());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertNull(option.getArgName());
        assertNull(option.getValue());
    }

    @Test
    public void testAddOptionShortWithArgAndDescription() {
        Options options = new Options();
        options.addOption('f', true, "foo");
        assertEquals(1, options.getOptions().size());
        Option option = options.getOptions().get(0);
        assertEquals('f', option.getKey());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("foo", option.getDescription());
        assertNull(option.getArgName());
        assertNull(option.getValue());
    }

    @Test
    public void testAddOptionShortWithArgAndArgName() {
        Options options = new Options();
        options.addOption('f', "arg");
        assertEquals(1, options.getOptions().size());
        Option option = options.getOptions().get(0);
        assertEquals('f', option.getKey());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertNotNull(option.getArgName());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
    }

    @Test
    public void testAddOptionShortWithArgAndArgNameAndDescription() {
        Options options = new Options();
        options.addOption('f', "arg", "foo");
        assertEquals(1, options.getOptions().size());
        Option option = options.getOptions().get(0);
        assertEquals('f', option.getKey());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertNotNull(option.getArgName());
        assertEquals("arg", option.getArgName());
        assertEquals("foo", option.getDescription());
        assertNull(option.getValue());
    }

    @Test
    public void testAddOptionShortWithValue() {
        Options options = new Options();
        options.addOption('f', "FOO");
        assertEquals(1, options.getOptions().size());
        Option option = options.getOptions().get(0);
        assertEquals('f', option.getKey());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getArgName());
        assertEquals("FOO", option.getValue());
    }

    @Test
    public void testAddOptionLong() {
        Options options = new Options();
        options.addOption("foo", true);
        assertEquals(1, options.getOptions().size());
        Option option = options.getOptions().get(0);
        assertNull(option.getKey());
        assertEquals("foo", option.getLongOpt());
        assertTrue(option.hasArg());
        assertNull(option.getArgName());
        assertNull(option.getValue());
    }

    @Test
    public void testAddOptionLongWithArg() {
        Options options = new Options();
        options.addOption("foo", true, "bar");
        assertEquals(1, options.getOptions().size());
        Option option = options.getOptions().get(0);
        assertNull(option.getKey());
        assertEquals("foo", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("bar", option.getArgName());
        assertNull(option.getValue());
    }

    @Test
    public void testAddOptionLongWithArgAndDescription() {
        Options options = new Options();
        options.addOption("foo", true, "bar", "baz");
        assertEquals(1, options.getOptions().size());
        Option option = options.getOptions().get(0);
        assertNull(option.getKey());
        assertEquals("foo", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("bar", option.getArgName());
        assertEquals("baz", option.getDescription());
        assertNull(option.getValue());
    }

    @Test
    public void testAddOptionLongWithValue() {
        Options options = new Options();
        options.addOption("foo", true, "FOO");
        assertEquals(1, options.getOptions().size());
        Option option = options.getOptions().get(0);
        assertNull(option.getKey());
        assertEquals("foo", option.getLongOpt());
        assertTrue(option.hasArg());
        assertNull(option.getArgName());
        assertEquals("FOO", option.getValue());
    }

    @Test
    public void testAddOptionWithLongOptAndShortOpt() {
        Options options = new Options();
        options.addOption("foo", true);
        options.addOption('f');
        assertEquals(2, options.getOptions().size());
        Option option1 = options.getOptions().get(0);
        assertNull(option1.getKey());
        assertEquals("foo", option1.getLongOpt());
        assertTrue(option1.hasArg());
        assertNull(option1.getArgName());
        assertNull(option1.getValue());

        Option option2 = options.getOptions().get(1);
        assertEquals('f', option2.getKey());
        assertNull(option2.getLongOpt());
        assertFalse(option2.hasArg());
        assertNull(option2.getArgName());
        assertNull(option2.getValue());
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        Options options = new Options();
        options.addOptionGroup(group);
        assertEquals(0, options.getOptions().size());
        assertEquals(1, options.getOptionGroups().size());
        OptionGroup optionGroup = options.getOptionGroups().get(0);
        assertEquals("group", optionGroup.getKey());
        assertTrue(optionGroup.isRequired());
        assertEquals(0, optionGroup.getOptions().size());
    }

    @Test
    public void testAddOptionWithOptionGroup() {
        OptionGroup group = new OptionGroup("group");
        Options options = new Options();
        Option option = new Option('f', true, "foo", "bar", group);
        options.addOption(option);
        assertEquals(1, options.getOptions().size());
        Option o = options.getOptions().get(0);
        assertEquals('f', o.getKey());
        assertNull(o.getLongOpt());
        assertTrue(o.hasArg());
        assertNull(o.getArgName());
        assertEquals("foo", o.getDescription());
        assertEquals("bar", o.getOptionGroup().getKey());
        assertTrue(o.getOptionGroup().isRequired());
    }
}