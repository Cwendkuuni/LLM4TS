package org.apache.commons.cli;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("value").create();
        assertEquals("value", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        Option option = OptionBuilder.hasArg(true).create();
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgWithoutBoolean() {
        Option option = OptionBuilder.hasArg(false).create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("name").create();
        assertEquals("name", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        Option option = OptionBuilder.withValueSeparator(':').create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator().create();
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        Option option = OptionBuilder.isRequired(true).create();
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testHasArgs() {
        Option option = OptionBuilder.hasArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        Option option = OptionBuilder.hasArgs(2).create();
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        Option option = OptionBuilder.hasOptionalArgs(2).create();
        assertEquals(2, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        Option option = OptionBuilder.withType(Integer.class).create();
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("description").create();
        assertEquals("description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithChar() {
        OptionBuilder.create('a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("option");
        assertEquals("option", option.getOpt());
    }

}