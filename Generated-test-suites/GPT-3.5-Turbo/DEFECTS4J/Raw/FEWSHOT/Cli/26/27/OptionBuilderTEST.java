package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("newLongopt");
        assertEquals("newLongopt", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.numberOfArgs);

        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("name");
        assertEquals("name", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertEquals(true, OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        assertEquals(true, OptionBuilder.required);

        OptionBuilder.isRequired(false);
        assertEquals(false, OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(true, OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertEquals(true, OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.numberOfArgs);
        assertEquals(true, OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("newDescription");
        assertEquals("newDescription", OptionBuilder.description);
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('o');
        assertEquals("o", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertEquals(null, option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals(null, option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(null, option.getType());
        assertEquals((char) 0, option.getValueSeparator());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("newLongopt");
        Option option = OptionBuilder.create();
        assertEquals(null, option.getOpt());
        assertEquals("newLongopt", option.getLongOpt());
        assertEquals(null, option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals(null, option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(null, option.getType());
        assertEquals((char) 0, option.getValueSeparator());
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("newOpt");
        assertEquals("newOpt", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertEquals(null, option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals(null, option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(null, option.getType());
        assertEquals((char) 0, option.getValueSeparator());
    }
}