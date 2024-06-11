package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();
        assertEquals(null, OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(0, OptionBuilder.valuesep);
        assertEquals(OptionBuilder.instance, OptionBuilder.instance);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.reset();
        OptionBuilder instance1 = OptionBuilder.withLongOpt("opt");
        assertEquals("opt", OptionBuilder.longopt);
        assertEquals(OptionBuilder.instance, OptionBuilder.instance);

        OptionBuilder.reset();
        OptionBuilder instance2 = OptionBuilder.withLongOpt("anotherOpt");
        assertEquals("anotherOpt", OptionBuilder.longopt);
        assertNotSame(instance1, OptionBuilder.instance);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.reset();
        OptionBuilder instance1 = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(OptionBuilder.instance, OptionBuilder.instance);

        OptionBuilder.reset();
        OptionBuilder instance2 = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNotSame(instance1, OptionBuilder.instance);
    }

    @Test
    public void testHasArgWithName() {
        OptionBuilder.reset();
        OptionBuilder instance1 = OptionBuilder.hasArg().withArgName("arg");
        assertEquals("arg", OptionBuilder.argName);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(OptionBuilder.instance, OptionBuilder.instance);

        OptionBuilder.reset();
        OptionBuilder instance2 = OptionBuilder.hasArg(false).withArgName("anotherArg");
        assertEquals("anotherArg", OptionBuilder.argName);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNotSame(instance1, OptionBuilder.instance);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.reset();
        OptionBuilder instance1 = OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
        assertEquals(OptionBuilder.instance, OptionBuilder.instance);

        OptionBuilder.reset();
        OptionBuilder instance2 = OptionBuilder.hasArg().isRequired();
        assertTrue(OptionBuilder.required);
        assertNotSame(instance1, OptionBuilder.instance);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.reset();
        OptionBuilder instance1 = OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(OptionBuilder.instance, OptionBuilder.instance);

        OptionBuilder.reset();
        OptionBuilder instance2 = OptionBuilder.hasOptionalArg(false);
        assertFalse(OptionBuilder.optionalArg);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNotSame(instance1, OptionBuilder.instance);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.reset();
        OptionBuilder instance1 = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertEquals(OptionBuilder.instance, OptionBuilder.instance);

        OptionBuilder.reset();
        OptionBuilder instance2 = OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
        assertNotSame(instance1, OptionBuilder.instance);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.reset();
        OptionBuilder instance1 = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertEquals(OptionBuilder.instance, OptionBuilder.instance);

        OptionBuilder.reset();
        OptionBuilder instance2 = OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.numberOfArgs);
        assertNotSame(instance1, OptionBuilder.instance);
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.reset();
        OptionBuilder instance1 = OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, OptionBuilder.numberOfArgs);
        assertEquals(OptionBuilder.instance, OptionBuilder.instance);

        OptionBuilder.reset();
        OptionBuilder instance2 = OptionBuilder.hasOptionalArgs().hasOptionalArgs(2);
        assertEquals(5, OptionBuilder.numberOfArgs);
        assertNotSame(instance1, OptionBuilder.instance);
    }

    @Test
    public void testWithType() {
        OptionBuilder.reset();
        OptionBuilder instance1 = OptionBuilder.withType(String.class);
        assertEquals(String.class, OptionBuilder.type);
        assertEquals(OptionBuilder.instance, OptionBuilder.instance);

        OptionBuilder.reset();
        OptionBuilder instance2 = OptionBuilder.withType("String");
        assertEquals(String.class, OptionBuilder.type);
        assertNotSame(instance1, OptionBuilder.instance);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.reset();
        OptionBuilder instance1 = OptionBuilder.withDescription("Description for Option 1");
        assertEquals("Description for Option 1", OptionBuilder.description);
        assertEquals(OptionBuilder.instance, OptionBuilder.instance);

        OptionBuilder.reset();
        OptionBuilder instance2 = OptionBuilder.withDescription("Description for Option 2");
        assertEquals("Description for Option 2", OptionBuilder.description);
        assertNotSame(instance1, OptionBuilder.instance);
    }

    @Test
    public void testCreate() {
        OptionBuilder.reset();
        OptionBuilder instance = OptionBuilder.withLongOpt("opt")
                .isRequired()
                .hasArg()
                .withArgName("arg");
        Option option = OptionBuilder.create("opt");

        assertEquals("opt", option.getLongOpt());
        assertEquals("Description for Option 1", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(true, option.hasArg());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());

        OptionBuilder.reset();
        instance = OptionBuilder.withLongOpt("opt")
                .isRequired(false)
                .hasArg(3)
                .withArgName("args");
        option = OptionBuilder.create("opt");

        assertEquals("opt", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(3, option.getArgs().length);
        assertEquals("args", option.getArgName());

        OptionBuilder.reset();
        instance = OptionBuilder.withLongOpt("opt")
                .isRequired(false)
                .hasOptionalArgs(2);
        option = OptionBuilder.create("opt");

        assertEquals("opt", option.getLongOpt());
        assertEquals("", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertTrue(option.hasOptionalArgs());
        assertEquals(2, option.getValues().size());
        assertNull(option.getArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.create();
    }
}