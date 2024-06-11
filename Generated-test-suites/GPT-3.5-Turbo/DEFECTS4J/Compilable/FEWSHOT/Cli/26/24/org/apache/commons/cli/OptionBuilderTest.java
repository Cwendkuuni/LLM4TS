package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longopt");
        Option option = OptionBuilder.create();
        assertEquals("longopt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create();
        assertEquals(1, option.getArgs());

        OptionBuilder.hasArg(false);
        option = OptionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        Option option = OptionBuilder.create();
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testWithValueSeparatorWithChar() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        Option option = OptionBuilder.create();
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create();
        assertEquals(true, option.isRequired());

        OptionBuilder.isRequired(false);
        option = OptionBuilder.create();
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(3);
        Option option = OptionBuilder.create();
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertEquals(1, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder.hasOptionalArgs(2);
        Option option = OptionBuilder.create();
        assertEquals(2, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        Option option = OptionBuilder.create();
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        Option option = OptionBuilder.create();
        assertEquals("description", option.getDescription());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateWithNullLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithCharOpt() {
        OptionBuilder.withLongOpt("longopt");
        Option option = OptionBuilder.create('o');
        assertEquals('o', option.getOpt());
    }

    @Test
    public void testCreateWithStringOpt() {
        OptionBuilder.withLongOpt("longopt");
        Option option = OptionBuilder.create("opt");
        assertEquals("opt", option.getOpt());
    }
}