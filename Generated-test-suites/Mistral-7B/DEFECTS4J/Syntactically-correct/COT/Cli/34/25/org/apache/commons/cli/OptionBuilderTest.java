package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import java.lang.reflect.InvocationTargetException;

public class OptionBuilderTest {

    @org.junit.Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOption");
        Option option = optionBuilder.create();
        assertEquals("longOption", option.getLongOpt());
    }

    @org.junit.Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertEquals(true, option.hasArg());
    }

    @org.junit.Test
    public void testHasArgWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option option = optionBuilder.create();
        assertEquals("argName", option.getArgName());
    }

    @org.junit.Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        assertTrue(option.isRequired());
    }

    @org.junit.Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }

    @org.junit.Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @org.junit.Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        assertTrue(option.hasOptionalArg());
    }

    @org.junit.Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create();
        assertEquals(Integer.class, option.getType());
    }

    @org.junit.Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Option description");
        Option option = optionBuilder.create();
        assertEquals("Option description", option.getDescription());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.create();
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOptLength() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("");
        optionBuilder.create();
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidArgNameLength() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("");
        optionBuilder.create();
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testCreateWithNullDescription() throws NoSuchMethodException,
            InvocationTargetException, InstantiationException {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withDescription(null);
        optionBuilder.create();
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(-1);
        optionBuilder.create();
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String[].class);
        optionBuilder.create();
    }

}